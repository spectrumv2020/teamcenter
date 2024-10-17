
#include <tccore/aom_prop.h>
#include <epm/epm.h>
#include <epm/signoff.h>
#include <sa/user.h>

static void report_signoff_team(tag_t perform_signoff_task_tag)
{    
    int n_signoffs = 0;
    tag_t *signoffs = NULL;
    IFERR_REPORT(EPM_ask_attachments(perform_signoff_task_tag, EPM_signoff_attachment, &n_signoffs, &signoffs));
    for (int ii = 0; ii < n_signoffs; ii++)
    {
        tag_t member_tag = NULLTAG;
        SIGNOFF_TYPE_t member_type_tag;
        IFERR_ABORT(EPM_ask_signoff_member(signoffs[ii], &member_tag, &member_type_tag));
        if(member_type_tag == SIGNOFF_GROUPMEMBER)
        {
            tag_t user_tag = NULLTAG;
            IFERR_ABORT(SA_ask_groupmember_user(member_tag, &user_tag));
            
            char user_id[SA_user_size_c + 1] = "";
            IFERR_ABORT(SA_ask_user_identifier(user_tag, user_id));
            
            tag_t person_tag = NULLTAG;
            IFERR_ABORT(SA_ask_user_person(user_tag, &person_tag));
            
            char *person_name = NULL;
            IFERR_ABORT(SA_ask_person_name2( person_tag, &person_name));
                        
            printf("\t User: %s - Person: %s\n", user_id, person_name);           
            if(person_name) MEM_free(person_name);
        }        
        else if(member_type_tag == SIGNOFF_RESOURCEPOOL)
        {   
            char *object_string = NULL;
            IFERR_REPORT(AOM_ask_value_string(member_tag, "object_string", &object_string));
            printf("\t Resource Pool: %s \n", object_string);
            if(object_string) MEM_free(object_string);
        }   

    }
    if(signoffs) MEM_free(signoffs);
}
