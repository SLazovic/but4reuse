package org.but4reuse.adapters.utils;

import org.apache.bcel.classfile.JavaClass;
import org.apache.bcel.classfile.Method;
import org.apache.bcel.util.BCELComparator;

public class ClassComparator implements BCELComparator{

	public static ClassComparator comparator=new ClassComparator();	
	@Override
	public boolean equals(Object arg0, Object arg1) {
		// TODO Auto-generated method stub
		if(!(arg0 instanceof JavaClass&&arg1 instanceof JavaClass))
			return false;
		JavaClass c1=(JavaClass)arg0, c2=(JavaClass)arg1;
		if(!c1.getClassName().equals(c2.getClassName()))
			return false;
		Method []m1=c1.getMethods(), m2=c2.getMethods();
		if(m1.length!=m2.length)
			return false;
		int i=0; 
		while(i<m1.length){
			int j=0;
			boolean found=false;
			while(j<m2.length&&!found){
				found=m1[i].equals(m2[j]);
				j++;
			}
			if(!found)
				return false;
			i++;
		}
		return true;
	}

	@Override
	public int hashCode(Object arg0) {
		// TODO Auto-generated method stub
		return 0;
	}
}
