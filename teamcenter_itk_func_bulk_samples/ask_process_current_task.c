/*HEAD ASK_PROCESS_CURRENT_TASK CCC ITK */
/* Replacement code for deprecated function CR_ask_current_release_level */
int ask_process_current_task(tag_t process, char current_task_name[WSO_name_size_c+1])
{
	
    int  
        retcode = ITK_ok,
        n_subtasks = 0,
        ii = 0,
        started = 0;
    tag_t 
        current_task_tag = NULLTAG,
        root_task = NULLTAG,
        *subtasks = NULL;
    EPM_state_t 
        state;

    IFERR_ABORT(EPM_ask_root_task(process, &root_task));
    
    /* EPM_ask_sub_tasks returns 7007 "Invalid Tag" if no subtasks exist */
    retcode = EPM_ask_sub_tasks(root_task, &n_subtasks, &subtasks);
    if( retcode == ITK_ok )
    {
        started = -1;
        for( ii = 0; ii < n_subtasks && retcode == ITK_ok; ii++ )
        {
            IFERR_REPORT(EPM_ask_state(subtasks[ii], &state));
            if( state == EPM_started)
            {
                started = ii;
                break;
            }
        }

        if( retcode == ITK_ok && started >= 0 )
        {
            current_task_tag = subtasks[started];
        }
    }
    SAFE_MEM_FREE( subtasks );               
    
    if( retcode == ITK_ok )
    {
        if( current_task_tag == NULLTAG )
        {
            strcpy( current_task_name, "" );
            retcode = CR_no_current_release_level;
        }
        else
        {
            retcode = EPM_ask_name( current_task_tag, current_task_name); 
        }
    }
    return retcode;
} 
