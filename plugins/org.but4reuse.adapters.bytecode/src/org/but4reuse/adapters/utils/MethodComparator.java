package org.but4reuse.adapters.utils;

import org.apache.bcel.classfile.Method;
import org.apache.bcel.util.BCELComparator;

public class MethodComparator implements BCELComparator{
	public static MethodComparator comparator=new MethodComparator();
	@Override
	public boolean equals(Object arg0, Object arg1) {
		// TODO Auto-generated method stub
		if(!(arg0 instanceof Method&&arg1 instanceof Method))
			return false;
		Method m1=(Method)arg0, m2=(Method)arg1;
		if(!m1.getSignature().equals(m2.getSignature()))
			return false;
		if(!m1.getCode().toString().equals(m2.getCode().toString()))//don't work with equals
			return false;
		
		return true;
	}

	@Override
	public int hashCode(Object arg0) {
		// TODO Auto-generated method stub
		return 0;
	}

}
