#include <tccore/license.h>
#include <tccore/workspaceobject.h>
#include <tccore/aom.h>

static void report_wso_licenses(tag_t wsoTag)
{
    int ifail = ITK_ok;
    
    char *name = NULL;
	ifail = WSOM_ask_object_id_string(wsoTag, &name);
    if (ifail != ITK_ok) {/* your error code logic here */}
    printf("\n %s \n", name);
    
    int n_licenses = 0;
    tag_t *licenses = NULL;
    ifail = WSOM_ask_licenses(wsoTag, &n_licenses, &licenses);
    if (ifail != ITK_ok) {/* your error code logic here */}
    for (int ii = 0; ii < n_licenses; ii++)
    {
        char *license_id = NULL;
        ifail = ADA_ask_license_id2(licenses[ii], &license_id);
        if (ifail != ITK_ok) {/* your error code logic here */}
        printf("    license_id: %s \n", license_id);
        if(license_id) MEM_free(license_id);       
     }
    if(name) MEM_free(name); 
    if(licenses) MEM_free(licenses); 
}

static void attach_ADA_license_to_WorkspaceObject(tag_t wsoTag, char license_id[ADA_license_id_size_c])
{
    int ifail = ITK_ok;
    
    tag_t licenseTag = NULLTAG;
    ifail = ADA_find_license(license_id, &licenseTag);
    if (ifail != ITK_ok) {/* your error code logic here */}
    
    ifail = ADA_add_license_object2(licenseTag, wsoTag, NULL);
    if (ifail != ITK_ok) {/* your error code logic here */}
    
    report_wso_licenses(wsoTag);
}
