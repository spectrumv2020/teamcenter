extern DLLAPI int ugpart_save_method(METHOD_message_t *m, va_list args)
{
    /*** ** va_list for TC_save_msg  *****/
    tag_t  ugpart  = va_arg(args, tag_t);
    /***************************************/

    printf( "\t ugpart_save_method - ugpart: %u\n", ugpart);

 
    return ITK_ok;
}
