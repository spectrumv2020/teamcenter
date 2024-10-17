extern DLLAPI int item_rev_revise_method(METHOD_message_t* message, va_list args)
{
    /*********  va_list for ITEM_copy_rev_msg  *********/
    tag_t  source_rev = va_arg(args, tag_t); /* args 1 */
    char  *rev_id = va_arg(args, char*);     /* args 2 */
    tag_t *new_rev = va_arg(args, tag_t*);   /* args 3 */
    /***************************************************/

    fprintf(stdout, "item_rev_revise_method\n");

    fprintf(stdout, "\t source_rev: %u", item);
    fprintf(stdout, " - rev_id: %s", rev_id);  
    fprintf(stdout, " - new_rev: %u\n", *new_rev);

   return ITK_ok;
}


