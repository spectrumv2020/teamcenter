/*HEAD ITEM_CREATE_POST_ACTION CCC ITK */

extern DLLAPI int item_create_post_action(METHOD_message_t *msg, va_list args)
{
    ECHO( "\n item_create_post_action \n");
    ECHO( "\n   Item - Create  - ITEM_create_msg \n");

    int ifail = ITK_ok;

    va_list largs;
    va_copy( largs, args );

    char   *item_id = va_arg(largs, char *);
    char   *item_name = va_arg(largs, char *);
    char   *type_name = va_arg(largs, char *);
    char   *rev_id = va_arg(largs, char *);
    tag_t  *new_item = va_arg(largs, tag_t *);
    tag_t  *new_rev = va_arg(largs, tag_t *);
    tag_t  item_master_form = va_arg(largs, tag_t);
    tag_t  item_rev_master_form = va_arg(largs, tag_t);

    va_end( largs );

    ECHO("   item_id: %s\n", item_id);
    ECHO("   item_name: %s\n", item_name);
    ECHO("   type_name: %s\n", type_name);
    ECHO("   rev_id: %s\n", rev_id);
    ECHO("   new_item: %u\n", *new_item);
    ECHO("   new_rev: %u\n", *new_rev);
    ECHO("   item_master_form: %u\n", item_master_form);
    ECHO("   item_rev_master_form: %u\n", item_rev_master_form);

    return ifail;
}
