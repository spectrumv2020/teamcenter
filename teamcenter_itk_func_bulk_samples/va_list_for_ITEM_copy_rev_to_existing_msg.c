/*HEAD VA_LIST_FOR_ITEM_COPY_REV_TO_EXISTING_MSG CCC ITK */
extern DLLAPI int GTAC_item_copy_rev_to_existing(METHOD_message_t *m, 
                                                 va_list args)
{
    /* Object Type: ItemRevision - Operation: Save As */

    /* va_list for ITEM_copy_rev_to_existing_msg */
    tag_t  revTag = va_arg(args, tag_t);
    char   *new_rev_id = va_arg(args, char *);
    char   *name = va_arg(args, char *);
    char   *description = va_arg(args, char *);
    tag_t  new_item = va_arg(args, tag_t );
    tag_t  *new_rev = va_arg(args, tag_t *);
    tag_t  item_rev_master_form = va_arg(args, tag_t );

    printf("\n GTAC_item_copy_rev_to_existing\n");

    printf("\trevTag: %u\n", revTag);
    printf("\tnew_rev_id: %s\n", new_rev_id);
    printf("\tname: %s\n", name);
    printf("\tdescription: %s\n", description);
    printf("\tnew_item: %u\n", new_item);
    printf("\tnew_rev: %u\n", *new_rev);
    printf("\titem_rev_master_form: %u\n", item_rev_master_form);

    return ITK_ok;
}
