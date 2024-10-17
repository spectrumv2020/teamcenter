/* 
    Note:  This sample code is for Tc10 and earlier versions.
	
      Business Object Name: PSOccurrence 
      Business Object Or Property: Property
      Proeprty Name: seq_no 
      Operation Name: BMF_PSO_ask_new_sequence_no 
      Extension Point: BaseAction
*/

#include <A2gtac/A2_PSO_ask_new_sequence_no.hxx>

#include <stdlib.h>
#include <string.h>
#include <ug_va_copy.h>
#include <fclasses/tc_string.h>
#include <itk/mem.h>
#include <ps/ps.h>
#include <tc/emh.h>

#pragma warning(disable: 4100)
#pragma warning(disable: 4189)

/* Initial sequence number */
#define DEFAULT_SEQUENCE_NO "1"

int A2_PSO_ask_new_sequence_no( METHOD_message_t * msg, va_list args)
{
    int ifail = ITK_ok;

    va_list largs;
    va_copy( largs, args );
    tag_t  parent_bom_rev = va_arg(largs, tag_t);
    tag_t  item = va_arg(largs, tag_t);
    char  **seqno = va_arg(largs, char **);
    va_end( largs );

    if (parent_bom_rev == null_tag)
    {
       char *my_string = (char *)MEM_alloc(strlen(DEFAULT_SEQUENCE_NO) + 1);
       strcpy(my_string, DEFAULT_SEQUENCE_NO);
       *seqno = my_string;
    }
    else
    {
        char *sequence_no = 0;
        char *my_string = NULL;
        char *occurrence_seq = NULL;
        char match[6] = "False";
        int n_occurrences = 0;
        int i = 0;
        tag_t *t_occurrences;

        int ifail = PS_bvr_ask_default_next_seq_no(parent_bom_rev, item, &sequence_no);
        if ( ifail != ITK_ok )
        {
           if (ifail != PS_no_seq_no)
           {
                return ITK_ok;
           }
           else if( ifail != ITK_ok )
           {
               EMH_clear_last_error(PS_no_seq_no);
           }
        }

        ifail = PS_list_occurrences_of_bvr(parent_bom_rev, &n_occurrences, &t_occurrences);
		if ( ifail != ITK_ok )
        {
			return ifail;
		}
		for( i = 0; i < n_occurrences; i ++ ) //looping through occurrences
		{
			PS_ask_seq_no(parent_bom_rev, t_occurrences[i], &occurrence_seq);
			if ( ifail != ITK_ok )
			{
				return ifail;
			}
			if ( tc_strcasecmp(occurrence_seq, sequence_no) == 0)
			{
				strcpy(match, "True");
				*seqno = sequence_no;
				break;
			}
			else
			{
				strcpy(match, "False");
			}
		}

        if ( tc_strcasecmp(match, "False") == 0)
        {
            my_string = (char *)MEM_alloc ( strlen ( sequence_no ) + 1);
            sprintf(my_string, "%d", atoi(sequence_no) - 9);
            *seqno = my_string;
        }
    }
    return ifail;
}
