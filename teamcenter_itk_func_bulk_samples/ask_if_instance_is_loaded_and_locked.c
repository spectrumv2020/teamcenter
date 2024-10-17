#include <pom/pom/pom.h>
#include <tc/tc.h>
#include <tc/tc_startup.h>

#include <iostream>
#include <Error_Exception.hxx>
#include <base_utils/IFail.hxx>
#include <base_utils/TcResultStatus.hxx>
#include <mld/logging/TcMainLogger.hxx>

using namespace std;
using namespace Teamcenter;
using Teamcenter::Main::logger;

static logical is_instance_loaded(tag_t instance)
{
    int ifail = ITK_ok;
    ResultStatus rstat;
    
    logical is_loaded = FALSE;
    try
    {
        rstat = POM_is_loaded(instance, &is_loaded);
        cout << "    POM_is_loaded -";
        if(is_loaded == TRUE) cout << " true" << endl;
        if(is_loaded == FALSE) cout << " false" << endl;
    }
    catch( const IFail &e )
    {
        printf("\n\n\n");
        cout << "error " << e.ifail() << endl;
        cout << e.getMessage() << endl; 
    }
    return is_loaded;
}

static int ask_instance_lock(tag_t instance)
{
    int ifail = ITK_ok;
    ResultStatus rstat;
    
    int lock_token = POM_no_lock;
    try
    {
        ifail = POM_ask_instance_lock(instance, &lock_token);
        printf("    POM_ask_instance_lock -");
        if (ifail == 515111) 
        {
            printf(" POM_inst_not_loaded  \n");
        }
        
        if (ifail == 515115) 
        {
            printf(" POM_inst_newly_created  \n");
        }
        
        if (ifail == ITK_ok) 
        {       
            switch(lock_token)
            {
                case POM_no_lock:
                    printf(" POM_no_lock\n");
                    break;
                case POM_read_lock:
                    printf(" POM_read_lock\n");
                    break;
                case POM_modify_lock:
                    printf(" POM_modify_lock\n");
                    break;
                case POM_delete_lock:
                    printf(" POM_delete_lock\n");
                    break;              
            }
        }
    }
    catch( const IFail &e )
    {
        cout << endl << endl;
        cout << "error " << e.ifail() << endl;
        cout << e.getMessage() << endl; 
    }
    return lock_token;
}
