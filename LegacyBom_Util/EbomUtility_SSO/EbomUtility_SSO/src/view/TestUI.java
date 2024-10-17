package view;

import org.eclipse.swt.widgets.Display;
import org.eclipse.swt.widgets.Shell;
import org.eclipse.swt.widgets.Tree;
import org.eclipse.swt.widgets.TreeItem;
import org.eclipse.swt.widgets.Button;
import org.eclipse.swt.SWT;
import org.eclipse.swt.events.SelectionAdapter;
import org.eclipse.swt.events.SelectionEvent;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.layout.GridLayout;

public class TestUI {

	protected Shell shell;
	Display display;

	/**
	 * Launch the application.
	 * @param args
	 */
	public static void main(String[] args) {
		try {
			TestUI window = new TestUI();
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
		createContents();
		shell.open();
		shell.layout();
		while (!shell.isDisposed()) {
			if (!display.readAndDispatch()) {
				display.sleep();
			}
		}
	}

	/**
	 * Create contents of the window.
	 */
	protected void createContents() {
		shell = new Shell();
		shell.setSize(450, 300);
		shell.setText("SWT Application");
		
		Button btnOpenUi = new Button(shell, SWT.NONE);
		btnOpenUi.addSelectionListener(new SelectionAdapter() {
			@Override
			public void widgetSelected(SelectionEvent e) {
				getChoiceWindow();
			}
		});
		btnOpenUi.setBounds(10, 21, 75, 25);
		btnOpenUi.setText("Open UI");

	}
	
	public void getChoiceWindow(){
		
		boolean bBOM_Exist = false, bPartsExist = false;
		
		String[] sArrPlatforms = new String[] {"BOM Exist, do you want to override?", "A few Parts are not available, Do you want to create automatically?"};
			
		final Shell shellProjTree = new Shell(shell,SWT.CLOSE|SWT.RESIZE);
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
