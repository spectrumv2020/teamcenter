#ifdef __cplusplus
extern "C" {
#endif

void find_items_by_owning_group_using_enquiry(char *owning_group, int* count, tag_t** items)
{ 
    tag_t group_tag = NULLTAG;
    IFERR_REPORT(SA_find_group(owning_group, &group_tag));

    IFERR_REPORT(POM_enquiry_create("my_enq"));

    const char *sel_attrs[1] = {"puid"};
    IFERR_REPORT(POM_enquiry_add_select_attrs("my_enq", "Item", 1, sel_attrs));

    IFERR_REPORT(POM_enquiry_set_tag_value("my_enq", "value", 1, &group_tag, 
        POM_enquiry_bind_value));

    IFERR_REPORT(POM_enquiry_set_attr_expr ("my_enq", "expression", "Item", 
        "owning_group", POM_enquiry_equal , "value"));

    IFERR_REPORT(POM_enquiry_set_where_expr ( "my_enq", "expression" ));

    int  rows = 0, columns = 0;
    void ***report;
    IFERR_REPORT(POM_enquiry_execute("my_enq", &rows, &columns, &report));
    if (rows != 0)
    {
        *count = rows;
        *items = (tag_t *) MEM_alloc ( sizeof (tag_t) * rows);
        for (int ii = 0; ii < rows; ii++ )
        {
            (*items)[ii]  = *(tag_t*)(report[ii][0]);
        }
    }    
    GTAC_free(report);
    POM_enquiry_delete("my_enq");
}

#ifdef __cplusplus
}
#endif
