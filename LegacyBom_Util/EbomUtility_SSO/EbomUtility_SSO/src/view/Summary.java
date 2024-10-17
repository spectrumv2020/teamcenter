package view;

public class Summary {

	private String sMode = "";
	
	private int iTotalExcelRows = 0;
	
	private String sBomID = "";
	private String sBomID_Status = "";
	
	private String sPWB_ItemID = "";
	private String sPWB_ItemID_Status = "";
	
	private int iAvailPartsCnt = 0;
	private String sAvailPartsList = "";
	
	private int iNewPartsCnt = 0;
	private String sNewPartsList = "";
	
	public String getsMode() {
		return sMode;
	}
	public void setsMode(String sMode) {
		this.sMode = sMode;
	}
	
	public int getiTotalExcelRows() {
		return iTotalExcelRows;
	}
	public void setiTotalExcelRows(int iTotalExcelRows) {
		this.iTotalExcelRows = iTotalExcelRows;
	}
	public String getsBomID() {
		return sBomID;
	}
	public void setsBomID(String sBomID) {
		this.sBomID = sBomID;
	}
	public String getsBomID_Status() {
		return sBomID_Status;
	}
	public void setsBomID_Status(String sBomID_Status) {
		this.sBomID_Status = sBomID_Status;
	}
	public String getsPWB_ItemID() {
		return sPWB_ItemID;
	}
	public void setsPWB_ItemID(String sPWB_ItemID) {
		this.sPWB_ItemID = sPWB_ItemID;
	}
	public String getsPWB_ItemID_Status() {
		return sPWB_ItemID_Status;
	}
	public void setsPWB_ItemID_Status(String sPWB_ItemID_Status) {
		this.sPWB_ItemID_Status = sPWB_ItemID_Status;
	}
	public int getiAvailPartsCnt() {
		return iAvailPartsCnt;
	}
	public void setiAvailPartsCnt(int iAvailPartsCnt) {
		this.iAvailPartsCnt = iAvailPartsCnt;
	}
	public String getsAvailPartsList() {
		return sAvailPartsList;
	}
	public void setsAvailPartsList(String sAvailPartsList) {
		if(this.sAvailPartsList.isEmpty()) {
			this.sAvailPartsList = sAvailPartsList;
		}else {
			this.sAvailPartsList = this.sAvailPartsList + "," + sAvailPartsList;
		}
		
	}
	public int getiNewPartsCnt() {
		return iNewPartsCnt;
	}
	public void setiNewPartsCnt(int iNewPartsCnt) {
		this.iNewPartsCnt = iNewPartsCnt;
	}
	public String getsNewPartsList() {
		return sNewPartsList;
	}
	public void setsNewPartsList(String sNewPartsList) {
		if(this.sNewPartsList.isEmpty()) {
			this.sNewPartsList = sNewPartsList;
		}else {
			this.sNewPartsList = this.sNewPartsList + "," + sNewPartsList;
		}
		
	}
	@Override
	public String toString() {
		
		//below Logic Added for uniform printing of EBOM ID and PWB ID in teamcenter.
		String sTempEBOM_ID = getsBomID(), sTempPWB_ID = getsPWB_ItemID();
		if(sTempEBOM_ID.length()>sTempPWB_ID.length()) {
			int iCount = sTempEBOM_ID.length() - sTempPWB_ID.length();
			for (int i = 0; i < iCount; i++) {
				sTempPWB_ID = sTempPWB_ID + " ";
			}
		}else {
			int iCount = sTempPWB_ID.length() - sTempEBOM_ID.length();
			for (int i = 0; i < iCount; i++) {
				sTempEBOM_ID = sTempEBOM_ID + " ";
			}
		}
		
		return sMode + " Log Operation Summary:\n"+ "Total Excel Rows Count\t: " + iTotalExcelRows + "\nBOM ID\t\t\t:  " + sTempEBOM_ID
				+ "\t\tObject Status : " + sBomID_Status + "\nTc Available --> Part Line Item Count (excluding Parent Item ) : [" + iAvailPartsCnt + "]\n\t-   List of Parts : " + sAvailPartsList
				+ "\n"+(sMode.equalsIgnoreCase("execute")?"Newly created":"Tc Not Available")+" Parts count : " + iNewPartsCnt + "\n\t-   List of Parts : " + sNewPartsList;
	}
	
	
	
	
	
}
