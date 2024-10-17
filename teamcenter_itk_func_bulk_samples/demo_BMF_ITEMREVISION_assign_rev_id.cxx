#include <property/nr.h>

/*
    Note: 
    Custom code for BMF_ITEMREVISION_assign_rev_id must be assigned to COTS ItemRevision.
    The code can check the type and progress accordingly as shown below.
*/
int A2_ItemRevision_BMF_ITEMREVISION_assign_rev_id_BaseAction( METHOD_message_t * msg, va_list args )
{
    int ifail = ITK_ok;
    
    va_list largs;
    va_copy( largs, args );
    tag_t item_tag = va_arg(largs, tag_t);
    tag_t item_type_tag = va_arg(largs, tag_t);
    logical *mod = va_arg(largs, logical *);
    char **id = va_arg(largs, char **);
    va_end( largs );

    char type_name[TCTYPE_name_size_c + 1] = "";
    ifail = TCTYPE_ask_name( item_type_tag, type_name );
    if(ifail != ITK_ok )
    {
        return ifail;
    }

    if (strcmp(type_name, "A2ItemAssignId") == 0)
    {
        char *next_id = NULL;
        /*
            If the next_id is set to NULL either no Naming Rule is defined or
            the Naming Rule does not have a counter.
        */
        ifail = NR_next_value(type_name, "item_revision_id", item_tag,"","","",NULLTAG,"","", &next_id );
        if(ifail != ITK_ok )
        {
            return ifail;
        }

        *id = (char *) MEM_alloc(sizeof(char)*( tc_strlen(next_id) + 1 ) );
        tc_strcpy( *id, next_id);
        *mod = TRUE;
        if(next_id) MEM_free(next_id);
    }
    else
    {
        ifail = ITEM_new_revision_id( item_tag, item_type_tag, mod, id );
    }   
    
    return ITK_ok;
}
