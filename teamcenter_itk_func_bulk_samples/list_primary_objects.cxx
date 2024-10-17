
#include <itk/mem.h>
#include <tccore/aom_prop.h>
#include <tccore/grm.h>
#include <tccore/tctype.h>

void list_displayable_properties_with_value(char *indention, tag_t object)
{
    logical
        is_displayable = TRUE;
    int
        n_props = 0,
        ii = 0;
    char
        **prop_names = NULL,
        *disp_name = NULL,
        *value = NULL;

    IFERR_REPORT( AOM_ask_prop_names(object, &n_props, &prop_names) );
    for( ii = 0; ii < n_props; ii++)
    {
        IFERR_REPORT( AOM_UIF_is_displayable(object, prop_names[ii],
            &is_displayable));
        if (is_displayable == TRUE)
        {
            value = NULL;
            IFERR_REPORT( AOM_UIF_ask_name(object, prop_names[ii], &disp_name) );
            IFERR_REPORT( AOM_UIF_ask_value(object, prop_names[ii], &value) );
            if ( (value != NULL) && (strlen(value) > 0 ) )
            {
                if (strlen(value) == 1 )
                {
                    if ( strcmp(value, " ") != 0 )
                        fprintf(stdout, "%s %s: %s \n", indention, disp_name,
                            value );
                }
                else
                fprintf(stdout, "%s %s: %s\n",  indention, disp_name, value);
            }
        }
    }
    if (prop_names != NULL) MEM_free(prop_names);
    if (disp_name != NULL) MEM_free(disp_name);
    if (value != NULL) MEM_free(value);
}

void list_primary_objects(tag_t secondary_object, char *rel_type_name)
{
	ECHO("\n\n list_primary_objects \n\n");
	
	tag_t relation_type = NULLTAG;
	if ((rel_type_name != NULL) && (strlen(rel_type_name) > 0 ))
	{
		IFERR_REPORT(GRM_find_relation_type(rel_type_name, &relation_type));
	}
	int n_objects = 0;
	tag_t *objects = NULL;
	IFERR_REPORT(GRM_list_primary_objects_only(secondary_object, relation_type, 
		&n_objects, &objects)); 
	ECHO("   n_objects: %d \n", n_objects);
	for(int ii = 0; ii < n_objects; ii++)
	{
		list_displayable_properties_with_value("        ", objects[ii]);
	}
	ECHO(" \n");
	MEM_free(objects);
}