package view;

import java.awt.Desktop;
import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;
import java.util.HashMap;
import java.util.Iterator;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Properties;

import org.apache.poi.hssf.usermodel.HSSFSheet;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.FormulaEvaluator;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.xssf.usermodel.XSSFSheet;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.eclipse.nebula.widgets.grid.Grid;
import org.eclipse.nebula.widgets.grid.GridColumn;
import org.eclipse.nebula.widgets.grid.GridEditor;
import org.eclipse.nebula.widgets.grid.GridItem;
import org.eclipse.swt.SWT;
import org.eclipse.swt.events.DisposeEvent;
import org.eclipse.swt.events.DisposeListener;
import org.eclipse.swt.events.SelectionAdapter;
import org.eclipse.swt.events.SelectionEvent;
import org.eclipse.swt.events.SelectionListener;
import org.eclipse.swt.graphics.Cursor;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Button;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Display;
import org.eclipse.swt.widgets.FileDialog;
import org.eclipse.swt.widgets.MessageBox;
import org.eclipse.swt.widgets.ProgressBar;
import org.eclipse.swt.widgets.Shell;
import org.eclipse.swt.widgets.Tree;
import org.eclipse.swt.widgets.TreeItem;

import com.teamcenter.clientx.AppXSession;
import com.teamcenter.hello.DataManagement;
import com.teamcenter.hello.EnvelopeSend;
import com.teamcenter.hello.HomeFolder;
import com.teamcenter.hello.Query;
import com.teamcenter.schemas.soa._2006_03.exceptions.ServiceException;
import com.teamcenter.services.strong.bom._2008_06.StructureManagement.AddOrUpdateChildrenToParentLineInfo;
import com.teamcenter.services.strong.bom._2008_06.StructureManagement.AddOrUpdateChildrenToParentLineResponse;
import com.teamcenter.services.strong.bom._2008_06.StructureManagement.BOMLinesOutput;
import com.teamcenter.services.strong.bom._2008_06.StructureManagement.ItemLineInfo;
import com.teamcenter.services.strong.bom._2008_06.StructureManagement.RemoveChildrenFromParentLineResponse;
import com.teamcenter.services.strong.cad.StructureManagementService;
import com.teamcenter.services.strong.cad._2007_01.StructureManagement.CreateBOMWindowsInfo;
import com.teamcenter.services.strong.cad._2007_01.StructureManagement.CreateBOMWindowsResponse;
import com.teamcenter.services.strong.core.DataManagementService;
import com.teamcenter.services.strong.core._2006_03.DataManagement.CreateItemsResponse;
import com.teamcenter.services.strong.core._2006_03.DataManagement.ItemProperties;
import com.teamcenter.soa.client.model.ModelObject;
import com.teamcenter.soa.client.model.ServiceData;
import com.teamcenter.soa.client.model.strong.BOMLine;
import com.teamcenter.soa.client.model.strong.BOMWindow;
import com.teamcenter.soa.client.model.strong.Item;
import com.teamcenter.soa.client.model.strong.ItemRevision;
import com.teamcenter.soa.client.model.strong.ReleaseStatus;
import com.teamcenter.soa.client.model.strong.User;
import com.teamcenter.soa.exceptions.NotLoadedException;

public class BomUtilityView {

	protected Shell shlBomUtility;
	protected Grid gridImportTable;
	protected Grid gridLogFiles;
	private Button btnDryRun;
	private Button btnExecute;
	private Button btnClose;
	private Button btnImport;
	private Properties prop;
	private DataManagementService dmService;
	public static String serverHost;
	public static String sTcUserName;
	public static String sTcPassword;
	public static int iFindNoStart = 0;
	public static int iFindNoIncrement = 0;
	private Button btnClear;
	public static String[] sArrHeaders;
	public static String sPropFIlePath = "";
	public String s4TierURL = "";
	//Decide on Variant /Non-Variant item type. Default is non-variant.
	public String strToplineItemType = "";
	public boolean bCREATE_MISSING_PART = false; 

	String BOM_LINE_PROP = "\"bl_child_lines\", \"bl_item_object_name\",\"bl_sequence_no\",\"bl_line_name\"";

	int iBomLineNo = 0;

	HashMap<Integer, String> hmRowVsItemIDFindNo  = new HashMap<>();
	HashMap<Integer, String> hmTblRowVsItemIDFindNo  = new HashMap<>();
	HashMap<String, String> hmStoreItemisVsRefDes  = new HashMap<>();
	ArrayList<BOMLine> alBomLineNotInTbl = new ArrayList<>();
	ArrayList<String> alBomLineIDNotInTbl = new ArrayList<>();
	static BOMWindow bomWindowObj = null;
	static com.teamcenter.services.strong.cad.StructureManagementService cadSMService = null;

	int iColPartNumber = 0;
	int iCol_Level = 0;
	int iCol_TC_ObjStatus = 0;

	static SimpleDateFormat smpd = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");

	AppXSession   session = null;//new AppXSession(serverHost);
	HomeFolder   home = null;// new HomeFolder();
	Query       query = null;// new Query();
	DataManagement dm = null;// new DataManagement();
	// Establish a session with the Teamcenter Server
	User user = null;// session.loginUsingArguments("infodba","infodba");

	boolean bGenerateFindNo = true;

	public static String sInputLogFilePath = "";
	public static String sDryRunLogFilePath = "";
	public static String sExecuteLogFilePath = "";
	public static String sSummaryLogFilePath = "";

	int iCntPartAvail = 0;
	int iCntPartNotAvail = 0;
	int iCntPartIDEmpty = 0;

	ArrayList<String> alPartsExist = new ArrayList<>();
	ArrayList<String> alPartsNotExist = new ArrayList<>();
	ArrayList<String> alPartsNewlyCreated = new ArrayList<>();

	Display display;
	private ProgressBar progressBar;

	Cursor cursorHand = new Cursor(display, SWT.CURSOR_HAND);
	Cursor cursorArrow = new Cursor(display, SWT.CURSOR_ARROW);
	Cursor cursorWait = new Cursor(display, SWT.CURSOR_WAIT);

