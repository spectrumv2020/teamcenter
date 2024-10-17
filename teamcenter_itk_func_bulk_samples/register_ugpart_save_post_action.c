extern DLLAPI int ugpart_save_post_action(METHOD_message_t *m, va_list args)
{ 
    /***  va_list for TC_save_msg  ***/
    tag_t ugpart  = va_arg(args, tag_t);
    /***********************************/

    printf( "\t ugpart_save_post_action - ugpart: %u\n", ugpart);

    /* your ugpart post action code here */

    return ITK_ok;
}

extern int register_ugpart_save_post_action()
{
    int 
        ec = ITK_ok; 
    METHOD_id_t 
         method;
    TC_argument_list_t 
        *user_args = NULL;

    printf("\t register_ugpart_save_post_action");
       
    ERROR_CHECK( METHOD_find_method( "UGPART", "AE_save_dataset", &method) );
    if (method.id != NULLTAG)
    {    
        ERROR_CHECK( METHOD_add_action(method, METHOD_post_action_type,
            (METHOD_function_t) ugpart_save_post_action, user_args));
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
