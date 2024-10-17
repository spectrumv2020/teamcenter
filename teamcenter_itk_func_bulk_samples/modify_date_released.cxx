
#include <pom/pom/pom.h>
static void modify_date_released(tag_t wso_tag, char *new_date_released)
{
    /*
        Note: the current user must be a dba group member to successfully 
        modify Date Released.
    */
    date_t set_date;
    IFERR_ABORT(ITK_string_to_date(new_date_released, &set_date));
    
    tag_t attr_tag = NULLTAG;
    IFERR_REPORT(POM_attr_id_of_attr("date_released", "ItemRevision", &attr_tag)); 
    
    IFERR_ABORT(POM_refresh_instances_any_class(1, &wso_tag, POM_modify_lock));
    IFERR_ABORT(POM_set_attr_date(1, &wso_tag, attr_tag, set_date));  
    IFERR_ABORT(POM_save_instances(1, &wso_tag, true));
}
