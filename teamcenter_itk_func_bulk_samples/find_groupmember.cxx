#include <sa/groupmember.h>

static tag_t find_groupmember(char *group_name, char *role_name, char *user_id)
{
	int nGroupMembers = 0;
	tag_t *ptGroupMembers = NULL;	
	IFERR_ABORT(SA_find_groupmember_by_rolename(role_name, group_name, user_id,
		&nGroupMembers, &ptGroupMembers));
	
	/* should only be one since user_id, group_name and role_name are used */
	tag_t tGroupMember = ptGroupMembers[0];	
	if(ptGroupMembers) MEM_free(ptGroupMembers);
	return tGroupMember;
}