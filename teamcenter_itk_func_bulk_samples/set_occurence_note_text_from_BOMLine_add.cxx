
int set_occurence_note_text( METHOD_message_t * /*msg*/, va_list args)
{
     int ifail = ITK_ok;
    /* va_list for BOMLine_add_msg */
    va_list largs;
    va_copy( largs, args );
    tag_t parent_bomline_tag = va_arg(largs, tag_t);
    tag_t item_tag = va_arg(largs, tag_t);
    tag_t item_rev_tag = va_arg(largs, tag_t);
    tag_t bv_tag = va_arg(largs, tag_t);
    char *occType = va_arg(largs, char *);
    tag_t *newBomline_tag = va_arg(largs, tag_t *);
    va_end( largs );
    
    tag_t parent_rev_tag = NULLTAG;    
    IFERR_REPORT(AOM_ask_value_tag(parent_bomline_tag, "bl_revision", &parent_rev_tag));
    int n_bvrs = 0;
    tag_t *bvrs = NULL;
    IFERR_ABORT(ITEM_rev_list_bom_view_revs(parent_rev_tag, &n_bvrs, &bvrs)); 
    if (n_bvrs == NULLTAG)
    {
        ECHO("Parent Revision BVR not found!\n");
        exit (0);
    }
    // only be one bvr for this test case
    tag_t bvr_tag = bvrs[0];
    if (bvrs) MEM_free(bvrs);
    
    tag_t occ_tag = NULLTAG;
    IFERR_REPORT(AOM_ask_value_tag(*newBomline_tag, "bl_occurrence", &occ_tag)); 
    
    tag_t note_type_tag = NULLTAG;
    IFERR_REPORT(PS_find_note_type("UG REF SET", &note_type_tag));        
    IFERR_REPORT(PS_set_occurrence_note_text(bvr_tag, occ_tag, note_type_tag, 
        "New Note Text"));

    return ifail;
}