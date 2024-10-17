/*HEAD IS_WORKSPACE_OBJECT CCC ITK */

#include <pom/pom/pom.h>

static logical is_WorkspaceObject(tag_t object)
{
	tag_t wso_class = NULLTAG;
	IFERR_REPORT(POM_class_id_of_class("WorkspaceObject", &wso_class));
		
	tag_t class_tag = NULLTAG;
	IFERR_REPORT(POM_class_of_instance(object, &class_tag));
	
	logical verdict = FALSE;
	IFERR_REPORT(POM_is_descendant(wso_class, class_tag, &verdict));

	return (verdict);
}
