static void ask_imanfile_file_size(tag_t fileTag, char **file_size)
{
    int
        n_props = 0,
        ii = 0;
    char
        **props = NULL;

    IFERR_REPORT(AOM_ask_prop_names(fileTag, &n_props, &props));

    for (ii = 0; ii < n_props; ii++)
        if (!strcmp(props[ii], "file_size"))
            IFERR_REPORT(AOM_UIF_ask_value(fileTag, props[ii], file_size));

    free_string_array(n_props, props);

}

