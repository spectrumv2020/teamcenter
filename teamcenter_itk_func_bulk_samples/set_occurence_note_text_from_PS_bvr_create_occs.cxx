
int set_occurence_note_text( METHOD_message_t * /*msg*/, va_list args)
{
     int ifail = ITK_ok;
    /* va_list for PS_bvr_create_occs_msg */
    va_list largs;
    va_copy( largs, args );   
    tag_t parent_bvr_tag = va_arg(largs, tag_t);         
    tag_t child_item = va_arg(largs, tag_t);         
    tag_t child_bom_view = va_arg(largs, tag_t);    
    int n_occ_tags = va_arg(largs, int);
    tag_t** occ_thread_tags = va_arg(largs, tag_t**);     
    va_end( largs );

    for (int ii = 0;  ii < n_occ_tags; ii++)
    {
        tag_t note_type_tag = NULLTAG;
        IFERR_REPORT(PS_find_note_type("UG REF SET", &note_type_tag));
        IFERR_REPORT(PS_set_occurrence_note_text(parent_bvr_tag, *occ_thread_tags[ii], 
            note_type_tag, "New Note Text"));
    }
    return ifail;
}