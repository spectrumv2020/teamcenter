
#include <iostream>
#include <tccore/aom.h>
#include <tccore/aom_prop.h>
#include <tccore/item.h>
#include <tccore/tctype.h>
#include <base_utils/IFail.hxx>
#include <base_utils/ScopedSmPtr.hxx>
#include <base_utils/TcResultStatus.hxx>
#include <mld/logging/TcMainLogger.hxx>

using namespace std;
using namespace Teamcenter;
using Teamcenter::Main::logger;

int create_item()
{
    int ifail = ITK_ok;
	ResultStatus rstat;
	try
	{
		
		tag_t type_tag = NULLTAG;
		rstat = TCTYPE_find_type ("ItemRevision Master","ItemRevision Master", &type_tag);
				
		tag_t irmf_create_input_tag = NULLTAG;
		rstat = TCTYPE_construct_create_input (type_tag, &irmf_create_input_tag);
				
		const char *form_name[1] = {"123456/A"};
		rstat = TCTYPE_set_create_display_value( irmf_create_input_tag, "object_name", 1, form_name);
		
		const char *user_data_1[1] = {"Some Value"};
		rstat = TCTYPE_set_create_display_value( irmf_create_input_tag, "user_data_1", 1, user_data_1);
		
		tag_t form_tag = NULLTAG;
		rstat = TCTYPE_create_object(irmf_create_input_tag, &form_tag );
		rstat = AOM_save(form_tag);
		
		rstat = TCTYPE_find_type ("A2ItemRevision","A2ItemRevision", &type_tag);

		tag_t rev_create_input_tag = NULLTAG;
		rstat = TCTYPE_construct_create_input (type_tag, &rev_create_input_tag);
		
		rstat = AOM_set_value_tag(rev_create_input_tag, "item_master_tag", form_tag);
		
		const char *object_desc[] = {"Some Description "};
		rstat = TCTYPE_set_create_display_value(rev_create_input_tag, "object_desc", 1, object_desc );
	
		rstat = TCTYPE_find_type("A2Item",  "A2Item", &type_tag);
		
		tag_t item_create_input_tag = NULLTAG;
		rstat = TCTYPE_construct_create_input (type_tag, &item_create_input_tag);
	
		const char *item_id[1] = {"123456"};
		rstat = TCTYPE_set_create_display_value( item_create_input_tag, "item_id", 1, item_id);

		const char *object_name[1] = {"123456"};
		rstat = TCTYPE_set_create_display_value( item_create_input_tag, "object_name", 1, object_name);
		
		rstat = AOM_set_value_tag(item_create_input_tag, "revision", rev_create_input_tag);
		
		tag_t item_tag = NULLTAG;
		rstat = TCTYPE_create_object(item_create_input_tag, &item_tag );

		rstat = ITEM_save_item(item_tag);
	}
	catch( const IFail &e )
	{
		ifail = e.ifail();
        cout << "error " << e.ifail() << endl;
        cout << e.getMessage() << endl; 
		logger()->error(e.ifail(),e.getMessage().c_str());
	}
    return ifail;
}