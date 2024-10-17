
#include <tc/tc_startup.h>
#include <schmgt/schmgt_bridge_itk.h>

int create_schedule(char *schedule_type, char *schedule_id, tag_t *schedule_tag)
{
	int ifail = ITK_ok;
	
	date_t start_date;
    IFERR_ABORT(ITK_string_to_date( "27-Jun-2016 13:48", &start_date));

    date_t finish_date;
    IFERR_ABORT(ITK_string_to_date( "27-Jun-2016 13:48", &finish_date));
	
    SCHMGT_new_schedule_container_t create_inputs;
    
    create_inputs.id = schedule_id;
    create_inputs.name = schedule_id;
    create_inputs.description = schedule_id;
    create_inputs.customer_name = NULL;
    create_inputs.customer_number = NULL;
    create_inputs.bill_code = NULL;
    create_inputs.bill_sub_code = NULL;
    create_inputs.bill_type = NULL;
    create_inputs.bill_rate = NULLTAG;
    create_inputs.start_date = start_date;
    create_inputs.finish_date = finish_date;
    create_inputs.priority = 0;
    create_inputs.dates_linked = false;
    create_inputs.published = true;
    create_inputs.notifications_enabled = true;
    create_inputs.percent_linked = false;
    create_inputs.is_template = false;
    create_inputs.is_public = true;
    create_inputs.type = schedule_type;
    create_inputs.other_attributes_size = 0;
    create_inputs.typed_attribute_container_size = 0;
	
	int number_schedules_create = 1;
	tag_t *created_schedules = NULL;
    IFERR_ABORT(SCHMGT_create_new_schedule(&create_inputs, 1, &created_schedules));
    *schedule_tag = created_schedules[0];
	if(created_schedules) MEM_free(created_schedules);

    return ifail;
}
