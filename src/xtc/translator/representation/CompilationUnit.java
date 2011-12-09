package xtc.translator.representation;

import java.io.File;
import java.util.ArrayList;

import xtc.tree.GNode;
import xtc.tree.Node;
import xtc.tree.Visitor;

public class CompilationUnit extends Visitor{
	
	private String packageName;
	private ClassVisitor classVisitor;
	private ArrayList<String> imports;
	
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
		// Loop at imports here, and get those classes
		for (Object o : n) {
			if (o instanceof Node) {
				if (((Node) o).getName() == "ImportDeclaration") {
					dispatch((Node) o);
				}
				if (((Node) o).getName() == "PackageDeclaration") {
					dispatch((Node) o);
				}
			}
		}
		ClassVisitor newClass = new ClassVisitor();
		newClass.dispatch(n);
		this.classVisitor = newClass;
	}

	public void visitImportDeclaration(GNode n) {
		// get QualifiedIdentifier(s)
		String filename = "";
		for (int i = 0; i < n.size(); i++)
			filename += n.getGeneric(1).getString(i) + "/";
		filename = filename.substring(0, filename.length() - 1);
		filename += ".java";

		// packDirs will have the paths of the imported packages
		imports.addAll(getQualifiedIdentifier(n.getGeneric(1)));
	}

	private ArrayList<String> getQualifiedIdentifier(GNode n) {
		ArrayList<String> identifierList = new ArrayList<String>();
		for (int i = 0; i < n.size(); i++) {
			identifierList.add(n.getString(i));
		}
		return identifierList;
	}
	
	/**
	 * Catch-all visit.
	 */
    public void visit(Node n) {
        for (Object o : n)
            if (o instanceof Node)
                dispatch((Node) o);
    }


	public String toString(){
		return this.getClassVisitor().getIdentifier() + " in package: " + this.getPackageName(); 
	}
}
