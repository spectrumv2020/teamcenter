
#include <tc/tc_startup.h>
#include <pom/pom/pom.h>
#include <itk/mem.h>
#include <tccore/workspaceobject.h>

logical is_instance_in_database(char *variable_name, tag_t object)
{
    logical exists_in_db = FALSE;
    if(object == NULLTAG) ECHO("\t %s: NULLTAG \n", variable_name);
    else
    {
        char *uid = NULL;
        ITK__convert_tag_to_uid(object, &uid);
        POM_instance_exists(object, &exists_in_db);
        if(exists_in_db == TRUE)
        {
            if(is_WorkspaceObject(object) )
            {
                char *object_id = NULL;
                IFERR_REPORT(WSOM_ask_object_id_string(object, &object_id));
				
                char *object_type   = NULL;
                IFERR_REPORT(WSOM_ask_object_type2(object, &object_type));
				
                ECHO("\t %s: %s - POM_instance_exists: true %s (%s)\n", 
                    variable_name, uid, object_id, object_type);
                if(object_id) MEM_free(object_id);
                if(object_type) MEM_free(object_type);
            }
            else ECHO("\t %s:  %s - POM_instance_exists: true\n", 
                variable_name, uid);
        }
        else ECHO("\t %s - POM_instance_exists: false \n", variable_name);
        if(uid) MEM_free(uid);
    }
    return (exists_in_db);
}