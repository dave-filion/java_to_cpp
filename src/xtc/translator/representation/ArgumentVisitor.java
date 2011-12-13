package xtc.translator.representation;

import java.util.Map;

import xtc.tree.GNode;
import xtc.tree.Node;
import xtc.tree.Visitor;

public class ArgumentVisitor extends Visitor {

	private Arguments arguments;
	private Map<String, String> variableMap;
	
	public ArgumentVisitor(Map<String, String> variableMap) {
		arguments = new Arguments();
		this.variableMap = variableMap;
	}
	
	public void visitArguments(GNode n) {
		System.out.println("Arguments look like this: " + n);
		visit(n);
	}
	
	public void visitStringLiteral(GNode n) {
		arguments.addArgument("String");
	}
	
	public void visitPrimaryIdentifier(GNode n) {		
		String type = variableMap.get(n.getString(0));
		arguments.addArgument(type);
	}
	
	public void visitIntegerLiteral(GNode n) {
		arguments.addArgument("int");
	}
	
	public Arguments getArguments() {
		return arguments;
	}

	public void setArguments(Arguments arguments) {
		this.arguments = arguments;
	}

	public void visit(Node n) {
		for (Object o : n) {
			if (o instanceof Node)
				dispatch((Node) o);
		}
	}

}
