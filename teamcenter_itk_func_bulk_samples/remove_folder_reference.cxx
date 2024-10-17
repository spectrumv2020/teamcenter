
#include <pom/pom/pom.h>
#include <tc/folder.h>
#include <tccore/aom.h>

static logical is_descendant_of_folder(tag_t object_tag)
{
    tag_t parent_class = NULLTAG;
    IFERR_REPORT(POM_class_id_of_class("Folder", &parent_class));
        
    tag_t class_tag = NULLTAG;
    IFERR_REPORT(POM_class_of_instance(object_tag, &class_tag));
    
    logical verdict = FALSE;
    IFERR_REPORT(POM_is_descendant(parent_class, class_tag, &verdict));
	return verdict;
}

static void remove_folder_reference(tag_t object_tag, tag_t folder)
{
	if (is_descendant_of_folder(folder))
	{
	    IFERR_REPORT(AOM_refresh(folder, TRUE));
        IFERR_REPORT(FL_remove(folder, object_tag));
        IFERR_REPORT(AOM_save(folder));
        IFERR_REPORT(AOM_refresh(folder, FALSE));
	}
	else
	{
		ECHO("\n  Object is not a Folder or subtype of Folder \n\n");
	}
}
