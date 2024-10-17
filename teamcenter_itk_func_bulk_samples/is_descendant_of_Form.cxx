
#include <pom/pom/pom.h>
static logical is_descendant_of_Form(tag_t object)
{
    tag_t form_class = NULLTAG;
    IFERR_REPORT(POM_class_id_of_class("Form", &form_class));

    tag_t class_tag = NULLTAG;
    IFERR_REPORT(POM_class_of_instance(object, &class_tag));

    logical verdict = FALSE;
    IFERR_REPORT(POM_is_descendant(form_class, class_tag, &verdict));

    return (verdict);
}