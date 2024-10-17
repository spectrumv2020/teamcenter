static void report_all_dataset_named_references(tag_t dataset)
{
    int
        ii,
        reference_count = 0;
    tag_t
        prop_tag,
        reference_object;
    char
        reference_name[AE_reference_size_c + 1],
        *s_name = NULL,
        *type;
    AE_reference_type_t
        reference_type;

/*  Make sure no one messes with it while we're reading it */
    IFERR_REPORT(AOM_refresh(dataset, TRUE));

    IFERR_REPORT(AOM_ask_name(dataset, &s_name));
    ECHO("Dataset %s has these named references\n", s_name);
    if (s_name != NULL) MEM_free(s_name);

    IFERR_REPORT(AE_ask_dataset_ref_count(dataset, &reference_count));
    if (reference_count > 0)
    {
        for (ii = 0; ii < reference_count; ii++)
        {
            IFERR_REPORT(AE_find_dataset_named_ref(dataset, ii,
                reference_name, &reference_type, &reference_object));
            IFERR_REPORT(PROP_UIF_ask_property_by_name(reference_object,
                "Type", &prop_tag));
            IFERR_REPORT(PROP_UIF_ask_value(prop_tag, &type));
            ECHO("  %d. %s (%s)\n", ii+1, reference_name, type);
            if (type != NULL) MEM_free(type);
        }
    }

    IFERR_REPORT(AOM_unload(dataset));
}

