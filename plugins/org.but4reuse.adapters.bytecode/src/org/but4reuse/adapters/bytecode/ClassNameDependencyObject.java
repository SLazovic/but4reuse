package org.but4reuse.adapters.bytecode;

import org.but4reuse.adapters.IDependencyObject;

public class ClassNameDependencyObject implements IDependencyObject{
	
	private String className;
	
	public ClassNameDependencyObject(String className) {
		// TODO Auto-generated constructor stub
		this.className=className;
	}

	@Override
	public int getMinDependencies(String dependencyID) {
		// TODO Auto-generated method stub
		return 0;
	}

	@Override
	public int getMaxDependencies(String dependencyID) {
		// TODO Auto-generated method stub
		return 1;
	}

	@Override
	public String getDependencyObjectText() {
		// TODO Auto-generated method stub
		return className;
	}
	
	@Override
	public boolean equals(Object obj) {
		if (obj instanceof ClassNameDependencyObject)
			return className.equals(((ClassNameDependencyObject) obj).className);
		return super.equals(obj);
	}
}
