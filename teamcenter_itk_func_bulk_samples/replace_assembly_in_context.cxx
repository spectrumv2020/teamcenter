
/* In Manufacturing Process Planner File-> SaveAs…-> Replace Assembly in Context… */
int BOMViewRevision_fnd0ReplaceOccurrence( METHOD_message_t * msg, va_list args)
{
    ECHO("\n %s \n", __FUNCTION__);
    int ifail = ITK_ok; 

    va_list largs;
    va_copy( largs, args );
    tag_t  occ_tag  = va_arg(largs, tag_t);
    tag_t  child_item_tag  = va_arg(largs, tag_t);
    tag_t  childBomView_tag  = va_arg(largs, tag_t);
    va_end(largs);
    
    
    print_uid("occ_tag", occ_tag);
    print_uid("child_item_tag", child_item_tag);
    if (childBomView_tag != NULLTAG) 
        print_uid("childBomView_tag", childBomView_tag);
    
    return ifail;
}

static void print_uid(char variable_name[24], tag_t object)
{
    char *uid;
    ITK__convert_tag_to_uid(object, &uid);
    printf("    %s: %s \n", variable_name, uid);
    MEM_free(uid);
}

