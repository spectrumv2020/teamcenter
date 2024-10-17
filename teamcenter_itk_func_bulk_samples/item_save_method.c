extern DLLAPI int item_save_method(METHOD_message_t *m, va_list args)
{
    /***  va_list for TC_save_msg  ***/
    tag_t item  = va_arg(args, tag_t);
    /***********************************/

    printf( "\t item_save_method - item: %u\n", item);

    /* add your item save method code here */

    return ITK_ok;
}
