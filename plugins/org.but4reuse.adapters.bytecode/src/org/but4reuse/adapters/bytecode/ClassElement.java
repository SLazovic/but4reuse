package org.but4reuse.adapters.bytecode;

import org.apache.bcel.classfile.JavaClass;
import org.but4reuse.adapters.IElement;
import org.but4reuse.adapters.impl.AbstractElement;

public class ClassElement extends AbstractElement{
	private JavaClass jclass;
	private String name;
	
	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public JavaClass getJclass() {
		return jclass;
	}

	public void setJclass(JavaClass jclass) {
		this.jclass = jclass;
	}

	@Override
	public double similarity(IElement anotherElement) {
		// TODO Auto-generated method stub
		ClassElement tmpClass=(ClassElement)anotherElement;
		if(jclass.equals(tmpClass.getJclass())){
			return 1;
		}
		return 0;
	}

	@Override
	public String getText() {
		// TODO Auto-generated method stub
		return jclass.getClassName();
	}

}
