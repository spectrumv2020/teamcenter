
/******************************************************************************
  There are two kinds of option sets: 
   - Runtime or selected option set is a instance of type BOMsos. 
     Every BOMLine has a selected option set.   
   
   - Persistent or stored option set is a database instance of type 
     StoredOptionSet. 
     
  This sample sets the value on the selected option set.
******************************************************************************/
#include <bom/bom.h>
#include <itk/mem.h>

static void report_number_of_child_lines(tag_t window, tag_t top_line);

static void set_modular_variant_runtime_sos(tag_t window, char *value)
{
    tag_t top_line = NULLTAG;
    IFERR_ABORT(BOM_ask_window_top_line(window, &top_line));
    
    report_number_of_child_lines(window, top_line);

    tag_t rt_sos = NULLTAG;
    IFERR_ABORT(BOM_line_ask_sos(top_line, &rt_sos));
   
    int n_opts = 0;
    int *opts = NULL;
    char **paths =  NULL;
    IFERR_ABORT(BOM_sos_ask_entries(rt_sos, &n_opts, &opts, &paths));
   
    int how_set = BOM_option_set_by_user;
    IFERR_ABORT(BOM_sos_set_entry_string(rt_sos, opts[0], "", value, how_set));
    IFERR_ABORT(BOM_sos_apply(rt_sos, true));
    
    report_number_of_child_lines(window, top_line);
}

static void report_number_of_child_lines(tag_t window, tag_t top_line)
{
    IFERR_REPORT(BOM_window_hide_variants(window));
    
    int n_lines = 0;
    tag_t *lines = NULL;
    IFERR_REPORT(BOM_line_ask_child_lines(top_line, &n_lines, &lines));
    printf("\n n_lines: %d \n", n_lines);  
    if(lines) MEM_free(lines); 
}

