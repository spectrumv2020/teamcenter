
#include <pom/pom/pom.h>
static void get_class_attr_id_by_instance_tag(tag_t inst_tag,                 
                char attr_name[BMF_PROP_NAME_size_c + 1], 
                tag_t *attr_tag )
{
    tag_t class_tag = NULLTAG;
    IFERR_REPORT(POM_class_of_instance(inst_tag, &class_tag));
    
    char *class_name = NULL;
    IFERR_REPORT(POM_name_of_class(class_tag, &class_name));  
    
    IFERR_REPORT(POM_attr_id_of_attr(attr_name, class_name, attr_tag));    
    MEM_free(class_name);
}
