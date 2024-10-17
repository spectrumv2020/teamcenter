extern DLLAPI int register_post_action(char *type, char *operation, METHOD_function_t function_ptr)
{
    METHOD_id_t  method;

    IFERR_REPORT( METHOD_find_method( type, operation, &method) );
    if (method.id != NULLTAG)
    {
        IFERR_REPORT( METHOD_add_action(method, METHOD_post_action_type, function_ptr, NULL));
        ECHO(("    Registered Method: %s - %s - Post-Action \n", type, operation));
    }
    else
    {
        ECHO(("    Method not found for %s - %s\n", type, operation));
    }
    return ITK_ok;
}

