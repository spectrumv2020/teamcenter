
#include <tc/tc_startup.h>
#include <itk/mem.h>

static void print_uid(char variable_name[24], tag_t object)
{
    char *uid;       
    ITK__convert_tag_to_uid(object, &uid);
    printf("    %s: %s \n", variable_name, uid);
    MEM_free(uid);
}