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

