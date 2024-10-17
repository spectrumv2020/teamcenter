#include <stdio.h>
#include <itk/mem.h>
#include <tc/tc.h>
#include <tccore/tctype.h>

static void list_subtypes_of_type(char *type_name)
{
    int ifail = ITK_ok, n_sub_types = 0;
    tag_t type_tag = NULLTAG;
    tag_t* sub_type_tags = NULL;     
    char* sub_type_name = NULL;
	
    ifail = TCTYPE_find_type(type_name, NULL, &type_tag);
    if (ifail != ITK_ok) {/* add your error logic here */}

    ifail = TCTYPE_ask_child_types(type_tag, false, &n_sub_types, &sub_type_tags);
    if (ifail != ITK_ok) {/* add your error logic here */}
    
    for (int ii = 0; ii < n_sub_types; ii++) 
    {
        ifail = TCTYPE_ask_name2(sub_type_tags[ii], &sub_type_name);
        if (ifail != ITK_ok) {/* add your error logic here */}
        
        printf("%s \n", sub_type_name);
    }
	MEM_free(sub_type_name);
    MEM_free(sub_type_tags);
}
