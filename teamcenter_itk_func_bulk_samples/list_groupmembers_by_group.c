/*HEAD LIST_GROUPMEMBERS_BY_GROUP CCC ITK */
static void list_groupmembers_by_group(tag_t group)
{
    int
        n_members = 0,
        ii = 0;
    tag_t 
        *members = NULL, 
        user = NULLTAG, 
        role = NULLTAG;
    char 
        person_name[SA_person_name_size_c+1] = "",
        user_id[SA_user_size_c+1] = "",  
        group_name[SA_name_size_c+1] = "",
        role_name[SA_name_size_c+1] = "";  

    IFERR_REPORT(SA_find_groupmembers_by_group(group, &n_members, &members));
    ECHO("  n_members = %d\n", n_members);
    for (ii = 0; ii < n_members; ii++)
    {
        IFERR_REPORT(SA_ask_groupmember_user(members[ii], &user));
        IFERR_REPORT(SA_ask_groupmember_role(members[ii], &role));

        strcpy(role_name, "");


        if (user != NULLTAG)
        {
            IFERR_REPORT(SA_ask_user_person_name(user, person_name));
            IFERR_REPORT(SA_ask_user_identifier(user, user_id));
        }else strcpy(user_id, "");

        IFERR_REPORT(SA_ask_group_name(group, group_name));

        if (role != NULLTAG)
        {
            IFERR_REPORT(SA_ask_role_name(role, role_name));            
        }
        else strcpy(role_name, ""); 

        ECHO("\n        Person: %s\n", person_name);
        ECHO("       User ID: %s\n", user_id);
        ECHO("         Group: %s\n", group_name);
        ECHO("          Role: %s\n", role_name);
    }
    if (n_members > 0) MEM_free(members);
}

