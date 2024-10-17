
#include <itk/mem.h>
#include <tccore/workspaceobject.h>

static void report_referencers_of_workspaceobject(tag_t wso)
{
    int n_references = 0, *levels = NULL;
    tag_t *reference_tags = NULL;
    char **relations = NULL;
    IFERR_REPORT(WSOM_where_referenced(wso, 1 , &n_references, &levels, 
        &reference_tags,&relations));
    printf("\n n_references: %d \n", n_references);
    for (int ii = 0; ii < n_references; ii++)
    {
        char *id = NULL;
        IFERR_REPORT(WSOM_ask_object_id_string(reference_tags[ii], &id));

        char  type[WSO_name_size_c + 1] = "";
        IFERR_REPORT(WSOM_ask_object_type(reference_tags[ii], type));
        if ( (relations[ii] != NULL) && (strlen(relations[ii]) > 0 ) )
        {
            printf("   %s (%s) - Relation: %s \n", id, type, relations[ii]); 
        }
        else printf("   %s (%s) \n", id, type); 
        MEM_free(id);
    }
    MEM_free(levels);
    MEM_free(reference_tags);
    MEM_free(relations);
}

