#include <cfm/cfm.h>
#include <tccore/workspaceobject.h>

static void list_all_available_revision_rules(void)
{
	int n_rev_rules = 0;
	tag_t *rev_rule_tag_list = NULL;
	IFERR_REPORT(CFM_list( &n_rev_rules, &rev_rule_tag_list));

	char name[WSO_name_size_c+1]  ; 
	for(int ii =0; ii < n_rev_rules; ii++)
	{
	    IFERR_REPORT(WSOM_ask_name(rev_rule_tag_list[ii], name));
	    printf(" %s \n", name);
	}
}
