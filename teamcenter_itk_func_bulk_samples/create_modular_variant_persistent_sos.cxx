
/******************************************************************************
  There are two kinds of option sets: 
   - Runtime or selected option set is a instance of type BOMsos. 
     Every BOMLine has a selected option set.   
   
   - Persistent or stored option set is a database instance of type 
     StoredOptionSet. 
     
  This sample creates a stored option set.
******************************************************************************/

#include <bom/bom.h>
#include <tccore/aom_prop.h>
#include <tccore/grm.h>

static void create_modular_variant_persistent_sos(tag_t window, 
                char *opt_name, char *value)
{
    tag_t top_line = NULLTAG;
    IFERR_ABORT(BOM_ask_window_top_line(window, &top_line));
    
    tag_t rt_sos = NULLTAG;
    IFERR_ABORT(BOM_line_ask_sos(top_line, &rt_sos));
               
    tag_t var_config = NULLTAG;
    IFERR_ABORT(BOM_create_variant_config(NULLTAG, 1, &rt_sos, &var_config));

    tag_t item = NULLTAG;
    IFERR_ABORT(AOM_ask_value_tag(top_line, "bl_item", &item));
   
    int opt = 0;
    IFERR_ABORT(BOM_item_ask_option_handle(window, item, opt_name, &opt));
    
    int how_set = BOM_option_set_by_user;
    IFERR_ABORT(BOM_sos_set_entry_string(rt_sos, opt, "", value, how_set));
    
    int opt_name_length = strlen(opt_name) + strlen(value) + 2;
    char *db_sos_name = NULL;
    db_sos_name = (char *) MEM_alloc(opt_name_length * sizeof(char));
    sprintf(db_sos_name, "%s - %s", opt_name, value);

    tag_t db_sos = NULLTAG;
    IFERR_ABORT(BOM_sos_db_create(db_sos_name, var_config, &db_sos));
    IFERR_ABORT(AOM_save(db_sos));
    MEM_free(db_sos_name);

    /* To save sos as References (IMAN_refrence) to the revision */
    tag_t revision = NULLTAG;
    IFERR_ABORT(AOM_ask_value_tag(top_line, "bl_revision", &revision));
  
    tag_t rel_type = NULLTAG;
    IFERR_ABORT(GRM_find_relation_type( "IMAN_reference", &rel_type));
    
    tag_t relation  = NULLTAG;
    IFERR_ABORT(GRM_create_relation(revision, db_sos, rel_type, NULLTAG, &relation));
    IFERR_ABORT(GRM_save_relation(relation));  
}
