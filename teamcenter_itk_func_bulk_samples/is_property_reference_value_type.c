/*HEAD IS_PROPERTY_REFERENCE_VALUE_TYPE CCC ITK */
static logical is_property_reference_value_type(tag_t object, char *prop_name)
{
    logical
        is_reference = FALSE;
    char 
        *value_type_name = NULL;  
    PROP_value_type_t 
        value_type;

    IFERR_REPORT(AOM_ask_value_type(object, prop_name, &value_type,
        &value_type_name));
    if ((value_type == PROP_typed_reference) ||  
        (value_type == PROP_untyped_reference) ||  
        (value_type == PROP_external_reference))
    {
        is_reference = TRUE;
    }
    GTAC_free(value_type_name);
    return is_reference;
}


