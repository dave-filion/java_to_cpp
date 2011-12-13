package xtc.translator.representation;

import java.util.List;
import java.util.Map;

import xtc.translator.translation.CppPrinter;
import xtc.tree.GNode;
import xtc.tree.Node;
import xtc.tree.Visitor;

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

		// get arguments here
		Node arguments = n.getNode(3);
		ArgumentVisitor argumentVisitor = new ArgumentVisitor(variableMap);
		argumentVisitor.dispatch(arguments);
				
		// check methodMap
		String name = n.getString(2);
		
		System.out.println("Args for " + name + argumentVisitor.getArguments());
		
		List<Method> methodList = methodMap.get(name);
		
		//TODO need to special-case for print and println
		
		if (methodList == null) {
			//TODO: throw exception
			System.out.println("COULDNT FIND METHOD " + name);
		} else {
			for (Method method : methodList) {
				
				// check for method with matching arguments
				if (method.getArguments()
						.compareTo(argumentVisitor.getArguments()) == 0) {
					System.out.println("Found matching method " + method);
					
					representation += method.getOverloadedIdentifier();
				}
				
			}
		}		
	}
	
	public void visitSelectionExpression(GNode n) {
		visit(n);
		
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
