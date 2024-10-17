#include <tccore/aom.h>
#include <tccore/aom_prop.h>
#include <tccore/tctype.h>

static void create_company_location(void)
{
    tag_t type_tag = NULLTAG;
    IFERR_REPORT(TCTYPE_find_type("CompanyLocation", "", &type_tag));

    tag_t create_input = NULL;
    IFERR_ABORT(TCTYPE_construct_create_input(type_tag, &create_input));
    IFERR_REPORT(AOM_set_value_string(create_input, "object_name", "Siemens Product Lifecycle Management Software Inc"));
    IFERR_REPORT(AOM_set_value_string(create_input, "city", "Cypress"));
  
    IFERR_REPORT(AOM_set_value_string(create_input, "street", "10824 Hope Street"));
    IFERR_REPORT(AOM_set_value_string(create_input, "state_province", "CA"));

    tag_t company_location = NULLTAG;
    IFERR_REPORT(TCTYPE_create_object(create_input, &company_location));

    IFERR_REPORT(AOM_save(company_location));
}

