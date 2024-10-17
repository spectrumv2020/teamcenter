

#include <tccore/aom_prop.h>
#include <tccore/grm.h>
#include <tccore/grmtype.h>

extern DLLAPI int demo_refactoring_grm_create(METHOD_message_t *m, va_list args)
{    
    int ifail = ITK_ok;
    
    va_list largs;
    va_copy( largs, args );
    tag_t  new_relation  = va_arg(largs, tag_t);
    logical  isNew = va_arg(largs, logical);
    va_end( largs );

    if(isNew == TRUE) /* only execute on create */
    {
        tag_t primary_object = NULLTAG;
        IFERR_REPORT(GRM_ask_primary (new_relation , &primary_object));

        tag_t secondary_object = NULLTAG;
        IFERR_REPORT(GRM_ask_secondary(new_relation , &secondary_object));

        tag_t relation_type = NULLTAG;
        IFERR_REPORT(GRM_ask_relation_type(new_relation , &relation_type));
        
        tag_t user_data = NULLTAG;
        IFERR_REPORT( AOM_ask_value_tag(new_relation, "user_data", &user_data));      
    }  
    return ifail;
}
