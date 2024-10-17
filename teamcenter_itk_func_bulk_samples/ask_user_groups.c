static int ask_user_groups(tag_t user, tag_t **groups)
{
    int
        ii,
        n_members;
     char 
        group_name[SA_name_size_c + 1];
    tag_t
       *members;
       
    ITK_CALL(SA_find_groupmember_by_user(user, &n_members, &members));
    (*groups) = (tag_t *) MEM_alloc( n_members * sizeof(tag_t));
    for (ii = 0; ii < n_members; ii++)
    {
        ITK_CALL(SA_ask_groupmember_group(members[ii], *groups + ii));
    }
    MEM_free(members);
    return(n_members); 
} 
