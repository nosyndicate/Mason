package edu.gmu.cs.mason.handlers;

import org.eclipse.core.commands.AbstractHandler;
import org.eclipse.core.commands.ExecutionEvent;
import org.eclipse.core.commands.ExecutionException;
import org.eclipse.ui.IWorkbenchWindow;
import org.eclipse.ui.handlers.HandlerUtil;
import org.eclipse.jface.dialogs.MessageDialog;


public class GridGenerationHandler extends AbstractHandler {

	public GridGenerationHandler() {
	}


	public Object execute(ExecutionEvent event) throws ExecutionException {
//		IWorkbenchWindow window = HandlerUtil.getActiveWorkbenchWindowChecked(event);
//		MessageDialog.openInformation(
//				window.getShell(),
//				"Mason Eclipse Plugin",
//				"Hello, Eclipse world");
		System.out.println("field");
		
		return null;
	}
}
