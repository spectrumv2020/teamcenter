#include <DataExtractor.hxx>
#include <sa/tcfile.h>
#include <tccore/aom.h>
#include <tccore/aom_prop.h>
#include <pom/pom/pom.h>
#include <fclasses/tc_date.h>

static Logger *logger = Logger::getLogger("DataExtractor");
vector<tag_t> getItemTags (map<string, map<tag_t,TCItemData_t> > items){
    vector<tag_t> itemTags;
    map<string, map<tag_t, TCItemData_t> > ::iterator itr;
    for(itr = items.begin(); itr != items.end(); ++itr){
        map<tag_t, TCItemData_t> itemMap = itr->second;
        map<tag_t, TCItemData_t>::iterator itr1;
        for(itr1 = itemMap.begin(); itr1 != itemMap.end(); ++itr1){
            TCItemData_t itemData = itr1->second;
            itemTags.push_back(itemData.itemTag);
        }
    }
    return itemTags;
}

vector<tag_t> getItemRevTags (map<string, map<tag_t,TCItemData_t> > items){
    vector<tag_t> itemRevTags;
    map<string, map<tag_t, TCItemData_t> > ::iterator itr;
    for(itr = items.begin(); itr != items.end(); ++itr){
        map<tag_t, TCItemData_t> itemMap = itr->second;
        map<tag_t, TCItemData_t>::iterator itr1;
        for(itr1 = itemMap.begin(); itr1 != itemMap.end(); ++itr1){
            TCItemData_t itemData = itr1->second;
            map<tag_t, TCRevData_t> revMap = itemData.revData;
            map<tag_t, TCRevData_t>::iterator itr2;
            for(itr2 = revMap.begin(); itr2 != revMap.end(); ++itr2){
                itemRevTags.push_back(itr2->first);
            }
        }
    }
    return itemRevTags;
}

/**
  * This method extracts All Item revisions of given type 
  * 
    @param [in]  itemTypes          Item Types
    @param [out] items             map<Type, map<Item tag,Item data>>
*/
int extractRevisions(vector<string> itemTypes, map<string, map<tag_t,TCItemData_t> > &items){
    int iFail = ITK_ok;
    JOURNAL_routine_start ("extractRevisions");
	JOURNAL_routine_call();
    int rows = 0;
    int cols = 0;
    void ***objects = NULL;

    char **types = (char **)MEM_alloc(sizeof(char *)*(itemTypes.size()));
    for(int inx = 0; inx<itemTypes.size(); inx++){
        types[inx] = (char *)MEM_alloc(sizeof(char)*(strlen(itemTypes[inx].c_str())+1));
        strcpy(types[inx], itemTypes[inx].c_str());
    }
    
    static tag_t initialized = NULLTAG;
    // Query for all Item Types that need to be processed

    const char *enqId = "RBGetItemRevision";
    if (initialized == NULLTAG) {
        const char *itemRevAttrList[] = { "puid" , "item_revision_id" };
        const char *itemAttrList[] = { "puid" , "item_id" };
        const char *wsoAttrList[] = { "object_type", "object_name" };

        POM_cache_for_session(&initialized);
		initialized = 1;

        ITK_TRY ( POM_enquiry_create ( enqId ) );

        ITK_TRY ( POM_enquiry_add_select_attrs ( enqId,"Item", 2, itemAttrList ) );

        ITK_TRY ( POM_enquiry_add_select_attrs ( enqId, "ItemRevision", 2, 
                                                                itemRevAttrList ) );

        ITK_TRY ( POM_enquiry_add_select_attrs ( enqId, "WorkspaceObject", 2, 
                                                                wsoAttrList ) );

        ITK_TRY ( POM_enquiry_set_join_expr( enqId, "IdExpr1", "Item", "puid",
                                    POM_enquiry_equal,"WorkspaceObject", "puid" ) );
        
        ITK_TRY ( POM_enquiry_set_join_expr( enqId, "IdExpr2", "Item", "puid",
                                    POM_enquiry_equal,"ItemRevision", "items_tag" ) );

        int activeSeq = 1;
        ITK_TRY(
                POM_enquiry_set_int_value(enqId, "IdExpr3",1 , &activeSeq,
                                                            POM_enquiry_bind_value) );
        ITK_TRY(
                POM_enquiry_set_attr_expr(enqId, "IdExpr4", "ItemRevision", "active_seq", 
                                                            POM_enquiry_equal,"IdExpr3") );

        ITK_TRY ( POM_enquiry_set_string_value ( enqId, "IdExpr5", itemTypes.size(), (const char **)types, 
                                                            POM_enquiry_bind_value ) );

        ITK_TRY ( POM_enquiry_set_attr_expr ( enqId, "IdExpr6", "WorkspaceObject",
                                        "object_type", POM_enquiry_in, "IdExpr5" ) );

        ITK_TRY ( POM_enquiry_set_expr ( enqId, "IdExpr7", "IdExpr1", POM_enquiry_and,
                                                                        "IdExpr2" ) );

        ITK_TRY ( POM_enquiry_set_expr ( enqId, "IdExpr8", "IdExpr6", POM_enquiry_and,
                                                                        "IdExpr4" ) );

        ITK_TRY ( POM_enquiry_set_expr ( enqId, "IdExpr9", "IdExpr8", POM_enquiry_and,
                                                                        "IdExpr7" ) );
        ITK_TRY ( POM_enquiry_set_where_expr ( enqId,"IdExpr9") );
    }else{
         ITK_TRY ( POM_enquiry_set_string_value ( enqId, "IdExpr5", itemTypes.size(), 
                                         (const char **)types, POM_enquiry_bind_value ) );
    }
 
    ITK_TRY ( POM_enquiry_execute ( enqId, &rows, &cols, &objects) );

    for ( int inx = 0; inx<rows; inx++ ){
        tag_t itemTag = *static_cast < tag_t * > (objects[inx][0]);
        char *itemId = static_cast < char * > (objects[inx][1]);
        tag_t revTag = *static_cast < tag_t * > (objects[inx][2]);
        char *revId = static_cast < char * > (objects[inx][3]);
        char *type = static_cast < char * > (objects[inx][4]);
        char *name = static_cast < char * > (objects[inx][5]);
        string iType = string(type);
        map<string, map<tag_t, TCItemData_t> >::iterator itr;
        itr = items.find(iType);
        map<tag_t, TCItemData_t> itemMap;
        TCItemData_t itemData;

        /**
           Populate Item information
        */
        date_t dateCreated = NULLDATE;
        date_t dateModified = NULLDATE;
        tag_t lastModifier = NULLTAG;
        tag_t owner = NULLTAG;
        char *creationDate = NULL;
        char *lastModDate = NULL;
        char *owningUser = NULL;
        char *lastModUser = NULL;

        ITK_TRY(AOM_ask_value_date(itemTag, "creation_date", &dateCreated));
        ITK_TRY(DATE_date_to_string(dateCreated, "%Y-%m-%d %H:%M:%S", &creationDate));
         /* Last Modified Date */
        ITK_TRY(AOM_ask_value_date(itemTag, "last_mod_date", &dateModified));
        ITK_TRY(DATE_date_to_string(dateModified, "%Y-%m-%d %H:%M:%S", &lastModDate));

        ITK_TRY(AOM_ask_value_tag(itemTag, "owning_user", &owner));
        ITK_TRY(AOM_ask_value_string(owner, "user_id", &owningUser));

        ITK_TRY(AOM_ask_value_tag(itemTag, "last_mod_user", &lastModifier));
        ITK_TRY(AOM_ask_value_string(lastModifier, "user_id", &lastModUser));

        /**
           Populate Item Revision information
        */
        date_t revDateCreated = NULLDATE;
        date_t revDateModified = NULLDATE;
        tag_t revLastModifier = NULLTAG;
        tag_t revOwner = NULLTAG;
        char *revCreationDate = NULL;
        char *revLastModDate = NULL;
        char *revOwningUser = NULL;
        char *revLastModUser = NULL;

        ITK_TRY(AOM_ask_value_date(revTag, "creation_date", &revDateCreated));
        ITK_TRY(DATE_date_to_string(revDateCreated, "%Y-%m-%d %H:%M:%S", &revCreationDate));
         /* Last Modified Date */
        ITK_TRY(AOM_ask_value_date(revTag, "last_mod_date", &revDateModified));
        ITK_TRY(DATE_date_to_string(revDateModified, "%Y-%m-%d %H:%M:%S", &revLastModDate));

        ITK_TRY(AOM_ask_value_tag(revTag, "owning_user", &revOwner));
        ITK_TRY(AOM_ask_value_string(revOwner, "user_id", &revOwningUser));

        ITK_TRY(AOM_ask_value_tag(revTag, "last_mod_user", &revLastModifier));
        ITK_TRY(AOM_ask_value_string(revLastModifier, "user_id", &revLastModUser));

        /* EASS and DCN */
        char *eass = NULL;
        char *dcn = NULL;
        if(iType == "TR4_PN"){
            ITK_TRY(AOM_ask_value_string(revTag, "tr4_EASS", &eass));
            ITK_TRY(AOM_ask_value_string(revTag, "tr4_DCN", &dcn));
        }

        int count = 0;
        tag_t *statuses = NULL;
        char *releaseStatus = NULL;

        ITK_TRY(AOM_ask_value_tags(revTag, "release_status_list", &count, &statuses));
        if(count>0){
            ITK_TRY(AOM_ask_value_string(statuses[0], "name", &releaseStatus));
        }

        char *description = NULL;
        char *revDescription = NULL;
        ITK_TRY(AOM_ask_value_string(itemTag, "object_desc", &description));
		if(description != NULL){
            for(int inx = 0;inx<strlen(description); inx++){
                if(description[inx] == '\n'){
                    description[inx] = ' ';
                }
            }
        }
        ITK_TRY(AOM_ask_value_string(revTag, "object_desc", &revDescription));
		if(revDescription != NULL){
            for(int inx = 0;inx<strlen(revDescription); inx++){
                if(revDescription[inx] == '\n'){
                    revDescription[inx] = ' ';
                }
            }
        }
        if (itr != items.end()){
            itemMap = itr->second;
            map<tag_t, TCItemData_t>::iterator itr1 = itemMap.find(itemTag);   
            if (itr1 != itemMap.end()){
                itemData = itr1->second;
            }else{
                itemData.itemTag = itemTag;
                itemData.itemId = itemId;
                itemData.itemType = iType;
                itemData.itemName = name;
                itemData.owningUser = string(owningUser?owningUser:"");
                itemData.lastModUser = string(lastModUser?lastModUser:"");
                itemData.creationDate = string(creationDate?creationDate:"");
                itemData.lastModDate = string(lastModDate?lastModDate:"");
                itemData.itemDesc = string(description?description:"");      
            }
        }else{
            itemData.itemTag = itemTag;
            itemData.itemId = itemId;
            itemData.itemType = iType;
            itemData.itemName = name;
            itemData.owningUser = string(owningUser?owningUser:"");
            itemData.lastModUser = string(lastModUser?lastModUser:"");
            itemData.creationDate = string(creationDate?creationDate:"");
            itemData.lastModDate = string(lastModDate?lastModDate:"");
            itemData.itemDesc = string(description?description:"");               
        }  
        TCRevData_t revData;
        revData.revTag = revTag;
        revData.revId = revId;
        revData.owningUser = string(revOwningUser?revOwningUser:"");
        revData.lastModUser = string(revLastModUser?revLastModUser:"");
        revData.creationDate = string(revCreationDate?revCreationDate:"");
        revData.lastModDate = string(revLastModDate?revLastModDate:"");
        revData.eass = string(eass?eass:"");
        revData.dcn = string(dcn?dcn:"");
        revData.releaseStatus = string(releaseStatus?releaseStatus:"");
        revData.revDesc = string(revDescription?revDescription:"");
        itemData.revData[revTag] = revData;
        itemMap[itemTag] = itemData;
        items[iType] = itemMap;
        MEM_free(creationDate);
        MEM_free(lastModDate);
        MEM_free(owningUser);
        MEM_free(lastModUser);
        MEM_free(revCreationDate);
        MEM_free(revLastModDate);
        MEM_free(revOwningUser);
        MEM_free(revLastModUser);
        MEM_free(dcn);
        MEM_free(eass);
        MEM_free(statuses);
        MEM_free(releaseStatus);
        MEM_free(description);
        MEM_free(revDescription);       
    }

    MEM_free (objects);
    MEM_free (types);
	JOURNAL_routine_end();
    return iFail;
}

