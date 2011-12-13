package xtc.translator.representation;

import java.util.HashMap;
import java.util.Map;

import xtc.tree.GNode;
import xtc.tree.Node;
import xtc.tree.Visitor;

public class VariableVisitor extends Visitor{

	private Map<String, String> variableMap;
	
	public VariableVisitor() {
		variableMap = new HashMap<String, String>();
	}

	public Map<String, String> getVariableMap() {
		return variableMap;
	}

	public void setVariableMap(Map<String, String> variableMap) {
		this.variableMap = variableMap;
	}
	
	public void visitMethodDeclaration(GNode n) {
		visit(n);
	}
	
	// TODO: This is somewhat unstable, since there can be multiple 
	// declarators. Make it more robust in the future.
	public void visitFieldDeclaration(GNode n) {
		String type = n.getNode(1).getNode(0).getString(0);
		String name = n.getNode(2).getNode(0).getString(0);
		
		variableMap.put(name, type);
	}
	
	public void visitFormalParameter(GNode n) {		
		String type = n.getNode(1).getNode(0).getString(0);
		String name = n.getString(3);
		
		variableMap.put(name, type);
	}
		
	public void visit(Node n) {
		for (Object o : n) {
			if (o instanceof Node)
				dispatch((Node) o);
		}
	}

}
