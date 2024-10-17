extern DLLAPI int ugmaster_save_post_action(METHOD_message_t *m, va_list args)
{
    /*** ** va_list for TC_save_msg  *****/
    tag_t  ugmaster  = va_arg(args, tag_t);
    /***************************************/

    printf( "\t ugmaster_save_post_action - ugmaster: %u\n", ugmaster);

    /* ugmaster save post action code here */

    return ITK_ok;
}

extern int register_ugmaster_save_post_action()
{
    int 
        ec = ITK_ok; 
    METHOD_id_t 
         method;
    TC_argument_list_t 
        *user_args = NULL;

    printf("\t register_ugmaster_save_post_action");
       
    ERROR_CHECK( METHOD_find_method( "UGMASTER", "AE_save_dataset", &method) );
    if (method.id != NULLTAG)
    {    
        ERROR_CHECK( METHOD_add_action(method, METHOD_post_action_type,
            (METHOD_function_t) ugmaster_save_post_action, user_args));
        printf( " - successful!");
    }
    else
    {
        TC_write_syslog("\t\t method not found!\n");
        printf(" -  method not found!");
        ec = FAIL;
    }
    printf("\n");

    return ec;
}
