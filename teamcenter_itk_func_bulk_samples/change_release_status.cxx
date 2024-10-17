
#include <tccore/aom.h.h>
#include <tccore/releasestatus.h.h>
#include <tccore/workspaceobject.h.h>

static void change_release_status(tag_t wso_tag, char *old_release_status, char *new_release_status)
{
    int n_statuses = 0;
    tag_t *statuses  = NULL;
    ifail = WSOM_ask_release_status_list(wso_tag, &n_statuses , &statuses);
    if (ifail != ITK_ok) { /* your error logic here */ }
    
    for(int ii = 0; ii < n_statuses; ii++)
    {
        char *existing_status = NULL;
        ifail = RELSTAT_ask_release_status_type(statuses[ii], &existing_status);
        if (ifail != ITK_ok) { /* your error logic here */ }
        
        if(strcmp(existing_status, old_release_status) == 0)
        {
            ifail = AOM_refresh(statuses[ii], TRUE);
            if (ifail != ITK_ok) { /* your error logic here */ }
            
            ifail = RELSTAT_set_release_status_type(statuses[ii], new_release_status);
            if (ifail != ITK_ok) { /* your error logic here */ }
            
            ifail = AOM_save(statuses[ii]);
            if (ifail != ITK_ok) { /* your error logic here */ }
            
            ifail = AOM_refresh(statuses[ii], FALSE);
            if (ifail != ITK_ok) { /* your error logic here */ }
            
            ifail = AOM_unload(statuses[ii]);
            if (ifail != ITK_ok) { /* your error logic here */ }    
        }
    }
    if(statuses) MEM_free(statuses);    
}

