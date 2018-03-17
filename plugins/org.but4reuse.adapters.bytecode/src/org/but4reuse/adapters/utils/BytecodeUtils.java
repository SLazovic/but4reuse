package org.but4reuse.adapters.utils;

import java.io.BufferedInputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.net.URI;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import org.apache.bcel.classfile.ClassFormatException;
import org.apache.bcel.classfile.ClassParser;
import org.apache.bcel.classfile.JavaClass;
import org.apache.bcel.classfile.Method;
import org.but4reuse.adapters.IElement;
import org.but4reuse.adapters.bytecode.ClassElement;
import org.but4reuse.adapters.bytecode.ClassNameDependencyObject;
import org.but4reuse.utils.files.FileUtils;

public class BytecodeUtils {
	
	public static final byte []magicNumber={(byte)0xCA, (byte)0xFE, (byte)0xBA, (byte)0xBE};
	
	/**
	 * Check if a file is a bytecode based on the magic number (CAFEBABE)
	 * 
	 * @param file
	 * @return true if it is a bytecode
	 */
	public static boolean isBytecodeFile(File file) {
		if(file==null||!file.exists())
			return false;
		BufferedInputStream bis=null;
		byte []buf=new byte[4];
		try {
			bis=new BufferedInputStream(new FileInputStream(file));
			bis.read(buf, 0, 4);
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			return false;
		}finally {
			if(bis!=null){
				try {
					bis.close();
				} catch (IOException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
			}
		}
		return Arrays.equals(buf, magicNumber);
	}

	/**
	 * Check if a file is a bytecode or if a directory contains a bytecode file based on the extension and the magic number using isBytecodeFile
	 * 
	 * @param file
	 * @param name
	 * @return whether it is found or not
	 */
	public static boolean containsBytecodeFile(File file, String name){
		if(file.isDirectory()){
			for (File child : file.listFiles()) {
				if(containsBytecodeFile(child, child.getName())){
					return true;
				}
			}
			return false;
		}else{
			return name.endsWith(".class")&&isBytecodeFile(file);
		}
	}
	
	public static List<IElement> getArtefactElements(URI uri) {
		List<IElement> listElem=new ArrayList<IElement>();
		File file = FileUtils.getFile(uri);
		List<File> listF=FileUtils.getAllFiles(file);
		JavaClass.setComparator(ClassComparator.comparator);
		Method.setComparator(MethodComparator.comparator);
		for(File f:listF){
			ClassParser cp=new ClassParser(f.getAbsolutePath());
			JavaClass jc=null;
			try {
				jc=cp.parse();
			} catch (ClassFormatException | IOException e) {
				// TODO Auto-generated catch block
				//e.printStackTrace();
			}
			if(jc!=null){
				ClassElement ce=new ClassElement();
				ce.setName(f.getAbsolutePath());
				ce.setJclass(jc);
				ce.addDependency(new ClassNameDependencyObject(jc.getClassName()));
				listElem.add(ce);
			}
		}
		return listElem;
	}
}
