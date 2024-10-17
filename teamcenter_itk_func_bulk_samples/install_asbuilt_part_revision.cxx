#include <asbuilt/asbuilt.h>
#include <mrocore/mrocore.h>

static tag_t get_physical_part_rev(tag_t partRev)
{
    tag_t relation = NULLTAG;
    ifail = GRM_find_relation_type("PhysicalRealization", &relation);
    if(ifail != ITK_ok) {/* add your error logic here */}
    
    int n_primary_object_tags = 0;
    tag_t *primary_object_tags = NULL;
    ifail = GRM_list_primary_objects_only(partRev, relation,
        &n_primary_object_tags, &primary_object_tags);
    if(ifail != ITK_ok) {/* add your error logic here */}       

    /* just one in this case */
    tag_t physPartRev = primary_object_tags[0];
    if (primary_object_tags) MEM_free(primary_object_tags);
 
    return physPartRev;   
}

static void install_asbuilt_part_revision(tag_t parentPart, tag_t childPart, 
    tag_t devDoc)
{       
    int ifail = ITK_ok; 
 
    date_t set_date;
    ifail = ITK_string_to_date( "21-Jun-2016 10:52", &set_date); 
    if(ifail != ITK_ok) {/* add your error logic here */}
     
    tag_t parentPartRev = NULLTAG;
    ifail = ITEM_ask_latest_rev(parentPart, &parentPartRev);
    if(ifail != ITK_ok) {/* add your error logic here */}
        
    tag_t parentPhysPartRev = get_physical_part_rev(parentPartRev);
    
    tag_t parentPhysPart = NULLTAG;
    ifail = ITEM_ask_item_of_rev(parentPhysPartRev, &parentPhysPart); 
    if(ifail != ITK_ok) {/* add your error logic here */}
    
    tag_t childPartRev = NULLTAG;
    ifail = ITEM_ask_latest_rev(childPart, &childPartRev);
    if(ifail != ITK_ok) {/* add your error logic here */}
    
       
    /************************************************************************** 
        Begin ITK equivalent of User Interface actions
            Select Part-> RMB-> Send To-> As-Built Manager 
    **************************************************************************/
    tag_t  neutralWindow  = NULLTAG;
    ifail = MROCORE_create_window (&neutralWindow );
    if(ifail != ITK_ok) {/* add your error logic here */}
        
    tag_t neutralBOMLine = NULLTAG; 
    ifail = MROCORE_set_window_top_line(neutralWindow, childPart, 
        childPartRev, NULLTAG, &neutralBOMLine); 
    if(ifail != ITK_ok) {/* add your error logic here */}    

    
    /************************************************************************** 
        Begin ITK equivalent of User Interface actions 
            Select NeutralBOMLine-> RMB-> Generate As-Built Structure
    **************************************************************************/
    char *itemId = NULL;
    ifail = ITEM_ask_id2(childPart, &itemId);
        
    GenerateStructureParams generateParams;
    generateParams.partNumber = itemId;
    generateParams.serialNumber = "";
    generateParams.lotNumber = "";
    generateParams.manufacturerId = "Acme";   
    generateParams.structureContext =  itemId;
    generateParams.installationDate = set_date;
    generateParams.manufactureDate = set_date;
    generateParams.useSrlNoGen = true;
    generateParams.numberOfLevels = -1;
    generateParams.physicalQuantity  = 1;   
    tag_t childPhysPartRev = NULLTAG;
    ifail = ASBUILT_gen_phystruc(neutralBOMLine, &generateParams, 
        &childPhysPartRev);
    if(ifail != ITK_ok) {/* add your error logic here */}
        
    tag_t childPhysPart = NULLTAG;
    ifail = ITEM_ask_item_of_rev(childPhysPartRev, &childPhysPart);
    if(ifail != ITK_ok) {/* add your error logic here */}
    
    ifail = BOM_close_window(neutralWindow);
    if(ifail != ITK_ok) {/* add your error logic here */}   
 
 
    /************************************************************************** 
        Begin ITK equivalent of User Interface actions 
            Select AsBulitBOMLIne that represents the PhysicalPartRevision 
            to be installed -> RMB-> Copy to Clipboard
    **************************************************************************/    
    tag_t childWindow = NULLTAG;
    ifail = MROCORE_create_window(&childWindow); 
    if(ifail != ITK_ok) {/* add your error logic here */}
       
    tag_t  selectedBOMLine = NULLTAG;
    ifail = ASBUILT_line_set_window_top(childWindow, NULLTAG, childPhysPartRev,
        &selectedBOMLine); 
    if(ifail != ITK_ok) {/* add your error logic here */}
    
    
    /************************************************************************** 
        Begin ITK equivalent of User Interface actions 
            Select PhysicalPartRevision to get the installed AsBuiltBOMLine-> 
                RMB-> Send To-> As-Built Manager 
            Select AsBulitBOMLIne-> RMB-> Setup Deviation
        And release the DeviationDoc.
    **************************************************************************/         
    int markpoint;
    logical state_has_changed;
    ifail = POM_place_markpoint(&markpoint);
    if(ifail != ITK_ok) {/* add your error logic here */}    
           
    tag_t parentWindow = NULLTAG;
    ifail = MROCORE_create_window(&parentWindow);  
    if(ifail != ITK_ok) {/* add your error logic here */}   
    
    tag_t  parentBOMLine = NULLTAG;
    ifail = ASBUILT_line_set_window_top(parentWindow, parentPhysPart, 
        parentPhysPartRev, &parentBOMLine); 
    if(ifail != ITK_ok) {/* add your error logic here */}
                 
    tag_t allowedDeviation = NULLTAG;
    ifail = MROCORE_setup_deviation(parentPhysPart, childPhysPart, 
        devDoc, &allowedDeviation);
    if(ifail != ITK_ok) {/* add your error logic here */}   

    tag_t release_stat = NULLTAG;
    ifail = RELSTAT_create_release_status( "TCM Released", &release_stat );
    if(ifail != ITK_ok) {/* add your error logic here */}
    
    ifail = RELSTAT_add_release_status ( release_stat, 1, &devDoc, true );
    if(ifail != ITK_ok) {/* add your error logic here */}
 
 
    /************************************************************************** 
        Begin ITK equivalent of User Interface actions 
            Select Parent AsBulitBOMLIne-> RMB-> Install Physical Part
    **************************************************************************/     
    InstallPartParams inParams;  
    inParams.installationDate = set_date;
    inParams.usageName = "DevDoc11";
    tag_t asbuiltStructure = NULLTAG;
    tag_t installedLineTag = NULLTAG;    
    ifail = ASBUILT_install_asbuilt_part_revision_2(parentBOMLine, 
        parentPhysPartRev, childPhysPartRev, selectedBOMLine,
        &inParams, &asbuiltStructure, &installedLineTag);
    if(ifail != ITK_ok) {/* add your error logic here */}

    ifail = BOM_save_window(parentWindow);
    if(ifail != ITK_ok) {/* add your error logic here */}
      
    POM_forget_markpoint(markpoint);       
    ifail = BOM_close_window(childWindow);
    if(ifail != ITK_ok) {/* add your error logic here */}
    
    ifail = BOM_close_window(parentWindow);
    if(ifail != ITK_ok) {/* add your error logic here */}
    
    if(itemId) MEM_free(itemId);
}

