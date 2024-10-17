
#include <bom/bom.h>
#include <me/me.h>
#include <qry/qry.h>
#include <tc/tc.h>
#include <tc/tc_startup.h>
#include <tccore/aom_prop.h>

static void structure_search_by_item_id(char *item_id, tag_t item)
{		
	tag_t query = NULLTAG;
	IFERR_ABORT(QRY_find("Item...", &query));
	
	int n_entries = 1;
	char *entries[1] = {"Item ID"};		
	char **values = NULL;
	values = (char **) MEM_alloc(n_entries * sizeof(char *));
	values[0] = (char *)MEM_alloc( strlen( item_id ) + 1);
    strcpy(values[0], item_id);	
	
    tag_t tWindow = NULLTAG;
    IFERR_ABORT(BOM_create_window(&tWindow));
    
    tag_t topline = NULLTAG;
    IFERR_ABORT(BOM_set_window_top_line(tWindow, item, NULLTAG, NULLTAG, &topline));
		
	int n_scopes = 1;
	tag_t *scopes = NULL;
	scopes = (tag_t *) MEM_alloc(n_scopes * sizeof(tag_t));
	scopes[0] = topline;

	ME_saved_query_expression_s qry_exp;
	qry_exp.saved_qry_tag = query;
	qry_exp.num_entries = n_entries;
	qry_exp.entries = entries;
	qry_exp.values = values;

    ME_search_expression_set_t search_exp;
    search_exp.num_saved_query_expressions = 1;
    search_exp.saved_query_expressions = &qry_exp;
    search_exp.num_occ_note_expressions = 0;
    search_exp.occ_note_expressions = NULL;
	
    ME_mfg_search_criteria_t criteria; 
		
	int n_lines = 0;
	tag_t *lines = NULL;
	IFERR_ABORT(ME_execute_structure_search(n_scopes, scopes, &search_exp, &criteria, &n_lines, &lines));
	for (int ii = 0; ii < n_lines; ii++)
	{
		char* line_title = NULL;
		IFERR_ABORT(AOM_ask_value_string(lines[ii], "bl_formatted_title", &line_title));
		printf("\n  %s \n", line_title);
		if(line_title) MEM_free(line_title);	
	}
	if(scopes) MEM_free(scopes);	
	if(values) MEM_free(values);
	if(lines) MEM_free(lines);
}
