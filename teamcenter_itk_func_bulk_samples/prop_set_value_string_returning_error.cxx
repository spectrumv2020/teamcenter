/*HEAD PROP SET VALUE STRING RETURNING ERROR CCC ITK */

extern DLLAPI int prop_set_value_string_returning_error(METHOD_message_t *m, va_list args)
{
    int ifail = ITK_ok;
    tag_t object_tag  = m->object_tag;

    va_list largs;
    va_copy( largs, args );
        tag_t prop_tag      = va_arg( largs, tag_t );
        char *value =  va_arg( largs, char*);
    va_end( largs );

        /* get property name */
    const char *property_name = NULL;
    METHOD_PROP_MESSAGE_PROP_NAME(m, property_name);

    if(strcmp(value, "bad string") == 0)
    {
        char err_msg[132] = "";
        sprintf(err_msg, "%s cannot be set to \"%s\"!", property_name, value);

        /* put custom error on the stack */
        EMH_store_error_s1(EMH_severity_error, GTAC_custom_error, err_msg);

        /* return PROP_value_not_set */
        ifail = PROP_value_not_set;
    }
    return ifail;
}
