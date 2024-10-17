
#include <tc/tc.h>
#include <property/propdesc.h>
#include <tccore/item.h>
#include <tccore/aom_prop.h>
#include <tccore/tctype.h>

static void list_item_revisions_by_creation_date(tag_t item_tag)
{
	int ifail = ITK_ok;
	
	int n_revs = 0;
	tag_t *rev_tags = NULL;
	ifail = ITEM_list_all_revs(item_tag, &n_revs, &rev_tags);
	if (ifail != ITK_ok) { /* your error logic here */ }
	
	tag_t type_tag = NULLTAG;
	ifail = TCTYPE_find_type("ItemRevision", "ItemRevision", &type_tag);
	if (ifail != ITK_ok) { /* your error logic here */ }
	
	tag_t prop_tag = NULLTAG;
	ifail = TCTYPE_ask_property_by_name(type_tag, "creation_date", &prop_tag);
	if (ifail != ITK_ok) { /* your error logic here */ }

	tag_t base_prop_tag = NULLTAG;
	tag_t base_type_tag = NULLTAG;
	ifail = PROPDESC_ask_base_descriptor( prop_tag, &base_prop_tag, &base_type_tag);
	if (ifail != ITK_ok) { /* your error logic here */ }

	int *order = (int *) MEM_alloc(sizeof(int));
	order[0] = 0;
	
	int  n_sorted_tags = 0;
	tag_t *sorted_tags = NULL;	
	ifail = AOM_sort_tags_by_properties(n_revs, rev_tags, 1, &base_prop_tag, order, &n_sorted_tags, &sorted_tags);
	if (ifail != ITK_ok) { /* your error logic here */ }
	for(int ii = 0; ii < n_sorted_tags; ii++)
	{
		char *name = NULL;
		ifail = WSOM_ask_object_id_string(rev_tags[ii], &name);
		if (ifail != ITK_ok) { /* your error logic here */ }
		ECHO("\t %s \n", name);
		if (name) MEM_free(name);
	}
	MEM_free(order);
	if(sorted_tags) MEM_free(sorted_tags);
	if(rev_tags) MEM_free(rev_tags);	
}
