
#include <pom/pom/pom.h>
#include <sa/user.h>
#include <tccore/aom_prop.h>

static void get_newstuff_folder_contents(int *n_objects, tag_t** objects)
{
    char *user_name_string = NULL;
    tag_t user_tag = NULLTAG;
    IFERR_REPORT(POM_get_user(&user_name_string, &user_tag));
    MEM_free(user_name_string);
    
    tag_t folder = NULLTAG;
    IFERR_REPORT(SA_ask_user_newstuff_folder(user_tag, &folder));  
    IFERR_REPORT(AOM_ask_value_tags(folder, "contents", n_objects, objects));
}