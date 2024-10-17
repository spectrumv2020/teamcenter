
#include <stdlib.h>
#include <string>
#include <sa/sa.h>
#include <tc/emh.h>
#include <tccore/method.h>
#include <epm/cr.h>
#include <epm/epm.h>

int simple_notify_handler(EPM_action_message_t msg)
{
	int ifail = ITK_ok;

	/* tc_mail_smtp arguments and character length limits */
	char to[SA_email_size_c + 1] = "";
	char subject[100] = "";
	char server[100] = "";
	char body[200] = "";
	char user[SA_email_size_c + 1]= "";
		
	strcpy(to, "john.smith@gmail.com");
	strcpy(subject, "\"The email subject!\"");
	strcpy(server, "cysmtp.ugs.com");
	strcpy(body, "C:\\Temp\\body.txt");
	strcpy(user, "jane.doe@gmail.com");

	char cmd[256] = "";
	sprintf(cmd, "tc_mail_smtp -to=%s -subject=%s -server=%s -body=%s -user=%s",
		to, subject, server, body,  user);

	system(cmd);

    return ifail;
}
