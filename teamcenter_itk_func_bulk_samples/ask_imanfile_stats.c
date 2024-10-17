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

static void ask_imanfile_stats(tag_t fileTag, IMF_file_status *stats)
{
    int
        machine_type;
    tag_t
        volume;
    char
        path[SS_MAXPATHLEN];

    ask_imanfile_path(fileTag, path);
    IFERR_ABORT(IMF_stat(path, stats));
}

