#include <tccore/aom.h>
#include <tccore/aom_prop.h>
#include <ps/ps.h>
#include <tccoreext/gde.h>
#include <tccore/tctype.h>


/* GDE_create_occurrence will be obsolete. This sample shows how to use generic create of BusinessObject to replace it. */
/* This sample code is for reference only, please add your own logic for error checking where appropriate               */
int GDE_create_occurrence_custom(
    tag_t       gde_bvr_tag,           /* <I> */
    tag_t       child_gde_tag,         /* <I> */
    tag_t       child_view_type,       /* <I> */
    tag_t       occurrence_type,       /* <I> */
    tag_t*      gde_occ_tag            /* <O> */
    )
{  
    /* please modify to return your own user error                       */
    int ifail = -1;
    tag_t gdeocctypetag = NULLTAG;
    tag_t myviewtype = NULLTAG;
    tag_t gdeocctag = NULLTAG;
    tag_t boinputtag = NULLTAG;

    /* Find the tag for GDEOccurrence type to create  */
    ifail = TCTYPE_ask_type ( "GDEOccurrence", &gdeocctypetag);

    /* get the view type of input and compare against the child_view_type and see if they are the same */
    ifail = GDE_ask_view_type(gde_bvr_tag, &myviewtype);

    if (myviewtype != child_view_type)
    {
       return -1; /* please return your error code here */
    }
 
    /* Create object input */
    ifail = TCTYPE_construct_create_input ( gdeocctypetag, &boinputtag );

    /* Set inputs as shown in BMIDE CreateInput in Operation Descriptor, parent_gde_bvr and gde_object */
    /* Note, if more properties need to be set, those properties should be added to the CreateInput    */
    ifail = AOM_set_value_tag(boinputtag, "parent_gde_bvr", gde_bvr_tag);
    ifail = AOM_set_value_tag(boinputtag, "gde_object", child_gde_tag);

    /* Create the object */
    ifail = TCTYPE_create_object(boinputtag, &gdeocctag);

    /* Save the object */
    ifail = AOM_save_with_extensions(gdeocctag);

    /* set other property */
    ifail = GDE_set_occurrence_type (gdeocctag, occurrence_type);
    *gde_occ_tag = gdeocctag;
    
     return ifail; 
}
