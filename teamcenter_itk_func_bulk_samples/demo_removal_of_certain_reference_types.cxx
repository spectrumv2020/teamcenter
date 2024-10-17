#include <epm/epm.h>
#include <pom/pom/pom.h>
#include <tc/folder.h>
#include <tccore/aom.h>
#include <tccore/grm.h>
#include <tccore/grmtype.h>
#include <tccore/tctype.h>
#include <tccore/workspaceobject.h>

static logical is_descendant_of_folder(tag_t object_tag)
{
    tag_t parent_class = NULLTAG;
    IFERR_REPORT(POM_class_id_of_class("Folder", &parent_class));
        
    tag_t class_tag = NULLTAG;
    IFERR_REPORT(POM_class_of_instance(object_tag, &class_tag));
    
    logical verdict = FALSE;
    IFERR_REPORT(POM_is_descendant(parent_class, class_tag, &verdict));

	return verdict;
}

static void demo_removal_of_certain_reference_types(tag_t object_tag)
{
    int n_references = 0;
	int *levels = NULL;
    tag_t *reference_tags = NULL;
    char **relation_type_name = NULL;
    IFERR_REPORT(WSOM_where_referenced(object_tag, 1, &n_references, &levels, 
		&reference_tags, &relation_type_name));

    for (int ii = 0; ii < n_references; ii++)
    {
		char  type_name[WSO_name_size_c + 1] = "";
		IFERR_REPORT(WSOM_ask_object_type(reference_tags[ii], type_name));

		if (is_descendant_of_folder(reference_tags[ii]))
		{
			tag_t folder = reference_tags[ii];
		    IFERR_REPORT(AOM_refresh(folder, TRUE));
            IFERR_REPORT(FL_remove(folder, object_tag));
            IFERR_REPORT(AOM_save(folder));
            IFERR_REPORT(AOM_refresh(folder, FALSE));
		}
		else if ( (relation_type_name[ii] != NULL) && 
			(strlen(relation_type_name[ii]) > 0 ) )
		{
			tag_t relation_type_tag = NULLTAG;
			IFERR_REPORT(GRM_find_relation_type(relation_type_name[ii],
				&relation_type_tag));
			
			// GRM_delete_relation requires the relation object which we don't have
			tag_t relation_tag = NULLTAG;

			// First try relation using the reference as a primary object
			tag_t primary_object = reference_tags[ii];
			tag_t second_object = object_tag; 
			IFERR_REPORT(GRM_find_relation(primary_object, second_object, 
				relation_type_tag, &relation_tag));

			// If no relation is found try the reference as a secondary object
			if (relation_tag == NULLTAG)
			{
				primary_object = object_tag;
				second_object = reference_tags[ii]; 
				IFERR_REPORT(GRM_find_relation(primary_object, second_object, 
					relation_type_tag, &relation_tag));
			}
			IFERR_REPORT(GRM_delete_relation(relation_tag ));
		}
		else if(strcmp(type_name, "EPMTask") == 0 )
		{
			tag_t task = reference_tags[ii];
			EPM_state_t state;
			IFERR_REPORT(EPM_ask_state(task, &state));

			char state_string[WSO_name_size_c + 1] = "";
            IFERR_REPORT(EPM_ask_state_string(state, state_string));

			if(strcmp(state_string, "Completed") == 0)
			{
				ECHO("\n Can't remove targets of completed jobs! \n");
			}
			else
			{
				ITK_set_bypass(TRUE);
				IFERR_REPORT(EPM_remove_attachments(task, 1, &object_tag));
			}
		}

    }
    if(levels) MEM_free(levels);
    if(reference_tags) MEM_free(reference_tags);
    if(relation_type_name) MEM_free(relation_type_name);
}
