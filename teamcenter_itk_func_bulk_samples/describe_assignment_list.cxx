
#include <epm/epm.h>
#include <epm/epm_task_template_itk.h>
#include <sa/groupmember.h>
#include <tccore/aom_prop.h>
#include <tccore/tctype.h>

void report_logical_value(char *variable_name, logical verdict)
{
        if(verdict == TRUE) printf("  %s: true\n", variable_name);
        if(verdict == FALSE) printf("  %s: false\n", variable_name);
}

static void describe_assignment_list(tag_t tAssignList)
{
	char *list_name = NULL;  
	int desc_length = 0;  
	char **desc_array = NULL;  
	logical is_shared = FALSE;
	tag_t proc_template = NULLTAG;  
	int resource_list_count = 0;  
	tag_t *templates_array = NULL;  
	tag_t *resource_lists = NULL; 
	
	IFERR_ABORT(EPM_assignment_list_describe(tAssignList, &list_name, &desc_length, &desc_array, &is_shared, &proc_template, &resource_list_count, &templates_array, &resource_lists));
	printf("\n  list_name: %s \n", list_name);
	report_logical_value("is_shared", is_shared);
	
	char *templateName = NULL;
	IFERR_REPORT(AOM_ask_value_string(proc_template, "template_name", &templateName));
	printf("  template_name: %s \n", templateName);
	MEM_free(templateName);
	
	printf("  resource_list_count: %d \n", resource_list_count);
	for(int ii = 0; ii < resource_list_count; ii++)
	{
		char *object_string = NULL;
		IFERR_ABORT(AOM_ask_value_string(resource_lists[ii], "object_string", &object_string));
		
		tag_t type_tag = NULLTAG;
		IFERR_ABORT(TCTYPE_ask_object_type(resource_lists[ii], &type_tag));
		
		char type_name[TCTYPE_name_size_c+1] = "";
		IFERR_ABORT(TCTYPE_ask_name(type_tag, type_name));
		
		char *uid = NULL;
		ITK__convert_tag_to_uid(resource_lists[ii], &uid);
		
		printf("\n\n\t resource_lists[%d]: %s - %s (%s)\n", ii, object_string, type_name, uid);
		
		int rev_quorum = 0;
		int ack_quorum = 0; 
		int wait_for_undecided_reviewers = 0; 
		int resource_count = 0;  
		tag_t *resources = NULL; 
		tag_t *profiles = NULL;  
		int *actions = NULL; 
		IFERR_ABORT(EPM_resource_list_describe(resource_lists[ii], &rev_quorum, &ack_quorum, &wait_for_undecided_reviewers, &resource_count, &resources, &profiles, &actions));
		printf("\t rev_quorum: %d \n", rev_quorum);
		printf("\t ack_quorum: %d \n", ack_quorum);
		printf("\t wait_for_undecided_reviewers: %d \n", wait_for_undecided_reviewers);
		printf("\n\t\t resource_count: %d \n", resource_count);
		for(int kk = 0; kk < resource_count; kk++)
		{
			if (profiles[kk] != NULLTAG)
			{
				IFERR_ABORT(AOM_ask_value_string(profiles[kk], "object_string", &object_string));
				IFERR_ABORT(TCTYPE_ask_object_type(profiles[kk], &type_tag));
				IFERR_ABORT(TCTYPE_ask_name(type_tag, type_name));
				ITK__convert_tag_to_uid(profiles[kk], &uid);
				printf("\n\t\t profiles[%d]: %s - %s (%s)\n", kk, object_string, type_name, uid);;
			}
			
			if (resources[kk] != NULLTAG)
			{
				IFERR_ABORT(AOM_ask_value_string(resources[kk], "object_string", &object_string));
				IFERR_ABORT(TCTYPE_ask_object_type(resources[kk], &type_tag));
				IFERR_ABORT(TCTYPE_ask_name(type_tag, type_name));
				ITK__convert_tag_to_uid(resources[kk], &uid);				
				printf("\t\t resources[%d]: %s - %s (%s)\n", kk, object_string, type_name, uid);
			}	
			printf("\t\t actions[%d]: %d \n", kk, actions[kk]);

		}
		MEM_free(resources);
		MEM_free(profiles);
		MEM_free(actions);
		MEM_free(object_string);
		MEM_free(uid);
	}
	
	MEM_free(list_name);
	MEM_free(desc_array);
	MEM_free(templates_array);
	MEM_free(resource_lists);
}