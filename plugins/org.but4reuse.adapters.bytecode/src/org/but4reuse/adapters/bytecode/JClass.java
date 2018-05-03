package org.but4reuse.adapters.bytecode;

import java.util.ArrayList;
import java.util.List;

import org.objectweb.asm.tree.FieldNode;
import org.objectweb.asm.tree.MethodNode;

public class JClass {
	private List<FieldNode> fields;
	private List<MethodNode> methods;
	private List<String> interfaces;
	private String superClassName;
	private String className;
	private int accessFlags;
	
	public JClass(String className){
		fields=new ArrayList<FieldNode>();
		methods=new ArrayList<MethodNode>();
		interfaces=new ArrayList<String>();
		this.className=className;
	}
	public List<FieldNode> getFields() {
		return fields;
	}
	public void addField(FieldElement field) {
		this.fields.add(field.getField());
	}
	public List<MethodNode> getMethods() {
		return methods;
	}
	public void addMethod(MethodElement method) {
		this.methods.add(method.getMethod());
	}
	public List<String> getInterfaces() {
		return interfaces/*.toArray(new String[interfaces.size()])*/;
	}
	public void addInterface(String interfaceName) {
		this.interfaces.add(interfaceName);
	}
	public String getClassName() {
		return className;
	}
	public int getAccessFlags() {
		return accessFlags;
	}
	public void setAccessFlags(int accessFlags) {
		this.accessFlags = accessFlags;
	}
	public String getSuperClassName() {
		return superClassName;
	}
	public void setSuperClassName(String superClassName) {
		this.superClassName = superClassName;
	}
}
