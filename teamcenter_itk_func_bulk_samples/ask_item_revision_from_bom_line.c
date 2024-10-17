static tag_t ask_item_revision_from_bom_line(tag_t bom_line)
{
    tag_t
        item_revision = NULLTAG;
    char 
        *item_id = NULL,
        *rev_id = NULL;
    
    ERROR_CHECK(AOM_ask_value_string(bom_line, "bl_item_item_id", &item_id ));
    ERROR_CHECK(AOM_ask_value_string(bom_line, "bl_rev_item_revision_id", 
        &rev_id));
    ERROR_CHECK(ITEM_find_rev(item_id, rev_id, &item_revision));
    if (item_id) MEM_free(item_id);
    if (rev_id) MEM_free(rev_id);
    return item_revision;
}
