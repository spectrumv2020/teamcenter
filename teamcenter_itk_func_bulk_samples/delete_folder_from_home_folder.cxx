
#include <pom/pom/pom.h>
#include <sa/user.h>
#include <tc/folder.h>
#include <tccore/aom.h>

static void delete_folder_from_home_folder(tag_t  folder_tag)
{  
    int ifail = ITK_ok;
    
    ifail = AOM_refresh(folder_tag, TRUE);
	if (ifail != ITK_ok) { /* your error logic here */ }
    
    FL_sort_criteria_t sort = FL_fsc_by_name;
    int n_contents = 0;
    tag_t *contents = NULL;
    ifail = FL_ask_references(folder_tag, sort, &n_contents, &contents);
	if (ifail != ITK_ok) { /* your error logic here */ }
    for (int ii = 0; ii < n_contents; ii++)
    {
        ifail = FL_remove(folder_tag, contents[ii]);
        if (ifail != ITK_ok) { /* your error logic here */ }
    }
    ifail = AOM_save(folder_tag);
	if (ifail != ITK_ok) { /* your error logic here */ }
    
    ifail = AOM_refresh(folder_tag, FALSE);
	if (ifail != ITK_ok) { /* your error logic here */ }

    char *user_name_string = NULL;
    tag_t user_tag = NULLTAG;
    ifail = POM_get_user(&user_name_string, &user_tag);
	if (ifail != ITK_ok) { /* your error logic here */ }
    
    MEM_free(user_name_string);
    
    tag_t home_folder_tag = NULLTAG;
    ifail = SA_ask_user_home_folder(user_tag, &home_folder_tag);
	if (ifail != ITK_ok) { /* your error logic here */ }
      
    ifail = AOM_refresh( home_folder_tag, TRUE);
	if (ifail != ITK_ok) { /* your error logic here */ }
    
    ifail = FL_remove(home_folder_tag, folder_tag);
	if (ifail != ITK_ok) { /* your error logic here */ }
    
    ifail = AOM_save(home_folder_tag);
	if (ifail != ITK_ok) { /* your error logic here */ }
    
    ifail = AOM_refresh( home_folder_tag, FALSE);
	if (ifail != ITK_ok) { /* your error logic here */ }
     
    ifail = AOM_lock_for_delete(folder_tag);
	if (ifail != ITK_ok) { /* your error logic here */ }
    
    ifail = AOM_delete(folder_tag);
	if (ifail != ITK_ok) { /* your error logic here */ }   
}
