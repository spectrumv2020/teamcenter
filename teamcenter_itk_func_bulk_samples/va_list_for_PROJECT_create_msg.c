extern DLLAPI int gtac_extension_rule(METHOD_message_t *m, va_list args)
{
	/* 
        Business Object or Type Name: TC_Project
        Operation Name: PROJECT_create
    */

    /* va_list for PROJECT_create_msg */
    char *project_id = va_arg(args, char *);
    char *name = va_arg(args, char *); 
    char *desc = va_arg(args, char *); 
    tag_t *new_project = va_arg(args, tag_t*);

    ECHO(( "\n\n\t project_id = %s\n", project_id));
    ECHO(( "\t name = %s\n", name));
    ECHO(( "\t desc = %s\n", desc));
    ECHO(( "\t new_project = %u\n", *new_project));

    return ITK_ok;
}