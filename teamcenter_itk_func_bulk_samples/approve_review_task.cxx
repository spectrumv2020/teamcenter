/* 
	This code is only valid in Tc10 and earlier versions 
*/
#include <epm/epm.h>

static void approve_review_task(tag_t perform_signoff_task_tag)
{
    int n_signoffs = 0;
    tag_t *signoffs = NULL;
    IFERR_REPORT(EPM_ask_attachments(perform_signoff_task_tag, 
        EPM_signoff_attachment, &n_signoffs, &signoffs));
        
    tag_t signoff_tag = signoffs[0]; // should only be one     
    IFERR_ABORT(EPM_set_task_decision2(perform_signoff_task_tag, signoff_tag, 
        CR_approve_decision, ""));
	if(signoffs) MEM_free(signoffs);
}
