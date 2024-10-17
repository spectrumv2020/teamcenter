
#include <ps/ps.h>

static void report_occurrence_note_type_text(tag_t bvr_tag, tag_t occ_tag, char note_type_name[156])
{
    tag_t note_type_tag = NULLTAG;
    IFERR_REPORT(PS_find_note_type(note_type_name, &note_type_tag));
 
    tag_t child_item, child_bom_view;
    IFERR_REPORT(PS_ask_occurrence_child(bvr_tag, occ_tag, &child_item, 
        &child_bom_view));
    
    char *id_string = NULL;
    IFERR_REPORT(WSOM_ask_id_string(child_item, &id_string));
    
    char *note_text = NULL;
    IFERR_REPORT(PS_ask_occurrence_note_text(bvr_tag, occ_tag, note_type_tag, 
        &note_text));
    if(note_text != NULL || strlen(note_text) != 0)
    {
        ECHO("  %s - %s - %s\n", id_string, note_type_name, note_text);
    }
    if(note_text) MEM_free(note_text);
    MEM_free(id_string);
}