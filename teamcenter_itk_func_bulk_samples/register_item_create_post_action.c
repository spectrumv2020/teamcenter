extern DLLAPI int item_create_post_action(METHOD_message_t *m, va_list args)
{
    /*************  va_list for ITEM_create_msg  ****************/   
    char *item_id = va_arg(args, char *);             /* args 1 */ 
    char *item_name = va_arg(args, char *);           /* args 2 */
    char *type_name = va_arg(args, char *);           /* args 3 */
    char *rev_id = va_arg(args, char *);              /* args 4 */
    tag_t *new_item = va_arg(args, tag_t *);          /* args 5 */
    tag_t *new_rev = va_arg(args, tag_t *);           /* args 6 */
    tag_t item_master_form = va_arg(args, tag_t);     /* args 7 */
    tag_t item_rev_master_form = va_arg(args, tag_t); /* args 8 */
    /************************************************************/

    printf( "\t item_create_post_action \n");

    /* add your post-action code here */

    return ITK_ok;
}

extern int register_item_create_post_action()
{
    /***  va_list for USER_init_module is void  ***/

    int ec = ITK_ok; 
    METHOD_id_t method;
    TC_argument_list_t *user_args = NULL;

    printf("\t register_item_create_post_action \n");
       
    ERROR_CHECK( METHOD_find_method( "Item", "ITEM_create", &method) );
    if (method.id != NULLTAG)
    {    
        ERROR_CHECK( METHOD_add_action(method, METHOD_post_action_type,
            (METHOD_function_t) item_create_post_actionn, user_args));
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
