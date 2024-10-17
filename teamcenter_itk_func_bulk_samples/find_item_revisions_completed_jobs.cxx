
#include <epm/epm.h>
#include <itk/mem.h>
#include <pom/pom/pom.h>
#include <tccore/workspaceobject.h>

static void find_item_revisions_completed_jobs(tag_t item_revision)
{
   int n_references = 0, *levels = NULL;
   tag_t *reference_tags = NULL;
   char **relations = NULL;
   IFERR_REPORT(WSOM_where_referenced(item_revision, 1, &n_references, &levels, 
        &reference_tags,&relations));
   printf("\n n_references: %d \n", n_references);
   for (int ii = 0; ii < n_references; ii++)
   {
       tag_t class_id = NULLTAG;
       IFERR_REPORT(POM_class_of_instance(reference_tags[ii], &class_id));
       
       char* class_name = NULL;
       IFERR_REPORT(POM_name_of_class(class_id, &class_name));

       if (!strcmp(class_name,"EPMTask"))
       {
           char task_name[WSO_name_size_c+1] = "";
           IFERR_REPORT(EPM_ask_name(reference_tags[ii], task_name));
           
           EPM_state_t state;
           IFERR_REPORT(EPM_ask_state(reference_tags[ii], &state));
           
           char  state_string[WSO_name_size_c+1] = ""; 
           IFERR_REPORT(EPM_ask_state_string(state, state_string));
           printf("%s - %s\n", task_name, state_string);
       }
       if(class_name) MEM_free(class_name);
   }
   if(levels) MEM_free(levels);
   if(reference_tags) (reference_tags);
   if(relations) MEM_free(relations);
}

