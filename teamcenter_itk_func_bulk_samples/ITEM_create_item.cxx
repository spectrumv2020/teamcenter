/*
    ITEM_create_item is deprecated and will be removed from Tc11.
    So for everyone's convenience - here is a wrapper replacement
*/
#include <tccore/item.h>
#include <tccore/tctype.h>
#include <tccore/aom.h>
#include <tccore/aom_prop.h>

#define ITEM_create_item GTAC_create_item

static void GTAC_create_item(char *item_id, char *item_name, char *item_type,
                char *rev_id, tag_t *item, tag_t *rev)  
{
    /* Determine the correct name for the Item Revision */
    char *rev_type_name = NULL;
    rev_type_name = (char *) 
        MEM_alloc((strlen(item_type) + strlen("Revision") + 1) * sizeof(char));

    sprintf(rev_type_name, "%sRevision", item_type);
    tag_t rev_type_tag = NULLTAG;
    IFERR_REPORT(TCTYPE_find_type(rev_type_name, NULL, &rev_type_tag));
    if (rev_type_name) MEM_free(rev_type_name);

    /* Construct a CreateInput object for Item Revision */
    tag_t rev_create_input_tag = NULLTAG;
    IFERR_REPORT(TCTYPE_construct_create_input(rev_type_tag, &rev_create_input_tag));
    IFERR_REPORT(AOM_set_value_string(rev_create_input_tag, "item_revision_id",
         rev_id));
    IFERR_REPORT(AOM_set_value_string(rev_create_input_tag, "object_name", item_name));

    /* Construct a CreateInput object for Item */
    tag_t item_type_tag = NULLTAG;
    IFERR_REPORT(TCTYPE_find_type(item_type, NULL, &item_type_tag));

    tag_t item_create_input_tag = NULLTAG;
    IFERR_REPORT(TCTYPE_construct_create_input(item_type_tag, &item_create_input_tag));
    IFERR_REPORT(AOM_set_value_string(item_create_input_tag, "item_id", item_id));
    IFERR_REPORT(AOM_set_value_string(item_create_input_tag, "object_name", item_name));
    IFERR_REPORT(AOM_set_value_tag(item_create_input_tag, "revision",
        rev_create_input_tag));


    IFERR_REPORT(TCTYPE_create_object(item_create_input_tag, item));

    /* the deprecated function ITEM_create_item does not save the item
       TCTYPE_create_object requires AOM_save or AOM_save_with extensions
    */
    IFERR_REPORT(AOM_save_with_extensions(*item));
    IFERR_REPORT(ITEM_ask_latest_rev(*item, rev));
}

