package edu.gmu.cs.mason.wizards.project.ui.agent;


import java.util.ArrayList;


import edu.gmu.cs.mason.wizards.model.AgentInformation;
import edu.gmu.cs.mason.wizards.model.FieldInformation;
import org.eclipse.core.runtime.IStatus;
import org.eclipse.jdt.core.JavaConventions;
import org.eclipse.jdt.core.JavaCore;
import org.eclipse.jface.dialogs.IDialogConstants;
import org.eclipse.jface.dialogs.TitleAreaDialog;
import org.eclipse.swt.SWT;
import org.eclipse.swt.events.ModifyEvent;
import org.eclipse.swt.events.ModifyListener;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Combo;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Control;
import org.eclipse.swt.widgets.Label;
import org.eclipse.swt.widgets.Shell;
import org.eclipse.swt.widgets.Text;


public class AgentInfoDialog extends TitleAreaDialog {

	private static final String ADDTITLE = "Add New Agent";
	private static final String ADDMESSAGE = "Enter Information for New Agent";
	private static final String EDITTITLE = "Edit a Agent";
	private static final String EDITMESSAGE = "Edit Information of Agent";
	private static final String DEFAULT_NAME = "Agent0";
	private static final String FIELD_NAME = "Field";
	private static final String INITIALIZATION = "Initialization Template";

	

	
	//widget
	private Text nameText;
	private Text numberText;
	private Combo fieldSelectionCombo;
	private Combo initializationSelectionCombo;

	private ModifyListener textModifyListener;
	
	
	// Fields value
	public AgentInformation agentInfo;

	
	
	public AgentInfoDialog(Shell parentShell) {
		super(parentShell);
		
		
		
		textModifyListener = new ModifyListener() {
			public void modifyText(ModifyEvent e) {
				setDialogComplete(validateData());
			}
		};
	}

	
	@Override
	protected Control createDialogArea(Composite parent) {

		Composite container = (Composite) super.createDialogArea(parent);
		
        initializeDialogUnits(parent);
		
        container.setLayout(new GridLayout(1,false));
        container.setLayoutData(new GridData(GridData.FILL_BOTH));
		
		container.setFont(parent.getFont());


		this.createAgentNameAndNumberGroup(container);
		this.createFieldSelectionGroup(container);
		this.createInitializationSelectionGroup(container);
		
		
		return container;
	}
	
	
	private void createInitializationSelectionGroup(Composite container) {
		Composite group = new Composite(container, SWT.NONE);
		group.setLayout(new GridLayout(2,false));
		group.setLayoutData(new GridData(SWT.FILL,SWT.CENTER,true,false));
		group.setFont(container.getFont());
		
		Label nameLabel = new Label(group, SWT.NONE);
		nameLabel.setText(INITIALIZATION);
		nameLabel.setLayoutData(new GridData(SWT.RIGHT, SWT.CENTER, false, false));
		nameLabel.setFont(group.getFont());
		
		initializationSelectionCombo = new Combo(group, SWT.DROP_DOWN | SWT.READ_ONLY);
		initializationSelectionCombo.setLayoutData(new GridData(SWT.FILL, SWT.CENTER, true, false));
		initializationSelectionCombo.setFont(group.getFont());
		
		//add select item and set the default selection
//		Template[] templates = MasonTemplatePool.getInstance().getTemplates();
//		for(int i = 0;i<templates.length;++i)
//		{
//			initializationSelectionCombo.add(templates[i].getName());
//			initializationSelectionCombo.setData(templates[i].getName(), templates[i]);
//		}
//		initializationSelectionCombo.select(0);
		
	}


	
	private void createFieldSelectionGroup(Composite container) {
		Composite group = new Composite(container, SWT.NONE);
		group.setLayout(new GridLayout(2,false));
		group.setLayoutData(new GridData(SWT.FILL,SWT.CENTER,true,false));
		group.setFont(container.getFont());
		
		Label nameLabel = new Label(group, SWT.NONE);
		nameLabel.setText(FIELD_NAME);
		nameLabel.setLayoutData(new GridData(SWT.RIGHT, SWT.CENTER, false, false));
		nameLabel.setFont(group.getFont());
		
		fieldSelectionCombo = new Combo(group, SWT.DROP_DOWN | SWT.READ_ONLY);
		fieldSelectionCombo.setLayoutData(new GridData(SWT.FILL, SWT.CENTER, true, false));
		fieldSelectionCombo.setFont(group.getFont());
		
		
		fieldSelectionCombo.addModifyListener(new ModifyListener() {
			
			public void modifyText(ModifyEvent e) {
				fieldChanged(e);
				setDialogComplete(validateData());
			}

		});
		
	}

	private void fieldChanged(ModifyEvent e) {
		if(fieldSelectionCombo.getData(fieldSelectionCombo.getText())==null)
		{
			initializationSelectionCombo.select(0);
			initializationSelectionCombo.setEnabled(false);
		}
		else {
			initializationSelectionCombo.setEnabled(true);
		}
	}
	

	private void createAgentNameAndNumberGroup(Composite container) {
		Composite group = new Composite(container, SWT.NONE);
		
        group.setLayout(new GridLayout(2,true));
        group.setLayoutData(new GridData(GridData.FILL_BOTH));
		group.setFont(container.getFont());
		
		nameText = addLabelAndText(group, "Agent Class");
		nameText.setText(DEFAULT_NAME);
		nameText.addModifyListener(textModifyListener);
		
		numberText = addLabelAndText(group, "Number of Agents");
		numberText.setText("1");
		numberText.addModifyListener(textModifyListener);
		
	}


