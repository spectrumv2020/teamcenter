/*HEAD REPORT_PROJECT_MEMBERS CCC ITK */
#include <tccore/aom_prop.h>
#include <tccore/project.h>
#include <sa/user.h>
static void report_project_members(tag_t project_tag)
{
    tag_t project_team_tag = NULLTAG;
    IFERR_REPORT(AOM_ask_value_tag(project_tag, "project_team", &project_team_tag));

    int n_members = 0;
    tag_t *project_members = NULL;
    IFERR_REPORT(AOM_ask_value_tags(project_team_tag, "project_members", &n_members, &project_members));    

    int n_author_members = 0;
    tag_t *author_members = NULL;
    IFERR_REPORT(PROJ_ask_author_members(project_tag, &n_author_members, &author_members));
   
    ECHO("\n n_members: %d - n_author_members: %d \n", n_members, n_author_members);
    for(int ii = 0; ii < n_members; ii++)
    {
        char *object_name = NULL;
        IFERR_REPORT(AOM_ask_value_string(project_members[ii], "object_name", &object_name));

        char *user_name = NULL;
        IFERR_REPORT(AOM_ask_value_string(project_members[ii], "user_name", &user_name));

        tag_t user_tag = NULLTAG;
        IFERR_REPORT(SA_find_user(user_name, &user_tag));

        logical is_privileged = FALSE;
        IFERR_REPORT(PROJ_is_user_a_privileged_member(project_tag, user_tag, &is_privileged));

        if (is_privileged == TRUE)
        {
            ECHO("    %s(%s) - Privileged Member \n", object_name, user_name);
        }
        else
        {
            ECHO("    %s(%s) - Regular Member \n", object_name, user_name);
        }
        GTAC_free(object_name);
        GTAC_free(user_name);
    }
    GTAC_free(project_members);
    GTAC_free(author_members);
}

