#include <fstream>
#include <sstream>
#include <set>
#include <DataExtractor.hxx>
#include <tccore/aom.h>
#include <tccore/aom_prop.h>
#include <fclasses/tc_date.h>

typedef struct TCLinkData_s
{
    tag_t objTag;
    string relationType;
    string creationDate;
    string itemId;
    string revId;
    string name;
    string description;
}TCLinkData_t;

typedef struct TCDCNData_s
{
    tag_t dcnTag;
    tag_t dcnRevTag;

    string dcnId;
    string dcnRevId;
    string dcnName;
    string dcnState; // DCN Phase
    string dcnStage; // DCN Stage
    string solnDescription;
    string tcDescription;
    string detailedDescription;
    map<tag_t, TCLinkData_t> problemParts;
    map<tag_t, TCLinkData_t> solutionParts;
    map<tag_t, TCLinkData_t> proptotypeParts;
    string releaseStatus;
    string owningUser;
    string creationDate;
    string lastModifiedUser;
    string lastModifiedDate;
    string engineer;
    string principalEngineer;
    string engManager;
    string relCoordinator;
    string dcnCoordinator;
    string dcnCreationDate;
    string dcnLastModDate;
} TCDCNData_t;

FILE *fpLogFile;
FILE *fpMetaDataFile;
FILE *fpLinksFile;

static void printUsage();
static map<string, string> getArguments( int argc, char** argv );
vector<string> readItemIds(const string& inputFile);
map<tag_t, TCDCNData_t> getDCNData (map<string, map<tag_t,TCItemData_t> > items);
static int updateDCNData(map<tag_t, TCDCNData_t> &dcnData);
int getLinks(tag_t dcnRevTag, string &relation, map<tag_t, TCLinkData_t> &objLinkMap);
int writeToFile(map<tag_t, TCDCNData_t> &dcnData);
string trim(const string& str);

