
#include <schmgt/schmgt_bridge_itk.h>
#include <schmgt/schmgt_itk.h>

static void create_schedule_task_with_workflow_template(tag_t schedule)
{
    int ifail = ITK_ok;
    
    date_t start_date;
    ifail = ITK_string_to_date( "27-Jun-2016 13:48", &start_date); 

    date_t finish_date;
    ifail = ITK_string_to_date( "27-Jun-2016 13:48", &finish_date);
    
    tag_t wfTemplate = NULLTAG;
    ifail = EPM_find_process_template("TCM Release Process", &wfTemplate);
    if (ifail != ITK_ok) {/* add your error logic here */}

    char* workflowUID = NULL;
    ITK__convert_tag_to_uid(wfTemplate, &workflowUID);
    
    tag_t parentTask = NULLTAG;
    ifail = AOM_ask_value_tag(schedule, "fnd0SummaryTask", &parentTask);
    if (ifail != ITK_ok) {/* add your error logic here */}
    
    AttributeUpdateContainer_t otherAttributes[2];
    otherAttributes[ 0 ].attrName = "workflow_trigger_type";
    otherAttributes[ 0 ].attrType = 4; 
    otherAttributes[ 0 ].attrValue = "2";
    
    otherAttributes[ 1 ].attrName = "workflow_template";
    otherAttributes[ 1 ].attrValue = workflowUID;
    otherAttributes[ 1 ].attrType = 8; //reference

    TaskCreateContainer_t createInputs[ 1 ];
    createInputs[0].newTag = NULLTAG;
    createInputs[0].name = "NewTask";
    createInputs[0].desc =  "";  
    createInputs[0].objectType = "ScheduleTask";
    createInputs[0].start = start_date;
    createInputs[0].finish = finish_date;
    createInputs[0].workEstimate = 480;
    createInputs[0].parent = parentTask;
    createInputs[0].prevSibling = NULLTAG;
    createInputs[0].otherAttributesSize = 2;
    createInputs[0].otherAttributes[0] = otherAttributes[0];
    createInputs[0].otherAttributes[1] = otherAttributes[1];
    
    tag_t *created = NULL;
    int n_updated = 0;
    tag_t *updated = NULL;
    ifail = SCHMGT_create_tasks_non_interactive(schedule, 1, createInputs, &created, &n_updated, &updated);
    if (ifail != ITK_ok) {/* add your error logic here */}
    ECHO("  n_updated: %d \n", n_updated);
  
    if(created) MEM_free(created);
    if(updated) MEM_free(updated);  
    MEM_free(workflowUID);
}
