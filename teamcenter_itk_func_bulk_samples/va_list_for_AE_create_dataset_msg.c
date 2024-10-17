extern DLLAPI int ae_create_dataset(METHOD_message_t *m, va_list args)
{
    ECHO(("\n ae_create_dataset \n"));

	va_list largs;
    va_copy( largs, args );

    /* Object Type: Dataset - Operation: AE_create_dataset */   
    /* va_list for AE_create_dataset_msg */
    tag_t datasetTypeTag = va_arg (largs, tag_t); 
    char* datasetName = va_arg (largs, char*);
    char* datasetDescription = va_arg (largs, char*);
    char* datasetID = va_arg (largs, char*);
    char* datasetRevision = va_arg (largs, char*);
    tag_t newDataset = va_arg (largs, tag_t);
   
    va_end( largs );
    
    ECHO("\t datasetTypeTag: %u\n", datasetTypeTag);
    ECHO("\t datasetName: %s\n", datasetName);
    ECHO("\t datasetDescription: %s\n", datasetDescription);
    ECHO("\t datasetID: %s\n", datasetID);
    ECHO("\t datasetRevision: %s\n", datasetRevision);
    ECHO("\t newDataset: %u\n", newDataset);

    return ITK_ok;
}

