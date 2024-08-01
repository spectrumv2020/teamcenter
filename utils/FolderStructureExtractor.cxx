#include <fstream>
#include <sstream>
#include <set>
#include <DataExtractor.hxx>

FILE *fpOutputFile;

static void printUsage();
static map<string, string> getArguments( int argc, char** argv );

Logger *logger = Logger::getLogger("FolderStructureExtractor");

int main(int argc, char **argv) {
    int iFail = ITK_ok;
    string syslogName = "";
    string outFile;
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

    if (outFile.empty()) {
        printUsage();
        return ITK_ok;
    }

    //
    time_t startClock;
    time_t endClock;
    time(&startClock);
    //

    string logFile = outFile + ".log";  
    string outputFile = outFile + ".csv";
    fpOutputFile = fopen(outputFile.c_str(), "w");

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

    map<tag_t,set<string> >::iterator itr;
    for(itr = objectLocations.begin(); itr != objectLocations.end(); ++itr){
        tag_t objTag = itr->first;
        set<string> locations = itr->second;
        char *objString = NULL;
        char *objType = NULL;
        char *className = NULL;
        tag_t classTag = NULLTAG;
        ITK_TRY(WSOM_ask_object_id_string(objTag, &objString));
        ITK_TRY(WSOM_ask_object_type2(objTag, &objType));
        ITK_TRY(POM_class_of_instance(objTag, &classTag));
        ITK_TRY(POM_name_of_class(classTag, &className));
        if(strcmp(className,"Item") == 0 ||
           strcmp(className,"Document") == 0){
            char *itemId = NULL;
            ITK_TRY(ITEM_ask_id2(objTag, &itemId));
            fprintf(fpOutputFile, "%s| ", itemId);
            MEM_free(itemId);
        }else if(strcmp(className,"ItemRevision") == 0||
           strcmp(className,"DocumentRevision") == 0){
            char *itemId = NULL;
            char *revId = NULL;
            tag_t itemTag = NULLTAG;
            ITK_TRY(ITEM_ask_item_of_rev(objTag, &itemTag));
            ITK_TRY(ITEM_ask_id2(itemTag, &itemId));
            ITK_TRY(ITEM_ask_rev_id2(objTag, &revId));
            fprintf(fpOutputFile, "%s|%s", itemId,revId);
            MEM_free(itemId);
            MEM_free(revId);
        }else if(strcmp(className, "Dataset") == 0 ||
           strcmp(className, "Form") == 0){
            char *name = NULL;
            ITK_TRY(WSOM_ask_name2(objTag, &name));
            fprintf(fpOutputFile, "%s| ", name);
            MEM_free(name);
        }else{
            char *name = NULL;
            ITK_TRY(WSOM_ask_name2(objTag, &name));
            fprintf(fpOutputFile, "%s| ", name);
            MEM_free(name);
        }

        fprintf(fpOutputFile, "|%s|%s|", className,objType );
        set<string>::iterator itr;
        for(itr = locations.begin();itr != locations.end(); ++itr){
            fprintf(fpOutputFile,"%s",(*itr).c_str());
            if(*itr != *locations.rbegin()){
                fprintf(fpOutputFile, "%s", ",");
            } 
        }
        fprintf(fpOutputFile,"|\n");

        MEM_free(objString);
        MEM_free(objType);
        MEM_free(className);
    }
    ITK_exit_module(TRUE);
    fclose(fpOutputFile);
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
    cout << "Usage: MCADExtractor" << endl;
    cout << "[-u=<username>] [-p=<password> or -pf=<password file>] [-g=<Group>] "<< endl;
    cout << "[ -item_types = Item Types to be extracted (Example: PN,DCN) OR "<< endl;
    cout << "  -inputItemFile = Input file with list of Item Ids to be extracted "<< endl;
    cout << "  -inputRevFile = Input file with list of Item Id|Rev Id(s) to be extracted ] "<< endl;
    cout << "-dataset_types = Dataset Types to be extracted(ProPart,ProAsm)"<< endl;
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
