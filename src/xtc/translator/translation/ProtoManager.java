package xtc.translator.translation;

import java.util.ArrayList;
import java.util.List;

import xtc.translator.representation.ClassVisitor;
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
			
			for (ClassVisitor classVisitor : compilationUnit.getClassVisitors()) {
				classVisitor.setPackageName(protoClass.getPackageName());
				classVisitor.setImports(compilationUnit.getImports());
				classVisitor.setSourceNode(protoClass.getSourceNode());				
			}
			
		}
		
		return compilationUnits;
	}
}
