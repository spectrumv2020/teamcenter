

#include <sa/group.h>
#include <sa/groupmember.h>
#include <sa/user.h>
#include <sa/role.h>

static void change_current_group_and_role(char *group_name, char *role_name)
{
	tag_t group_tag = NULLTAG;
	IFERR_ABORT(SA_find_group(group_name, &group_tag));
	
	tag_t role_tag = NULLTAG;
	IFERR_ABORT(SA_find_role(role_name, &role_tag));
	IFERR_ABORT(SA_change_group(group_tag, role_tag));		
}

static void change_current_group_only(char *group_name)
{
	tag_t group_tag = NULLTAG;
	IFERR_ABORT(SA_find_group(group_name, &group_tag));
	
	/* if role_tag is NULLTAG the default role for the group will be used */
	IFERR_ABORT(SA_change_group(group_tag, NULLTAG));
}

static void change_current_role_only(char *role_name)
{
	tag_t current_member_tag = NULLTAG;
	IFERR_ABORT(SA_ask_current_groupmember(&current_member_tag));
	 
	tag_t current_group_tag = NULLTAG;
	IFERR_ABORT(SA_ask_groupmember_group(current_member_tag, &current_group_tag));
	
	tag_t role_tag = NULLTAG;
	IFERR_ABORT(SA_find_role(role_name, &role_tag));
	
	int n_role_tags = 0;
	tag_t *role_tags = NULL;
	IFERR_ABORT(SA_ask_roles_from_group(current_group_tag, &n_role_tags, &role_tags));
	logical match = FALSE;
	for (int ii = 0; ii < n_role_tags; ii++)
	{	
		if(role_tags[ii] == role_tag)
		{
			/* group_tag cannot be NULLTAG */
			IFERR_ABORT(SA_change_group(current_group_tag, role_tag));
			match = TRUE;
		}
	}
	if(match == FALSE) 
		ECHO("\n %s does not belong to the current group!\n\n\n", role_name);
}
