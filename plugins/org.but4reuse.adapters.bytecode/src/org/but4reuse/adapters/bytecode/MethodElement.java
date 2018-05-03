package org.but4reuse.adapters.bytecode;

import org.but4reuse.adapters.IElement;
import org.but4reuse.adapters.impl.AbstractElement;
import org.but4reuse.adapters.utils.BytecodeUtils;
import org.objectweb.asm.tree.MethodNode;

public class MethodElement extends AbstractElement{
	
	private MethodNode method;
	private String className;
	
	@Override
	public double similarity(IElement anotherElement) {
		// TODO Auto-generated method stub
		if(anotherElement instanceof MethodElement){
			MethodElement tmp=(MethodElement)anotherElement;
			if(className.equals(tmp.getClassName())&&BytecodeUtils.methodComparator(method, tmp.getMethod()))
				return 1;
		}
		return 0;
	}

	@Override
	public String getText() {
		// TODO Auto-generated method stub
		return className+"-Method-"+method.name+"-"+method.desc;
	}

	public MethodElement(MethodNode method, String className) {
		super();
		this.method = method;
		this.className = className;
	}

	public MethodNode getMethod() {
		return method;
	}

	public void setMethod(MethodNode method) {
		this.method = method;
	}

	public String getClassName() {
		return className;
	}

	public void setClassName(String className) {
		this.className = className;
	}
}
