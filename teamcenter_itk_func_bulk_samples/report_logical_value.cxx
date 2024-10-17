
#include <stdio.h>
#include <tc/tc_startup.h>

void ECHO(char *format, ...)
{
    char msg[1000];
    va_list args;
    va_start(args, format);
    vsprintf(msg, format, args);
    va_end(args);
    printf(msg);
    TC_write_syslog(msg);
}

void report_logical_value(char *variable_name, logical verdict)
{
	if(verdict == TRUE) ECHO("   %s: true\n", variable_name);
	if(verdict == FALSE) ECHO("   %s: false\n", variable_name);
}