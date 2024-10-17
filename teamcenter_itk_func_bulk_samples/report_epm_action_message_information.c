
int report_epm_action_message_information(EPM_action_message_t message)
{
    int
        ec = ITK_ok,
        n_iman_args = 0;
    tag_t
        root_task = NULLTAG; 
    char  
        name[WSO_name_size_c+1] = "",
        action_string[WSO_name_size_c + 1] = "",
        proposed_state[WSO_name_size_c + 1] = "";

    IFERR_REPORT(EPM_ask_name(message.task, name));
    printf("    message.task: %u \t task name: %s\n", message.task, name);

    IFERR_REPORT(EPM_ask_action_string(message.action, action_string));
    printf("    message.action: %d \t task action: %s\n", 
        message.action, action_string);

    IFERR_REPORT(EPM_ask_state_string(message.proposed_state, proposed_state));
    printf("    message.proposed_state: %d \t  proposed state: %s\n", 
        message.proposed_state, proposed_state );

	IFERR_REPORT(EPM_ask_root_task( message.task, &root_task));
    if (message.task != root_task)
    {
	    IFERR_REPORT(EPM_ask_name(root_task, name));
        printf("    root_task: %d \t root_task name: %s\n",
            root_task, name);

    }
	return ITK_ok;
}

