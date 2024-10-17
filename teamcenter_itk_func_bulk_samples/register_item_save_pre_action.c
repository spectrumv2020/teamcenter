extern DLLAPI int item_save_pre_action(METHOD_message_t *m, va_list args)
{
    /***  va_list for TC_save_msg  ***/
    tag_t item  = va_arg(args, tag_t);
    /***********************************/

    printf( "\t item_save_pre_action - item: %u\n", item);

    /* add your pre-action code here */

    return ITK_ok;
}

extern int register_item_save_pre_action()
{
    int 
        ec = ITK_ok; 
    METHOD_id_t 
         method;
    TC_argument_list_t 
        *user_args = NULL;
      
    printf("\t register_item_save_pre_action");

    ERROR_CHECK( METHOD_find_method( "Item", "IMAN_save", &method) );
    if (method.id != NULLTAG)
    {    
        ERROR_CHECK( METHOD_add_action(method, METHOD_pre_action_type,
            (METHOD_function_t) item_save_pre_action, user_args));
        printf( " - successful!");
    }
    else
    {
        printf(" -  method not found!");
        ec = FAIL;
    }
    printf("\n");
    
	return ec;
}
