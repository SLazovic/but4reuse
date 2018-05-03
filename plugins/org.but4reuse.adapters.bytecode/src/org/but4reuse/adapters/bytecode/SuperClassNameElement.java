package org.but4reuse.adapters.bytecode;

import org.but4reuse.adapters.IElement;
import org.but4reuse.adapters.impl.AbstractElement;

public class SuperClassNameElement extends AbstractElement{
	
	private String superClassName;
	private String className;
	
	@Override
	public double similarity(IElement anotherElement) {
		// TODO Auto-generated method stub
		if(anotherElement instanceof SuperClassNameElement){
			SuperClassNameElement tmp=(SuperClassNameElement)anotherElement;
			if(className.equals(tmp.getClassName())&&superClassName.equals(tmp.getSuperClassName()))
				return 1;
		}
		return 0;
	}

	@Override
	public String getText() {
		// TODO Auto-generated method stub
		return className+"-SuperClass-"+superClassName;
	}
	public SuperClassNameElement(String superClassName, String className) {
		super();
		this.superClassName = superClassName;
		this.className = className;
	}

	public String getSuperClassName() {
		return superClassName;
	}

	public void setSuperClassName(String superClassName) {
		this.superClassName = superClassName;
	}

	public String getClassName() {
		return className;
	}

	public void setClassName(String className) {
		this.className = className;
	}
}
