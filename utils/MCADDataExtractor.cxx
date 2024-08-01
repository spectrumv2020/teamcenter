#include <fstream>
#include <sstream>
#include <set>
#include <DataExtractor.hxx>

FILE *fpLogFile;
FILE *fpFileInfo;
FILE *fpMetaData;

static void printUsage();
static map<string, string> getArguments( int argc, char** argv );
void updateItemData(map<tag_t, map<tag_t,TCDatasetData_t> > revDatasets, 
                    map<tag_t, map<tag_t,TCFileData_t> > revFiles,
                    map<tag_t, map<string,string> > revMetaData,
                    map<string, map<tag_t,TCItemData_t> > &items );
void updatedDatasetData(map<tag_t, map<tag_t,TCFileData_t> > revFiles,
                    map<tag_t, map<string,string> > revMetaData,
                    map<tag_t, map<tag_t,TCDatasetData_t> > &datasets );
void printItemData(map<string, map<tag_t, TCItemData_t> > items );
string trim(const string& str);
vector<string> readItemIds(const string& inputFile);
set<string> readItemRevs(const string& inputFile, vector<string> &keys);
void writeToFile(map<string, map<tag_t, TCItemData_t> > items );

Logger *logger = Logger::getLogger("MCADDataExtractor");

int main(int argc, char **argv) {
    int iFail = ITK_ok;
    string syslogName = "";
    string outFile;
    string item_types;
    string dataset_types;
    string inputItemFile;
    string inputRevFile;
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

    argItr = args.find("item_types");
    if (argItr != args.end()) {
        item_types = argItr->second;
    }
    argItr = args.find("inputItemFile");
    if (argItr != args.end()) {
        inputItemFile = argItr->second;
    }
    argItr = args.find("inputRevFile");
    if (argItr != args.end()) {
        inputRevFile = argItr->second;
    }
       
    argItr = args.find("dataset_types");
    if (argItr != args.end()) {
        dataset_types = argItr->second;
    }    

    if (outFile.empty() || ( item_types.empty() && inputItemFile.empty() && inputRevFile.empty() ) || dataset_types.empty()) {
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

    vector<string> itemTypes;
    vector<string> datasetTypes;

    std::string delimiter = ",";
    
    string buffer = item_types;
    size_t pos = 0;
    std::string token;
    while ((pos = buffer.find(delimiter)) != std::string::npos) {
        token = buffer.substr(0, pos);
        itemTypes.push_back(token);
        buffer.erase(0, pos + delimiter.length());
    }
    itemTypes.push_back(buffer);

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
    printf("Processing Item Types :");
    for(int inx=0;inx<itemTypes.size();inx++){
        printf("%s",itemTypes[inx].c_str());
        if(inx != itemTypes.size()-1){
            printf(",");
        }
    }
    printf("\n");

    printf("Processing Dataset Types :");
    for(int inx=0;inx<datasetTypes.size();inx++){
        printf("%s",datasetTypes[inx].c_str());
        if(inx != datasetTypes.size()-1){
            printf(",");
        }
    }
    printf("\n");

    set<string> itemidSet;
    vector<string> keys;
    vector<string> itemIds;
    if(inputItemFile.empty()){
        printf("Processing Input File : %s\n", inputRevFile.c_str());
        itemidSet = readItemRevs(inputRevFile, keys);
        set<string>::const_iterator itr;
        for(itr = itemidSet.begin(); itr != itemidSet.end(); ++itr){
            itemIds.push_back(*itr);
        }
    }else if(inputRevFile.empty()){
        printf("Processing Input File : %s\n", inputItemFile.c_str());
        itemIds = readItemIds(inputItemFile);
    }

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

    map<string, map<tag_t,TCItemData_t> > items;
    map<tag_t, map<tag_t,TCDatasetData_t> > itemDatasets;
    map<tag_t, map<tag_t,TCDatasetData_t> > revDatasets;
    map<tag_t, map<tag_t,TCFileData_t> > revFiles;
    map<tag_t, map<string,string> > revMetaData;

    if (inputItemFile.empty() && inputRevFile.empty()){
        ITK_TRY (extractRevisions (itemTypes, items));
    }else if(inputItemFile.empty()){
        if(itemIds.size() == 0){
            cout << "No Item Revisions to process " << endl;
            return ITK_ok;
        }
        ITK_TRY (extractItems (itemIds, keys, items));
    }else if(inputRevFile.empty()){
         if(itemIds.size() == 0){
            cout << "No Item to process " << endl;
            return ITK_ok;
        }
        ITK_TRY (extractItems (itemIds, items));
    }
    
    vector<tag_t> itemTags = getItemTags(items);
    vector<tag_t> revTags = getItemRevTags(items);
    cout << "No. of Items = " << itemTags.size() << endl;
    cout << "No. of Revisions = " << revTags.size() << endl;
    /*ITK_TRY (extractDatasets (itemTags, datasetTypes, itemDatasets));
    vector<tag_t> itemDatasetTags = getDatasetTags(itemDatasets);
    ITK_TRY (extractFiles (itemDatasetTags, itemFiles));
    updateItemData(itemDatasets, items);*/
    if ( allDatasets ){
        ITK_TRY (extractAllDatasets (revTags, revDatasets));
    }else{
        ITK_TRY (extractDatasets (revTags, datasetTypes, revDatasets));
    }
    vector<tag_t> revDatasetTags = getDatasetTags(revDatasets);
    ITK_TRY (extractFiles (revDatasetTags, revFiles));
    //ITK_TRY (extractMetaData (revDatasetTags, revMetaData));
    updatedDatasetData(revFiles,revMetaData, revDatasets);
    updateItemData(revDatasets, revFiles, revMetaData, items);

    //printItemData(items);
    writeToFile(items);
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

vector<string> readItemIds(const string& inputFile){
    vector<string> itemIds;
    ifstream fileStream(inputFile.c_str(), ios::in );

    if (!fileStream){
        ostringstream oss;
        oss << "ERROR: " << endl;
        oss << "Could not open file: " << inputFile << endl << endl;
        clog << oss.str();

        return itemIds;
    }
    string buffer2;
    while (std::getline(fileStream, buffer2)){
        itemIds.push_back(trim(buffer2));
    }
    return itemIds;
}

set<string> readItemRevs(const string& inputFile, vector<string> &keys){
    set<string> itemIds;
    ifstream fileStream(inputFile.c_str(), ios::in );

    if (!fileStream){
        ostringstream oss;
        oss << "ERROR: " << endl;
        oss << "Could not open file: " << inputFile << endl << endl;
        clog << oss.str();

        return itemIds;
    }
    string buffer2;
    size_t pos1 = 0;
    while (std::getline(fileStream, buffer2)){
        string record = buffer2;
        vector<string> tokens;
        string delimiter = "|";
        string token;
        
        while ((pos1 = buffer2.find(delimiter)) != std::string::npos) {
            token = buffer2.substr(0, pos1);
            tokens.push_back(token);
            buffer2.erase(0, pos1 + delimiter.length());
        }
        tokens.push_back(buffer2);
        if(tokens.size() != 2){
            fprintf(fpLogFile, "Invalid Entery in input file %s\n", record.c_str() );
            continue;
        }
        itemIds.insert(tokens[0]);
        keys.push_back(tokens[0]+"#"+tokens[1]);
    }
    return itemIds;
}

string trim(const string& str){
    size_t first = str.find_first_not_of(' ');
    if (string::npos == first)
    {
        return str;
    }
    size_t last = str.find_last_not_of(' ');
    return str.substr(first, (last - first + 1));
}

void updatedDatasetData(map<tag_t, map<tag_t,TCFileData_t> > files,
                    map<tag_t, map<string,string> > metaData,
                    map<tag_t, map<tag_t,TCDatasetData_t> > &datasets ){
    logger->debug("START: updatedDatasetData");
    map<tag_t, map<tag_t,TCDatasetData_t> > ::iterator itr;
    for(itr = datasets.begin(); itr != datasets.end(); ++itr){
        tag_t revTag = itr->first;
        map<tag_t, TCDatasetData_t> datasetMap = itr->second;
        map<tag_t, TCDatasetData_t>::iterator itr1;
        for(itr1 = datasetMap.begin(); itr1 != datasetMap.end(); ++itr1){
            tag_t datasetTag = itr1->first;
            TCDatasetData_t datasetData = itr1->second;
            map<tag_t, TCFileData_t> fileMap = datasetData.fileData;
            map<tag_t, TCFileData_t>::iterator itr2;
            for(itr2 = fileMap.begin(); itr2 != fileMap.end(); ++itr2){
                // tag_t fileTag = itr2->first; - Not needed
                TCFileData_t fileData = itr2->second;
                map<tag_t, map<tag_t,TCFileData_t> >::iterator itr3;
                itr3 = files.find(datasetTag);
                if (itr3 != files.end()){
                    map<tag_t,TCFileData_t> fileMap = itr3->second;
                    datasetData.fileData = fileMap;
                }
            }           
            map<string,string> metaDataMap = datasetData.metaData;
            map<tag_t, map<string,string>>::iterator itr4;
            for(itr4 = metaData.begin(); itr4 != metaData.end(); ++itr4){
                map<tag_t, map<string,string> >::iterator itr5;
                itr5 = metaData.find(datasetTag);
                if (itr5 != metaData.end()){
                    map<string,string> metDataMap = itr5->second;
                    datasetData.metaData = metDataMap;
                }
            }
            datasetMap[datasetTag] = datasetData;
            datasets[revTag] = datasetMap;
        }
    }
    logger->debug("END: updatedDatasetData");
}

void updateItemData(map<tag_t, map<tag_t,TCDatasetData_t> > revDatasets, 
                    map<tag_t, map<tag_t,TCFileData_t> > revFiles,
                    map<tag_t, map<string,string> > revMetaData,
                    map<string, map<tag_t,TCItemData_t> > &items ){
    logger->debug("START: updateItemData");
    map<string, map<tag_t, TCItemData_t> > ::iterator itr;
    for(itr = items.begin(); itr != items.end(); ++itr){
        string type = itr->first;
        map<tag_t, TCItemData_t> itemMap = itr->second;
        map<tag_t, TCItemData_t>::iterator itr1;
        for(itr1 = itemMap.begin(); itr1 != itemMap.end(); ++itr1){
            tag_t itemTag = itr1->first;
            TCItemData_t itemData = itr1->second;
            map<tag_t, TCRevData_t> revMap = itemData.revData;
            map<tag_t, TCRevData_t>::iterator itr2;
            for(itr2 = revMap.begin(); itr2 != revMap.end(); ++itr2){
                tag_t revTag = itr2->first;
                TCRevData_t revData = itr2->second;
                map<tag_t, map<tag_t,TCDatasetData_t> >::iterator itr3;
                itr3 = revDatasets.find(revTag);
                if (itr3 != revDatasets.end()){
                    map<tag_t,TCDatasetData_t> datasetMap = itr3->second;
                    map<tag_t, TCDatasetData_t>::iterator itr4;
                    for(itr4 = datasetMap.begin(); itr4 != datasetMap.end(); ++itr4){
                        tag_t datasetTag = itr4->first;
                        TCDatasetData_t datasetData = itr4->second;
                        map<tag_t, map<tag_t,TCFileData_t> >::iterator itr5;
                        itr5 = revFiles.find(datasetTag);
                        if(itr5 != revFiles.end()){
                            map<tag_t, TCFileData_t> fileMap = itr5->second;
                            datasetData.fileData = fileMap;
                        }
                        map<tag_t, map<string,string> >::iterator itr6;
                        itr6 = revMetaData.find(datasetTag);
                        if(itr6 != revMetaData.end()){
                            map<string, string> metaData = itr6->second;
                            datasetData.metaData = metaData;
                        }
                        datasetMap[datasetTag] = datasetData;
                        revData.datasetData = datasetMap;
                        revMap[revTag] = revData;
                        itemData.revData = revMap;
                        itemMap[itemTag] = itemData;
                        items[type] = itemMap;
                    }
                }
            }
        }
    }
    logger->debug("END: updateItemData");
}

void printItemData(map<string, map<tag_t, TCItemData_t> > items ){
    map<string, map<tag_t, TCItemData_t> > ::iterator itr;
    for(itr = items.begin(); itr != items.end(); ++itr){
        //cout << "Type [" << itr->first << "]" << endl;
        map<tag_t, TCItemData_t> itemMap = itr->second;
        map<tag_t, TCItemData_t>::iterator itr1;
        for(itr1 = itemMap.begin(); itr1 != itemMap.end(); ++itr1){
            TCItemData_t itemData = itr1->second;
            cout << "[" << itemData.itemId << "]" << endl;
            map<tag_t, TCRevData_t> revMap = itemData.revData;
            map<tag_t, TCRevData_t>::iterator itr2;
            for(itr2 = revMap.begin(); itr2 != revMap.end(); ++itr2){
                TCRevData_t revData = itr2->second;
                map<tag_t,TCDatasetData_t> datasetMap = revData.datasetData;
                cout << "    [" << itemData.itemId << "/" << revData.revId << "]" << endl;
                map<tag_t,TCDatasetData_t>::iterator itr3;
                
                for (itr3 = datasetMap.begin(); itr3 != datasetMap.end(); ++itr3 ){
                    TCDatasetData_t datasetData = itr3->second;
                    cout << "        [" << datasetData.datasetName << "]:["  << datasetData.datasetType <<"][" << datasetData.relation << "]" << endl;
                    map<tag_t, TCFileData_t> fileMap = datasetData.fileData;
                    map<string, string> metaData = datasetData.metaData;
                    map<tag_t, TCFileData_t>::iterator itr4;
                    for(itr4 = fileMap.begin(); itr4 != fileMap.end(); ++itr4){
                        TCFileData_t fileData = itr4->second;
                        cout << "            [" << fileData.origFileName << "]:["  << fileData.fullPath <<"]" << endl;
                    }
                    map<string, string>::iterator itr5;
                    if(metaData.size()>0){
                         cout << "            ";
                    }
                    for(itr5 = metaData.begin(); itr5 != metaData.end(); ++itr5){
                        cout << "[" << itr5->first << "]:["  <<itr5->second <<"] ";
                    }
                    if(metaData.size()>0){
                        cout << endl;
                    }
                }
            }
        }
    }
}

void writeToFile(map<string, map<tag_t, TCItemData_t> > items ){
    map<string, map<tag_t, TCItemData_t> > ::iterator itr;
    fprintf(fpFileInfo, "Item ID||Revision ID||Item Name||Dataset Name||Dataset Type||Relation||File Name||Volume Name||Host Name||Path||OS File Name||Size||Ref Type||Owner||Creation Date||Last Modified User||Last Modified Date\n");
    fprintf(fpMetaData, "Current ID||Current Name||Current Revision||Current Description||Creation Date||Date Modified||Owner||Last Modifying User||Release Status||Type");
    fprintf(fpMetaData, "||Part Name||Part Description||Part Creation Date||Part Revision Creation Date||Part Date Modified||Part Revision Date Modified||Owner||Last Modifying User||Release Status||EASS||DCN\n");

    for(itr = items.begin(); itr != items.end(); ++itr){
        map<tag_t, TCItemData_t> itemMap = itr->second;
        map<tag_t, TCItemData_t>::iterator itr1;
        for(itr1 = itemMap.begin(); itr1 != itemMap.end(); ++itr1){
            TCItemData_t itemData = itr1->second;
            map<tag_t, TCRevData_t> revMap = itemData.revData;
            map<tag_t, TCRevData_t>::iterator itr2;
            for(itr2 = revMap.begin(); itr2 != revMap.end(); ++itr2){
                TCRevData_t revData = itr2->second;
                map<tag_t,TCDatasetData_t> datasetMap = revData.datasetData;
                if (datasetMap.size() == 0){
                    fprintf(fpFileInfo, "%s||%s||%s|| || || || || || || || || || || || || ||\n", 
                                            itemData.itemId.c_str(),
                                            revData.revId.c_str(),
                                            itemData.itemName.c_str());
                    fprintf(fpMetaData, "%s|| ||%s|| || || || || || || ", 
                                            itemData.itemId.c_str(),
                                            revData.revId.c_str());
                    fprintf(fpMetaData, "||%s||%s||%s||%s||%s||%s||%s||%s||%s||%s||%s\n", 
                                            itemData.itemName.c_str(),
                                            itemData.itemDesc.c_str(),
                                            itemData.creationDate.c_str(),
                                            revData.creationDate.c_str(),
                                            itemData.lastModDate.c_str(),
                                            revData.lastModDate.c_str(),
                                            revData.owningUser.c_str(),
                                            revData.lastModUser.c_str(),
                                            revData.releaseStatus.c_str(),
                                            revData.eass.c_str(),
                                            revData.dcn.c_str());                                            
                    continue;
                }
                map<tag_t,TCDatasetData_t>::iterator itr3;
                for (itr3 = datasetMap.begin(); itr3 != datasetMap.end(); ++itr3 ){
                    TCDatasetData_t datasetData = itr3->second;
                    map<tag_t, TCFileData_t> fileMap = datasetData.fileData;
                    map<string, string> metaData = datasetData.metaData;
                    map<tag_t, TCFileData_t>::iterator itr4;
                    if (fileMap.size() == 0){
                        fprintf(fpFileInfo, "%s||%s||%s||%s||%s||%s|| || || || || || || || || || || \n", 
                                        itemData.itemId.c_str(),
                                        revData.revId.c_str(),
                                        itemData.itemName.c_str(),
                                        datasetData.datasetName.c_str(),
                                        datasetData.datasetType.c_str(),
                                        datasetData.relation.c_str());
                        fprintf(fpMetaData, "%s||%s||%s||%s||%s||%s||%s||%s||%s||%s", 
                                        itemData.itemId.c_str(),
                                        datasetData.datasetName.c_str(),
                                        revData.revId.c_str(),
                                        datasetData.description.c_str(),
                                        datasetData.creationDate.c_str(),
                                        datasetData.lastModDate.c_str(),
                                        datasetData.owningUser.c_str(),
                                        datasetData.lastModUser.c_str(),
                                        datasetData.releaseStatus.c_str(),
                                        datasetData.datasetType.c_str());
                        fprintf(fpMetaData, "||%s||%s||%s||%s||%s||%s||%s||%s||%s||%s||%s\n", 
                                        itemData.itemName.c_str(),
                                        itemData.itemDesc.c_str(),
                                        itemData.creationDate.c_str(),
                                        revData.creationDate.c_str(),
                                        itemData.lastModDate.c_str(),
                                        revData.lastModDate.c_str(),
                                        revData.owningUser.c_str(),
                                        revData.lastModUser.c_str(),
                                        revData.releaseStatus.c_str(),
                                        revData.eass.c_str(),
                                        revData.dcn.c_str());   
                        continue;
                    }
                    for(itr4 = fileMap.begin(); itr4 != fileMap.end(); ++itr4){
                        TCFileData_t fileData = itr4->second;
                        string primarySecondary = getPrimarySecondary(datasetData.datasetType, fileData.origFileName);
                        fprintf(fpFileInfo, "%s||%s||%s||%s||%s||%s||%s||%s||%s||%s||%s||%ld||%s||%s||%s||%s||%s\n", 
                                   itemData.itemId.c_str(),
                                   revData.revId.c_str(),
                                   itemData.itemName.c_str(),
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
                        fprintf(fpMetaData, "%s||%s||%s||%s||%s||%s||%s||%s||%s||%s", 
                                    itemData.itemId.c_str(),
                                    datasetData.datasetName.c_str(),
                                    revData.revId.c_str(),
                                    datasetData.description.c_str(),
                                    datasetData.creationDate.c_str(),
                                    datasetData.lastModDate.c_str(),
                                    datasetData.owningUser.c_str(),
                                    datasetData.lastModUser.c_str(),
                                    datasetData.releaseStatus.c_str(),
                                    datasetData.datasetType.c_str());
                        fprintf(fpMetaData, "||%s||%s||%s||%s||%s||%s||%s||%s||%s||%s||%s\n", 
                                    itemData.itemName.c_str(),
                                    itemData.itemDesc.c_str(),
                                    itemData.creationDate.c_str(),
                                    revData.creationDate.c_str(),
                                    itemData.lastModDate.c_str(),
                                    revData.lastModDate.c_str(),
                                    revData.owningUser.c_str(),
                                    revData.lastModUser.c_str(),
                                    revData.releaseStatus.c_str(),
                                    revData.eass.c_str(),
                                    revData.dcn.c_str());   
                    }
                    /*map<string, string>::iterator itr5;
                    if(metaData.size()>0){
                         cout << "            ";
                    }
                    for(itr5 = metaData.begin(); itr5 != metaData.end(); ++itr5){
                        cout << "[" << itr5->first << "]:["  <<itr5->second <<"] ";
                    }
                    if(metaData.size()>0){
                        cout << endl;
                    }*/
                }
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
    cout << "Usage: MCADExtractor" << endl;
    cout << "[-u=<username>] [-p=<password> or -pf=<password file>] [-g=<Group>] "<< endl;
    cout << "[ -item_types = Item Types to be extracted (Example: PN,DCN) OR "<< endl;
    cout << "  -inputItemfile = Input file with list of Item Ids to be extracted "<< endl;
    cout << "  -inputRevfile = Input file with list of Item Id|Rev Id(s) to be extracted ] "<< endl;
    cout << "-dataset_types = Dataset Types to be extracted(ProPart,ProAsm)"<< endl;
    cout << "-outfile = Output files prefix"<< endl;
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
