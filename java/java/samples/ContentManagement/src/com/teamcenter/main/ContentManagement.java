//==================================================
//
//  Copyright 2015 Siemens Product Lifecycle Management Software Inc. All Rights Reserved.
//
//==================================================

package com.teamcenter.main;

import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.nio.charset.Charset;
import java.util.Date;
import java.util.Enumeration;
import java.util.Vector;
import java.util.zip.ZipEntry;
import java.util.zip.ZipFile;
import org.apache.xerces.parsers.DOMParser;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;
import org.xml.sax.SAXException;
import com.teamcenter.clientx.AppXSession;
import com.teamcenter.schemas.soa._2006_03.exceptions.ServiceException;
import com.teamcenter.services.strong.bom._2008_06.StructureManagement.AddOrUpdateChildrenToParentLineInfo;
import com.teamcenter.services.strong.bom._2008_06.StructureManagement.AddOrUpdateChildrenToParentLineResponse;
import com.teamcenter.services.strong.bom._2008_06.StructureManagement.ItemLineInfo;
import com.teamcenter.services.strong.cad._2007_01.StructureManagement.CreateBOMWindowsInfo;
import com.teamcenter.services.strong.cad._2007_01.StructureManagement.CreateBOMWindowsResponse;
import com.teamcenter.services.strong.contmgmtbase._2011_06.ContentManagement.ComposeInput;
import com.teamcenter.services.strong.contmgmtbase._2011_06.ContentManagement.ComposeResponse;
import com.teamcenter.services.strong.core.DataManagementService;
import com.teamcenter.services.strong.core._2008_06.DataManagement.CreateIn;
import com.teamcenter.services.strong.core._2008_06.DataManagement.CreateResponse;
import com.teamcenter.services.strong.query.SavedQueryService;
import com.teamcenter.services.strong.query._2006_03.SavedQuery.GetSavedQueriesResponse;
import com.teamcenter.services.strong.query._2007_09.SavedQuery.QueryResults;
import com.teamcenter.services.strong.query._2007_09.SavedQuery.SavedQueriesResponse;
import com.teamcenter.services.strong.query._2008_06.SavedQuery.QueryInput;
import com.teamcenter.soa.client.FileManagementUtility;
import com.teamcenter.soa.client.model.ModelObject;
import com.teamcenter.soa.client.model.ServiceData;
import com.teamcenter.soa.client.model.strong.BOMLine;
import com.teamcenter.soa.client.model.strong.BOMWindow;
import com.teamcenter.soa.client.model.strong.ImanQuery;
import com.teamcenter.soa.client.model.strong.Item;
import com.teamcenter.soa.client.model.strong.ItemRevision;

/**
 * Perform different operations in the ContentManagement
 */
public class ContentManagement
{
    
    // Create a unique file name under the input directory. Using time for the file name.
    private String genUniqueFileName( String scInputDir, String extention )
    {
        long nowLong = new Date().getTime();

        String uniqueFileName = Long.toString( nowLong );
        File filename = new File( scInputDir, uniqueFileName + extention ); 
        if ( filename.exists() )
        {
            return genUniqueFileName( scInputDir, extention );
        }
        else
        {
            return filename.getAbsolutePath();
        }
    }
    
    // displays the service data errors
    private String displayErrors(ServiceData serviceData)
    {        
        StringBuffer buf = new StringBuffer();
        
        for (int x = 0; x < serviceData.sizeOfPartialErrors(); ++x)
        {
            String[] messages = serviceData.getPartialError(x).getMessages();
            
            for (int y = 0; y < messages.length; ++y)
            {
                buf.append( messages[y] );
                buf.append( "\n" );                                 
            }
        }
        
        String errorMessage = buf.toString();
        
        System.out.println(errorMessage);
        
        return errorMessage;
    }
    
