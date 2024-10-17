
#include <epm/epm.h>
#include <lov/lov.h>
#include <tccore/aom_prop.h>

#include <Error_Exception.hxx>
#include <base_utils/IFail.hxx>
#include <base_utils/ScopedSmPtr.hxx>
#include <base_utils/TcResultStatus.hxx>
#include <mld/logging/TcMainLogger.hxx>

using namespace std;
using namespace Teamcenter;
using Teamcenter::Main::logger;

static void report_all_ReleaseStatuses()
{
    int ifail = ITK_ok;
    ResultStatus stat;
    try
    {
        int n_LOVs = 0;
        scoped_smptr<tag_t> LOVs;
        stat = LOV_find( "Fnd0ReleaseStatus", &n_LOVs, &LOVs); 
        for (int ii = 0; ii < n_LOVs; ii++)
        {
            int n_statuses = 0;
            char **real_names;
            stat = LOV_ask_values_string (LOVs[ii], &n_statuses, &real_names);
            for (int kk = 0; kk < n_statuses; kk++)
            {
                tag_t status = NULLTAG;
                stat = EPM_find_status_type(real_names[kk], &status);

                char *display_name = NULL;
                char loc_status;
                logical master;
                stat = AOM_UIF_ask_localized_value_string(status, "tasktype_name", "en_US", &display_name, &loc_status, &master);
                printf("    %s \n", display_name);
                if(display_name) MEM_free(display_name);                    
            }  
            if(real_names) MEM_free(real_names);
        }
    }
    catch( const IFail &e )
    {
        ifail = e.ifail();
        cout << "error " << e.ifail() << endl;
        cout << e.getMessage() << endl; 
        logger()->error(e.ifail(),e.getMessage().c_str());
    }
}
