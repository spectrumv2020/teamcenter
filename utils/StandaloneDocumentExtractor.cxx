#include <fstream>
#include <sstream>
#include <set>
#include <DataExtractor.hxx>

FILE *fpLogFile;
FILE *fpFileInfo;
FILE *fpMetaData;

static void printUsage();
static map<string, string> getArguments( int argc, char** argv );
void updatedStandaloneDatasetData(map<tag_t, map<tag_t,TCFileData_t> > revFiles,
                    map<tag_t, map<string,string> > revMetaData,
                    map<string, map<tag_t,TCDatasetData_t> > &datasets );
void printItemData(map<string, map<tag_t, TCItemData_t> > items );
vector<tag_t> getStandaloneDatasetTags (map<string, 
                               map<tag_t,TCDatasetData_t> > datasetMap);
vector<string> readItemIds(const string& inputFile);
set<string> readItemRevs(const string& inputFile, vector<string> &keys);
void writeToFile(map<string, map<tag_t, TCDatasetData_t> > datasets, 
                                 map<tag_t,set<string> > objectLocations );
string getObjectLocations(tag_t object, map<tag_t, set<string> > objectLocations);

Logger *logger = Logger::getLogger("StandaloneDocumentExtractor");

int main(int argc, char **argv) {
    int iFail = ITK_ok;
    string syslogName = "";
    string outFile;
    string dataset_types;
    bool allDatasets = false;
    map<string,string> args = getArguments(argc, argv);
    map<string,string>::const_iterator argItr;

    argItr = args.find("h");
    if (argItr != args.end()) {
        cout << "Generating help ..." << endl;
        printUsage();
        return ITK_ok;
    }
    
    // Output filename to write extracted data
    argItr = args.find("outfile");
    if (argItr != args.end()) {
        outFile = argItr->second;
    }
      
    argItr = args.find("dataset_types");
    if (argItr != args.end()) {
        dataset_types = argItr->second;
    }    

    if (outFile.empty() || dataset_types.empty()) {
        printUsage();
        return ITK_ok;
    }

    //
    time_t startClock;
    time_t endClock;
    time(&startClock);
    //

    string logFile = outFile + ".log";  
    string fileInfoFile = outFile + "_fileinfo.csv";
    string metaDataFile = outFile + "_metaData.csv";
    fpFileInfo = fopen(fileInfoFile.c_str(), "w");
    fpMetaData = fopen(metaDataFile.c_str(), "w");
    fpLogFile = fopen(logFile.c_str(), "w");

    vector<string> datasetTypes;

    std::string delimiter = ",";

    string buffer1 = dataset_types;
    size_t pos1 = 0;
    std::string token1;
    while ((pos1 = buffer1.find(delimiter)) != std::string::npos) {
        token1 = buffer1.substr(0, pos1);
        datasetTypes.push_back(token1);
        buffer1.erase(0, pos1 + delimiter.length());
    }
    datasetTypes.push_back(buffer1);

    if (datasetTypes.size() == 1 && datasetTypes[0] == "all"){
        allDatasets = true;
    }

    printf("Processing Dataset Types :");
    for(int inx=0;inx<datasetTypes.size();inx++){
        printf("%s",datasetTypes[inx].c_str());
        if(inx != datasetTypes.size()-1){
            printf(",");
        }
    }
    printf("\n");

    ITK_init_from_cpp(argc, argv);
    ITK_initialize_text_services(0);
    ITK_auto_login( );
	ITK_set_bypass(true);

    char* syslog = NULL;
    syslog = ERROR_ask_system_log();
    if (syslog == NULL)
    {
        cout << "No Syslog found during this session" << endl;
        return ITK_ok;
    } else {
        syslogName = syslog;
        cout << "Syslog Name :  "<< syslogName <<endl;
    }

    string folderName = "Engineering";
    vector<tag_t> folders;
    map<tag_t,set<string> > objectLocations;
    ITK_TRY (findFolders(folderName, folders));

    for (int inx = 0;inx<folders.size();inx++){
        getObjectLocations(folders[inx], folderName, objectLocations);
    }

    map<string, map<tag_t,TCDatasetData_t> > standaloneDatasets;
    map<tag_t, map<tag_t,TCFileData_t> > stdaloneFiles;
    map<tag_t, map<string,string> > metaData;

    ITK_TRY (extractStandaloneDatasets (datasetTypes, standaloneDatasets));
    cout << "No. of Dataset Types " << standaloneDatasets.size() << endl;
    map<string, map<tag_t,TCDatasetData_t> >::iterator itr1;
    for(itr1 = standaloneDatasets.begin(); itr1 != standaloneDatasets.end(); ++itr1){
        map<tag_t,TCDatasetData_t> datasets = itr1->second;
        cout << "[" << itr1->first << "] = [" << datasets.size() << "]" << endl;
    }
    vector<tag_t> datasetTags = getStandaloneDatasetTags(standaloneDatasets);
    ITK_TRY (extractFiles (datasetTags, stdaloneFiles));
    cout << "No. of Files " << stdaloneFiles.size() << endl;
    updatedStandaloneDatasetData(stdaloneFiles,metaData, standaloneDatasets);

    //printItemData(items);
    writeToFile(standaloneDatasets,objectLocations);
    ITK_exit_module(TRUE);

    fclose(fpLogFile);
    fclose(fpFileInfo);
    fclose(fpMetaData);
    //
    time(&endClock);
    double minutes = difftime(endClock, startClock) / 60;
    cout << "[" << minutes << "] minutes - Total time" << endl;
    //

    return iFail;
}

