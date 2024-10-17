static int ImanRelationCreate( METHOD_message_t *msg, va_list args )
{
    int ifail = ITK_ok;
    
    /* va_list for GRM_create_msg */
	va_list largs;
    va_copy( largs, args );
    tag_t  primary_object = va_arg(largs, tag_t);
    tag_t  secondary_object = va_arg(largs, tag_t);
    tag_t  relation_type = va_arg(largs, tag_t);
    tag_t  user_data = va_arg(largs, tag_t);
    tag_t  *new_relation = va_arg(largs, tag_t *);
    va_end(largs);


	return ifail;
}