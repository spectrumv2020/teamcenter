static void ask_property_value_by_name(tag_t object, char *prop_name,
    char **prop_value)
{
    tag_t
        prop_tag = NULLTAG;

    *prop_value = NULL;

    if (PROP_UIF_ask_property_by_name(object, prop_name, &prop_tag) == ITK_ok)
        IFERR_REPORT(PROP_UIF_ask_value(prop_tag, prop_value));
}

