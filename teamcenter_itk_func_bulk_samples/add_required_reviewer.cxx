
static void add_required_reviewer(tag_t task_tag, tag_t group_member_tag)
{
    int ifail = ITK_ok;
	
    /**************************************************************************
        Possible values for signoff_required:
        * Optional - Signoff decision is not required as long as quorum is met
            and can be manually overridden to Required Modifiable 
                     before perform signoffs task starts.
                     
        * RequiredModifiable - Signoff decision is required even if quorum is
            met and can be manually overridden back to Optional before perform
            signoffs task starts.   
            
        * RequiredUnmodifiable - Signoff decision is required even if quorum is
            met and cannot be manually overridden to Optional before perform 
            signoffs task starts    
    */  
    char signoff_required[] = "RequiredUnmodifiable";   
    
    ifail = EPM_add_reviewers_on_task_with_signoff(task_tag, TRUE, 1, 
				&group_member_tag, signoff_required);
    if(ifail != ITK_ok) {/* add your error logic here */}
    
    ifail = EPM_set_adhoc_signoff_selection_done (task_tag,TRUE);
    if(ifail != ITK_ok) {/* add your error logic here */}
    
    ifail = EPM_trigger_action (task_tag, EPM_complete_action, "Some comment");
    if(ifail != ITK_ok) {/* add your error logic here */}
}
