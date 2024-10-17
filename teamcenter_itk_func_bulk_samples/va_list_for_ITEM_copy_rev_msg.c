/*HEAD VA_LIST_FOR_ITEM_COPY_REV_MSG CCC ITK */
extern DLLAPI int GTAC_item_copy_rev(METHOD_message_t *m, va_list args)
{
    /* Object Type: ItemRevision - Operation: Revise */

    /* va_list for ITEM_copy_rev_msg */
    tag_t  source_rev = va_arg(args, tag_t);
    char   *rev_id = va_arg(args, char *);
    tag_t  *new_rev = va_arg(args, tag_t  *);
    tag_t  item_rev_master_form = va_arg(args, tag_t);

    printf("\n GTAC_item_copy_rev\n");

    printf("\tsource_rev: %u\n", source_rev);
    printf("\trev_id: %s\n", rev_id);
    printf("\tnew_rev: %u\n", *new_rev);
    printf("\titem_rev_master_form: %u\n", item_rev_master_form);

    return ITK_ok;
}
