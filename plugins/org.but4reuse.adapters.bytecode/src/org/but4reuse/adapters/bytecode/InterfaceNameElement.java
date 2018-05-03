package org.but4reuse.adapters.bytecode;

import org.but4reuse.adapters.IElement;
import org.but4reuse.adapters.impl.AbstractElement;

public class InterfaceNameElement extends AbstractElement{
	
	private String interfaceName;
	private String className;
	
	@Override
	public double similarity(IElement anotherElement) {
		// TODO Auto-generated method stub
		if(anotherElement instanceof InterfaceNameElement){
			InterfaceNameElement tmp=(InterfaceNameElement)anotherElement;
			if(className.equals(tmp.getClassName())&&interfaceName.equals(tmp.getInterfaceName()))
				return 1;
		}
		return 0;
	}

	@Override
	public String getText() {
		// TODO Auto-generated method stub
		return className+"-Interface-"+interfaceName;
	}

	public InterfaceNameElement(String interfaceName, String className) {
		super();
		this.interfaceName = interfaceName;
		this.className = className;
	}

	public String getInterfaceName() {
		return interfaceName;
	}

	public void setInterfaceName(String interfaceName) {
		this.interfaceName = interfaceName;
	}

	public String getClassName() {
		return className;
	}

	public void setClassName(String className) {
		this.className = className;
	}
	
}