/**
  * This method extracts datasets attached to all input objects
  * These objects could be anything - Items or Item Revisions 
  * 
    @param [in]  objectsTags          Vector of tags
    @param [in]  datasetTypes         Dataset Types to be extracted
    @param [out] datasets             map<input object, map<dataset tag,dataset data>>
*/
int extractDatasets(vector<tag_t> objectTags, vector<string> datasetTypes,
                    map<tag_t, map<tag_t, TCDatasetData_t> > &datasets){
    int iFail = ITK_ok;
    JOURNAL_routine_start ("extractDatasets");
	JOURNAL_routine_call();
    int rows = 0;
    int cols = 0;
    void ***objects = NULL;

    if (objectTags.size() <= 0){
        return iFail;
    }
    tag_t *objTags = NULL;
    objTags = (tag_t *)MEM_alloc(sizeof(tag_t)*(objectTags.size()));
    //Collect all Object tags in an array
    for (int inx =0;inx<objectTags.size(); inx++){
        objTags[inx] = objectTags[inx];
    }

    char **types = (char **)MEM_alloc(sizeof(char *)*(datasetTypes.size()));
    for(int inx = 0; inx<datasetTypes.size(); inx++){
        types[inx] = (char *)MEM_alloc(sizeof(char)*(strlen(datasetTypes[inx].c_str())+1));
        strcpy(types[inx], datasetTypes[inx].c_str());
    }

    static tag_t initialized = NULLTAG;
    // Query for all Item Types that need to be processed

    const char *enqId = "RBGetDatasets";
    if (initialized == NULLTAG) {
        const char *imanRelationAttrList[] = { "puid", "primary_object", "secondary_object" };
        const char *imanTypeAttrList[] = { "type_name" };
        const char *datasetAttrList[] = { "puid" };
        const char *wsoAttrList[] = {"object_type", "object_name"};

        POM_cache_for_session(&initialized);
		initialized = 1;

        ITK_TRY ( POM_enquiry_create ( enqId ));
        ITK_TRY ( POM_enquiry_add_select_attrs ( enqId,"ImanRelation", 3, imanRelationAttrList ));
        ITK_TRY ( POM_enquiry_add_select_attrs ( enqId, "ImanType", 1, imanTypeAttrList ));
        ITK_TRY ( POM_enquiry_add_select_attrs ( enqId, "Dataset", 1, datasetAttrList )); 
        ITK_TRY ( POM_enquiry_add_select_attrs ( enqId, "WorkspaceObject", 2, wsoAttrList ) );

        ITK_TRY ( POM_enquiry_set_join_expr( enqId, "IdExpr1", "ImanRelation", "secondary_object",
                                    POM_enquiry_equal,"Dataset", "puid" ) );
        
        ITK_TRY( POM_enquiry_set_tag_value( enqId, "objTags", objectTags.size(), objTags, 
                                                                   POM_enquiry_bind_value));
        
        ITK_TRY( POM_enquiry_set_attr_expr( enqId, "IdExpr2", "ImanRelation", "primary_object", 
                                                                POM_enquiry_in, "objTags"));

        ITK_TRY ( POM_enquiry_set_join_expr( enqId, "IdExpr3", "ImanRelation", "relation_type",
                                    POM_enquiry_equal,"ImanType", "puid" ) );
        
        ITK_TRY ( POM_enquiry_set_join_expr( enqId, "IdExpr4", "Dataset", "puid",
                                    POM_enquiry_equal,"WorkspaceObject", "puid" ) );

        ITK_TRY ( POM_enquiry_set_string_value ( enqId, "datasetTypes", datasetTypes.size(), 
                                                 (const char **)types, POM_enquiry_bind_value ) );

        ITK_TRY ( POM_enquiry_set_attr_expr ( enqId, "IdExpr5", "WorkspaceObject",
                                        "object_type", POM_enquiry_in, "datasetTypes" ) );

        ITK_TRY ( POM_enquiry_set_expr ( enqId, "IdExpr6", "IdExpr2", POM_enquiry_and,
                                                                        "IdExpr1" ) );

        ITK_TRY ( POM_enquiry_set_expr ( enqId, "IdExpr7", "IdExpr4", POM_enquiry_and,
                                                                        "IdExpr3" ) );

        ITK_TRY ( POM_enquiry_set_expr ( enqId, "IdExpr8", "IdExpr6", POM_enquiry_and,
                                                                        "IdExpr5" ) );

        ITK_TRY ( POM_enquiry_set_expr ( enqId, "IdExpr9", "IdExpr8", POM_enquiry_and,
                                                                        "IdExpr7" ) );
        ITK_TRY ( POM_enquiry_set_where_expr ( enqId,"IdExpr9") );
    }else{
        ITK_TRY( POM_enquiry_set_tag_value( enqId, "objTags", objectTags.size(), objTags, 
                                                                   POM_enquiry_bind_value));

        ITK_TRY ( POM_enquiry_set_string_value ( enqId, "datasetTypes", datasetTypes.size(), 
                                                 (const char **)types, POM_enquiry_bind_value ) );
    }
    ITK_TRY ( POM_enquiry_execute ( enqId, &rows, &cols, &objects) );

    for ( int inx = 0; inx<rows; inx++ ){
        tag_t relationTag = *static_cast < tag_t * > (objects[inx][0]);
        tag_t objTag = *static_cast < tag_t * > (objects[inx][1]);
        tag_t secondaryTag = *static_cast < tag_t * > (objects[inx][2]);
        char *relationName = static_cast < char * > (objects[inx][3]);
        tag_t datasetTag = *static_cast < tag_t * > (objects[inx][4]);
        char *datasetType = static_cast < char * > (objects[inx][5]);
        char *datasetName = static_cast < char * > (objects[inx][6]);

        date_t dateCreated = NULLDATE;
        date_t dateModified = NULLDATE;

        char *creationDate = NULL;
        char *lastModDate = NULL;
        char *owningUser = NULL;
        char *lastModUser = NULL;

        tag_t owner = NULLTAG;
        tag_t lastModifier = NULLTAG;
        tag_t *statuses = NULL;
        char *releaseStatus = NULL;
        int count = 0;
        char *description = NULL;

        ITK_TRY(AOM_refresh(datasetTag, false));
        /* Creation Date */        
        ITK_TRY(AOM_ask_value_date(datasetTag, "creation_date", &dateCreated));
        ITK_TRY(DATE_date_to_string(dateCreated, "%Y-%m-%d %H:%M:%S", &creationDate));
         /* Last Modified Date */
        ITK_TRY(AOM_ask_value_date(datasetTag, "last_mod_date", &dateModified));
        ITK_TRY(DATE_date_to_string(dateModified, "%Y-%m-%d %H:%M:%S", &lastModDate));

        ITK_TRY(AOM_ask_value_tag(datasetTag, "owning_user", &owner));
        ITK_TRY(AOM_ask_value_string(owner, "user_id", &owningUser));

        ITK_TRY(AOM_ask_value_tag(datasetTag, "last_mod_user", &lastModifier));
        ITK_TRY(AOM_ask_value_string(lastModifier, "user_id", &lastModUser));

        ITK_TRY(AOM_ask_value_tags(datasetTag, "release_status_list", &count, &statuses));
        if(count>0){
            ITK_TRY(AOM_ask_value_string(statuses[0], "name", &releaseStatus));
        }

        ITK_TRY(AOM_ask_value_string(datasetTag, "object_desc", &description));   
		if(description != NULL){
            for(int inx = 0;inx<strlen(description); inx++){
                if(description[inx] == '\n'){
                    description[inx] = ' ';
                }
            }
        }
        map<tag_t, map<tag_t, TCDatasetData_t> >::iterator itr;
        itr = datasets.find(objTag);
        map<tag_t, TCDatasetData_t> datasetMap;
        TCDatasetData_t datasetData;

        if (itr != datasets.end()){
            datasetMap = itr->second;
            map<tag_t, TCDatasetData_t>::iterator itr1 = datasetMap.find(datasetTag);   
            if (itr1 != datasetMap.end()){
                datasetData = itr1->second;
            }else{
                datasetData.dataset = datasetTag;
                datasetData.datasetName = datasetName;
                datasetData.datasetType = datasetType;
                datasetData.relation = relationName;
                datasetData.owningUser = string(owningUser);
                datasetData.lastModUser = string(lastModUser?lastModUser:"");
                datasetData.creationDate = string(creationDate);
                datasetData.lastModDate = string(lastModDate);
                datasetData.releaseStatus = string(releaseStatus?releaseStatus:"");
                datasetData.description = string(description?description:"");
            }
        }else{
            datasetData.dataset = datasetTag;
            datasetData.datasetName = datasetName;
            datasetData.datasetType = datasetType;
            datasetData.relation = relationName;
            datasetData.owningUser = string(owningUser);
            datasetData.lastModUser = string(lastModUser?lastModUser:"");
            datasetData.creationDate = string(creationDate);
            datasetData.lastModDate = string(lastModDate);
            datasetData.releaseStatus = string(releaseStatus?releaseStatus:"");
            datasetData.description = string(description?description:"");
        }  
        datasetMap[datasetTag] = datasetData;
        datasets[objTag] = datasetMap;
        MEM_free(lastModUser);
        MEM_free(owningUser);
        MEM_free(creationDate);
        MEM_free(lastModDate); 
        MEM_free(releaseStatus);
        MEM_free(statuses); 
        MEM_free(description);
    }

    MEM_free (objects);
    MEM_free (types);
	JOURNAL_routine_end();
    return iFail;
}

