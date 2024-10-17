
#include <qry/qry.h>
#include <itk/mem.h>
#include <tccore/aom_prop.h>
#include <tccore/workspaceobject.h>

static void report_wso_object_string_and_owning_user(tag_t object)
{
    char *object_string = NULL;
    IFERR_REPORT(WSOM_ask_object_id_string(object, &object_string));

    char *owning_user = NULL;
    IFERR_REPORT(AOM_UIF_ask_value(object, "owning_user", &owning_user));

    ECHO("       %s - %s \n", object_string, owning_user);
    MEM_free(object_string);
    MEM_free(owning_user);
}

static void demo_qry_using_date(void)
{
	tag_t query = NULLTAG;
	IFERR_REPORT(QRY_find("Item...", &query));
	
	int n_entries = 2;
	
	/* entries and values must be ordered pairs */
    char *entries[2] = {"Item ID", "Created After"}, 
	     **values = NULL;

	values = (char **) MEM_alloc(n_entries * sizeof(char *));

	values[0] = (char *)MEM_alloc( strlen( "0001*" ) + 1);
	strcpy(values[0], "0001*" );

	/* date and time should match the format in the user interface */
	values[1] = (char *)MEM_alloc( strlen( "01-Mar-2017 00:00" ) + 1);
	strcpy(values[1], "01-Mar-2017 00:00"  );

	int n_items = 0;
	tag_t *items = NULL;
	IFERR_REPORT(QRY_execute(query, n_entries, entries, values, &n_items, &items));
	for (int ii = 0; ii < n_items; ii++)
		report_wso_object_string_and_owning_user(items[ii]);
	
	if(values) MEM_free(values);
	if(items) MEM_free(items);
}
