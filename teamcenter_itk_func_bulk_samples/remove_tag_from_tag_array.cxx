
static int remove_tag_from_tag_array(tag_t remove_tag, int n_old_tag_list, tag_t *old_tag_list, int *n_new_tag_list, tag_t **new_tag_list)
{
    int matched = 0;
    int count = 0;
    for (int ii = 0; ii < n_old_tag_list; ii++)
    {
        if(old_tag_list[ii] != remove_tag)
        {
            count++;
            if (count == 1)
            {
                (*new_tag_list) = (tag_t *) MEM_alloc( sizeof(tag_t));
            }
            else
            {
                (*new_tag_list) = (tag_t *) MEM_realloc( (*new_tag_list), 
                    count * sizeof(tag_t));
            }
            (*new_tag_list)[count - 1] = old_tag_list[ii];
            *n_new_tag_list = count;                                            
        }
        else matched++;
    }
    if (matched == 0) ECHO("\n remove_tag is not in the array!\n");
    return matched;
}
