
#include <ae/datasettype.h>
#include <itk/mem.h>

void list_dataset_type_file_references(char *dataset_type_name)
{
	tag_t  dataset_type = NULLTAG;
    IFERR_REPORT(AE_find_datasettype(dataset_type_name, &dataset_type)); 

	if (dataset_type == NULLTAG)
	{
		ECHO("\n\n Dataset Type \"%s\" NOT found!\n\n", dataset_type_name); 
        return;
	}

	int n_refs = 0;
	char **refs = NULL;
    IFERR_REPORT(AE_ask_datasettype_refs(dataset_type, &n_refs, &refs));

	ECHO("\n Dataset Type: %s - Number of Referencers: %d \n\n", 
		dataset_type_name, n_refs);

	for (int ii = 0; ii < n_refs; ii++)
	{
		int n_file_refs = 0;
		char **file_type = NULL;  
		char **format  = NULL; 
		IFERR_REPORT(AE_ask_datasettype_file_refs(dataset_type, refs[ii], 
			&n_file_refs, &file_type, &format)); 
		for (int kk = 0; kk < n_file_refs; kk++)
		{
			ECHO("   %s \t %s \t %s \n", refs[ii], file_type[kk], format[kk]);
		}
		MEM_free(file_type);
		MEM_free(format);
	}
	MEM_free(refs);
}
