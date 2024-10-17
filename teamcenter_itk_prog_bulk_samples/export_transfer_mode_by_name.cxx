/*HEAD EXPORT_TRANSFER_MODE_BY_NAME.CXX CCC ITK */
#ifdef __cplusplus
extern "C" {
#endif

#include <stdlib.h>
#include <stdarg.h>
#include <string.h>
#include <stdio.h>
#include <tccore/aom_prop.h>
#include <tc/emh.h>
#include <pie/pie.h>
#include <tc/tc.h>
#include <tc\tc_macros.h>
#include <tc\tc_startup.h>
#include <fclasses/tc_string.h>

#define IFERR_ABORT(X)  (report_error( __FILE__, __LINE__, #X, X, TRUE))
#define IFERR_REPORT(X) (report_error( __FILE__, __LINE__, #X, X, FALSE))

static int report_error(char *file, int line, char *call, int status, 
    logical exit_on_error);
static void ECHO(char *format, ...);
static void GTAC_free(void *what);

void usage ()
{
    printf("\n This program will export a the xml file to the current directory. \n\n");
    printf ("\n\n USAGE:  \n");
    printf ("\n\n   export_transfer_mode_by_name -t=<transfer mode name> \n");
    return;
}

static void do_it(void)
{
	if( (ITK_ask_cli_argument ("-h") != NULL) ||
		(ITK_ask_cli_argument ("-t=") == NULL) )
    {
        usage();
        exit(0);
    }
    char  *transfermode = ITK_ask_cli_argument("-t=");
    logical found = FALSE;

    int n_transfer_modes = 0; 
    tag_t *transfer_modes = NULL;
    IFERR_REPORT(PIE_extent_transfer_modes(&n_transfer_modes, &transfer_modes));
    for (int ii = 0; ii < n_transfer_modes; ii++)
    {
        char  name[WSO_name_size_c + 1] = "";
        IFERR_REPORT(WSOM_ask_name(transfer_modes[ii], name)); 

        if(tc_strcasecmp(transfermode, name) == 0)
        {
            found = TRUE;

            char *uid = NULL;
            ITK__convert_tag_to_uid(transfer_modes[ii],  &uid);

            ECHO("\n\nExporting %s.xml to current directory. \n", name);

            char cmd[132] = "";
            sprintf(cmd, "tcxml_export -u=infodba -p=infodba -file=\"%s.xml\" -uid=%s", name, uid);
            system(cmd);

            GTAC_free(uid);
        }
    }
    if (found == FALSE) ECHO("\n\nTransferMode %s NOT found!\n", transfermode);
    GTAC_free(transfer_modes);
}

int ITK_user_main(int argc, char* argv[])
{
    IFERR_REPORT(ITK_initialize_text_services(ITK_BATCH_TEXT_MODE));
    IFERR_ABORT(ITK_auto_login());
    IFERR_REPORT(ITK_set_journalling(TRUE));

    do_it();

    IFERR_REPORT(ITK_exit_module(FALSE));
    return ITK_ok;
}

static int report_error(char *file, int line, char *call, int status,
    logical exit_on_error)
{
    if (status != ITK_ok)
    {
        int
            n_errors = 0;
        const int
            *severities = NULL,
            *statuses = NULL;
        const char
            **messages;

        EMH_ask_errors(&n_errors, &severities, &statuses, &messages);
        if (n_errors > 0)
        {
            ECHO("\n%s\n", messages[n_errors-1]);
            EMH_clear_errors();
        }
        else
        {
            char *error_message_string;
            EMH_get_error_string (NULLTAG, status, &error_message_string);
            ECHO("\n%s\n", error_message_string);
        }

        ECHO("error %d at line %d in %s\n", status, line, file);
        ECHO("%s\n", call);
        if (exit_on_error)
        {
            ECHO("%s", "Exiting program!\n");
            exit (status);
        }
    }
    return status;
}

static void ECHO(char *format, ...)
{
    char msg[1000];
    va_list args;
    va_start(args, format);
    vsprintf(msg, format, args);
    va_end(args);
    printf(msg);
    TC_write_syslog(msg);
}

static void GTAC_free(void *what)
{
    if (what != NULL)
    {
        MEM_free(what);
        what = NULL;
    }

}

#ifdef __cplusplus
}
#endif