/**
  * Get Dataset Tags from input map
  * @param [in] revDatasetMap   map<revTag, map<datasetTag, Dataset Data> >
  * @returns vector of dataset tags
  */
vector<tag_t> getDatasetTags (map<tag_t, map<tag_t,TCDatasetData_t> > revDatasetMap){
    vector<tag_t> datastTags;
    map<tag_t, map<tag_t, TCDatasetData_t> > ::iterator itr;
    for(itr = revDatasetMap.begin(); itr != revDatasetMap.end(); ++itr){
        map<tag_t, TCDatasetData_t> datasetMap = itr->second;
        map<tag_t, TCDatasetData_t>::iterator itr1;
        for(itr1 = datasetMap.begin(); itr1 != datasetMap.end(); ++itr1){
            datastTags.push_back(itr1->first);
        }
    }
    return datastTags;
}

/**
  * This method extracts ImanFile(s) attached to all input datasets
  * 
    @param [in]  datasetTags       Vector of tags
    @param [out] files             map<input dataset, map<ImanFile tag,ImanFile data>>
*/
int extractFiles(vector<tag_t> datasetTags, 
                        map<tag_t, map<tag_t, TCFileData_t> > &files){
    int iFail = ITK_ok;
    JOURNAL_routine_start ("extractFiles");
	JOURNAL_routine_call();
    int rows = 0;
    int cols = 0;
    void ***objects = NULL;

    if (datasetTags.size() <= 0){
        return iFail;
    }
    tag_t *objTags = NULL;
    objTags = (tag_t *)MEM_alloc(sizeof(tag_t)*(datasetTags.size()));
    //Collect all dataset tags in an array
    for (int inx =0;inx<datasetTags.size(); inx++){
        objTags[inx] = datasetTags[inx];
    }

    static tag_t initialized = NULLTAG;
    // Query for all Item Types that need to be processed

    const char *enqId = "RBGetFiles";
    if (initialized == NULLTAG) {
        const char *datasetAttrList[] = { "puid" };
        const char *imanFileAttrList[] = {"puid", "sd_path_name", "file_name", "original_file_name"};
        const char *imanVolumeAttrList[] = {"puid", "volume_name", "node_name", "wnt_path_name", "unix_path_name"};

        POM_cache_for_session(&initialized);
		initialized = 1;

        ITK_TRY ( POM_enquiry_create ( enqId ));
        ITK_TRY ( POM_enquiry_add_select_attrs ( enqId, "Dataset", 1, datasetAttrList )); 
        ITK_TRY ( POM_enquiry_add_select_attrs ( enqId, "ImanFile", 4, imanFileAttrList ) );
        ITK_TRY ( POM_enquiry_add_select_attrs ( enqId, "ImanVolume", 5, imanVolumeAttrList ) );

        ITK_TRY( POM_enquiry_set_pseudo_calias( enqId, "Dataset", "ref_list", "ref_list"));

        ITK_TRY( POM_enquiry_set_join_expr( enqId, "IdExpr1","ref_list", "puid", POM_enquiry_equal,
                                          "Dataset", "puid"));

        ITK_TRY ( POM_enquiry_set_join_expr( enqId, "IdExpr2", "ref_list", "PVAL", POM_enquiry_equal,
                                                                 "ImanFile", "puid" ) );

        ITK_TRY ( POM_enquiry_set_join_expr( enqId, "IdExpr3", "ImanFile", "volume_tag", POM_enquiry_equal,
                                                                 "ImanVolume", "puid" ) );

        ITK_TRY( POM_enquiry_set_tag_value( enqId, "objTags", datasetTags.size(), objTags, 
                                                                   POM_enquiry_bind_value));
        
        ITK_TRY( POM_enquiry_set_attr_expr( enqId, "IdExpr4", "Dataset", "puid", 
                                                                POM_enquiry_in, "objTags"));

        ITK_TRY ( POM_enquiry_set_expr ( enqId, "IdExpr5", "IdExpr2", POM_enquiry_and,
                                                                        "IdExpr1" ) );

        ITK_TRY ( POM_enquiry_set_expr ( enqId, "IdExpr6", "IdExpr4", POM_enquiry_and,
                                                                        "IdExpr3" ) );

        ITK_TRY ( POM_enquiry_set_expr ( enqId, "IdExpr7", "IdExpr6", POM_enquiry_and,
                                                                        "IdExpr5" ) );

        ITK_TRY ( POM_enquiry_set_where_expr ( enqId,"IdExpr7") );
    }else{
        ITK_TRY( POM_enquiry_set_tag_value( enqId, "objTags", datasetTags.size(), objTags, 
                                                                   POM_enquiry_bind_value));

    }
    ITK_TRY ( POM_enquiry_execute ( enqId, &rows, &cols, &objects) );

    for ( int inx = 0; inx<rows; inx++ ){
        tag_t datasetTag = *static_cast < tag_t * > (objects[inx][0]);
        tag_t imanFileTag = *static_cast < tag_t * > (objects[inx][1]);
        char *sdPathName = static_cast < char * > (objects[inx][2]);
        char *fileName = static_cast < char * > (objects[inx][3]);
        char *origFileName = static_cast < char * > (objects[inx][4]);
        tag_t volumeTag = *static_cast < tag_t * > (objects[inx][5]);
        char *volumeName = static_cast < char * > (objects[inx][6]);
        char *nodeName = static_cast < char * > (objects[inx][7]);
        char *winPathName = static_cast < char * > (objects[inx][8]);
        char *unixPathName = static_cast < char * > (objects[inx][9]);
        
        map<tag_t, map<tag_t, TCFileData_t> >::iterator itr;
        itr = files.find(datasetTag);
        map<tag_t, TCFileData_t> fileMap;
        TCFileData_t fileData;

        date_t dateCreated = NULLDATE;
        date_t dateModified = NULLDATE;

        char *creationDate = NULL;
        char *lastModDate = NULL;
        char *owningUser = NULL;
        char *lastModUser = NULL;

        tag_t owner = NULLTAG;
        tag_t lastModifier = NULLTAG;
        string absPath ;
 
        ITK_TRY(AOM_refresh(imanFileTag, false));
        /* Creation Date */        
        ITK_TRY(AOM_ask_value_date(imanFileTag, "creation_date", &dateCreated));
        ITK_TRY(DATE_date_to_string(dateCreated, "%Y-%m-%d %H:%M:%S", &creationDate));
         /* Last Modified Date */
        ITK_TRY(AOM_ask_value_date(imanFileTag, "last_mod_date", &dateModified));
        ITK_TRY(DATE_date_to_string(dateModified, "%Y-%m-%d %H:%M:%S", &lastModDate));

        ITK_TRY(AOM_ask_value_tag(imanFileTag, "owning_user", &owner));
        ITK_TRY(AOM_ask_value_string(owner, "user_id", &owningUser));

        ITK_TRY(AOM_ask_value_tag(imanFileTag, "last_mod_user", &lastModifier));
        ITK_TRY(AOM_ask_value_string(lastModifier, "user_id", &lastModUser));
     
        if (itr != files.end()){
            fileMap = itr->second;
            map<tag_t, TCFileData_t>::iterator itr1 = fileMap.find(imanFileTag);   
            if (itr1 != fileMap.end()){
                fileData = itr1->second;
            }else{
                fileData.imanFile = imanFileTag;
                fileData.volumeName = volumeName;
                fileData.nodeName = nodeName;
                fileData.fileName = fileName;
                fileData.sdPathName = sdPathName;
                fileData.origFileName = origFileName;
                if (winPathName!=NULL){
                    fileData.fullPath =  string(winPathName) + "\\" + string(sdPathName);
                    absPath = fileData.fullPath + "\\" + fileName;
                }
                if (unixPathName!=NULL){
                    fileData.fullPath =  string(unixPathName) + "/" + string(sdPathName);
                    absPath = fileData.fullPath + "/" + fileName;
                }
                /* Get File Size */
                IMF_file_status stats;
                ITK_TRY( IMF_stat(absPath.c_str(), &stats) );
                fileData.fileSize = stats.size;
                fileData.owningUser = string(owningUser);
                fileData.lastModUser = string(lastModUser?lastModUser:"");
                fileData.creationDate = string(creationDate);
                fileData.lastModDate = string(lastModDate);
            }
        }else{
            fileData.imanFile = imanFileTag;
            fileData.volumeName = volumeName;
            fileData.nodeName = nodeName;
            fileData.fileName = fileName;
            fileData.sdPathName = sdPathName;
            fileData.origFileName = origFileName;
            if (winPathName!=NULL){
                fileData.fullPath =  string(winPathName) + "\\" + string(sdPathName) ;
                absPath = fileData.fullPath + "\\" + fileName;
            }
            if (unixPathName!=NULL){
                fileData.fullPath =  string(unixPathName) + "/" + string(sdPathName) ;
                absPath = fileData.fullPath + "/" + fileName;
            }
           
            /* Get File Size */
            IMF_file_status stats;
            ITK_TRY( IMF_stat(absPath.c_str(), &stats) );
            fileData.fileSize = stats.size;
            fileData.owningUser = string(owningUser);
            fileData.lastModUser = string(lastModUser?lastModUser:"");
            fileData.creationDate = string(creationDate);
            fileData.lastModDate = string(lastModDate);
        } 
         
        fileMap[imanFileTag] = fileData;
        files[datasetTag] = fileMap;
        MEM_free(lastModUser);
        MEM_free(owningUser);
        MEM_free(creationDate);
        MEM_free(lastModDate);   
    }

    MEM_free (objects);
	JOURNAL_routine_end();
    return iFail;
}

