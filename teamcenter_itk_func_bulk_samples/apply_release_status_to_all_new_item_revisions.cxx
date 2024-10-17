
#include <ug_va_copy.h>
#include <tccore/releasestatus.h>
#include <tccore/tctype.h>
#include <tccore/aom.h>

/* Placement: ItemRevision - IMAN_save - PostAction */
int A2_ItemRevision_IMAN_save_PostAction( METHOD_message_t * msg, va_list args )
{
    int ifail = ITK_ok; 

    va_list largs;
    va_copy( largs, args );
    tag_t  new_rev_tag  = va_arg(largs, tag_t);
    logical  isNew = va_arg(largs, logical);
    va_end( largs );

    TCTYPE_save_operation_context_t saveContext;
    IFERR_REPORT(TCTYPE_ask_save_operation_context(&saveContext));
    if ( (saveContext == TCTYPE_save_on_create) ||
         (saveContext == TCTYPE_save_on_saveas) ||
         (saveContext == TCTYPE_save_on_revise) )
    {
        tag_t release_stat = NULLTAG;
        IFERR_REPORT(RELSTAT_create_release_status("TCM Released", &release_stat));
        IFERR_REPORT(RELSTAT_add_release_status(release_stat, 1, &new_rev_tag, true));
    }
    return ifail;
}
