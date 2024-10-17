static void import_text_file(char *os_path_name, tag_t *file_tag)
{
    int
        file_type = SS_TEXT, 
        machine_type = SS_WNT_MACHINE; 
    char
        *file_name = NULL, 
        pathname[SS_MAXPATHLEN];
    IMF_file_t
        file_descriptor;
        
    
    ITK_CALL(IMF_import_file(os_path_name, file_name, file_type, 
                 file_tag, &file_descriptor));
    ITK_CALL(IMF_ask_file_pathname(*file_tag, machine_type, pathname));
    printf("pathname of file on disk = %s\n", pathname);

    ITK_CALL(AOM_save(*file_tag));    
}
    
