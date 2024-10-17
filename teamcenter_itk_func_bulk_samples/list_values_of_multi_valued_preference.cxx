
#include <tc/preferences.h>

static void list_values_of_multi_valued_preference(char *preference_name)
{
	int n_values = 0;  
	char **values = NULL;
	IFERR_REPORT(PREF_ask_char_values(preference_name, &n_values, &values));
	printf("\t n_values: %d \n", n_values);

	for(int ii = 0; ii < n_values; ii++)
	{
	  printf("\t  values[%d]: %s \n", ii, values[ii]);
	}
	MEM_free(values);
}

