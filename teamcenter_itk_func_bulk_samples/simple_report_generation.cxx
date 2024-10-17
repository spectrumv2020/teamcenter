
#include <itk/mem.h>
#include <qry/crf.h>

static void simple_report_generation(char *reportId)
{
	int n_tags = 0;  
	tag_t *report_definition_tags; 
	IFERR_REPORT(CRF_get_report_definitions(reportId, NULL, NULL, NULL, 0, 0,
		NULL, &n_tags, &report_definition_tags));

	/* ReportID must be unique, only one will exist */
	tag_t report_tag = report_definition_tags[0];
	char * report_path_name = NULL;
	IFERR_REPORT(CRF_generate_report(report_tag, NULLTAG, 0, NULL, 0, NULL,
		NULL, NULL, &report_path_name));

	char *transientVolumeDir = NULL;
	char *copiedFileName = NULL;
	IFERR_REPORT(CRF_copy_report_to_transient_vol(report_path_name, 
		&transientVolumeDir, &copiedFileName));

	ECHO("\n report_path_name: %s \n", report_path_name);
	ECHO(" transientVolumeDir: %s \n", transientVolumeDir);
	ECHO(" copiedFileName: %s \n", copiedFileName);

	MEM_free(report_definition_tags);
	MEM_free(report_path_name);
	MEM_free(transientVolumeDir);
	MEM_free(copiedFileName);
}
