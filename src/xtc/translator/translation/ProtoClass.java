package xtc.translator.translation;

import xtc.translator.representation.CompilationUnit;
import xtc.tree.Node;


public class ProtoClass {

	private Node sourceNode;
	private String packageName;
	
	public ProtoClass(String packageName, Node sourceNode) {
		this.sourceNode = sourceNode;
		this.packageName = packageName;
	}
	
	public CompilationUnit makeCompilationUnit() {		
		CompilationUnit cu = new CompilationUnit();
		cu.dispatch(sourceNode);
		cu.setPackageName(this.getPackageName());
		return cu;
	}
	
	public Node getSourceNode() {
		return sourceNode;
	}

	public void setSourceNode(Node sourceNode) {
		this.sourceNode = sourceNode;
	}

	public String getPackageName() {
		return packageName;
	}

	public void setPackageName(String packageName) {
		this.packageName = packageName;
	}

	public String toString() {
		return "Class in " + packageName;
	}
}
