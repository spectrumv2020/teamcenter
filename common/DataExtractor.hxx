#pragma once 

#include <stdio.h>
#include <time.h>

#include <string>
#include <iostream>
#include <vector>
#include <set>
#include <map>

#include <tcinit/tcinit.h>
#include <tc/emh.h>
#include <error.h>
#include <mld/logging/Logger.hxx>
#include <mld/journal/journal.h>
#include <pom/enq/enq.h>
#include <tccore/item.h>
#include <ict/ict_userservice.h>
#include <tc/folder.h>


using namespace std;
using namespace Teamcenter::Logging;

#define ITK_TRY( argument ) \
{ \
    int rc = ITK_ok; \
    logger->debug ( #argument ); \
    rc = argument; \
    char* buff = NULL;\
    if ( rc != ITK_ok ) \
    { \
        logger->error ( "Error in API" + argument ); \
        EMH_ask_error_text(rc,&buff); \
        TC_write_syslog (" Error [%s] in %s, line #%d\n",buff,  __FUNCTION__, __LINE__); \
        cout << "Error : " << buff << endl; \
        MEM_free (buff); \
        return rc; \
    } \
    MEM_free (buff);\
}

typedef struct TCFileData_s
{
    tag_t imanFile;
    string volumeName;
    string nodeName;
    string fileName;
    string sdPathName;
    string origFileName;
    string fullPath;
    unsigned long int fileSize;
    string owningUser;
    string creationDate;
    string lastModUser;
    string lastModDate;

} TCFileData_t;

typedef struct TCDatasetData_s
{
    tag_t dataset;
    string datasetName;
    string datasetType;
    string relation;
    string status;
    map<tag_t, TCFileData_t> fileData;
    map<string, string> metaData;
    string owningUser;
    string creationDate;
    string lastModUser;
    string lastModDate;
    string releaseStatus;
    string description;
} TCDatasetData_t;

typedef struct TCRevData_s
{
    tag_t revTag;
    string revId;
    string owningUser;
    string creationDate;
    string lastModUser;
    string lastModDate;
    string releaseStatus;
    string dcn;
    string eass;
    string revDesc;
    // Map of Dataset Tag and Dataset Data   
    map<tag_t, TCDatasetData_t> datasetData;    
} TCRevData_t;

typedef struct TCItemData_s
{
    tag_t itemTag;
    string itemId;
    string itemType;
    string itemName;
    string owningUser;
    string creationDate;
    string lastModUser;
    string lastModDate;
    string itemDesc;
    // Map of Dataset Tag and Dataset Data   
    map<tag_t, TCDatasetData_t> dsetDsetDataMap; 
    map<tag_t, TCRevData_t> revData;   
} TCItemData_t;

extern DLLAPI int extractRevisions(vector<string> itemTypes, map<string, map<tag_t,TCItemData_t> > &items);
extern DLLAPI vector<tag_t> getItemTags (map<string, map<tag_t,TCItemData_t> > typeItemMap);
extern DLLAPI  vector<tag_t> getItemRevTags (map<string, map<tag_t,TCItemData_t> > typeItemMap);
extern DLLAPI int extractDatasets(vector<tag_t> objectTags, vector<string> datasetTypes, 
                                    map<tag_t, map<tag_t, TCDatasetData_t> > &datasets);
extern DLLAPI vector<tag_t> getDatasetTags (map<tag_t, map<tag_t,TCDatasetData_t> > revDatasetMap);
extern DLLAPI int extractFiles(vector<tag_t> datasetTags, 
                        map<tag_t, map<tag_t, TCFileData_t> > &files);
extern DLLAPI int extractMetaData(vector<tag_t> datasetTags, 
                        map<tag_t, map<string, string> > &dsetMetaData);
extern DLLAPI int extractItems(vector<string> itemIds, map<string, map<tag_t,TCItemData_t> > &items);
extern DLLAPI int getItemsOfType(const string& type, vector<string>& items);
extern DLLAPI int extractItems(vector<string> itemIds, vector<string> keys, map<string, map<tag_t,TCItemData_t> > &items);
extern DLLAPI int getRevisionsWithDatasetTypes(vector<string> datasetTypes,  vector<string>& activeRevisions, 
                                               vector<string>& inactiveRevisions);
extern DLLAPI int extractAllDatasets(vector<tag_t> objectTags, map<tag_t, map<tag_t, TCDatasetData_t> > &datasets);
extern DLLAPI string getPrimarySecondary(const string &datasetType, const string &fileName);
extern DLLAPI int getObjectLocations( tag_t folder, string location, map<tag_t, set<string> > &objectLocations );
extern DLLAPI int findFolders (const string& folder, vector<tag_t> &folders);
extern DLLAPI int extractStandaloneDatasets(vector<string> datasetTypes, 
                                            map<string, map<tag_t, TCDatasetData_t> > &datasets);
