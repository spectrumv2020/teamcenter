static void create_dataset(char *type_name, char *name, tag_t item, tag_t rev, 
    tag_t *dataset)
{
    char
        format_name[AE_io_format_size_c + 1] = "BINARY_REF";
    tag_t
        datasettype = NULLTAG,
        tool = NULLTAG;
    
    ERROR_CHECK(AE_find_datasettype(type_name, &datasettype));
    if (datasettype == NULLTAG)
    {
        printf("Dataset Type %s not found!\n", type_name);
        exit (EXIT_FAILURE);
    }
    
    ERROR_CHECK(AE_ask_datasettype_def_tool(datasettype, &tool));
    
    printf("Creating Dataset: %s\n", name);
    ERROR_CHECK(AE_create_dataset(datasettype, name, "", dataset));
    
    ERROR_CHECK(AE_set_dataset_tool(*dataset, tool));
    if (strcmp(type_name, "Text")) strcpy(format_name, "TEXT_REF");
    
    ERROR_CHECK(AE_set_dataset_format(*dataset, format_name));
    printf("Saving Dataset: %s\n", name);
    ERROR_CHECK(AOM_save(*dataset));
    
    /*attach dataset to item revision */
    ERROR_CHECK(ITEM_attach_rev_object(rev, *dataset, ITEM_specification_atth));
    ERROR_CHECK(ITEM_save_item(item));

}
