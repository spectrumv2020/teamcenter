
#include <pom/pom/pom.h>

logical is_object_descendant_of_class(tag_t tObject, char *pcClassName)
{
    tag_t tParentClass = NULLTAG;
    IFERR_REPORT(POM_class_id_of_class(pcClassName, &tParentClass));

    tag_t tObjectClass = NULLTAG;
    IFERR_REPORT(POM_class_of_instance(tObject, &tObjectClass));

    logical lVerdict = FALSE;
    IFERR_REPORT(POM_is_descendant(tParentClass, tObjectClass, &lVerdict));

    return (lVerdict);
}
