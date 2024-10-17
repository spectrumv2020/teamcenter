
extern DLLAPI int register_pre_action(char *type, char *operation, char *extension_point, METHOD_function_t function_ptr)
{
    int number_of_arguments = 1;    
    TC_argument_list_t *user_args = NULL;
    METHOD_id_t  method;

    IFERR_REPORT( METHOD_find_method( type, operation, &method) );
    if (method.id != NULLTAG)
    {
        user_args = (TC_argument_list_t *) MEM_alloc(sizeof(TC_argument_list_t) );

        user_args->number_of_arguments = number_of_arguments;
        user_args->arguments = (TC_argument_t *) MEM_alloc(user_args->number_of_arguments * sizeof(TC_argument_t));

        user_args->arguments[0].type = 1; /* string */
        user_args->arguments[0].array_size = 32;
        user_args->arguments[0].val_union.str_value = extension_point;

        IFERR_REPORT( METHOD_add_action(method, METHOD_pre_action_type, function_ptr, user_args));
        ECHO(("\n Registered Method: %s - %s - %s \n", type, operation, extension_point));
    }
    else
    {
        ECHO(("\n Method not found for %s - %s - %s \n", type, operation, extension_point));
    }

    return ITK_ok;
}

