extern DLLAPI int GTAC_wso_copy(METHOD_message_t *m, va_list args)
{
    /* va_list for WSO_copy_msg */
    tag_t  wso_tag = va_arg(args, tag_t);
    char   *new_name = va_arg(args, char *);
    tag_t  *new_wso = va_arg(args, tag_t *);

    printf("wso_tag = %u\n", wso_tag);
    printf("new_name = %s\n", new_name);
    printf("new_wso = %u\n", new_wso);

    return ITK_ok;
}

