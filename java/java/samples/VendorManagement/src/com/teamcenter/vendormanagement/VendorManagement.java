//==================================================
//
//  Copyright 2017 Siemens Product Lifecycle Management Software Inc. All Rights Reserved.
//
//==================================================

package com.teamcenter.vendormanagement;



import com.teamcenter.clientx.AppXSession;

//Include the VendorManagement Service Interface

import com.teamcenter.services.strong.vendormanagement.VendorManagementService;
import com.teamcenter.services.strong.vendormanagement._2007_06.VendorManagement.CreateBidPacksResponse;
import com.teamcenter.services.strong.vendormanagement._2007_06.VendorManagement.VendorPartProperties;
import com.teamcenter.services.strong.vendormanagement._2007_06.VendorManagement.VendorProperties;
import com.teamcenter.services.strong.vendormanagement._2007_06.VendorManagement.BidPackageProps;
import com.teamcenter.services.strong.vendormanagement._2012_02.VendorManagement.LineItemPropertiesWithType;
import com.teamcenter.soa.client.model.StrongObjectFactoryVendormanagement;



/**
 * Perform different operations in the VendorManagementService
 *
 */
public class VendorManagement
{



    /** TEST - 1
         * This test Creates or updates a group of vendor, vendor revisions and vendor roles.
         * Also creates a Vendor Role attaches to the vendor
         * This test also creates a Revsion B to the same Vendor
         *  Service Tested: createOrUpdateVendors
         * Input Parameters are :
         *    itemId         - Id of Vendor to be created
         *    revId          - Id of VendorRevision to be created
         *    name           - Name of Vendor to be created
         *    description    - Description of Vendor
         *    certifiStatus  - Certification status of Vendor
         *    vendorStatus   - Approval status of Vendor
         *    roleType       - Role type for Vendor(Supplier,Distributor,or Manufacturer)
     */



    public void createVendors()
    {
        StrongObjectFactoryVendormanagement.init();

        // Get the service stub
        VendorManagementService vmService = VendorManagementService.getService(AppXSession.getConnection());

        VendorProperties[] vendorProps = new VendorProperties[1];
            VendorProperties vendorProperty = new VendorProperties();

        vendorProperty.clientId = "AppX-Test";
        vendorProperty.itemId   = "Java-Ven-99";
        vendorProperty.revId    = "A";
        vendorProperty.name     = "AppX-Test-Vendor";
        vendorProperty.type     = "Vendor";
        vendorProperty.roleType = "Manufacturer";
        vendorProperty.certifiStatus = "Gold";
        vendorProperty.vendorStatus = "Approved";
        vendorProperty.description = "Test Vendor for the SOA AppX sample application.";



        vendorProps[0] = vendorProperty;

        vmService.createOrUpdateVendors(vendorProps, null, "References");


    }

    /** TEST - 2
         * This Test Deletes Vendors and associated VendorRevisions,VendorRoles
         *  Service Tested: deleteVendors
         *  Input Parameters are :
         *    itemId    -  Vendor id to be deleted
         *    revId     -  VendorRevision id to be deleted
     */

    public void deleteVendors()
    {
        StrongObjectFactoryVendormanagement.init();

        // Get the service stub
        VendorManagementService vmService = VendorManagementService.getService(AppXSession.getConnection());

        VendorProperties[] vendorProps = new VendorProperties[1];
            VendorProperties vendorProperty = new VendorProperties();

        vendorProperty.clientId = "AppX-Test";
        vendorProperty.itemId   = "Java-Ven-99";

        vendorProps[0] = vendorProperty;

        vmService.deleteVendors(vendorProps);
    }


    /** TEST - 3
         *  This Test Deletes VendorRoles associated with a VendorRevision
         *  Service Tested: deleteVendorRoles
         *  Input Parameters are :
         *    itemId    - Vendor id  to which VendorRole is attached
         *    revId     - VendorRevision id  to which VendorRole is attached
         *    roleType  - VendorRole type
     */

    public void deleteVendorRoles()
    {
        StrongObjectFactoryVendormanagement.init();

        // Get the service stub
        VendorManagementService vmService = VendorManagementService.getService(AppXSession.getConnection());

        VendorProperties[] vendorProps = new VendorProperties[1];
            VendorProperties vendorProperty = new VendorProperties();

        vendorProperty.clientId = "AppX-Test";
        vendorProperty.itemId   = "Java-Ven-99";
        vendorProperty.revId    = "A";
        vendorProperty.roleType = "Manufacturer";


        vendorProps[0] = vendorProperty;

        vmService.deleteVendorRoles(vendorProps);


    }


    /** TEST - 4
        * This Test  Creates or updates a group of bidPackage, bidPackage revisions
        * Creates two BidPackage Revsions A and B
        *  Service Tested: createOrUpdateBidPackages
        *  Input Parameters are :
        *    itemId      -  Id of BidPackage to be created
        *    revId       -  Id of BidPackageRevision to be created
        *    name        -  Name  of BidPackage to be created
    */


