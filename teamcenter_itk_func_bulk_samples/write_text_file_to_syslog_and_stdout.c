
static void write_text_file_to_syslog_and_stdout(char *fspec)
{
    FILE
        *the_file = NULL;
    char
        line_of_text[256] = "",
        header[] = "/*****************  Begin ****************************/\n",
        footer[] = "/*****************  End   ****************************/\n";

    if ( (imanfile = fopen(fname, "r")) != NULL )
    {
        TC_write_syslog("%s", header); 
        fprintf(stdout,"%s", header); 
        while ((fgets(line_of_text, 256, imanfile)) != NULL)
        {
            TC_write_syslog("%s\n", line_of_text);
            fprintf(stdout,"%s\n", line_of_text);
        }
        fclose(imanfile);
        TC_write_syslog("%s", footer); 
        fprintf(stdout,"%s", footer);       
     }
}