/**
  * This method extracts Creo MetaData attached to all input datasets
  * 
    @param [in]  datasetTags       Vector of tags
    @param [out] metadata          map<input dataset, map<property,value>>
*/
int extractMetaData(vector<tag_t> datasetTags, 
                        map<tag_t, map<string, string> > &dsetMetaData){
    int iFail = ITK_ok;
    JOURNAL_routine_start ("extractMetaData");
	JOURNAL_routine_call();
    int rows = 0;
    int cols = 0;
    void ***objects = NULL;

    if (datasetTags.size() <= 0){
        return iFail;
    }
    tag_t *objTags = NULL;
    objTags = (tag_t *)MEM_alloc(sizeof(tag_t)*(datasetTags.size()));
    //Collect all dataset tags in an array
    for (int inx =0;inx<datasetTags.size(); inx++){
        objTags[inx] = datasetTags[inx];
    }

    static tag_t initialized = NULLTAG;
    // Query for all Item Types that need to be processed

    const char *enqId = "RBGetMetaData";
    if (initialized == NULLTAG) {
        const char *datasetAttrList[] = { "puid" };
        const char *formAttrList[] = {"puid" };
        const char *dataFileClassAttrList[] = {"puid", "task_no", "material"};

        POM_cache_for_session(&initialized);
		initialized = 1;

        ITK_TRY ( POM_enquiry_create ( enqId ));
        ITK_TRY ( POM_enquiry_add_select_attrs ( enqId, "Dataset", 1, datasetAttrList )); 
        ITK_TRY ( POM_enquiry_add_select_attrs ( enqId, "Form", 1, formAttrList ) );
        ITK_TRY ( POM_enquiry_add_select_attrs ( enqId, "UGPartAttrStorage", 3, dataFileClassAttrList ) );

        ITK_TRY( POM_enquiry_set_pseudo_calias( enqId, "Dataset", "ref_list", "ref_list"));

        ITK_TRY( POM_enquiry_set_join_expr( enqId, "IdExpr1","ref_list", "puid", POM_enquiry_equal,
                                          "Dataset", "puid"));

        ITK_TRY ( POM_enquiry_set_join_expr( enqId, "IdExpr2", "ref_list", "PVAL", POM_enquiry_equal,
                                                                 "Form", "puid" ) );

        ITK_TRY ( POM_enquiry_set_join_expr( enqId, "IdExpr3", "Form", "data_file", POM_enquiry_equal,
                                                                 "UGPartAttrStorage", "puid" ) );

        ITK_TRY( POM_enquiry_set_tag_value( enqId, "objTags", datasetTags.size(), objTags, 
                                                                   POM_enquiry_bind_value));
        
        ITK_TRY( POM_enquiry_set_attr_expr( enqId, "IdExpr4", "Dataset", "puid", 
                                                                POM_enquiry_in, "objTags"));

        ITK_TRY ( POM_enquiry_set_expr ( enqId, "IdExpr5", "IdExpr2", POM_enquiry_and,
                                                                        "IdExpr1" ) );

        ITK_TRY ( POM_enquiry_set_expr ( enqId, "IdExpr6", "IdExpr4", POM_enquiry_and,
                                                                        "IdExpr3" ) );

        ITK_TRY ( POM_enquiry_set_expr ( enqId, "IdExpr7", "IdExpr6", POM_enquiry_and,
                                                                        "IdExpr5" ) );

        ITK_TRY ( POM_enquiry_set_where_expr ( enqId,"IdExpr7") );
    }else{
        ITK_TRY( POM_enquiry_set_tag_value( enqId, "objTags", datasetTags.size(), objTags, 
                                                                   POM_enquiry_bind_value));

    }
    ITK_TRY ( POM_enquiry_execute ( enqId, &rows, &cols, &objects) );

    map<string, string> metaData;
    for ( int inx = 0; inx<rows; inx++ ){
        tag_t datasetTag = *static_cast < tag_t * > (objects[inx][0]);
        tag_t formTag = *static_cast < tag_t * > (objects[inx][1]);
        tag_t classTag = *static_cast < tag_t * > (objects[inx][2]);
        char *taskNo = static_cast < char * > (objects[inx][3]);
        char *material = static_cast < char * > (objects[inx][4]);
        metaData["task_no"] = taskNo;
        metaData["material"] = material;
        dsetMetaData[datasetTag] = metaData; 
    }

    MEM_free (objects);
	JOURNAL_routine_end();
    return iFail;
}

/**
  * This method extracts All Item revisions of input Item Ids 
  * 
    @param [in]  itemIds           Item Ids
    @param [out] items             map<Type, map<Item tag,Item data>>
*/
int extractItems(vector<string> itemIds, map<string, map<tag_t,TCItemData_t> > &items){
    int iFail = ITK_ok;
    JOURNAL_routine_start ("extractItems");
	JOURNAL_routine_call();
    int rows = 0;
    int cols = 0;
    void ***objects = NULL;

    char **ids = (char **)MEM_alloc(sizeof(char *)*(itemIds.size()));
    for(int inx = 0; inx<itemIds.size(); inx++){
        ids[inx] = (char *)MEM_alloc(sizeof(char)*(strlen(itemIds[inx].c_str())+1));
        strcpy(ids[inx], itemIds[inx].c_str());
    }
    
    static tag_t initialized = NULLTAG;
    // Query for all Item Types that need to be processed

    const char *enqId = "RBGetItems";
    if (initialized == NULLTAG) {
        const char *itemRevAttrList[] = { "puid" , "item_revision_id" };
        const char *itemAttrList[] = { "puid" , "item_id" };
        const char *wsoAttrList[] = { "object_type", "object_name" };

        POM_cache_for_session(&initialized);
		initialized = 1;

        ITK_TRY ( POM_enquiry_create ( enqId ) );

        ITK_TRY ( POM_enquiry_add_select_attrs ( enqId,"Item", 2, itemAttrList ) );

        ITK_TRY ( POM_enquiry_add_select_attrs ( enqId, "ItemRevision", 2, 
                                                                itemRevAttrList ) );

        ITK_TRY ( POM_enquiry_add_select_attrs ( enqId, "WorkspaceObject", 2, 
                                                                wsoAttrList ) );

        ITK_TRY ( POM_enquiry_set_join_expr( enqId, "IdExpr1", "Item", "puid",
                                    POM_enquiry_equal,"WorkspaceObject", "puid" ) );
        
        ITK_TRY ( POM_enquiry_set_join_expr( enqId, "IdExpr2", "Item", "puid",
                                    POM_enquiry_equal,"ItemRevision", "items_tag" ) );

        int activeSeq = 1;
        ITK_TRY(
                POM_enquiry_set_int_value(enqId, "IdExpr3",1 , &activeSeq,
                                                            POM_enquiry_bind_value) );
        ITK_TRY(
                POM_enquiry_set_attr_expr(enqId, "IdExpr4", "ItemRevision", "active_seq", 
                                                            POM_enquiry_equal,"IdExpr3") );

        ITK_TRY ( POM_enquiry_set_string_value ( enqId, "IdExpr5", itemIds.size(), (const char **)ids, 
                                                            POM_enquiry_bind_value ) );

        ITK_TRY ( POM_enquiry_set_attr_expr ( enqId, "IdExpr6", "Item",
                                        "item_id", POM_enquiry_in, "IdExpr5" ) );

        ITK_TRY ( POM_enquiry_set_expr ( enqId, "IdExpr7", "IdExpr1", POM_enquiry_and,
                                                                        "IdExpr2" ) );

        ITK_TRY ( POM_enquiry_set_expr ( enqId, "IdExpr8", "IdExpr6", POM_enquiry_and,
                                                                        "IdExpr4" ) );

        ITK_TRY ( POM_enquiry_set_expr ( enqId, "IdExpr9", "IdExpr8", POM_enquiry_and,
                                                                        "IdExpr7" ) );
        ITK_TRY ( POM_enquiry_set_where_expr ( enqId,"IdExpr9") );
    }else{
         ITK_TRY ( POM_enquiry_set_string_value ( enqId, "IdExpr5", itemIds.size(), 
                                         (const char **)ids, POM_enquiry_bind_value ) );
    }
 
    ITK_TRY ( POM_enquiry_execute ( enqId, &rows, &cols, &objects) );

    for ( int inx = 0; inx<rows; inx++ ){
        tag_t itemTag = *static_cast < tag_t * > (objects[inx][0]);
        char *itemId = static_cast < char * > (objects[inx][1]);
        tag_t revTag = *static_cast < tag_t * > (objects[inx][2]);
        char *revId = static_cast < char * > (objects[inx][3]);
        char *type = static_cast < char * > (objects[inx][4]);
        char *name = static_cast < char * > (objects[inx][5]);
        string iType = string(type);
        map<string, map<tag_t, TCItemData_t> >::iterator itr;
        itr = items.find(iType);
        map<tag_t, TCItemData_t> itemMap;
        TCItemData_t itemData;
        
        /**
           Populate Item information
        */
        date_t dateCreated = NULLDATE;
        date_t dateModified = NULLDATE;
        tag_t lastModifier = NULLTAG;
        tag_t owner = NULLTAG;
        char *creationDate = NULL;
        char *lastModDate = NULL;
        char *owningUser = NULL;
        char *lastModUser = NULL;

        ITK_TRY(AOM_ask_value_date(itemTag, "creation_date", &dateCreated));
        ITK_TRY(DATE_date_to_string(dateCreated, "%Y-%m-%d %H:%M:%S", &creationDate));
         /* Last Modified Date */
        ITK_TRY(AOM_ask_value_date(itemTag, "last_mod_date", &dateModified));
        ITK_TRY(DATE_date_to_string(dateModified, "%Y-%m-%d %H:%M:%S", &lastModDate));

        ITK_TRY(AOM_ask_value_tag(itemTag, "owning_user", &owner));
        ITK_TRY(AOM_ask_value_string(owner, "user_id", &owningUser));

        ITK_TRY(AOM_ask_value_tag(itemTag, "last_mod_user", &lastModifier));
        ITK_TRY(AOM_ask_value_string(lastModifier, "user_id", &lastModUser));

        /**
           Populate Item Revision information
        */
        date_t revDateCreated = NULLDATE;
        date_t revDateModified = NULLDATE;
        tag_t revLastModifier = NULLTAG;
        tag_t revOwner = NULLTAG;
        char *revCreationDate = NULL;
        char *revLastModDate = NULL;
        char *revOwningUser = NULL;
        char *revLastModUser = NULL;

        ITK_TRY(AOM_ask_value_date(revTag, "creation_date", &revDateCreated));
        ITK_TRY(DATE_date_to_string(revDateCreated, "%Y-%m-%d %H:%M:%S", &revCreationDate));
         /* Last Modified Date */
        ITK_TRY(AOM_ask_value_date(revTag, "last_mod_date", &revDateModified));
        ITK_TRY(DATE_date_to_string(revDateModified, "%Y-%m-%d %H:%M:%S", &revLastModDate));

        ITK_TRY(AOM_ask_value_tag(revTag, "owning_user", &revOwner));
        ITK_TRY(AOM_ask_value_string(revOwner, "user_id", &revOwningUser));

        ITK_TRY(AOM_ask_value_tag(revTag, "last_mod_user", &revLastModifier));
        ITK_TRY(AOM_ask_value_string(revLastModifier, "user_id", &revLastModUser));

        /* EASS and DCN */
        char *eass = NULL;
        char *dcn = NULL;
        if(iType == "TR4_PN"){
            ITK_TRY(AOM_ask_value_string(revTag, "tr4_EASS", &eass));
            ITK_TRY(AOM_ask_value_string(revTag, "tr4_DCN", &dcn));
        }

        int count = 0;
        tag_t *statuses = NULL;
        char *releaseStatus = NULL;

        ITK_TRY(AOM_ask_value_tags(revTag, "release_status_list", &count, &statuses));
        if(count>0){
            ITK_TRY(AOM_ask_value_string(statuses[0], "name", &releaseStatus));
        }
		
        char *description = NULL;
        char *revDescription = NULL;
        ITK_TRY(AOM_ask_value_string(itemTag, "object_desc", &description));
		if(description != NULL){
            for(int inx = 0;inx<strlen(description); inx++){
                if(description[inx] == '\n'){
                    description[inx] = ' ';
                }
            }
        }
        ITK_TRY(AOM_ask_value_string(revTag, "object_desc", &revDescription));
		if(revDescription != NULL){
            for(int inx = 0;inx<strlen(revDescription); inx++){
                if(revDescription[inx] == '\n'){
                    revDescription[inx] = ' ';
                }
            }
        }
        if (itr != items.end()){
            itemMap = itr->second;
            map<tag_t, TCItemData_t>::iterator itr1 = itemMap.find(itemTag);   
            if (itr1 != itemMap.end()){
                itemData = itr1->second;
            }else{
                itemData.itemTag = itemTag;
                itemData.itemId = itemId;
                itemData.itemType = iType; 
                itemData.itemName = name;
                itemData.owningUser = string(owningUser?owningUser:"");
                itemData.lastModUser = string(lastModUser?lastModUser:"");
                itemData.creationDate = string(creationDate?creationDate:"");
                itemData.lastModDate = string(lastModDate?lastModDate:"");
                itemData.itemDesc = string(description?description:"");      				
            }
        }else{
            itemData.itemTag = itemTag;
            itemData.itemId = itemId;
            itemData.itemType = iType;
            itemData.itemName = name;
            itemData.owningUser = string(owningUser?owningUser:"");
            itemData.lastModUser = string(lastModUser?lastModUser:"");
            itemData.creationDate = string(creationDate?creationDate:"");
            itemData.lastModDate = string(lastModDate?lastModDate:"");
			itemData.itemDesc = string(description?description:"");
        }  
        TCRevData_t revData;
        revData.revTag = revTag;
        revData.revId = revId;
        revData.owningUser = string(revOwningUser?revOwningUser:"");
        revData.lastModUser = string(revLastModUser?revLastModUser:"");
        revData.creationDate = string(revCreationDate?revCreationDate:"");
        revData.lastModDate = string(revLastModDate?revLastModDate:"");
        revData.eass = string(eass?eass:"");
        revData.dcn = string(dcn?dcn:"");
        revData.releaseStatus = string(releaseStatus?releaseStatus:"");
		revData.revDesc = string(revDescription?revDescription:"");
        itemData.revData[revTag] = revData;
        itemMap[itemTag] = itemData;
        items[iType] = itemMap; 
        MEM_free(creationDate);
        MEM_free(lastModDate);
        MEM_free(owningUser);
        MEM_free(lastModUser);
        MEM_free(revCreationDate);
        MEM_free(revLastModDate);
        MEM_free(revOwningUser);
        MEM_free(revLastModUser);
        MEM_free(dcn);
        MEM_free(eass);
        MEM_free(statuses);
        MEM_free(releaseStatus);
        MEM_free(description);
        MEM_free(revDescription);
    }

    MEM_free (objects);
    MEM_free (ids);
	JOURNAL_routine_end();
    return iFail;
}

