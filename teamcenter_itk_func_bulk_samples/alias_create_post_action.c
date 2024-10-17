/*HEAD ALIAS_CREATE_POST_ACTION CCC ITK */

extern DLLAPI int alias_create_post_action(METHOD_message_t *msg, va_list args)
{
    ECHO( "\n alias_create_post_action \n");
    ECHO( "\n   IdentifierRevision - Create - IDENTIFIER_create_alias_msg \n");

    int ifail = ITK_ok;
  
    va_list largs;
    va_copy( largs, args );

    const char* identifier_type = va_arg(largs, const char *);
    tag_t*  new_identifier_tag = va_arg(largs, tag_t*); 

    va_end( largs );

    ECHO("     identifier_type: %s \n", identifier_type);
    ECHO("     new_identifier_tag: %u \n", new_identifier_tag);

    return ifail;
}

