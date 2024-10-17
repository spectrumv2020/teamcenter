/*HEAD VA_LIST_FOR_ITEM_CREATE_MSG CCC ITK */
extern DLLAPI int GTAC_item_create(METHOD_message_t *m, va_list args)
{
    /* Object Type: Item - Operation: Create
       Executed at File-> New-> Item  
    */

    /* va_list for ITEM_create_msg */
    char   *item_id = va_arg(args, char *);
    char   *item_name = va_arg(args, char *);
    char   *type_name = va_arg(args, char *);
    char   *rev_id = va_arg(args, char *);
    tag_t  *new_item = va_arg(args, tag_t *);
    tag_t  *new_rev = va_arg(args, tag_t *);
    tag_t  item_master_form = va_arg(args, tag_t);
    tag_t  item_rev_master_form = va_arg(args, tag_t);

    printf("\n GTAC_item_create\n");

    printf("\titem_id: %s\n", item_id);
    printf("\titem_name: %s\n", item_name);
    printf("\ttype_name: %s\n", type_name);
    printf("\trev_id: %s\n", rev_id);
    printf("\tnew_item: %u\n", *new_item);
    printf("\tnew_rev: %u\n", *new_rev);
    printf("\titem_master_form: %u\n", item_master_form);
    printf("\titem_rev_master_form: %u\n", item_rev_master_form);

    return ITK_ok;
}

