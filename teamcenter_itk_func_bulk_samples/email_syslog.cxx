#include <stdlib.h>
#include <sa/sa.h>
#include <tc/emh.h>
#include <tc/tc.h>

int email_syslog(char to[SA_email_size_c], char from[SA_email_size_c])
{
    int ifail = ITK_ok;

    /* tc_mail_smtp arguments and character length limits */
    char subject[100] = "\"Syslog\"";
    char server[100] = "cysmtp.ugs.com";
    const char* body = EMH_ask_system_log();
        
    char cmd[256] = "";
    sprintf(cmd, "tc_mail_smtp -to=%s -subject=%s -server=%s -body=%s -user=%s",
        to, subject, server, body,  from);

    system(cmd);
    
    return ifail;
}
