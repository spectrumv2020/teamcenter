
#include <qry/qry.h>
#include <schmgt/schmgt_bridge_itk.h>
static void create_schedule_deliverable(void)
{
	int ifail = ITK_ok;
	tag_t tQuery = NULLTAG;
	QRY_find("Schedules...", &tQuery);
	
	char *entries[1] = {"Schedule Name"};
	char *values[1] = {"9008323"};
	int iNumSchedules = 0;
	tag_t *ptSchedules = NULL;
	IFERR_ABORT(QRY_execute(tQuery, 1, entries, values, &iNumSchedules, &ptSchedules));
	
	SCHMGT_schedule_deliverable_data_t deliverableInput;
	deliverableInput.schedule = ptSchedules[0];  // assuming just one
	deliverableInput.deliverable_type = "Dataset";
	deliverableInput.deliverable_name = "ScheduleDeliverable";
	deliverableInput.deliverable_reference = NULLTAG;

	tag_t *ptDeliverable = NULL;
	IFERR_ABORT(SCHMGT_create_schedule_deliverable(&deliverableInput, 1, &ptDeliverable));
	
	MEM_free(ptDeliverable);
	MEM_free(ptSchedules);
}
