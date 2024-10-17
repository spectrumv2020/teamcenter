#include <tccore/grm.h>
 
 tag_t create_relation(char relation_type[GRM_relationtype_name_size_c + 1],
		tag_t primary_object_tag, tag_t secondary_object_tag)
{
	tag_t relation_type_tag = NULLTAG;
	IFERR_REPORT(GRM_find_relation_type(relation_type, &relation_type_tag));

	tag_t relation_tag  = NULLTAG;
	IFERR_REPORT(GRM_create_relation(primary_object_tag, secondary_object_tag,
			relation_type_tag, NULLTAG, &relation_tag));
	IFERR_REPORT(GRM_save_relation(relation_tag));
	return relation_tag;
}