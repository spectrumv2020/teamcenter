
#include <pom/pom/pom.h>

static logical is_stub(tag_t object_tag)
{
    tag_t stub_class = NULLTAG;
    IFERR_REPORT(POM_class_id_of_class("POM_stub", &stub_class));

    tag_t class_tag = NULLTAG;
    IFERR_REPORT(POM_class_of_instance(object, &class_tag));

    logical verdict = FALSE;
    IFERR_REPORT(POM_is_descendant(stub_class, class_tag, &verdict));

    return (verdict);
}

