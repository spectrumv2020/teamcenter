static void traverse_product_structure(tag_t line, int indention)
{
    indention++;
   
    char *name = NULL;
    IFERR_REPORT(AOM_ask_value_string(line, "bl_line_name", &name));

    /* indent to show hierarchy */
    for (int ii = 0; ii < indention; ii++) printf ("  ");

    printf("%s\n", name);
    
    int n_children;
    tag_t *children = NULL;
    IFERR_REPORT(BOM_line_ask_child_lines(line, &n_children, &children));
    for (int ii = 0; ii < n_children; ii++)
        traverse_product_structure(children[ii], indention);

    if (name) MEM_free(name);
    if (children) MEM_free(children);    
}

static void send_item_revision_to_pse(tag_t item_rev)    
{       
    tag_t window = NULLTAG;    
    IFERR_REPORT(BOM_create_window (&window));
    
    tag_t rule = NULLTAG; 
    IFERR_REPORT(CFM_find("Latest Working", &rule));    
    IFERR_REPORT(BOM_set_window_config_rule(window, rule));
    
    IFERR_REPORT(BOM_set_window_pack_all(window, TRUE));
    
    tag_t top_line = NULLTAG;
    IFERR_REPORT(BOM_set_window_top_line(window, NULLTAG, item_rev, NULLTAG, &top_line));
    
    int indention = 0;
    traverse_product_structure(top_line, indention);
    IFERR_REPORT(BOM_close_window(window));
}