
#include <sa/tcvolume.h>
#include <sa/tcfile.h>
#include <sa/tcfile_cache.h>

static void report_full_ImanFile_path_on_disk(tag_t fileTag)
{  
    tag_t volumeTag = NULLTAG;
    IFERR_ABORT(IMF_ask_volume(fileTag, &volumeTag));
    
    int machineTypeTag;
    IFERR_ABORT(VM_ask_machine_type(volumeTag, &machineTypeTag));
    
    char *filePath = NULL;
    IFERR_ABORT(IMF_ask_file_pathname2(fileTag, machineTypeTag, &filePath));
    
    printf("\n File Path: %s \n", filePath);
    if(filePath) MEM_free(filePath);
}    

static void report_ImanFile_cached_file_path(tag_t fileTag)
{        
    IMF_file_data_p_t file_data;
    IFERR_REPORT(IMF_get_file_access(fileTag, 0, &file_data));
    
    char *fcPath = NULL;
    IFERR_REPORT(IMF_get_filename(file_data, &fcPath));
    
    printf("\n File Cache Path: %s \n", fcPath);
    
    if(fcPath) MEM_free(fcPath);
}

static void report_ImanFile_original_file_name(tag_t fileTag)
{  
    /* Name shown in the Dataset Named Reference Dialog */
    char *namedRefName = NULL;
    IFERR_ABORT(IMF_ask_original_file_name2(fileTag, &namedRefName));
    
    printf("\n Named Reference Name: %s \n", namedRefName);
    
    if(namedRefName) MEM_free(namedRefName);    
}

