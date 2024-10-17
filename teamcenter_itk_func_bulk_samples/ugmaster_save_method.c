extern DLLAPI int ugmaster_save_method(METHOD_message_t *m, va_list args)
{
    /*** ** va_list for TC_save_msg  *****/
    tag_t  ugmaster  = va_arg(args, tag_t);
    /***************************************/

    printf( "\t ugmaster_save_method - ugmaster: %u\n", ugmaster);

    return ITK_ok;
}
