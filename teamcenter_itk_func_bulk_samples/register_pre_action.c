extern DLLAPI int register_pre_action(char *type, char *message, METHOD_function_t function_ptr)
{
    METHOD_id_t  method;

    IFERR_REPORT( METHOD_find_method( type, message, &method) );
    if (method.id != NULLTAG)
    {
        IFERR_REPORT( METHOD_add_action(method, METHOD_pre_action_type, function_ptr, NULL));
        ECHO(("    Registered Method: %s - %s - Pre-Action \n", type, message));
    }
    else
    {
        ECHO(("   Method not found for %s - %s\n", type, message));
    }
    return ITK_ok;
}

