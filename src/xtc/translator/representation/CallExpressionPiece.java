package xtc.translator.representation;

import java.util.List;
import java.util.Map;

import xtc.translator.translation.CppPrinter;
import xtc.tree.GNode;
import xtc.tree.Node;
import xtc.tree.Visitor;
import xtc.type.C;

public class CallExpressionPiece extends Visitor implements CppPrintable{

	public Map<String, List<Method>> methodMap;
	private Map<String, String> variableMap;
	public Node baseNode;
	public String representation;
	
	public CallExpressionPiece(Node baseNode) {
		this.baseNode = baseNode;
		this.methodMap = null;
	}

	public void processNode(){
		representation = "";
		dispatch(baseNode);
		
	}
	
	public void visitCallExpression(GNode n) {		
		visit(n);

		System.out.println("Call expression " + n);
		
		// get caller
		Node caller = n.getNode(0);
		if (caller.getName().equals("PrimaryIdentifier")) {
			representation += caller.getString(0);
		}
		
		// get arguments here
		Node arguments = n.getNode(3);
		ArgumentVisitor argumentVisitor = new ArgumentVisitor(variableMap);
		argumentVisitor.dispatch(arguments);
				
		// check methodMap
		String name = n.getString(2);		

		// Special case for print and println
		if (name.equals("println") || name.equals("print")) {
			
			representation += ".";
			representation += name;
			//TODO: make this more robust
			representation += "(" + argumentVisitor.getArguments().getArguments().get(0).value + ")";
			
		} else {

			// get method list for name
			List<Method> methodList = methodMap.get(name);

			if (methodList == null) {
				// TODO: throw exception
				System.out.println("COULDNT FIND METHOD " + name);
			} else {
				for (Method method : methodList) {

					// check for method with matching arguments
					if (method.getArguments().compareTo(
							argumentVisitor.getArguments()) == 0) {

						// TODO: need to check method type to see whether to go
						// thru v pointer or statically
						if (method.isStatic) {
							representation += "."
									+ method.getOverloadedIdentifier();
						} else {
							representation += "->__vptr ->"
									+ method.getOverloadedIdentifier();
						}
					}
				}
			}

			// Print arguments
			representation += "(";
			representation += "__this";

			// TODO: fix this horrible get.get thing
			for (Argument arg : argumentVisitor.getArguments().getArguments()) {
				representation += ",";
				representation += arg.value;
			}

			representation += ")";
		}
	}
	
	public void visitThisExpression(GNode n) {
		representation += "__this";
	}
	
	public void visitPrimaryIdentifier(GNode n) {
		//representation += n.getString(0);
	}
	
	public void visitSelectionExpression(GNode n) {		
		String primaryId = n.getNode(0).getString(0);
		String selection = n.getString(1);
		
		representation += primaryId + "." + selection;
	}
	
	public void visit(Node n) {
		for (Object o : n) {
			if (o instanceof Node)
				dispatch((Node) o);
		}
	}
	
	@Override
	public void printCpp(CppPrinter cp) {
		cp.p(representation);
	}

	public Map<String, List<Method>> getMethodMap() {
		return methodMap;
	}

	public void setMethodMap(Map<String, List<Method>> methodMap) {
		this.methodMap = methodMap;
	}

	public Map<String, String> getVariableMap() {
		return variableMap;
	}

	public void setVariableMap(Map<String, String> variableMap) {
		this.variableMap = variableMap;
	}

	public Node getBaseNode() {
		return baseNode;
	}

	public void setBaseNode(Node baseNode) {
		this.baseNode = baseNode;
	}

	public String getRepresentation() {
		return representation;
	}

	public void setRepresentation(String representation) {
		this.representation = representation;
	}
	

}
