package edu.gmu.cs.mason.wizards.project.ui.field;


import java.util.HashMap;

import edu.gmu.cs.mason.wizards.model.FieldInformation;
import edu.gmu.cs.mason.wizards.model.FieldInformation.PortrayType;

import org.eclipse.jface.viewers.ArrayContentProvider;
import org.eclipse.jface.viewers.CellEditor;
import org.eclipse.jface.viewers.ColumnViewer;
import org.eclipse.jface.viewers.ComboBoxViewerCellEditor;
import org.eclipse.jface.viewers.EditingSupport;
import org.eclipse.jface.viewers.LabelProvider;
import org.eclipse.swt.SWT;
import org.eclipse.swt.widgets.Composite;



public class FieldPortrayalEditingSupport extends EditingSupport {

	static 
	{
		FieldPortrayalEditingSupport.fieldPortrayMap = new HashMap<String, PortrayType[]>();
		//continuous
		PortrayType[] types = new PortrayType[]{PortrayType.None,PortrayType.ContinuousPortrayal2D};
		FieldPortrayalEditingSupport.fieldPortrayMap.put("Continuous2D", types);
		types = new PortrayType[]{PortrayType.None,PortrayType.ContinuousPortrayal3D};
		FieldPortrayalEditingSupport.fieldPortrayMap.put("Continuous3D", types);
		
		//IntGrid and DoubleGrid
		types = new PortrayType[]{PortrayType.None,PortrayType.ValueGridPortrayal2D,
				PortrayType.HexaValueGridPortrayal2D,
				PortrayType.FastValueGridPortrayal2D,PortrayType.FastHexaValueGridPortrayal2D};
		FieldPortrayalEditingSupport.fieldPortrayMap.put("IntGrid2D", types);
		FieldPortrayalEditingSupport.fieldPortrayMap.put("DoubleGrid2D", types);
		
		types = new PortrayType[]{PortrayType.None,PortrayType.ValueGridPortrayal3D};
		FieldPortrayalEditingSupport.fieldPortrayMap.put("IntGrid3D", types);
		FieldPortrayalEditingSupport.fieldPortrayMap.put("DoubleGrid3D", types);
		
		//ObjectGrid
		types = new PortrayType[]{PortrayType.None,PortrayType.ObjectGridPortrayal2D,
				PortrayType.HexaObjectGridPortrayal2D,
				PortrayType.FastObjectGridPortrayal2D,PortrayType.FastHexaObjectGridPortrayal2D};
		FieldPortrayalEditingSupport.fieldPortrayMap.put("ObjectGrid2D", types);
		types = new PortrayType[]{PortrayType.None,PortrayType.ObjectGridPortrayal3D};
		FieldPortrayalEditingSupport.fieldPortrayMap.put("ObjectGrid3D", types);
		
		//SparseGrid
		types = new PortrayType[]{PortrayType.None,PortrayType.SparseGridPortrayal2D, 
				PortrayType.HexaSparseGridPortrayal2D};
		FieldPortrayalEditingSupport.fieldPortrayMap.put("SparseGrid2D", types);
		types = new PortrayType[]{PortrayType.None,PortrayType.SparseGridPortrayal3D};
		FieldPortrayalEditingSupport.fieldPortrayMap.put("SparseGrid3D", types);
		
		//DenseGrid
		types = new PortrayType[]{PortrayType.None,PortrayType.DenseGridPortrayal2D};
		FieldPortrayalEditingSupport.fieldPortrayMap.put("DenseGrid2D", types);
		
		types = new PortrayType[]{PortrayType.None};
		FieldPortrayalEditingSupport.fieldPortrayMap.put("DenseGrid3D", types);
		
		
	}
	
	
	public static HashMap<String, PortrayType[]> fieldPortrayMap;

	
	private ComboBoxViewerCellEditor cellEditor = null;
	
	public FieldPortrayalEditingSupport(ColumnViewer viewer) {
		super(viewer);
		

		cellEditor = new ComboBoxViewerCellEditor((Composite) getViewer().getControl(), SWT.READ_ONLY);
		cellEditor.setLabelProvider(new LabelProvider());
		cellEditor.setContentProvider(ArrayContentProvider.getInstance());

		
	}
	


	@Override
	protected CellEditor getCellEditor(Object element) {
		return cellEditor;
		
	}


	@Override
	protected boolean canEdit(Object element) {
		return true;
	}


	@Override
	protected Object getValue(Object element) {
		FieldInformation fieldInfo = (FieldInformation)element;
		return fieldInfo.getPortrayal();
	}


	@Override
	protected void setValue(Object element, Object value) {
		FieldInformation fieldInfo = (FieldInformation)element;
		PortrayType portrayal = (PortrayType)value;
		fieldInfo.setPortrayal(portrayal);
		getViewer().update(element, null);
	}



	public void setProperInput(FieldInformation fieldInfo) {
		String key = fieldInfo.getFieldType()+fieldInfo.getDimension().toString();
		PortrayType[] values = (PortrayType[]) FieldPortrayalEditingSupport.fieldPortrayMap.get(key);
		cellEditor.setInput(values);
		
	}
	
	

}
