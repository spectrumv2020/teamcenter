static int ask_dataset_imanfile_named_references(tag_t dataset,
    char ***reference_names, tag_t **reference_objects)
{
    int
        ii,
        imf_count = 0,
        reference_count = 0;
    tag_t
        prop_tag,
        reference_object;
    char
        reference_name[AE_reference_size_c + 1],
        *type;
    AE_reference_type_t
        reference_type;

/*  Make sure no one messes with it while we're reading it */
    ITK_CALL(AOM_refresh(dataset, TRUE));

    ITK_CALL(AE_ask_dataset_ref_count(dataset, &reference_count));
    if (reference_count > 0)
    {
        *reference_names = allocate_string_array(reference_count,
            AE_reference_size_c + 1);
        *reference_objects = MEM_alloc(reference_count * sizeof(tag_t));

        for (ii = 0; ii < reference_count; ii++)
        {
            ITK_CALL(AE_find_dataset_named_ref(dataset, ii,
                reference_name, &reference_type, &reference_object));
            ask_property_value_by_name(reference_object, "Type", &type);
            if (!strcmp(type, "ImanFile"))
            {
                strcpy((*reference_names)[imf_count], reference_name);
                (*reference_objects)[imf_count] = reference_object;
                imf_count++;
            }
            if (type != NULL) MEM_free(type);
        }
    }

    ITK_CALL(AOM_unlock(dataset));

    return imf_count;
}

