int ImanRelationCopy(METHOD_message_t *msg,va_list args)
{
	int ifail = ITK_ok;

	va_list largs;
    va_copy( largs, args );
    
    /* va_list for GRM_copy_msg */
    tag_t  primary_object = va_arg(largs, tag_t);
    tag_t  secondary_object = va_arg(largs, tag_t);
    tag_t  relation_type = va_arg(largs, tag_t);
    tag_t  user_data = va_arg(largs, tag_t);
    tag_t  *new_relation = va_arg(largs, tag_t *);
    
    va_end(largs);

    is_instance_in_database("primary_object", primary_object);
    is_instance_in_database("secondary_object", secondary_object);
    is_instance_in_database("*new_relation", *new_relation);
	return ifail;
}