/*HEAD PROP_ASK_DISPLAY_NAME.CXX CCC ITK */
#include <tccore/aom_prop.h>

/* wrapper to give a more meaningful name to AOM_UIF_ask_name*/ 
static void prop_ask_display_name(tag_t object, char *real_name, char **display_name)
{
    IFERR_REPORT(AOM_UIF_ask_name(object, real_name, display_name));
}

