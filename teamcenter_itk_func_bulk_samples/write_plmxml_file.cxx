
#include <bom/bom.h>
#include <cfm/cfm.h>

static void write_plmxml_file(tag_t item_rev)    
{
    int ifail = ITK_ok;

    tag_t window = NULLTAG;        
    IFERR_ABORT(BOM_create_window (&window));

    tag_t rule = NULLTAG;
    IFERR_ABORT(CFM_find("Latest Working--", &rule));

    IFERR_ABORT(BOM_set_window_config_rule(window, rule));
    IFERR_ABORT(BOM_set_window_pack_all(window, TRUE));

    tag_t top_line = NULLTAG;
    IFERR_ABORT(BOM_set_window_top_line(window, NULLTAG, item_rev, NULLTAG, &top_line));
    
    int n_selected = 0;
    tag_t *selected = NULL;
    IFERR_ABORT(BOM_line_ask_child_lines(top_line, &n_selected, &selected)); 

    BOM_writer_output* output = 0;
    ifail = BOM_writer_new_output_file( &output );
    output->file.filehandle = fopen("W:\\the_file.xml", "r");
    output->common.object = MEM_string_copy("W:\\the_file.xml");      
            
    BOM_writer_format* format = 0;
    IFERR_ABORT(BOM_writer_new_format_plmxml(&format));
    format->plmxml.builder_name = MEM_string_copy("AbsoluteOccurrences"); 
    format->plmxml.transform_type = TransformType_AbsOcc;  
    format->plmxml.transfer_mode = MEM_string_copy("ConfiguredDataFilesExportDefault");   
         
    BOM_writer_traversal* traversal = 0;     
    IFERR_ABORT(BOM_writer_new_traversal(&traversal));
    traversal->selected_count = n_selected; 
    traversal->selected_lines = selected; 
    traversal->no_descendants = false; 
    traversal->transient_unpack = true; 

    IFERR_ABORT(BOM_writer_write_bomwindow(window, output, format, traversal));     
    MEM_free(selected); 
    IFERR_REPORT(BOM_close_window(window));
}