    // creates a topic
    private Item createTopic(String objectType, String topicType) throws Exception
    {
        DataManagementService dmService = DataManagementService.getService(AppXSession.getConnection());
        
        String [] fields = new String[1];
        String [] values = new String[1];
        fields[0] = new String("Name");        
        values[0] = new String("English US");        
        ModelObject[] languages = queryForItems("__ctm0_Language_Query", fields, values );
        
        fields[0] = new String("Name");
        values[0] = topicType;        
        ModelObject[] topicTypes = queryForItems("__ctm0_Topic_Type_Query", fields, values );
        
        com.teamcenter.services.strong.core._2008_06.DataManagement.CreateInput [] topicRev = new com.teamcenter.services.strong.core._2008_06.DataManagement.CreateInput[1];
        topicRev[0] = new com.teamcenter.services.strong.core._2008_06.DataManagement.CreateInput();
        topicRev[0].boName = objectType + "Revision";        
        topicRev[0].tagProps.put("ctm0MasterLanguageTagref", languages[0]);
        topicRev[0].tagProps.put("ctm0TopicTypeTagref", topicTypes[0]);
                
        com.teamcenter.services.strong.core._2008_06.DataManagement.CreateInput topic = new com.teamcenter.services.strong.core._2008_06.DataManagement.CreateInput();
        topic.boName = objectType;        
        topic.stringProps.put("object_name", "create_topic");        
        topic.compoundCreateInput.put("revision", topicRev);
        
        CreateIn[] inputs = new CreateIn[1];
        inputs[0] = new CreateIn();
        inputs[0].clientId = "createTopic";
        inputs[0].data = topic;
                        
        CreateResponse response = dmService.createObjects(inputs);
        
        if (response.serviceData.sizeOfPartialErrors() > 0)
        {
            displayErrors(response.serviceData);
            
            throw new Exception( "createTopic returned a partial error.");
        }
        
        if (response.serviceData.sizeOfCreatedObjects() > 0)
        {
            for (int x = 0; x < response.serviceData.sizeOfCreatedObjects(); ++x)
            {
                if ( response.serviceData.getCreatedObject(x) instanceof Item )
                {
                    return (Item) response.serviceData.getCreatedObject(x);
                }
            }
        }

        return null;
    }
    
    // executes a stored query
    private ModelObject[] queryForItems(String queryName, String [] fields, String [] values ) throws Exception
    {
        ImanQuery query = null;

        // Get the service stub.
        SavedQueryService queryService = SavedQueryService.getService(AppXSession.getConnection());
        DataManagementService dmService= DataManagementService.getService(AppXSession.getConnection());
        
        try
        {
            // *****************************
            // Execute the service operation
            // *****************************
            GetSavedQueriesResponse savedQueries = queryService.getSavedQueries();

            if (savedQueries.queries.length == 0)
            {
                throw new Exception( "queryForItems: There are no saved queries in the system.");
            }

            // Find one called 'Item Name'
            for (int i = 0; i < savedQueries.queries.length; i++)
            {
                if (savedQueries.queries[i].name.equals(queryName))
                {
                    query = savedQueries.queries[i].query;
                    break;
                }
            }
        }
        catch (ServiceException e)
        {
            e.printStackTrace();
            
            throw new Exception( "queryForItems: GetSavedQueries service request failed.");
        }

        if (query == null)
        {           
            throw new Exception( "queryForItems: There is not a '" + queryName + "' query.");
        }

        try
        {
            //Search for all Items, returning a maximum of 25 objects
            QueryInput savedQueryInput[] = new QueryInput[1];
            savedQueryInput[0] = new QueryInput();
            savedQueryInput[0].query = query;
            savedQueryInput[0].maxNumToReturn = 25;
            savedQueryInput[0].limitList = new ModelObject[0];
            savedQueryInput[0].entries = fields;
            savedQueryInput[0].values = values;
           
            //*****************************
            //Execute the service operation
            //*****************************
            SavedQueriesResponse savedQueryResult = queryService.executeSavedQueries(savedQueryInput);
            QueryResults found = savedQueryResult.arrayOfResults[0]; 
                                                
            String [] uids = new String[ found.objectUIDS.length ];
            
            for(int i=0; i< found.objectUIDS.length; ++i)
            {                            
                uids[i] = found.objectUIDS[i];
            }
            
            ServiceData sd = dmService.loadObjects( uids );
                
            ModelObject[] foundObjs = new ModelObject[ sd.sizeOfPlainObjects() ];            
            for( int i = 0; i < sd.sizeOfPlainObjects(); ++i)
            {
                foundObjs[i] = sd.getPlainObject(i);
            }

            AppXSession.printObjects( foundObjs );

            return foundObjs;
        }
        catch (Exception e)
        {
            e.printStackTrace();
            
            throw new Exception( "queryForItems: ExecuteSavedQuery service request failed.");
        }
    }    
    
