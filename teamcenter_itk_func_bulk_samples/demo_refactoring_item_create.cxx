

static tag_t ask_master_form(tag_t object)
{
    int
        n_secondary_objects = 0;
    tag_t
        relation = NULLTAG,
        *secondary_objects = NULL,
        master_form = NULLTAG;

    IFERR_REPORT(GRM_find_relation_type("IMAN_master_form", &relation));
    IFERR_REPORT(GRM_list_secondary_objects_only(object, relation,
        &n_secondary_objects, &secondary_objects));

    /* should always be just one */
    master_form = secondary_objects[0];

    if (secondary_objects) MEM_free(secondary_objects);
    return master_form;
}

int A3RefactorItemCreatePostAction( METHOD_message_t *msg, va_list args )
{
	ECHO("\n A3RefactorItemCreatePostAction (Item - IMAN_save - PostAction) \n");
    int ifail = ITK_ok;

    va_list largs;
    va_copy( largs, args );
    tag_t  new_item  = va_arg(largs, tag_t);
    logical  isNew = va_arg(largs, logical);
    va_end( largs );

    if(isNew == TRUE) /* only execute on create */
    {
    	char *item_id = NULL;
    	IFERR_REPORT(ITEM_ask_id2(new_item, &item_id));
 
    	char *item_name = NULL;
    	IFERR_REPORT(ITEM_ask_name2(new_item, &item_name));

    	char *type_name = NULL;
    	IFERR_REPORT(ITEM_ask_type2(new_item, &type_name));

        tag_t new_rev = NULLTAG;
        IFERR_REPORT(ITEM_ask_latest_rev(new_item , &new_rev));

    	char *rev_id = NULL;
    	IFERR_REPORT(ITEM_ask_rev_id2(new_rev, &rev_id));

    	tag_t item_master_tag = ask_master_form(new_item);
    	tag_t item_rev_master_tag = ask_master_form(new_rev);

    	/* your code here */

    	if(item_id) MEM_free(item_id);
    	if(item_name) MEM_free(item_name);
    	if(rev_id) MEM_free(rev_id);
    	if(type_name) MEM_free(type_name);
    }
    return ifail;
}
