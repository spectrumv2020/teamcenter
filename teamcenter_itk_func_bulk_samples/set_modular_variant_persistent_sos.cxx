
/******************************************************************************
  There are two kinds of option sets: 
   - Runtime or selected option set is a instance of type BOMsos. 
     Every BOMLine has a selected option set.   
   
   - Persistent or stored option set is a database instance of type 
     StoredOptionSet. 
     
  This sample finds and applies a stored option set to top BOMLine.
******************************************************************************/
#include <bom/bom.h>
#include <itk/mem.h>
#include <tccore/aom_prop.h>
#include <tccore/grm.h>
#include <tccore/workspaceobject.h>

static void report_number_of_child_lines(tag_t window, tag_t top_line);
static tag_t find_stored_option_set_by_name(tag_t revision, char *db_sos_name);

static void set_modular_variant_persistent_sos(tag_t window, char *db_sos_name)
{
    tag_t top_line = NULLTAG;
    IFERR_ABORT(BOM_ask_window_top_line(window, &top_line));

    tag_t revision = NULLTAG;
    IFERR_ABORT(AOM_ask_value_tag(top_line, "bl_revision", &revision));
   
    tag_t db_sos = find_stored_option_set_by_name(revision, db_sos_name);
   
    tag_t rt_sos = NULLTAG;
    IFERR_ABORT(BOM_line_ask_sos(top_line, &rt_sos));
    
    tag_t var_config = NULLTAG;
    IFERR_ABORT(BOM_create_variant_config(NULLTAG, 1, &rt_sos, &var_config));       
    IFERR_ABORT(BOM_sos_db_read(db_sos, var_config));  
    IFERR_ABORT(BOM_variant_config_apply(var_config));    
}

static tag_t find_stored_option_set_by_name(tag_t revision, char *db_sos_name)
{
    tag_t relation_type = NULLTAG;
    IFERR_ABORT(GRM_find_relation_type("IMAN_reference", &relation_type));
    
    int n_objs = 0;
    tag_t *objs = NULL;
    IFERR_ABORT(GRM_list_secondary_objects_only(revision, relation_type, 
        &n_objs, &objs)); 
        
    tag_t db_sos = NULLTAG;
    for(int ii = 0; ii < n_objs; ii++)
    {
        char type[WSO_name_size_c + 1] = "";
        IFERR_ABORT(WSOM_ask_object_type(objs[ii], type));       
        if (strcmp(type,"StoredOptionSet") == 0)
        {
            char *name = NULL;
            IFERR_ABORT(WSOM_ask_name2(objs[ii], &name));
            if (strcmp(name, db_sos_name ) == 0) db_sos = objs[ii];
        }
    }
    if (objs)MEM_free(objs);
    return db_sos;
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

