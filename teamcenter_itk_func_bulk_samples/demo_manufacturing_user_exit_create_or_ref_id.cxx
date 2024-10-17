/*
    Define and Assign extension rule on Item - BMF_ITEM_create_or_ref_id - BaseAction
    
    Note: This extension rule MUST be defined and assigned to Business Object Type Item.
    
    Execute the me_create_mbom
    Example:
      me_create_mbom -ebomroot=7441126 -revrule="Latest Working" -mrevrule="Latest Working"
*/
int A2_BMF_ITEM_create_or_ref_id( METHOD_message_t* msg, va_list args )
{
    ECHO("\n BMF_ITEM_create_or_ref_id \n");
    int ifail = ITK_ok;

    tag_t ebom_node = va_arg(args, tag_t);
    tag_t ebom_line_node = va_arg(args, tag_t);
    char *user_data = va_arg(args, char*);
    int* num_mfk_keys = va_arg(args, int*);
    char*** mfk_keys = va_arg(args, char***);
    char*** mfk_values = va_arg(args, char***);
    tag_t* to_be_created_type = va_arg(args, tag_t*);
    tag_t* create_input = va_arg(args, tag_t*);

    // Construct a CreateInput object for Mfg Item Revision
    tag_t mfg_revision_type = NULLTAG;
    TCTYPE_find_type("ItemRevision", "ItemRevision", &mfg_revision_type);
    *to_be_created_type = mfg_revision_type;

    tag_t revision_create_input =  NULLTAG;
    TCTYPE_construct_create_input(mfg_revision_type, &revision_create_input);

    AOM_set_value_string(revision_create_input, "item_revision_id", "AA");
    AOM_set_value_string(revision_create_input, "object_name", "New Mfg Revision");
    AOM_set_value_string(revision_create_input, "object_desc", "New Mfg Revision");

    // Construct a CreateInput object for Mfg Item
    tag_t mfg_item_type = NULLTAG;
    TCTYPE_find_type("Item", "Item", &mfg_item_type);

    tag_t item_create_input = NULLTAG;
    TCTYPE_construct_create_input(mfg_item_type, &item_create_input);

    AOM_set_value_string(item_create_input, "item_id", "NewMfgItem");
    AOM_set_value_string(item_create_input, "object_name", "New Mfg Item");
    AOM_set_value_string(item_create_input, "object_desc", "New Mfg Item");
    AOM_set_value_tag(item_create_input, "revision", revision_create_input);

    *create_input = item_create_input;
    return ifail;
}
