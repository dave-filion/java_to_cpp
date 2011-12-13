package xtc.translator.representation;

import java.util.Map;

import xtc.translator.translation.CppPrinter;
import xtc.tree.Node;

public class CallExpressionPiece implements CppPrintable{

	public Map methodMap;
	public Node baseNode;
	public String representation;
	
	public CallExpressionPiece(Node baseNode) {
		this.baseNode = baseNode;
		this.methodMap = null;
	}

	public void setMethodMap(Map methodMap) {
		this.methodMap = methodMap;
	}

	public void processNode(){
		System.out.println("Processing " + baseNode);

		Node arguments = baseNode.getNode(3);

		// get arguments here
		
		// check methodMap
		String name = baseNode.getString(2);
		
		// TODO : replace this
		representation = name;
	}
	
	@Override
	public void printCpp(CppPrinter cp) {
		cp.p(representation);
	}

}
