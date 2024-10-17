
#ifdef __cplusplus
extern "C" {
#endif

#include <bom/bom.h>
#include <cfm/cfm.h>
#include <me/me.h>
#include <property/nr.h>
#include <tccore/aom.h>
#include <tccore/item.h>
#include <tccore/workspaceobject.h>

static void clone_process_from_template(char *process_item_id)
{   
    tag_t item_tag = NULLTAG;
    IFERR_REPORT(ITEM_find_item(process_item_id, &item_tag));
    if(item_tag == NULLTAG)
    {
        ECHO("\n MEProcess NOT found! \n");
        return;      
    }
    tag_t revision_tag = NULLTAG;
    IFERR_REPORT(ITEM_ask_latest_rev(item_tag, &revision_tag ));

    MEBOM_init_module();
    
    tag_t window_tag = NULLTAG;
    IFERR_REPORT(ME_create_bop_window(&window_tag));
    
    tag_t rule_tag  = NULLTAG;
    IFERR_REPORT(CFM_find("Latest Working", &rule_tag ));
    
    IFERR_REPORT(BOM_set_window_config_rule(window_tag, rule_tag)); 

    char *next_id = NULL;
    IFERR_REPORT(NR_next_value("A2MEProcess", "item_id", NULLTAG, "", "", "", 
        NULLTAG, "", "", &next_id));
        
    tag_t clone_tag = NULLTAG; 
    IFERR_REPORT(ME_create_process_from_template(next_id, "A", 
        "MEProcess Clone", "", revision_tag , rule_tag , window_tag, 
        "Process.Template.Mapping_Consumes", &clone_tag)) ;
    
    if(next_id) MEM_free(next_id);
    
    IFERR_REPORT(AOM_refresh(clone_tag, TRUE)); 
    IFERR_REPORT(AOM_save(clone_tag));
    IFERR_REPORT(AOM_refresh(clone_tag, FALSE));
    
    char *item_id = NULL;
    IFERR_REPORT(WSOM_ask_id_string(clone_tag, &item_id));

    printf("\n\t MEProcess Clone ID: %s\n", item_id);
    IFERR_REPORT(AOM_unload(clone_tag));

    if(item_id != NULL)
        MEM_free(item_id);
}
