/*HEAD FIND_FORMS_BY_NAME_AND_TYPE CCC ITK */
static void find_forms_by_name_and_type(char *name, char *type, int *n_forms, tag_t **forms)
{
    int   
        n_entries = 2;
    tag_t 
        query = NULLTAG;
    char
        *entries[2] = {"Name", "Type"},
        **values = NULL;

     values = (char **) MEM_alloc(n_entries * sizeof(char *));

     values[0] = (char *)MEM_alloc( strlen( name ) + 1);
     strcpy(values[0], name );

     values[1] = (char *)MEM_alloc( strlen( type ) + 1);
     strcpy(values[1], type );

    IFERR_REPORT(QRY_find("General...", &query));
    IFERR_REPORT(QRY_execute(query, n_entries, entries, values, n_forms, forms));
}
