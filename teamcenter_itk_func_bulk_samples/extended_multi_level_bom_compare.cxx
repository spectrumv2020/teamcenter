
#include <bom/bom.h>
#include <cfm/cfm.h>

static void extended_multi_level_bom_compare(tag_t item)
{
    int ifail = ITK_ok;
    
    tag_t window1 = NULLTAG;
    ifail = BOM_create_window(&window1);
    if (ifail != ITK_ok) { /* your error logic here */ }
    
    tag_t top_line1 = NULLTAG;
    ifail = BOM_set_window_top_line(window1, item, NULLTAG, NULLTAG, 
        &top_line1);
    if (ifail != ITK_ok) { /* your error logic here */ }
    
    tag_t rule1= NULLTAG;
    ifail = CFM_find("Latest Working", &rule1);
    if (ifail != ITK_ok) { /* your error logic here */ }
    
    ifail = BOM_set_window_config_rule(window1, rule1);
    if (ifail != ITK_ok) { /* your error logic here */ }

    tag_t window2 = NULLTAG;
    ifail = BOM_create_window(&window2);
    if (ifail != ITK_ok) { /* your error logic here */ }
    
    tag_t top_line2 = NULLTAG;
    ifail = BOM_set_window_top_line(window2, item, NULLTAG, NULLTAG, 
        &top_line2);
    if (ifail != ITK_ok) { /* your error logic here */ }
    
    tag_t rule2 = NULLTAG;
    ifail = CFM_find("Any status; No Working", &rule2);
    if (ifail != ITK_ok) { /* your error logic here */ }
    
    ifail = BOM_set_window_config_rule(window2, rule2);
    if (ifail != ITK_ok) { /* your error logic here */ }
    
    ifail = BOM_compare_execute(NULLTAG, top_line1, top_line2, 
        "IMAN_bcm_ext_var_level", BOM_compare_output_report);
    if (ifail != ITK_ok) { /* your error logic here */ }
    
    int report_length = 0;
    char** report_lines = 0;
    tag_t* report_items = 0;
    ifail = BOM_compare_report(top_line1, &report_length, &report_lines, 
        &report_items);
    if (ifail != ITK_ok) { /* your error logic here */ }
    for(int ii = 0; ii < report_length; ii++)
    {
        printf("\n %s \n", report_lines[ii]);
    }
    if(report_lines) MEM_free(report_lines);
    if(report_items) MEM_free(report_items);
    ifail = BOM_close_window(window1);
    if (ifail != ITK_ok) { /* your error logic here */ }
    
    ifail = BOM_close_window(window2);
    if (ifail != ITK_ok) { /* your error logic here */ }
}
  