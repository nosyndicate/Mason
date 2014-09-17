package edu.gmu.cs.mason.wizards.project.ui.field;


import edu.gmu.cs.mason.wizards.model.FieldInformation;

import org.eclipse.jface.viewers.ArrayContentProvider;
import org.eclipse.jface.viewers.ColumnLabelProvider;
import org.eclipse.jface.viewers.ColumnViewerEditorActivationEvent;
import org.eclipse.jface.viewers.ColumnViewerEditorActivationListener;
import org.eclipse.jface.viewers.ColumnViewerEditorDeactivationEvent;
import org.eclipse.jface.viewers.IDoubleClickListener;
import org.eclipse.jface.viewers.ISelection;
import org.eclipse.jface.viewers.ISelectionChangedListener;
import org.eclipse.jface.viewers.IStructuredSelection;
import org.eclipse.jface.viewers.TableViewer;
import org.eclipse.jface.viewers.TableViewerColumn;
import org.eclipse.swt.SWT;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Group;
import org.eclipse.swt.widgets.TableColumn;

public class FieldPortrayalViewer {
	
	private TableViewer tableViewer;
	private FieldPortrayalEditingSupport editingSupport;
	
	private static final int FIELD_BOUND = 150;
	private static final int PORTRAY_BOUND = 150;
	private static final int TYPE_BOUND = 200;
	
	private static final String FIELD = "Field name";
	private static final String TYPE = "Field type";
	private static final String PORTRAY = "Portrayal of the field";
	
	
	public FieldPortrayalViewer(Group group, int style) {
		tableViewer = new TableViewer(group, style);
		
		createColumns(group, tableViewer);	
		tableViewer.getTable().setHeaderVisible(true);
		tableViewer.getTable().setLinesVisible(true);
		
		tableViewer.setContentProvider(ArrayContentProvider.getInstance());
		
		tableViewer.getColumnViewerEditor().addEditorActivationListener(new ColumnViewerEditorActivationListener() {
			
			@Override
			public void beforeEditorActivated(ColumnViewerEditorActivationEvent event) {
				IStructuredSelection selection = (IStructuredSelection) tableViewer.getSelection();
				FieldInformation fieldInfo = (FieldInformation) selection.getFirstElement();
				editingSupport.setProperInput(fieldInfo);
			}
			
			@Override
			public void afterEditorDeactivated(ColumnViewerEditorDeactivationEvent event) {}
			
			@Override
			public void afterEditorActivated(ColumnViewerEditorActivationEvent event) {}

			@Override
			public void beforeEditorDeactivated(ColumnViewerEditorDeactivationEvent event) {}
		});

		
	}
	
	

	
	private void createColumns(Composite parent, TableViewer viewer) {

		TableViewerColumn col = createTableViewerColumn(FIELD, FIELD_BOUND, 0);
	    col.setLabelProvider(new ColumnLabelProvider() {
	      @Override
	      public String getText(Object element) {
	        FieldInformation fieldInfo = (FieldInformation) element;
	        return fieldInfo.getFieldName();
	      }
	    });

	    col = createTableViewerColumn(TYPE, TYPE_BOUND, 1);
	    col.setLabelProvider(new ColumnLabelProvider() {
	      @Override
	      public String getText(Object element) {
	        FieldInformation fieldInfo = (FieldInformation) element;
	        return fieldInfo.getFieldType().toString()+fieldInfo.getDimension().toString();
	      }
	    });

	    col = createTableViewerColumn(PORTRAY, PORTRAY_BOUND, 2);
	    editingSupport = new FieldPortrayalEditingSupport(col.getViewer());
	    col.setEditingSupport(editingSupport);
	    col.setLabelProvider(new ColumnLabelProvider() {
	      @Override
	      public String getText(Object element) {
	    	  FieldInformation fieldInfo = (FieldInformation) element;
	    	  return fieldInfo.getPortrayal().toString();
	      }
	    });
	}
	
	
	  private TableViewerColumn createTableViewerColumn(String title, int bound, int colNumber) {
		    final TableViewerColumn viewerColumn = new TableViewerColumn(tableViewer, SWT.NONE);
		    final TableColumn column = viewerColumn.getColumn();
		    column.setText(title);
		    column.setWidth(bound);
		    column.setResizable(true);
		    column.setMoveable(true);
		    return viewerColumn;
		  }
		
		public void addSelectionChangedListener(ISelectionChangedListener listener)
		{
			tableViewer.addSelectionChangedListener(listener);
		}
		  
		public void addDoubleClickListener(IDoubleClickListener listener)
		{
			tableViewer.addDoubleClickListener(listener);
		}
		  
		public ISelection getSelection()
		{
			return tableViewer.getSelection();
		}
		
		public void setInput(Object input)
		{
			tableViewer.setInput(input);
		}
		
		
		public void setLayoutData(Object layoutData)
		{
			tableViewer.getTable().setLayoutData(layoutData);
		}
		
			
		public void refresh()
		{
			tableViewer.refresh();
		}
	
	
}
