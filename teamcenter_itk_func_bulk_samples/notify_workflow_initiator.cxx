
#include <tc/tc.h>
#include <tc/tc_startup.h>

#include <epm/epm.h>
#include <sa/sa.h>
#include <tc/folder.h>
#include <tccore/aom.h>
#include <tccore/aom_prop.h>

#include <iostream>
#include <Error_Exception.hxx>
#include <base_utils/IFail.hxx>
#include <base_utils/ScopedSmPtr.hxx>
#include <base_utils/TcResultStatus.hxx>

using namespace std;
using namespace Teamcenter;

int notify_initiator(tag_t task)
{
    int ifail = ITK_ok;
    ResultStatus rstat;

    try
    {
        tag_t job = NULLTAG;
        rstat = EPM_ask_job(task, &job);

        tag_t initiator = NULLTAG;
        rstat = AOM_ask_value_tag(job, "owning_user", &initiator);

        tag_t type = NULLTAG;
        rstat = TCTYPE_find_type ("Envelope", "Envelope", &type);

        tag_t create_input_tag = NULLTAG;
        rstat =  TCTYPE_construct_create_input (type, &create_input_tag);

        const char *subject[1] = {"This is the Subject"};
        rstat =  TCTYPE_set_create_display_value(create_input_tag, "object_name", 1, 
					subject);

        const char *comment[1] = {"This is the message body"};
        rstat =  TCTYPE_set_create_display_value(create_input_tag, "object_desc", 1, 
					comment);

        int n_receivers = 1;
        tag_t receivers[1] = {initiator};
        rstat =  AOM_set_value_tags(create_input_tag, "listOfReceivers", n_receivers, 
					receivers);

        tag_t envelope = NULLTAG;
        rstat =  TCTYPE_create_object(create_input_tag, &envelope );

        tag_t group = NULLTAG;
        rstat =  SA_find_group("dba", &group);

        rstat =  AOM_refresh(envelope, true);
        rstat =  AOM_set_ownership(envelope, initiator, group);
        rstat =  AOM_save(envelope);
        rstat =  AOM_refresh(envelope, false);
        rstat =  AOM_unload(envelope);

        tag_t mailbox = NULLTAG;
        rstat =  SA_ask_user_mailbox(initiator, &mailbox);

        rstat =  AOM_refresh(mailbox, true);
        rstat =  FL_insert(mailbox, envelope, 999);
        rstat =  AOM_save(mailbox);
        rstat =  AOM_refresh(mailbox, false);
        rstat =  AOM_unload(mailbox);

    }
    catch( const IFail &e )
    {
        printf("\n\n\n");
        cout << "error " << e.ifail() << endl;
        cout << e.getMessage() << endl;
    }
    return ifail;
}