	/**
	 * Launch the application.
	 * @param args
	 */
	public static void main(String[] args) {
		System.out.println(System.getProperty("java.io.tmpdir"));
		try {
			//Cheange file Name as Excel Validation logs
			sInputLogFilePath =  System.getProperty("java.io.tmpdir") + ""+"\\Input_File_Validation_"+smpd.format(new Date()).toString().replaceAll(":", "_")+".log";
		} catch (Exception e1) {
			e1.printStackTrace();
		}
		if (args.length > 0)
		{
			sPropFIlePath = args[0];
			System.out.println("1 th Argument is "+args[0]);
			if (args[0].equals("-help") || args[0].equals("-h"))
			{
				System.out.println("usage: java [-Dhost=http://server:port/tc] com.teamcenter.hello.Hello");
				System.exit(0);
			}
		}else {
			writeLogFile(sInputLogFilePath, "Please run the Program along with Input Configuration file path");
			return;
		}
		try {
			BomUtilityView window = new BomUtilityView();
			window.open();
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	/**
	 * Open the window.
	 */
	public void open() {
		display = Display.getDefault();
		try {
			createContents();
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		shlBomUtility.open();
		shlBomUtility.layout();
		while (!shlBomUtility.isDisposed()) {
			if (!display.readAndDispatch()) {
				display.sleep();
			}
		}
	}

	/**
	 * Create contents of the window.
	 */
	protected void createContents() throws Exception{
		String sHeaders = "";
		try {
			File propFile = new File(sPropFIlePath);

			if(!propFile.exists()) {
				writeLogFile(sInputLogFilePath, "Input Configuration file is not Available");
				//				return;
			}

			prop = new Properties();
			FileInputStream fin = new FileInputStream(propFile);
			prop.load(fin);
			sHeaders = prop.getProperty("MIN_EXCEL_HEADERS");
			System.out.println(sHeaders);

			if(sHeaders.contains(",")) {
				sArrHeaders = sHeaders.split(",");
			}

			serverHost = prop.getProperty("TC_WEB_URL");
			sTcUserName = prop.getProperty("TC_USER_ID");
			sTcPassword = prop.getProperty("TC_PASSWORD");
			if(prop.getProperty("FIND_NO_STARTS_WITH")!=null && prop.getProperty("FIND_NO_STARTS_WITH").length()>0) {
				iFindNoStart = Integer.parseInt(prop.getProperty("FIND_NO_STARTS_WITH"));
			}
			if(prop.getProperty("FIND_NO_INCREMENT")!=null && prop.getProperty("FIND_NO_INCREMENT").length()>0) {
				iFindNoIncrement = Integer.parseInt(prop.getProperty("FIND_NO_INCREMENT"));
			}

		} catch (Exception e) {
			e.printStackTrace();
		}

		HashMap<String, Boolean> hmKeyMappingVsAvailability = new HashMap<>();
		hmKeyMappingVsAvailability.put("MIN_EXCEL_HEADERS", false);
		hmKeyMappingVsAvailability.put("TC_WEB_URL", false);
		hmKeyMappingVsAvailability.put("TC_USER_ID", false);
		hmKeyMappingVsAvailability.put("TC_PASSWORD", false);
		/*hmKeyMappingVsAvailability.put("NON_VARIANT_TOP_LINE_ITEM_TYPE", false);
		hmKeyMappingVsAvailability.put("VARIANT_TOP_LINE_ITEM_TYPE", false);
		hmKeyMappingVsAvailability.put("FIRST_LINE_ITEM_TYPE", false);
		hmKeyMappingVsAvailability.put("CHILD_LINE_ITEM_TYPE", false);
		hmKeyMappingVsAvailability.put("CREATE_MISSING_PART", false);*/
		//hmKeyMappingVsAvailability.put("MIN_EXCEL_HEADERS", false);
		boolean bMappingNotAVail = false;
		for (String sKey : hmKeyMappingVsAvailability.keySet()) {
			if(prop.getProperty(sKey)==null || prop.getProperty(sKey).trim().isEmpty()) {
				writeLogFile(sInputLogFilePath, "Mapping for "+sKey+" is not available in config File or Value is Empty.");
				bMappingNotAVail = true;
			}
		}

		/*String sCreateMissingPart = prop.getProperty("CREATE_MISSING_PART");
		if(sCreateMissingPart.trim().equalsIgnoreCase("TRUE"))
			bCREATE_MISSING_PART = true;*/

		strToplineItemType = prop.getProperty("NON_VARIANT_TOP_LINE_ITEM_TYPE");
		if(bMappingNotAVail) {
			//shlBomUtility.setCursor(cursorArrow);
			return;
		}
		if(sArrHeaders.length<4) {
			writeLogFile(sInputLogFilePath, "Minimum 4 columns should be mapped in MIN_EXCEL_HEADERS in config file.");
			shlBomUtility.setCursor(cursorArrow);
			return;
		}

		shlBomUtility = new Shell();
		//Modify below line for ICON - on top left corner
		//shlBomUtility.setImage(null);
		shlBomUtility.addDisposeListener(new DisposeListener() {
			public void widgetDisposed(DisposeEvent arg0) {
				try {
					if(session!=null) {
						session.logout();
					}
					System.out.println("Logged out Successfully...");
				} catch (Exception e) {
					System.out.println("Logout Operation Failed!...");
				}
			}
		});
		shlBomUtility.setSize(903, 568);
		shlBomUtility.setText("Legacy BOM Import Utility");
		shlBomUtility.setLayout(new GridLayout(1, false));
		//TODO:ICON should be TC ICON.

		Composite compTable = new Composite(shlBomUtility, SWT.NONE);
		compTable.setLayout(new GridLayout(1, false));
		compTable.setLayoutData(new GridData(SWT.FILL, SWT.FILL, true, true, 1, 1));

		gridImportTable = new Grid(compTable, SWT.BORDER | SWT.H_SCROLL | SWT.V_SCROLL | SWT.MULTI);
		GridLayout gl_grid = new GridLayout(1, false);
		gl_grid.horizontalSpacing = 0;
		gl_grid.verticalSpacing = 0;
		gl_grid.marginHeight = 0;
		gl_grid.marginBottom = 0;
		gridImportTable.setLayoutData(new GridData(SWT.FILL, SWT.FILL, true, true, 1, 1));
		gridImportTable.setLayout(gl_grid);
		gridImportTable.setHeaderVisible(true);

		if(sArrHeaders.length>=4) {
			BomUtility_Grid_Col_Create.BomUtility_Import_Tbl_GridColumn(gridImportTable, sArrHeaders);
		}

		gridLogFiles = new Grid(compTable, SWT.BORDER | SWT.H_SCROLL | SWT.V_SCROLL | SWT.MULTI);
		GridLayout gl_gridLogFiles = new GridLayout(1, false);
		gl_gridLogFiles.horizontalSpacing = 0;
		gl_gridLogFiles.verticalSpacing = 0;
		gl_gridLogFiles.marginHeight = 0;
		gl_gridLogFiles.marginBottom = 0;
		GridData gd_gridLogFiles = new GridData(SWT.FILL, SWT.BOTTOM, true, false, 1, 1);
		gd_gridLogFiles.minimumHeight = 10;
		gd_gridLogFiles.heightHint = 120;
		gridLogFiles.setLayoutData(gd_gridLogFiles);
		gridLogFiles.setLayout(gl_gridLogFiles);
		gridLogFiles.setHeaderVisible(true);

		BomUtility_Grid_Col_Create.BomUtility_LogFiles_GdCol(gridLogFiles);

		Composite compButoons = new Composite(shlBomUtility, SWT.NONE);
		compButoons.setLayout(new GridLayout(2, false));
		compButoons.setLayoutData(new GridData(SWT.FILL, SWT.BOTTOM, true, false, 1, 1));

		Composite compLeftBtns = new Composite(compButoons, SWT.NONE);
		compLeftBtns.setLayout(new GridLayout(2, false));

		btnImport = new Button(compLeftBtns, SWT.NONE);
		btnImport.addSelectionListener(new SelectionAdapter() {
			@Override
			public void widgetSelected(SelectionEvent e) {
				shlBomUtility.setCursor(cursorWait);
				printTCURLInLog(sInputLogFilePath);
				try {
					BufferedWriter writer = new BufferedWriter(new FileWriter(sInputLogFilePath, false));
					writer.close();
				} catch (IOException e1) {
					e1.printStackTrace();
				}
				if(gridImportTable.getItems().length>0) {
					readExcelFile(gridImportTable);
					if(gridImportTable.getItems().length>0) {
						btnImport.setEnabled(false);
						if(gridImportTable.getData("FIND_NO_INVALID")!=null) {
							String sFindNoValidation = (String)gridImportTable.getData("FIND_NO_INVALID");
							if(sFindNoValidation.isEmpty() || sFindNoValidation.trim().length()==0) {
								btnDryRun.setEnabled(true);
							}else {
								gridImportTable.setData("FIND_NO_INVALID", "");
							}
						}
						//btnDryRun.setEnabled(true);
					}
				}else {
					readExcelFile(gridImportTable);
					if(gridImportTable.getItems().length>0) {
						btnImport.setEnabled(false);
						if(gridImportTable.getData("FIND_NO_INVALID")!=null) {
							String sFindNoValidation = (String)gridImportTable.getData("FIND_NO_INVALID");
							if(sFindNoValidation.isEmpty() || sFindNoValidation.trim().length()==0) {
								btnDryRun.setEnabled(true);
							}else {
								gridImportTable.setData("FIND_NO_INVALID", "");
							}
						}
						//btnDryRun.setEnabled(true);
					}
				}
				updateLogFilePath();
				shlBomUtility.setCursor(cursorArrow);
			}
		});
		btnImport.setBounds(0, 0, 90, 30);
		btnImport.setText("Import BOM");

		progressBar = new ProgressBar(compLeftBtns, SWT.SMOOTH);//SWT.INTERMEDIATE
		GridData gd_progressBar = new GridData(SWT.LEFT, SWT.CENTER, false, false, 1, 1);
		gd_progressBar.exclude = true;
		progressBar.setLayoutData(gd_progressBar);
		//progressBar.

		Composite compRightBtns = new Composite(compButoons, SWT.NONE);
		compRightBtns.setLayout(new GridLayout(4, false));
		compRightBtns.setLayoutData(new GridData(SWT.RIGHT, SWT.CENTER, true, false, 1, 1));

		btnDryRun = new Button(compRightBtns, SWT.NONE);
		btnDryRun.addSelectionListener(new SelectionAdapter() {
			@Override
			public void widgetSelected(SelectionEvent e) {
				try {
					iBomLineNo = 0;
					hmRowVsItemIDFindNo = new HashMap<>();
					shlBomUtility.setCursor(cursorWait);
					btnDryRun.setEnabled(false);
					try {
						sDryRunLogFilePath =  System.getProperty("java.io.tmpdir") + "\\"+"LegacyBOM_Dry_Run_Logs_"+smpd.format(new Date()).toString().replaceAll(":", "_")+".log";
					} catch (Exception e1) {
						e1.printStackTrace();
					}

					writeLogFile(sDryRunLogFilePath, "Dry Run Started");
					printTCURLInLog(sDryRunLogFilePath);
					Summary summary = new Summary();
					summary.setsMode("Dry Run");
					summary.setiTotalExcelRows(gridImportTable.getItemCount());

					getColSeq();
					if(user == null) {
						//writeLogFile(sDryRunLogFilePath, "Logging in to "+serverHost+" by using "+sTcUserName);
						session = new AppXSession(serverHost);
						//TODO:Validate Host and Username, Password
						home = new HomeFolder();
						query = new Query();
						dm = new DataManagement();
						dmService = DataManagementService.getService(AppXSession.getConnection());

						// Establish a session with the Teamcenter Server
						user = session.loginUsingArguments(sTcUserName,sTcPassword);
						writeLogFile(sDryRunLogFilePath, "Login is successful");
					}

					boolean bEBOMAvailable = true, bAllPartsAvailable = true;

					//EBOM Should be unquie in all row
					GridItem[] gdItems = gridImportTable.getItems();
					String sEBOM_Id = "";

					boolean bPWBNotAvailable = false;

					if(gdItems.length>0)
					{
						String sLineErrors = "", sBomIDErrors = "", sPWBErrors = "";
						for (int i = 0; i < gdItems.length; i++) {
							String sTempPartNo = gdItems[i].getText(iColPartNumber);

							if(sTempPartNo==null || sTempPartNo.trim().isEmpty()) {
								sLineErrors = sLineErrors + ("ERROR Excel Row Number : "+(i+2)+" - PWB/MOOG Part ID is missing in Excel Column.\n");
							}

							if(sEBOM_Id.isEmpty()) {
								sEBOM_Id = gdItems[i].getText(0);
							}
							else if(!sEBOM_Id.equalsIgnoreCase(gdItems[i].getText(0)))
							{
								sBomIDErrors = sBomIDErrors + ("ERROR Excel Row Number : "+(i+2)+" - BOM Unique ("+gdItems[i].getText(0)+") Error, Please check\n");
							}

							if(i==0) {
								//GET THE PWB ID, It should match with PWB ITEM Type Mapping.
								String sPWB_PartNo = gdItems[i].getText(iColPartNumber);

								summary.setsPWB_ItemID(sPWB_PartNo);

								ModelObject[] eBomObj = query.queryItemsUsingID(sPWB_PartNo, "*");
								if(eBomObj!=null && eBomObj.length>0){
									summary.setsPWB_ItemID_Status("Available in Teamcenter");
									dmService.getProperties(eBomObj, new String[] {"object_type"});

								}else {
									bPWBNotAvailable = true;
									summary.setsPWB_ItemID_Status("Not Available in Teamcenter");
								}
							}
						}

						addExtraCols(gridImportTable);

						String sItemAvailInfo = updatePartAvailability(gridImportTable, sDryRunLogFilePath);

						boolean bPWB_Avail = false;
						for (String sPart : alPartsExist) {
							if(true/*summary.getsPWB_ItemID()!=null && (!summary.getsPWB_ItemID().isEmpty()) && !sPart.equalsIgnoreCase(summary.getsPWB_ItemID())*/)
								summary.setsAvailPartsList(sPart);
							else
								bPWB_Avail = true;
						}

						/*if(bPWB_Avail)
							summary.setiAvailPartsCnt(iCntPartAvail-1);
						else*/
						summary.setiAvailPartsCnt(iCntPartAvail);

						if(bPWBNotAvailable) {
							summary.setiNewPartsCnt((iCntPartNotAvail));
							for (String sPart : alPartsNotExist) {
								if(true/*summary.getsPWB_ItemID()!=null && (!summary.getsPWB_ItemID().isEmpty())*/) {
									if(/*!sPart.equalsIgnoreCase(summary.getsPWB_ItemID().trim())*/true) {
										summary.setsNewPartsList(sPart);
									}
								}
							}
						}else {
							summary.setiNewPartsCnt(iCntPartNotAvail);
							for (String sPart : alPartsNotExist) {
								summary.setsNewPartsList(sPart);
							}
						}

						if(sBomIDErrors.isEmpty()&&sLineErrors.isEmpty()&&sPWBErrors.isEmpty()) {
							summary.setsBomID(sEBOM_Id);

							ModelObject[] eBomObj = query.queryItemsUsingID(sEBOM_Id, "*");
							if(eBomObj!=null && eBomObj.length>0){
								try {
									Item parentItem = (Item)eBomObj[0];
									String[] props = { "item_revision", "item_revision_id", "revision_list"};
									ServiceData serviceData = dmService.getProperties(new ModelObject[]{parentItem}, new String[]{ "item_revision", "item_revision_id", "revision_list"});

									ModelObject[] revList = parentItem.get_revision_list();        
									if (revList.length > 0)
									{
										ItemRevision parentRev = (ItemRevision) revList[revList.length-1];										
										dmService.getProperties(new ModelObject[]{parentRev}, new String[]{ "item_id","release_status_list", "item_revision_id", "owning_user"});
										String sBomID = parentRev.getPropertyDisplayableValue("item_id");
										String sBomrev = parentRev.getPropertyDisplayableValue("item_revision_id");
										String sBomrevOwner = parentRev.getPropertyDisplayableValue("owning_user");

										String owningUserID = sBomrevOwner.substring(sBomrevOwner.indexOf('(')+1, sBomrevOwner.indexOf(')'));

										if(!owningUserID.equalsIgnoreCase(sTcUserName)) {
											writeLogFile(sDryRunLogFilePath, "Bom Owner ["+owningUserID+"] is not same as login user ["+sTcUserName+"]");
											writeLogFile(sDryRunLogFilePath, "Dry Run Results : Failed");
											btnExecute.setEnabled(false);
											getMessageBox(shlBomUtility, SWT.ICON_INFORMATION, "Information", "Bom Owner ["+owningUserID+"] is not same as login user ["+sTcUserName+"]");
											return;
										}

										ReleaseStatus[] relStatusList = parentRev.get_release_status_list();
										if(relStatusList!=null && relStatusList.length>0) {
											writeLogFile(sDryRunLogFilePath, "Item ID ["+sBomID + "] LATEST Rev ID ["+sBomrev+"]"+" is Released.\nIt cannot be overridden.");
											writeLogFile(sDryRunLogFilePath, "Dry Run Results : Failed");
											btnExecute.setEnabled(false);
											getMessageBox(shlBomUtility, SWT.ICON_INFORMATION, "Information", "Item ID ["+sBomID + "] LATEST Rev ID ["+sBomrev+"]"+" is Released.\nIt cannot be overridden.");
											return;
										}

										System.out.println("Debug values ---> "+sBomID + "/"+sBomrev+"\n");
										writeLogFile(sDryRunLogFilePath, "--------------->Item ID is "+sBomID + " LATEST Rev ID is ["+sBomrev+"]\n");

										hmTblRowVsItemIDFindNo  = new HashMap<>();
										alBomLineNotInTbl = new ArrayList<>();
										alBomLineIDNotInTbl = new ArrayList<>();

										ItemRevision[] itemRevParts = new ItemRevision[gdItems.length];
										ArrayList<LinkedHashMap<String, String>> alPropValues = new ArrayList<>();

										for (int i = 0; i < gdItems.length; i++) {
											LinkedHashMap<String, String> hmRealNameVsValue = getRealNameVsValue(gdItems[i]);
											alPropValues.add(hmRealNameVsValue);
										}

										for (int i = 0; i < gdItems.length; i++) {
											LinkedHashMap<String, String> hmRealNameVsValue = getRealNameVsValue(gdItems[i]);

											String sPartNo = gdItems[i].getText(iColPartNumber);
											String sFindNo = hmRealNameVsValue.get("bl_sequence_no");
											String sRefDes = hmRealNameVsValue.get("bl_ref_designator");

											hmTblRowVsItemIDFindNo.put(i, sPartNo+sFindNo+sRefDes);
										}
									}
								} catch (Exception e2) {
									e2.printStackTrace();
								}

								summary.setsBomID_Status("Available in Teamcenter");
								writeLogFile(sDryRunLogFilePath, sEBOM_Id+" EBOM Item is Available in TC");
							}else{
								bEBOMAvailable = false;
								summary.setsBomID_Status("Not Available in Teamcenter");
								writeLogFile(sDryRunLogFilePath, sEBOM_Id+" EBOM Item is not Available in TC");
							}

							if(iCntPartNotAvail>0 && bEBOMAvailable) {
								int buttonID = getMessageBox(gridImportTable.getShell(),
										SWT.ICON_QUESTION | SWT.YES | SWT.NO, "User Confirmation",
										"Given BOM is already available in Teamcenter, however there are BOMline item(s) missing.\nDo you want to continue the process?\n\n'Yes' - Missing Parts will not be created and BOM will be overriden.\n'No' - Execute operation will be terminated.");

								writeLogFile(sDryRunLogFilePath, sEBOM_Id
										+ " BOM already exists, and few Items are missing in Teamcenter.\nDo you want to continue the process?\n\n'Yes' -  Missing Parts will not be created and BOM will be overriden.\n'No' - Execute operation will be terminated."
										+ (buttonID == SWT.YES ? "Yes" : (buttonID == SWT.NO ? "NO" : buttonID)));
							}else if(!bEBOMAvailable) {
								getMessageBox(gridImportTable.getShell(), SWT.ICON_INFORMATION | SWT.OK , "Information",
										"Top Bomline (or Parent Item) is not available in Teamcenter and this tool does not support creation of Items. So Execute Operation will not be enabled.");

								writeLogFile(sDryRunLogFilePath, "WARNING---------------->"+ " Top Bomline (or Parent Item) is not available in Teamcenter and this tool does not support creation of Items. So Execute Operation will not be enabled.");

							}else if(iCntPartNotAvail>0) {

								//if(bCREATE_MISSING_PART) {
								int result = getMessageBox(gridImportTable.getShell(), SWT.ICON_QUESTION | SWT.YES | SWT.NO,
										"User Confirmation",
										"A few Items are not available in Teamcenter.\nDo you want to create it automatically and continue the process?");

								writeLogFile(sDryRunLogFilePath, "A few Items are not available in Teamcenter.\nDo you want to create it automatically and continue the process?"+ " User Input "+ (result==SWT.YES?"Yes":(result==SWT.NO?"NO":result)));
								//}
								if(!bCREATE_MISSING_PART) {
									writeLogFile(sDryRunLogFilePath, "WARNING---------------->"+ " THIS TOOL WONT CREATE ANY MISSING PART. PLS CONTACT ADMINISTRATOR");
								}


							}else if(bEBOMAvailable) {
								int buttonID = getMessageBox(gridImportTable.getShell(), SWT.ICON_QUESTION | SWT.YES | SWT.NO, "User Confirmation",
										"BOM is already available in Teamcenter.\nDo you want to override the BOM Information?");

								writeLogFile(sDryRunLogFilePath, "BOM is already available in Teamcenter.\nDo you want to override the BOM Information?"+ " User Input "+ (buttonID==SWT.YES?"Yes":(buttonID==SWT.NO?"NO":buttonID)));
							}

							if(bEBOMAvailable) {
								btnExecute.setEnabled(true);
							}

							//writeLogFile(sDryRunLogFilePath, "Dry Run Results : \n"+sBomIDErrors + sLineErrors + sPWBErrors + sItemAvailInfo);

							writeLogFile(sDryRunLogFilePath, "===========================================================================================", false);
							writeLogFile(sDryRunLogFilePath, "Dry Run Results : Success");	
							writeLogFile(sDryRunLogFilePath, summary.toString());

							String sTempID = "";
							int iCurrentFindNo = 0;
							//HashMap<Integer, Integer> hmRowVsFindNo = new HashMap<>();
							if(bGenerateFindNo) {
								writeLogFile(sDryRunLogFilePath, "NOTE : 'Find No' is generated based on the configuration file.");
								for (int i = 0; i < gdItems.length; i++) {
									boolean bFindNoWithIDExist = false;
									int iFindNoPreviouslySet = 0;
									String sCurrID = gdItems[i].getText(iColPartNumber);
									System.out.println("Curr ID is"+sCurrID);
									if(i==0) {
										iCurrentFindNo += iFindNoStart;
									}else {
										if(!sCurrID.equalsIgnoreCase(sTempID)) {

											for (int j = 0; j < i; j++) {
												String sPreviousID = gdItems[j].getText(iColPartNumber);
												if(sPreviousID.equalsIgnoreCase(sCurrID)) {
													bFindNoWithIDExist = true;
													iFindNoPreviouslySet = Integer.parseInt(gdItems[j].getText(1));
												}
											}
											if(!bFindNoWithIDExist) {
												sTempID = sCurrID;
												iCurrentFindNo += iFindNoIncrement;
											}
										}
									}
									if(bFindNoWithIDExist) {
										gdItems[i].setText(1, iFindNoPreviouslySet+"");
									}else {
										gdItems[i].setText(1, iCurrentFindNo+"");
									}

								}
							}else {
								writeLogFile(sDryRunLogFilePath, "NOTE : 'Find No' is considered from the imported Excel file.");
							}
						}
						else {
							btnExecute.setEnabled(false);
							writeLogFile(sDryRunLogFilePath, "Dry Run Results : Failed\n"+sBomIDErrors + sLineErrors + sPWBErrors + sItemAvailInfo);

							writeLogFile(sDryRunLogFilePath, "Dry Run Failed, Please check the Above Errors.");
						}
					}else {
						writeLogFile(sDryRunLogFilePath, "Invalid input format.\n\nPlease contact your System Administrator.");
					}
					//copyDemo1.copyThread.cancel();
				} catch (Exception e3) {
					// TODO Auto-generated catch block
					e3.printStackTrace();
				} finally {
					updateLogFilePath();
					btnDryRun.setEnabled(true);
					writeLogFile(sDryRunLogFilePath, "Dry Run Completed");
					getMessageBox(shlBomUtility, SWT.ICON_INFORMATION, "Information", "Dry Run operation completed, please check the log for more details.");
					shlBomUtility.setCursor(cursorArrow);
				}
				try {
					if(session!=null) {
						session.logout();
					}
					System.out.println("Logged out Successfully...");
				} catch (Exception e2) {
					System.out.println("Logout Operation Failed!...");
				}
			}
		});
		btnDryRun.setText("Dry Run");
		btnDryRun.setEnabled(false);

		btnExecute = new Button(compRightBtns, SWT.NONE);
		btnExecute.addSelectionListener(new SelectionAdapter() {
			@Override
			public void widgetSelected(SelectionEvent e) {
				shlBomUtility.setCursor(cursorWait);
				boolean bAttachedSuccessfully = true;
				String summaryFileName="";
				try {
					sExecuteLogFilePath =  System.getProperty("java.io.tmpdir") + "\\"+"LegacyBOM_Execute_Logs_"+smpd.format(new Date()).toString().replaceAll(":", "_")+".log";
					summaryFileName = "LegacyBOM_Summary_Logs_"+smpd.format(new Date()).toString().replaceAll(":", "_")+".log";
					
					sSummaryLogFilePath =  System.getProperty("java.io.tmpdir") + "\\"+summaryFileName;

					session.logout();			        
					user = session.loginUsingArguments(sTcUserName,sTcPassword);
					printTCURLInLog(sExecuteLogFilePath);
					writeLogFile(sExecuteLogFilePath, "Login is successful");
				} catch (Exception e1) {
					e1.printStackTrace();
				}

				writeLogFile(sExecuteLogFilePath, "Execute Started");
				StringBuilder sbReportTemplate = new StringBuilder();
				sbReportTemplate.append("--------------------------------------------------------------------\n");
				sbReportTemplate.append("\t\t\t\t\tExecute Operation Report\n");
				sbReportTemplate.append("--------------------------------------------------------------------\n");
				try {
					sbReportTemplate.append("User:"+ user.get_user_name()+" ("+user.get_user_id()+")\t\t");
				} catch (NotLoadedException e4) {
					// TODO Auto-generated catch block
					e4.printStackTrace();
				}
				sbReportTemplate.append("Time: "+ smpd.format(new Date()).toString()+"\n");
				Summary summary = new Summary();
				summary.setsMode("Execute");
				summary.setiTotalExcelRows(gridImportTable.getItemCount());
				sbReportTemplate.append("Total Item Ids: "+gridImportTable.getItemCount()+"\n");
				String sEBOM_Id = "";
				boolean bEBOMAvailable = true;

				GridItem[] gdItems = gridImportTable.getItems();

				if(gdItems.length>0){
					sEBOM_Id = gdItems[0].getText(0);
				}
				sbReportTemplate.append("EBOM ID: "+sEBOM_Id+"\n");
				sbReportTemplate.append("--------------------------------------------------------------------\n");
				if(gdItems.length>0){
					HashMap<String, Integer> hmTblNameVsTblCol = new HashMap<>();

					GridColumn[] gdCols = gridImportTable.getColumns();
					for (int i = 0; i < gdCols.length; i++) {
						hmTblNameVsTblCol.put(gdCols[i].getText(), i);
					}

					Integer iFindNoTblCol = hmTblNameVsTblCol.get("FIND");
					Integer iBOMTblCol = hmTblNameVsTblCol.get("BOM");
					Integer iPartTblCol = hmTblNameVsTblCol.get("PART");
					Integer iRefDesTblCol = hmTblNameVsTblCol.get("REF DES");

					HashMap<String, Integer> hmPartVsQuantity = new HashMap<>();                	

					for (int i = 0; i < gdItems.length; i++) {
						String sFindNo = gdItems[i].getText(iFindNoTblCol);
						String sBomID = gdItems[i].getText(iBOMTblCol);
						String sPartID = gdItems[i].getText(iPartTblCol);
						String sRefDes = gdItems[i].getText(iRefDesTblCol);

						if(hmPartVsQuantity.isEmpty() || (!hmPartVsQuantity.containsKey(sFindNo+sRefDes+sPartID))) {
							hmPartVsQuantity.put(sFindNo+sRefDes+sPartID, 1);
						}else if(hmPartVsQuantity.containsKey(sFindNo+sRefDes+sPartID)){
							int oldQuantity = hmPartVsQuantity.get(sFindNo+sRefDes+sPartID);
							hmPartVsQuantity.put(sFindNo+sRefDes+sPartID, (oldQuantity+1));
						}
					}

					for (int i = 0; i < gdItems.length; i++) {
						String sFindNo = gdItems[i].getText(iFindNoTblCol);
						String sBomID = gdItems[i].getText(iBOMTblCol);
						String sPartID = gdItems[i].getText(iPartTblCol);
						String sRefDes = gdItems[i].getText(iRefDesTblCol);

						if(hmPartVsQuantity.containsKey(sFindNo+sRefDes+sPartID)){
							int Quantity = hmPartVsQuantity.get(sFindNo+sRefDes+sPartID);
							gdItems[i].setData("QUANTITY_CALCULATED", Quantity+"");
						}
					}
				}

				hmTblRowVsItemIDFindNo  = new HashMap<>();
				alBomLineNotInTbl = new ArrayList<>();
				alBomLineIDNotInTbl = new ArrayList<>();

				summary.setsBomID(sEBOM_Id);
				ModelObject[] eBomObj = query.queryItemsUsingID(sEBOM_Id, "*");
				if(eBomObj!=null && eBomObj.length>0){
					summary.setsBomID_Status("Already Exist in Teamcenter");
					writeLogFile(sExecuteLogFilePath, "EBOM Item is Available in TC");
				}else{
					bEBOMAvailable = false;
					writeLogFile(sExecuteLogFilePath, "EBOM Item is not Available in TC");
				}

				boolean bAllPartsAvailable = false;
				try {
					bAllPartsAvailable = updatePartAvailability(gridImportTable, sExecuteLogFilePath).isEmpty();
				} catch (Exception e3) {
					// TODO Auto-generated catch block
					e3.printStackTrace();
				}

				int result = -100;
				//------------------------------------------------------------
				if(iCntPartNotAvail>0 && bEBOMAvailable) {
					result = getMessageBox(gridImportTable.getShell(),
							SWT.ICON_QUESTION | SWT.YES | SWT.NO, "User Confirmation",
							"EBOM Item is already Available in TC and A few Items are not available in Teamcenter\nDo you want to continue the process?\nIf you press Yes - Missing Parts will not be created and BOM will be overriden.\nIf you press No - process will be terminated.");
					writeLogFile(sExecuteLogFilePath,
							"EBOM Item is already Available in TC and A few Items are not available in Teamcenter\\nDo you want to continue the process?\\nIf you press Yes - Missing Parts will not be created and BOM will be overriden.\\nIf you press No - process will be terminated."
									+ " User Selection is "+(result==SWT.YES?"Yes":(result==SWT.NO?"NO":result)));

				}else if(iCntPartNotAvail>0) {
					//if(bCREATE_MISSING_PART) {
					result = getMessageBox(gridImportTable.getShell(), SWT.ICON_QUESTION | SWT.YES | SWT.NO,
							"User Confirmation",
							"A few Items are not available in Teamcenter.\nDo you want to create it automatically and continue the process?");
					writeLogFile(sExecuteLogFilePath,
							"A few Items are not available in Teamcenter.\nDo you want to create it automatically and continue the process?"
									+ " User Selection is "+(result==SWT.YES?"Yes":(result==SWT.NO?"NO":result)));
					//}
					if(!bCREATE_MISSING_PART) {
						writeLogFile(sDryRunLogFilePath, "WARNING---------------->"+ " THIS TOOL WILL NOT CREATE ANY MISSING PART. PLS CONTACT ADMINISTRATOR");
						//result = -102;
					}
				}else if(bEBOMAvailable) {
					result = getMessageBox(gridImportTable.getShell(), SWT.ICON_QUESTION | SWT.YES | SWT.NO, "User Confirmation",
							"EBOM Item is already Available in TC\nDo you want to override the BOM Information?");
					writeLogFile(sExecuteLogFilePath,
							"EBOM Item is already Available in TC\nDo you want to override the BOM Information?"
									+ " User Selection is "+(result==SWT.YES?"Yes":(result==SWT.NO?"NO":result)));
				}

				boolean bPWB_NotAvailable = false, bPWB_Created = false;

				if(result!=-100 && result == SWT.NO) {
					shlBomUtility.setCursor(cursorArrow);
					updateLogFilePath();
					return;
				}

				if(!bAllPartsAvailable && result!=-100) {
					if(result == SWT.YES) {
						writeLogFile(sExecuteLogFilePath, "INFO : Started creating PWB/MOOG Parts");
						writeLogFile(sExecuteLogFilePath, "INFO : No of parts to be created in Teamcenter :: " + iCntPartNotAvail);

						int iNewPartsCreated = 0, iPartsFailed = 0;

						for (int i = 0; i < gdItems.length; i++) {
							String sPartNo = gdItems[i].getText(iColPartNumber), sItemType = "";

							if(gdItems[i].getText(iCol_TC_ObjStatus).equalsIgnoreCase("Not Available")) {

								writeLogFile(sExecuteLogFilePath, "Info : Part not available - Started creating Excel Row : "+(i+1)+" in TC. Part no : "+sPartNo+" Type : "+sItemType);

								if(i==0) {
									summary.setsPWB_ItemID(sPartNo);
								}

								ModelObject[] modelObject = null;
								if(bCREATE_MISSING_PART) {
									modelObject = createItem(sPartNo, sPartNo, "01", "", sItemType, "", "");
								}

								if(!bCREATE_MISSING_PART) {
									iPartsFailed++;
									writeLogFile(sExecuteLogFilePath, "Error : Part ["+sPartNo+"] is not created. Part ID : " + sPartNo+" Type : "+sItemType);
								}else if(modelObject==null || modelObject.length==0) {
									iPartsFailed++;
									writeLogFile(sExecuteLogFilePath, "Error : Part creation failed. Part ID : " + sPartNo+" Type : "+sItemType);
								}else {
									if(i ==0) {
										bPWB_Created = true;
									}
									iNewPartsCreated++;
									writeLogFile(sExecuteLogFilePath, "Info : Part creation Success - Completed creating Excel Row : "+(i+1)+" in TC. Part no : "+sPartNo+" Type : "+sItemType);
									if(i==0) {
										summary.setsPWB_ItemID(sPartNo);
										summary.setsPWB_ItemID_Status("Newly Created");
									}else {
										summary.setsNewPartsList(sPartNo);
									}
								}
							}else {
								if(i==0) {
									summary.setsPWB_ItemID(sPartNo);
									summary.setsPWB_ItemID_Status("Already Exist");
								}
							}
						}

						writeLogFile(sExecuteLogFilePath, "Info : Completed creating PWB/MOOG Parts - count of total Parts Created" + iNewPartsCreated + "count of total parts creation failed "+iPartsFailed);

						summary.setiNewPartsCnt(iNewPartsCreated);

						if(bPWB_NotAvailable && (!bPWB_Created)) {//TODO: Validate this Condition...
							summary.setsPWB_ItemID_Status("Newly Created");
						}else if(bPWB_NotAvailable && bPWB_Created) {
							summary.setiNewPartsCnt((iNewPartsCreated-1));
						}

						try {
							bAllPartsAvailable =  updatePartAvailability(gridImportTable, "").isEmpty();
						} catch (Exception e1) {
							e1.printStackTrace();
						}
						if(!bAllPartsAvailable) {
							getMessageBox(gridImportTable.getShell(), SWT.ICON_INFORMATION|SWT.OK,
									"Item Creation Failed",
									"Kindly check the Log Files or console for more Details.");
						}else {
							getMessageBox(gridImportTable.getShell(), SWT.ICON_INFORMATION|SWT.OK,
									"Item Creation Successful",
									"Item Creation Completed Successfully.");
						}
					}
				}else {
					summary.setsPWB_ItemID(gdItems[0].getText(iColPartNumber));
					summary.setsPWB_ItemID_Status("Already Exist");
				}

				if(summary.getsPWB_ItemID_Status()!=null && !summary.getsPWB_ItemID_Status().isEmpty() && summary.getsPWB_ItemID_Status().equalsIgnoreCase("Already Exist")) {
					summary.setiAvailPartsCnt(iCntPartAvail);
					for (String sPart : alPartsExist) {
						if(true/*!sPart.equalsIgnoreCase(summary.getsPWB_ItemID())*/)
							summary.setsAvailPartsList(sPart);
					}
				}else {
					summary.setiAvailPartsCnt(iCntPartAvail);
					for (String sPart : alPartsExist) {
						summary.setsAvailPartsList(sPart);
					}
				}


				int iColPartNumber = BomUtility_Grid_Col_Create.GetColSeqInGrid(gridImportTable, "ImportTable_COL5");

				if(bEBOMAvailable && result!=-100) {
					if(result == SWT.YES) {
						writeLogFile(sExecuteLogFilePath, "EBOM Item is already Available in TC\nDo you want to override the BOM Information? "+ " User Confirmation : YES");

						System.out.println("Yes Pressed");
						try {
							//writeLogFile(sExecuteLogFilePath, "Started removing child bomlines");
							//removeBomLines((Item)eBomObj[0]);
							//writeLogFile(sExecuteLogFilePath, "Completed removing child bomlines");
						} catch (Exception e2) {
							e2.printStackTrace();
						}

						ItemRevision[] itemRevParts = new ItemRevision[gdItems.length];
						ArrayList<LinkedHashMap<String, String>> alPropValues = new ArrayList<>();

						ArrayList<String> partListOnExecute = new ArrayList<String>();		
						for (int i = 0; i < gdItems.length; i++) {//for gditems
							String sPartNo = gdItems[i].getText(iColPartNumber);			
							partListOnExecute.add(sPartNo);
						}//for gditems
						writeLogFile(sExecuteLogFilePath, "There are [" + partListOnExecute.size() + "],ItemID's from Grid Table");

						HashMap<String, ItemRevision> hmItemIDVsItemRevObj = new HashMap<>();
						try {
							String sTempPartID = "";
							ModelObject[] childPartsMO = query.queryAllChildItems(partListOnExecute);
							if(childPartsMO!=null){
								dmService.getProperties(childPartsMO, new String[] {"revision_list", "item_id", "object_type"});
								for (int j = 0; j < childPartsMO.length; j++) {
									if(childPartsMO[j] instanceof com.teamcenter.soa.client.model.strong.Item){				            			
										Item itemPart = (Item)childPartsMO[j];
										try {
											sTempPartID = itemPart.getPropertyDisplayableValue("item_id");
											ModelObject[] temprevList = itemPart.get_revision_list();
											if(hmItemIDVsItemRevObj.containsKey(sTempPartID)) {
												String sTempPartType = hmItemIDVsItemRevObj.get(sTempPartID).getPropertyDisplayableValue("object_type");
												String sCurrPartType = temprevList[temprevList.length-1].getPropertyDisplayableValue("object_type");

												if(sTempPartType.contains("AD4_Legacy_Item")||sCurrPartType.contains("Vendor")) {

												}else if(sTempPartType.contains("GL4_Vendor_Part") && sCurrPartType.contains("AD4_Legacy_Item")) {
													hmItemIDVsItemRevObj.put(sTempPartID, (ItemRevision)temprevList[temprevList.length-1]); 
												}else if(sTempPartType.contains("GL4_Vendor_Part")) {
													hmItemIDVsItemRevObj.put(sTempPartID, (ItemRevision)temprevList[temprevList.length-1]); 
												}else   {
													hmItemIDVsItemRevObj.put(sTempPartID, (ItemRevision)temprevList[temprevList.length-1]); 
												}

											}else {
												hmItemIDVsItemRevObj.put(sTempPartID, (ItemRevision)temprevList[temprevList.length-1]);
											}

										} catch (NotLoadedException e1) {
											e1.printStackTrace();
										}
									}
								}
							}
						} catch (Exception e3) {
							e3.printStackTrace();
						}
						StringBuilder sbUnavailablePartsList = new StringBuilder();
						StringBuilder sbAvailablePartsList = new StringBuilder();
						int unAvailableCount =1;
						int loadedCount =1;
						
						for (int i = 0; i < gdItems.length; i++) {//for							

							LinkedHashMap<String, String> hmRealNameVsValue = getRealNameVsValue(gdItems[i]);
							alPropValues.add(hmRealNameVsValue);
							String sItemType = null;
							String sPartNo = gdItems[i].getText(iColPartNumber);

							if(hmItemIDVsItemRevObj.containsKey(sPartNo)){
								itemRevParts[i] = hmItemIDVsItemRevObj.get(sPartNo);
								/*
								 * dmService.getProperties(new ModelObject[]{itemRevParts[i]}, new String[]{
								 * "object_type"}); try { String obj_type =
								 * itemRevParts[i].getPropertyDisplayableValue("object_type");
								 * //sbAvailablePartsList.append(loadedCount+") "+sPartNo+"- "+obj_type+"\n"); }
								 * catch (NotLoadedException e1) { // TODO Auto-generated catch block
								 * e1.printStackTrace(); }
								 */
								
								loadedCount++;
							}else {
								sbUnavailablePartsList.append(unAvailableCount+") "+sPartNo+"\n");
								unAvailableCount++;
							}

						}//for
						/*
						 * sbReportTemplate.append("Loaded TC Count: "+(loadedCount-1)+"\n");
						 * sbReportTemplate.append(
						 * "--------------------------------------------------------------------\n");
						 * 
						 * sbReportTemplate.append(sbAvailablePartsList+"\n"); sbReportTemplate.append(
						 * "--------------------------------------------------------------------\n");
						 */sbReportTemplate.append("Unavailable TC Count: "+(unAvailableCount-1)+"\n");
						sbReportTemplate.append("--------------------------------------------------------------------\n");
						sbReportTemplate.append(sbUnavailablePartsList+"\n");
						try {
							Item parentItem = (Item)eBomObj[0];
							ModelObject[] temprevList = parentItem.get_revision_list();
							ItemRevision tempParentRev = (ItemRevision)temprevList[temprevList.length-1];

							dmService.getProperties(new ModelObject[]{tempParentRev}, new String[]{ "item_id","current_revision_id ", "item_revision_id"});
							String sBomID = tempParentRev.getPropertyDisplayableValue("item_id");
							String sBomrev = tempParentRev.getPropertyDisplayableValue("item_revision_id");
							System.out.println("Debug values ---> "+sBomID + "/"+sBomrev+"\n");
							writeLogFile(sExecuteLogFilePath, "--------------->Item ID is "+sBomID + " LATEST Rev ID is ["+sBomrev+"]\n");

							for (int i = 0; i < gdItems.length; i++) {
								LinkedHashMap<String, String> hmRealNameVsValue = getRealNameVsValue(gdItems[i]);

								String sPartNo = gdItems[i].getText(iColPartNumber);
								String sFindNo = hmRealNameVsValue.get("bl_sequence_no");
								String sRefDes = hmRealNameVsValue.get("bl_ref_designator");

								hmTblRowVsItemIDFindNo.put(i, sPartNo+sFindNo+sRefDes);
							}

							truncateAsembly(tempParentRev, sExecuteLogFilePath);

							readBomdetails(tempParentRev, sExecuteLogFilePath);
							if(alBomLineNotInTbl.size() > 0) {
								removeExistingBomLines(bomWindowObj, alBomLineNotInTbl, sExecuteLogFilePath, false);
							}

							writeLogFile(sExecuteLogFilePath, "--------------->Started Building the Assembly Structure.....\n");							

							if(itemRevParts!=null && itemRevParts.length>0) {
								//attachChild((Item)eBomObj[0], itemRevParts[0].get_items_tag(), alPropValues.get(0));

								ArrayList<ModelObject> hmChildRevMO = new ArrayList<>();
								ArrayList<LinkedHashMap<String, String>> alChildRevMOPropValues = new ArrayList<>();

								String sPartentID = "";
								Item parent = (Item)eBomObj[0];

								String[] props = { "item_id", "item_revision", "item_revision_id", "revision_list"};
								dmService.getProperties( new ModelObject[]{parent}, props );
								dmService.getProperties( itemRevParts, props );

								ItemRevision parentRev = null;
								ModelObject childRev = null;

								ModelObject[] revList = parent.get_revision_list();        
								if (revList.length > 0)
								{
									parentRev = (ItemRevision) revList[revList.length-1];
								}
								sPartentID = parent.get_item_id();

								if(itemRevParts.length>0) {//if
									boolean retValue = true;
									for (int i = 0; i < itemRevParts.length; i++) {//for										
										writeLogFile(sExecuteLogFilePath, "---__>PROCESSING, Child Line Item   ["+(i+1) + "] of ["+ itemRevParts.length+"]\n");									

										if(itemRevParts[i]!=null) {//if parts null					            			
											Item child = itemRevParts[i].get_items_tag();
											String sChildID = itemRevParts[i].getPropertyDisplayableValue("item_id");

											try {
												String sFindNo = (alPropValues.get(i)).get("bl_sequence_no");
												String sRefDes = (alPropValues.get(i)).get("bl_ref_designator");
												Integer iExistLineIdx = isBomLineExist(sChildID+sFindNo+sRefDes);
												if(iExistLineIdx != -1){
													writeLogFile(sExecuteLogFilePath, "!!!!!!WARNING!!!!--->BomLine Item already Exists,  Item ID is "+sChildID+" Find No is "+sFindNo+" Ref Des is "+sRefDes);						            				
													continue;
												}
											} catch (Exception e2) {
												e2.printStackTrace();
											} 

											hmChildRevMO.add(itemRevParts[i]);
											alChildRevMOPropValues.add(alPropValues.get(i));
										}//if parts null
										else if(!bCREATE_MISSING_PART) {
											String sPartNo = gdItems[i].getText(iColPartNumber);					            			
											writeLogFile(sExecuteLogFilePath, "!!!!!!WARNING!!!!: sPartNo ["+sPartNo+"] is NOT CREATED, SO WONT BE IN BOM");
										}
										writeLogFile(sExecuteLogFilePath, "---__>PROCESSED STATUS , Child Line Item   [" + (i+1) + "] Creation Status [" + bCREATE_MISSING_PART + "], Update Status[" + retValue + "]\n");
									}//for

									ItemLineInfo[] itemLineInfos = new ItemLineInfo[hmChildRevMO.size()];
									for (int i = 0; i < hmChildRevMO.size(); i++) {
										itemLineInfos[i] = new ItemLineInfo();        
										itemLineInfos[i].itemRev = hmChildRevMO.get(i);
										itemLineInfos[i].itemLineProperties = alChildRevMOPropValues.get(i);//This is map of properties and their values
									}

									bAttachedSuccessfully = attachChildInBulk(parentRev, itemLineInfos, sbReportTemplate);

									writeLogFile(sExecuteLogFilePath, "--------------->FINISHED Building the Assembly Structure.....\n");

								}//if
							}
						} catch (Exception e1) {
							e1.printStackTrace();
						}
						summary.setsBomID_Status("Already Exist in Teamcenter & BOM Details Updated.");
					}
				} else {
					//if (result == SWT.YES) 
					{
						summary.setsBomID(sEBOM_Id);

						writeLogFile(sExecuteLogFilePath,
								"Info : EBOM not available - Started creating in TC. Part no : " + sEBOM_Id + " Type : "
										+ strToplineItemType);

						ModelObject[] modelObject = createItem(sEBOM_Id, sEBOM_Id, "01", "", strToplineItemType, "",
								"");

						if (modelObject == null || modelObject.length == 0) {
							writeLogFile(sExecuteLogFilePath, "Error : EBOM Item creation Failed. Item ID" + sEBOM_Id);
						} else {
							writeLogFile(sExecuteLogFilePath,
									"INFO : EBOM Item creation Successfully. Item ID" + sEBOM_Id);
							summary.setsBomID_Status("Newly Created");
						}

						ItemRevision itemRevEBOM = null;

						if (modelObject != null && modelObject.length > 0) {
							for (int i = 0; i < modelObject.length; i++) {
								if (modelObject[i] instanceof com.teamcenter.soa.client.model.strong.ItemRevision) {
									itemRevEBOM = (ItemRevision) modelObject[i];
								} else if (modelObject[i] instanceof com.teamcenter.soa.client.model.strong.Item) {
									Item itemEBOM = (Item) modelObject[i];
									try {
										dmService.getProperties(new ModelObject[] { itemEBOM },
												new String[] { "revision_list" });
										ModelObject[] temprevList = itemEBOM.get_revision_list();
										itemRevEBOM = (ItemRevision) temprevList[temprevList.length - 1];
										// itemRevEBOM = (ItemRevision) itemEBOM.get_revision_list()[0];
									} catch (NotLoadedException e1) {
										e1.printStackTrace();
									}
								}
							}
						}

						ItemRevision[] itemRevParts = new ItemRevision[gdItems.length];

						ArrayList<LinkedHashMap<String, String>> alPropValues = new ArrayList<>();

						for (int i = 0; i < gdItems.length; i++) {
							if(i%25 == 0) {
								writeLogFile(sExecuteLogFilePath, "--------------->Running ["+i + "] of ["+gdItems.length+"]\n");
							}
							LinkedHashMap<String, String> hmRealNameVsValue = getRealNameVsValue(gdItems[i]);
							alPropValues.add(hmRealNameVsValue);
							String sItemType = null;
							String sPartNo = gdItems[i].getText(iColPartNumber);
							if (i == 0) {
								sItemType = prop.getProperty("FIRST_LINE_ITEM_TYPE");
							} else {
								sItemType = prop.getProperty("CHILD_LINE_ITEM_TYPE");
							}
							ModelObject[] moPartObjs = query.queryItemsUsingID(sPartNo, "*");
							if (moPartObjs == null)
								continue;
							for (int j = 0; j < moPartObjs.length; j++) {
								if (moPartObjs[j] instanceof com.teamcenter.soa.client.model.strong.ItemRevision) {
									itemRevParts[j] = (ItemRevision) moPartObjs[j];
								} else if (moPartObjs[j] instanceof com.teamcenter.soa.client.model.strong.Item) {
									Item itemPart = (Item) moPartObjs[j];
									try {
										dmService.getProperties(new ModelObject[] { moPartObjs[j] },
												new String[] { "revision_list" });
										// itemRevParts[i] = (ItemRevision) itemPart.get_revision_list()[0];
										ModelObject[] temprevList = itemPart.get_revision_list();
										itemRevParts[i] = (ItemRevision) temprevList[temprevList.length - 1];
									} catch (NotLoadedException e1) {
										e1.printStackTrace();
									}
								}
							}
						}

						try {
							if (itemRevParts != null && itemRevParts.length > 0) {
								// attachChild(itemRevEBOM.get_items_tag(), itemRevParts[0].get_items_tag(),
								// alPropValues.get(0));
								if (itemRevParts.length > 0) {
									for (int i = 0; i < itemRevParts.length; i++) {
										if (itemRevParts[i] != null) {
											dmService.getProperties(new ModelObject[] { itemRevEBOM, itemRevParts[i] },
													new String[] { "items_tag" });
											Item parent = (Item) (itemRevEBOM.get_items_tag()),
													child = itemRevParts[i].get_items_tag();
											String sPartentID = "", sChildID = "";
											try {
												dmService.getProperties(new ModelObject[] { parent, child },
														new String[] { "item_id" });
												sPartentID = parent.get_item_id();
												sChildID = child.get_item_id();
											} catch (Exception e2) {
												e2.printStackTrace();
											}
											writeLogFile(sExecuteLogFilePath, "Started attaching child bomline("
													+ sChildID + ") to Parent" + sPartentID);

											if (attachChild(parent, child, alPropValues.get(i))) {
												writeLogFile(sExecuteLogFilePath, "Completed attaching child bomline("
														+ sChildID + ") to Parent" + sPartentID);
											} else {
												bAttachedSuccessfully = false;
												writeLogFile(sExecuteLogFilePath,
														"Error occured while attaching child bomline(" + sChildID
														+ ") to Parent" + sPartentID
														+ " Kindly chek the Above Errors.");
											}
											if (bAttachedSuccessfully)
												summary.setsBomID_Status("Newly Created & BOM Details Updated.");
											else
												summary.setsBomID_Status("Newly Created.");
										} else if (!bCREATE_MISSING_PART) {
											String sPartNo = gdItems[i].getText(iColPartNumber);
											writeLogFile(sExecuteLogFilePath, "Error: sPartNo [" + sPartNo
													+ "] is not created. Also it will not be available in BOM");
										}
									}
								}
							}
						} catch (Exception e1) {
							e1.printStackTrace();
						}
					}
				}
				writeLogFile(sSummaryLogFilePath, sbReportTemplate.toString(),false);
				writeLogFile(sExecuteLogFilePath, "Execute Completed");
				updateLogFilePath();
				if(bAttachedSuccessfully && iCntPartNotAvail>0){
					getMessageBox(shlBomUtility, SWT.ICON_INFORMATION | SWT.OK, "Error", "Execute operation has been completed but not all parts were loaded. Kindly check the Log File.");
					
					//sendEmail(sSummaryLogFilePath, summaryFileName,sEBOM_Id);
				}else if(bAttachedSuccessfully) {
					writeLogFile(sExecuteLogFilePath, "===========================================================================================", false);
					writeLogFile(sExecuteLogFilePath, "Execution Results : Success");	
					writeLogFile(sExecuteLogFilePath, summary.toString());
					getMessageBox(shlBomUtility, SWT.ICON_INFORMATION | SWT.OK, "Information", "Execute operation has been completed successfully.");



				}else {
					getMessageBox(shlBomUtility, SWT.ICON_INFORMATION | SWT.OK, "Error", "Execute operation has been failed. Kindly check the Log File.");
					//sendEmail(sSummaryLogFilePath, summaryFileName,sEBOM_Id);
				}
				shlBomUtility.setCursor(cursorArrow);

				try {
					session.logout();	
					writeLogFile(sExecuteLogFilePath, "Logged out successfully");
				} catch (Exception e1) {
					e1.printStackTrace();
				}

			}
		});
		btnExecute.setText("Execute");
		btnExecute.setEnabled(false);

		btnClear = new Button(compRightBtns, SWT.NONE);
		btnClear.addSelectionListener(new SelectionAdapter() {
			@Override
			public void widgetSelected(SelectionEvent e) {
				try {
					btnImport.setEnabled(true);
					hmStoreItemisVsRefDes.clear();
					sDryRunLogFilePath = "";
					sExecuteLogFilePath = "";
					sSummaryLogFilePath="";
					updateLogFilePath();
					GridColumn[] gdCols = gridImportTable.getColumns();

					for (int i = 0; i < gdCols.length; i++) {
						boolean bAvailInMapping = false;
						for (int j = 0; j < sArrHeaders.length; j++) {
							if(gdCols[i].getText().trim().equalsIgnoreCase(sArrHeaders[j].trim()))
								bAvailInMapping = true;
						}
						if(!bAvailInMapping) {
							gdCols[i].dispose();
						}
					}

					btnImport.setEnabled(true);
					btnDryRun.setEnabled(false);
					btnExecute.setEnabled(false);
					gridImportTable.disposeAllItems();
					gridImportTable.removeAll();
				} catch (Exception e2) {
					// TODO: handle exception
				}
			}
		});
		btnClear.setText("Clear");

		btnClose = new Button(compRightBtns, SWT.NONE);
		btnClose.addSelectionListener(new SelectionAdapter() {
			@Override
			public void widgetSelected(SelectionEvent e) {
				shlBomUtility.close();
			}
		});
		btnClose.setText("Close");

		gridImportTable.pack();
		updateLogFilePath();
	}

	protected void sendEmail(String filePath, String filename, String eBOM) {
		int result = getMessageBox(gridImportTable.getShell(), SWT.ICON_QUESTION | SWT.YES | SWT.NO, "User Confirmation",
				"Do you want to send the email?");
		if(result==SWT.YES) {
			try 
			{ 
				EnvelopeSend envelope = new EnvelopeSend(AppXSession.getConnection()); 

				if(envelope.createEnvelope(filePath, filename,eBOM)) 
					envelope.sendEnvelope(); 
			} 
			catch (ServiceException e) 
			{ 
				e.printStackTrace(); 
			}
		}

	}

	public void addExtraCols(Grid gdTable) {
		GridColumn[] gdColumns = gdTable.getColumns();

		boolean bExtraColsAdded = false;

		for (int i = 0; i < gdColumns.length; i++) {
			if(gdColumns[i].getText().equalsIgnoreCase("Item") || gdColumns[i].getText().equalsIgnoreCase("Teamcenter Status")) {
				bExtraColsAdded = true;
			}
		}
		if(!bExtraColsAdded){
			BomUtility_Grid_Col_Create.BomUtility_Import_Tbl_AddExtra_GdCol(gdTable);
			gdTable.redraw();
		}
		getColSeq();
	}




	public String updatePartAvailability(Grid gdTable, String sLogFilePath) throws Exception{//updatePartAvailability		
		String sPartAvailError = "";		
		GridItem[] gdItems = gdTable.getItems();		
		ArrayList<String> partList = new ArrayList<String>();		
		for (int i = 0; i < gdItems.length; i++) {//for gditems
			String sPartNo = gdItems[i].getText(iColPartNumber);
			if(!partList.contains(sPartNo)) {
				partList.add(sPartNo);
			}

			gdItems[i].setText(iCol_TC_ObjStatus, "Not Available");
		}//for gditems
		writeLogFile(sLogFilePath, "There are [" + partList.size() + "],ItemID's from Grid Table");
		HashMap<String, String> hmItemIDVsItemRevObj = new HashMap<>();
		ModelObject[] childPartsMO = query.queryAllChildItems(partList);			
		dmService.getProperties(childPartsMO, new String[] {"item_id","object_type"});
		for (int j = 0; j < childPartsMO.length; j++) {

			if(childPartsMO[j] instanceof com.teamcenter.soa.client.model.strong.Item){				            			
				Item itemPart = (Item)childPartsMO[j];
				try {
					String sTempPartID = childPartsMO[j].getPropertyDisplayableValue("item_id");

					String sItemType = childPartsMO[j].getPropertyObject("object_type").getStringValue();
					//ModelObject[] temprevList = itemPart.get_revision_list();
					if(hmItemIDVsItemRevObj.containsKey(sTempPartID)) {
						String sTempPartType = hmItemIDVsItemRevObj.get(sTempPartID);
						String sCurrPartType = sItemType;

						if(sTempPartType.contains("AD4_Legacy_Item")||sCurrPartType.contains("Vendor")) {

						}else if(sTempPartType.contains("GL4_Vendor_Part") && sCurrPartType.contains("AD4_Legacy_Item")) {
							hmItemIDVsItemRevObj.put(sTempPartID, sCurrPartType); 
						}else if(sTempPartType.contains("GL4_Vendor_Part")) {
							hmItemIDVsItemRevObj.put(sTempPartID, sCurrPartType); 
						}else   {
							hmItemIDVsItemRevObj.put(sTempPartID, sCurrPartType); 
						}

					}else {
						hmItemIDVsItemRevObj.put(sTempPartID, sItemType);
					}

				} catch (NotLoadedException e1) {
					e1.printStackTrace();
				}
			}
		}
		/*for (int i = 0; i < childPartsMO.length; i++) {//for i
		 if(childPartsMO[i] == null)
			 continue;
		String sResultPartID = childPartsMO[i].getPropertyDisplayableValue("item_id");

		String sItemType = childPartsMO[i].getPropertyObject("object_type").getStringValue();*/

		for (int j = 0; j < gdItems.length; j++) {//for j
			String sPartNo = gdItems[j].getText(iColPartNumber);			
			if(sPartNo==null || sPartNo.trim().isEmpty()) {continue;}

			//writeLogFile(sLogFilePath, "part ID is ["+sPartNo+"] "+" Result Part ID is ["+sResultPartID+"]"+(sPartNo.equalsIgnoreCase(sResultPartID)));

			if(hmItemIDVsItemRevObj.containsKey(sPartNo)) {
				gdItems[j].setText(iCol_TC_ObjStatus, "Available");
				//if(gdItems[j].getText(iCol_Level).isEmpty())// added due to multi ID issue
				gdItems[j].setText(iCol_Level, hmItemIDVsItemRevObj.get(sPartNo));
			}
		}//for j
		//}//for i

		for (int j = 0; j < gdItems.length; j++) {//for j
			if(gdItems[j].getText(iColPartNumber).equalsIgnoreCase("Not Available")) {
				sPartAvailError = sPartAvailError + ("INFO Excel Row Number : "+(j+2)+" - Item ("+gdItems[j].getText(iColPartNumber)+") is not available in Teamcenter\n");
			}
		}	

		writePartAvailInLog(gdTable, sLogFilePath);
		return sPartAvailError;
	}//updatePartAvailability


	public String updatePartAvailability_OLD(Grid gdTable, String sLogFilePath) {

		String sPartAvailError = "";

		GridItem[] gdItems = gdTable.getItems();

		List partList = new ArrayList<>();


		for (int i = 0; i < gdItems.length; i++) {
			String sPartNo = gdItems[i].getText(iColPartNumber);
			String sItemType = "";

			if(sPartNo==null || sPartNo.trim().isEmpty()) {
				gdItems[i].setText(iCol_TC_ObjStatus, "");
			}
			else {

				partList.add(sPartNo);

				/*ModelObject[] moParts = query.queryItemsUsingID(sPartNo, "*");

				if(moParts!=null && moParts.length>0){
					gdItems[i].setText(iCol_TC_ObjStatus, "Available");
					String sItemRevAttr[] = {"object_type"};

					dmService.getProperties(moParts, sItemRevAttr);
					try {
						sItemType = moParts[0].getPropertyObject(sItemRevAttr[0]).getStringValue();
					} catch (NotLoadedException e) {
						e.printStackTrace();
					}
				}
				else{
					sPartAvailError = sPartAvailError + ("INFO Excel Row Number : "+(i+2)+" - Item ("+gdItems[i].getText(iColPartNumber)+") is not available in Teamcenter\n");
					gdItems[i].setText(iCol_TC_ObjStatus, "Not Available");
				}
				 */
			}
			gdItems[i].setText(iCol_Level, sItemType);
		}

		try {
			/*ModelObject[] childPartsMO; = query.queryAllChildItems(partList);

			dmService.getProperties(childPartsMO, new String[] {"item_id"});

			for (int i = 0; i < childPartsMO.length; i++) {
				String sResultPartID = childPartsMO[i].getPropertyDisplayableValue("item_id");
				for (int j = 0; j < gdItems.length; j++) {
					String sPartNo = gdItems[i].getText(iColPartNumber);

					if(sPartNo==null || sPartNo.trim().isEmpty()) {
						continue;
					}


				}
			}*/


		} catch (Exception e) {
			e.printStackTrace();
		}
		writePartAvailInLog(gdTable, sLogFilePath);
		return sPartAvailError;
	}

	public void writePartAvailInLog(Grid gdTable, String sLogFilePath){
		iCntPartAvail = 0;
		iCntPartNotAvail = 0;
		iCntPartIDEmpty = 0;

		if(!sDryRunLogFilePath.equalsIgnoreCase("")) {
			alPartsExist = new ArrayList<>();
			alPartsNotExist = new ArrayList<>();
		}
		GridItem[] gdItems = gdTable.getItems();
		for (int i = 0; i < gdItems.length; i++) {
			String sPartStatus = gdItems[i].getText(iCol_TC_ObjStatus);

			if(sPartStatus!=null) {
				if(sPartStatus.trim().isEmpty())
					iCntPartIDEmpty++;
				else if(sPartStatus.trim().equalsIgnoreCase("Available")) {
					iCntPartAvail++;
					if(!alPartsNewlyCreated.contains(gdItems[i].getText(iColPartNumber)))
						alPartsExist.add(gdItems[i].getText(iColPartNumber));
				}
				else if(sPartStatus.trim().equalsIgnoreCase("Not Available")) {
					iCntPartNotAvail++;
					alPartsNotExist.add(gdItems[i].getText(iColPartNumber));
				}
			}
		}
	}

	public void truncateAsembly(ItemRevision itemRev, String sLogFile) {
		try {

			openBOMWindow(itemRev);
			BOMLine top_bomline = (BOMLine)bomWindowObj.get_top_line();

			ModelObject[] childbomlines;
			dmService.getProperties(new ModelObject[] {top_bomline}, new String[] {"bl_child_lines"});
			childbomlines = top_bomline.get_bl_child_lines();

			writeLogFile(sLogFile, " ----- REMOVING EXISTING BOM LINE START --------");
			com.teamcenter.services.strong.bom.StructureManagementService  smBomService = null;
			smBomService = com.teamcenter.services.strong.bom.StructureManagementService.getService( AppXSession.getConnection() );

			BOMLine[] childBomLine = new BOMLine[childbomlines.length];
			for (int i = 0; i < childbomlines.length; i++) {
				childBomLine[i] = (BOMLine)childbomlines[i];
			}

			RemoveChildrenFromParentLineResponse response = smBomService.removeChildrenFromParentLine(childBomLine);

			if (response.serviceData.sizeOfPartialErrors() > 0)
			{            
				displayErrors(response.serviceData, false);            

				throw new Exception( "removeExistingBomLines removeChildrenFromParentLine returned an error.");
			}
			// save bom window
			BOMWindow[] bomWindow = new BOMWindow[1];
			bomWindow[0] = bomWindowObj;

			cadSMService.saveBOMWindows(bomWindow);
			cadSMService.closeBOMWindows(bomWindow);
			writeLogFile(sLogFile, " ----- REMOVING EXISTING BOM LINE END --------");

			printBomLines(top_bomline,sLogFile);

		} catch (Exception e) {

			// save bom window
			BOMWindow[] bomWindow = new BOMWindow[1];
			bomWindow[0] = bomWindowObj;

			cadSMService.saveBOMWindows(bomWindow);
			cadSMService.closeBOMWindows(bomWindow);
			writeLogFile(sLogFile, " ----- REMOVING EXISTING BOM LINE END --------");

			e.printStackTrace();
		}
	}

	public void readBomdetails(ItemRevision itemRev, String sLogFile) {
		try {

			hmRowVsItemIDFindNo = new HashMap<>();
			iBomLineNo = 0;

			openBOMWindow(itemRev);
			BOMLine top_bomline = (BOMLine)bomWindowObj.get_top_line();
			writeLogFile(sLogFile, "Value in BOM_LINE_PROP is "+BOM_LINE_PROP);	
			System.out.println("Value in BOM_LINE_PROP is "+BOM_LINE_PROP);
			dmService.getProperties(new ModelObject[] { top_bomline }, new String[]{"bl_ref_designator","bl_item_item_id","bl_child_lines", "bl_item_object_name","bl_sequence_no","bl_line_name"});
			//System.out.println("Model No : "+bomline.getPropertyDisplayableValue("bl_item_item_id"));

			String sBomID = top_bomline.getPropertyDisplayableValue("bl_item_item_id");
			String sFindNo = top_bomline.getPropertyDisplayableValue("bl_sequence_no");
			String srefDes = top_bomline.getPropertyDisplayableValue("bl_ref_designator");

			writeLogFile(sLogFile, "  "+(iBomLineNo++)+"  --> BomLine Name is "+sBomID+" \tFind no is "+ sFindNo+"\tRef Des is "+srefDes);

			printBomLines(top_bomline,sLogFile);

		} catch (Exception e) {
			e.printStackTrace();
		}

	}

	public void printBomLines(BOMLine bomline, String sLogFile) {
		try {
			ModelObject[] childbomlines;
			childbomlines = bomline.get_bl_child_lines();
			dmService.getProperties( childbomlines , new String[]{"bl_ref_designator","bl_item_item_id","bl_child_lines", 
					"bl_item_object_name","bl_sequence_no","bl_line_name","bl_has_substitutes","bl_substitute_list"});

			for (int i = 0;  i<childbomlines.length; i++) {
				BOMLine blinechild = (BOMLine) childbomlines[i];				
				String sBomID = blinechild.getPropertyDisplayableValue("bl_item_item_id");
				String sFindNo = blinechild.getPropertyDisplayableValue("bl_sequence_no");
				String srefDes = blinechild.getPropertyDisplayableValue("bl_ref_designator");

				boolean sHasSubstitute = blinechild.get_bl_has_substitutes();
				String sSubtituteList = blinechild.getPropertyDisplayableValue("bl_substitute_list");

				//String sHasSubsitute = sHasSubstitute?"True":"False";

				writeLogFile(sLogFile, "  "+(iBomLineNo)+"  --> BomLine Name is "+sBomID+" \tFind no is "+ sFindNo+"\tRef Des is "+srefDes);

				if(sHasSubstitute) {					
					writeLogFile(sLogFile, "  "+(iBomLineNo)+"  --> (WARNING - SUBSTITUTE PRESENT) BomLine Name is "+sBomID+" \tSubstitute is[ "+ sSubtituteList+" ]");
				}
				iBomLineNo++;

				hmRowVsItemIDFindNo.put(iBomLineNo, sBomID+sFindNo+srefDes);

				if(!isBomLineExistinTable(sBomID+sFindNo+srefDes)) {
					alBomLineNotInTbl.add(blinechild);
					alBomLineIDNotInTbl.add(sBomID+sFindNo+srefDes);
				}


			}
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	public Integer isBomLineExist(String sBomIDFindNo) {
		for (Integer sRow : hmRowVsItemIDFindNo.keySet()) {
			if(sBomIDFindNo.equalsIgnoreCase(hmRowVsItemIDFindNo.get(sRow)))
				return sRow;
		}
		return -1;
	}

	public boolean isBomLineExistinTable(String sBomIDFindNo) {
		for (Integer sRow : hmTblRowVsItemIDFindNo.keySet()) {
			if(sBomIDFindNo.equalsIgnoreCase(hmTblRowVsItemIDFindNo.get(sRow)))
				return true;
		}
		return false;
	}

	public static void openBOMWindow(ItemRevision parentItemRev) {
		//ArrayList bomWindowandParentLine = new ArrayList(2);
		CreateBOMWindowsInfo[] createBOMWindowsInfo = new CreateBOMWindowsInfo[1];
		createBOMWindowsInfo[0] = new CreateBOMWindowsInfo();
		createBOMWindowsInfo[0].itemRev = (ItemRevision) parentItemRev;
		cadSMService = StructureManagementService.getService(AppXSession.getConnection());

		CreateBOMWindowsResponse createBOMWindowsResponse = cadSMService.createBOMWindows(createBOMWindowsInfo);

		if (createBOMWindowsResponse.serviceData.sizeOfPartialErrors() > 0) {
			for (int i = 0; i < createBOMWindowsResponse.serviceData.sizeOfPartialErrors(); i++) {
				System.out.println("Partial Error in Open BOMWindow = "
						+ createBOMWindowsResponse.serviceData.getPartialError(i).getMessages()[0]);
			}
		}
		//bomWindowandParentLine.add(createBOMWindowsResponse.output[0].bomLine);// TOPLine
		bomWindowObj = createBOMWindowsResponse.output[0].bomWindow;
	}

	public void removeExistingBomLines(BOMWindow bomWindowObj,ArrayList alBomLineNotInTbl, String sLogFile, boolean isDryRun) throws Exception {

		if(alBomLineNotInTbl.size()>0) {
			writeLogFile(sLogFile, " ----- REMOVING EXISTING BOM LINE START --------");
			com.teamcenter.services.strong.bom.StructureManagementService  smBomService = null;
			smBomService = com.teamcenter.services.strong.bom.StructureManagementService.getService( AppXSession.getConnection() );

			BOMLine[] childBomLine = new BOMLine[alBomLineNotInTbl.size()];
			for (int i = 0; i < alBomLineNotInTbl.size(); i++) {
				childBomLine[i] = (BOMLine)alBomLineNotInTbl.get(i);
				String sBomID = childBomLine[i].getPropertyDisplayableValue("bl_item_item_id");
				String sFindNo = childBomLine[i].getPropertyDisplayableValue("bl_sequence_no");
				String srefDes = childBomLine[i].getPropertyDisplayableValue("bl_ref_designator");
				writeLogFile(sLogFile, " --> BomLine Name is "+sBomID+" \tFind no is "+ sFindNo+"\tRef Des is "+srefDes);

				if(!isDryRun) {
					RemoveChildrenFromParentLineResponse response = smBomService.removeChildrenFromParentLine(new BOMLine[] {childBomLine[i]});

					if (response.serviceData.sizeOfPartialErrors() > 0)
					{            
						displayErrors(response.serviceData);            

						throw new Exception( "removeExistingBomLines removeChildrenFromParentLine returned an error.");
					}
				}

			}
			// save bom window
			BOMWindow[] bomWindow = new BOMWindow[1];
			bomWindow[0] = bomWindowObj;

			cadSMService.saveBOMWindows(bomWindow);
			cadSMService.closeBOMWindows(bomWindow);
			writeLogFile(sLogFile, " ----- REMOVING EXISTING BOM LINE END --------");
		}

	}

	/*public void removeBomLines(Item parent) throws Exception {
        DataManagementService dmService = null;
        com.teamcenter.services.strong.cad.StructureManagementService smService = null;                
        com.teamcenter.services.strong.bom.StructureManagementService  smBomService = null;

        dmService = DataManagementService.getService(AppXSession.getConnection());        
        smService = com.teamcenter.services.strong.cad.StructureManagementService.getService( AppXSession.getConnection() );
        smBomService = com.teamcenter.services.strong.bom.StructureManagementService.getService( AppXSession.getConnection() );

        String[] props = { "item_revision", "item_revision_id", "revision_list"};
        ServiceData serviceData = dmService.getProperties( new ModelObject[]{parent }, props );

        if (serviceData.sizeOfPartialErrors() > 0)
        {
            displayErrors(serviceData);            

            throw new Exception( "attachChild getProperties returned an error.");
        }

        ItemRevision parentRev = null;
        //ModelObject childRev = null;

        ModelObject[] revList = parent.get_revision_list();        
        if (revList.length > 0)
        {
            parentRev = (ItemRevision) revList[revList.length-1];
        }

        /*revList = child.get_revision_list();
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

        dmService.getProperties(new ModelObject[] {topLine}, new String[] {"bl_child_lines"});

        ModelObject[] childLines = topLine.get_bl_child_lines();

        BOMLine childBomLine[] = new BOMLine[childLines.length];

        if(childLines.length>0) {
        	for (int i = 0; i < childLines.length; i++) {
        		childBomLine[i] = (BOMLine)childLines[i];
			}
        }
        else{
        	return;
        }

        RemoveChildrenFromParentLineResponse response = smBomService.removeChildrenFromParentLine(childBomLine);

        if (response.serviceData.sizeOfPartialErrors() > 0)
        {            
            displayErrors(response.serviceData);            

            throw new Exception( "attachChild addOrUpdateChildrenToParentLine returned an error.");
        }

        // save bom window
        BOMWindow[] bomWindow = new BOMWindow[1];
        bomWindow[0] = createBOMWindowsResponse.output[0].bomWindow;

        smService.saveBOMWindows(bomWindow); 
        smService.closeBOMWindows(bomWindow);
	}*/

	public int getMessageBox(Shell shell, int Args, String sText, String sMessage) {
		MessageBox messageBox = new MessageBox(shell, Args);
		messageBox.setText(sText);
		messageBox.setMessage(sMessage);
		int buttonID = messageBox.open();
		return buttonID;
	}

	public LinkedHashMap<String, String> getRealNameVsValue(GridItem gdItem) {
		LinkedHashMap<String, String> hmRealNameVsValue = new LinkedHashMap<>();

		GridColumn[] gdCols = gridImportTable.getColumns();
		String PartNumber = null  ;
		for (int j = 0; j < gdCols.length; j++) {
			String sRealName = (String) prop.get("REAL_"+gridImportTable.getColumn(j).getText());
			if(gridImportTable.getColumn(j).getText().equals("PART"))
				PartNumber = gdItem.getText(j);
			if(sRealName!=null && (!sRealName.equalsIgnoreCase("item_id"))) {
				hmRealNameVsValue.put(sRealName, gdItem.getText(j));
			}
		}	

		/*if(gdItem.getData("QUANTITY_CALCULATED")!=null) {
			try {
				String sQuantity = (String)gdItem.getData("QUANTITY_CALCULATED");
				int iQuantity = Integer.parseInt(sQuantity.trim());
				hmRealNameVsValue.put("bl_quantity", iQuantity+"");
			} catch (Exception e) {
				e.printStackTrace();
				hmRealNameVsValue.put("bl_quantity", "1");
			}
		}else {
			hmRealNameVsValue.put("bl_quantity", "1");
		}*/
		if(!hmRealNameVsValue.containsKey("bl_quantity")) {
			hmRealNameVsValue.put("bl_quantity", "1");
		}
		//if(gdItem.getData("bl_occ_ad4OccRefDesignators")!=null && PartNumber!=null) {
		if(PartNumber!=null) {
			String sRefDesValue  =hmStoreItemisVsRefDes.get(PartNumber);
			String[] sArrRefDes = sRefDesValue.split(",");
			Arrays.sort(sArrRefDes);

			StringBuilder strBuilder = new StringBuilder();

			for (int ii = 0; ii < sArrRefDes.length; ii++) {
				strBuilder.append(sArrRefDes[ii]);
				if (ii < sArrRefDes.length - 1) {
					strBuilder.append(",");
				}
			}
			hmRealNameVsValue.put("bl_occ_ad4OccRefDesignators",strBuilder.toString());
			//hmRealNameVsValue.put("bl_occ_ad4OccRefDesignators",gdItem.getData("bl_occ_ad4OccRefDesignators").toString());
		}

		return hmRealNameVsValue;
	}

	public ModelObject[] createItem(String sClientID, String sEBOM_Id, String sRevId, String sName, String sType,
			String sDesc, String sUom) {
		ModelObject[] modelObject = null;
		try {
			ItemProperties itemProperty = new ItemProperties();
			itemProperty.clientId 	= sClientID;
			itemProperty.itemId 	= sEBOM_Id;
			itemProperty.revId 		= sRevId;
			itemProperty.name 		= sName;
			itemProperty.type 		= sType;
			itemProperty.description = sDesc;
			itemProperty.uom 		= sUom;

			CreateItemsResponse response = dmService.createItems(new ItemProperties[] {itemProperty}, null, "");
			if (response.serviceData.sizeOfPartialErrors() > 0) {
				System.err.println("Note: Error caught while creating Objects in TC.\nKindly check once.");
				displayErrors(response.serviceData);
				System.out.println(displayErrors(response.serviceData));
				writeLogFile(sExecuteLogFilePath,
						"Warning!.. Following Error Caught while trying to Create Objects [Item ID : " + sEBOM_Id
						+ " Rev ID : " + sRevId + "]\n " + displayErrors(response.serviceData));
			}

			modelObject = new ModelObject[response.serviceData.sizeOfCreatedObjects()];
			if(response.serviceData.sizeOfCreatedObjects()>0) {
				for (int i = 0; i < response.serviceData.sizeOfCreatedObjects(); i++) {
					modelObject[i] = response.serviceData.getCreatedObject(i);
				}
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		return modelObject;
	}

	private boolean attachChildInBulk(ItemRevision parent, ItemLineInfo[] itemLineInfos,StringBuilder sbReportTemplate) throws Exception
	{
		boolean bExecutedSuccess = true;
		DataManagementService dmService = null;
		com.teamcenter.services.strong.cad.StructureManagementService smService = null;                
		com.teamcenter.services.strong.bom.StructureManagementService  smBomService = null;

		dmService = DataManagementService.getService(AppXSession.getConnection());        
		smService = com.teamcenter.services.strong.cad.StructureManagementService.getService( AppXSession.getConnection() );
		smBomService = com.teamcenter.services.strong.bom.StructureManagementService.getService( AppXSession.getConnection() );

		// create BOMWindow
		CreateBOMWindowsInfo[] createBOMWindowsInfo = new CreateBOMWindowsInfo[1];
		createBOMWindowsInfo[0] = new CreateBOMWindowsInfo();        
		createBOMWindowsInfo[0].itemRev = parent;

		CreateBOMWindowsResponse createBOMWindowsResponse =
				smService.createBOMWindows(createBOMWindowsInfo);

		// get the topline
		BOMLine topLine = createBOMWindowsResponse.output[0].bomLine;

		// add child to parent                
		AddOrUpdateChildrenToParentLineInfo[] addChildInfo = new AddOrUpdateChildrenToParentLineInfo[1];
		addChildInfo[0] = new AddOrUpdateChildrenToParentLineInfo();            
		addChildInfo[0].items = itemLineInfos;
		addChildInfo[0].parentLine = topLine;
		addChildInfo[0].viewType = "view";

		AddOrUpdateChildrenToParentLineResponse saveBomWinResp = smBomService.addOrUpdateChildrenToParentLine(addChildInfo);
		
		int loadedCount =0;
		BOMLinesOutput[]  linesOutputs=saveBomWinResp.itemLines;
		
		loadedCount = linesOutputs.length;
		sbReportTemplate.append( "--------------------------------------------------------------------\n");
		sbReportTemplate.append("Loaded TC Count: "+(loadedCount)+"\n");
		sbReportTemplate.append("--------------------------------------------------------------------\n");
		for(int i=0;i<loadedCount; i++) {
			
			BOMLine  bomLine = linesOutputs[i].bomline;
			dmService.getProperties(new ModelObject[]{bomLine}, new String[]{"bl_item_item_id","bl_item_object_type"}); 
			try { 
				String item_id =(bomLine).getPropertyDisplayableValue("bl_item_item_id");
				String obj_type =(bomLine).getPropertyDisplayableValue("bl_item_object_type");
				sbReportTemplate.append((i+1)+") "+item_id+"- "+obj_type+"\n"); 
			}
			catch (NotLoadedException e1) { // TODO Auto-generated catch block
				e1.printStackTrace(); }
		}
		if (saveBomWinResp.serviceData.sizeOfPartialErrors() > 0)
		{            
			bExecutedSuccess = false;
			int unloadedCount = saveBomWinResp.serviceData.sizeOfPartialErrors();
			String errorMsg = displayErrors(saveBomWinResp.serviceData);
			sbReportTemplate.append( "--------------------------------------------------------------------\n");
			sbReportTemplate.append("UnLoaded TC Count: "+(unloadedCount)+"\n");
			sbReportTemplate.append("--------------------------------------------------------------------\n");
			
			for(int errIndex =0;errIndex<unloadedCount;errIndex++) {
				ModelObject  bomLine = saveBomWinResp.serviceData.getPartialError(errIndex).getAssociatedObject();
				dmService.getProperties(new ModelObject[]{bomLine}, new String[]{"item_id","object_type"}); 
				try { 
					String item_id =(bomLine).getPropertyDisplayableValue("item_id");
					String obj_type =(bomLine).getPropertyDisplayableValue("object_type");
					sbReportTemplate.append((errIndex+1)+") "+item_id+"- "+obj_type+"\n"); 
				}
				catch (NotLoadedException e1) { // TODO Auto-generated catch block
					e1.printStackTrace(); }
			}
			
			sbReportTemplate.append("--------------------------------------------------------------------\n");
			sbReportTemplate.append("Error Info:\n");
			sbReportTemplate.append(errorMsg);
			sbReportTemplate.append("\n--------------------------------------------------------------------\n");
			System.out.println( "attachChild addOrUpdateChildrenToParentLine returned an error.");
			//throw new Exception();
		}

		// save bom window
		BOMWindow[] bomWindow = new BOMWindow[1];
		bomWindow[0] = createBOMWindowsResponse.output[0].bomWindow;

		smService.saveBOMWindows(bomWindow);  
		smService.closeBOMWindows(bomWindow);  
		return bExecutedSuccess;
	}

	private boolean attachChild(Item parent, Item child, LinkedHashMap<String, String> hmUpdateAttrs) throws Exception
	{
		boolean bExecutedSuccess = true;
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
			parentRev = (ItemRevision) revList[revList.length-1];
		}

		revList = child.get_revision_list();
		if (revList.length > 0)
		{
			childRev = revList[revList.length-1];
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
		if(hmUpdateAttrs != null && hmUpdateAttrs.size() > 0)
		{
			itemLineInfos[0].itemLineProperties = hmUpdateAttrs;//This is map of properties and their values
		}
		// itemLineInfos[0].occType = "DC_ComposableReferenceR";

		AddOrUpdateChildrenToParentLineInfo[] addChildInfo = new AddOrUpdateChildrenToParentLineInfo[1];
		addChildInfo[0] = new AddOrUpdateChildrenToParentLineInfo();            
		addChildInfo[0].items = itemLineInfos;
		addChildInfo[0].parentLine = topLine;
		addChildInfo[0].viewType = "view";

		AddOrUpdateChildrenToParentLineResponse saveBomWinResp = smBomService.addOrUpdateChildrenToParentLine(addChildInfo);

		if (saveBomWinResp.serviceData.sizeOfPartialErrors() > 0)
		{            
			bExecutedSuccess = false;
			displayErrors(serviceData);            

			throw new Exception( "attachChild addOrUpdateChildrenToParentLine returned an error.");
		}

		// save bom window
		BOMWindow[] bomWindow = new BOMWindow[1];
		bomWindow[0] = createBOMWindowsResponse.output[0].bomWindow;

		smService.saveBOMWindows(bomWindow); 
		smService.closeBOMWindows(bomWindow);
		return bExecutedSuccess;
	}

	// displays the service data errors
	private String displayErrors(ServiceData serviceData)
	{   
		return displayErrors(serviceData, true);
	}

	private String displayErrors(ServiceData serviceData, boolean bWriteInLog)
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
		if(bWriteInLog) {
			writeLogFile(sExecuteLogFilePath, errorMessage);
		}

		return errorMessage;
	}

	public void readExcelFile(Grid gridImportTable){
		btnDryRun.setEnabled(false);
		writeLogFile(sInputLogFilePath, "Read Input file Called...");

		FileDialog fileDialog = new FileDialog(gridImportTable.getShell(), SWT.MULTI);

		String firstFile = fileDialog.open();
		String sFilePath = "";

		if (firstFile != null) {
			System.out.println("File Path is " + fileDialog.getFilterPath());
			String[] selectedFiles = fileDialog.getFileNames();
			if (selectedFiles == null || (selectedFiles.length > 1)) {
				return;
			} else {
				sFilePath = fileDialog.getFilterPath() + "\\" + selectedFiles[0];



				File file = new File(sFilePath);

				if (file.exists()) {

					writeLogFile(sInputLogFilePath, "File Exist. Path is "+sFilePath);

					String name = file.getName();
					shlBomUtility.setText("Legacy BOM Import Utility - Processing File...["+name + "]"); 
					String sFileext = name.substring(name.lastIndexOf(".") + 1);

					if(sFileext.equals("xlsx")) {
						writeLogFile(sInputLogFilePath, "File type is "+sFileext);
						readfile_xlsx(new File(sFilePath), gridImportTable);
					}
					else if(sFileext.equals("xls")) {
						writeLogFile(sInputLogFilePath, "File type is "+sFileext);
						readfile_xls(new File(sFilePath), gridImportTable);
					}else if(sFileext.equals("csv")) {
						writeLogFile(sInputLogFilePath, "File type is "+sFileext);
						readfile_csv(new File(sFilePath), gridImportTable);
					}else{
						writeLogFile(sInputLogFilePath, "Invalid file type is selected. File type is "+sFileext);
					}
				}else{
					writeLogFile(sInputLogFilePath, "File not exist. Path is "+sFilePath);
				}
			}
		}
	}

	public void readfile_xls(File eBOMfilepath, Grid gridImportTable){
		FileInputStream fis = null;
		try {
			int iExcelRows = -1;
			HashMap<String, Integer> hmTblNameVsTblCol = new HashMap<>();
			HashMap<Integer, Integer> hmExlColVsTblCol = new HashMap<>();
			ArrayList<String> alExcelHeaders = new ArrayList<>();

			int iFindNoNotAvailableAll= 0;
			int iFindNoAvailableAll= 0;
			int iFindNoNotAvailableFew= 0;
			boolean bVariantCondition= true;
			boolean enableDryRun = true;

			GridColumn[] gdCols = gridImportTable.getColumns();
			for (int i = 0; i < gdCols.length; i++) {
				hmTblNameVsTblCol.put(gdCols[i].getText(), i);
			}

			fis = new FileInputStream(eBOMfilepath.getAbsolutePath());
			@SuppressWarnings("resource")
			HSSFWorkbook workbook = new HSSFWorkbook(fis);
			HSSFSheet sheet = workbook.getSheetAt(0);
			Iterator<Row> rowIterator = sheet.iterator();
			while (rowIterator.hasNext())
			{
				if(iExcelRows == -1) {//To Skip Row1 in Excel File
					iExcelRows++;
					Row row = rowIterator.next();
					Iterator<Cell> cellIterator = row.cellIterator();

					while (cellIterator.hasNext())
					{
						Cell cell = cellIterator.next();
						int iColIdx = cell.getColumnIndex();
						if(iColIdx  == 2) {
							String strVariant = cell.getStringCellValue();
							/*if(strVariant != null && !strVariant.isEmpty()) {
                        		if(strVariant.equalsIgnoreCase("y")) {
                        			strToplineItemType = prop.getProperty("VARIANT_TOP_LINE_ITEM_TYPE");
                        			break;
                        		} else if(strVariant.equalsIgnoreCase("n")) {
                        			strToplineItemType = prop.getProperty("NON_VARIANT_TOP_LINE_ITEM_TYPE");
                        			break;
                        		} else {
                        			//error in log - not enable dryrun
                        			bVariantCondition= false;
                        			break;
                        		}
                        	} else {
                        		//error in log - not enable dryrun
                        		bVariantCondition= false;
                        		break;
                        	}*/
						}
					}
					continue;
				}
				String sInputFileError = "";
				if(iExcelRows ==0) {
					sInputFileError = "";
				}
				GridItem gridItem = null;
				if(iExcelRows !=0) {
					gridItem = new GridItem(gridImportTable, SWT.NONE);
				}

				Row row = rowIterator.next();
				Iterator<Cell> cellIterator = row.cellIterator();
				while (cellIterator.hasNext())
				{
					Cell cell = cellIterator.next();
					FormulaEvaluator formulaEvaluator = workbook.getCreationHelper().createFormulaEvaluator();

					int iCellIndex = cell.getColumnIndex();

					if(iExcelRows ==0) {

						switch (formulaEvaluator.evaluateInCell(cell).getCellTypeEnum())
						{
						case STRING:
						{
							if(cell.getStringCellValue()!=null && !cell.getStringCellValue().isEmpty()) {
								alExcelHeaders.add(cell.getStringCellValue().trim());
								if(!hmContainsKey(hmTblNameVsTblCol, cell.getStringCellValue().trim()).isEmpty()) {
									hmExlColVsTblCol.put(iCellIndex, hmTblNameVsTblCol.get(hmContainsKey(hmTblNameVsTblCol, cell.getStringCellValue().trim())));
								}else {
									sInputFileError = sInputFileError + "Error : Following Excel Column Name is not available in Config file ["+cell.getStringCellValue().trim()+"]";
								}
							}
						}
						break;

						default:
							break;
						}
					}else if(hmExlColVsTblCol.containsKey(iCellIndex)){
						switch (formulaEvaluator.evaluateInCell(cell).getCellTypeEnum())
						{
						case NUMERIC:
							gridItem.setText(hmExlColVsTblCol.get(iCellIndex), ""+cell.getNumericCellValue());
							break;

						case STRING:
							gridItem.setText(hmExlColVsTblCol.get(iCellIndex), cell.getStringCellValue());
							break;

						case BLANK:
							gridItem.setText(hmExlColVsTblCol.get(iCellIndex), "");
							break;	

						default:
							break;
						}
					}
				}

				boolean isBOMCellValueAvailable = false;
				boolean isPARTCellValueAvailable = false;

				if(iExcelRows==1) {
					if(!sInputFileError.trim().isEmpty()) {
						writeLogFile(sInputLogFilePath, sInputFileError);
					}
					if(!getMinExcelHeaderError(hmTblNameVsTblCol, alExcelHeaders).isEmpty()) {
						return;
					}
				}else if(iExcelRows!=-1) {
					Integer iFindNoTblCol = hmTblNameVsTblCol.get("FIND");
					Integer iBOMTblCol = hmTblNameVsTblCol.get("BOM");
					Integer iPartTblCol = hmTblNameVsTblCol.get("PART");
					Integer iRefDesTblCol = hmTblNameVsTblCol.get("REF DES");
					if(gridItem!=null) {

						String sRefDesValue = gridItem.getText(iRefDesTblCol);
						if(sRefDesValue!=null && !(sRefDesValue.trim().isEmpty())) {
							String[] sArrRefDes = sRefDesValue.split(",");
							
							Arrays.sort(sArrRefDes);

							StringBuilder strBuilder = new StringBuilder();

							for (int ii = 0; ii < sArrRefDes.length; ii++) {
								strBuilder.append(sArrRefDes[ii]); 
								if (ii < sArrRefDes.length - 1) {
									strBuilder.append(","); 
								} 
							}
							 
							for (int iRefDesCnt = 0; iRefDesCnt < sArrRefDes.length; iRefDesCnt++) {
								if(iRefDesCnt==0) {
									gridItem.setText(iRefDesTblCol, sArrRefDes[iRefDesCnt]);
									StoreRefdesInHashMap(gridItem.getText(iPartTblCol),sArrRefDes[iRefDesCnt] );
									
									//gridItem.setData("bl_occ_ad4OccRefDesignators",strBuilder.toString() );
								}else {
									GridItem newgridItem = new GridItem(gridImportTable, SWT.NONE);
									newgridItem.setText(iFindNoTblCol, gridItem.getText(iFindNoTblCol));
									newgridItem.setText(iBOMTblCol, gridItem.getText(iBOMTblCol));
									newgridItem.setText(iPartTblCol, gridItem.getText(iPartTblCol));
									newgridItem.setText(iRefDesTblCol, sArrRefDes[iRefDesCnt]);
									StoreRefdesInHashMap(gridItem.getText(iPartTblCol),sArrRefDes[iRefDesCnt] );
									//newgridItem.setData("bl_occ_ad4OccRefDesignators",strBuilder.toString() );

								}
							}
						}
						String sFindValue = gridItem.getText(iFindNoTblCol);
						if(sFindValue.trim().isEmpty()) {
							if(iFindNoNotAvailableFew==0 && iFindNoAvailableAll==0) {
								iFindNoNotAvailableAll++;
							}else if(iFindNoAvailableAll>0) {
								iFindNoNotAvailableFew++;
								//TODO: Write error Message.
							}
						}else{
							int iCurrentFindNo = Integer.parseInt(sFindValue.trim());
							if(iFindNoNotAvailableAll==0 && iFindNoNotAvailableFew==0)
								iFindNoAvailableAll++;
						}

						String sBOMCell = gridItem.getText(iBOMTblCol);
						String sPARTCell = gridItem.getText(iPartTblCol);
						if(sBOMCell != null && sBOMCell.trim().length() >0) {
							isBOMCellValueAvailable = true;
						}
						if(sPARTCell != null && sPARTCell.trim().length() >0) {
							isPARTCellValueAvailable = true;
						}
					}
					if((!isBOMCellValueAvailable || !isPARTCellValueAvailable)) {
						writeLogFile(sInputLogFilePath, ":::::::::Either BOM or PART column cell value is empty at Row : "+ (iExcelRows+2));
						enableDryRun = false;
						//break;
					}
					if(!isBOMCellValueAvailable) {
						writeLogFile(sInputLogFilePath, ":::::::::Found Empty cell for First Column Value at Row : "+ (iExcelRows+2));
						writeLogFile(sInputLogFilePath, "::::::::: Excel Rows after this Row : " + (iExcelRows+2) + " will be ignored...!");
						iFindNoNotAvailableAll--;
						iFindNoNotAvailableFew--;
						enableDryRun = true;
						gridImportTable.remove(gridImportTable.getItemCount()-1);
						break;
					}
				}
				iExcelRows++;
			}

			//writeLogFile(sInputLogFilePath, "Total columns found in excel "+alExcelHeaders.size());
			writeLogFile(sInputLogFilePath, "Total rows found in excel "+iExcelRows);

			if(iFindNoNotAvailableAll==0 && iFindNoNotAvailableFew==0 && iFindNoAvailableAll>0)
				bGenerateFindNo = false;
			else
				bGenerateFindNo = true;
			if(ValidateVariantOrFindNo(bVariantCondition, iFindNoNotAvailableFew)) {
				btnDryRun.setEnabled(false);
			}
			else {
				btnDryRun.setEnabled(enableDryRun);
			}

			fis.close();
		} catch (Exception e){
			System.out.println("Exception eBOM file reading "+e);
			e.printStackTrace();

			try {
				fis.close();
			} catch (IOException e1) {
				e1.printStackTrace();
			}
		}
		return;
	}

	public void readfile_xlsx_New_Format(File eBOMfilepath, Grid gridImportTable){

		FileInputStream fis = null;
		try {
			int iExcelRows = -1;

			HashMap<String, Integer> hmTblNameVsTblCol = new HashMap<>();
			HashMap<Integer, Integer> hmExlColVsTblCol = new HashMap<>();
			ArrayList<String> alExcelHeaders = new ArrayList<>();

			int iFindNoNotAvailableAll= 0;
			int iFindNoAvailableAll= 0;
			int iFindNoNotAvailableFew= 0;
			boolean bVariantCondition= true;
			boolean enableDryRun = true;

			GridColumn[] gdCols = gridImportTable.getColumns();

			for (int i = 0; i < gdCols.length; i++) {
				hmTblNameVsTblCol.put(gdCols[i].getText(), i);
			}

			fis = new FileInputStream(eBOMfilepath.getAbsolutePath());
			@SuppressWarnings("resource")
			XSSFWorkbook book = new XSSFWorkbook(fis);
			XSSFSheet sheet = book.getSheetAt(0);
			Iterator<Row> itr = sheet.iterator();
			// Iterating over Excel file in Java
			while (itr.hasNext()) {

				if(iExcelRows == -1) {}
				String sInputFileError = "";
				if(iExcelRows ==0) {
					sInputFileError = "";
				}

				GridItem gridItem = null;

				if(iExcelRows !=-1) {
					gridItem = new GridItem(gridImportTable, SWT.NONE);
				}

				Row row = itr.next();
				// Iterating over each column of Excel file
				Iterator<Cell> cellIterator = row.cellIterator();


				while (cellIterator.hasNext()) {

					Cell cell = cellIterator.next();

					FormulaEvaluator formulaEvaluator = book.getCreationHelper().createFormulaEvaluator();

					int iCellIndex = cell.getColumnIndex();
					if(iExcelRows ==-1) {
						//String sInputFileError = "";
						switch (formulaEvaluator.evaluateInCell(cell).getCellTypeEnum())
						{
						case STRING:
						{
							if(cell.getStringCellValue()!=null && !cell.getStringCellValue().isEmpty()) {
								alExcelHeaders.add(cell.getStringCellValue().trim());
								if(!hmContainsKey(hmTblNameVsTblCol, cell.getStringCellValue().trim()).isEmpty()) {
									hmExlColVsTblCol.put(iCellIndex, hmTblNameVsTblCol.get(hmContainsKey(hmTblNameVsTblCol, cell.getStringCellValue().trim())));
								}else {
									sInputFileError = sInputFileError + "Error : Following Excel Column Name is not available in Config file ["+cell.getStringCellValue().trim()+"]";
								}
							}
						}
						break;

						default:
							break;
						}
					}else if(hmExlColVsTblCol.containsKey(iCellIndex)){
						switch (formulaEvaluator.evaluateInCell(cell).getCellTypeEnum())
						{
						case BLANK:
							gridItem.setText(hmExlColVsTblCol.get(iCellIndex), "");
							break;
						case NUMERIC:
							gridItem.setText(hmExlColVsTblCol.get(iCellIndex), ""+(int) Math.round(cell.getNumericCellValue()));
							break;

						case STRING:
							gridItem.setText(hmExlColVsTblCol.get(iCellIndex), cell.getStringCellValue());
							break;

						default:
							break;
						}
					}
				}

				boolean isBOMCellValueAvailable = false;
				boolean isPARTCellValueAvailable = false;

				if(iExcelRows==0) {
					if(!sInputFileError.trim().isEmpty()) {
						writeLogFile(sInputLogFilePath, sInputFileError);
					}
					if(!getMinExcelHeaderError(hmTblNameVsTblCol, alExcelHeaders).isEmpty()) {
						return;
					}
				}else if(iExcelRows!=-1) {
					Integer iFindNoTblCol = hmTblNameVsTblCol.get("FIND");
					Integer iBOMTblCol = hmTblNameVsTblCol.get("BOM");
					Integer iPartTblCol = hmTblNameVsTblCol.get("PART");
					Integer iRefDesTblCol = hmTblNameVsTblCol.get("REF DES");
					Integer iQtyTblCol = hmTblNameVsTblCol.get("QTY");
					if(gridItem!=null) {

						String sRefDesValue = gridItem.getText(iRefDesTblCol);
						if(sRefDesValue!=null && !(sRefDesValue.trim().isEmpty())) {
							String[] sArrRefDes = sRefDesValue.split(",");
							
							Arrays.sort(sArrRefDes);

							StringBuilder strBuilder = new StringBuilder();

							for (int ii = 0; ii < sArrRefDes.length; ii++) {
								strBuilder.append(sArrRefDes[ii]); 
								if (ii < sArrRefDes.length - 1) {
									strBuilder.append(","); 
								}
							}
							 
							for (int iRefDesCnt = 0; iRefDesCnt < sArrRefDes.length; iRefDesCnt++) {
								if(iRefDesCnt==0) {
									gridItem.setText(iRefDesTblCol, sArrRefDes[iRefDesCnt]);
									StoreRefdesInHashMap(gridItem.getText(iPartTblCol),sArrRefDes[iRefDesCnt] );
									//gridItem.setData("bl_occ_ad4OccRefDesignators",strBuilder.toString() );
								}else {
									GridItem newgridItem = new GridItem(gridImportTable, SWT.NONE);
									newgridItem.setText(iFindNoTblCol, gridItem.getText(iFindNoTblCol));
									newgridItem.setText(iBOMTblCol, gridItem.getText(iBOMTblCol));
									newgridItem.setText(iPartTblCol, gridItem.getText(iPartTblCol));
									newgridItem.setText(iRefDesTblCol, sArrRefDes[iRefDesCnt]);
									newgridItem.setText(iQtyTblCol, gridItem.getText(iQtyTblCol));
									StoreRefdesInHashMap(gridItem.getText(iPartTblCol),sArrRefDes[iRefDesCnt] );
									//newgridItem.setData("bl_occ_ad4OccRefDesignators",strBuilder.toString() );

								}
							}
						}


						String sFindValue = gridItem.getText(iFindNoTblCol);
						if(sFindValue.trim().isEmpty()) {
							if(iFindNoNotAvailableFew==0 && iFindNoAvailableAll==0) {
								iFindNoNotAvailableAll++;
							}else if(iFindNoAvailableAll>0) {
								iFindNoNotAvailableFew++;
								//TODO: Write error Message.
							}
						}else{
							int iCurrentFindNo = Integer.parseInt(sFindValue.trim());
							if(iFindNoNotAvailableAll==0 && iFindNoNotAvailableFew==0)
								iFindNoAvailableAll++;
						}

						String sBOMCell = gridItem.getText(iBOMTblCol);
						String sPARTCell = gridItem.getText(iPartTblCol);
						if(sBOMCell != null && sBOMCell.trim().length() >0) {
							isBOMCellValueAvailable = true;
						}
						if(sPARTCell != null && sPARTCell.trim().length() >0) {
							isPARTCellValueAvailable = true;
						}
					}
					if((!isBOMCellValueAvailable || !isPARTCellValueAvailable)) {
						writeLogFile(sInputLogFilePath, ":::::::::Either BOM or PART column cell value is empty at Row : "+ (iExcelRows+2));
						enableDryRun = false;
						//break;
					}
					if(!isBOMCellValueAvailable) {
						writeLogFile(sInputLogFilePath, ":::::::::Found Empty cell for First Column Value at Row : "+ (iExcelRows+2));
						writeLogFile(sInputLogFilePath, "::::::::: Excel Rows after this Row : " + (iExcelRows+2) + " will be ignored...!");
						iFindNoNotAvailableAll--;
						iFindNoNotAvailableFew--;
						enableDryRun = true;
						gridImportTable.remove(gridImportTable.getItemCount()-1);
						break;
					}
				}


				iExcelRows++;
			}

			//writeLogFile(sInputLogFilePath, "Total columns found in excel "+alExcelHeaders.size());
			writeLogFile(sInputLogFilePath, "Total valid input rows found in excel "+iExcelRows);

			if(iFindNoNotAvailableAll==0 && iFindNoNotAvailableFew==0 && iFindNoAvailableAll>0)
				bGenerateFindNo = false;
			else
				bGenerateFindNo = true;
			if(ValidateVariantOrFindNo(bVariantCondition, iFindNoNotAvailableFew)) {
				btnDryRun.setEnabled(false);
			}
			else {
				btnDryRun.setEnabled(enableDryRun);
			}

			fis.close();
		} catch (Exception e){

			System.out.println("Exception in Bom Utility file reading "+e);
			e.printStackTrace();
			try {
				fis.close();
			} catch (IOException e1) {
				e1.printStackTrace();
			}
		}finally {
			try {
				fis.close();
			} catch (IOException e1) {
				e1.printStackTrace();
			}
		}
		return;
	}

	public static boolean isNumeric(String str) { 
		try {  
			Integer.parseInt(str);  
			return true;
		} catch(NumberFormatException e){  
			return false;  
		}  
	}

	private static List<String> splitCSVLine(String csvLine) {
		List<String> fields = new ArrayList<>();
		StringBuilder currentField = new StringBuilder();
		boolean insideQuotes = false;

		for (char c : csvLine.toCharArray()) {
			if (c == '\"') {
				insideQuotes = !insideQuotes;
			} else if (c == ',' && !insideQuotes) {
				fields.add(currentField.toString());
				currentField = new StringBuilder();
			} else {
				currentField.append(c);
			}
		}
		fields.add(currentField.toString()); // Add the last field

		return fields;
	}

	public void readfile_csv(File eBOMfilepath, Grid gridImportTable){

		boolean bNewFormat = false;
		String line = "";  
		String splitBy = ","; 
		if(true){
			try {
				BufferedReader br = new BufferedReader(new FileReader(eBOMfilepath.getAbsolutePath())); 
				int row=0;
				while ((line = br.readLine()) != null)   //returns a Boolean value  
				{  
					if(row==0) {
						String[] bomlinedetails = line.split(splitBy);// use comma as separator 
						int no_of_columns = bomlinedetails.length;
						for(int i=0;i<no_of_columns;i++){
							if(bomlinedetails[i].equalsIgnoreCase("FIND")){
								bNewFormat = true;
								break;
							}
						}
					}else {
						break;
					}

					row++;
				}  


			} catch (Exception e) {
				e.printStackTrace();
			}
			//return;
		}

		GridColumn[] gdColsExist = gridImportTable.getColumns();
		for (int i = 0; i < gdColsExist.length; i++) {
			gdColsExist[i].dispose();
		}

		if(bNewFormat){
			BomUtility_Grid_Col_Create.BomUtility_Import_Tbl_GridColumn_NewFormat(gridImportTable, sArrHeaders);
			readfile_xlsx_New_Format(eBOMfilepath, gridImportTable);
			return;
		}else{
			BomUtility_Grid_Col_Create.BomUtility_Import_Tbl_GridColumn(gridImportTable, sArrHeaders);
		}

		try {
			int csvRow = 0;

			HashMap<String, Integer> hmTblNameVsTblCol = new HashMap<>();
			HashMap<Integer, Integer> hmExlColVsTblCol = new HashMap<>();
			ArrayList<String> alCSVHeaders = new ArrayList<>();

			int iFindNoNotAvailableAll= 0;
			int iFindNoAvailableAll= 0;
			int iFindNoNotAvailableFew= 0;
			boolean bVariantCondition= true;
			boolean enableDryRun = true;

			GridColumn[] gdCols = gridImportTable.getColumns();
			String Ebom_id;
			for (int i = 0; i < gdCols.length; i++) {
				hmTblNameVsTblCol.put(gdCols[i].getText(), i);
			}

			BufferedReader br = new BufferedReader(new FileReader(eBOMfilepath.getAbsolutePath()));  
			Integer iFindNoTblCol = hmTblNameVsTblCol.get("FIND");
			Integer iBOMTblCol = hmTblNameVsTblCol.get("BOM");
			Integer iPartTblCol = hmTblNameVsTblCol.get("PART");
			Integer iRefDesTblCol = hmTblNameVsTblCol.get("REF DES");
			while ((line = br.readLine()) != null)   //returns a Boolean value  
			{  
				//String[] bomlinedetails = line.split(splitBy);// use comma as separator 
				List<String> bomlinedetails = splitCSVLine(line);
				int no_of_columns = bomlinedetails.size();
				String sInputFileError = "";

				if(csvRow==1) {
					sInputFileError = "";
					for(int i=0;i<no_of_columns;i++){
						String header = bomlinedetails.get(i);
						if(header!=null && !header.isEmpty()) {
							alCSVHeaders.add(header.trim());
							if(!hmContainsKey(hmTblNameVsTblCol, header.trim()).isEmpty()) {
								hmExlColVsTblCol.put(i, hmTblNameVsTblCol.get(hmContainsKey(hmTblNameVsTblCol, header.trim())));
							}else {
								sInputFileError = sInputFileError + "Error : Following CSV Column Name is not available in Config file ["+header.trim()+"]";
							}
						}
					}
					csvRow++;
					continue;
				}
				if(csvRow==0) {
					if(no_of_columns>0 && !bomlinedetails.get(0).isEmpty()) {
						Ebom_id = bomlinedetails.get(0);
						writeLogFile(sInputLogFilePath, "EB0M==> "+Ebom_id);
					}

				}else {
					boolean isBOMCellValueAvailable = false;
					boolean isPARTCellValueAvailable = false;
					GridItem gridItem = new GridItem(gridImportTable, SWT.NONE);
					for(int i=0;i<no_of_columns;i++){

						if(hmExlColVsTblCol.containsKey(i)) {
							String value = bomlinedetails.get(i);
							if(isNumeric(value))
								gridItem.setText(hmExlColVsTblCol.get(i),""+(int)Math.round(Integer.parseInt(value)));
							else {
								gridItem.setText(hmExlColVsTblCol.get(i),value);
								if(i==iRefDesTblCol) {
									gridItem.setText(hmExlColVsTblCol.get(i+1),bomlinedetails.get(i+1));
									String sRefDesValue = gridItem.getText(iRefDesTblCol);
									if(sRefDesValue!=null && !(sRefDesValue.trim().isEmpty())) {
										String[] sArrRefDes = sRefDesValue.split(",");

										Arrays.sort(sArrRefDes); 
										StringBuilder strBuilder = new StringBuilder();

										for (int ii = 0; ii < sArrRefDes.length; ii++) {
											strBuilder.append(sArrRefDes[ii]); 
											if (ii < sArrRefDes.length - 1) {
												strBuilder.append(","); 
											} 
										}
										 
										for (int iRefDesCnt = 0; iRefDesCnt < sArrRefDes.length; iRefDesCnt++) {
											if(iRefDesCnt==0) {
												gridItem.setText(iRefDesTblCol, sArrRefDes[iRefDesCnt]);
												StoreRefdesInHashMap(gridItem.getText(iPartTblCol),sArrRefDes[iRefDesCnt] );
												//gridItem.setData("bl_occ_ad4OccRefDesignators",strBuilder.toString() );
											}else {
												GridItem newgridItem = new GridItem(gridImportTable, SWT.NONE);
												newgridItem.setText(iFindNoTblCol, gridItem.getText(iFindNoTblCol));
												newgridItem.setText(iBOMTblCol, gridItem.getText(iBOMTblCol));
												newgridItem.setText(iPartTblCol, gridItem.getText(iPartTblCol));
												newgridItem.setText(iRefDesTblCol, sArrRefDes[iRefDesCnt]);
												StoreRefdesInHashMap(gridItem.getText(iPartTblCol),sArrRefDes[iRefDesCnt] );
												//newgridItem.setData("bl_occ_ad4OccRefDesignators",strBuilder.toString() );

											}
										}
									}
								}

							}
						}

					}

					String sFindValue = gridItem.getText(iFindNoTblCol);
					if(sFindValue.trim().isEmpty()) {
						if(iFindNoNotAvailableFew==0 && iFindNoAvailableAll==0) {
							iFindNoNotAvailableAll++;
						}else if(iFindNoAvailableAll>0) {
							iFindNoNotAvailableFew++;
							//TODO: Write error Message.
						}
					}else{
						int iCurrentFindNo = Integer.parseInt(sFindValue.trim());
						if(iFindNoNotAvailableAll==0 && iFindNoNotAvailableFew==0)
							iFindNoAvailableAll++;
					}
					String sBOMCell = gridItem.getText(iBOMTblCol);
					String sPARTCell = gridItem.getText(iPartTblCol);
					if(sBOMCell != null && sBOMCell.trim().length() >0) {
						isBOMCellValueAvailable = true;
					}
					if(sPARTCell != null && sPARTCell.trim().length() >0) {
						isPARTCellValueAvailable = true;
					}
					if((!isBOMCellValueAvailable || !isPARTCellValueAvailable)) {
						writeLogFile(sInputLogFilePath, ":::::::::Either BOM or PART column cell value is empty at Row : "+ (csvRow+1));
						enableDryRun = false;
						//break;
					}
					if(!isBOMCellValueAvailable) {
						writeLogFile(sInputLogFilePath, ":::::::::Found Empty cell for First Column Value at Row : "+ (csvRow+1));
						writeLogFile(sInputLogFilePath, "::::::::: CSV Rows after this Row : " + (csvRow+1) + " will be ignored...!");
						iFindNoNotAvailableAll--;
						iFindNoNotAvailableFew--;
						enableDryRun = true;
						gridImportTable.remove(gridImportTable.getItemCount()-1);
						break;
					}

				}

				csvRow++;
			}
			writeLogFile(sInputLogFilePath, "Total valid input rows found in CSV "+csvRow);

			if(iFindNoNotAvailableAll==0 && iFindNoNotAvailableFew==0 && iFindNoAvailableAll>0)
				bGenerateFindNo = false;
			else
				bGenerateFindNo = true;
			if(ValidateVariantOrFindNo(bVariantCondition, iFindNoNotAvailableFew)) {
				btnDryRun.setEnabled(false);
			}
			else {
				btnDryRun.setEnabled(enableDryRun);
			}
		}catch (Exception e) {
			// TODO: handle exception
		}
		return;
	}

	public void readfile_xlsx(File eBOMfilepath, Grid gridImportTable){

		boolean bNewFormat = false;

		if(true){
			try {
				int iExcelRows = -1;
				FileInputStream fis = null;
				fis = new FileInputStream(eBOMfilepath.getAbsolutePath());
				@SuppressWarnings("resource")
				XSSFWorkbook book = new XSSFWorkbook(fis);
				XSSFSheet sheet = book.getSheetAt(0);
				Iterator<Row> itr = sheet.iterator();

				while (itr.hasNext()&&iExcelRows == -1) {
					if(iExcelRows == -1) { //To Skip Row1 in Excel File
						iExcelRows++;
						Row row = itr.next();
						Iterator<Cell> cellIterator = row.cellIterator();

						while (cellIterator.hasNext())
						{
							Cell cell = cellIterator.next();
							int iColIdx = cell.getColumnIndex();
							System.out.println("cell.getStringCellValue()  ---> "+cell.getStringCellValue());
							if(iColIdx  == 0) {
								String strFindNo = cell.getStringCellValue();
								if(strFindNo.equalsIgnoreCase("FIND")){
									bNewFormat = true;
									break;
								}

							}
						}
						continue;
					}
				}
				fis.close();
			} catch (Exception e) {
				e.printStackTrace();
			}
			//return;
		}

		GridColumn[] gdColsExist = gridImportTable.getColumns();
		for (int i = 0; i < gdColsExist.length; i++) {
			gdColsExist[i].dispose();
		}

		if(bNewFormat){
			BomUtility_Grid_Col_Create.BomUtility_Import_Tbl_GridColumn_NewFormat(gridImportTable, sArrHeaders);
			readfile_xlsx_New_Format(eBOMfilepath, gridImportTable);
			return;
		}else{
			BomUtility_Grid_Col_Create.BomUtility_Import_Tbl_GridColumn(gridImportTable, sArrHeaders);
		}

		FileInputStream fis = null;
		try {
			int iExcelRows = -1;

			HashMap<String, Integer> hmTblNameVsTblCol = new HashMap<>();
			HashMap<Integer, Integer> hmExlColVsTblCol = new HashMap<>();
			ArrayList<String> alExcelHeaders = new ArrayList<>();

			int iFindNoNotAvailableAll= 0;
			int iFindNoAvailableAll= 0;
			int iFindNoNotAvailableFew= 0;
			boolean bVariantCondition= true;
			boolean enableDryRun = true;

			GridColumn[] gdCols = gridImportTable.getColumns();

			for (int i = 0; i < gdCols.length; i++) {
				hmTblNameVsTblCol.put(gdCols[i].getText(), i);
			}

			fis = new FileInputStream(eBOMfilepath.getAbsolutePath());
			@SuppressWarnings("resource")
			XSSFWorkbook book = new XSSFWorkbook(fis);
			XSSFSheet sheet = book.getSheetAt(0);
			Iterator<Row> itr = sheet.iterator();
			// Iterating over Excel file in Java
			while (itr.hasNext()) {

				if(iExcelRows == -1) { //To Skip Row1 in Excel File
					iExcelRows++;
					Row row = itr.next();
					Iterator<Cell> cellIterator = row.cellIterator();

					while (cellIterator.hasNext())
					{
						Cell cell = cellIterator.next();
						int iColIdx = cell.getColumnIndex();
						if(iColIdx  == 2) {
							String strVariant = cell.getStringCellValue();
							/*if(strVariant != null && !strVariant.isEmpty()) {
                        		if(strVariant.equalsIgnoreCase("y")) {
                        			strToplineItemType = prop.getProperty("VARIANT_TOP_LINE_ITEM_TYPE");
                        			break;
                        		} else if(strVariant.equalsIgnoreCase("n")) {
                        			strToplineItemType = prop.getProperty("NON_VARIANT_TOP_LINE_ITEM_TYPE");
                        			break;
                        		} else {
                        			//error in log - not enable dryrun
                        			bVariantCondition= false;
                        			break;
                        		}
                        	}else {
                        		//error in log - not enable dryrun
                        		bVariantCondition= false;
                        		break;
                        	}*/
						}
					}
					continue;
				}
				String sInputFileError = "";
				if(iExcelRows ==0) {
					sInputFileError = "";
				}

				GridItem gridItem = null;

				if(iExcelRows !=0) {
					gridItem = new GridItem(gridImportTable, SWT.NONE);
				}

				Row row = itr.next();
				// Iterating over each column of Excel file
				Iterator<Cell> cellIterator = row.cellIterator();

				
				while (cellIterator.hasNext()) {

					Cell cell = cellIterator.next();

					FormulaEvaluator formulaEvaluator = book.getCreationHelper().createFormulaEvaluator();

					int iCellIndex = cell.getColumnIndex();
					if(iExcelRows ==0) {
						//String sInputFileError = "";
						switch (formulaEvaluator.evaluateInCell(cell).getCellTypeEnum())
						{
						case STRING:
						{
							if(cell.getStringCellValue()!=null && !cell.getStringCellValue().isEmpty()) {
								alExcelHeaders.add(cell.getStringCellValue().trim());
								
								if(!hmContainsKey(hmTblNameVsTblCol, cell.getStringCellValue().trim()).isEmpty()) {
									hmExlColVsTblCol.put(iCellIndex, hmTblNameVsTblCol.get(hmContainsKey(hmTblNameVsTblCol, cell.getStringCellValue().trim())));
								}else {
									sInputFileError = sInputFileError + "Error : Following Excel Column Name is not available in Config file ["+cell.getStringCellValue().trim()+"]";
								}
							}
						}
						break;

						default:
							break;
						}
					}else if(hmExlColVsTblCol.containsKey(iCellIndex)){
						switch (formulaEvaluator.evaluateInCell(cell).getCellTypeEnum())
						{
						case BLANK:
							gridItem.setText(hmExlColVsTblCol.get(iCellIndex), "");
							break;
						case NUMERIC:
							gridItem.setText(hmExlColVsTblCol.get(iCellIndex), ""+(int) Math.round(cell.getNumericCellValue()));
							break;

						case STRING:
							gridItem.setText(hmExlColVsTblCol.get(iCellIndex), cell.getStringCellValue());
							break;

						default:
							break;
						}
					}
				}

				boolean isBOMCellValueAvailable = false;
				boolean isPARTCellValueAvailable = false;

				if(iExcelRows==0) {
					if(!sInputFileError.trim().isEmpty()) {
						writeLogFile(sInputLogFilePath, sInputFileError);
					}
					if(!getMinExcelHeaderError(hmTblNameVsTblCol, alExcelHeaders).isEmpty()) {
						return;
					}
				}else if(iExcelRows!=-1) {
					Integer iFindNoTblCol = hmTblNameVsTblCol.get("FIND");
					Integer iBOMTblCol = hmTblNameVsTblCol.get("BOM");
					Integer iPartTblCol = hmTblNameVsTblCol.get("PART");
					Integer iRefDesTblCol = hmTblNameVsTblCol.get("REF DES");
					
					if(gridItem!=null) {

						String sRefDesValue = gridItem.getText(iRefDesTblCol);
						if(sRefDesValue!=null && !(sRefDesValue.trim().isEmpty())) {
							String[] sArrRefDes = sRefDesValue.split(",");
							
							Arrays.sort(sArrRefDes);

							StringBuilder strBuilder = new StringBuilder();

							for (int ii = 0; ii < sArrRefDes.length; ii++) {
								strBuilder.append(sArrRefDes[ii]); 
								if (ii < sArrRefDes.length - 1) {
									strBuilder.append(","); 
								} 
							}
							 
							for (int iRefDesCnt = 0; iRefDesCnt < sArrRefDes.length; iRefDesCnt++) {
								if(iRefDesCnt==0) {
									gridItem.setText(iRefDesTblCol, sArrRefDes[iRefDesCnt]);
									//gridItem.setData("bl_occ_ad4OccRefDesignators",strBuilder.toString() );
									StoreRefdesInHashMap(gridItem.getText(iPartTblCol),sArrRefDes[iRefDesCnt] );
								}else {
									GridItem newgridItem = new GridItem(gridImportTable, SWT.NONE);
									newgridItem.setText(iFindNoTblCol, gridItem.getText(iFindNoTblCol));
									newgridItem.setText(iBOMTblCol, gridItem.getText(iBOMTblCol));
									newgridItem.setText(iPartTblCol, gridItem.getText(iPartTblCol));
									newgridItem.setText(iRefDesTblCol, sArrRefDes[iRefDesCnt]);
									//newgridItem.setData("bl_occ_ad4OccRefDesignators",strBuilder.toString() );
									StoreRefdesInHashMap(gridItem.getText(iPartTblCol),sArrRefDes[iRefDesCnt] );
								}
							}
						}


						String sFindValue = gridItem.getText(iFindNoTblCol);
						if(sFindValue.trim().isEmpty()) {
							if(iFindNoNotAvailableFew==0 && iFindNoAvailableAll==0) {
								iFindNoNotAvailableAll++;
							}else if(iFindNoAvailableAll>0) {
								iFindNoNotAvailableFew++;
								//TODO: Write error Message.
							}
						}else{
							int iCurrentFindNo = Integer.parseInt(sFindValue.trim());
							if(iFindNoNotAvailableAll==0 && iFindNoNotAvailableFew==0)
								iFindNoAvailableAll++;
						}

						String sBOMCell = gridItem.getText(iBOMTblCol);
						String sPARTCell = gridItem.getText(iPartTblCol);
						if(sBOMCell != null && sBOMCell.trim().length() >0) {
							isBOMCellValueAvailable = true;
						}
						if(sPARTCell != null && sPARTCell.trim().length() >0) {
							isPARTCellValueAvailable = true;
						}
					}
					if((!isBOMCellValueAvailable || !isPARTCellValueAvailable)) {
						writeLogFile(sInputLogFilePath, ":::::::::Either BOM or PART column cell value is empty at Row : "+ (iExcelRows+2));
						enableDryRun = false;
						//break;
					}
					if(!isBOMCellValueAvailable) {
						writeLogFile(sInputLogFilePath, ":::::::::Found Empty cell for First Column Value at Row : "+ (iExcelRows+2));
						writeLogFile(sInputLogFilePath, "::::::::: Excel Rows after this Row : " + (iExcelRows+2) + " will be ignored...!");
						iFindNoNotAvailableAll--;
						iFindNoNotAvailableFew--;
						enableDryRun = true;
						gridImportTable.remove(gridImportTable.getItemCount()-1);
						break;
					}
				}


				iExcelRows++;
			}

			//writeLogFile(sInputLogFilePath, "Total columns found in excel "+alExcelHeaders.size());
			writeLogFile(sInputLogFilePath, "Total valid input rows found in excel "+iExcelRows);

			if(iFindNoNotAvailableAll==0 && iFindNoNotAvailableFew==0 && iFindNoAvailableAll>0)
				bGenerateFindNo = false;
			else
				bGenerateFindNo = true;
			if(ValidateVariantOrFindNo(bVariantCondition, iFindNoNotAvailableFew)) {
				btnDryRun.setEnabled(false);
			}
			else {
				btnDryRun.setEnabled(enableDryRun);
			}

			fis.close();
		} catch (Exception e){

			System.out.println("Exception in Bom Utility file reading "+e);
			e.printStackTrace();
			try {
				fis.close();
			} catch (IOException e1) {
				e1.printStackTrace();
			}
		}finally {
			try {
				fis.close();
			} catch (IOException e1) {
				e1.printStackTrace();
			}
		}
		return;
	}

	private void StoreRefdesInHashMap(String Part, String refDes) {
		// TODO Auto-generated method stub
		if(hmStoreItemisVsRefDes.get(Part)==null)
			hmStoreItemisVsRefDes.put(Part, refDes);
		else
		{
			String ExistingrefDes=hmStoreItemisVsRefDes.get(Part);
			if(!ExistingrefDes.contains(refDes))
				ExistingrefDes+=","+refDes;
			hmStoreItemisVsRefDes.replace(Part, ExistingrefDes);
		}
	}

	public boolean ValidateVariantOrFindNo(boolean bVariantCondition, int iFindNoNotAvailableFew) {
		String sErrorTest = "";
		if(iFindNoNotAvailableFew>0) {
			sErrorTest = sErrorTest + "Few Objects are not having Find No. So Dry-Run is not enabled. \n";
		}
		if(!bVariantCondition) {
			sErrorTest = sErrorTest + "Variant Condition is failed. So Dry-Run is not enabled. \n";
		}
		if(iFindNoNotAvailableFew>0 || (!bVariantCondition)) {
			writeLogFile(sInputLogFilePath, sErrorTest);
			gridImportTable.setData("VARIANT_FINDNO_INVALID", "TRUE");
			return true;
		}
		return false;
	}

	public String getMinExcelHeaderError(HashMap<String, Integer> hmTblNameVsTblCol, ArrayList<String> alExcelHeaders) {
		String sMinExcelHeaderError = "";

		for (String sKey : hmTblNameVsTblCol.keySet()) {
			boolean bAvailable = false;
			for (String sExcelHeader : alExcelHeaders) {
				if(sKey.trim().equalsIgnoreCase(sExcelHeader.trim())) {
					bAvailable = true;
				}
			}
			if(!bAvailable) {
				sMinExcelHeaderError = "["+ sKey + "]" + sMinExcelHeaderError + "Error : Following Minimum Excel Header is not available in input Excel sheet\n";
			}
		}
		if(!sMinExcelHeaderError.trim().isEmpty()) {
			writeLogFile(sInputLogFilePath, sMinExcelHeaderError);
			getMessageBox(shlBomUtility, SWT.ERROR | SWT.OK, "Error", "Excel format Error, Please correct your input Excel or contact System Administrator");
			return sMinExcelHeaderError;
		}
		return sMinExcelHeaderError;
	}

	public String hmContainsKey(HashMap<String, Integer> hmTblNameVsTblCol, String sCellValue) {
		if(hmTblNameVsTblCol!=null) {
			for (String sKey : hmTblNameVsTblCol.keySet()) {
				if(sKey.trim().equalsIgnoreCase(sCellValue)) {
					return sKey;
				}
			}
		}
		return "";
	}

	public void getColSeq() {
		iColPartNumber = BomUtility_Grid_Col_Create.GetColSeqInGrid(gridImportTable, "ImportTable_COL5");
		iCol_Level = BomUtility_Grid_Col_Create.GetColSeqInGrid(gridImportTable, "ImportTable_COL6");
		iCol_TC_ObjStatus = BomUtility_Grid_Col_Create.GetColSeqInGrid(gridImportTable, "ImportTable_COL7");
	}

	public void updateLogFilePath() {
		GridItem gridItem = null;

		GridItem[] gdItems;
		if(gridLogFiles!=null) {

			gdItems = gridLogFiles.getItems();

			if(gdItems!=null && gdItems.length>0) {
				for (GridItem mGdItem : gdItems) {
					if(mGdItem.getData("BUTTON")!=null && !((Button)mGdItem.getData("BUTTON")).isDisposed()) {
						((Button)mGdItem.getData("BUTTON")).dispose();
					}

					if(mGdItem.getData("EDITOR")!=null) {
						((GridEditor)mGdItem.getData("EDITOR")).dispose();
					}
				}
			}

			gridLogFiles.disposeAllItems();
			gridLogFiles.removeAll();
		}

		if(sInputLogFilePath!=null && sInputLogFilePath.length()>0) {
			gridItem = new GridItem(gridLogFiles, SWT.NONE);
			gridItem.setText(0, "Import File Validation");
			gridItem.setText(1, sInputLogFilePath);

			setGridEditor(gridItem);
			//gridItem.getParent().layout();
		}

		if(sDryRunLogFilePath!=null && sDryRunLogFilePath.length()>0) {
			gridItem = new GridItem(gridLogFiles, SWT.NONE);
			gridItem.setText(0, "DryRun Logs");
			gridItem.setText(1, sDryRunLogFilePath);

			setGridEditor(gridItem);
		}

		if(sExecuteLogFilePath!=null && sExecuteLogFilePath.length()>0) {
			gridItem = new GridItem(gridLogFiles, SWT.NONE);
			gridItem.setText(0, "Execution Logs");
			gridItem.setText(1, sExecuteLogFilePath);

			setGridEditor(gridItem);
		}
		if(sSummaryLogFilePath!=null && sSummaryLogFilePath.length()>0) {
			gridItem = new GridItem(gridLogFiles, SWT.NONE);
			gridItem.setText(0, "Summary Logs");
			gridItem.setText(1, sSummaryLogFilePath);

			setGridEditor(gridItem);
		}
	}

	public void printTCURLInLog(String sLogFilePath) {
		writeLogFile(sLogFilePath, "Logging in to "+serverHost+" by using "+sTcUserName);
	}

	public void setGridEditor(GridItem gridItem) {
		GridEditor editor = new GridEditor(gridLogFiles);
		Button btnOk = new Button(gridLogFiles, SWT.NONE);
		btnOk.setText("Open Log");
		editor.minimumWidth = 50;
		editor.grabHorizontal = true;
		editor.setEditor(btnOk, gridItem, 2);
		Display.getCurrent().update();
		editor.layout();

		shlBomUtility.redraw();
		shlBomUtility.layout();
		shlBomUtility.redraw();

		gridItem.setData("BUTTON", btnOk);
		gridItem.setData("EDITOR", editor);

		btnOk.setData("KEY", gridItem);

		btnOk.addSelectionListener(new SelectionListener() {

			@Override
			public void widgetSelected(SelectionEvent arg0) {
				File mFile = new File(gridItem.getText(1));
				System.out.println(gridItem.getText(1));
				if(mFile.exists()) {
					try {
						Desktop.getDesktop().open(mFile);
					} catch (IOException e) {
						e.printStackTrace();
					}
				}
			}
			@Override
			public void widgetDefaultSelected(SelectionEvent arg0) {
			}
		});

		/*gridItem.getParent().redraw();
		gridItem.getParent().update();
		combo.update();
		combo.redraw();
		gridLogFiles.getParent().pack();
		gridLogFiles.getParent().layout();*/
	}

	public static void writeLogFile(String sLogFilePath, String sOutput, boolean bTimeStamp) {
		try {
			BufferedWriter writer = new BufferedWriter(new FileWriter(sLogFilePath, true));
			if(bTimeStamp)
				writer.write(smpd.format(new Date()).toString() + "   " + sOutput);
			else
				writer.write(sOutput);
			writer.newLine();
			writer.close();
		} catch (Exception e) {
			System.out.println("Erro in Writing LogFIle");
			e.printStackTrace();
		}
	}

	public static void writeLogFile(String sLogFilePath, String sOutput) {
		writeLogFile(sLogFilePath, sOutput, true);
	}

	public void getChoiceWindow(){
		String[] sArrPlatforms = new String[] {"Value 1", "Value 2"};

		final Shell shellProjTree = new Shell(shlBomUtility,SWT.CLOSE|SWT.RESIZE);
		shellProjTree.setLayout(new GridLayout(1, false));
		shellProjTree.setLocation(display.getCursorLocation());

		final Tree treeProj = new Tree(shellProjTree, SWT.CHECK | SWT.BORDER | SWT.V_SCROLL | SWT.H_SCROLL|SWT.MULTI);
		GridData gd_treeProj = new GridData(SWT.FILL, SWT.CENTER, true, false, 1, 1);
		treeProj.setLayoutData(gd_treeProj);
		TreeItem treeItemProj = null;
		for(String PlatformName : sArrPlatforms)
		{
			if(!PlatformName.equals("Select All"))
			{
				PlatformName = PlatformName.replace(",", ";");
				treeItemProj = new TreeItem(treeProj, SWT.NONE);
				treeItemProj.setText(PlatformName);
			}
		}

		treeProj.setData("shell", shellProjTree);
		treeProj.setSize(200,200);

		Button okBtn = new Button(shellProjTree, SWT.NONE);
		GridData gd_okBtn = new GridData(SWT.CENTER, SWT.CENTER, false, false, 1, 1);
		okBtn.setText("Confirm");
		okBtn.setLayoutData(gd_okBtn);

		okBtn.addSelectionListener(new SelectionAdapter() {
			public void widgetSelected(SelectionEvent sEvent) {
				shellProjTree.dispose();
			}
		});

		treeProj.addSelectionListener(new SelectionAdapter()
		{
			public void widgetSelected(SelectionEvent sEvent) {
				String sEventDetail = sEvent.detail == SWT.CHECK ? "Checked": "Selected";
				if(sEventDetail.equalsIgnoreCase("checked"))
				{
					TreeItem tItem = (TreeItem) sEvent.item;
					Boolean bIsChecked = tItem.getChecked();
				}
			}
		});

		shellProjTree.setSize(240, 330);
		shellProjTree.setText("Platform");
		shellProjTree.open();
		while (!shellProjTree.isDisposed()) {
			if (!display.readAndDispatch())
				display.sleep();
		}
	}

}
