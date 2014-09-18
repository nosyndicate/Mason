package edu.gmu.cs.mason.handlers;


import java.awt.Dialog;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashSet;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Set;

import org.eclipse.core.commands.AbstractHandler;
import org.eclipse.core.commands.ExecutionEvent;
import org.eclipse.core.commands.ExecutionException;
import org.eclipse.jdt.core.ICompilationUnit;
import org.eclipse.jdt.core.IJavaElement;
import org.eclipse.jdt.core.IType;
import org.eclipse.jdt.core.JavaModelException;
import org.eclipse.jdt.internal.ui.dialogs.SourceActionDialog;
import org.eclipse.jdt.internal.ui.javaeditor.CompilationUnitEditor;
import org.eclipse.jdt.internal.ui.javaeditor.EditorUtility;
import org.eclipse.jdt.internal.ui.javaeditor.JavaEditor;
import org.eclipse.jdt.internal.ui.javaeditor.JavaTextSelection;
import org.eclipse.jdt.ui.JavaUI;
import org.eclipse.jface.dialogs.TitleAreaDialog;
import org.eclipse.jface.text.ITextSelection;
import org.eclipse.jface.viewers.ISelection;
import org.eclipse.jface.viewers.IStructuredSelection;
import org.eclipse.swt.widgets.Shell;
import org.eclipse.ui.IEditorInput;
import org.eclipse.ui.IEditorPart;
import org.eclipse.ui.dialogs.CheckedTreeSelectionDialog;
import org.eclipse.ui.handlers.HandlerUtil;

import edu.gmu.cs.mason.ui.dialogs.CodeInsertionDialog;


public class PropertyGenerationHandler extends AbstractHandler {

	public Object execute(ExecutionEvent event) throws ExecutionException {
		Shell shell = HandlerUtil.getActiveWorkbenchWindow(event).getShell();
		IEditorPart editor = HandlerUtil.getActiveEditor(event); 
		ICompilationUnit unit = getCompilationUnitFromEditor(editor);
		try {
			IType top = getTopJavaElement(unit);

			CodeInsertionDialog dialog = new CodeInsertionDialog(shell, top);
			dialog.open();
			
		} catch (JavaModelException e) {
			e.printStackTrace();
		}
				
		return null;
	}
	
	/**
	 * From the the current active editor, get the compilation unit from it
	 * @param editor
	 * @return
	 */
	private ICompilationUnit getCompilationUnitFromEditor(IEditorPart editor)
	{
		if (editor != null) 
		{
			IEditorInput editorInput= editor.getEditorInput();
			if (editorInput != null) 
			{
				ICompilationUnit unit = (ICompilationUnit) JavaUI.getEditorInputJavaElement(editorInput);
				if(unit!=null)
					return unit;
				else {
					throw new NullPointerException("The Active Compilation Unit doesn't contain an IJavaElement");
				}
			}
		}
		return null;
	}
	
