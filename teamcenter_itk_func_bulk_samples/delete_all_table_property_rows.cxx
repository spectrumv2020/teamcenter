

#include <iostream>
#include <base_utils/IFail.hxx>
#include <base_utils/ScopedSmPtr.hxx>
#include <base_utils/TcResultStatus.hxx>

using namespace std;
using namespace Teamcenter;

int delete_table_property_rows(tag_t object_tag)
{
    int ifail = ITK_ok;
	ResultStatus rstat;
	try
	{
        rstat = RES_checkout(object_tag, "auto checkout", "", "C:\\temp", RES_EXCLUSIVE_RESERVE);       
        rstat = AOM_lock(object_tag); 
		
		int n_rows = 0;
		scoped_smptr<tag_t> rows;
        rstat =  AOM_ask_table_rows(object_tag, "a2_Paint_Colors_Table", &n_rows, &rows );
		
		// delete all rows
		rstat = AOM_delete_table_rows(object_tag, "a2_Paint_Colors_Table", 0, n_rows);

		rstat = AOM_save(object_tag); 
        rstat = AOM_unlock(object_tag); 
        rstat = RES_checkin(object_tag);	
	}
	catch( const IFail &e )
	{
        cout << "error " << e.ifail() << endl;
        cout << e.getMessage() << endl;
	}
    return ifail;
}
