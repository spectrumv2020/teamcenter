
#include <tccore/aom.h>
#include <tccore/tctype.h>

static tag_t revise_item_revision(tag_t item)
{

    tag_t item_revision_type_tag = NULLTAG;
    IFERR_REPORT(TCTYPE_find_type("ItemRevision", NULL, 
        &item_revision_type_tag));

    tag_t new_item_rev_create_input_tag = NULLTAG;
    IFERR_REPORT(TCTYPE_construct_create_input(item_revision_type_tag, 
        &new_item_rev_create_input_tag));

    IFERR_REPORT(AOM_set_value_string(new_item_rev_create_input_tag, 
        "object_name", "new_rev_name"));

    IFERR_REPORT(AOM_set_value_tag(new_item_rev_create_input_tag, "items_tag",
         item)); 
       	
    tag_t new_rev_tag  = NULLTAG;
    IFERR_REPORT(TCTYPE_create_object(new_item_rev_create_input_tag, 
        &new_rev_tag)); 
       
    IFERR_REPORT(AOM_save_with_extensions(new_rev_tag));

    return new_rev_tag;
}