vector<tag_t> getStandaloneDatasetTags (map<string, map<tag_t,TCDatasetData_t> > datasetMap){
    vector<tag_t> datastTags;
    map<string, map<tag_t, TCDatasetData_t> > ::iterator itr;
    for(itr = datasetMap.begin(); itr != datasetMap.end(); ++itr){
        map<tag_t, TCDatasetData_t> datasetMap = itr->second;
        map<tag_t, TCDatasetData_t>::iterator itr1;
        for(itr1 = datasetMap.begin(); itr1 != datasetMap.end(); ++itr1){
            datastTags.push_back(itr1->first);
        }
    }
    return datastTags;
}

void updatedStandaloneDatasetData(map<tag_t, map<tag_t,TCFileData_t> > files,
                    map<tag_t, map<string,string> > metaData,
                    map<string, map<tag_t,TCDatasetData_t> > &datasets ){
    logger->debug("START: updatedStandaloneDatasetData");
    map<string, map<tag_t,TCDatasetData_t> > ::iterator itr;
    for(itr = datasets.begin(); itr != datasets.end(); ++itr){
        string type = itr->first;
        map<tag_t, TCDatasetData_t> datasetMap = itr->second;
        map<tag_t, TCDatasetData_t>::iterator itr1;
        for(itr1 = datasetMap.begin(); itr1 != datasetMap.end(); ++itr1){
            tag_t datasetTag = itr1->first;
            TCDatasetData_t datasetData = itr1->second;
            map<tag_t, map<tag_t, TCFileData_t> >::iterator itr2; 
            itr2 = files.find(datasetTag);
            if (itr2 != files.end()){
                map<tag_t,TCFileData_t> fileMap = itr2->second;
                datasetData.fileData = fileMap;
                datasetMap[datasetTag] = datasetData;
                datasets[type] = datasetMap;
            }
        }
    }
    logger->debug("END: updatedStandaloneDatasetData");
}

string getObjectLocations(tag_t object, map<tag_t, set<string> > objectLocations){
    string locations;
    map<tag_t, set<string>>::iterator itr = objectLocations.find(object);
    if( itr != objectLocations.end()){
        set<string> objLocations = itr->second;
        set<string>::iterator itr;
        for(itr = objLocations.begin();itr != objLocations.end(); ++itr){
            locations = locations+(*itr);
            if(*itr != *objLocations.rbegin()){
                locations = locations+",";
            } 
        }
    }
    return locations;
}

