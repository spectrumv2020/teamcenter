/*HEAD GET_PROJECT_TEAM_TAG CCC ITK */
#include <tccore/aom_prop.h>
static void get_project_team_tag(tag_t project_tag, tag_t *project_team_tag)
{
    IFERR_REPORT(AOM_ask_value_tag(project_tag, "project_team", project_team_tag));
    ECHO("project_team_tag: %u \n", *project_team_tag);
}

