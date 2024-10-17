static void create_item_with_mandatory_properties(void)
{
    tag_t item_type_tag = NULLTAG;
    IFERR_ABORT(TCTYPE_find_type("A2Item",  "A2Item", &item_type_tag));

    tag_t rev_type_tag = NULLTAG;
    IFERR_ABORT(TCTYPE_find_type ("A2ItemRevision","A2ItemRevision", &rev_type_tag));

    tag_t item_create_input_tag = NULLTAG;
    IFERR_ABORT(TCTYPE_construct_create_input (item_type_tag, &item_create_input_tag));

    const char *item_id[1] = {"6817989"};
    IFERR_ABORT(TCTYPE_set_create_display_value( item_create_input_tag, 
		"item_id", 1, item_id));

    const char *object_name[1] = {"Some Name"};
    IFERR_ABORT(TCTYPE_set_create_display_value( item_create_input_tag, 
        "object_name", 1, object_name ));

    const char *object_desc[] = {"Some Description "};
    IFERR_ABORT(TCTYPE_set_create_display_value(item_create_input_tag, 
        "object_desc", 1, object_desc ));

    const char *a2_mandatory_prop[] = {"a2_mandatory_prop"};
    IFERR_ABORT(TCTYPE_set_create_display_value( item_create_input_tag, 
        "a2_mandatory_prop", 1, a2_mandatory_prop ));

    tag_t item_tag = NULLTAG;
    IFERR_ABORT(TCTYPE_create_object(item_create_input_tag, &item_tag ));

    IFERR_ABORT(ITEM_save_item(item_tag));
}
