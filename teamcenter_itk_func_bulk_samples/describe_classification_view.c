/*HEAD DESCRIBE_CLASSIFICATION_VIEW CCC ITK */
static void describe_classification_view(char *class_name, char *view_name)
{
    tag_t
        class_tag = NULLTAG,
        view_tag = NULLTAG;
    int 
        ii = 0,
        nAttributes = 0,  
        *attributeIds = NULL,  /* PR-6326084 */
        *formats = NULL,  
        *flags = NULL; 
    char 
        **annotations = NULL,  /* PR-6326084 */
        **attributeNames = NULL,  
        **shortNames = NULL,  
        **units = NULL,  
        **configBefore = NULL,  
        **configField = NULL,  
        **configAfter = NULL;  

    ICS_init_module();
    IFERR_ABORT(ICS_find_class(class_name, &class_tag));
    IFERR_ABORT(ICS_find_view(class_tag, view_name, &view_tag));

    IFERR_REPORT(ICS_describe_view(view_tag, &nAttributes, &attributeIds /* PR-6326084 */, &formats, 
        &annotations /* PR-6326084 */, &attributeNames, &shortNames, &units, &configBefore, 
        &configField, &configAfter, &flags ));
    ECHO(("\n nAttributes: %d\n", nAttributes));
    for (ii = 0; ii < nAttributes; ii++)
    {       
        ECHO(("    attributeId: %d\n", attributeIds[ii])); /* PR-6326084 */
        ECHO(("    format: %d\n", formats[ii]));
        ECHO(("    annotation: %s\n", annotations[ii])); /* PR-6326084 */
        ECHO(("    attributeName: %s\n", attributeNames[ii]));
        ECHO(("    shortName: %s\n", shortNames[ii]));
        ECHO(("    unit: %s\n", units[ii]));
        ECHO(("    configBefore: %s\n", configBefore[ii]));
        ECHO(("    configField: %s\n", configField[ii]));
        ECHO(("    configAfter: %s\n", configAfter[ii]));
        ECHO(("    flag: %d\n", flags[ii]));
        ECHO(("\n"));
    }
    ICS_exit_module();

    GTAC_free(attributeIds);
    GTAC_free(formats);
    GTAC_free(flags);
	GTAC_free(annotations);
    GTAC_free(attributeNames);
    GTAC_free(units);
    GTAC_free(configBefore);
    GTAC_free(configField);
}

