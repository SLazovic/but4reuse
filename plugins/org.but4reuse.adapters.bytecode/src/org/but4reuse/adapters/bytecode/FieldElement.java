package org.but4reuse.adapters.bytecode;

import org.but4reuse.adapters.IElement;
import org.but4reuse.adapters.impl.AbstractElement;
import org.but4reuse.adapters.utils.BytecodeUtils;
import org.objectweb.asm.tree.FieldNode;

public class FieldElement extends AbstractElement{
	
	private FieldNode field;
	private String className;
	
	@Override
	public double similarity(IElement anotherElement) {
		// TODO Auto-generated method stub
		if(anotherElement instanceof FieldElement){
			FieldElement tmp=(FieldElement)anotherElement;
			if(className.equals(tmp.getClassName())&&BytecodeUtils.fieldComparator(field, tmp.getField())){
				return 1;
			}
		}
		return 0;
	}

	@Override
	public String getText() {
		// TODO Auto-generated method stub
		return className+"-Field-"+field.name;
	}

	public FieldElement(FieldNode field, String className) {
		super();
		this.field = field;
		this.className = className;
	}

	public FieldNode getField() {
		return field;
	}

	public void setField(FieldNode field) {
		this.field = field;
	}

	public String getClassName() {
		return className;
	}

	public void setClassName(String className) {
		this.className = className;
	}

}
