static tag_t prompt_for_existing_item_revision(void)
{
    tag_t
        rev = NULLTAG;
    char
        item_id[ITEM_id_size_c +1],
        rev_id[ITEM_id_size_c + 1];

    do
    {
        printf("\nEnter Item ID: ");
        gets(item_id);
        if (!strcmp(item_id, "")) return NULLTAG;

        printf("Enter %s Rev: ", item_id);
        gets(rev_id);
        if (!strcmp(rev_id, "")) return NULLTAG;

        ECHO("Searching for Item ID %s with Rev ID %s...\n", item_id, rev_id);
        IFERR_REPORT(ITEM_find_rev(item_id, rev_id, &rev));
        if (rev == NULL_TAG)
            ECHO("ItemRev %s/%s was NOT found!\n", item_id, rev_id);
        else
            return rev;

    } while (TRUE);

}

