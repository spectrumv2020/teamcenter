
#include <itk/mem.h>
#include <tccore/aom_prop.h>
#include <tccore/workspaceobject.h>
#include <pom/pom/pom.h>

static void report_wso_object_string_and_owning_user(tag_t object)
{
    char *object_string = NULL;
    IFERR_REPORT(WSOM_ask_object_id_string(object, &object_string));

    char *owning_user = NULL;
    IFERR_REPORT(AOM_UIF_ask_value(object, "owning_user", &owning_user));

    ECHO("       %s - %s \n", object_string, owning_user);
    MEM_free(object_string);
    MEM_free(owning_user);
}

static void find_all_dataset_owned_by_user(tag_t user_tag)
{  
    tag_t dataset_class = NULLTAG;
    IFERR_ABORT(POM_class_id_of_class("Dataset", &dataset_class)); 
    
    tag_t owning_user_id = NULLTAG;
    IFERR_ABORT(POM_attr_id_of_attr("owning_user", "Folder", &owning_user_id));
    
    tag_t enq = NULLTAG;
    IFERR_ABORT(POM_create_enquiry_on_tag(dataset_class, owning_user_id,
        POM_contains,  &user_tag, &enq));

    int no_of_hits = 0;
    tag_t *tag_list = NULL;
    IFERR_ABORT(POM_execute_enquiry(enq, &no_of_hits, &tag_list));
    ECHO("\n  no_of_hits: %d \n", no_of_hits);
    for (int ii = 0; ii < no_of_hits; ii++)
        report_wso_object_string_and_owning_user(tag_list[ii]);
    
}
