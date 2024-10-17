/*HEAD ITEM_REVISION_SAVE_AS_PRE_CONDITION CCC ITK */

extern DLLAPI int item_rev_save_as_pre_condition(METHOD_message_t *msg, va_list args)
{
    ECHO( "\n item_rev_save_as_pre_condition \n");
    ECHO( "\n   ItemRevision - Save As - ITEM_copy_rev_to_existing_msg \n");

    int ifail = ITK_ok;

    va_list largs;
    va_copy( largs, args );

    tag_t  revTag = va_arg(largs, tag_t);
    char   *new_rev_id = va_arg(largs, char *);
    char   *name = va_arg(largs, char *);
    char   *description = va_arg(largs, char *);
    tag_t  new_item = va_arg(largs, tag_t );
    tag_t  *new_rev = va_arg(largs, tag_t *);
    tag_t  item_rev_master_form = va_arg(largs, tag_t );

    va_end( largs );

    ECHO("    revTag: %u\n", revTag);
    ECHO("    new_rev_id: %s\n", new_rev_id);
    ECHO("    name: %s\n", name);
    ECHO("    description: %s\n", description);
    ECHO("    new_item: %u\n", new_item);
    ECHO("    new_rev: %u\n", *new_rev);
    ECHO("    item_rev_master_form: %u\n", item_rev_master_form);

    return ifail;
}

