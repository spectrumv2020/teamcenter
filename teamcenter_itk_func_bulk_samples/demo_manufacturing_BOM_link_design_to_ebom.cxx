/*
	Define and Assign extension rule on 
		BOMLine - BOM_link_design_to_ebom - BaseAction
*/		
#include <ug_va_copy.h>
#include <me/me.h>

int S4_BOM_link_design_to_ebom( METHOD_message_t* msg, va_list args )
{
	int ifail = ITK_ok;


    va_list largs;
    va_copy( largs, args );
	tag_t tDesignLine = va_arg(args, tag_t); //Design BOM Line
    tag_t tPartLine = va_arg(args, tag_t); //Part BOM Line
	int iAlignMode  = va_arg(args, int); // Alignment Mode
    va_end( largs );
	
	/*
		Insert your custom logic before making alignment 
		and finally do alignment
	*/
	ifail = ME_align_design_bom(tDesignLine, tPartLine, iAlignMode);  

	return ifail;
}