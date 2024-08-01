#include <fstream>
#include <sstream>
#include <DataExtractor.hxx>

FILE *fpLogFile;
FILE *fpOutFile;

static void printUsage();
static map<string, string> getArguments( int argc, char** argv );

Logger *logger = Logger::getLogger("ItemIdExtractor");

int main(int argc, char **argv) {
    int iFail = ITK_ok;
    string syslogName = "";
    string outFile;
    string item_type;
    string dataset_types;

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

    argItr = args.find("item_type");
    if (argItr != args.end()) {
        item_type = argItr->second;
    }

    argItr = args.find("dataset_types");
    if (argItr != args.end()) {
        dataset_types = argItr->second;
    }

    if (outFile.empty() || ( item_type.empty() && dataset_types.empty() ) ) {
        printUsage();
        return ITK_ok;
    }

    vector<string> datasetTypes;
    string buffer1 = dataset_types;
    string delimiter = ",";
    size_t pos1 = 0;
    std::string token1;
    while ((pos1 = buffer1.find(delimiter)) != std::string::npos) {
        token1 = buffer1.substr(0, pos1);
        datasetTypes.push_back(token1);
        buffer1.erase(0, pos1 + delimiter.length());
    }
    datasetTypes.push_back(buffer1);


    //
    time_t startClock;
    time_t endClock;
    time(&startClock);
    //

    string logFile = outFile + ".log";  
    fpOutFile = fopen(outFile.c_str(), "w");
    fpLogFile = fopen(logFile.c_str(), "w");

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
   
    if (item_type.empty() == false)
    {
        vector<string> items;
        ITK_TRY (getItemsOfType (item_type, items));
        for(int inx = 0; inx<items.size(); inx++){
            fprintf(fpOutFile, "%s\n", items[inx].c_str());
        }
    }else{
        vector<string> revisions;
        vector<string> inactiverevisions;
        ITK_TRY(getRevisionsWithDatasetTypes(datasetTypes, revisions, inactiverevisions));
        for(int inx = 0; inx<revisions.size(); inx++){
            fprintf(fpOutFile, "%s\n", revisions[inx].c_str());
        }
        for(int inx = 0; inx<inactiverevisions.size(); inx++){
            fprintf(fpLogFile, "%s\n", inactiverevisions[inx].c_str());
        }
    }
 
    ITK_exit_module(TRUE);

    fclose(fpLogFile);
    fclose(fpOutFile);
    //
    time(&endClock);
    double minutes = difftime(endClock, startClock) / 60;
    cout << "[" << minutes << "] minutes - Total time" << endl;
    //

    return iFail;
}

/**
 * Prints the usage of this standalone itk program
 *
 */
static void printUsage() {
    cout << endl;
    cout << "Usage: ItemIdExtractor" << endl;
    cout << "[-u=<username>] [-p=<password> or -pf=<password file>] [-g=<Group>] "<< endl;
    cout << "[-item_type = Item Type for which Item Ids to be extracted (Example: PN,DCN) or "<< endl;
    cout << " -dataset_types = Dataset Types to extract associated item revisions (Example: ProPrt,ProAsm) ] "<< endl;
    cout << "-outfile = Output file name"<< endl;
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
