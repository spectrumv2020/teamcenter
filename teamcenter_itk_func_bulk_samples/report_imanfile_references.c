static void report_imanfile_references(int n, char **reference_names,
    tag_t *reference_objects)
{
    int ii;
    char name[IMF_filename_size_c + 1];

    for (ii = 0; ii < n; ii++)
    {
        ITK_CALL(IMF_ask_original_file_name(reference_objects[ii], name));
        printf("%d.  %s - %s\n", ii+1, reference_names[ii], name);
    }
}

