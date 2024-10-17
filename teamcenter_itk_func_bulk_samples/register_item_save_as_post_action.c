#define ECHO(X)  printf X; IMAN_write_syslog X

extern DLLAPI int item_save_as_post_action(METHOD_message_t *m, va_list args)
{
    /***********  va_list for ITEM_create_from_rev_msg  **********/
    tag_t old_item  = va_arg (args, tag_t);             /* args  */
    tag_t old_rev  = va_arg (args, tag_t);              /* args  */
    char  *new_item_id  = va_arg (args, char*);         /* args  */
    char  *new_rev_id  = va_arg (args, char*);          /* args  */
    tag_t *new_item  = va_arg (args, tag_t*);           /* args  */
    tag_t *new_rev  = va_arg (args, tag_t*);            /* args  */
    char  *new_name  = va_arg (args, char*);            /* args  */
    char  *new_description  = va_arg (args, char*);     /* args  */
    tag_t item_master_form  = va_arg (args, tag_t);     /* args  */
    tag_t item_rev_master_form  = va_arg (args, tag_t); /* args  */
    /**************************************************************/

    ECHO(("old_item = %d\n", old_item));
    ECHO(("old_rev = %d\n", old_rev));
    ECHO(("new_item_id = %s\n", new_item_id));
    ECHO(("new_rev_id = %s\n", new_rev_id));
    ECHO(("new_item = %d\n", *new_item));
    ECHO(("new_rev = %d\n", *new_rev));
    ECHO(("new_name = %s\n", new_name));
    ECHO(("new_description = %s\n", new_description));
    ECHO(("item_master_form = %d\n", item_master_form));
    ECHO(("item_rev_master_form = %d\n", item_rev_master_form));

    /* your post action code here */

    return ITK_ok;
}

extern int register_item_save_as_post_action()
{
    int
        ec = ITK_ok;
    METHOD_id_t
         method;
    TC_argument_list_t
        *user_args = NULL;

    printf("\t register_item_save_post_action");

    ERROR_CHECK( METHOD_find_method( "Item", "ITEM_create_from_rev", &method) );
    if (method.id != NULLTAG)
    {
        ERROR_CHECK( METHOD_add_action(method, METHOD_post_action_type,
            (METHOD_function_t) item_save_as_post_action, user_args));
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