	/**
	 * Force a reconcile of the unit and then get the very first top-level element from the unit
	 * @param unit
	 * @return
	 * @throws JavaModelException
	 */
	private IType getTopJavaElement(ICompilationUnit unit) throws JavaModelException {
		
		// force a reconcile of the unit
		unit.reconcile(
				ICompilationUnit.NO_AST,
				false /* don't force problem detection */,
				null /* use primary owner */,
				null /* no progress monitor */);
		
		// get the first top level type from the compilation unit
		IType types[]  = unit.getTypes();
		return types[0];
	}
	
//	private static class GetterSetterTreeSelectionDialog extends SourceActionDialog {
//
//		private AddGetterSetterContentProvider fContentProvider;
//
//		private static final int SELECT_GETTERS_ID= IDialogConstants.CLIENT_ID + 1;
//		private static final int SELECT_SETTERS_ID= IDialogConstants.CLIENT_ID + 2;
//		private final String SETTINGS_SECTION= "AddGetterSetterDialog"; //$NON-NLS-1$
//		private final String SORT_ORDER= "SortOrdering"; //$NON-NLS-1$
//		private final String ALLOW_SETTERS_FOR_FINALS= "RemoveFinal"; //$NON-NLS-1$
//
//		private IDialogSettings fSettings;
//		private SettersForFinalFieldsFilter fSettersForFinalFieldsFilter;
//
//		private boolean fSortOrder;
//		private boolean fAllowSettersForFinals;
//
//		private ArrayList<GetterSetterEntry> fPreviousSelectedFinals;
//
//
//		public GetterSetterTreeSelectionDialog(Shell parent, ILabelProvider labelProvider, AddGetterSetterContentProvider contentProvider, CompilationUnitEditor editor, IType type) throws JavaModelException {
//			super(parent, labelProvider, contentProvider, editor, type, false);
//			fContentProvider= contentProvider;
//			fPreviousSelectedFinals= new ArrayList<GetterSetterEntry>();
//
//			// http://bugs.eclipse.org/bugs/show_bug.cgi?id=19253
//			IDialogSettings dialogSettings= JavaPlugin.getDefault().getDialogSettings();
//			fSettings= dialogSettings.getSection(SETTINGS_SECTION);
//			if (fSettings == null) {
//				fSettings= dialogSettings.addNewSection(SETTINGS_SECTION);
//				fSettings.put(SORT_ORDER, false);
//				fSettings.put(ALLOW_SETTERS_FOR_FINALS, false);
//			}
//
//			fSortOrder= fSettings.getBoolean(SORT_ORDER);
//			fAllowSettersForFinals= fSettings.getBoolean(ALLOW_SETTERS_FOR_FINALS);
//
//			fSettersForFinalFieldsFilter= new SettersForFinalFieldsFilter(contentProvider);
//		}
//
//		public boolean getSortOrder() {
//			return fSortOrder;
//		}
//
//		public void setSortOrder(boolean sort) {
//			if (fSortOrder != sort) {
//				fSortOrder= sort;
//				fSettings.put(SORT_ORDER, sort);
//				if (getTreeViewer() != null) {
//					getTreeViewer().refresh();
//				}
//			}
//		}
//
//		private boolean allowSettersForFinals() {
//			return fAllowSettersForFinals;
//		}
//
//		public void allowSettersForFinals(boolean allowSettersForFinals) {
//			if (fAllowSettersForFinals != allowSettersForFinals) {
//				fAllowSettersForFinals= allowSettersForFinals;
//				fSettings.put(ALLOW_SETTERS_FOR_FINALS, allowSettersForFinals);
//				CheckboxTreeViewer treeViewer= getTreeViewer();
//				if (treeViewer != null) {
//					ArrayList<GetterSetterEntry> newChecked= new ArrayList<GetterSetterEntry>();
//					if (allowSettersForFinals) {
//						newChecked.addAll(fPreviousSelectedFinals);
//					}
//					fPreviousSelectedFinals.clear();
//					Object[] checkedElements= treeViewer.getCheckedElements();
//					for (int i= 0; i < checkedElements.length; i++) {
//						if (checkedElements[i] instanceof GetterSetterEntry) {
//							GetterSetterEntry entry= (GetterSetterEntry) checkedElements[i];
//							if (allowSettersForFinals || entry.isGetter || !entry.isFinal) {
//								newChecked.add(entry);
//							} else {
//								fPreviousSelectedFinals.add(entry);
//							}
//						}
//					}
//					if (allowSettersForFinals) {
//						treeViewer.removeFilter(fSettersForFinalFieldsFilter);
//					} else {
//						treeViewer.addFilter(fSettersForFinalFieldsFilter);
//					}
//					treeViewer.setCheckedElements(newChecked.toArray());
//				}
//				updateOKStatus();
//			}
//		}
//
//		/* (non-Javadoc)
//		 * @see org.eclipse.ui.dialogs.CheckedTreeSelectionDialog#createTreeViewer(org.eclipse.swt.widgets.Composite)
//		 */
//		@Override
//		protected CheckboxTreeViewer createTreeViewer(Composite parent) {
//			CheckboxTreeViewer treeViewer= super.createTreeViewer(parent);
//			if (!fAllowSettersForFinals) {
//				treeViewer.addFilter(fSettersForFinalFieldsFilter);
//			}
//			return treeViewer;
//		}
//
//		@Override
//		protected void configureShell(Shell shell) {
//			super.configureShell(shell);
//			PlatformUI.getWorkbench().getHelpSystem().setHelp(shell, IJavaHelpContextIds.ADD_GETTER_SETTER_SELECTION_DIALOG);
//		}
//
//		private void createGetterSetterButtons(Composite buttonComposite) {
//			createButton(buttonComposite, SELECT_GETTERS_ID, ActionMessages.GetterSetterTreeSelectionDialog_select_getters, false);
//			createButton(buttonComposite, SELECT_SETTERS_ID, ActionMessages.GetterSetterTreeSelectionDialog_select_setters, false);
//		}
//
//		@Override
//		protected void buttonPressed(int buttonId) {
//			super.buttonPressed(buttonId);
//			switch (buttonId) {
//				case SELECT_GETTERS_ID: {
//					getTreeViewer().setCheckedElements(getGetterSetterElements(true));
//					updateOKStatus();
//					break;
//				}
//				case SELECT_SETTERS_ID: {
//					getTreeViewer().setCheckedElements(getGetterSetterElements(false));
//					updateOKStatus();
//					break;
//				}
//			}
//		}
//
//		@Override
//		protected Composite createInsertPositionCombo(Composite composite) {
//			Button addRemoveFinalCheckbox= addAllowSettersForFinalslCheckbox(composite);
//			addRemoveFinalCheckbox.setLayoutData(new GridData(GridData.FILL_HORIZONTAL));
//
//			Composite entryComposite= super.createInsertPositionCombo(composite);
//			addSortOrder(entryComposite);
//			addVisibilityAndModifiersChoices(entryComposite);
//			return entryComposite;
//		}
//
//		private Button addAllowSettersForFinalslCheckbox(Composite entryComposite) {
//			Button allowSettersForFinalsButton= new Button(entryComposite, SWT.CHECK);
//			allowSettersForFinalsButton.setText(ActionMessages.AddGetterSetterAction_allow_setters_for_finals_description);
//
//			allowSettersForFinalsButton.addSelectionListener(new SelectionListener() {
//				public void widgetSelected(SelectionEvent e) {
//					boolean isSelected= (((Button) e.widget).getSelection());
//					allowSettersForFinals(isSelected);
//				}
//
//				public void widgetDefaultSelected(SelectionEvent e) {
//					widgetSelected(e);
//				}
//			});
//			allowSettersForFinalsButton.setSelection(allowSettersForFinals());
//			return allowSettersForFinalsButton;
//		}
//
//		private Composite addSortOrder(Composite composite) {
//			Label label= new Label(composite, SWT.NONE);
//			label.setText(ActionMessages.GetterSetterTreeSelectionDialog_sort_label);
//			GridData gd= new GridData(GridData.FILL_BOTH);
//			label.setLayoutData(gd);
//
//			final Combo combo= new Combo(composite, SWT.READ_ONLY);
//			combo.setItems(new String[] { ActionMessages.GetterSetterTreeSelectionDialog_alpha_pair_sort,
//					ActionMessages.GetterSetterTreeSelectionDialog_alpha_method_sort});
//			final int methodIndex= 1; // Hard-coded. Change this if the
//			// list gets more complicated.
//			// http://bugs.eclipse.org/bugs/show_bug.cgi?id=38400
//			int sort= getSortOrder() ? 1 : 0;
//			combo.setText(combo.getItem(sort));
//			gd= new GridData(GridData.FILL_BOTH);
//			combo.setLayoutData(gd);
//			combo.addSelectionListener(new SelectionAdapter() {
//
//				@Override
//				public void widgetSelected(SelectionEvent e) {
//					setSortOrder(combo.getSelectionIndex() == methodIndex);
//				}
//			});
//			return composite;
//		}
//
//		private Object[] getGetterSetterElements(boolean isGetter) {
//			Object[] allFields= fContentProvider.getElements(null);
//			Set<GetterSetterEntry> result= new HashSet<GetterSetterEntry>();
//			for (int i= 0; i < allFields.length; i++) {
//				IField field= (IField) allFields[i];
//				GetterSetterEntry[] entries= getEntries(field);
//				for (int j= 0; j < entries.length; j++) {
//					AddGetterSetterAction.GetterSetterEntry entry= entries[j];
//					if (entry.isGetter == isGetter)
//						result.add(entry);
//				}
//			}
//			return result.toArray();
//		}
//
//		private GetterSetterEntry[] getEntries(IField field) {
//			List<Object> result= Arrays.asList(fContentProvider.getChildren(field));
//			return result.toArray(new GetterSetterEntry[result.size()]);
//		}
//
//		@Override
//		protected Composite createSelectionButtons(Composite composite) {
//			Composite buttonComposite= super.createSelectionButtons(composite);
//
//			GridLayout layout= new GridLayout();
//			buttonComposite.setLayout(layout);
//
//			createGetterSetterButtons(buttonComposite);
//
//			layout.marginHeight= 0;
//			layout.marginWidth= 0;
//			layout.numColumns= 1;
//
//			return buttonComposite;
//		}
//
//		/*
//		 * @see org.eclipse.jdt.internal.ui.dialogs.SourceActionDialog#createLinkControl(org.eclipse.swt.widgets.Composite)
//		 */
//		@Override
//		protected Control createLinkControl(Composite composite) {
//			Link link= new Link(composite, SWT.WRAP);
//			link.setText(ActionMessages.AddGetterSetterAction_template_link_description);
//			link.addSelectionListener(new SelectionAdapter() {
//				@Override
//				public void widgetSelected(SelectionEvent e) {
//					openCodeTempatePage(CodeTemplateContextType.GETTERCOMMENT_ID);
//				}
//			});
//			link.setToolTipText(ActionMessages.AddGetterSetterAction_template_link_tooltip);
//
//			GridData gridData= new GridData(SWT.FILL, SWT.BEGINNING, true, false);
//			gridData.widthHint= convertWidthInCharsToPixels(40); // only expand further if anyone else requires it
//			link.setLayoutData(gridData);
//			return link;
//		}
//	}
//
//	private static class GetterSetterEntry {
//		public final IField field;
//		public final boolean isGetter;
//		public final boolean isFinal;
//
//		GetterSetterEntry(IField field, boolean isGetterEntry, boolean isFinal) {
//			this.field= field;
//			this.isGetter= isGetterEntry;
//			this.isFinal= isFinal;
//		}
//	}
//}

}
