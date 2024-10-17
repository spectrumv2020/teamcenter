
int custom_action_handler(EPM_action_message_t msg)
{
    int ifail = ITK_ok;
    int ii = 0;
    tag_t root_task = NULLTAG;
    int n_attachs = 0;
    tag_t* attachs = NULL;
    tag_t rev_tag = NULLTAG;
    tag_t relation_type_tag = NULLTAG;
    int n_specs = 0;
    tag_t* specs = NULL;
    char type_name[TCTYPE_name_size_c+1] = "";
    tag_t type_tag = NULLTAG;
    int n_refs = 0;
    tag_t* refs = NULL;
    
    /* Target attachment types should use the root task */    
    ifail = EPM_ask_root_task(msg.task, &root_task);
    if (ifail != ITK_ok) {/* your error logic here */ }
    
    ifail = EPM_ask_attachments(root_task, EPM_target_attachment, &n_attachs, &attachs);
    if (ifail != ITK_ok) {/* your error logic here */ }
    /* Assuming one ItemRevision taget attachment */
    rev_tag = attachs[0];
    
    ifail = GRM_find_relation_type("IMAN_specification", &relation_type_tag);
    if (ifail != ITK_ok) { /* your error logic here */ }
    
    ifail = GRM_list_secondary_objects_only(rev_tag, relation_type_tag, &n_specs, &specs);
    if (ifail != ITK_ok) { /* your error logic here */ }
    
    for (ii = 0; ii < n_specs; ii++)
    {
        ifail = TCTYPE_ask_object_type (specs[ii], &type_tag);
        if (ifail != ITK_ok) { /* your error logic here */ }
        
        ifail = TCTYPE_ask_name(type_tag, type_name);
        if (ifail != ITK_ok) { /* your error logic here */ }
        if (strcmp(type_name, "Text") == 0)
        {
            ifail = AE_ask_all_dataset_named_refs(specs[ii], "Text", &n_refs, &refs);
            if (ifail != ITK_ok) { /* your error logic here */ }
            
            ifail = AE_export_named_ref(specs[ii], "Text", "C:\\Temp\\export_text.dat");
            if (ifail != ITK_ok) { /* your error logic here */ }
        }
    }

    if(attachs) MEM_free(attachs);
    if(specs) MEM_free(specs);
    if(refs) MEM_free(refs);
    return ifail;
}