Logger *logger = Logger::getLogger("DCNDataExtractor");
int main(int argc, char **argv) {
    int iFail = ITK_ok;
    string syslogName = "";
    string outFile;
    string inputItemFile;
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

    argItr = args.find("inputItemFile");
    if (argItr != args.end()) {
        inputItemFile = argItr->second;
    }

    if (outFile.empty() || inputItemFile.empty() ) {
        printUsage();
        return ITK_ok;
    }

    //
    time_t startClock;
    time_t endClock;
    time(&startClock);
    //
    string logFile = outFile + ".log";  
    string metaDataFile = outFile + "_metadata.csv";
    string linksFile = outFile + "_links.csv";
    fpMetaDataFile = fopen(metaDataFile.c_str(), "w");
    fpLinksFile = fopen(linksFile.c_str(), "w");
    fpLogFile = fopen(logFile.c_str(), "w");

    vector<string> itemIds;
    printf("Processing Input File : %s\n", inputItemFile.c_str());
    itemIds = readItemIds(inputItemFile);

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

    if(itemIds.size() == 0){
        cout << "No Item to process " << endl;
        return ITK_ok;
    }

    map<tag_t, TCDCNData_t> dcnData;

    ITK_TRY (extractItems (itemIds, items));
    dcnData = getDCNData(items);
   
    cout << "No. of Revisions = " << dcnData.size() << endl;

    ITK_TRY (updateDCNData (dcnData));

    writeToFile(dcnData);
    ITK_exit_module(TRUE);

    fclose(fpLogFile);
    fclose(fpMetaDataFile);
    fclose(fpLinksFile);
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

string trim(const string& str){
    size_t first = str.find_first_not_of(' ');
    if (string::npos == first)
    {
        return str;
    }
    size_t last = str.find_last_not_of(' ');
    return str.substr(first, (last - first + 1));
}

map<tag_t, TCDCNData_t> getDCNData (map<string, map<tag_t,TCItemData_t> > items){
    map<tag_t, TCDCNData_t> dcnData;
    map<string, map<tag_t, TCItemData_t> > ::iterator itr;
    for(itr = items.begin(); itr != items.end(); ++itr){
        string objType = itr->first;
        if(objType != "TR4_DCN")
            continue;
        map<tag_t, TCItemData_t> itemMap = itr->second;
        map<tag_t, TCItemData_t>::iterator itr1;
        for(itr1 = itemMap.begin(); itr1 != itemMap.end(); ++itr1){

            TCItemData_t itemData = itr1->second;
            tag_t itemTag = itemData.itemTag;
            string itemId = itemData.itemId;
            string itemName = itemData.itemName;
            map<tag_t, TCRevData_t> revMap = itemData.revData;
            map<tag_t, TCRevData_t>::iterator itr2;
            for(itr2 = revMap.begin(); itr2 != revMap.end(); ++itr2){
                tag_t revTag = itr2->first;
                TCRevData_t revData = itr2->second;
                string revId = revData.revId;
                TCDCNData_t dcnRev;
                dcnRev.dcnTag = itemTag;
                dcnRev.dcnRevTag = revTag;
                dcnRev.dcnId = itemId;
                dcnRev.dcnRevId = revId;
                dcnRev.dcnName = itemName;
                dcnData[revTag] = dcnRev;
            }
        }
    }
    return dcnData;
}

/**
 * Prints the usage of this standalone itk program
 *
 */
static void printUsage() {
    cout << endl;
    cout << "Usage: DCNDataExtractor" << endl;
    cout << "[-u=<username>] [-p=<password> or -pf=<password file>] [-g=<Group>] "<< endl;
    cout << "-inputItemFile = Input file with list of Item Ids to be extracted "<< endl;
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

int updateDCNData(map<tag_t, TCDCNData_t> &dcnData){
    int ifail = ITK_ok;
    map<tag_t, TCDCNData_t>::iterator itr;
    for(itr = dcnData.begin(); itr != dcnData.end(); ++itr){
        TCDCNData_t data = itr->second;
        tag_t dcnRevTag = data.dcnRevTag;
        tag_t dcnTag = data.dcnTag;
        char *dcnState = NULL;
        char *dcnStage = NULL;
        char *description = NULL;
        char *solutionDesc = NULL;
        char *releaseStatus = NULL;
        char *owningUser = NULL;
        char *lastModUser = NULL;
        char *engineer = NULL;
        char *principalEngineer = NULL;
        char *engManager = NULL;
        char *relCoordinator = NULL;
        char *dcnCoordinator = NULL;
        char *creationDate = NULL;
        char *lastModDate = NULL;

        int count = 0;
        tag_t *statuses = NULL;
        ITK_TRY(AOM_refresh(dcnRevTag, false));
        ITK_TRY(AOM_ask_value_string(dcnRevTag, "tr4_dcn_stage", &dcnStage ));
        data.dcnStage = string(dcnStage?dcnStage:"");
        ITK_TRY(AOM_ask_value_string(dcnRevTag, "tr4_design_phase", &dcnState ));
        data.dcnState = string(dcnState?dcnState:"");
        ITK_TRY(AOM_ask_value_string(dcnRevTag, "tr4_description", &description ));
        data.detailedDescription = string(description?description:"");
        ITK_TRY(AOM_ask_value_string(dcnRevTag, "tr4_solution_description", &solutionDesc ));
        data.solnDescription = string(solutionDesc?solutionDesc:"");
        ITK_TRY(AOM_ask_value_tags(dcnRevTag, "release_status_list", &count, &statuses));
        if(count>0){
            ITK_TRY(AOM_ask_value_string(statuses[0], "name", &releaseStatus));
        }
        data.releaseStatus = string(releaseStatus?releaseStatus:"");
        char *desc = NULL;
        ITK_TRY(AOM_ask_value_string(dcnRevTag, "object_desc", &desc ));
        data.tcDescription = string(desc?desc:"");

        date_t dateCreated, dateModified;
        tag_t lastModifier, owner;

        /* Creation Date */ 
        ITK_TRY(AOM_ask_value_date(dcnRevTag, "creation_date", &dateCreated));
        ITK_TRY(DATE_date_to_string(dateCreated, "%Y-%m-%d %H:%M:%S", &creationDate));
         /* Last Modified Date */
        ITK_TRY(AOM_ask_value_date(dcnRevTag, "last_mod_date", &dateModified));
        ITK_TRY(DATE_date_to_string(dateModified, "%Y-%m-%d %H:%M:%S", &lastModDate));

        ITK_TRY(AOM_ask_value_tag(dcnRevTag, "owning_user", &owner));
        ITK_TRY(AOM_ask_value_string(owner, "user_id", &owningUser));

        ITK_TRY(AOM_ask_value_tag(dcnRevTag, "last_mod_user", &lastModifier));
        ITK_TRY(AOM_ask_value_string(lastModifier, "user_id", &lastModUser));

        data.owningUser = string(owningUser);
        data.lastModifiedUser = string(lastModUser?lastModUser:"");
        data.creationDate = string(creationDate);
        data.lastModifiedDate = string(lastModDate);
        
        ITK_TRY(AOM_ask_value_string(dcnRevTag, "tr4_engineer_user_id", &engineer ));
        ITK_TRY(AOM_ask_value_string(dcnRevTag, "tr4_principal_eng_user_id", &principalEngineer ));
        ITK_TRY(AOM_ask_value_string(dcnRevTag, "tr4_rel_coordinator_user_id", &relCoordinator ));
        ITK_TRY(AOM_ask_value_string(dcnRevTag, "tr4_eng_manager_user_id", &engManager));
        ITK_TRY(AOM_ask_value_string(dcnRevTag, "tr4_dcn_coordinator_user_id", &dcnCoordinator));

        data.engineer = string(engineer?engineer:"");
        data.principalEngineer = string(principalEngineer?principalEngineer:"");
        data.engManager = string(engManager?engManager:"");
        data.relCoordinator = string(relCoordinator?relCoordinator:"");
        data.dcnCoordinator = string(dcnCoordinator?dcnCoordinator:"");

        map<tag_t, TCLinkData_t> prototypeMap;
        map<tag_t, TCLinkData_t> solutionMap;
        map<tag_t, TCLinkData_t> problemParts;

        getLinks(dcnRevTag, string("TR4_CMHasPrototypePart"),prototypeMap);
        getLinks(dcnRevTag, string("TR4_CMHasSolutionPart"), solutionMap);
        getLinks(dcnRevTag, string("TR4_CMHasProblemPart"),problemParts);
        data.problemParts = problemParts;
        data.solutionParts = solutionMap;
        data.proptotypeParts = prototypeMap;

        date_t dcnDateCreated = NULLDATE;
        char *dcnCreationDate = NULL;
        date_t dcnDateModified = NULLDATE;
        char *dcnLastModDate = NULL;
        ITK_TRY(AOM_ask_value_date(dcnTag, "creation_date", &dcnDateCreated));
        ITK_TRY(DATE_date_to_string(dcnDateCreated, "%Y-%m-%d %H:%M:%S", &dcnCreationDate));

        ITK_TRY(AOM_ask_value_date(dcnTag, "last_mod_date", &dcnDateModified));
        ITK_TRY(DATE_date_to_string(dcnDateModified, "%Y-%m-%d %H:%M:%S", &dcnLastModDate));
        data.dcnCreationDate = dcnCreationDate;
        data.dcnLastModDate = dcnLastModDate;

        dcnData[dcnRevTag] = data;
        MEM_free(dcnState);
        MEM_free(dcnStage);
        MEM_free(description);
        MEM_free(solutionDesc);
        MEM_free(releaseStatus);
        MEM_free(owningUser);
        MEM_free(lastModUser);
        MEM_free(engineer);
        MEM_free(principalEngineer);
        MEM_free(engManager);
        MEM_free(relCoordinator);
        MEM_free(dcnCoordinator);
        MEM_free(creationDate);
        MEM_free(lastModDate);
        MEM_free(dcnLastModDate);
        MEM_free(dcnCreationDate);
        MEM_free(desc);
    }
    return ifail;
}

int writeToFile(map<tag_t, TCDCNData_t> &dcnData){
    int ifail = ITK_ok;
    fprintf(fpMetaDataFile,"Item ID||Item Name||Item Creation Date||Item Last Modified Date||Revision ID||Description||Revision Creation Date||Revision Last Modified Date||Owning User||Last Modified User||Design State\n");
    fprintf(fpLinksFile,"Item ID||Revision ID||Solution Part||Solution Part Revision||DCN Creation Date||DCN Modified Date||DCN Description\n");
    map<tag_t, TCDCNData_t>::iterator itr;
    for(itr = dcnData.begin(); itr != dcnData.end(); ++itr){
        TCDCNData_t data = itr->second;
        
        /*
        cout << "DCN ID = [" << data.dcnId << "]" << endl;
        cout << "DCN Rev ID = [" << data.dcnRevId << "]" << endl;
        cout << "DCN Name = [" << data.dcnName << "]" << endl;
        cout << "DCN State = [" << data.dcnState << "]" << endl;
        cout << "DCN Stage = [" << data.dcnStage << "]" << endl;
        cout << "DCN desc = [" << data.tcDescription << "]" << endl;
        cout << "DCN releaseStatus = [" << data.releaseStatus << "]" << endl;
        cout << "DCN owningUser = [" << data.owningUser << "]" << endl;
        cout << "DCN creationDate = [" << data.creationDate << "]" << endl;
        cout << "DCN lastModifiedUser = [" << data.lastModifiedUser << "]" << endl;
        cout << "DCN engineer = [" << data.engineer << "]" << endl;
        cout << "DCN principalEngineer = [" << data.principalEngineer << "]" << endl;
        cout << "DCN engManager = [" << data.engManager << "]" << endl;
        cout << "DCN relCoordinator = [" << data.relCoordinator << "]" << endl;
        cout << "DCN dcnCoordinator = [" << data.dcnCoordinator << "]" << endl;
        */

        /* fprintf(fpMetaDataFile,"Item ID||Item Name||Item Creation Date||Item Last Modified Date||Revision ID||Description|| \
                            Revision Creation Date||Revision Last Modified Date||Owning User||Last Modified User||Design State\n");*/
        fprintf(fpMetaDataFile,"%s||%s||%s||%s||%s||%s||%s||%s||%s||%s||%s\n", 
                                data.dcnId.c_str(),
                                data.dcnName.c_str(),
                                data.dcnCreationDate.c_str(),
                                data.dcnLastModDate.c_str(),
                                data.dcnRevId.c_str(),
                                data.tcDescription.c_str(),
                                data.creationDate.c_str(),
                                data.lastModifiedDate.c_str(),
                                data.owningUser.c_str(),
                                data.lastModifiedUser.c_str(),
                                data.dcnState.c_str());

        map<tag_t, TCLinkData_t> problemParts = data.problemParts;
        map<tag_t, TCLinkData_t> solutionParts = data.solutionParts;
        map<tag_t, TCLinkData_t> proptotypeParts = data.proptotypeParts;
        map<tag_t, TCLinkData_t>::iterator itr;
        /*cout << "== PROBLEM PARTS ==" << endl;
        for(itr = problemParts.begin(); itr != problemParts.end(); ++itr){
            TCLinkData_t link = itr->second;
            cout << "ItemId= [" << link.itemId << "]" << endl;
            cout << "revId= [" << link.revId << "]" << endl;
            cout << "relationType= [" << link.relationType << "]" << endl;
            cout << "name= [" << link.name << "]" << endl;
            cout << "description= [" << link.description << "]" << endl;
        }
        cout << "===================" << endl;
        cout << "== SOLUTION PARTS ==" << endl;*/
        for(itr = solutionParts.begin(); itr != solutionParts.end(); ++itr){
            TCLinkData_t link = itr->second;
            /*fprintf(fpLinksFile,"Item ID||Revision ID||Solution Part||Solution Part Revision||DCN Creation Date||DCN Modified Date||DCN Description\n");*/
            fprintf(fpLinksFile,"%s||%s||%s||%s||%s||%s||%s\n",
                                data.dcnId.c_str(),
                                data.dcnRevId.c_str(),
                                link.itemId.c_str(),
                                link.revId.c_str(),
                                data.dcnCreationDate.c_str(),
                                data.dcnLastModDate.c_str(),
                                data.tcDescription.c_str());
        }
        /*cout << "===================" << endl;
        cout << "== PROTOTYPE PARTS ==" << endl;
        for(itr = proptotypeParts.begin(); itr != proptotypeParts.end(); ++itr){
            TCLinkData_t link = itr->second;
            cout << "ItemId= [" << link.itemId << "]" << endl;
            cout << "revId= [" << link.revId << "]" << endl;
            cout << "relationType= [" << link.relationType << "]" << endl;
            cout << "name= [" << link.name << "]" << endl;
            cout << "description= [" << link.description << "]" << endl;
        }
        cout << "===================" << endl;*/
    }

    return ifail;
}

int getLinks(tag_t dcnRevTag, string &relation, map<tag_t, TCLinkData_t> &objLinkMap){
    int count = 0;
    tag_t *parts = NULL;
    int ifail = ITK_ok;

    ITK_TRY(AOM_ask_value_tags(dcnRevTag, relation.c_str(), &count, &parts));
    for(int jnx=0;jnx<count;jnx++){
        TCLinkData_t link;
        char *objType = NULL;
        char *objString = NULL;
        ITK_TRY(WSOM_ask_object_type2(parts[jnx], &objType));
        ITK_TRY(WSOM_ask_object_id_string(parts[jnx], &objString));
        if(strcmp(objType, "TR4_PN Revision") != 0){

            fprintf(fpLogFile, "%s - Not a Revision [Object Type : %s] in [%s]\n", objString, objType, relation.c_str());
            continue;
        }
        char *itemId = NULL;
        char *revId = NULL;
        char *name = NULL;
        char *description = NULL;
        tag_t itemTag = NULLTAG;
        ITK_TRY(ITEM_ask_item_of_rev(parts[jnx], &itemTag));
        ITK_TRY(ITEM_ask_id2(itemTag, &itemId));
        ITK_TRY(ITEM_ask_rev_id2(parts[jnx], &revId));
        ITK_TRY(WSOM_ask_name2(parts[jnx], &name));
        ITK_TRY(AOM_ask_value_string(parts[jnx], "object_desc", &description));
        
        link.objTag = parts[jnx];
        link.relationType = relation;
        link.itemId = string(itemId);
        link.revId = string(revId);
        link.name = string(name?name:"");
        link.description = string(description?description:"");
        objLinkMap[parts[jnx]] = link;

        MEM_free(objType);
        MEM_free(objString);
        MEM_free(itemId);
        MEM_free(revId);
        MEM_free(name);
        MEM_free(description);
    }
    MEM_free(parts);
    return ifail;
}