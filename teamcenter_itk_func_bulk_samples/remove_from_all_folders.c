static int remove_from_all_folders(tag_t object)
{
    int
        cnt = 0,
        ii,
        n_levels,
        n_instances,
        *instance_levels,
        *instance_where_found,
        n_classes,
        *class_levels,
        *class_where_found;
    tag_t
        folder_type,
        newstuff_type,
        *ref_instances,
        *ref_classes,
        type = NULLTAG;

    IFERR_REPORT(TCTYPE_find_type("Folder", NULL, &folder_type));
    if (folder_type == NULLTAG)
    {
        ECHO("Folder type not found!\n");
        return FALSE;
    }

    IFERR_REPORT(TCTYPE_find_type("Newstuff Folder", NULL, &newstuff_type));
    if (newstuff_type == NULLTAG)
    {
        ECHO("Newstuff Folder type not found!\n");
        return FALSE;
    }

    IFERR_REPORT(POM_referencers_of_instance(object, 1, POM_in_ds_and_db,
        &n_instances, &ref_instances, &instance_levels,
        &instance_where_found, &n_classes, &ref_classes, &class_levels,
        &class_where_found));

    if (n_instances > 0)
    {
        for (ii = 0; ii < n_instances; ii++)
        {
            IFERR_REPORT(TCTYPE_ask_object_type(ref_instances[ii], &type));
            if ((type == folder_type) || (type == newstuff_type))
            {
                IFERR_REPORT(AOM_refresh(ref_instances[ii], TRUE));
                IFERR_REPORT(FL_remove(ref_instances[ii], object));
                IFERR_REPORT(AOM_save(ref_instances[ii]));
                IFERR_REPORT(AOM_refresh(ref_instances[ii], FALSE));
                cnt++;
            }
        }
        MEM_free(ref_instances);
        MEM_free(instance_levels);
        MEM_free(instance_where_found);
    }

    if (n_classes > 0)
    {
        MEM_free(ref_classes);
        MEM_free(class_levels);
        MEM_free(class_where_found);
    }

    return cnt;
}

