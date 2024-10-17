
#include <iostream>
#include <epm/epm.h>
#include <tccore/aom_prop.h>

#include <Error_Exception.hxx>
#include <base_utils/IFail.hxx>
#include <base_utils/TcResultStatus.hxx>
#include <mld/logging/TcMainLogger.hxx>

using namespace std;
using Teamcenter::Main::logger;

int get_status_display_name(char *real_name, char **display_name)
{
    int ifail = ITK_ok;
	ResultStatus rstat;
	try
	{        
        tag_t status = NULLTAG;
        rstat = EPM_find_status_type(real_name, &status );

        char localization_status;
        logical master;
        rstat = AOM_UIF_ask_localized_value_string(status, "tasktype_name", "en_US",
                    display_name, &localization_status, &master);
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
