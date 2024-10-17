
#include <bom/bom.h>
static void report_existing_bom_window_legacy_variant_options(tag_t window)
{
    int n_opts = 0;
    tag_t *opts = NULL;
    tag_t *opt_revs = NULL;
    IFERR_ABORT(BOM_window_ask_options(window, &n_opts, &opts, &opt_revs));
    printf("  n_opts = %d \n", n_opts);
    for (int i = 0; i < n_opts; i++)
    {
        tag_t owning_item = NULLTAG;
        char *name = NULL;
        char *desc = NULL;
        IFERR_ABORT(BOM_ask_option_data(opts[i], &owning_item, &name, &desc));
        printf("\n    name: %s - desc: %s \n", name, desc);
        
        int n_values = 0; 
        int *ind = NULL;
        IFERR_ABORT(BOM_list_option_rev_values(opt_revs[i], &n_values, &ind));
        printf("    n_values: %d \n", n_values);     
        for(int k = 0; k < n_values; k++)
        {                             
            char *value = NULL;
            IFERR_ABORT(BOM_ask_option_rev_value(opt_revs[i], ind[k],
                &value));          
            printf("      index: %d - value: %s \n", ind[k], value);           
            if (value) MEM_free(value);
        }
        if(ind) MEM_free(ind);
        if(name) MEM_free(name);
        if(desc) MEM_free(desc);
    }  
    if(opts) MEM_free(opts);
    if(opt_revs) MEM_free(opt_revs);
}

