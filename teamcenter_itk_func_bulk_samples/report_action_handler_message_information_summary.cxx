/*HEAD REPORT_ACTION_HANDLER_MESSAGE_INFORMATION_SUMMARY.CXX CCC ITK */

#include <epm/epm.h>
extern DLLAPI int report_action_handler_message_information_summary(EPM_action_message_t message)
{

    int ifail = ITK_ok;

    char task_name[WSO_name_size_c+1] = "";
    IFERR_REPORT(EPM_ask_name(message.task, task_name));

    char action[WSO_name_size_c + 1] = "";
    IFERR_REPORT(EPM_ask_action_string(message.action, action));

    char state[WSO_name_size_c + 1] = "";
    IFERR_REPORT(EPM_ask_state_string(message.proposed_state, state));


    tag_t root_task = NULLTAG;
    IFERR_REPORT(EPM_ask_root_task(message.task, &root_task));

    char root_name[WSO_name_size_c+1] = "";
    IFERR_REPORT(EPM_ask_name(root_task, root_name));

    tag_t parent_task = NULLTAG;
    char parent_name[WSO_name_size_c+1] = "None";
    ifail = EPM_ask_parent_task(message.task, &parent_task);
    if (parent_task != NULLTAG)
    {
        IFERR_REPORT(EPM_ask_name(parent_task, parent_name));
    }

    ECHO("\n     Task: %s - Action: %s - State: %s - Parent Task: %s - Root Task: %s\n", 
        task_name, action, state, parent_name, root_name);

    return ifail;
}
