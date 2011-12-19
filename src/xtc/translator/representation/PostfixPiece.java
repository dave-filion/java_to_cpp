package xtc.translator.representation;

import java.util.Map;

import xtc.translator.translation.CppPrinter;
import xtc.tree.Node;

public class PostfixPiece implements CppPrintable{

	public Node baseNode;
	private Map<String, String> variableMap;
	private String representation;
	
	public PostfixPiece(Node baseNode) {
		this.baseNode = baseNode;
		this.representation = "";
	}

	@Override
	public void printCpp(CppPrinter cp) {
		cp.p(representation).p("++");
	}


	public Node getBaseNode() {
		return baseNode;
	}


	public void setBaseNode(Node baseNode) {
		this.baseNode = baseNode;
	}


	public Map<String, String> getVariableMap() {
		return variableMap;
	}


	public void setVariableMap(Map<String, String> variableMap) {
		this.variableMap = variableMap;
	}


	public void processNode() {
		String id = baseNode.getNode(0).getString(0);
				
		String result = variableMap.get(id);
		
		if (result == null) {
			representation += "__this -> " + id;
		} else {
			representation += id;
		}	
	}
}
