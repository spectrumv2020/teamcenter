/*HEAD REPORT_CURRENT_LOGIN_INFOMATION CCC ITK */
static void report_current_login_infomation()
{
    tag_t 
        current_member = NULLTAG, 
        current_user = NULLTAG, 
        current_group = NULLTAG,  
        current_role = NULLTAG;
    char 
        person_name[SA_person_name_size_c+1] = "",
        user_id[SA_user_size_c+1] = "",  
        group_name[SA_name_size_c+1] = "",
        role_name[SA_name_size_c+1] = "";  

    IFERR_ABORT(SA_ask_current_groupmember(&current_member));
    IFERR_REPORT(SA_ask_groupmember_user(current_member, &current_user));
   IFERR_REPORT(SA_ask_groupmember_group(current_member, &current_group));
    IFERR_REPORT(SA_ask_groupmember_role(current_member, &current_role));

    IFERR_REPORT(SA_ask_user_person_name(current_user, person_name));
    IFERR_REPORT(SA_ask_user_identifier(current_user, user_id));
    IFERR_REPORT(SA_ask_group_name(current_group, group_name));
    IFERR_REPORT(SA_ask_role_name(current_role, role_name));

    ECHO("\n        Person: %s\n", person_name);
    ECHO("       User ID: %s\n", user_id);
    ECHO("         Group: %s\n", group_name);
    ECHO("          Role: %s\n", role_name);
}
