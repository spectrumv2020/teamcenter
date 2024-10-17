/*HEAD REPORT_ALL_RELATION_INFO CCC ITK */
void list_displayable_properties_with_value(tag_t object)
{
    logical
        is_displayable = TRUE;
    int
        n_props = 0,
        ii = 0;
    char
        **prop_names = NULL,
        *disp_name = NULL,
        *value = NULL;
    
    IFERR_REPORT(AOM_ask_prop_names(object, &n_props, &prop_names) );
    for( ii = 0; ii < n_props; ii++)
    {
        IFERR_REPORT( AOM_UIF_is_displayable(object, prop_names[ii],
            &is_displayable));
        if (is_displayable == TRUE)
        {
            value = NULL;
            IFERR_REPORT(AOM_UIF_ask_name(object, prop_names[ii], &disp_name));
            IFERR_REPORT(AOM_UIF_ask_value(object, prop_names[ii], &value));
            if ( (value != NULL) && (strlen(value) > 0 ) )
            {
                if (strlen(value) == 1 )
                {
                    if ( strcmp(value, " ") != 0 )
                        ECHO("         %s: %s \n", disp_name, value );
                }
                else
                ECHO("         %s: %s\n",  disp_name, value);
            }
        }
    }
    if (prop_names != NULL) MEM_free(prop_names);
    if (disp_name != NULL) MEM_free(disp_name);
    if (value != NULL) MEM_free(value);
}

static void report_all_relation_info(tag_t object)
{
    int n = 0;
    GRM_relation_t *relation_list;   
    IFERR_REPORT(GRM_list_all_related_objects(object, &n, &relation_list)); 
    for (int ii = 0; ii < n; ii++)
    {
        char *type = NULL;
        tag_t relation = relation_list[ii].the_relation;
        IFERR_REPORT(AOM_ask_value_string(relation, "type_string", &type));
        ECHO("\n\n  Relation Type: %s \n", type);
        GTAC_free(type);

        ECHO("\n      Primary Object Properties... \n");
        list_displayable_properties_with_value(relation_list[ii].primary);

        ECHO("\n     Secondary Object  Properties... \n");
        list_displayable_properties_with_value(relation_list[ii].secondary);
    }
}

