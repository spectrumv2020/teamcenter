#include <ug_va_copy.h>
#include <tccore/item_errors.h>

int BlockRevise( METHOD_message_t *msg, va_list args )
{
	int ifail = ITK_ok;

    /* va_list for ITEM_copy_rev_msg  */
	va_list largs;
    va_copy( largs, args );
    tag_t  source_rev_tag = va_arg(largs, tag_t);
    char   *rev_id = va_arg(largs, char *);
    tag_t  *new_rev_tag = va_arg(largs, tag_t  *);
    tag_t  item_rev_master_tag = va_arg(largs, tag_t);
    va_end( largs );
    
    /* your logic here*/

	if(/* undesired condition exists */)
	{
        /* 919013 is a custom error code and message defined in ue_error.xml */
		EMH_store_error (EMH_severity_error, 919013);
		ifail = ITEM_rev_unable_to_create;
	}
	return ifail;
}
