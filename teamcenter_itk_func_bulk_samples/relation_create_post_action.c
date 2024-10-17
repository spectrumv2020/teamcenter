/*HEAD RELATION_CREATE_POST_ACTION CCC ITK */

extern DLLAPI int relation_create_post_action(METHOD_message_t *msg, va_list args)
{
    ECHO( "\n relation_create_pre_condition \n");
    ECHO( "\n   ImanRelation - Create - GRM_create_msg \n");

    int ifail = ITK_ok;

    va_list largs;
    va_copy( largs, args );

    tag_t  primary_object = va_arg(largs, tag_t);
    tag_t  secondary_object = va_arg(largs, tag_t);
    tag_t  relation_type = va_arg(largs, tag_t);
    tag_t  user_data = va_arg(largs, tag_t);
    tag_t  *new_relation = va_arg(largs, tag_t *);

    va_end( largs );

    ECHO("   primary_object: %u\n", primary_object);
    ECHO("   secondary_object: %u\n", secondary_object);
    ECHO("   relation_type: %u\n", relation_type);
    ECHO("   user_data: %u\n", user_data);
    ECHO("   new_relation: %u\n", *new_relation);

	return ifail;
}

