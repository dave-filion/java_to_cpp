package xtc.translator.translation;

import java.util.ArrayList;
import java.util.List;

import xtc.translator.representation.CompilationUnit;

public class ProtoManager {
	private List<ProtoClass> protoClasses;

	public ProtoManager(List<ProtoClass> protoClasses) {
		this.protoClasses = protoClasses;
	}
	
	public ArrayList<CompilationUnit> processProtoClasses() {
				
		ArrayList<CompilationUnit> compilationUnits = new ArrayList<CompilationUnit>();
		
		for (ProtoClass protoClass : protoClasses) {
			CompilationUnit compilationUnit = protoClass.makeCompilationUnit();
			compilationUnit.setPackageName(protoClass.getPackageName());			
			compilationUnits.add(compilationUnit);
		}
		
		return compilationUnits;
	}
}
