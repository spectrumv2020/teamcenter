
#include <pom/enq/enq.h>

static void find_all_ChangeRequestRevision_with_problem_items(void)
{
    IFERR_ABORT(POM_enquiry_create("enqid"));
    const char *select_attrs_Revision[] = { "puid" };
    const char *select_attrs_Relation[] = { "secondary_object" };
    IFERR_ABORT(POM_enquiry_add_select_attrs("enqid", "ChangeRequestRevision",
        1, select_attrs_Revision));
    IFERR_ABORT(POM_enquiry_add_select_attrs("enqid", "CMHasProblemItem", 1,
        select_attrs_Relation));
    
    /* limit ChangeRequestRevision.puid == CMHasProblemItem.primary_object */
    IFERR_ABORT(POM_enquiry_set_join_expr("enqid", "join_expr", 
        "ChangeRequestRevision", "puid", POM_enquiry_equal, 
        "CMHasProblemItem", "primary_object"));
    IFERR_ABORT(POM_enquiry_set_where_expr("enqid", "join_expr"));   
   
    int n_rows = 0;
    int n_cols = 0;  
    void ***report = NULL;
    IFERR_ABORT(POM_enquiry_execute("enqid", &n_rows, &n_cols, &report));
    printf("\n\t Change Revision \t\t Problem Item\n");
    printf("\t =============== \t\t ============");
    for (int row = 0; row < n_rows; row++)
    {   
        printf("\n");     
        for (int col = 0; col < n_cols; col++)
        {
            tag_t object_tag = *((tag_t *)(report[row][col]));
            
            char *object_string;
            IFERR_REPORT(WSOM_ask_object_id_string(object_tag, &object_string));
            printf("\t %s \t", object_string);
            MEM_free(object_string);
        }
    }
}