    // attaches a child to a parent
    private void attachChild(Item parent, Item child ) throws Exception
    {
        DataManagementService dmService = null;
        com.teamcenter.services.strong.cad.StructureManagementService smService = null;                
        com.teamcenter.services.strong.bom.StructureManagementService  smBomService = null;
        
        dmService = DataManagementService.getService(AppXSession.getConnection());        
        smService = com.teamcenter.services.strong.cad.StructureManagementService.getService( AppXSession.getConnection() );
        smBomService = com.teamcenter.services.strong.bom.StructureManagementService.getService( AppXSession.getConnection() );
        
        String[] props = { "item_revision", "item_revision_id", "revision_list"};
        ServiceData serviceData = dmService.getProperties( new ModelObject[]{parent, child }, props );
        
        if (serviceData.sizeOfPartialErrors() > 0)
        {
            displayErrors(serviceData);            
            
            throw new Exception( "attachChild getProperties returned an error.");
        }
        
        ItemRevision parentRev = null;
        ModelObject childRev = null;
        
        ModelObject[] revList = parent.get_revision_list();        
        if (revList.length > 0)
        {
            parentRev = (ItemRevision) revList[0];
        }
        
        revList = child.get_revision_list();
        if (revList.length > 0)
        {
            childRev = revList[0];
        }        
        
        // create BOMWindow
        CreateBOMWindowsInfo[] createBOMWindowsInfo = new CreateBOMWindowsInfo[1];
        createBOMWindowsInfo[0] = new CreateBOMWindowsInfo();        
        createBOMWindowsInfo[0].itemRev = parentRev;
        
        CreateBOMWindowsResponse createBOMWindowsResponse =
                smService.createBOMWindows(createBOMWindowsInfo);
                        
        // get the topline
        BOMLine topLine = createBOMWindowsResponse.output[0].bomLine;
                
        // add child to parent                
        ItemLineInfo[] itemLineInfos = new ItemLineInfo[1];
        itemLineInfos[0] = new ItemLineInfo();        
        itemLineInfos[0].itemRev = childRev;
        itemLineInfos[0].occType = "DC_ComposableReferenceR";
        
        AddOrUpdateChildrenToParentLineInfo[] addChildInfo = new AddOrUpdateChildrenToParentLineInfo[1];
        addChildInfo[0] = new AddOrUpdateChildrenToParentLineInfo();            
        addChildInfo[0].items = itemLineInfos;
        addChildInfo[0].parentLine = topLine;
        addChildInfo[0].viewType = "view";
        
                
        AddOrUpdateChildrenToParentLineResponse saveBomWinResp = smBomService.addOrUpdateChildrenToParentLine(addChildInfo);
        
        if (saveBomWinResp.serviceData.sizeOfPartialErrors() > 0)
        {            
            displayErrors(serviceData);            
            
            throw new Exception( "attachChild addOrUpdateChildrenToParentLine returned an error.");
        }
        
        // save bom window
        BOMWindow[] bomWindow = new BOMWindow[1];
        bomWindow[0] = createBOMWindowsResponse.output[0].bomWindow;
        
        smService.saveBOMWindows(bomWindow);        
    }
    
