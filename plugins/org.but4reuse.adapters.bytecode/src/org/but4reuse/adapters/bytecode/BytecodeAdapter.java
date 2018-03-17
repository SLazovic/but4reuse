package org.but4reuse.adapters.bytecode;

import java.io.File;
import java.io.IOException;
import java.net.URI;
import java.util.List;
import org.but4reuse.adapters.IAdapter;
import org.but4reuse.adapters.IElement;
import org.but4reuse.adapters.utils.BytecodeUtils;
import org.but4reuse.utils.files.FileUtils;
import org.eclipse.core.runtime.IProgressMonitor;

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
		for(IElement e:elements){
			if (e instanceof ClassElement){
				ClassElement ce=(ClassElement)e;
				try {
					String packages=ce.getJclass().getPackageName();
					File f=new File(dir+packages.replace('.', '/'));
					f.mkdirs();
					ce.getJclass().dump(f.getAbsoluteFile()+"/"+(ce.getText().substring(packages.length()+1)+".class"));
				} catch (IOException e1) {
					// TODO Auto-generated catch block
					e1.printStackTrace();
				}
			}
		}
	}

}
