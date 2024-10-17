static void build_similar_temp_filespec(char *filespec, char *tempspec)
{
    char *temp_dir = getenv("TC_TMP_DIR");

    if (SS_MACHINE_TYPE == SS_WNT_MACHINE)
        sprintf(tempspec, "%s\\%s", temp_dir, filespec);
    else
        sprintf(tempspec, "%s/%s", temp_dir, filespec);
}

