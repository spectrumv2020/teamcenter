#include <tc/emh.h>
#include <tc/tc.h>
#include <sa/sa.h>
#include <stdarg.h>

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

#define IFERR_ABORT(X)  (report_error( __FILE__, __LINE__, #X, X, TRUE))
#define IFERR_REPORT(X) (report_error( __FILE__, __LINE__, #X, X, FALSE))

static int report_error(char *file, int line, char *call, int status,
    logical exit_on_error)
{
    if (status != ITK_ok)
    {
        int
            n_errors = 0,
            *severities = NULL,
            *statuses = NULL;
        char
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

#define C_START 1
#define C_END 100

static void do_it(void)
{
    int
        ii;
    tag_t
        group = NULLTAG,
        member = NULLTAG,
        person = NULLTAG,
        user = NULLTAG;
    char
        person_name[SA_name_size_c+1],
        user_name[SA_user_size_c+1];

    IFERR_ABORT(SA_init_module());
    IFERR_ABORT(SA_find_group("BigGroup", &group));

    for (ii = C_START; ii <= C_END; ii++)
    {
        sprintf(person_name, "Person %d", ii);
        IFERR_ABORT(SA_create_person(person_name, &person));
        IFERR_ABORT(AOM_save(person));

        sprintf(user_name, "User%d", ii);
        IFERR_ABORT(SA_create_user(user_name, person_name, user_name, &user));
        IFERR_ABORT(SA_set_user_login_group(user, group));
        IFERR_ABORT(SA_set_os_user_name(user, "infodba"));
        IFERR_ABORT(AOM_save(user));
    }

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
