extern DLLAPI int tc_save(METHOD_message_t *m, va_list args)
{    
    /* va_list for TC_save_msg */
    va_list largs;
    va_copy( largs, args );

    tag_t  object_tag = va_arg(largs, tag_t);
    logical  isNew = va_arg(largs, logical);

    va_end( largs );

    return ITK_ok;
}


