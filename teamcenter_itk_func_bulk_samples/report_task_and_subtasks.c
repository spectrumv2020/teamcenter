#define INDENT(X)  { int yy; for (yy = 0; yy < X; yy++) ECHO(("  ")); }

static void report_epm_state(EPM_state_t state)
{
    switch (state)
    {
        case EPM_unassigned:
            ECHO(("EPM_unassigned"));
        break;
        case EPM_pending:
            ECHO(("EPM_pending"));
        break;
        case EPM_started:
            ECHO(("EPM_started"));
        break;
        case EPM_completed:
            ECHO(("EPM_completed"));
        break;
        case EPM_skipped:
            ECHO(("EPM_skipped"));
        break;
        case EPM_aborted:
            ECHO(("EPM_aborted"));
        break;
        case EPM_suspended:
            ECHO(("EPM_suspended"));
        break;
    }
}

static void report_task_and_subtasks(tag_t task, int indent)
{
    int
        ii,
        n_subtasks;
    tag_t
        *subtasks = NULL;
    char
        task_name[WSO_name_size_c+1];
    EPM_state_t
        state;

    IFERR_RETURN(EPM_ask_name(task, task_name));
    IFERR_REPORT(EPM_ask_state(task, &state));
    INDENT(indent);
    ECHO(("%s is ", task_name));
    report_epm_state(state);

    IFERR_RETURN(EPM_ask_sub_tasks(task, &n_subtasks, &subtasks));
    ECHO((" and has %d subtasks\n", n_subtasks));
    for (ii = 0; ii < n_subtasks; ii++)
        report_task_and_subtasks(subtasks[ii], indent + 1);
    if (n_subtasks > 0) MEM_free(subtasks);
}

