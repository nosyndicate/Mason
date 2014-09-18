package edu.gmu.cs.mason.wizards.agent;

import org.eclipse.jface.viewers.IStructuredSelection;
import org.eclipse.jface.wizard.Wizard;
import org.eclipse.ui.INewWizard;
import org.eclipse.ui.IWorkbench;

public class AgentWizard extends Wizard implements INewWizard {

	public AgentWizard() {

	}

	public void init(IWorkbench workbench, IStructuredSelection selection) {


	}

	@Override
	public boolean performFinish() {

		return false;
	}

}
