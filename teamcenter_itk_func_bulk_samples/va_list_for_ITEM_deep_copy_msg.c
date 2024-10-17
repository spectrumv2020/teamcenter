extern DLLAPI int item_deep_copy(METHOD_message_t *m, va_list args)
{    
    /* Object Type: ItemRevision - Operation: ITEM_deep_copy */ 
    /* va_list for ITEM_deep_copy_msg */
    tag_t  new_rev = va_arg(args, tag_t); 
    char*  operation = va_arg(args, char*); 
    tag_t  parent_rev = va_arg(args, tag_t);
    int*  copyCount = va_arg(args, int*); 
    ITEM_deepcopy_info_t*  copyInfo = va_arg(args, ITEM_deepcopy_info_t*);
    int*  count = va_arg(args, int*); 
    tag_t**  copied_objects = va_arg(args, tag_t**); 

    ECHO("\n item_deep_copy_post_action \n");
    
    ECHO("   new_rev: %u\n", new_rev);
    ECHO("   operation: %s\n", operation);
    ECHO("   parent_rev: %u\n", parent_rev);
    ECHO("   count: %d\n", *count);
    for (int ii = 0; ii < copyCount; ii++)
    {
        char temp[32] = "";
        sprintf(temp, "copied_objects[%d]", ii);
        ECHO("   copied_objects[%d]: %u\n", (*copied_objects)[ii] );
    }

    return ITK_ok;
}