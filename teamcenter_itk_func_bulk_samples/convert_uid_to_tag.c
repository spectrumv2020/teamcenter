/*HEAD CONVERT_UID_TO_TAG CCC ITK */
static logical convert_uid_to_tag(char *uid_string, tag_t *object_tag)
{
    logical  is_valid = TRUE;

    /* this function does not return an error code */
    ITK__convert_uid_to_tag(uid_string, object_tag);
    if (object_tag == NULLTAG)
    {
        is_valid = FALSE;
    }
    IFERR_REPORT(POM_is_tag_valid(*object_tag, &is_valid));
    return is_valid;
}


