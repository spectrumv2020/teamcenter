#include <epm/epm.h>

void get_target_attachments(EPM_action_message_t msg, int *n_attachs, tag_t **attachs)
{
	/* Target attachment types should use the root task */
	tag_t root_task = NULLTAG;
	IFERR_REPORT(EPM_ask_root_task(msg.task, &root_task));
	IFERR_REPORT(EPM_ask_attachments(root_task, EPM_target_attachment, n_attachs, attachs));
}

