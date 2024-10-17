
#include <ae/dataset.h>
#include <sa/tcfile.h>

static void set_dataset_named_reference_name(tag_t dataset_tag)
{
	AE_reference_type_t reference_type;
	tag_t file_tag = NULLTAG;
	IFERR_ABORT(AE_ask_dataset_named_ref2(dataset_tag, "UGPART", &reference_type, &file_tag));
	IFERR_ABORT(AOM_refresh(file_tag, TRUE));
	IFERR_ABORT(IMF_set_original_file_name2(file_tag, "new_name.prt"));
	IFERR_ABORT(AOM_save(file_tag));
	IFERR_ABORT(AOM_refresh(file_tag, FALSE));
	IFERR_ABORT(AOM_unlock(file_tag));
}
