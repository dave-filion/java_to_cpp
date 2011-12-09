package xtc.translator.translation;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import xtc.translator.representation.Method;

import xtc.tree.GNode;
import xtc.tree.Node;
import xtc.tree.Visitor;

public class OverloadVisitor extends Visitor {

	public List<Map> overloadedMethods;
	public List<Map<String, Method>> methodMap;
	public Method currentMethod;
	
	public OverloadVisitor(){
		overloadedMethods = new ArrayList<Map>();
		methodMap = new ArrayList<Map<String, Method>>();
		currentMethod = null;
	}
	
	public void visitMethodDeclaration(GNode n){		
		Node returnType = n.getNode(2);
		
		String returnString;
		if (returnType.getName().equals("VoidType"))
			returnString = "void";
		else
			returnString = returnType.getString(0);
		
		String identifier = n.getString(3);

		currentMethod = new Method(identifier, returnString);
		
		Node parameters = n.getNode(4);
		dispatch(parameters);
		
		Map<String, Method> map = new HashMap<String, Method>();
		map.put(identifier, currentMethod);
		
		methodMap.add(map);
		
		currentMethod.generateOverloadedIdentifier();
	}
		
	public void visitFormalParameter(GNode n) {

		for (Object o : n) {

			// FIXME: This needs to be able to handle qualified/primitive type
			// plus Dimension if it is an array
			if (o instanceof Node) {
				if (((Node) o).getName() == "Type") {
					Node innerType = (Node) ((Node) o).get(0);
					String type = (String) innerType.get(0);
					currentMethod.argumentTypes.add(type);
				}
			}
		}

	}	
	
	/**
	 * Catch-all visit.
	 */
    public void visit(Node n) {
        for (Object o : n)
            if (o instanceof Node)
                dispatch((Node) o);
    }


}
