/*HEAD REPORT_ATTRIBUTES_OF_CLASSIFICATION_OBJECT CCC ITK */

static void report_attributes_of_classification_object(tag_t ico_tag)
{
    int
        ii = 0,
        nAttributes = 0;
    char
        **attributeNames = NULL,
        **attributeValues = NULL;
                                 
    IFERR_REPORT(ICS_ask_attributes_of_classification_obj(ico_tag, 
        &nAttributes, &attributeNames, &attributeValues));
        
    ECHO(("ICS_ask_attributes_of_classification_obj nAttributes = %d\n", nAttributes));
    for(ii = 0; ii < nAttributes; ii++)
        ECHO(("\t%s - %s\n", attributeNames[ii], attributeValues[ii]));

    GTAC_free(attributeNames);
    GTAC_free(attributeValues);     
}

