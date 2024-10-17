/*HEAD ALIAS_SAVE_POST_ACTION CCC ITK */

extern DLLAPI int alias_save_post_action(METHOD_message_t *msg, va_list args)
{
    ECHO( "\n alias_save_post_action \n");
    ECHO( "\n   IdentifierRev - Save - TC_save_msg - TC_save_msg\n");

    int ifail = ITK_ok;
  
    va_list largs;
    va_copy( largs, args );

    tag_t  new_identifier_tag = va_arg(largs, tag_t); 
    logical  isNew = va_arg(largs, logical);

    va_end( largs );

    if (isNew ) 
    {
        ECHO("     new_identifier_tag: %u - isNew: TRUE\n", new_identifier_tag);
    }
    else 
    {   
        ECHO("     new_identifier_tag: %u - isNew: FALSE\n", new_identifier_tag);
    }

    return ifail;
}

