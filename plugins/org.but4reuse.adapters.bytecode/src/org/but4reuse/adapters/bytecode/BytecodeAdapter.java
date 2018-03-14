package org.but4reuse.adapters.bytecode;
import java.net.URI;
import java.util.List;

import org.but4reuse.adapters.IAdapter;
import org.but4reuse.adapters.IElement;
import org.but4reuse.adapters.utils.BytecodeUtils;
import org.eclipse.core.runtime.IProgressMonitor;

public class BytecodeAdapter implements IAdapter{

	@Override
	public boolean isAdaptable(URI uri, IProgressMonitor monitor) {
		// TODO Auto-generated method stub
		return BytecodeUtils.isBytecodeFile(uri.getPath());
	}

	@Override
	public List<IElement> adapt(URI uri, IProgressMonitor monitor) {
		// TODO Auto-generated method stub
		List<IElement> list=null;
		list=BytecodeUtils.getArtefactElements(uri);
		return list;
	}

	@Override
	public void construct(URI uri, List<IElement> elements, IProgressMonitor monitor) {
		// TODO Auto-generated method stub
		
	}

}
