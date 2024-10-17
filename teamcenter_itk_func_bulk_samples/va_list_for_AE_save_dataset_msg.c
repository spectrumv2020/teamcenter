extern DLLAPI int ae_save_dataset(METHOD_message_t *m, va_list args)
{
    /* Object Type: Dataset - Operation: AE_save_dataset */
    
    /* va_list for AE_save_dataset_msg */
    tag_t datasetTag = va_arg (args, tag_t);
    logical isNew = va_arg (args, logical);
   
    ECHO(("\n ae_save_dataset_pre_condition \n"));
    ECHO(("\t datasetTag: %u - isNew: %d\n", datasetTag, isNew));

    return ITK_ok;
}


