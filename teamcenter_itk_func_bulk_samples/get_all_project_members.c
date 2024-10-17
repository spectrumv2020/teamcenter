/*HEAD GET_ALL_PROJECT_MEMBERS CCC ITK */
#include <tccore/aom_prop.h>
static void get_all_project_members(tag_t project_tag, int *n_members, tag_t **project_members)
{
    tag_t project_team_tag = NULLTAG;
    IFERR_REPORT(AOM_ask_value_tag(project_tag, "project_team", &project_team_tag));
    IFERR_REPORT(AOM_ask_value_tags(project_team_tag, "project_members", n_members, project_members));  
}

