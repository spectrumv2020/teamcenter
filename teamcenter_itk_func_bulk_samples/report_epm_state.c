static void report_epm_state(EPM_state_t state)
{
    switch (state)
    {
        case EPM_unassigned:
            ECHO("EPM_unassigned\n");
        break;
        case EPM_pending:
            ECHO("EPM_pending\n");
        break;
        case EPM_started:
            ECHO("EPM_started\n");
        break;
        case EPM_completed:
            ECHO("EPM_completed\n");
        break;
        case EPM_skipped:
            ECHO("EPM_skipped\n");
        break;
        case EPM_aborted:
            ECHO("EPM_aborted\n");
        break;
        case EPM_suspended:
            ECHO("EPM_suspended\n");
        break;
    }
}

