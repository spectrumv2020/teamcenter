
static logical is_WorkspaceObject(tag_t object)
{
    tag_t wso_class = NULLTAG;
    IFERR_REPORT(POM_class_id_of_class("WorkspaceObject", &wso_class));

        tag_t class_tag = NULLTAG;
    IFERR_REPORT(POM_class_of_instance(object, &class_tag));

        logical verdict = FALSE;
    IFERR_REPORT(POM_is_descendant(wso_class, class_tag, &verdict));

    return (verdict);
}

static logical convert_uid_to_tag(char *uid_string, tag_t *object_tag)
{
    logical  is_valid = TRUE;

    /* this function does not return an error code */
    ITK__convert_uid_to_tag(uid_string, object_tag);
    if (object_tag == NULLTAG)
    {
        is_valid = FALSE;
    }
    IFERR_REPORT(POM_is_tag_valid(*object_tag, &is_valid));
    return is_valid;
}

static void demo_audit_get_records(void)
{
    int ifail = ITK_ok;

    AUDIT_log_query_t aLogQuery;
    AUDIT_initialize_log_query(&aLogQuery);
    aLogQuery.objTypeName = "Item";
    aLogQuery.objectOperator = AUDIT_equal;
    aLogQuery.name = "6970819";
    aLogQuery.nameOperator = AUDIT_equal;
    //aLogQuery.eventTypeName = "__Check_Out";
    //aLogQuery.eventTypeNameOperator = AUDIT_equal;

    int colCount = 0; 
    char **colNames = NULL; 
    QRY_user_query_row_t *resRows; 
    IFERR_ABORT(AUDIT_get_records(aLogQuery , 0, NULL, &colCount, &colNames, &resRows)); 

    int ObjectUIDIndx = 0;
    int ObjectTypeIndx = 0;
    int EventTypeNameIndx = 0;
    int LoggedDateIndx = 0;
    int UserDataIndx = 0;

    for (int ii =0; ii < colCount; ii++)
    {
        if(strcmp(colNames[ii], "ObjectUID") == 0)
        {
            ObjectUIDIndx = ii;
        }
        else if(strcmp(colNames[ii], "ObjectType") == 0)
        {
            ObjectTypeIndx = ii;
        }
        else if(strcmp(colNames[ii], "EventTypeName") == 0)
        {
            EventTypeNameIndx = ii;
        }
        else if(strcmp(colNames[ii], "LoggedDate") == 0)
        {
            LoggedDateIndx = ii;
        }
        else if(strcmp(colNames[ii], "UserData") == 0)
        {
            UserDataIndx = ii;
        }
    }

    QRY_user_query_row_t* curRow = NULL;
    QRY_user_query_row_t* nextRow = NULL;
    curRow = resRows;
    while (curRow != NULL)
    {
		char *ObjectUID = curRow->values[ObjectUIDIndx];
		char *ObjectType = curRow->values[ObjectTypeIndx];
		char *EventTypeName = curRow->values[EventTypeNameIndx];
		char *LoggedDate = curRow->values[LoggedDateIndx];
		char *UserData = curRow->values[UserDataIndx];

        
        tag_t object_tag = NULLTAG;
        logical  is_valid = convert_uid_to_tag(ObjectUID, &object_tag);

        if (is_valid)
        {

	        tag_t type = NULLTAG;
	        IFERR_REPORT(TCTYPE_ask_object_type(object_tag, &type));

            char type_name[TCTYPE_name_size_c+1] = "";
            IFERR_REPORT(TCTYPE_ask_name(type, type_name)); 
            char *object_id_string = NULL;
            if(is_WorkspaceObject(object_tag))
            {
               char *object_id_string = NULL;
	           IFERR_REPORT(WSOM_ask_object_id_string(object_tag, &object_id_string));
               printf("\n %s \n", object_id_string);
               if(object_id_string) MEM_free(object_id_string);
            }
        }
        else  printf("\n %s \n", ObjectUID);

        printf(" \t ObjectUID: %s \n", ObjectUID);
        printf(" \t ObjectType: %s \n",  ObjectType);
        printf(" \t EventTypeName: %s  \n",  EventTypeName);
        printf(" \t LoggedDate: %s \n", LoggedDate );
        printf(" \t UserData: %s \n", UserData);

        nextRow = curRow->next;
        curRow = nextRow;


    }
    if (colNames) MEM_free(colNames);
}

