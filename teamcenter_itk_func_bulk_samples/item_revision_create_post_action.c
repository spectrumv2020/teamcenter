/*HEAD ITEM_REVISION_CREATE_POST_ACTION CCC ITK */

extern DLLAPI int item_rev_create_post_action(METHOD_message_t *msg, va_list args)
{
    ECHO( "\n item_rev_create_post_action \n");
    ECHO( "\n   ItemRevision - Create - ITEM_create_rev_msg \n");

    int ifail = ITK_ok;

    va_list largs;
    va_copy( largs, args );

    tag_t  item = va_arg(largs, tag_t);
    char   *rev_id = va_arg(largs, char *);
    tag_t  *new_rev = va_arg(largs, tag_t *);
    tag_t  item_rev_master_form = va_arg(largs, tag_t );

    va_end( largs );

    ECHO("   item: %u\n", item);
    ECHO("   rev_id: %s\n", rev_id);
    ECHO("   new_rev: %u\n", *new_rev);
    ECHO("   item_rev_master_form: %u\n", item_rev_master_form);

    return ifail;
}
