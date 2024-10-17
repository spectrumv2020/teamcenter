
#include <pom/pom/pom.h>

static void getSignoffAuditRecords(tag_t task_tag, tag_t signoff_tag, int *n_records, tag_t **records)
{
    tag_t class_tag = NULLTAG;
    POM_class_id_of_class( "Fnd0WorkflowAudit", &class_tag);
    
    tag_t object_attr = NULLTAG;
    POM_attr_id_of_attr("fnd0Object", "Fnd0WorkflowAudit", &object_attr);
    
    tag_t signoff_attr = NULLTAG;
    POM_attr_id_of_attr("fnd0Signoff", "Fnd0WorkflowAudit", &signoff_attr);
    
    tag_t userid_attr = NULLTAG;
    POM_attr_id_of_attr("fnd0UserId", "Fnd0WorkflowAudit", &userid_attr);
    
    tag_t logged_date_attr  = NULLTAG;
    POM_attr_id_of_attr( "fnd0LoggedDate",  "Fnd0WorkflowAudit", &logged_date_attr );
    
    tag_t find_task = NULLTAG;   
    POM_create_enquiry_on_tag (class_tag, object_attr, POM_is_equal_to, &task_tag, &find_task);
    
    tag_t find_signoff = NULLTAG;
    POM_create_enquiry_on_tag (class_tag, signoff_attr, POM_is_equal_to, &signoff_tag, &find_signoff);
    
    tag_t find_task_signoffs = NULLTAG;
    POM_combine_enquiries( find_task , POM_and , find_signoff , &find_task_signoffs);
    
    int n_sort_attrs = 1;
    int sort_order[1] = {POM_order_ascending};
    POM_order_enquiry( find_task_signoffs, 1, &logged_date_attr, sort_order );

    int n_found = 0;
    tag_t *found = NULL;
    POM_execute_enquiry(find_task_signoffs, &n_found, &found);
    *n_records = n_found;
    (*records) = (tag_t *) MEM_alloc ( sizeof (tag_t) * n_found);
    for(int ii = 0;  ii < n_found; ii++)
    {
        (*records)[ii] = found[ii];
    }  
    if(n_found > 0) MEM_free(found); 
}
