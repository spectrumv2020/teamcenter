
static void list_all_occurrence_note_types()
{
    int n_note_types = 0;
    tag_t *note_types = NULL;
    IFERR_ABORT(PS_note_type_extent(&n_note_types, &note_types));
    for (int ii = 0; ii < n_note_types; ii++)
    {
        char *name = NULL;
        IFERR_ABORT(PS_ask_note_type_name(note_types[ii], &name));
        printf("  %s \n", name);
        if(name) MEM_free(name);
    }
    if(n_note_types) MEM_free(note_types);  
}
