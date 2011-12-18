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
		
		// get caller
		Node caller = n.getNode(0);
		if (caller.getName().equals("PrimaryIdentifier")) {
			representation += caller.getString(0);
		}
		
		// recurse into caller for chained methods
		dispatch(caller);
		
		// get arguments here
		Node arguments = n.getNode(3);
		ArgumentVisitor argumentVisitor = new ArgumentVisitor(variableMap, methodMap);
		argumentVisitor.dispatch(arguments);
				
		// check methodMap
		String name = n.getString(2);		

		// Special case for print and println
		if (name.equals("println") || name.equals("print")) {
			
			representation += ".";
			representation += name;
			//TODO: make this more robust
			if (argumentVisitor.getArguments().getArguments().size() == 0) {
				representation += "()";
			} else {
				representation += "(" + argumentVisitor.getArguments().getArguments().get(0).value + ")";				
			}
			
		} else {
			
			List<Method> methodList = null;
			
			// if caller is self, use current method map
			// otherwise, need to get methodmap for caller
			if (caller.getName().equals("PrimaryIdentifier")) {
				String className = variableMap.get(caller.getString(0));
				System.out.println("Class for " + name + " is " + className + " and caller is " + caller);
				Map<String, List<Method>> mm = MethodMaps.getMethodMapForClass(className);

				// no method map for this class, means it is something we don't know what to do with
				if (mm == null) {
					System.out.println("DONT HAVE " + className);
					representation += ".";
					representation += name;
					
					//TODO do arguments
					representation += "DO_ARGS_HERE";
					return;
				} else {
					methodList = mm.get(name);
				}
			} else {
				// get method list for name
				methodList = methodMap.get(name);
			}

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
			
			if (caller.getName().equals("PrimaryIdentifier")) {
				representation += caller.getString(0);
			} else {
				representation += "__this";
			}

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
		
	public void visitSelectionExpression(GNode n) {		
		String primaryId = n.getNode(0).getString(0);
		String selection = n.getString(1);
		
		representation += primaryId + "::" + selection;
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
