
#include <tccore/grm.h>
#include <tccore/grmtype.h>
#include <bom/bom.h>
#include <ps/vrule.h>

static void create_saved_variant_rule(tag_t window, tag_t rev_tag, char *target_value)
{      
    int ifail = ITK_ok;
    tag_t vrule_tag = NULLTAG;
    ifail = BOM_window_ask_variant_rule(window, &vrule_tag);  
    if (ifail != ITK_ok) { /* your error logic here */ }

    int n_options = 0;
    tag_t *options = NULL;
    tag_t *option_revs = NULL;
    ifail = BOM_variant_rule_ask_options(vrule_tag, &n_options, &options , &option_revs);
    if (ifail != ITK_ok) { /* your error logic here */ }
    
    /* assuming only one */
    tag_t opt_tag = options[0];
    tag_t opt_rev_tag = option_revs[0];
    
    if(options) MEM_free(options);
    if(option_revs) MEM_free(option_revs);
  
    int n_values = 0; 
    int *index = NULL;
    ifail = BOM_list_option_rev_values(opt_rev_tag, &n_values, &index);
    if (ifail != ITK_ok) { /* your error logic here */ }
    for(int ii = 0; ii < n_values; ii++)
    {                             
        char *value = NULL;
        ifail = BOM_ask_option_rev_value(opt_rev_tag, index[ii], &value); 
        if (ifail != ITK_ok) { /* your error logic here */ }
        if(strcmp(target_value, value) == 0)      
        {
            ifail = BOM_variant_rule_set_option_values(vrule_tag , opt_tag, 1, &index[ii]);
            if (ifail != ITK_ok) { /* your error logic here */ }
                       
            tag_t saved_vrule_tag = NULLTAG;
            ifail = VRULE_create_from_variant_rule("MyVarRule", "", vrule_tag, 1, &opt_tag,  &saved_vrule_tag);
            if (ifail != ITK_ok) { /* your error logic here */ }
            
            tag_t relation_type_tag = NULLTAG;
            ifail = GRM_find_relation_type( "IMAN_specification", &relation_type_tag);
            if (ifail != ITK_ok) { /* your error logic here */ }
            
            tag_t relation_tag = NULLTAG;
            ifail = GRM_create_relation(rev_tag, saved_vrule_tag , relation_type_tag, NULLTAG, &relation_tag);
            if (ifail != ITK_ok) { /* your error logic here */ }
            
            ifail = GRM_save_relation(relation_tag);
            if (ifail != ITK_ok) { /* your error logic here */ }
        }
        if(value) MEM_free(value);
    }
    if(index) MEM_free(index);
}

