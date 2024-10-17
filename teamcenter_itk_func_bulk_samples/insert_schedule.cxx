
#include <schmgt/schmgt_bridge_itk.h>

int insert_schedule(tag_t master_schedule_tag, tag_t sub_schedule_tag)
{
	int ifail = ITK_ok;
	
	int number_schedules_to_insert = 1;
    int number_updated_tasks = 0;
    tag_t *updated_tasks = NULL;
	
    SCHMGT_insert_schedule_container_t insert_container;
	insert_container.master_schedule = master_schedule_tag;
	insert_container.sub_schedule = sub_schedule_tag;
	insert_container.master_schedule_task = NULLTAG;
	insert_container.adjust_master_dates = true;
	
	IFERR_ABORT(SCHMGT_insert_schedule(&insert_container, 
										number_schedules_to_insert, 
										&number_updated_tasks, 
										&updated_tasks));
    return ifail;
}