    // composes a topic
    private Vector <File> composeForEdit(Item topic) throws Exception
    {
        Vector <File> vFiles = new Vector<File>();
        
        com.teamcenter.services.strong.contmgmtbase.ContentManagementService cmService = null;        
        cmService = com.teamcenter.services.strong.contmgmtbase.ContentManagementService.getService( AppXSession.getConnection() );

        ComposeInput[] composeInputs = new ComposeInput[1];
        composeInputs[0] = new ComposeInput();
        composeInputs[0].composableBO = topic;
        composeInputs[0].processingDataVars.put("{root-dir}",         "c:\\graphics\\");
        composeInputs[0].keyValueArgs.put("actionType",             "edit");
        composeInputs[0].keyValueArgs.put("languageName",             "English US");        
        composeInputs[0].keyValueArgs.put("styleSheetType",         "EDITOR_VIEW");        
        composeInputs[0].keyValueArgs.put("includeGraphics",         "false");
        composeInputs[0].keyValueArgs.put("includeSchemas",         "false");
        composeInputs[0].keyValueArgs.put("includeStyleSheets",     "false");
        composeInputs[0].keyValueArgs.put("includeContentRefs",     "false");
        composeInputs[0].keyValueArgs.put("includeComposeRefs",     "false");
        
        //composeInputs[0].keyValueArgs.put("styleType", "");
        //composeInputs[0].keyValueArgs.put("toolName", "");
        //composeInputs[0].keyValueArgs.put("grphPrioName", "");
        //composeInputs[0].keyValueArgs.put("graphicMode", "");        
        //composeInputs[0].keyValueArgs.put("copyType", "");
        //composeInputs[0].keyValueArgs.put("linkType", "");
        //composeInputs[0].keyValueArgs.put("translationVersion", "");
                
        ComposeResponse composeResponse = cmService.composeContent( composeInputs );
        
        if (composeResponse.serviceData.sizeOfPartialErrors() > 0)
        {
            displayErrors(composeResponse.serviceData);
            
            throw new Exception( "composeForEdit composeContent returned a partial error.");
        }
        
        // Get the composed file
        
        FileManagementUtility fmUtility = new FileManagementUtility(AppXSession.getConnection());
        
        File zipfile = null;
        
        String readTicket = composeResponse.composedData[0].composedTransientFileReadTicket;
                
        try 
        {            
            if (readTicket.endsWith("zip"))
            {
                String filename = genUniqueFileName(System.getProperty("java.io.tmpdir"), ".zip");
                zipfile = fmUtility.getTransientFile(readTicket, filename);
            }
            else
            {
                String filename = genUniqueFileName(System.getProperty("java.io.tmpdir"), ".xml");
                File xmlfile = fmUtility.getTransientFile(readTicket, filename);
                vFiles.add(xmlfile);
            }
            
            // verify meta data file was generated
            readTicket = composeResponse.composedData[0].composedMetaDataReadTicket;
            
            String filename = genUniqueFileName(System.getProperty("java.io.tmpdir"), ".meta");
            File meta = fmUtility.getTransientFile(readTicket, filename);
            vFiles.add(meta);
        }
        catch (Exception e)
        {
            e.printStackTrace();    
            
            throw new Exception( "composeForEdit file download failed.");
        }    
                                    
        fmUtility.term();
        
        if ( zipfile != null && zipfile.getName().endsWith(".zip") )
        {            
            // open zip file
            ZipFile zf = new ZipFile( zipfile.getAbsoluteFile(), Charset.forName("Cp437") );
            
            Enumeration<? extends ZipEntry> entries = zf.entries();

            // loop thru zip entries
            while ( entries.hasMoreElements() )
            {
                ZipEntry entry = entries.nextElement();
                String name = entry.getName();
                if ( !entry.isDirectory() )
                {
                    // write out file
                    File file = new File( System.getProperty("java.io.tmpdir"), name );
                    
                    InputStream in = new BufferedInputStream( zf.getInputStream( entry ) );
                    OutputStream out = new BufferedOutputStream( new FileOutputStream( file ) );

                    try
                    {
                        int c = 0;
                        while ( ( c = in.read() ) != -1 )
                        {
                            out.write( (byte) c );
                        }
                    }
                    finally
                    {
                        out.close();
                        in.close();
                    }
                    
                    vFiles.add(file);
                }
                else
                {
                    File dir = new File( System.getProperty("java.io.tmpdir") + name);
                    dir.mkdir();
                }
            }
            zf.close();
        }
        
        return vFiles;
    }
    
