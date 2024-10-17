
static tag_t get_current_users_home_folder()
{
    char *user_name_string = NULL;
    tag_t user_tag = NULLTAG;

    IFERR_REPORT(SA_init_module());
    IFERR_REPORT(POM_get_user(&user_name_string, &user_tag));
	MEM_free(user_name_string);

	tag_t home_folder_tag = NULLTAG;
    IFERR_REPORT(SA_ask_user_home_folder(user_tag, &home_folder_tag));
    IFERR_REPORT(SA_exit_module());

	return home_folder_tag;
}

