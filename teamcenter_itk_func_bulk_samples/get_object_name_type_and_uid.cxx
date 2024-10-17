
static void get_object_name_and_type(tag_t object_tag, char **name, char **type_name, char **uid)
{ 
    ITK__convert_tag_to_uid(object_tag, uid);
    tag_t type = NULLTAG;
    IFERR_REPORT(TCTYPE_ask_object_type(object_tag, &type));
    IFERR_REPORT(TCTYPE_ask_name2(type, type_name)); 

    if(is_WorkspaceObject(object_tag))
    { 
        IFERR_REPORT(WSOM_ask_object_id_string(object_tag, name));
    }
    else
    { 
        //try to guess what a name property might be 
        int ifail = ITK_ok;
        for (int ii = 0; ii < 5; ii++)
        { 
            ifail = AOM_ask_value_string(object_tag, "object_name", name);
            if(ifail != PROP_not_found) break;      
            
            ifail = AOM_ask_value_string(object_tag, "name", name);
            if(ifail != PROP_not_found) break;

            ifail = AOM_ask_value_string(object_tag, "current_name", name);
            if(ifail != PROP_not_found) break;

            ifail = AOM_ask_value_string(object_tag, "user_name", name);
            if(ifail != PROP_not_found) break;

            ifail = AOM_ask_value_string(object_tag, "list_name", name);
            if(ifail != PROP_not_found) break;
        }
    }
    if(strlen(*name) == 0) strcpy(*name, "<null>");
}