/**
  * This method extracts All Item of input Type 
  * 
    @param [in]  type           Item Type
*/
int getItemsOfType(const string& type, vector<string>& itemIds){
    int iFail = ITK_ok;
    JOURNAL_routine_start ("getItemsOfType");
	JOURNAL_routine_call();
    int rows = 0;
    int cols = 0;
    void ***objects = NULL;
    
    
    const char* objType = type.c_str();
     
    static tag_t initialized = NULLTAG;
    // Query for all Item Types that need to be processed

    const char *enqId = "RBGetItemsOfType";
    if (initialized == NULLTAG) {
        const char *itemAttrList[] = { "puid" , "item_id" };
        const char *wsoAttrList[] = { "object_type" };

        POM_cache_for_session(&initialized);
		initialized = 1;

        ITK_TRY ( POM_enquiry_create ( enqId ) );

        ITK_TRY ( POM_enquiry_add_select_attrs ( enqId,"Item", 2, itemAttrList ) );


        ITK_TRY ( POM_enquiry_add_select_attrs ( enqId, "WorkspaceObject", 1, 
                                                                wsoAttrList ) );

        ITK_TRY ( POM_enquiry_set_join_expr( enqId, "IdExpr1", "Item", "puid",
                                    POM_enquiry_equal,"WorkspaceObject", "puid" ) );        

        ITK_TRY ( POM_enquiry_set_string_value ( enqId, "IdExpr2", 1, &objType, 
                                                            POM_enquiry_bind_value ) );

        ITK_TRY ( POM_enquiry_set_attr_expr ( enqId, "IdExpr3", "WorkspaceObject",
                                        "object_type", POM_enquiry_equal, "IdExpr2" ) );

        ITK_TRY ( POM_enquiry_set_expr ( enqId, "IdExpr4", "IdExpr1", POM_enquiry_and,
                                                                        "IdExpr3" ) );

        ITK_TRY ( POM_enquiry_set_where_expr ( enqId,"IdExpr4") );
    }else{
        ITK_TRY ( POM_enquiry_set_string_value ( enqId, "IdExpr2", 1, &objType, 
                                                            POM_enquiry_bind_value ) );
    }
 
    ITK_TRY ( POM_enquiry_execute ( enqId, &rows, &cols, &objects) );

    for ( int inx = 0; inx<rows; inx++ ){
        tag_t itemTag = *static_cast < tag_t * > (objects[inx][0]);
        char *itemId = static_cast < char * > (objects[inx][1]);
        itemIds.push_back(string(itemId));
    }

    MEM_free (objects);
	JOURNAL_routine_end();
    return iFail;
}

/**
  * This method extracts All Item revisions of input Item Ids 
  * 
    @param [in]  itemIds           Item Ids
    @param [in]  keys              Item Id#Revision ID
    @param [out] items             map<Type, map<Item tag,Item data>>
*/
int extractItems(vector<string> itemIds, vector<string> keys, map<string, map<tag_t,TCItemData_t> > &items){
    int iFail = ITK_ok;
    JOURNAL_routine_start ("extractItems");
	JOURNAL_routine_call();
    int rows = 0;
    int cols = 0;
    void ***objects = NULL;

    char **ids = (char **)MEM_alloc(sizeof(char *)*(itemIds.size()));
    for(int inx = 0; inx<itemIds.size(); inx++){
        ids[inx] = (char *)MEM_alloc(sizeof(char)*(strlen(itemIds[inx].c_str())+1));
        strcpy(ids[inx], itemIds[inx].c_str());
    }
    
    static tag_t initialized = NULLTAG;
    // Query for all Item Types that need to be processed

    const char *enqId = "RBGetItems";
    if (initialized == NULLTAG) {
        const char *itemRevAttrList[] = { "puid" , "item_revision_id" };
        const char *itemAttrList[] = { "puid" , "item_id" };
        const char *wsoAttrList[] = { "object_type", "object_name"};

        POM_cache_for_session(&initialized);
		initialized = 1;

        ITK_TRY ( POM_enquiry_create ( enqId ) );

        ITK_TRY ( POM_enquiry_add_select_attrs ( enqId,"Item", 2, itemAttrList ) );

        ITK_TRY ( POM_enquiry_add_select_attrs ( enqId, "ItemRevision", 2, 
                                                                itemRevAttrList ) );

        ITK_TRY ( POM_enquiry_add_select_attrs ( enqId, "WorkspaceObject", 2, 
                                                                wsoAttrList ) );

        ITK_TRY ( POM_enquiry_set_join_expr( enqId, "IdExpr1", "Item", "puid",
                                    POM_enquiry_equal,"WorkspaceObject", "puid" ) );
        
        ITK_TRY ( POM_enquiry_set_join_expr( enqId, "IdExpr2", "Item", "puid",
                                    POM_enquiry_equal,"ItemRevision", "items_tag" ) );

        int activeSeq = 1;
        ITK_TRY(
                POM_enquiry_set_int_value(enqId, "IdExpr3",1 , &activeSeq,
                                                            POM_enquiry_bind_value) );
        ITK_TRY(
                POM_enquiry_set_attr_expr(enqId, "IdExpr4", "ItemRevision", "active_seq", 
                                                            POM_enquiry_equal,"IdExpr3") );

        ITK_TRY ( POM_enquiry_set_string_value ( enqId, "IdExpr5", itemIds.size(), (const char **)ids, 
                                                            POM_enquiry_bind_value ) );

        ITK_TRY ( POM_enquiry_set_attr_expr ( enqId, "IdExpr6", "Item",
                                        "item_id", POM_enquiry_in, "IdExpr5" ) );

        ITK_TRY ( POM_enquiry_set_expr ( enqId, "IdExpr7", "IdExpr1", POM_enquiry_and,
                                                                        "IdExpr2" ) );

        ITK_TRY ( POM_enquiry_set_expr ( enqId, "IdExpr8", "IdExpr6", POM_enquiry_and,
                                                                        "IdExpr4" ) );

        ITK_TRY ( POM_enquiry_set_expr ( enqId, "IdExpr9", "IdExpr8", POM_enquiry_and,
                                                                        "IdExpr7" ) );
        ITK_TRY ( POM_enquiry_set_where_expr ( enqId,"IdExpr9") );
    }else{
         ITK_TRY ( POM_enquiry_set_string_value ( enqId, "IdExpr5", itemIds.size(), 
                                         (const char **)ids, POM_enquiry_bind_value ) );
    }
 
    ITK_TRY ( POM_enquiry_execute ( enqId, &rows, &cols, &objects) );

    for ( int inx = 0; inx<rows; inx++ ){
        tag_t itemTag = *static_cast < tag_t * > (objects[inx][0]);
        char *itemId = static_cast < char * > (objects[inx][1]);
        tag_t revTag = *static_cast < tag_t * > (objects[inx][2]);
        char *revId = static_cast < char * > (objects[inx][3]);
        char *type = static_cast < char * > (objects[inx][4]);
        char *name = static_cast < char * > (objects[inx][5]);
        string iType = string(type);
        map<string, map<tag_t, TCItemData_t> >::iterator itr;
        itr = items.find(iType);
        map<tag_t, TCItemData_t> itemMap;
        TCItemData_t itemData;
        string key = string(itemId)+"#"+string(revId);
        if ( std::find(keys.begin(), keys.end(), key) == keys.end() ){
            TC_write_syslog("Item Revision not required to process %s\n", key.c_str());
            continue;
        }

        /**
           Populate Item information
        */
        date_t dateCreated = NULLDATE;
        date_t dateModified = NULLDATE;
        tag_t lastModifier = NULLTAG;
        tag_t owner = NULLTAG;
        char *creationDate = NULL;
        char *lastModDate = NULL;
        char *owningUser = NULL;
        char *lastModUser = NULL;

        ITK_TRY(AOM_ask_value_date(itemTag, "creation_date", &dateCreated));
        ITK_TRY(DATE_date_to_string(dateCreated, "%Y-%m-%d %H:%M:%S", &creationDate));
         /* Last Modified Date */
        ITK_TRY(AOM_ask_value_date(itemTag, "last_mod_date", &dateModified));
        ITK_TRY(DATE_date_to_string(dateModified, "%Y-%m-%d %H:%M:%S", &lastModDate));

        ITK_TRY(AOM_ask_value_tag(itemTag, "owning_user", &owner));
        ITK_TRY(AOM_ask_value_string(owner, "user_id", &owningUser));

        ITK_TRY(AOM_ask_value_tag(itemTag, "last_mod_user", &lastModifier));
        ITK_TRY(AOM_ask_value_string(lastModifier, "user_id", &lastModUser));

        /**
           Populate Item Revision information
        */
        date_t revDateCreated = NULLDATE;
        date_t revDateModified = NULLDATE;
        tag_t revLastModifier = NULLTAG;
        tag_t revOwner = NULLTAG;
        char *revCreationDate = NULL;
        char *revLastModDate = NULL;
        char *revOwningUser = NULL;
        char *revLastModUser = NULL;

        ITK_TRY(AOM_ask_value_date(revTag, "creation_date", &revDateCreated));
        ITK_TRY(DATE_date_to_string(revDateCreated, "%Y-%m-%d %H:%M:%S", &revCreationDate));
         /* Last Modified Date */
        ITK_TRY(AOM_ask_value_date(revTag, "last_mod_date", &revDateModified));
        ITK_TRY(DATE_date_to_string(revDateModified, "%Y-%m-%d %H:%M:%S", &revLastModDate));

        ITK_TRY(AOM_ask_value_tag(revTag, "owning_user", &revOwner));
        ITK_TRY(AOM_ask_value_string(revOwner, "user_id", &revOwningUser));

        ITK_TRY(AOM_ask_value_tag(revTag, "last_mod_user", &revLastModifier));
        ITK_TRY(AOM_ask_value_string(revLastModifier, "user_id", &revLastModUser));

        /* EASS and DCN */
        char *eass = NULL;
        char *dcn = NULL;

        if(iType == "TR4_PN"){
            ITK_TRY(AOM_ask_value_string(revTag, "tr4_EASS", &eass));
            ITK_TRY(AOM_ask_value_string(revTag, "tr4_DCN", &dcn));
        }

        int count = 0;
        tag_t *statuses = NULL;
        char *releaseStatus = NULL;

        ITK_TRY(AOM_ask_value_tags(revTag, "release_status_list", &count, &statuses));
        if(count>0){
            ITK_TRY(AOM_ask_value_string(statuses[0], "name", &releaseStatus));
        }

        char *description = NULL;
        char *revDescription = NULL;
        ITK_TRY(AOM_ask_value_string(itemTag, "object_desc", &description));
		if(description != NULL){
            for(int inx = 0;inx<strlen(description); inx++){
                if(description[inx] == '\n'){
                    description[inx] = ' ';
                }
            }
        }
        ITK_TRY(AOM_ask_value_string(revTag, "object_desc", &revDescription));
		if(revDescription != NULL){
            for(int inx = 0;inx<strlen(revDescription); inx++){
                if(revDescription[inx] == '\n'){
                    revDescription[inx] = ' ';
                }
            }
        }
        if (itr != items.end()){
            itemMap = itr->second;
            map<tag_t, TCItemData_t>::iterator itr1 = itemMap.find(itemTag);   
            if (itr1 != itemMap.end()){
                itemData = itr1->second;
            }else{
                itemData.itemTag = itemTag;
                itemData.itemId = itemId;
                itemData.itemType = iType;
                itemData.itemName = name;
                itemData.owningUser = string(owningUser?owningUser:"");
                itemData.lastModUser = string(lastModUser?lastModUser:"");
                itemData.creationDate = string(creationDate?creationDate:"");
                itemData.lastModDate = string(lastModDate?lastModDate:"");
				itemData.itemDesc = string(description?description:"");
            }
        }else{
            itemData.itemTag = itemTag;
            itemData.itemId = itemId;
            itemData.itemType = iType;
            itemData.itemName = name;  
            itemData.owningUser = string(owningUser?owningUser:"");
            itemData.lastModUser = string(lastModUser?lastModUser:"");
            itemData.creationDate = string(creationDate?creationDate:"");
            itemData.lastModDate = string(lastModDate?lastModDate:"");
			itemData.itemDesc = string(description?description:"");
        }  
        TCRevData_t revData;
        revData.revTag = revTag;
        revData.revId = revId;
        revData.owningUser = string(revOwningUser?revOwningUser:"");
        revData.lastModUser = string(revLastModUser?revLastModUser:"");
        revData.creationDate = string(revCreationDate?revCreationDate:"");
        revData.lastModDate = string(revLastModDate?revLastModDate:"");
        revData.eass = string(eass?eass:"");
        revData.dcn = string(dcn?dcn:"");
        revData.releaseStatus = string(releaseStatus?releaseStatus:"");
		revData.revDesc = string(revDescription?revDescription:"");
        itemData.revData[revTag] = revData;
        itemMap[itemTag] = itemData;
        items[iType] = itemMap;
        MEM_free(creationDate);
        MEM_free(lastModDate);
        MEM_free(owningUser);
        MEM_free(lastModUser);
        MEM_free(revCreationDate);
        MEM_free(revLastModDate);
        MEM_free(revOwningUser);
        MEM_free(revLastModUser);
        MEM_free(dcn);
        MEM_free(eass);
        MEM_free(statuses);
        MEM_free(releaseStatus);
		        MEM_free(description);
        MEM_free(revDescription);  
    }

    MEM_free (objects);
    MEM_free (ids);
	JOURNAL_routine_end();
    return iFail;
}

