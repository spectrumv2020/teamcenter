/*HEAD REPORT_WSO_OBJECT_STRING_AND_OWNING_USER CCC ITK */

#include <itk/mem.h>
#include <tccore/aom_prop.h>
#include <tccore/workspaceobject.h>

void report_wso_object_string_and_owning_user(tag_t object)
{
    char *object_string = NULL;
    IFERR_REPORT(WSOM_ask_object_id_string(object, &object_string));

    char *owning_user = NULL;
    IFERR_REPORT(AOM_UIF_ask_value(object, "owning_user", &owning_user));

    ECHO("       %s - %s \n", object_string, owning_user);
	MEM_free(object_string);
	MEM_free(owning_user);
}

