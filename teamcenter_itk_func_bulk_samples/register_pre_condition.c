extern DLLAPI int register_pre_condition(char *type, char *operation, METHOD_function_t function_ptr)
{
    METHOD_id_t  method;

    IFERR_REPORT( METHOD_find_method( type, operation, &method) );
    if (method.id != NULLTAG)
    {
        IFERR_REPORT( METHOD_add_pre_condition(method, function_ptr, NULL));
        ECHO(("    Registered Method: %s - %s - Pre-Condition \n", type, operation));
    }
    else
    {
        ECHO(("    Method not found for %s - %s\n", type, operation));
    }
    return ITK_ok;
}

