/*HEAD GET_PERSONS_USER_TAGS CCC ITK */

static void get_persons_user_tags(char *name, int *n_users, tag_t **user_tags)
{
    int 
        n_all_users = 0, 
        count = 0;
    tag_t 
        *all_user_tags = NULL;
    char  
        person_name[SA_person_name_size_c+1] = "";

    IFERR_REPORT(SA_extent_user(&n_all_users, &all_user_tags));
    for (int ii = 0; ii < n_all_users; ii++)
    {
        IFERR_REPORT(SA_ask_user_person_name(all_user_tags[ii], person_name));
        if (strcmp (person_name, name) == 0)
        {
            count++;
            if (count == 1)
            {
                (*user_tags) = (tag_t *) MEM_alloc( sizeof(tag_t));
            }
            else
            {
                (*user_tags) = (tag_t *) MEM_realloc( (*user_tags), count * sizeof(tag_t));
            }
            (*user_tags)[count - 1] = all_user_tags[ii];
            *n_users = count;
        }
    }
    GTAC_free(all_user_tags);
}
