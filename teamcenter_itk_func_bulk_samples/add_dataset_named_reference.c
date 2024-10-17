static void add_dataset_named_reference(tag_t dataset, char *reference_name,
    tag_t imported_file)
{

    ITK_CALL(AOM_refresh(dataset, TRUE));

    ITK_CALL(AE_add_dataset_named_ref(dataset, reference_name, AE_PART_OF,
        imported_file));

    ITK_CALL(AE_save_myself(dataset));
    ITK_CALL(AOM_unlock(dataset));
}

