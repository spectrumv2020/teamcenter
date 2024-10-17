/*HEAD ITEM_REVISION_SAVE_PRE_ACTION CCC ITK */

extern DLLAPI int item_revision_save_pre_action(METHOD_message_t *msg, va_list args)
{
    ECHO( "\n item_revision_save_pre_action \n");
    ECHO( "\n   ItemRevision - Save - TC_save_msg - TC_save_msg \n")

    int ifail = ITK_ok;
  
    va_list largs;
    va_copy( largs, args );

    tag_t rev = va_arg(largs, tag_t);
    logical  isNew = va_arg(largs, logical);

    va_end( largs );

    if (isNew ) ECHO("     rev: %u - isNew: TRUE\n", rev);
    else ECHO("     rev: %u - isNew: FALSE\n", rev);

    return ifail;
}

