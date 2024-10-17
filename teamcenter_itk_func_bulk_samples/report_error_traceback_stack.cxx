
#include <stdio.h>
#include <tc/emh.h>
#include <tc/tc_startup.h>

static void report_error_stack()
{
    int n_errors = 0;
    const int *severities = NULL;
    const int *error_code = NULL;
    const char **msg;
    int ii = 0;
    EMH_ask_errors(&n_errors, &severities, &error_code, &msg);
    TC_write_syslog("\n n_errors: %d \n", n_errors);
    for (ii = n_errors - 1; ii >= 0  ; ii--)
    {
        if (severities[ii] == EMH_severity_information) 
            TC_write_syslog("    Information - error_code[%d]: %d - msg[%d]: %s\n", 
                ii, error_code[ii],ii, msg[ii]);
        if (severities[ii] == EMH_severity_warning) 
            TC_write_syslog("    Warning - error_code[%d]: %d - msg[%d]: %s\n", 
                ii, error_code[ii],ii, msg[ii]);                
        if (severities[ii] == EMH_severity_error) 
            TC_write_syslog("    Error - error_code[%d]: %d - msg[%d]: %s\n", 
            ii, error_code[ii],ii, msg[ii]);
    }
}
