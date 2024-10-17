static void set_item_revisions_master_form_property(tag_t item_revision, 
    char *property_name, char *property_value)
{
    int
        n_secondary_objects = 0;
    tag_t
        relation = NULLTAG,
        *secondary_objects = NULL,
        irm_form = NULLTAG;
  
    ERROR_CHECK(GRM_find_relation_type("IMAN_master_form", &relation));
    ERROR_CHECK(GRM_list_secondary_objects_only(item_revision, relation, 
        &n_secondary_objects, &secondary_objects) );
    
    irm_form = secondary_objects[0]; /* should always be just one */
    if (secondary_objects) MEM_free(secondary_objects);

    ERROR_CHECK(AOM_refresh(irm_form, TRUE)); /* reload and lock for modify */
    ERROR_CHECK(AOM_set_value_string(irm_form, property_name, property_value));
    ERROR_CHECK(AOM_save(irm_form));
    ERROR_CHECK(AOM_unload(irm_form));
}