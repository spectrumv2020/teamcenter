extern DLLAPI int iman_save(METHOD_message_t *m, va_list args)
{    
    /* va_list for TC_save_msg */
    tag_t object = va_arg (args, tag_t);
   
    ECHO(("\n iman_save \n"));
    ECHO(("\t object: %u \n", object));

    return ITK_ok;
}


