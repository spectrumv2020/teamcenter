
#ifdef __cplusplus
extern "C" {
#endif

#include <epm/epm.h>
#include <epm/epm_task_template_itk.h>
#include <itk/mem.h>
#include <qry/qry.h>
#include <sa/sa.h>

void find_items_by_owning_group_using_query(char *owning_group, int* count, tag_t** items)
{ 
    int n_entries = 1;
    tag_t  query = NULLTAG;
    tag_t *objects = NULL;
    char *entries[1] = {"Owning Group"};
    char **values = NULL; 

    IFERR_REPORT(QRY_find("Item...", &query ));
	if (query != NULLTAG)
	{
		values  = (char **) MEM_alloc(n_entries * sizeof(char *));
		values[0] = (char *)MEM_alloc( strlen( owning_group ) + 1);
		strcpy(values[0], owning_group );

		if (query != NULLTAG)
		{
			IFERR_REPORT(QRY_execute(query, n_entries, entries, values, count, items));
		}
	}
}

static void new_process_using_query_results(void)
{
	char owning_group[SA_group_name_size_c + 1] = "Engineering";
	int n_items = 0;
	tag_t *items = NULL;
	find_items_by_owning_group_using_query(owning_group, &n_items, &items);
	ECHO("\n n_items: %d \n", n_items);

	if (n_items > 0)
	{
		// Must have an attachment type for each object
		int *attach_types = NULL;
		attach_types = (int *) MEM_alloc (n_items * sizeof(int));
		for (int ii = 0; ii < n_items; ii++)
		{
			attach_types[ii] = EPM_target_attachment;
		}

		tag_t process_template = NULLTAG;
		IFERR_REPORT(EPM_find_process_template("AAReviewTaskNoStatus", 
			&process_template));

		tag_t process = NULLTAG;
		IFERR_REPORT(EPM_create_process("WFP", "", process_template,n_items, 
			items, attach_types, &process));	

		MEM_free(attach_types);
	}
	else ECHO("\n No items found! \n");
	MEM_free(items);
}

#ifdef __cplusplus
}
#endif
