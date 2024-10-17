
#include <stdio.h>
#include <tc/tc_startup.h>

#define ECHO write_to_stdout_and_syslog

void write_to_stdout_and_syslog(char *format, ...)
{
    char msg[1000];
    va_list args;
    va_start(args, format);
    vsprintf(msg, format, args);
    va_end(args);
    printf(msg);
    TC_write_syslog(msg);
}
