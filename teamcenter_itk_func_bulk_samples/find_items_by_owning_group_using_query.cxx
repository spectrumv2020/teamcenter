
#ifdef __cplusplus
extern "C" {
#endif

void find_items_by_owning_group_using_query(char *owning_group, int* count, tag_t** items)
{ 
    int   n_entries = 1;
    tag_t  query = NULLTAG;
    tag_t *objects = NULL;
    char *entries[1] = {"Owning Group"};
    char **values = NULL; 

    IFERR_REPORT(QRY_find("Item...", &query ));
    values  = (char **) MEM_alloc(n_entries * sizeof(char *));
    values[0] = (char *)MEM_alloc( strlen( owning_group ) + 1);
    strcpy(values[0], owning_group );

    if (query != NULLTAG)
    {
        IFERR_REPORT(QRY_execute(query, n_entries, entries, values, count, items));
    }
}

#ifdef __cplusplus
}
#endif
