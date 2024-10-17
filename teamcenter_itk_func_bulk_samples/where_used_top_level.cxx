
static void where_used_top_level(tag_t rev_tag)
{
    int n_parents = 0;
    int *levels = 0;
    tag_t *parents = NULL;   
    ifail = PS_where_used_all(rev_tag, PS_where_used_all_levels, &n_parents, &levels, &parents); 
    if (ifail != ITK_ok {/* add your error logic here */}
    
    ECHO("\n\n Top Level\n"); 
    for (int index = 0; index < n_parents; index++ )
    {
        if (index == n_parents - 1  ||  levels[index] >= levels[index+1] )
        {
            char *id_string = NULL;
            IFERR_REPORT(WSOM_ask_object_id_string(parents[index], &id_string)); 
            ECHO("\t%s \n", id_string); 
            MEM_free(id_string); 
        }
    } 
    if(levels) MEM_free(levels); 
    if(parents) MEM_free(parents);     
}
