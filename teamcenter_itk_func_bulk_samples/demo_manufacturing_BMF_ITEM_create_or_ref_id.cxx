/*
    Define and Assign extension rule on Item - BMF_ITEM_create_or_ref_id - BaseAction
    Note: This extension rule MUST be defined and assigned to Business Object Type Item.

*/
#include <ug_va_copy.h>
#include <me/me.h>
#include <tccore/aom.h>
#include <tccore/aom_prop.h>
#include <tccore/tctype.h>

#include <base_utils/IFail.hxx>
#include <base_utils/TcResultStatus.hxx>
#include <base_utils/ScopedSmPtr.hxx>
#include <mld/logging/TcMainLogger.hxx>

using namespace std;
using namespace Teamcenter;
using Teamcenter::Main::logger;

int S4_BMF_ITEM_create_or_ref_id( METHOD_message_t* /*msg*/, va_list args )
{
    int ifail = ITK_ok;
    ResultStatus stat;
    
    va_list largs;
    va_copy( largs, args );     
    tag_t ebom_node = va_arg(largs, tag_t); // Design
    tag_t ebom_line_node = va_arg(largs, tag_t); // BOMLine
    char *user_data = va_arg(largs, char*);
    int* num_mfk_keys = va_arg(largs, int*);
    char*** mfk_keys = va_arg(largs, char***);
    char*** mfk_values = va_arg(largs, char***);
    tag_t* to_be_created_type = va_arg(largs, tag_t*);
    tag_t* create_input = va_arg(largs, tag_t*);
    va_end( largs );
    
    tag_t part_item_type = NULLTAG;
    tag_t part_rev_type = NULLTAG;
    
    tag_t item_create_input = NULLTAG;
    tag_t revision_create_input = NULLTAG;

    try
    {
        if("Design to be Skipped or Traversed ") /*Design to be Skipped or Traversed*/
        {
            *num_mfk_keys = 1;
            *mfk_keys = (char **) MEM_alloc(sizeof(char *) * (*num_mfk_keys));
            *mfk_values = (char **) MEM_alloc(sizeof(char *) * (*num_mfk_keys));
            if ( *mfk_keys != NULL )
            {
                (*mfk_keys)[0] = (char *)MEM_alloc(sizeof(char) * strlen("SKIP_EBOM_LINES") + 1);//SKIP_EBOM_LINES for skip or TRAVERSE_EBOM_LINES for Traverse
                (*mfk_values)[0] = (char *)MEM_alloc(sizeof(char) * 2);
                if ( (*mfk_keys)[0] && (*mfk_values)[0] ) 
                {
                    tc_strcpy( (*mfk_keys)[0],"SKIP_EBOM_LINES");//TRAVERSE_EBOM_LINES for Traverse
                    tc_strcpy( (*mfk_values)[0],"1");
                }
             }

             return ITK_ok;
        }
        
        stat = TCTYPE_find_type("Part", NULL, &part_item_type);
        stat = TCTYPE_construct_create_input(part_item_type, &item_create_input);
        
        *to_be_created_type = part_item_type;
        
        if("Part Exists") /*If Part exists set item_type, item_id to create_input and return */
        {
            stat = AOM_set_value_string(item_create_input, "item_id", "001234");
            *create_input = item_create_input;
            return ITK_ok;
        }
        else /*Construct create_input for new Part item creation*/
        {   
            stat = TCTYPE_find_type("Part Revision", NULL , &part_rev_type);
            stat = TCTYPE_construct_create_input(part_rev_type, &revision_create_input);
            stat = AOM_set_value_string(revision_create_input, "item_revision_id", "001");
            stat = AOM_set_value_string(revision_create_input, "object_name", "Test");
            stat = AOM_set_value_string(item_create_input, "item_id", "001234");
            stat = AOM_set_value_tag(item_create_input, "revision", revision_create_input);
            *create_input = item_create_input;
            return ITK_ok;          
        }
    }
    catch( const IFail &ex )
    {
        logger()->error( ex.ifail(), ex.getMessage());
        cout << ex.getMessage()  << endl;
        cout << "error " << ex.ifail() << endl;        
    }

    return ifail;
}