    // opens an xml document in parser
    private static Document getDoc(File file) throws SAXException, IOException
    {
        DOMParser parser = new DOMParser();
        
        parser.setFeature( "http://xml.org/sax/features/validation", false );
        parser.setFeature( "http://apache.org/xml/features/nonvalidating/load-external-dtd", false );
        parser.setFeature( "http://xml.org/sax/features/external-parameter-entities", false );
        parser.setFeature( "http://apache.org/xml/features/dom/include-ignorable-whitespace", false );
        
        parser.parse(file.getAbsolutePath());
        
        return parser.getDocument();        
    }
    
    // This test creates a map with a child topic and calls compose on it.
    // If the example_precomposefilter_callback_fn callback is installed the
    // first child of a map should be skipped in the compose.
    //  
    // Install:
    //   install_callback -u=infodba -p=infodba -g=dba -mode=create -type=ContmgmtPreComposeFilter -library=libCtm0Callbacks -function=example_precomposefilter_callback_fn -name=example_precomposefilter_callback_fn
    // Remove: 
    //   install_callback -u=infodba -p=infodba -g=dba -mode=delete -type=ContmgmtPreComposeFilter -library=libCtm0Callbacks -function=example_precomposefilter_callback_fn -name=example_precomposefilter_callback_fn
    public void preComposeCallbackTest(Boolean callbackInstalled)
    {        
        try 
        {
            // create topics
            Item parentTopic = createTopic("DC_DitaDynMap", "DITA 1.3 Map");
            Item childTopic = createTopic("DC_DitaTopic", "DITA 1.3 Generic Topic");
            
            // Add child to parent
            attachChild( parentTopic, childTopic );
            
            // compose
            Vector <File> vFiles = composeForEdit(parentTopic);
            
            File ditamap = null;
            File meta = null;
            for (int x = 0; x < vFiles.size(); ++x)
            {
                if (vFiles.elementAt(x).getName().endsWith("ditamap"))
                {
                    ditamap = vFiles.elementAt(x);
                }
                else if (vFiles.elementAt(x).getName().endsWith("meta"))
                {
                    meta = vFiles.elementAt(x);
                }
            }
            
            // verify data    
            if (ditamap != null)
            {                    
                Document document = getDoc(ditamap);
                
                Element root = document.getDocumentElement();
                NodeList children = root.getChildNodes();
                
                String childName = null;
                for ( int x = 0; x < children.getLength(); ++x )
                {
                    Node node = children.item( x );
                    
                    if( node.getNodeType() == Node.ELEMENT_NODE )
                    {
                        String elemName = node.getNodeName();
                        
                        if (elemName != null && elemName.compareTo( "topicref" ) == 0)
                        {
                            Node attribute = node.getAttributes().getNamedItem("href");
                            childName = attribute.getNodeValue();
                            
                            break;
                        }
                    }
                }
                
                if ( childName!=null && callbackInstalled)
                {
                    // callback should be installed
                    
                    throw new Exception( "preComposeCallbackTest: ERROR: Child topic " + childName + " found, callback should have remove it.\n");
                }
                else if ( childName==null && !callbackInstalled)
                {
                    // callback should not be installed
                    
                    throw new Exception( "preComposeCallbackTest: ERROR: Child topic not found.\n");
                }
            }
            else                
            {
                throw new Exception( "preComposeCallbackTest: ERROR: Map not found.\n");
            }
            
            // verify data    
            if (meta != null)
            {                    
                Document document = getDoc(meta);
                
                Element root = document.getDocumentElement();
                NodeList parentList = root.getChildNodes();
                
                if (parentList.getLength() == 0)
                {
                    throw new Exception( "preComposeCallbackTest: ERROR: Metadata parent not found.\n");
                }
                
                Node parent = parentList.item( 0 );
                
                NodeList children = parent.getChildNodes();
                
                String childName = null;
                for ( int x = 0; x < children.getLength(); ++x )
                {
                    Node node = children.item( x );
                    
                    if( node.getNodeType() == Node.ELEMENT_NODE )
                    {
                        String elemName = node.getNodeName();
                        
                        if (elemName != null && elemName.compareTo( "child" ) == 0)
                        {
                            Node attribute = node.getAttributes().getNamedItem("id");
                            childName = attribute.getNodeValue();
                            
                            break;
                        }
                    }
                }
                
                if ( childName!=null && callbackInstalled)
                {
                    // callback should be installed
                    
                    throw new Exception( "preComposeCallbackTest: ERROR: Child topic " + childName + " found, callback should have remove it.\n");
                }
                else if ( childName==null && !callbackInstalled)
                {
                    // callback should not be installed
                    
                    throw new Exception( "preComposeCallbackTest: ERROR: Child topic not found.\n");
                }
            }
            else                
            {
                throw new Exception( "preComposeCallbackTest: ERROR: meta file not found.\n");
            }            
        } 
        catch (Exception e) 
        {        
            e.printStackTrace();
        }        
    }
    
