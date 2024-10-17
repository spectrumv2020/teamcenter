
#include <A2gtac/A2disableAssignChildLine.hxx>
#include <string.h>
#include <ug_va_copy.h>
#include <tccore/aom_prop.h>
#include <me/me_errors.h>

/* disable unreferenced formal parameter errors */
#pragma warning(disable: 4100)

int A2disableAssignChildLine( METHOD_message_t *msg, va_list args )
{
	printf("\n BOMLine - fnd0assignChildLine - PreAction \n");

    int ifail = ITK_ok;

	/* va_list for fnd0assignChildLine */
    va_list largs;
    va_copy (largs, args)    ;
    tag_t srcLine = va_arg(largs, tag_t);
    const char *occTypeName = va_arg(largs, const char*);
    tag_t *pasteCandidate =  va_arg(largs, tag_t *);
    va_end(largs);

    tag_t targetLine = msg->object_tag;
    char* targetLineName=0;
    ifail = AOM_ask_value_string(targetLine,"object_string", &targetLineName);
    printf(" Target parent line: %s\n", targetLineName);
    MEM_free(targetLineName);

    char* srcLineName=0;
    ifail = AOM_ask_value_string(srcLine,"object_string", &srcLineName);
    printf(" Source line to be pasted: %s\n", srcLineName);
    MEM_free(srcLineName);

    printf(" Occtype = %s\n", occTypeName?occTypeName:"NULL");

    /* Disables assign if the occtype is not MEConsumed */
    if (!occTypeName || strcmp(occTypeName,"MEConsumed"))
    {
        pasteCandidate=0;
        return ME_assignment_not_supported;
    }
    return ifail;
}