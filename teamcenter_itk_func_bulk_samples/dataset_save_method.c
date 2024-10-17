extern DLLAPI int dataset_save_method(METHOD_message_t *m, va_list args)
{ 
    /***  va_list for TC_save_msg  ***/
    tag_t dataset  = va_arg(args, tag_t);
    /***********************************/

    printf( "\t dataset_save_method - dataset: %u\n", dataset);

    /* your dataset save method code here */

    return ITK_ok;
}
