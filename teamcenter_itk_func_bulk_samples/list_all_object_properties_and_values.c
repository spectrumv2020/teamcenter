static void list_all_object_properties_and_values(tag_t object)
{
    int
        ii,
        n_props;
    tag_t
        *props;
    char
        *name,
        *value;

    ERROR_CHECK(PROP_list_properties(object, &n_props, &props));
    for (ii = 0; ii < n_props; ii++)
    {
        ERROR_CHECK(PROP_UIF_ask_name(props[ii], &name));
        ERROR_CHECK(PROP_UIF_ask_value(props[ii], &value));
        printf("%s = %s\n", name, value);
        if (name) MEM_free(name);
        if(value) MEM_free(value);
    }
    if (n_props > 0) MEM_free(props);
}

