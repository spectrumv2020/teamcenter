int DatasetExport(METHOD_message_t *msg, va_list args)
{
    int ifail = ITK_ok;
	va_list largs;
    va_copy( largs, args );
    
    /* va_list for AE_export_file_msg */
    tag_t  dataset_tag = va_arg(largs, tag_t);
    char *referenceName = va_arg(largs, char *);
    char *destinationName = va_arg(largs, char *);
    
    va_end(largs);
    
    is_instance_in_database("dataset_tag", dataset_tag);
    ECHO("   referenceName: %s \n", referenceName);
    ECHO("   destinationName: %s \n", destinationName);

	return ifail;    
}