    public void createBidPackages()
    {
        StrongObjectFactoryVendormanagement.init();

        // Get the service stub
        VendorManagementService vmService = VendorManagementService.getService(AppXSession.getConnection());





        BidPackageProps[] bidProps = new BidPackageProps[1];
        BidPackageProps bidProperty = new BidPackageProps();

        bidProperty.clientId    = "AppX-Test";
        bidProperty.itemId     = "Java-Bp-01";
        bidProperty.revId    = "A";
        bidProperty.name     = "AppX-Test-BidPackage";
        bidProperty.type     = "BidPackage";

        bidProperty.description = "Test BidPackage for the SOA AppX sample application.";



        bidProps[0] = bidProperty;

        vmService.createOrUpdateBidPackages(bidProps, null, "References");


    }

    /** TEST - 5
         * This Test Creates or updates a group of bidpackage lineitems and associates properties
         *  Service Tested: createOrUpdateLineItemsWithType
         *  Input Parameters are :
         *  name            -   name for lineitem to be created
         *  description     -   description for the lineitem object to be created
         *  liccname        -   name for lineitemconfigcontext to be created
         *  liccdesc        -   description for the lineitemconfigcontext object to be created
         *  partid          -   partid to be associated with lineitem to be created
         *  viewtype        -   PSView Type to be associated with lineitemconfigcontext
         *  quantity        -   quantity to be created for lineitems
         *  revRule         -   revision rule to be associated with ineitemconfigcontext
         *  varRule         -   variant rule to be associated with ineitemconfigcontext
         *  closureRule     -   closure rule to be associated with ineitemconfigcontext
         *  quote           -   tag for quote
         *  bpliTypeName    -   Bid Package Line Item Type name to be used
     */


    public void createLineItems()
    {
        StrongObjectFactoryVendormanagement.init();

        // Get the service stub
        VendorManagementService vmService = VendorManagementService.getService(AppXSession.getConnection());



        LineItemPropertiesWithType[] lineItemInputs = new LineItemPropertiesWithType[1];
        LineItemPropertiesWithType lineItemInput = new LineItemPropertiesWithType();

        lineItemInput.name = "LineItemName111228" + "-" + 0;
        lineItemInput.description = "LineItemDesc111228" + "-" + 0;
        lineItemInput.liccname = "LineItemCCName111228" + "-" + 0;
        lineItemInput.liccdesc = "LineItemCCDesc111228" + "-" + 0;
        lineItemInput.partid = "";
        lineItemInput.viewtype = "";
        lineItemInput.quantity = 5;
        lineItemInput.revRule = "";
        lineItemInput.varRule = "";
        lineItemInput.closureRule = "";
        lineItemInput.quote = null;
        lineItemInput.bpliTypeName = "BidPackageLineItem";
        lineItemInputs[0] = lineItemInput;
        
        BidPackageProps[] bidProps = new BidPackageProps[1];
        BidPackageProps bidProperty = new BidPackageProps();

        bidProperty.clientId    = "AppX-Test";
        bidProperty.itemId     = "Java-Bp-01";
        bidProperty.revId    = "A";
        bidProperty.name     = "AppX-Test-BidPackage";
        bidProperty.type     = "BidPackage";

        bidProperty.description = "Test BidPackage for the SOA AppX sample application.";

        bidProps[0] = bidProperty;

        CreateBidPacksResponse createResponse = vmService.createOrUpdateBidPackages( bidProps, null, "References" );

        vmService.createOrUpdateLineItemsWithType( lineItemInputs, createResponse.output[0].bidPackageRev );

    }

    /** TEST - 6
     * This Test Tests the  createOrUpdateVendorParts Service for CommercialPart
     *
     *  Input Parameters are :
     *    partId              - Id for part to be created
     *    name                - Name for the part object to be created
     *    type                - Part Type to be created(Only CommercialPart
     *                          or ManufacturerPart are valid)
     *    revId               - Part Revision Id for create
     *    description         - Description for the part object to be created
     *    vendorid            - Vendor Id to be associated with Part
     *                          vendorid is optional for CommercialPart
     *    isDesignReq         - flag value to decide if design required
     *    uom                 - Unit of measure tag value
     *    makebuy             - makebuy value for Part
     */


    public void createVendorCommercialParts()
    {
        StrongObjectFactoryVendormanagement.init();

        // Get the service stub
        VendorManagementService vmService = VendorManagementService.getService(AppXSession.getConnection());

        VendorPartProperties[] partProps = new VendorPartProperties[1];
        VendorPartProperties vendorpartInput = new VendorPartProperties();
        
        vendorpartInput.partId = "TestCP1_111228" + "-" + 0;
        vendorpartInput.name = "soaJavaTest";
        vendorpartInput.clientId = String.valueOf(0);
        vendorpartInput.type = "CommercialPart";
        vendorpartInput.revId = "A";
        vendorpartInput.description = "xyz";
        vendorpartInput.commercialpartid = "";
        vendorpartInput.commercialpartrevid = "";
        vendorpartInput.isDesignReq = true;
        vendorpartInput.uom = null;
        vendorpartInput.makebuy = 2; // Default value for make/by is 2

        partProps[0] = vendorpartInput;

        vmService.createOrUpdateVendorParts(partProps, null, "References");

    }
}


