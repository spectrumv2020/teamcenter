void report_folder_references(tag_t folder)
{
    int
        num_of_references,
        ii;
    char
        *object_id,
        name[WSO_name_size_c + 1],
        type[WSO_name_size_c + 1];
    tag_t
        *list_of_references;
        
    ITK_CALL(FL_ask_references(folder, FL_fsc_by_name, &num_of_references, 
        &list_of_references));
    for (ii = 0; ii < num_of_references; ii++)
    {
        ITK_CALL(WSOM_ask_object_id_string(list_of_references[ii], &object_id));
        ITK_CALL(WSOM_ask_name(list_of_references[ii], name));
        ITK_CALL(WSOM_ask_object_type(list_of_references[ii], type));
        printf("%s\t%s\t%s\n", object_id, name, type);
        MEM_free(object_id);
    } 
    if(num_of_references) MEM_free(list_of_references);     
}   