/**
  * This method extracts datasets attached to all input objects
  * These objects could be anything - Items or Item Revisions 
  * 
    @param [in]  datasetTypes         Dataset Types to be extracted
*/
int getRevisionsWithDatasetTypes(vector<string> datasetTypes, vector<string>& activeRevisions, 
                                 vector<string>& inactiveRevisions){
   
    int iFail = ITK_ok;
    JOURNAL_routine_start ("getRevisionsWithDatasetTypes");
	JOURNAL_routine_call();
    int rows = 0;
    int cols = 0;
    void ***objects = NULL;

    char **types = (char **)MEM_alloc(sizeof(char *)*(datasetTypes.size()));
    for(int inx = 0; inx<datasetTypes.size(); inx++){
        types[inx] = (char *)MEM_alloc(sizeof(char)*(strlen(datasetTypes[inx].c_str())+1));
        strcpy(types[inx], datasetTypes[inx].c_str());
    }

    static tag_t initialized = NULLTAG;
    // Query for all Item Types that need to be processed

    const char *enqId = "RBGetRevsWithDatasetTypes";
    if (initialized == NULLTAG) {
        const char *itemAttributes[] = { "puid", "item_id" };
        const char *revAttributes[] = { "puid", "item_revision_id", "active_seq" };
        const char *datasetAttrList[] = { "puid" };
        const char *wsoAttrList[] = {"object_type", "object_name"};

        POM_cache_for_session(&initialized);
		initialized = 1;

        ITK_TRY ( POM_enquiry_create ( enqId ));
        ITK_TRY ( POM_enquiry_add_select_attrs ( enqId,"Item", 2, itemAttributes ));
        ITK_TRY ( POM_enquiry_add_select_attrs ( enqId, "ItemRevision", 3, revAttributes ));
        ITK_TRY ( POM_enquiry_add_select_attrs ( enqId, "Dataset", 1, datasetAttrList )); 
        ITK_TRY ( POM_enquiry_add_select_attrs ( enqId, "WorkspaceObject", 2, wsoAttrList ) );

        ITK_TRY ( POM_enquiry_set_join_expr( enqId, "IdExpr1", "ImanRelation", "secondary_object",
                                    POM_enquiry_equal,"Dataset", "puid" ) );

        ITK_TRY ( POM_enquiry_set_join_expr( enqId, "IdExpr2", "ImanRelation", "primary_object",
                                    POM_enquiry_equal,"ItemRevision", "puid" ) );

        ITK_TRY ( POM_enquiry_set_join_expr( enqId, "IdExpr3", "ItemRevision", "items_tag",
                                    POM_enquiry_equal,"Item", "puid" ) );

        ITK_TRY ( POM_enquiry_set_join_expr( enqId, "IdExpr4", "Dataset", "puid",
                                    POM_enquiry_equal,"WorkspaceObject", "puid" ) );

        ITK_TRY ( POM_enquiry_set_string_value ( enqId, "datasetTypes", datasetTypes.size(), 
                                                 (const char **)types, POM_enquiry_bind_value ) );

        ITK_TRY ( POM_enquiry_set_attr_expr ( enqId, "IdExpr5", "WorkspaceObject",
                                        "object_type", POM_enquiry_in, "datasetTypes" ) );

        ITK_TRY ( POM_enquiry_set_expr ( enqId, "IdExpr6", "IdExpr2", POM_enquiry_and,
                                                                        "IdExpr1" ) );

        ITK_TRY ( POM_enquiry_set_expr ( enqId, "IdExpr7", "IdExpr4", POM_enquiry_and,
                                                                        "IdExpr3" ) );

        ITK_TRY ( POM_enquiry_set_expr ( enqId, "IdExpr8", "IdExpr6", POM_enquiry_and,
                                                                        "IdExpr5" ) );

        ITK_TRY ( POM_enquiry_set_expr ( enqId, "IdExpr9", "IdExpr8", POM_enquiry_and,
                                                                        "IdExpr7" ) );
        ITK_TRY ( POM_enquiry_set_where_expr ( enqId,"IdExpr9") );
    }else{
        ITK_TRY ( POM_enquiry_set_string_value ( enqId, "datasetTypes", datasetTypes.size(), 
                                                 (const char **)types, POM_enquiry_bind_value ) );
    }
    ITK_TRY ( POM_enquiry_execute ( enqId, &rows, &cols, &objects) );
    for ( int inx = 0; inx<rows; inx++ ){
        char *dset = NULL;
        tag_t itemTag = *static_cast < tag_t * > (objects[inx][0]);
        char *itemId = static_cast < char * > (objects[inx][1]);
        tag_t revTag = *static_cast < tag_t * > (objects[inx][2]);
        char *revId = static_cast < char * > (objects[inx][3]);
        int active_seq = *static_cast < tag_t * > (objects[inx][4]);
        tag_t datasetTag = *static_cast < tag_t * > (objects[inx][5]);
        char *datasetType = static_cast < char * > (objects[inx][6]);
        char *datasetName = static_cast < char * > (objects[inx][7]);
        ITK_TRY(POM_tag_to_string(datasetTag, &dset));
       
        if(active_seq == 1){
            activeRevisions.push_back(string(itemId)+"|"+string(revId)+"|"+string(dset)+"|"+string(datasetType)+"|"+string(datasetName));
        }else if(active_seq == 0){
            inactiveRevisions.push_back(string(itemId)+"|"+string(revId)+"|"+string(dset)+"|"+string(datasetType)+"|"+string(datasetName));
        }
        MEM_free(dset);        
    }

    MEM_free (objects);
    MEM_free (types);
	JOURNAL_routine_end();
    return iFail;
}

