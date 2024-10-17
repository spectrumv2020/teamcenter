
#include <tccore/aom_prop.h>

int A3BOMLine_fnd0assignChildLine( METHOD_message_t * msg, va_list args )
{
    int ifail = ITK_ok;
    
    /* va_list for fnd0assignChildLine */
    va_list largs;
    va_copy (largs, args)    ;
    tag_t sourceBOMLineTag = va_arg(largs, tag_t);
    char *occTypeName = va_arg(largs, char*);
    tag_t *newChildBOMLineTag =  va_arg(largs, tag_t *);
    va_end(largs);

    tag_t parentBOMLineTag = msg->object_tag;
    char *item_id = NULL;
    IFERR_REPORT(AOM_ask_value_string(parentBOMLineTag, "bl_item_item_id", 
        &item_id ));
    ECHO("\n\n parentBOMLineTag bl_item_item_id: %s \n", item_id);

    if (item_id) MEM_free(item_id);
	return ifail;
}

