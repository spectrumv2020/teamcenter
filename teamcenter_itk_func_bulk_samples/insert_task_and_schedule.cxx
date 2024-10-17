
#include <tc/tc_startup.h>
#include <tccore/aom_prop.h>
#include <schmgt/schmgt_bridge_itk.h>

int insert_task_and_schedule(tag_t master_schedule_tag, tag_t sub_schedule_tag, char *task_id)
{
	int ifail = ITK_ok;
	
	date_t start_date;
    IFERR_ABORT(ITK_string_to_date( "27-Jun-2016 13:48", &start_date));

    date_t finish_date;
    IFERR_ABORT(ITK_string_to_date( "27-Jun-2016 13:48", &finish_date));
	
	tag_t summary_task_tag = NULLTAG;
    IFERR_ABORT(AOM_ask_value_tag(master_schedule_tag, "fnd0SummaryTask", &summary_task_tag));
	
	int num_create = 1;
	TaskCreateContainer_t create_inputs;
	create_inputs.name = task_id;
	create_inputs.desc = "";
	create_inputs.objectType = "ScheduleTask";
	create_inputs.start = start_date;
	create_inputs.finish = finish_date;
	create_inputs.workEstimate = 8;
	create_inputs.parent = summary_task_tag;
	create_inputs.prevSibling  = NULLTAG;	
	create_inputs.otherAttributesSize = 1;
	create_inputs.otherAttributes[0].attrName = "priority";
	create_inputs.otherAttributes[0].attrValue = "1";                
	create_inputs.otherAttributes[0].attrType = 1;
	create_inputs.typedAttrContSize  = 0;
	
	tag_t *created_tasks = NULL;
    int num_updated_tasks = 0;
    tag_t *updated_tasks = NULL;
    IFERR_ABORT(SCHMGT_create_tasks_non_interactive(master_schedule_tag, 
					num_create, &create_inputs, &created_tasks, 
					&num_updated_tasks, &updated_tasks));
					
	tag_t schedule_task_tag = created_tasks[0];
	if(created_tasks) MEM_free(created_tasks);
	if(updated_tasks) MEM_free(updated_tasks);
	
	int number_schedules_to_insert = 1;
	int number_updated_tasks = 0;
	
    SCHMGT_insert_schedule_container_t insert_container;
	insert_container.master_schedule = master_schedule_tag;
	insert_container.sub_schedule = sub_schedule_tag;
	insert_container.master_schedule_task = schedule_task_tag;
	insert_container.adjust_master_dates = true;
	
	IFERR_ABORT(SCHMGT_insert_schedule(&insert_container, 
					number_schedules_to_insert, &number_updated_tasks, 
					&updated_tasks));
	if(updated_tasks) MEM_free(updated_tasks);				
    return ifail;
}
