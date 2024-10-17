#include <bom/bom.h>

static void set_bom_window_legacy_variant_option(tag_t window, char *target_opt, char *target_value)
{
    tag_t topline = NULLTAG;
    IFERR_REPORT(BOM_ask_window_top_line(window, &topline));
    
    IFERR_REPORT(BOM_window_hide_variants(window));
    
    int n_lines = 0;
    tag_t *lines = NULL;
    IFERR_REPORT(BOM_line_ask_child_lines(topline, &n_lines, &lines));
    printf("\n Before setting option n_lines: %d \n", n_lines);
       
    int n_opts = 0;
    tag_t *opts = NULL;
    tag_t *opt_revs = NULL;
    IFERR_ABORT(BOM_window_ask_options(window, &n_opts, &opts, &opt_revs));
    for (int i = 0; i < n_opts; i++)
    {
        tag_t owning_item = NULLTAG;
        char *name = NULL;
        char *desc = NULL;
        IFERR_ABORT(BOM_ask_option_data(opts[i], &owning_item, &name, &desc));

        if(strcmp(target_opt, name) == 0)
        {
            int n_values = 0; 
            int *index = NULL;
            IFERR_ABORT(BOM_list_option_rev_values(opt_revs[i], &n_values, 
                &index));
            printf("    n_values: %d \n", n_values);     
            for(int k = 0; k < n_values; k++)
            {                             
                char *value = NULL;
                IFERR_ABORT(BOM_ask_option_rev_value(opt_revs[i], index[k],
                    &value));           
                if(strcmp(target_value, value) == 0)
                {
                    IFERR_ABORT(BOM_window_set_option_value(window, opts[i], 
                        index[k]));
                }
                if (value) MEM_free(value);
            }
            if(index) MEM_free(index);
        }     
        if(name) MEM_free(name);
        if(desc) MEM_free(desc);
        
    }  
    if(opts) MEM_free(opts);
    if(opt_revs) MEM_free(opt_revs);
    
    IFERR_REPORT(BOM_line_ask_child_lines(topline, &n_lines, &lines));
    printf("\n After setting option n_lines: %d \n", n_lines);
    if(lines) MEM_free(lines); 
}

