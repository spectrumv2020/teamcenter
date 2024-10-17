
#include <sa/workcontext.h>
#include <pom/enq/enq.h>
/*
	The term default project is a little misleading.  It is simply the project used for the last login session.
*/
static int get_user_default_project(tag_t the_user, tag_t *project_tag)
{
	
	int ifail = ITK_ok;
	const char* user_context_enq ="Find user context for the given user";
	const char *sel_attrs[1] = {"puid"};

	if ((ifail = POM_enquiry_create(user_context_enq)) != ITK_ok)
	{
		POM_enquiry_delete(user_context_enq);
		return ifail;
	}
	
	if ((ifail = POM_enquiry_add_select_attrs(user_context_enq, "TC_UserContext", 1, sel_attrs)) != ITK_ok)
	{
		POM_enquiry_delete(user_context_enq);
		return ifail;
	}
	
	if ((ifail = POM_enquiry_set_distinct(user_context_enq, true)) != ITK_ok)
	{
		POM_enquiry_delete(user_context_enq);
		return ifail;
	}	
	
	if ((ifail = POM_enquiry_set_tag_value(user_context_enq, "userval", 1, &the_user, POM_enquiry_bind_value)) != ITK_ok)
	{
		POM_enquiry_delete(user_context_enq);
		return ifail;
	}

	if ((ifail = POM_enquiry_set_attr_expr(user_context_enq, "expr1", "TC_UserContext", "user", POM_enquiry_equal,"userval")) != ITK_ok)
	{
		POM_enquiry_delete(user_context_enq);
		return ifail;
	}

	if ((ifail = POM_enquiry_set_where_expr(user_context_enq, "expr1")) != ITK_ok)
	{
		POM_enquiry_delete(user_context_enq);
		return ifail;
	}
	
    int n_rows = 0;
	int n_cols = 0;
    void *** values;	
	if ((ifail = POM_enquiry_execute(user_context_enq, &n_rows, &n_cols, &values)) != ITK_ok)
    {
        return ifail;
    }
	
	tag_t usercontext_tag = NULLTAG;
    if( n_rows > 0 )
    {
        usercontext_tag = *((tag_t *)values[0][0]);
        MEM_free ( values );
    }

	IFERR_ABORT(AOM_ask_value_tag(usercontext_tag, "current_project", project_tag));
	return ifail;
}
