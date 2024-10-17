extern DLLAPI int ae_save_dataset(METHOD_message_t *m, va_list args)
{
    int ifail = ITK_ok;
	
	/* va_list for AE_create_dataset_msg */
	va_list largs;
    va_copy( largs, args );	
    tag_t datasetTag = va_arg (largs, tag_t);
    va_end( largs ); 
    
    is_instance_in_database("datasetTag", datasetTag); 
    return ifail;
}


