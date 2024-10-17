
#include <string.h>
#include <time.h>
#include <tccore/project.h>

static void get_current_date_and_time(char *date_time)
{
    time_t the_time;
    struct tm *time_ptr;
    char *time_format = "%Y%m%d-%H%M%S";

    the_time = time((time_t *)0);
    time_ptr = localtime (&the_time);
    strftime(date_time, 128, time_format, time_ptr);
}

static void create_project(void)
{
    /* get unique const char* for project id and name*/
    char current_date_time_str[128 + 1] = {"\0"};
    get_current_date_and_time(current_date_time_str);
    const char *const_proj_id = (const char *) &current_date_time_str;

    tag_t project_tag = NULLTAG;
    IFERR_REPORT(PROJ_create_project(const_proj_id, const_proj_id, "",
        &project_tag));
    /* Saving the project is not necessary */
}
