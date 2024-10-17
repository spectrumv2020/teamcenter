
#include <tc/tc_startup.h>
#include <schmgt/schmgt_bridge_itk.h>

static void create_schedule_task_resource_assignment(tag_t scheduleTag, tag_t taskTag, tag_t userTag)
{
    int ifail = ITK_ok;
    
    char *taskUID = NULL;
    ITK__convert_tag_to_uid(taskTag, &taskUID);
    
    char *userUID = NULL;
    ITK__convert_tag_to_uid(userTag, &userUID);  
    
    AssignmentCreate_t createInputs[1];
    createInputs[0].newAssignTag = NULLTAG;
    createInputs[0].taskID = taskUID;
    createInputs[0].assigneeID = userUID;
    createInputs[0].disciplineID = "";
    createInputs[0].assignedPercentage = 30;
    
    int n_createdAssignments = 0;
    tag_t *createdAssignments = NULL;
    ifail = SCHMGT_create_assignments(scheduleTag, 1, createInputs, &n_createdAssignments, &createdAssignments);
    if (ifail != ITK_ok) {/* add your error logic here */}
    
    ECHO("  n_createdAssignments: %d \n", n_createdAssignments);
     
    if(createdAssignments) MEM_free(createdAssignments);   
    if(taskUID) MEM_free(taskUID);
    if(userUID) MEM_free(userUID);
}
