static void ask_imanfile_path(tag_t fileTag, char *path)
{
    int
        machine_type;
    tag_t
        volume;

    IFERR_ABORT(IMF_ask_volume(fileTag, &volume));
    IFERR_ABORT(VM_ask_machine_type(volume, &machine_type));
    IFERR_ABORT(IMF_ask_file_pathname(fileTag, machine_type, path));
}

static tag_t import_os_file(char *os_path_name)
{
    int
        file_type;
    tag_t
        file_tag = NULLTAG;
    char
        pathname[SS_MAXPATHLEN];
    IMF_file_t
        file_descriptor;

    file_type = ask_file_format(os_path_name);

    IFERR_ABORT(IMF_import_file(os_path_name, NULL, file_type,
                 &file_tag, &file_descriptor));
    ask_imanfile_path(file_tag, pathname);
    ECHO("imported %s to %s\n", os_path_name, pathname);

    IFERR_ABORT(AOM_save(file_tag));
    IFERR_ABORT(AOM_unlock(file_tag));

    return file_tag;
}