/**
  * This method extracts all datasets attached to all input objects
  * These objects could be anything - Items or Item Revisions 
  * 
    @param [in]  objectsTags          Vector of tags
    @param [out] datasets             map<input object, map<dataset tag,dataset data>>
*/
int extractAllDatasets(vector<tag_t> objectTags, map<tag_t, map<tag_t, TCDatasetData_t> > &datasets){
    int iFail = ITK_ok;
    JOURNAL_routine_start ("extractAllDatasets");
	JOURNAL_routine_call();
    int rows = 0;
    int cols = 0;
    void ***objects = NULL;

    if (objectTags.size() <= 0){
        return iFail;
    }
    tag_t *objTags = NULL;
    objTags = (tag_t *)MEM_alloc(sizeof(tag_t)*(objectTags.size()));
    //Collect all Object tags in an array
    for (int inx =0;inx<objectTags.size(); inx++){
        objTags[inx] = objectTags[inx];
    }

    static tag_t initialized = NULLTAG;
    // Query for all Item Types that need to be processed

    const char *enqId = "RBGetAllDatasets";
    if (initialized == NULLTAG) {
        const char *imanRelationAttrList[] = { "puid", "primary_object", "secondary_object" };
        const char *imanTypeAttrList[] = { "type_name" };
        const char *datasetAttrList[] = { "puid" };
        const char *wsoAttrList[] = {"object_type", "object_name"};

        POM_cache_for_session(&initialized);
		initialized = 1;

        ITK_TRY ( POM_enquiry_create ( enqId ));
        ITK_TRY ( POM_enquiry_add_select_attrs ( enqId,"ImanRelation", 3, imanRelationAttrList ));
        ITK_TRY ( POM_enquiry_add_select_attrs ( enqId, "ImanType", 1, imanTypeAttrList ));
        ITK_TRY ( POM_enquiry_add_select_attrs ( enqId, "Dataset", 1, datasetAttrList )); 
        ITK_TRY ( POM_enquiry_add_select_attrs ( enqId, "WorkspaceObject", 2, wsoAttrList ) );

        ITK_TRY ( POM_enquiry_set_join_expr( enqId, "IdExpr1", "ImanRelation", "secondary_object",
                                    POM_enquiry_equal,"Dataset", "puid" ) );
        
        ITK_TRY( POM_enquiry_set_tag_value( enqId, "objTags", objectTags.size(), objTags, 
                                                                   POM_enquiry_bind_value));
        
        ITK_TRY( POM_enquiry_set_attr_expr( enqId, "IdExpr2", "ImanRelation", "primary_object", 
                                                                POM_enquiry_in, "objTags"));

        ITK_TRY ( POM_enquiry_set_join_expr( enqId, "IdExpr3", "ImanRelation", "relation_type",
                                    POM_enquiry_equal,"ImanType", "puid" ) );
        
        ITK_TRY ( POM_enquiry_set_join_expr( enqId, "IdExpr4", "Dataset", "puid",
                                    POM_enquiry_equal,"WorkspaceObject", "puid" ) );

        ITK_TRY ( POM_enquiry_set_expr ( enqId, "IdExpr5", "IdExpr2", POM_enquiry_and,
                                                                        "IdExpr1" ) );

        ITK_TRY ( POM_enquiry_set_expr ( enqId, "IdExpr6", "IdExpr4", POM_enquiry_and,
                                                                        "IdExpr3" ) );

        ITK_TRY ( POM_enquiry_set_expr ( enqId, "IdExpr7", "IdExpr6", POM_enquiry_and,
                                                                        "IdExpr5" ) );
        ITK_TRY ( POM_enquiry_set_where_expr ( enqId,"IdExpr7") );
    }else{
        ITK_TRY( POM_enquiry_set_tag_value( enqId, "objTags", objectTags.size(), objTags, 
                                                                   POM_enquiry_bind_value));
    }
    ITK_TRY ( POM_enquiry_execute ( enqId, &rows, &cols, &objects) );

    for ( int inx = 0; inx<rows; inx++ ){
        tag_t relationTag = *static_cast < tag_t * > (objects[inx][0]);
        tag_t objTag = *static_cast < tag_t * > (objects[inx][1]);
        tag_t secondaryTag = *static_cast < tag_t * > (objects[inx][2]);
        char *relationName = static_cast < char * > (objects[inx][3]);
        tag_t datasetTag = *static_cast < tag_t * > (objects[inx][4]);
        char *datasetType = static_cast < char * > (objects[inx][5]);
        char *datasetName = static_cast < char * > (objects[inx][6]);
        
        date_t dateCreated = NULLDATE;
        date_t dateModified = NULLDATE;

        char *creationDate = NULL;
        char *lastModDate = NULL;
        char *owningUser = NULL;
        char *lastModUser = NULL;

        tag_t owner = NULLTAG;
        tag_t lastModifier = NULLTAG;
        tag_t *statuses = NULL;
        char *releaseStatus = NULL;
        int count = 0;
        char *description = NULL;
 
        ITK_TRY(AOM_refresh(datasetTag, false));
        /* Creation Date */        
        ITK_TRY(AOM_ask_value_date(datasetTag, "creation_date", &dateCreated));
        ITK_TRY(DATE_date_to_string(dateCreated, "%Y-%m-%d %H:%M:%S", &creationDate));
         /* Last Modified Date */
        ITK_TRY(AOM_ask_value_date(datasetTag, "last_mod_date", &dateModified));
        ITK_TRY(DATE_date_to_string(dateModified, "%Y-%m-%d %H:%M:%S", &lastModDate));

        ITK_TRY(AOM_ask_value_tag(datasetTag, "owning_user", &owner));
        ITK_TRY(AOM_ask_value_string(owner, "user_id", &owningUser));

        ITK_TRY(AOM_ask_value_tag(datasetTag, "last_mod_user", &lastModifier));
        ITK_TRY(AOM_ask_value_string(lastModifier, "user_id", &lastModUser));

        ITK_TRY(AOM_ask_value_tags(datasetTag, "release_status_list", &count, &statuses));
        if(count>0){
            ITK_TRY(AOM_ask_value_string(statuses[0], "name", &releaseStatus));
        }
        ITK_TRY(AOM_ask_value_string(datasetTag, "object_desc", &description));
        map<tag_t, map<tag_t, TCDatasetData_t> >::iterator itr;
        itr = datasets.find(objTag);
        map<tag_t, TCDatasetData_t> datasetMap;
        TCDatasetData_t datasetData;
        if(description != NULL){
            for(int inx = 0;inx<strlen(description); inx++){
                if(description[inx] == '\n'){
                    description[inx] = ' ';
                }
            }
        }

        if (itr != datasets.end()){
            datasetMap = itr->second;
            map<tag_t, TCDatasetData_t>::iterator itr1 = datasetMap.find(datasetTag);   
            if (itr1 != datasetMap.end()){
                datasetData = itr1->second;
            }else{
                datasetData.dataset = datasetTag;
                datasetData.datasetName = datasetName;
                datasetData.datasetType = datasetType;
                datasetData.relation = relationName;
                datasetData.owningUser = string(owningUser);
                datasetData.lastModUser = string(lastModUser?lastModUser:"");
                datasetData.creationDate = string(creationDate);
                datasetData.lastModDate = string(lastModDate);
                datasetData.releaseStatus = string(releaseStatus?releaseStatus:"");
                datasetData.description = string(description?description:"");
            }
        }else{
            datasetData.dataset = datasetTag;
            datasetData.datasetName = datasetName;
            datasetData.datasetType = datasetType;
            datasetData.relation = relationName;
            datasetData.owningUser = string(owningUser);
            datasetData.lastModUser = string(lastModUser?lastModUser:"");
            datasetData.creationDate = string(creationDate);
            datasetData.lastModDate = string(lastModDate);
            datasetData.releaseStatus = string(releaseStatus?releaseStatus:"");
            datasetData.description = string(description?description:"");
        }  
        datasetMap[datasetTag] = datasetData;
        datasets[objTag] = datasetMap; 
        MEM_free(lastModUser);
        MEM_free(owningUser);
        MEM_free(creationDate);
        MEM_free(lastModDate); 
        MEM_free(releaseStatus);
        MEM_free(statuses);   
        MEM_free(description);         
    }

    MEM_free (objects);
	JOURNAL_routine_end();
    return iFail;
}

string getPrimarySecondary(const string &datasetType, const string &fileName){
    string output = "Primary";

    if(datasetType == "ProPrt" && fileName.find(".prt.") == std::string::npos){
        output = "Secondary";
    }
    if(datasetType == "ProAsm" && fileName.find(".asm.") == std::string::npos){
        output = "Secondary";
    }
    if(datasetType == "ProDrw" && fileName.find(".drw.") == std::string::npos){
        output = "Secondary";
    }
    if(datasetType == "TR4_E3View" && fileName.find(".e3v") == std::string::npos){
        output = "Secondary";
    }
    if(datasetType == "TR4_E3Project" && fileName.find(".e3s") == std::string::npos){
        output = "Secondary";
    }
    return output;
}

int getObjectLocations( tag_t folder, string parentLocation,
                        map<tag_t, set<string> > &objectLocations ){
    int iFail = ITK_ok;
    JOURNAL_routine_start ("getObjectLocations");
	JOURNAL_routine_call();
    
    FL_sort_criteria_t sort_criteria;
    ITK_TRY(FL_ask_sort_criteria(folder, &sort_criteria));
    int num_of_references = 0;
    tag_t* all_references = NULL;
    ITK_TRY(FL_ask_references(folder, sort_criteria,
                                &num_of_references, &all_references));
    for(int inx = 0; inx<num_of_references; inx++){
        tag_t objTag = all_references[inx];
        char *name = NULL;
        char *type = NULL;
        char *className = NULL;
        tag_t classTag = NULLTAG;
        ITK_TRY(WSOM_ask_name2(objTag, &name));
        ITK_TRY(WSOM_ask_object_type2(objTag, &type));
        ITK_TRY(POM_class_of_instance(objTag, &classTag));
        ITK_TRY(POM_name_of_class(classTag, &className));
        if(strcmp(className, "Folder") != 0){          
            map<tag_t, set<string> >::iterator itr = objectLocations.find(objTag);
            set<string> locations;
            if(itr!=objectLocations.end()){
                locations = itr->second;
            } 
            locations.insert(parentLocation);
            objectLocations[objTag] = locations;
        }else{
            string location = parentLocation+"~"+string(name);
            getObjectLocations(objTag,location,objectLocations);
        }
        MEM_free(name);
        MEM_free(type);
        MEM_free(className);
    }
    MEM_free(all_references);
    return iFail;
}

