void delete_all_folder_references(tag_t folder)
{
    int
        num_of_references,
        ii;
    char
        *object_id = NULL;
    tag_t
        *list_of_references;

    ITK_CALL(FL_ask_references(folder, FL_fsc_by_name, &num_of_references, 
        &list_of_references));
    ITK_CALL(AOM_refresh(folder, TRUE));
    for (ii = 0; ii < num_of_references; ii++)
    {
        ITK_CALL(FL_remove(folder, list_of_references[ii]));
    }
    ITK_CALL(AOM_save(folder));
    ITK_CALL(AOM_unlock(folder));
    ITK_CALL(AOM_refresh(folder, TRUE));    
    for (ii = 0; ii < num_of_references; ii++)
    {
        ITK_CALL(WSOM_ask_object_id_string(list_of_references[ii], &object_id));
        printf("Delete %s\n", object_id); 
        ITK_CALL(AOM_delete(list_of_references[ii]));
        MEM_free(object_id);
    }
   
    if(num_of_references) MEM_free(list_of_references);
}
