
#include <sa/groupmember.h>
#include <epm/epm.h>
#include <epm/epm_task_template_itk.h>

static tag_t find_groupmember(char *group_name, char *role_name, char *user_id)
{
	int nGroupMembers = 0;
	tag_t *ptGroupMembers = NULL;	
	IFERR_ABORT(SA_find_groupmember_by_rolename(role_name, group_name, user_id,
		&nGroupMembers, &ptGroupMembers));
	
	/* should only be one since user_id, group_name and role_name are used */
	tag_t tGroupMember = ptGroupMembers[0];	
	if(ptGroupMembers) MEM_free(ptGroupMembers);
	return tGroupMember;
}

static void create_assignment_list(void)
{	
	tag_t tGroupMember1 = find_groupmember("Engineering", "Designer", "user1");
	tag_t tGroupMember2 = find_groupmember("Engineering", "Quality", "user2");
	tag_t tGroupMember3 = find_groupmember("Engineering", "Quality", "user3");
	
	char list_name[] = "AssignList1"; 
	int desc_length = 1;  
	char *desc_array[1] = {"Assignment List 1"};
	
	tag_t proc_template = NULLTAG;
	IFERR_ABORT(EPM_find_process_template("-TwoReviewTasksNoStatus", &proc_template));
	
	int n_subtask_templates = 0;
	tag_t *subtask_templates = NULL;
	IFERR_ABORT(EPM_ask_subtask_templates(proc_template, &n_subtask_templates, &subtask_templates));
	
	logical is_shared = FALSE; 	
	int resource_list_count = 2;		
	EPM_resource_list_t *resourceList = NULL;
	resourceList = (EPM_resource_list_t *)MEM_alloc(resource_list_count * sizeof (EPM_resource_list_t));
	
	/* EPM_resource_list_t for ReviewTask1 */	
	resourceList[0].revQuorum = -100;
	resourceList[0].ackQuorum = -100;
	resourceList[0].waitForUndecidedReviewers = 0;
	
	/* ReviewTask1 has 2 sets of resources/profiles/actions */
	int resource_count = 2;
	resourceList[0].count = resource_count;
	
	resourceList[0].profiles = (tag_t*) MEM_alloc(resource_count * sizeof(tag_t));
	resourceList[0].resources = (tag_t*) MEM_alloc(resource_count * sizeof(tag_t));
	resourceList[0].actions = (int*) MEM_alloc(resource_count * sizeof(int));
	
	resourceList[0].profiles[0] = NULLTAG;
	resourceList[0].resources[0] = tGroupMember1;
	resourceList[0].actions[0] = EPM_Review;

	resourceList[0].profiles[1] = NULLTAG;
	resourceList[0].resources[1] = tGroupMember2;
	resourceList[0].actions[1] = EPM_Review;
	
	/* EPM_resource_list_t for ReviewTask2 */	
	resourceList[1].revQuorum = -100;
	resourceList[1].ackQuorum = -100;
	resourceList[1].waitForUndecidedReviewers = 0;
	
	/* ReviewTask2 has 1 set of resources/profiles/actions */
	resource_count = 1;
	resourceList[1].count = resource_count;
	
	resourceList[1].profiles = (tag_t*) MEM_alloc(resource_count * sizeof(tag_t));
	resourceList[1].resources = (tag_t*) MEM_alloc(resource_count * sizeof(tag_t));
	resourceList[1].actions = (int*) MEM_alloc(resource_count * sizeof(int));
		
	resourceList[1].profiles[0] = NULLTAG;	
	resourceList[1].resources[0] = tGroupMember3;
	resourceList[1].actions[0] = EPM_Review;
		
	tag_t tAssignList = NULLTAG;	
	IFERR_ABORT(EPM_assignment_list_create(list_name, desc_length, desc_array, proc_template, FALSE, 
		resource_list_count, subtask_templates, resourceList, &tAssignList));
		
	if(subtask_templates) MEM_free(subtask_templates);	
}
