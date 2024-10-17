static void free_string_array(int n_elements, char **string_array)
{
    int ii;

    for (ii = 0; ii < n_elements; ii++) MEM_free(string_array[ii]);

    MEM_free(string_array);
}

static char **alloc_string_array(int n_elements, int n_chars)
{
    int ii;
    char **where = MEM_alloc(n_elements * sizeof(char *));

    for (ii = 0; ii < n_elements; ii++)
        where[ii] = MEM_alloc(n_chars * sizeof(char));

    return where;
}

static int ask_all_dataset_named_references(tag_t dataset,
    char ***reference_names, tag_t **reference_objects, logical **is_imanfile)
{
    int
        ii,
        reference_count = 0;
    tag_t
        prop_tag;
    char
        *type;
    AE_reference_type_t
        reference_type;

/*  Make sure no one messes with it while we're reading it */
    ITK_CALL(AOM_refresh(dataset, TRUE));

    ITK_CALL(AE_ask_dataset_ref_count(dataset, &reference_count));
    if (reference_count > 0)
    {
        *reference_names = alloc_string_array(reference_count,
            AE_reference_size_c + 1);
        *reference_objects = MEM_alloc(reference_count * sizeof(tag_t));
        *is_imanfile = MEM_alloc(reference_count * sizeof(logical));

        for (ii = 0; ii < reference_count; ii++)
        {
            ITK_CALL(AE_find_dataset_named_ref(dataset, ii,
                (*reference_names)[ii], &reference_type,
                &(*reference_objects)[ii]));
            ITK_CALL(PROP_UIF_ask_property_by_name((*reference_objects)[ii],
                "Type", &prop_tag));
            ITK_CALL(PROP_UIF_ask_value(prop_tag, &type));
            if (!strcmp(type, "ImanFile"))
                (*is_imanfile)[ii] = TRUE;
            else
                (*is_imanfile)[ii] = FALSE;
            if (type != NULL) MEM_free(type);
        }
    }

    ITK_CALL(AOM_unload(dataset));

    return reference_count;
}

