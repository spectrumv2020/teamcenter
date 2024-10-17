void ask_current_users_home_folder(tag_t *home_folder)
{
    char
        *user_name_string;
    tag_t
        user;

    ITK_CALL(SA_init_module());
    ITK_CALL(POM_get_user(&user_name_string, &user));
    ITK_CALL(SA_ask_user_home_folder(user, home_folder));
    MEM_free(user_name_string);
    ITK_CALL(SA_exit_module());
}
