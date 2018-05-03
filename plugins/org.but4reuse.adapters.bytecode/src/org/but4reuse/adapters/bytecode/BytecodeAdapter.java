package org.but4reuse.adapters.bytecode;

import java.io.DataOutputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.net.URI;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import org.but4reuse.adapters.IAdapter;
import org.but4reuse.adapters.IElement;
import org.but4reuse.adapters.utils.BytecodeUtils;
import org.but4reuse.utils.files.FileUtils;
import org.eclipse.core.runtime.IProgressMonitor;
import org.objectweb.asm.ClassWriter;
import org.objectweb.asm.tree.ClassNode;

public class BytecodeAdapter implements IAdapter{

	@Override
	public boolean isAdaptable(URI uri, IProgressMonitor monitor) {
		// TODO Auto-generated method stub
		File file=FileUtils.getFile(uri);
		return BytecodeUtils.containsBytecodeFile(file, file.getName());
	}

	@Override
	public List<IElement> adapt(URI uri, IProgressMonitor monitor) {
		// TODO Auto-generated method stub
		List<IElement> list=BytecodeUtils.getArtefactElements(uri);
		return list;
	}

	@Override
	public void construct(URI uri, List<IElement> elements, IProgressMonitor monitor) {
		// TODO Auto-generated method stub
		String dir=FileUtils.getFile(uri).getAbsolutePath()+"/";
		File dirF=new File(dir);
		dirF.mkdirs();
		Map<String, JClass> map=new HashMap<String, JClass>();
		for(IElement e:elements){
			if(e instanceof MethodElement){
				MethodElement me=(MethodElement)e;
				JClass c=map.get(me.getClassName());
				if(c==null){
					c=new JClass(me.getClassName());
					map.put(me.getClassName(), c);
				}
				c.addMethod(me);
			}else if(e instanceof FieldElement){
				FieldElement fe=(FieldElement)e;
				JClass c=map.get(fe.getClassName());
				if(c==null){
					c=new JClass(fe.getClassName());
					map.put(fe.getClassName(), c);
				}
				c.addField(fe);
			}else if(e instanceof InterfaceNameElement){
				InterfaceNameElement ine=(InterfaceNameElement)e;
				JClass c=map.get(ine.getClassName());
				if(c==null){
					c=new JClass(ine.getClassName());
					map.put(ine.getClassName(), c);
				}
				c.addInterface(ine.getInterfaceName());
			}else if(e instanceof SuperClassNameElement){
				SuperClassNameElement scne=(SuperClassNameElement)e;
				JClass c=map.get(scne.getClassName());
				if(c==null){
					c=new JClass(scne.getClassName());
					map.put(scne.getClassName(), c);
				}
				c.setSuperClassName(scne.getSuperClassName());
			}else if(e instanceof AccessFlagsElement){
				AccessFlagsElement afe=(AccessFlagsElement)e;
				JClass c=map.get(afe.getClassName());
				if(c==null){
					c=new JClass(afe.getClassName());
					map.put(afe.getClassName(), c);
				}
				c.setAccessFlags(afe.getAccessFlags());
			}
		}
		for(String key:map.keySet()){
			JClass c=map.get(key);
			ClassNode cn=new ClassNode();
			cn.name=c.getClassName();
			cn.access=c.getAccessFlags();
			cn.superName=c.getSuperClassName();
			cn.interfaces=c.getInterfaces();
			cn.fields=c.getFields();
			cn.methods=c.getMethods();
			try {
				String []split=cn.name.split("/");
				String packages=cn.name.replace(split[split.length-1], "");
				File f=new File(dir+packages);
				f.mkdirs();
				ClassWriter cw=new ClassWriter(ClassWriter.COMPUTE_MAXS|ClassWriter.COMPUTE_FRAMES);
				cn.accept(cw);
				DataOutputStream dout=new DataOutputStream(new FileOutputStream(new File(dir+cn.name+".class")));
				dout.write(cw.toByteArray());
				dout.close();
				//f.getAbsoluteFile()+"/"+(key.substring(packages.length()+1)+".class")
			} catch (IOException e1) {
				// TODO Auto-generated catch block
				e1.printStackTrace();
			}
		}
		
	}
}
