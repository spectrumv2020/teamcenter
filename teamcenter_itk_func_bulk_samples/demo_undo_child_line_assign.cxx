#include <A2gtac/A2assignChildLinePostAction.hxx>
#include <string.h>
#include <ug_va_copy.h>
#include <tccore/aom_prop.h>
#include <bom/bom.h>

/* disable unreferenced formal parameter errors */
#pragma warning(disable: 4100)

int A2assignChildLinePostAction( METHOD_message_t *msg, va_list args )
{
    printf("\n BOMLine - fnd0assignChildLine - PostAction \n");

    int ifail = ITK_ok;

    /* va_list for fnd0assignChildLine */
    va_list largs;
    va_copy (largs, args)    ;
    tag_t srcLine = va_arg(largs, tag_t);
    const char *occTypeName = va_arg(largs, const char*);
    tag_t *pastedLine =  va_arg(largs, tag_t *);
    va_end(largs);

    tag_t targetLine = msg->object_tag;
    char* targetLineName = NULL;
    ifail = AOM_ask_value_string(targetLine, "object_string", &targetLineName);
    printf(" Target parent line: %s\n", targetLineName);
    MEM_free(targetLineName);

    char* srcLineName = NULL;
    ifail = AOM_ask_value_string(srcLine, "object_string", &srcLineName);
    printf(" Source line to be pasted: %s\n", srcLineName);
    MEM_free(srcLineName);

    printf(" Occtype = %s\n", occTypeName?occTypeName:"NULL");

    char* pastedLineName = NULL;
    ifail = AOM_ask_value_string(*pastedLine, "object_string", &pastedLineName);
    printf(" Pasted line: %s\n", pastedLineName);
    MEM_free(srcLineName);

    /* Just to show that you can undo the previous. */
    ifail = BOM_line_cut(*pastedLine);
    if ( ifail == ITK_ok )
    {
        tag_t winTag = NULLTAG;
        ifail = BOM_line_ask_window(msg->object_tag, &winTag);
        ifail = BOM_save_window(winTag);
    }
    return ifail;
}