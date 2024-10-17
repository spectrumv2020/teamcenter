static void ask_imanfile_byte_size(tag_t fileTag, char **byte_size)
{
    int
        n_props = 0,
        ii = 0;
    char
        **props = NULL;

    IFERR_REPORT(AOM_ask_prop_names(fileTag, &n_props, &props));

    for (ii = 0; ii < n_props; ii++)
        if (!strcmp(props[ii], "byte_size"))
            IFERR_REPORT(AOM_UIF_ask_value(fileTag, props[ii], byte_size));

    free_string_array(n_props, props);

}

