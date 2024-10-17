#include <sa/user.h>
#include <pom/pom/pom.h>
#include <tccore/aom.h>

// PR-6954473 User status #defines for POM_set_user_license_status are missing
#define SET_STATUS_ACTIVE 0
#define SET_STATUS_INACTIVE 1

// PR-6433704 License level #defines are missing in pom.h fixed in Tc10.1
#define POM_license_level_author        0
#define POM_license_level_consumer      1
#define POM_license_level_occasional    2
#define POM_license_level_viewer        3
#define POM_license_level_user          4

static void set_user_license_status(char *user_name)
{
    tag_t user_tag = NULLTAG;
    IFERR_REPORT(SA_find_user(user_name, &user_tag));
    IFERR_REPORT(AOM_refresh(user_tag, TRUE));
        
    int new_status = SET_STATUS_ACTIVE;
    int license_level = POM_license_level_occasional;
    int purchased = 0;
    int used = 0; 
    IFERR_REPORT(POM_set_user_license_status(user_tag, new_status, 
        license_level, &purchased, &used));
    IFERR_REPORT(AOM_save(user_tag));
    IFERR_REPORT(AOM_refresh(user_tag, FALSE));
}
