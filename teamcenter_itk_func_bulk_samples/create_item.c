static void create_item(char *item_id, char *rev_id, char *type_name,
    tag_t *new_item, tag_t *new_rev)
{
    tag_t
        old_item = NULLTAG,
        type = NULLTAG;
    char
        item_name[ITEM_name_size_c + 1];

    IFERR_REPORT(ITEM_find_item(item_id, &old_item));
    if (old_item != NULLTAG)
    {
        ECHO("Item %s already exists!\n", item_id);
        return;
    }

    if (strcmp(type_name, ""))
    {
        IFERR_REPORT(TCTYPE_find_type(type_name, "Item", &type));
        if (type == NULLTAG)
        {
            ECHO("Item Type %s does not exist!\n", type_name);
            return;
        }
    }

    ECHO("Creating Item ID: %s\n", item_id);
    IFERR_REPORT(ITEM_create_item( item_id, item_name, type_name, rev_id,
        new_item, new_rev));

    if (*new_item != NULLTAG)
    {
        ECHO("Saving Item ID: %s\n", item_id);
        IFERR_REPORT(ITEM_save_item(*new_item));

        ECHO("Attaching Item ID: %s to Newstuff Folder\n", item_id);
        IFERR_REPORT(FL_user_update_newstuff_folder(*new_item));
    }
}

