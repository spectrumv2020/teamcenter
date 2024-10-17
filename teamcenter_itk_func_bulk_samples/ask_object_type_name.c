#include <tccore/tctype.h>

void ask_object_type_name(tag_t object, char type_name[TCTYPE_name_size_c+1])
{
    tag_t
        type = NULLTAG;

    IFERR_REPORT(TCYPE_ask_object_type (object, &type));
    IFERR_REPORT(TCTYPE_ask_name (type, type_name)); 
}