    // This test creates a topic and calls compose on it.
    // If the example_postcompose_callback_fn callback is installed the
    // returned xml should include an extra processing instruction.
    //  
    // Install:
    //   install_callback -u=infodba -p=infodba -g=dba -mode=create -type=ContmgmtPostCompose -library=libCtm0Callbacks -function=example_postcompose_callback_fn -name=example_postcompose_callback_fn
    // Remove: 
    //   install_callback -u=infodba -p=infodba -g=dba -mode=delete -type=ContmgmtPostCompose -library=libCtm0Callbacks -function=example_postcompose_callback_fn -name=example_postcompose_callback_fn
    public void postComposeCallbackTest(Boolean callbackInstalled)
    {        
        try 
        {
            // create topics
            Item topic = createTopic("DC_DitaTopic", "DITA 1.3 Generic Topic");            
            
            // compose
            Vector <File> vFiles = composeForEdit(topic);
            
            File dita = null;
            
            for (int x = 0; x < vFiles.size(); ++x)
            {
                if (vFiles.elementAt(x).getName().endsWith("dita"))
                {
                    dita = vFiles.elementAt(x);
                }
            }
            
            // verify data    
            if (dita != null)
            {                    
                Document document = getDoc(dita);
                
                Element root = document.getDocumentElement();
                NodeList children = root.getChildNodes();
                
                Boolean found = false;
                for ( int x = 0; x < children.getLength(); ++x )
                {
                    Node node = children.item( x );
                    
                    if( node.getNodeType() == Node.PROCESSING_INSTRUCTION_NODE )
                    {
                        String elemName = node.getNodeName();
                        
                        if (elemName != null && elemName.compareTo( "callback" ) == 0)
                        {
                            found = true;
                            
                            break;
                        }
                    }
                }
                
                if ( !found && callbackInstalled)
                {
                    // callback should be installed
                    
                    throw new Exception( "postComposeCallbackTest: ERROR: processing instruction not found, callback should have added it.\n");
                }
                else if ( found && !callbackInstalled)
                {
                    // callback should not be installed
                    
                    throw new Exception( "postComposeCallbackTest: ERROR: processing instruction found, callback must be installed!\n");
                }
            }
            else                
            {
                throw new Exception( "postComposeCallbackTest: ERROR: dita file not found.\n");
            }
        } 
        catch (Exception e) 
        {        
            e.printStackTrace();
        }        
    }
}