int findFolders (const string& folder, vector<tag_t>& folders){
    int iFail = ITK_ok;
    JOURNAL_routine_start ("getItemLocations");
	JOURNAL_routine_call();
    int rows = 0;
    int cols = 0;
    void ***objects = NULL;
    const char *folderName = folder.c_str();

    static tag_t initialized = NULLTAG;
    // Query for all Item Types that need to be processed

    const char *mainEnqId = "RBGetObjectLocations";
    if (initialized == NULLTAG) {
        const char *folderAttributes[] = {"puid"};
       
        POM_cache_for_session(&initialized);
		initialized = 1;   
        ITK_TRY ( POM_enquiry_create ( mainEnqId ));     
        ITK_TRY ( POM_enquiry_add_select_attrs ( mainEnqId , "Folder",
                                       1, folderAttributes));
                                       
        ITK_TRY ( POM_enquiry_set_join_expr( mainEnqId,
                                "volvoExprId0", "Folder","puid",
                                POM_enquiry_equal,"WorkspaceObject",
                                "puid" ));                                        

        ITK_TRY ( POM_enquiry_set_string_value ( mainEnqId, 
                                "volvoExprId1", 1, (const char **)&folderName,
                                POM_enquiry_bind_value ) );


        ITK_TRY ( POM_enquiry_set_attr_expr( mainEnqId,
                                        "volvoExprId2",
                                        "WorkspaceObject",
                                        "object_name",
                                        POM_enquiry_in,
                                        "volvoExprId1" ) );

        ITK_TRY ( POM_enquiry_set_expr( mainEnqId,
                                    "volvoExprId3",
                                    "volvoExprId2",
                                    POM_enquiry_and,
                                    "volvoExprId0" ) );

        ITK_TRY (  POM_enquiry_set_where_expr ( mainEnqId,"volvoExprId3") );

    }else{
        ITK_TRY ( POM_enquiry_set_string_value ( mainEnqId, 
                                "volvoExprId1", 1, (const char **)&folderName,
                                POM_enquiry_bind_value ) );
    }

    ITK_TRY ( POM_enquiry_execute ( mainEnqId, &rows, &cols, &objects) );
    cout << "No. of Folders" << rows << endl;

    for  ( int inx = 0;inx<rows; inx++)
    {
       tag_t folderTag  = NULLTAG;
       
       folderTag = *(tag_t *)(objects[inx][0]);
       folders.push_back (folderTag);
    }
    
    JOURNAL_routine_end();
    return iFail;
}

int extractStandaloneDatasets(vector<string> datasetTypes,
                               map<string, map<tag_t, TCDatasetData_t> > &datasets){
    int iFail = ITK_ok;
    JOURNAL_routine_start ("extractStandaloneDatasets");
	JOURNAL_routine_call();
    int rows = 0;
    int cols = 0;
    void ***objects = NULL;

    char **types = (char **)MEM_alloc(sizeof(char *)*(datasetTypes.size()));
    for(int inx = 0; inx<datasetTypes.size(); inx++){
        types[inx] = (char *)MEM_alloc(sizeof(char)*(strlen(datasetTypes[inx].c_str())+1));
        strcpy(types[inx], datasetTypes[inx].c_str());
    }

    static tag_t initialized = NULLTAG;

    const char *enqId = "RBGetStandaloneDatasets";
    const char *subEnqId = "RBRelationEnq";
    if (initialized == NULLTAG) {
        const char *datasetAttrList[] = { "puid" };
        const char *wsoAttrList[] = {"object_type", "object_name"};
        const char *imanRelationAttrList[] = {"secondary_object"};

        POM_cache_for_session(&initialized);
		initialized = 1;

        ITK_TRY ( POM_enquiry_create ( enqId ));
        ITK_TRY ( POM_enquiry_add_select_attrs ( enqId, "Dataset", 1, datasetAttrList )); 
        ITK_TRY ( POM_enquiry_add_select_attrs ( enqId, "WorkspaceObject", 2, wsoAttrList ) );
        ITK_TRY ( POM_enquiry_set_pseudo_calias ( enqId, "RevisionAnchor", "revisions", "dataset_revisions" ) );

        ITK_TRY ( POM_enquiry_set_join_expr( enqId, "IdExpr1", "Dataset", "puid",
                                    POM_enquiry_equal,"WorkspaceObject", "puid" ) );

        ITK_TRY ( POM_enquiry_set_string_value ( enqId, "datasetTypes", datasetTypes.size(), 
                                                 (const char **)types, POM_enquiry_bind_value ) );

        ITK_TRY ( POM_enquiry_set_attr_expr ( enqId, "IdExpr2", "WorkspaceObject",
                                        "object_type", POM_enquiry_in, "datasetTypes" ) );
        //const char *seqId = "0";
        const int seqId = 0;
        ITK_TRY ( POM_enquiry_set_int_value ( enqId, "seqid", 1, 
                                                 &seqId, POM_enquiry_bind_value ) );

        ITK_TRY ( POM_enquiry_set_attr_expr ( enqId, "IdExpr3", "dataset_revisions",
                                        "pseq", POM_enquiry_in, "seqid" ) );

        ITK_TRY ( POM_enquiry_set_join_expr( enqId, "IdExpr4", "Dataset", "puid",
                                    POM_enquiry_equal,"dataset_revisions", "PVAL" ) );

        ITK_TRY ( POM_enquiry_set_expr ( enqId, "IdExpr6", "IdExpr2", POM_enquiry_and,
                                                                        "IdExpr1" ) );
        ITK_TRY ( POM_enquiry_set_expr ( enqId, "IdExpr5", "IdExpr4", POM_enquiry_and,
                                                                        "IdExpr3" ) );                                                                
        ITK_TRY ( POM_enquiry_set_expr ( enqId, "IdExpr7", "IdExpr6", POM_enquiry_and,
                                                                        "IdExpr5" ) ); 
        ITK_TRY (POM_enquiry_set_sub_enquiry ( enqId, subEnqId ));
        ITK_TRY ( POM_enquiry_add_select_attrs ( subEnqId, "ImanRelation", 1, imanRelationAttrList ));    
        ITK_TRY ( POM_enquiry_set_join_expr( subEnqId, "subExpr1", "ItemRevision", "puid", POM_enquiry_equal,
                                                          "ImanRelation", "primary_object" ) );
        ITK_TRY ( POM_enquiry_set_where_expr ( subEnqId,"subExpr1") );
        ITK_TRY ( POM_enquiry_set_attr_expr ( enqId, "IdExpr8", "Dataset", "puid", POM_enquiry_not_in,
                                                                        subEnqId ) );
        ITK_TRY ( POM_enquiry_set_expr ( enqId, "IdExpr9", "IdExpr8", POM_enquiry_and,
                                                                        "IdExpr7" ) );

        ITK_TRY ( POM_enquiry_set_where_expr ( enqId,"IdExpr9") );
    }else{
        ITK_TRY ( POM_enquiry_set_string_value ( enqId, "datasetTypes", datasetTypes.size(), 
                                                 (const char **)types, POM_enquiry_bind_value ) );
    }
    ITK_TRY ( POM_enquiry_execute ( enqId, &rows, &cols, &objects) );

    for ( int inx = 0; inx<rows; inx++ ){
        tag_t datasetTag = *static_cast < tag_t * > (objects[inx][0]);
        char *datasetType = static_cast < char * > (objects[inx][1]);
        char *datasetName = static_cast < char * > (objects[inx][2]);

        date_t dateCreated = NULLDATE;
        date_t dateModified = NULLDATE;

        char *creationDate = NULL;
        char *lastModDate = NULL;
        char *owningUser = NULL;
        char *lastModUser = NULL;

        tag_t owner = NULLTAG;
        tag_t lastModifier = NULLTAG;
        tag_t *statuses = NULL;
        char *releaseStatus = NULL;
        int count = 0;
        char *description = NULL;

        ITK_TRY(AOM_refresh(datasetTag, false));
        /* Creation Date */        
        ITK_TRY(AOM_ask_value_date(datasetTag, "creation_date", &dateCreated));
        ITK_TRY(DATE_date_to_string(dateCreated, "%Y-%m-%d %H:%M:%S", &creationDate));
         /* Last Modified Date */
        ITK_TRY(AOM_ask_value_date(datasetTag, "last_mod_date", &dateModified));
        ITK_TRY(DATE_date_to_string(dateModified, "%Y-%m-%d %H:%M:%S", &lastModDate));

        ITK_TRY(AOM_ask_value_tag(datasetTag, "owning_user", &owner));
        ITK_TRY(AOM_ask_value_string(owner, "user_id", &owningUser));

        ITK_TRY(AOM_ask_value_tag(datasetTag, "last_mod_user", &lastModifier));
        ITK_TRY(AOM_ask_value_string(lastModifier, "user_id", &lastModUser));

        ITK_TRY(AOM_ask_value_tags(datasetTag, "release_status_list", &count, &statuses));
        if(count>0){
            ITK_TRY(AOM_ask_value_string(statuses[0], "name", &releaseStatus));
        }

        ITK_TRY(AOM_ask_value_string(datasetTag, "object_desc", &description));   
		if(description != NULL){
            for(int inx = 0;inx<strlen(description); inx++){
                if(description[inx] == '\n'){
                    description[inx] = ' ';
                }
            }
        }
        map<string, map<tag_t, TCDatasetData_t> >::iterator itr;
        itr = datasets.find(datasetType);
        map<tag_t, TCDatasetData_t> datasetMap;
        TCDatasetData_t datasetData;

        if (itr != datasets.end()){
            datasetMap = itr->second;
            map<tag_t, TCDatasetData_t>::iterator itr1 = datasetMap.find(datasetTag);   
            if (itr1 != datasetMap.end()){
                datasetData = itr1->second;
            }else{
                datasetData.dataset = datasetTag;
                datasetData.datasetName = datasetName;
                datasetData.datasetType = datasetType;
                datasetData.relation = "";
                datasetData.owningUser = string(owningUser);
                datasetData.lastModUser = string(lastModUser?lastModUser:"");
                datasetData.creationDate = string(creationDate);
                datasetData.lastModDate = string(lastModDate);
                datasetData.releaseStatus = string(releaseStatus?releaseStatus:"");
                datasetData.description = string(description?description:"");
            }
        }else{
            datasetData.dataset = datasetTag;
            datasetData.datasetName = datasetName;
            datasetData.datasetType = datasetType;
            datasetData.relation = "";
            datasetData.owningUser = string(owningUser);
            datasetData.lastModUser = string(lastModUser?lastModUser:"");
            datasetData.creationDate = string(creationDate);
            datasetData.lastModDate = string(lastModDate);
            datasetData.releaseStatus = string(releaseStatus?releaseStatus:"");
            datasetData.description = string(description?description:"");
        }  
        datasetMap[datasetTag] = datasetData;
        datasets[datasetType] = datasetMap;
        MEM_free(lastModUser);
        MEM_free(owningUser);
        MEM_free(creationDate);
        MEM_free(lastModDate); 
        MEM_free(releaseStatus);
        MEM_free(statuses); 
        MEM_free(description);
    }

    MEM_free (objects);
    MEM_free (types);
	JOURNAL_routine_end();
    return iFail;
}
