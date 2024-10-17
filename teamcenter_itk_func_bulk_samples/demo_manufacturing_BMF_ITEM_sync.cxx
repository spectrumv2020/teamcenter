/*
    Define and Assign extension rule on Item - BMF_ITEM_sync - BaseAction
    Note: This extension rule MUST be defined and assigned to Business Object Type Item.

*/
#include <ug_va_copy.h>
#include <me/me.h>

 
int S4_BMF_ITEM_sync( METHOD_message_t* /*msg*/, va_list args )
{
	int ifail = ITK_ok;
	
    va_list largs;
    va_copy( largs, args );
	
	/* Tag of the source Item */
	tag_t ebom_item = va_arg(largs, tag_t); 
	
	/* BOMLine of the source Item whose children need to be synchronized. */
    tag_t ebom_line_node = va_arg(largs, tag_t); 
	
	/* Target BOMLine where the sync needs to be done */
	tag_t mbom_line_node  = va_arg(largs, tag_t); 
	
    va_end( largs );
	
	logical run_custom_code = FALSE;
	
	/* your code here */
   
	if (run_custom_code == FALSE) ifail = ME_USER_EXIT_CONTINUE;
	
	if (run_custom_code == TRUE) ifail = ITK_ok;

	return ifail;
}