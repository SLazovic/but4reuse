package org.but4reuse.adapters.utils;

import java.io.BufferedInputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.net.URI;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import org.but4reuse.adapters.IElement;
import org.but4reuse.adapters.bytecode.AccessFlagsElement;
import org.but4reuse.adapters.bytecode.FieldElement;
import org.but4reuse.adapters.bytecode.InterfaceNameElement;
import org.but4reuse.adapters.bytecode.MethodElement;
import org.but4reuse.adapters.bytecode.NameDependencyObject;
import org.but4reuse.adapters.bytecode.SuperClassNameElement;
import org.but4reuse.utils.files.FileUtils;
import org.objectweb.asm.ClassReader;
import org.objectweb.asm.tree.AbstractInsnNode;
import org.objectweb.asm.tree.ClassNode;
import org.objectweb.asm.tree.FieldInsnNode;
import org.objectweb.asm.tree.FieldNode;
import org.objectweb.asm.tree.IincInsnNode;
import org.objectweb.asm.tree.IntInsnNode;
import org.objectweb.asm.tree.InvokeDynamicInsnNode;
import org.objectweb.asm.tree.JumpInsnNode;
import org.objectweb.asm.tree.LabelNode;
import org.objectweb.asm.tree.LdcInsnNode;
import org.objectweb.asm.tree.LineNumberNode;
import org.objectweb.asm.tree.LookupSwitchInsnNode;
import org.objectweb.asm.tree.MethodInsnNode;
import org.objectweb.asm.tree.MethodNode;
import org.objectweb.asm.tree.MultiANewArrayInsnNode;
import org.objectweb.asm.tree.TableSwitchInsnNode;
import org.objectweb.asm.tree.TypeInsnNode;
import org.objectweb.asm.tree.VarInsnNode;

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
		for(File f:listF){
			if(isBytecodeFile(f)){
				try {
					ClassReader cr=new ClassReader(new FileInputStream(f));
					ClassNode cn=new ClassNode();
					cr.accept(cn, ClassReader.EXPAND_FRAMES);
					String className=cn.name;
					for(FieldNode fn:cn.fields){
						FieldElement fe=new FieldElement(fn, className);
						fe.addDependency(new NameDependencyObject(fe.getText()));
						listElem.add(fe);
					}
					for(MethodNode mn:cn.methods){
						MethodElement me=new MethodElement(mn, className);
						me.addDependency(new NameDependencyObject(me.getText()));
						listElem.add(me);
					}
					for(String i:cn.interfaces){
						InterfaceNameElement ine=new InterfaceNameElement(i, className);
						ine.addDependency(new NameDependencyObject(ine.getText()));
						listElem.add(ine);
					}
					SuperClassNameElement scne=new SuperClassNameElement(cn.superName, className);
					scne.addDependency(new NameDependencyObject(scne.getText()));
					listElem.add(scne);
					AccessFlagsElement afe=new AccessFlagsElement(cn.access, className);
					listElem.add(afe);
					
				} catch (Exception e) {
					// TODO Auto-generated catch block
					System.out.println(f.getAbsolutePath());
					//e.printStackTrace();
				}
			}	
		}
		return listElem;
	}
	
	public static boolean fieldComparator(FieldNode f1, FieldNode f2){
		return f1.name.equals(f2.name)&&f1.desc.equals(f2.desc)&&f1.access==f2.access;
	}
	
	public static boolean instructionComparator(AbstractInsnNode ai1, AbstractInsnNode ai2){
		if(ai1.getOpcode()==ai2.getOpcode()&&ai1.getType()==ai2.getType()){
			if(ai1 instanceof FieldInsnNode){
				FieldInsnNode i1=(FieldInsnNode)ai1, i2=(FieldInsnNode)ai2;
				return i1.owner.equals(i2.owner)&&i1.name.equals(i2.name)&&i1.desc.equals(i2.desc);
			}else if(ai1 instanceof LdcInsnNode){
				LdcInsnNode i1=(LdcInsnNode)ai1, i2= (LdcInsnNode)ai2;
				return i1.cst.equals(i2.cst);
			}else if(ai1 instanceof IntInsnNode){
				IntInsnNode i1=(IntInsnNode)ai1, i2= (IntInsnNode)ai2;
				return i1.operand==i2.operand;
			}else if(ai1 instanceof VarInsnNode){
				VarInsnNode i1=(VarInsnNode)ai1, i2= (VarInsnNode)ai2;
				return i1.var==i2.var;
			}else if(ai1 instanceof TypeInsnNode){
				TypeInsnNode i1=(TypeInsnNode)ai1, i2= (TypeInsnNode)ai2;
				return i1.desc.equals(i2.desc);
			}else if(ai1 instanceof InvokeDynamicInsnNode){
				InvokeDynamicInsnNode i1=(InvokeDynamicInsnNode)ai1, i2= (InvokeDynamicInsnNode)ai2;
				return i1.name.equals(i2.name)&&i1.desc.equals(i2.desc);
			}else if(ai1 instanceof JumpInsnNode){
				/*JumpInsnNode i1=(JumpInsnNode)ai1, i2= (JumpInsnNode)ai2;
				return i1.label.getLabel().getOffset()==i2.label.getLabel().getOffset();*/
			}else if(ai1 instanceof LabelNode){
				/*LabelNode i1=(LabelNode)ai1, i2= (LabelNode)ai2;
				if(i1.getLabel()!=null&&i2.getLabel()!=null)
					return i1.getLabel().getOffset()==i2.getLabel().getOffset();*/
			}else if(ai1 instanceof IincInsnNode){
				IincInsnNode i1=(IincInsnNode)ai1, i2=(IincInsnNode)ai2;
				return i1.incr==i2.incr&&i1.var==i2.var;
			}else if(ai1 instanceof LineNumberNode){
				/*LineNumberNode i1=(LineNumberNode)ai1, i2=(LineNumberNode)ai2;
				return i1.line==i2.line&&i1.start.getLabel().getOffset()==i2.start.getLabel().getOffset();*/
			}else if(ai1 instanceof MethodInsnNode){
				MethodInsnNode i1=(MethodInsnNode)ai1, i2=(MethodInsnNode)ai2;
				return i1.owner.equals(i2.owner)&&i1.name.equals(i2.name)&&i1.desc.equals(i2.desc)&&i1.itf==i2.itf;
			}else if(ai1 instanceof MultiANewArrayInsnNode){
				MultiANewArrayInsnNode i1=(MultiANewArrayInsnNode)ai1, i2=(MultiANewArrayInsnNode)ai2;
				return i1.desc.equals(i2.desc)&&i1.dims==i2.dims;
			}else if(ai1 instanceof LookupSwitchInsnNode){
				LookupSwitchInsnNode i1=(LookupSwitchInsnNode)ai1, i2=(LookupSwitchInsnNode)ai2;
				boolean ret=/*i1.dflt.getLabel().getOffset()==i2.dflt.getLabel().getOffset()&&*/i1.keys.size()==i2.keys.size()&&i1.labels.size()==i2.labels.size();
				int i=0;
				/*while (i<i1.labels.size()&&ret) {
					ret&=i1.labels.get(i).getLabel().getOffset()==i2.labels.get(i).getLabel().getOffset();
					i++;
				}
				i=0;*/
				while (i<i1.keys.size()&&ret) {
					ret&=i1.keys.get(i)==i2.keys.get(i);
					i++;
				}
				return ret;
				
			}else if(ai1 instanceof TableSwitchInsnNode){
				TableSwitchInsnNode i1=(TableSwitchInsnNode)ai1, i2=(TableSwitchInsnNode)ai2;
				boolean ret=/*i1.dflt.getLabel().getOffset()==i2.dflt.getLabel().getOffset()&&*/i1.labels.size()==i2.labels.size()&&i1.max==i2.max&&i1.min==i2.min;
				/*int i=0;
				while (i<i1.labels.size()&&ret) {
					ret&=i1.labels.get(i).getLabel().getOffset()==i2.labels.get(i).getLabel().getOffset();
					i++;
				}*/
				return ret;
				
			}
			return true;
		}
		return false;
	}
	
	public static boolean methodComparator(MethodNode m1, MethodNode m2){
		if(m1.name.equals(m2.name)&&m1.desc.equals(m2.desc)&&m1.access==m2.access&&m1.instructions.size()==m2.instructions.size()){
			for(int i=0; i<m1.instructions.size(); i++){
				if(!instructionComparator(m1.instructions.get(i), m2.instructions.get(i)))
					return false;
			}
			return true;
		}
		return false;
	}
}
