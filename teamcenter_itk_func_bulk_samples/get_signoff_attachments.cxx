#include <epm/epm.h>

void get_signoff_attachments(tag_t task, int *n_attachs, tag_t **attachs)
{
	IFERR_REPORT(EPM_ask_attachments(task, EPM_signoff_attachment, n_attachs, attachs));
}
