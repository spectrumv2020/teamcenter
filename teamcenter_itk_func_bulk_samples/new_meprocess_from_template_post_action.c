/*HEAD NEW_MEPROCESS_FROM_TEMPLATE_POST_ACTION CCC ITK */

extern DLLAPI int meprocess_from_template_post_action(METHOD_message_t *msg, va_list args)
{
    ECHO( "\n me_from_template_post_action \n\n");
    ECHO( "   MEProcessRevision - From Template - ME_clone_template_action_msg \n\n");

    int ifail = ITK_ok;

    va_list largs;
    va_copy( largs, args );
    
    tag_t parent_tag = va_arg(largs, tag_t); 
    tag_t parent_line_tag = va_arg(largs, tag_t);
    tag_t new_object_tag = va_arg(largs, tag_t);
    const char *name = va_arg(largs, const char *);
    const char *desc = va_arg(largs, const char *);
    const char *id = va_arg(largs, const char *);
    const char *revid = va_arg(largs, const char *);

    va_end( largs );

    ECHO("   parent_tag: %u \n", parent_tag);
    ECHO("   parent_line_tag: %u \n", parent_line_tag);
    ECHO("   new_object_tag: %u \n", new_object_tag);
    ECHO("   name: %s \n", name);
    ECHO("   desc: %s \n", desc);
    ECHO("   id: %s \n", id);
    ECHO("   revid: %s \n", revid);

    return ifail;
}