	public void clearFieldInfo()
	{
		agentInfo = new AgentInformation();
	}
	
	
	@Override
	protected void okPressed()
	{
		this.saveDataToModel();
		super.okPressed();
	}
	
	
	
	private void saveDataToModel() {
		this.agentInfo.setAgentName(this.nameText.getText());
		this.agentInfo.setAgentNumber(this.numberText.getText());
		FieldInformation fieldInfo = (FieldInformation) fieldSelectionCombo.getData(fieldSelectionCombo.getText());
		if(fieldInfo==null)
		{
			agentInfo.setField(null);
		}
		else {
			this.agentInfo.setField(fieldInfo);
			
			//currently, we only have one insertion point
//			Template t =  (Template)initializationSelectionCombo.getData(initializationSelectionCombo.getText());
//			InsertionPoint point = new InsertionPoint(Point.SimStateStart,"SimState");
//			this.agentInfo.insertionList.add(new TemplateInsertion(t,point));
		}
		

		
	}

	public void storeModelToUi(ArrayList<FieldInformation> fieldList) {
		nameText.setText(agentInfo.getAgentName());
		numberText.setText(agentInfo.getAgentNumber()+"");
		
		//recover field is little bit hard, because that field may be delete from fieldList
		boolean inList = false;
		for(int i = 0;i<fieldList.size();++i)
		{
			if(agentInfo.getField()!=null&&
					agentInfo.getField().equals(fieldList.get(i)))
			{
				inList = true;
				fieldSelectionCombo.select(i+1);
				break;
			}
		}
		if(inList)
		{
			// FIXME we are currently only deal with one insertion point,
			// the recovery code could be complicated after we have multiple insertion points
//			x
		}
		else {
			fieldSelectionCombo.select(0);
			initializationSelectionCombo.select(0);
		}
		
	}
	

	private void setDialogComplete(boolean finished)
	{
		this.getButton(IDialogConstants.OK_ID).setEnabled(finished);
	}
	
	
	public int openToAdd(ArrayList<FieldInformation> fieldList) {
		this.create();
		this.initializeFieldCombo(fieldList);
		this.setTitle(ADDTITLE);
		this.setMessage(ADDMESSAGE);
		this.clearFieldInfo();
		
		return this.open();
	}

	private void initializeFieldCombo(ArrayList<FieldInformation> fieldList) {
		
		//if we don't have any field, then we don't bother to initialize it
		if(fieldList.size()==0)
		{
			initializationSelectionCombo.setEnabled(false);
		}
		
		//add a default none selection for convenience
		fieldSelectionCombo.add("None");
		fieldSelectionCombo.setData("None", null);
		
		for(int i = 0;i<fieldList.size();++i)
		{
			FieldInformation fieldInfo = fieldList.get(i);
			fieldSelectionCombo.add(fieldInfo.toString());
			fieldSelectionCombo.setData(fieldInfo.toString(), fieldInfo);
		}

		fieldSelectionCombo.select(0);
		
	}


	public int openToEdit(ArrayList<FieldInformation> fieldList) {
		this.create();
		this.initializeFieldCombo(fieldList);
		this.setTitle(EDITTITLE);
		this.setMessage(EDITMESSAGE);
		this.storeModelToUi(fieldList);

		return this.open();
	}


	private boolean validateData() {
		if(!this.checkAgentName())
			return false;
		if(!this.checkAgentNumber())
			return false;
		return true;
	}
	
	private boolean checkAgentNumber() {
		String numString = this.numberText.getText();
		try{
			Integer.parseInt(numString);
		}catch(NumberFormatException exception)
		{
			this.setErrorMessage("Number of agents must be a positive number");
			return false;
		}
		setErrorMessage(null);
		return true;
	}


	private boolean checkAgentName() {
		String nameString = this.nameText.getText();
		IStatus status = JavaConventions.validateJavaTypeName(nameString, JavaCore.VERSION_1_3, JavaCore.VERSION_1_3);
		if(status.isOK())
		{
			this.setErrorMessage(null);
			return true;
		}
		else if(status.getSeverity()==IStatus.ERROR){
			this.setErrorMessage(status.getMessage());
		}
		else if(status.getSeverity()==IStatus.WARNING)
		{
			this.setErrorMessage(status.getMessage());
		}
		return false;
	}


	private Text addLabelAndText(Composite container, String label) {
		Composite group = new Composite(container, SWT.NONE);
		group.setLayout(new GridLayout(2,false));
		group.setLayoutData(new GridData(SWT.FILL,SWT.CENTER,true,false));
		group.setFont(container.getFont());
		
		Label nameLabel = new Label(group, SWT.NONE);
		nameLabel.setText(label);
		nameLabel.setLayoutData(new GridData(SWT.RIGHT, SWT.CENTER, false, false));
		nameLabel.setFont(group.getFont());
		
		Text text = new Text(group, SWT.BORDER);
		text.setLayoutData(new GridData(SWT.FILL, SWT.CENTER, true, false));
		text.setFont(group.getFont());
		
		return text;
		
	}
}
