/*HEAD VA_LIST_FOR_ITEM_CREATE_FROM_REV_MSG CCC ITK */

#include <ug_va_copy.h>
#include <tccore/item_msg.h>
extern DLLAPI int gtac_item_create_from_rev(METHOD_message_t *m, va_list args)
{
    ECHO("\n gtac_item_create_from_rev \n");

	va_list largs;
    va_copy( largs, args );

	/* Object Type: Item - Operation: Save As */
    /* va_list for ITEM_create_from_rev_msg */
    tag_t  old_item = va_arg(largs, tag_t);
    tag_t  old_rev = va_arg(largs, tag_t);
    char   *new_item_id = va_arg(largs, char *);
    char   *new_rev_id = va_arg(largs, char *);
    tag_t  *new_item = va_arg(largs, tag_t *);
    tag_t  *new_rev = va_arg(largs, tag_t *);
    char   *new_name = va_arg(largs, char *);
    char   *new_description = va_arg(largs, char *);
    tag_t  item_master_form = va_arg(largs, tag_t );
    tag_t  item_rev_master_form = va_arg(largs, tag_t );
	
    va_end( largs );

    ECHO("    old_item: %u \n", old_item);
    ECHO("    old_rev: %u \n", old_rev);
    ECHO("    new_item_id: %s \n", new_item_id);
    ECHO("    new_rev_id: %s \n", new_rev_id);
    ECHO("    new_item: %u \n", *new_item);
    ECHO("    new_rev: %u \n", *new_rev);
    ECHO("    new_name: %s \n", new_name);
    ECHO("    new_description: %s \n", new_description);
    ECHO("    item_master_form: %u \n", item_master_form);
    ECHO("    item_rev_master_form: %u \n", item_rev_master_form);

    return ITK_ok;
}
