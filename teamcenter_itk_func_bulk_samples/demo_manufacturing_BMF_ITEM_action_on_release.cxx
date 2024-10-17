/*
    Define and Assign extension rule on Item - BMF_ITEM_action_on_release - BaseAction
    Note: This extension rule MUST be defined and assigned to Business Object Type Item.

*/
#include <ug_va_copy.h>
#include <me/me.h>

int S4_BMF_ITEM_action_on_release( METHOD_message_t* msg, va_list args )
{
	int ifail = ITK_ok;

    va_list largs;
    va_copy( largs, args );
	tag_t ebom_node = va_arg(largs, tag_t); // Design
    tag_t ebom_line_node = va_arg(largs, tag_t); // BOMLine
	tag_t  mbom_released_object  = va_arg(largs, tag_t); // Released Obj - Item or Item Rev or BVR
    tag_t mbom_line_node = va_arg(largs, tag_t); // BOMLine
    char *user_data = va_arg(largs, char*);
    int sugg_action = va_arg(largs, int); // Suggested action on release
	logical* modify_released =  va_arg(largs, logical*);
	int*  action_on_release = va_arg(largs, int*);
	va_end( largs );
	
	/* Action on Release to update property */
	*action_on_release = 1;	  
	
	/* Set true to enable bypass and do alignment */
	*modify_released = false; 
	
	return ifail;
}