void writeToFile(map<string, map<tag_t, TCDatasetData_t> > datasets, 
                                                    map<tag_t,set<string> > objectLocations){
    map<string, map<tag_t, TCDatasetData_t> > ::iterator itr;
    fprintf(fpFileInfo, "Item ID||Revision ID||Dataset Name||Dataset Type||Relation||File Name||Volume Name||Host Name||Path||OS File Name||Size||Ref Type||Owner||Creation Date||Last Modified User||Last Modified Date\n");
    fprintf(fpMetaData, "Current ID||Current Name||Current Revision||Current Description||Creation Date||Date Modified||Owner||Last Modifying User||Release Status||Type||Document Location\n");        
    for(itr = datasets.begin(); itr != datasets.end(); ++itr){
        map<tag_t,TCDatasetData_t> datasetMap = itr->second;
        map<tag_t,TCDatasetData_t>::iterator itr3;
        for (itr3 = datasetMap.begin(); itr3 != datasetMap.end(); ++itr3 ){
            TCDatasetData_t datasetData = itr3->second;
            string datasetLocations = getObjectLocations(datasetData.dataset, objectLocations);
            map<tag_t, TCFileData_t> fileMap = datasetData.fileData;
            map<string, string> metaData = datasetData.metaData;
            map<tag_t, TCFileData_t>::iterator itr4;
            if (fileMap.size() == 0){
                fprintf(fpFileInfo, "%s||%s||%s||%s||%s|| || || || || || || || || || || \n", 
                                "",
                                "",
                                datasetData.datasetName.c_str(),
                                datasetData.datasetType.c_str(),
                                datasetData.relation.c_str());
                fprintf(fpMetaData, "%s||%s||%s||%s||%s||%s||%s||%s||%s||%s||%s\n", 
                                "",
                                datasetData.datasetName.c_str(),
                                "",
                                datasetData.description.c_str(),
                                datasetData.creationDate.c_str(),
                                datasetData.lastModDate.c_str(),
                                datasetData.owningUser.c_str(),
                                datasetData.lastModUser.c_str(),
                                datasetData.releaseStatus.c_str(),
                                datasetData.datasetType.c_str(),
                                datasetLocations.c_str());
                continue;
            }
            for(itr4 = fileMap.begin(); itr4 != fileMap.end(); ++itr4){
                TCFileData_t fileData = itr4->second;
                string primarySecondary = getPrimarySecondary(datasetData.datasetType, fileData.origFileName);
                fprintf(fpFileInfo, "%s||%s||%s||%s||%s||%s||%s||%s||%s||%s||%ld||%s||%s||%s||%s||%s\n", 
                            "",
                            "",
                            datasetData.datasetName.c_str(),
                            datasetData.datasetType.c_str(),
                            datasetData.relation.c_str(),
                            fileData.origFileName.c_str(),
                            fileData.volumeName.c_str(),
                            fileData.nodeName.c_str(),
                            fileData.fullPath.c_str(), 
                            fileData.fileName.c_str(),
                            fileData.fileSize,
                            primarySecondary.c_str(),
                            fileData.owningUser.c_str(),
                            fileData.creationDate.c_str(),
                            fileData.lastModUser.c_str(),
                            fileData.lastModDate.c_str());
                fprintf(fpMetaData, "%s||%s||%s||%s||%s||%s||%s||%s||%s||%s||%s\n", 
                            "",
                            datasetData.datasetName.c_str(),
                            "",
                            datasetData.description.c_str(),
                            datasetData.creationDate.c_str(),
                            datasetData.lastModDate.c_str(),
                            datasetData.owningUser.c_str(),
                            datasetData.lastModUser.c_str(),
                            datasetData.releaseStatus.c_str(),
                            datasetData.datasetType.c_str(),
                            datasetLocations.c_str());
            }
        }
    }
}

/**
 * Prints the usage of this standalone itk program
 *
 */
static void printUsage() {
    cout << endl;
    cout << "Usage: StandaloneDocumentExtractor" << endl;
    cout << "[-u=<username>] [-p=<password> or -pf=<password file>] [-g=<Group>] "<< endl;
    cout << "-dataset_types = Dataset Types to be extracted(MSWord,Text)"<< endl;
    cout << "-outfile = Output file prefix"<< endl;
    cout << endl;
}



/**
 * Prints the usage of this standalone itk program
 * @param  int                No. of arguments  ( I )
 * @param  char **            List of arguments ( I )
 * @return map<string,string> Arguments as key-value pair
 */
static map<string,string> getArguments( int argc, char** argv ) {

    map<string,string> arguments;

    for (int i=1; i<argc; i++) {
        string arg = argv[i];
        //ignore argument if doesn't start with "-"
        if (arg.substr(0, 1).compare("-") != 0) {
            cerr << "Ignoring incorrect argument [" << arg << "]" << endl;
            continue;
        }
        //
        int eq = arg.find("=");
        string key = "";
        string value = "";
        //if (eq == -1)
        if (eq == string::npos) {
            key = arg.substr(1, arg.size()-1);
        } else {
            key = arg.substr(1, eq-1);
            value = arg.substr(eq+1, arg.size()-1);
        }
        arguments[key] = value;
    }
    //
    return arguments;
}
