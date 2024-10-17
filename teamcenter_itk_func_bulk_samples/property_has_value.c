/*HEAD PROPERTY_HAS_VALUE CCC ITK */
static logical property_has_value(tag_t object_tag, char *prop_name)
{
    logical has_value = FALSE;
    char *value = NULL;

    IFERR_REPORT(AOM_UIF_ask_value(object_tag, prop_name, &value));
    if ( (value != NULL) && (strlen(value) > 0 ) )
    {
        if (strlen(value) == 1 )
        {
            if ( strcmp(value, " ") != 0 ) has_value = TRUE;
        }
        else has_value = TRUE;
    }
    return has_value;
}


