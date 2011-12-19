package xtc.translator.representation;

import xtc.tree.Node;
import xtc.tree.GNode;

public class FieldVisitor extends BaseVisitor {

	public String headerRep;
	public String implementationRep;
	public String variableName;
	public String variableType;
	public ClassVisitor parent;
	public boolean isStatic;
	
	public FieldVisitor(ClassVisitor parent) {
		headerRep = "";
		implementationRep = "";
		this.parent = parent;
		isStatic = false;
	}
	
	public void visitFieldDeclaration(GNode n) {
		visit(n);
		headerRep += ";";
		implementationRep += ";";
	}
	
	public void visitModifier(GNode n) {
		String modifier = n.getString(0);
		
		if (modifier.equals("static")) {
			isStatic = true;
		}
		
		if (!modifier.equals("public") && !modifier.equals("final"))	
			headerRep += n.getString(0) + " ";
	}
		
	public void visitType(GNode n) {
		Node type = n.getNode(0);
		String name = type.getString(0);
		variableType = name;
		headerRep += name + " ";
		implementationRep += name + " ";
	}
	
	public void visitDeclarator(GNode n) {
		String name = n.getString(0);
		variableName = name;
		headerRep += name;
		implementationRep += "__" + parent.getIdentifier() + "::" + name;
		
		if (n.getNode(2) != null) {
			implementationRep += " = ";
		}
		
		visit(n);
	}
	
	public void visitNewClassExpression(GNode n) {
		implementationRep += "new ";
		implementationRep += "__";

		String identifier = n.getNode(2).getString(0);

		implementationRep += identifier;
		implementationRep += "( ";

		dispatch(n.getNode(3));

		implementationRep += " )";
	}
	
	public String forHeader() {
		return headerRep;
	}
	
	public String forImplementation() {
		return implementationRep;
	}

	public String getVariableName() {
		return variableName;
	}

	public void setVariableName(String variableName) {
		this.variableName = variableName;
	}

	public String getVariableType() {
		return variableType;
	}

	public void setVariableType(String variableType) {
		this.variableType = variableType;
	}

	@Override
	public String toString() {
		return variableName;
	}

	


}