static void report_epm_state(EPM_state_t state)
{
    switch (state)
    {
        case EPM_unassigned:
            ECHO("EPM_unassigned");
        break;
        case EPM_pending:
            ECHO("EPM_pending");
        break;
        case EPM_started:
            ECHO("EPM_started");
        break;
        case EPM_completed:
            ECHO("EPM_completed");
        break;
        case EPM_skipped:
            ECHO("EPM_skipped");
        break;
        case EPM_aborted:
            ECHO("EPM_aborted");
        break;
        case EPM_suspended:
            ECHO("EPM_suspended");
        break;
    }
}

static void report_task_state(tag_t task)
{
    char
        task_name[WSO_name_size_c+1];
    EPM_state_t
        state;

    IFERR_RETURN(EPM_ask_name(task, task_name));
    IFERR_REPORT(EPM_ask_state(task, &state));
    ECHO("%s is ", task_name);
    report_epm_state(state);
	ECHO("\n");
}

static void report_referencing_jobs(tag_t object)
{
    int
        cnt = 0,
        ii,
        n_levels,
        n_instances,
        *instance_levels,
        *instance_where_found,
        n_classes,
        *class_levels,
        *class_where_found;
    tag_t
        *ref_instances,
        *ref_classes,
        type = NULLTAG;
    char
        *class_name,
        *name,
        type_name[TCTYPE_name_size_c+1] = "";

    IFERR_REPORT(POM_referencers_of_instance(object, 1, POM_in_ds_and_db,
        &n_instances, &ref_instances, &instance_levels,
        &instance_where_found, &n_classes, &ref_classes, &class_levels,
        &class_where_found));

    if (n_instances > 0)
    {
        for (ii = 0; ii < n_instances; ii++)
        {
            IFERR_REPORT(AOM_ask_name(ref_instances[ii], &name));
            IFERR_REPORT(TCTYPE_ask_object_type(ref_instances[ii], &type));
            IFERR_REPORT(TCTYPE_ask_name(type, type_name));
            if (!strcmp(type_name, "EPMTask"))
            {
                ECHO("%d.  ", ++cnt);
                report_task_state(ref_instances[ii]);
            }

            if (name) MEM_free(name);
        }
        MEM_free(ref_instances);
        MEM_free(instance_levels);
        MEM_free(instance_where_found);
    }

    if (n_classes > 0)
    {
        MEM_free(ref_classes);
        MEM_free(class_levels);
        MEM_free(class_where_found);
    }

}

