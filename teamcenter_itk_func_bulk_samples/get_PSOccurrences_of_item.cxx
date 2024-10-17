
#include <stdarg.h>
#include <stdio.h>
#include <stdlib.h>
#include <string.h>
#include <bom/bom.h>
#include <cfm/cfm.h>
#include <pom/pom/pom.h>
#include <tc/tc.h>
#include <tccore/tctype.h>
#include <tccore/uom.h>

static void get_PSOccurrences_of_item(tag_t item_tag, int *n_occs, tag_t **occs)
{
    int n_instances = 0;
    int *instance_levels = 0; 
    int *instance_where_found = 0;
    int n_classes = 0;
    int *class_levels = 0;
    int *class_where_found;
    tag_t *ref_instances = NULL;
    tag_t *ref_classes = NULL;
    IFERR_REPORT(POM_referencers_of_instance(item_tag, 1, POM_in_ds_and_db,
        &n_instances, &ref_instances, &instance_levels,
        &instance_where_found, &n_classes, &ref_classes, &class_levels,
        &class_where_found));

    if (n_instances > 0)
    {
        int count = 0;
        for (int ii = 0; ii < n_instances; ii++)
        {
            tag_t type_tag = NULLTAG;
            IFERR_REPORT(TCTYPE_ask_object_type(ref_instances[ii], &type_tag));

            char type_name[TCTYPE_name_size_c+1] = "";
            IFERR_REPORT(TCTYPE_ask_name(type_tag, type_name));        
            if (!strcmp("PSOccurrence", type_name))
            {
                count++;
                if (count == 1)
                {
                    (*occs) = (tag_t *) MEM_alloc(sizeof(tag_t));
                }
                else
                {
                    (*occs) = (tag_t *) MEM_realloc((*occs), count * sizeof(tag_t));
                }
                (*occs)[count - 1] = ref_instances[ii];
                *n_occs = count;               
            }
        }
        if(instance_levels) MEM_free(instance_levels);
        if(instance_where_found) MEM_free(instance_where_found);
        if(class_levels) MEM_free(class_levels);
        if(ref_classes) MEM_free(ref_classes);
        if(class_where_found) MEM_free(class_where_found);
        if(ref_instances) MEM_free(ref_instances);
    }
}

