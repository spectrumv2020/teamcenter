
#include <tccore/aom.h>
#include <tccore/grm.h>

static void replace_design_representation(tag_t partRevTag, tag_t oldDesignRevTag, tag_t newDesignRevTag)
{
    int ifail = ITK_ok;
 
    tag_t priDesRepTypeTag = NULLTAG;
    ifail = GRM_find_relation_type("TC_Primary_Design_Representation", 
        &priDesRepTypeTag);
    if (ifail != ITK_ok) {/*  your error logic here  */}
    
    tag_t isRepByTypeTag = NULLTAG; 
    ifail = ifail = GRM_find_relation_type("TC_Is_Represented_By", 
        &isRepByTypeTag);
    if (ifail != ITK_ok) {/*  your error logic here  */} 
    
    ifail = AOM_refresh(partRevTag, TRUE);
    if (ifail != ITK_ok) {/*  your error logic here  */}
    
    ifail = AOM_refresh(oldDesignRevTag, TRUE);
    if (ifail != ITK_ok) {/*  your error logic here  */}
    
    ifail = ifail = AOM_refresh(newDesignRevTag, TRUE);
    if (ifail != ITK_ok) {/*  your error logic here  */}
 
    tag_t relationTag = NULLTAG;
    ifail = GRM_find_relation(partRevTag, oldDesignRevTag, priDesRepTypeTag, 
        &relationTag);
    if (ifail != ITK_ok) {/*  your error logic here  */}
        
    ifail = GRM_delete_relation(relationTag);
    if (ifail != ITK_ok) {/*  your error logic here  */}
  
    relationTag = NULLTAG;
    ifail = GRM_find_relation(partRevTag, oldDesignRevTag, isRepByTypeTag, 
        &relationTag);
    if (ifail != ITK_ok) {/*  your error logic here  */}
        
    ifail = GRM_delete_relation(relationTag);
    if (ifail != ITK_ok) {/*  your error logic here  */}
            
    relationTag  = NULLTAG;
    ifail = GRM_create_relation(partRevTag, newDesignRevTag, priDesRepTypeTag, 
        NULLTAG, &relationTag);
    ifail = GRM_save_relation(relationTag);
       
    relationTag  = NULLTAG;
    ifail = GRM_create_relation(partRevTag, newDesignRevTag, isRepByTypeTag, 
        NULLTAG, &relationTag);
    ifail = GRM_save_relation(relationTag);
     
    ifail = ifail = AOM_save(partRevTag);
    if (ifail != ITK_ok) {/*  your error logic here  */}
  
    ifail = AOM_save(oldDesignRevTag);
    if (ifail != ITK_ok) {/*  your error logic here  */}
    
     ifail = ifail = AOM_save(newDesignRevTag);
    if (ifail != ITK_ok) {/*  your error logic here  */}
    
    ifail = ifail = AOM_refresh(partRevTag, FALSE);
    if (ifail != ITK_ok) {/*  your error logic here  */}
    
    ifail = AOM_refresh(oldDesignRevTag, FALSE);
    if (ifail != ITK_ok) {/*  your error logic here  */}  
    
    ifail = ifail = AOM_refresh(newDesignRevTag, FALSE);
    if (ifail != ITK_ok) {/*  your error logic here  */}              
}
