
#include <tccore/aom.h>
#include <tccore/aom_prop.h>
#include <tccore/tctype.h>

static void create_tracelink(tag_t primary_object, tag_t secondary_object)
{
	tag_t type = NULLTAG;
	IFERR_REPORT(TCTYPE_find_type("FND_TraceLink", NULL, &type));

	tag_t type_create_input = NULLTAG;
	IFERR_REPORT(TCTYPE_construct_create_input(type, &type_create_input));

	IFERR_REPORT(AOM_set_value_tag(type_create_input, "primary_object", 
		primary_object));
	IFERR_REPORT(AOM_set_value_tag(type_create_input, "secondary_object", 
		secondary_object));
	IFERR_REPORT(AOM_set_value_tag(type_create_input, "relation_type", type));

	tag_t tracelink = NULLTAG;
	IFERR_REPORT(TCTYPE_create_object(type_create_input, &tracelink));
	IFERR_REPORT(AOM_save(tracelink));
}
