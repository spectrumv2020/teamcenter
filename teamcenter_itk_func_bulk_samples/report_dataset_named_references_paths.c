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

static void ask_property_value_by_name(tag_t object, char *prop_name,
    char **prop_value)
{
    tag_t
        prop_tag = NULLTAG;

    *prop_value = NULL;

    if (PROP_UIF_ask_property_by_name(object, prop_name, &prop_tag) == ITK_ok)
        IFERR_REPORT(PROP_UIF_ask_value(prop_tag, prop_value));
}

static void report_dataset_named_references_paths(tag_t dataset)
{
    int
        ii,
        reference_count = 0;
    tag_t
        reference_object;
    char
        dataset_name[WSO_name_size_c+1],
        path[SS_MAXPATHLEN],
        reference_name[AE_reference_size_c + 1],
        *type;
    AE_reference_type_t
        reference_type;

/*  Make sure no one messes with it while we're reading it */
    IFERR_REPORT(AOM_refresh(dataset, TRUE));

    IFERR_REPORT(WSOM_ask_name(dataset, dataset_name));
    ECHO("dataset = %s\n", dataset_name);

    IFERR_REPORT(AE_ask_dataset_ref_count(dataset, &reference_count));
    if (reference_count > 0)
    {
        for (ii = 0; ii < reference_count; ii++)
        {
            IFERR_REPORT(AE_find_dataset_named_ref(dataset, ii,
                reference_name, &reference_type, &reference_object));
            ask_property_value_by_name(reference_object, "Type", &type);
            if (!strcmp(type, "ImanFile"))
            {
                ask_imanfile_path(reference_object, path);
                ECHO("  imanfile named reference path = %s\n", path);
            }
            if (type != NULL) MEM_free(type);
        }
    }

    IFERR_REPORT(AOM_unload(dataset));
}

