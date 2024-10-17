static void export_backup_imanfile(tag_t imanfile)
{
    char
        backup[SS_MAXPATHLEN],
        name[IMF_filename_size_c + 1],
        *type;

    ITK_CALL(AOM_refresh(imanfile, TRUE));

    ask_property_value_by_name(imanfile, "Type", &type);
    if (!strcmp(type, "ImanFile"))
    {
        ITK_CALL(IMF_ask_original_file_name(imanfile, name));
        build_similar_temp_filespec(name, backup);
        unlink(backup);
        ITK_CALL(IMF_export_file(imanfile, backup));
        printf("Exported backup copy: %s\n", backup);
    }
    if (type != NULL) MEM_free(type);

    ITK_CALL(AOM_unlock(imanfile));
}

