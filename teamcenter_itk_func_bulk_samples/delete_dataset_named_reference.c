static void delete_dataset_named_reference(tag_t dataset, char *reference_name,
    tag_t reference_object)
{
    char
        backup[SS_MAXPATHLEN],
        name[IMF_filename_size_c + 1],
        pathname[SS_MAXPATHLEN],
        *type;

    ITK_CALL(AOM_refresh(dataset, TRUE));

    export_backup_imanfile(reference_object);
	ITK_CALL(AE_purge_dataset_revs(dataset));
    ITK_CALL(AE_remove_dataset_named_ref_by_tag(dataset, reference_name,
        reference_object));
    ITK_CALL(AE_save_myself(dataset));
    ITK_CALL(AOM_unlock(dataset));

    ITK_CALL(AOM_lock_for_delete(reference_object));
    ITK_CALL(IMF_ask_file_pathname(reference_object, SS_MACHINE_TYPE,
        pathname));
    printf("Deleted: %s\n", pathname);
    ITK_CALL(AOM_delete(reference_object));
}

