package view;

import org.eclipse.nebula.widgets.grid.Grid;
import org.eclipse.nebula.widgets.grid.GridColumn;
import org.eclipse.nebula.widgets.grid.GridColumnGroup;
import org.eclipse.swt.SWT;

public class BomUtility_Grid_Col_Create {

	public static void BomUtility_Import_Tbl_GridColumn(Grid ImportTable_grid, String[] sArrHeaders)
	{
		AddGridCol(ImportTable_grid,null,sArrHeaders[0], sArrHeaders[0], 130,true,"ImportTable_COL1");
		AddGridCol(ImportTable_grid,null,sArrHeaders[1], sArrHeaders[1], 85,true,"ImportTable_COL2");
		AddGridCol(ImportTable_grid,null,sArrHeaders[2], sArrHeaders[2], 120,true,"ImportTable_COL4");
		AddGridCol(ImportTable_grid,null,sArrHeaders[3], sArrHeaders[3], 140,true,"ImportTable_COL5");
		
		if(sArrHeaders.length>=5) {
			AddGridCol(ImportTable_grid,null,sArrHeaders[4], sArrHeaders[4], 140,true,"ImportTable_COL515");
		}
	}
	
	public static void BomUtility_Import_Tbl_GridColumn_NewFormat(Grid ImportTable_grid, String[] sArrHeaders)
	{
		AddGridCol(ImportTable_grid,null,sArrHeaders[0], sArrHeaders[0], 130,true,"ImportTable_COL1");
		AddGridCol(ImportTable_grid,null,sArrHeaders[1], sArrHeaders[1], 85,true,"ImportTable_COL2");
		AddGridCol(ImportTable_grid,null,sArrHeaders[2], sArrHeaders[2], 120,true,"ImportTable_COL4");
		AddGridCol(ImportTable_grid,null,sArrHeaders[3], sArrHeaders[3], 140,true,"ImportTable_COL5");
		
		AddGridCol(ImportTable_grid,null,"QTY", "QTY", 140,true,"ImportTable_COL515");
		
		/*AddGridCol(ImportTable_grid,null,sArrHeaders[2], sArrHeaders[2], 85,true,"ImportTable_COL3");
		AddGridCol(ImportTable_grid,null,sArrHeaders[3], sArrHeaders[3], 120,true,"ImportTable_COL4");
		AddGridCol(ImportTable_grid,null,sArrHeaders[4], sArrHeaders[4], 120,true,"ImportTable_COL5");*/
	}
	
	public static void BomUtility_Import_Tbl_AddExtra_GdCol(Grid ImportTable_grid)
	{
		AddGridCol(ImportTable_grid,null,"Item", "Item", 130,true,"ImportTable_COL6");
		AddGridCol(ImportTable_grid,null,"Teamcenter Status", "Teamcenter Status", 140,true,"ImportTable_COL7");
	}
	
	public static void BomUtility_LogFiles_GdCol(Grid gdLogFiles)
	{
		AddGridCol(gdLogFiles,null,"Log Files", "Log Files", 200,true,"LogFiles");
		AddGridCol(gdLogFiles,null,"File Path", "File Path", 450,true,"FilePath");
		AddGridCol(gdLogFiles,null,"Open Log", "Open Log", 200,true,"LogFiles");
	}
	
	public static GridColumn AddGridCol(Grid grid,GridColumnGroup gridColumnGroup,String sColTxt, String sColTIP, int iColWid, boolean setsummary,String colName)
	{
		GridColumn gridCol;
		if (grid!=null)
			gridCol = new GridColumn(grid, SWT.NONE);
		else
			gridCol = new GridColumn(gridColumnGroup, SWT.NONE);
		gridCol.setText(sColTxt);
		gridCol.setHeaderTooltip(sColTIP);
		gridCol.setWidth(iColWid);
		//gridCol.setSummary(setsummary);
		gridCol.setHeaderWordWrap(true);
		gridCol.setData("colName",colName);
		
		return gridCol;
	}
	
	public static int GetColSeqInGrid(Grid resultgrid, String COL_ID)
    {
        GridColumn[] myCols = resultgrid.getColumns();
        for (int i = 0; i < myCols.length; i++)
        {
            String grd_colName = (String) myCols[i].getData("colName");
            if (grd_colName.equals(COL_ID))
            {
                return i;
            }
        }
        return 0;
    }
	
}
