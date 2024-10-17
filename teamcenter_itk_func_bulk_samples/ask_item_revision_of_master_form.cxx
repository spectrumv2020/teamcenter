
#include <tccore/grm.h>
#include <tccore/grmtype.h>

static tag_t ask_item_revision_of_master_form(tag_t form_tag)
{
    int
        n_primary_object_tags = 0;
    tag_t
        relation = NULLTAG,
        *primary_object_tags = NULL,
        item_revision_tag = NULLTAG;

    IFERR_REPORT(GRM_find_relation_type("IMAN_master_form", &relation));
    IFERR_REPORT(GRM_list_primary_objects_only(form_tag, relation,
        &n_primary_object_tags, &primary_object_tags));

    /* should always be just one */
    item_revision_tag = primary_object_tags[0];

    if (primary_object_tags) MEM_free(primary_object_tags);
    return item_revision_tag;
}
