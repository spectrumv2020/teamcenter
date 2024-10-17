
int A3ItemRevisionDeepCopyPostAction( METHOD_message_t *msg, va_list args )
{
    int ifail;
    
    /* va_list for ITEM_deep_copy_msg */
    va_list largs;
    va_copy(largs, args);
    tag_t  new_rev = va_arg(largs, tag_t);
    char*  operation = va_arg(largs, char*);
    tag_t  parent_rev = va_arg(largs, tag_t);
    int  copyCount = va_arg(largs, int);
    ITEM_deepcopy_info_t*  copyInfo = va_arg(largs, ITEM_deepcopy_info_t*);
    int*  count = va_arg(largs, int*);
    tag_t**  copied_objects = va_arg(largs, tag_t**);
    va_end( largs );

    for(int ii = 0; ii < copyCount; ii++)
    {
        tag_t form_class = NULLTAG;
        IFERR_REPORT(POM_class_id_of_class("Form", &form_class));

        tag_t class_tag = NULLTAG;
        IFERR_REPORT(POM_class_of_instance((*copied_objects)[ii], &class_tag));

        logical verdict = FALSE;
        IFERR_REPORT(POM_is_descendant(form_class, class_tag, &verdict));
        if(verdict == TRUE)
        {
            IFERR_REPORT(AOM_set_value_string((*copied_objects)[ii], 
                "a3_custom_property", ""));
            IFERR_REPORT(AOM_save((*copied_objects)[ii]));
        }
    }
    return ifail;
}

