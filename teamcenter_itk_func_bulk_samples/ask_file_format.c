static int ask_file_format(char *path)
{
    IMF_file_status
        stats;

    ITK_CALL(IMF_stat(path, &stats));

    return stats.fmt;
}

