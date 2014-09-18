package edu.gmu.cs.mason.handlers.ui.dialogs;

import org.eclipse.jdt.core.IType;
import org.eclipse.jdt.core.JavaModelException;
import org.eclipse.swt.SWT;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Control;
import org.eclipse.swt.widgets.Shell;

import edu.gmu.cs.mason.ui.dialogs.CodeInsertionDialog;

public class PropertyGenerationDialog extends CodeInsertionDialog {

	public PropertyGenerationDialog(Shell shell, IType type)
			throws JavaModelException {
		super(shell, type);
		
	}


	protected Control createDialogArea(Composite parent) {
		Composite composite = (Composite) super.createDialogArea(parent);
		
		
		
		
		return composite;
	}
	
}
