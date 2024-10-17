
#include <epm/epm.h>

void report_decision_value(EPM_decision_t decision)
{
	if(decision == EPM_go)   ECHO("   decision: EPM_go \n");
	if(decision == EPM_nogo) ECHO("   decision: EPM_nogo \n");
}
