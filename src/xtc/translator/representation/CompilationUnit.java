package xtc.translator.representation;

import java.util.ArrayList;
import java.util.List;

import xtc.tree.GNode;
import xtc.tree.Node;
import xtc.tree.Visitor;

public class CompilationUnit extends BaseVisitor{
	
	private String packageName;
	private ClassVisitor classVisitor;
	private ArrayList<String> imports;
	
	public CompilationUnit(){
		imports = new ArrayList<String>();
	}
	
	public String getPackageName() {
		return packageName;
	}

	public ClassVisitor getClassVisitor() {
		return classVisitor;
	}

	public ArrayList<String> getImports() {
		return imports;
	}

	public void setPackageName(String packageName) {
		this.packageName = packageName;
	}

	/**
	 * Every time a compilationUnit is visited, create a new ClassVisitor and
	 * dispatch it to collect relevant class data.
	 */
	public void visitCompilationUnit(GNode n) {
		visit(n);
		
		ClassVisitor newClass = new ClassVisitor();
		newClass.dispatch(n);
		this.classVisitor = newClass;
	}

	public void visitImportDeclaration(GNode n) {
		String theImport = "";
		
		Node qualifiedIdentifier = n.getNode(1);
		
		for (int i = 0; i < qualifiedIdentifier.size(); i++) {
			theImport += qualifiedIdentifier.getString(i);
			
			if (i != qualifiedIdentifier.size() - 1) {
				theImport += ".";
			}
		}
		
		if ( "*".equals(n.getString(2)) ) {
			theImport += ".*";
		}
		
		imports.add(theImport);
	}
	
}
