
static tag_t ask_item_revisions_master_form(tag_t item_revision)
{
    int
        n_secondary_objects = 0;
    tag_t
        relation = NULLTAG,
        *secondary_objects = NULL,
        item_revision_master_form = NULLTAG;

    IFERR_REPORT(GRM_find_relation_type("IMAN_master_form", &relation));
    IFERR_REPORT(GRM_list_secondary_objects_only(item_revision, relation,
        &n_secondary_objects, &secondary_objects));

    /* should always be just one */
    item_revision_master_form = secondary_objects[0];

    if (secondary_objects) MEM_free(secondary_objects);
    return item_revision_master_form;
}

int demo_refactoring_item_create_rev( METHOD_message_t * msg, va_list args )
{
    int ifail = ITK_ok;
    va_list largs;
    va_copy( largs, args );
    tag_t  new_rev_tag  = va_arg(largs, tag_t);
    logical  isNew = va_arg(largs, logical);
    va_end( largs );

    if(isNew == TRUE) /* only execute on create */
    {

        tag_t  item_tag = NULLTAG;
        IFERR_REPORT(ITEM_ask_item_of_rev(new_rev_tag, &item_tag));

        char   *rev_id = NULL;
        IFERR_REPORT(ITEM_ask_rev_id2(new_rev_tag, &rev_id));

        tag_t  item_rev_master_tag = ask_item_revisions_master_form(new_rev_tag);

        /* your code here */

        if(rev_id) MEM_free(rev_id);
    }
    return ifail;
}