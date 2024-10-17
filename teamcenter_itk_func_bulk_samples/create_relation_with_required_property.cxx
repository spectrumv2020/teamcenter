#include <tccore/aom.h>
#include <tccore/aom_prop.h>
#include <tccore/tctype.h>

static void create_relation_with_required_property(tag_t primary_object, 
		tag_t secondary_object, tag_t relation_type)
{
	tag_t grm_type = NULLTAG;
    IFERR_REPORT(TCTYPE_find_type("A2ImanRelation", NULL, &grm_type));

	tag_t grm_create_input = NULLTAG;
	IFERR_REPORT(TCTYPE_construct_create_input(grm_type, &grm_create_input));

	IFERR_REPORT(AOM_set_value_tag(grm_create_input, "primary_object", 
		primary_object));
	IFERR_REPORT(AOM_set_value_tag(grm_create_input, "secondary_object", 
		secondary_object));
	IFERR_REPORT(AOM_set_value_tag(grm_create_input, "relation_type",
		relation_type));
	IFERR_REPORT(AOM_set_value_string(grm_create_input, "a2_required_prop",
		"some string"));

	tag_t relation = NULLTAG;
	IFERR_REPORT(TCTYPE_create_object(grm_create_input, &relation));
	IFERR_REPORT(AOM_save(relation));
}
