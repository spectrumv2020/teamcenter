
#include <pom/pom/pom.h>
static void get_class_attr_id_by_class_name(
                char class_name[TCTYPE_class_name_size_c + 1], 
                char attr_name[BMF_PROP_NAME_size_c + 1], 
                tag_t *attr_tag )
{
    IFERR_REPORT(POM_attr_id_of_attr(attr_name, class_name, attr_tag));
    MEM_free(class_name);   
}
