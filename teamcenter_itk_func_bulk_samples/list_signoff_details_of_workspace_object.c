/*HEAD LIST_SIGNOFF_DETAILS_OF_WORKSPACE_OBJECT CCC ITK */
void list_signoff_details_of_workspace_object(tag_t object)
{   
    logical
        verdict = FALSE;
    int
        ii = 0,
        n_statuses = 0,
        n_signoffs = 0,
        kk = 0;
    tag_t
        *statuses = NULL;
    char  
        *object_string = NULL,
        object_type[WSO_name_size_c + 1] = "",
        *status_name = NULL,
        **signers = NULL,
        *date_string = NULL,
        decision_string[3][12] = {"Rejected", "No Decision", "Approved"};
    EPM_decision_t    
        *decisions = NULL;  
    date_t
        *sign_off_dates = NULL;


    verdict = is_workspace_object(object);
    if (verdict == FALSE)
    {
        printf("Object must be Workspace Object!\n");
        exit (1);
    }
    else
    {
        IFERR_REPORT(WSOM_ask_object_id_string(object, &object_string));
        IFERR_REPORT(WSOM_ask_object_type(object, object_type));
        printf("%s  Type: %s \n", object_string, object_type);
        SAFE_MEM_FREE(object_string);

        IFERR_REPORT(WSOM_ask_release_status_list(object, &n_statuses, 
            &statuses));
        printf("  Number of statuses: %d \n\n", n_statuses);
        for (ii = 0; ii < n_statuses; ii++)
        {
            IFERR_REPORT(AOM_ask_name(statuses[ii], &status_name));
            printf("    Status Name: %s\n", status_name);
            SAFE_MEM_FREE(status_name);

            IFERR_REPORT(EPM_ask_signoff_details(object, statuses[ii], 
                &n_signoffs, &decisions, &signers, &sign_off_dates));
            for (kk = 0; kk < n_signoffs; kk++)
            {
                IFERR_REPORT(ITK_date_to_string(sign_off_dates[kk], 
                    &date_string));
                printf("      %s %s %s\n\n", signers[kk], 
                    decision_string[decisions[kk]], date_string);
                SAFE_MEM_FREE(date_string);
            }
            for (kk = 0; kk < n_signoffs; kk++) SAFE_MEM_FREE(signers[kk]);
            SAFE_MEM_FREE(signers);
            SAFE_MEM_FREE(decisions);
            SAFE_MEM_FREE(sign_off_dates);
        }
        SAFE_MEM_FREE(statuses);
    }
}
