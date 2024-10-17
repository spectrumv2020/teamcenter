
#include <tc/emh.h>
#include <epm/epm.h>

int action_handler_to_set_condition_result_with_message(EPM_action_message_t msg)
{
    int ifail = ITK_ok;
    bool conditionResult = true;
    
    /* your error logic here */   
     
    if (conditionResult == true)
    {       
        /* Result parameter value is mixed case as shown here */
        ifail = EPM_set_task_result(msg.task, EPM_RESULT_True);
        if (ifail != ITK_ok) { /* your error logic here */ }

        EMH_clear_errors();
        EMH_store_error_s1(EMH_severity_warning, 9500001, "Result is true");
        if (ifail != ITK_ok) { /* your error logic here */ }
    }
    else
    {    
        /* Result parameter value is mixed case as shown here */
        ifail = EPM_set_task_result(msg.task, EPM_RESULT_False);
        if (ifail != ITK_ok) { /* your error logic here */ }  
        
        EMH_clear_errors();
        EMH_store_error_s1(EMH_severity_warning, 9500001, "Result is false");
        if (ifail != ITK_ok) { /* your error logic here */ }        
    }
    /* Must return ITK_ok for the workflow to progress */
    return ifail;
}
