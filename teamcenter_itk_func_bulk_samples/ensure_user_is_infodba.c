void ensure_user_is_infodba(void)
{
    char
        *user_name_string,
        groupname[SA_name_size_c + 1];
    tag_t
        user_tag,
        group_tag;
        
    ITK_CALL(SA_init_module());
    ITK_CALL(POM_get_user(&user_name_string, &user_tag));
    ITK_CALL(SA_ask_user_login_group(user_tag, &group_tag));
    ITK_CALL(SA_ask_group_name(group_tag, groupname));
    if (strcmp(user_name_string, "infodba") && strcmp(groupname, "dba"))
    {
        printf("Must be infodba to execute this program\n");
        printf("Exiting program...\n");        
        MEM_free(user_name_string);
        ITK_exit_module(TRUE);
        exit();
    }
    MEM_free(user_name_string);
    ITK_CALL(SA_exit_module());
}
