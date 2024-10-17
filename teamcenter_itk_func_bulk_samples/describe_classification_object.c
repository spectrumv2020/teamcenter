/*HEAD DESCRIBE_CLASSIFICATION_OBJECT CCC ITK */

static void describe_classification_object(tag_t ico_tag)
{
    int 
        nAttributes = 0,  
        *unctNumbers = NULL,  
        *unctFormats = NULL,
        ii = 0; 
    tag_t
        view_tag = NULLTAG;
    char 
        **attributeNames = NULL,
        **attributeValues = NULL, 
        **units   = NULL; 

    IFERR_REPORT(ICS_describe_classification_object(ico_tag, &view_tag, 
        &nAttributes, &unctNumbers, &unctFormats, &attributeNames, 
        &attributeValues, &units)); 

    for (ii = 0; ii < nAttributes; ii++)
        ECHO(("%d - %s - %s\n", unctNumbers[ii], attributeNames[ii], 
            attributeValues[ii]));

    GTAC_free(unctNumbers);  
    GTAC_free(unctFormats);  
    GTAC_free(attributeNames);
    GTAC_free(attributeValues);
    GTAC_free(units);   
}

