
#include <ae/dataset.h>
#include <sa/tcfile.h>
#include <tccore/aom.h>
#include <sa/tcfile_cache.h>

static void replace_dataset_named_reference(tag_t dataset_tag, tag_t old_file,
            char *new_file_path, char *ref_name)
{
    IMF_file_data_p_t file_data;
    IFERR_REPORT(IMF_get_file_access(old_file, 0, &file_data));

    tag_t new_file_tag = NULLTAG;
    IFERR_REPORT(AOM_lock(old_file));
    IFERR_REPORT(IMF_replace_file_and_get_new_tag(old_file, new_file_path,
        FALSE, &new_file_tag)); 

    IFERR_REPORT(AOM_lock(dataset_tag));
    IFERR_REPORT(AE_replace_dataset_named_ref(dataset_tag, old_file, ref_name,
        AE_PART_OF, new_file_tag));

    IFERR_REPORT(AE_save_myself(dataset_tag));
    IFERR_REPORT(AOM_unload(dataset_tag));
    IFERR_REPORT(IMF_release_file_access (&file_data));

    /* assuming the old file should be deleted */
    IFERR_REPORT(AOM_lock_for_delete(old_file));
    IFERR_REPORT(AOM_delete(old_file));
}
