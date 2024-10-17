/*HEAD ITEM_REVISION_DEEP_COPY_PRE_ACTION CCC ITK */

extern DLLAPI int deep_copy_pre_action(METHOD_message_t *m, va_list args)
{
    ECHO("\n deep_copy_pre_action \n");
    ECHO( "\n   ItemRevision - ITEM_deep_copy_msg\n");

    int ifail = ITK_ok;

    va_list largs;
    va_copy( largs, args );

    tag_t  new_rev = va_arg(largs, tag_t); 
    char*  operation = va_arg(largs, char*); 
    tag_t  parent_rev = va_arg(largs, tag_t);
    int*  copyCount = va_arg(largs, int*); 
    ITEM_deepcopy_info_t*  copyInfo = va_arg(largs, ITEM_deepcopy_info_t*);
    int*  count = va_arg(largs, int*); 
    tag_t **copied_objects = va_arg(largs, tag_t**); 

    va_end( largs );


    for (int ii = 0; ii < *count; ii++)
    {
	    char *object_id = NULL;
	    IFERR_REPORT(WSOM_ask_object_id_string((*copied_objects)[ii], &object_id));

	    char *object_type       = NULL;
	    IFERR_REPORT(WSOM_ask_object_type2((*copied_objects)[ii], &object_type));

	    ECHO("   copied_objects[%d]: %u - %s (%s) \n", ii, (*copied_objects)[ii], object_id, object_type);

	    MEM_free(object_id);
	    MEM_free(object_type);
    }

    return ITK_ok;k;
}
