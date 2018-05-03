package org.but4reuse.adapters.bytecode;

import org.but4reuse.adapters.IElement;
import org.but4reuse.adapters.impl.AbstractElement;

public class AccessFlagsElement extends AbstractElement{
	
	private int accessFlags;
	private String className;
	
	@Override
	public double similarity(IElement anotherElement) {
		// TODO Auto-generated method stub
		if(anotherElement instanceof AccessFlagsElement){
			AccessFlagsElement tmp=(AccessFlagsElement)anotherElement;
			if(className.equals(tmp.getClassName())&&accessFlags==tmp.getAccessFlags()){
				return 1;
			}
		}
		return 0;
	}

	@Override
	public String getText() {
		// TODO Auto-generated method stub
		return className+"-AccessFlags-"+accessFlags;
	}

	public AccessFlagsElement(int accessFlags, String className) {
		super();
		this.accessFlags = accessFlags;
		this.className = className;
	}

	public int getAccessFlags() {
		return accessFlags;
	}

	public void setAccessFlags(int accessFlags) {
		this.accessFlags = accessFlags;
	}

	public String getClassName() {
		return className;
	}

	public void setClassName(String className) {
		this.className = className;
	}

}
