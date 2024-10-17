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
