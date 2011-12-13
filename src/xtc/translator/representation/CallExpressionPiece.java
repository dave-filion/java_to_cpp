package xtc.translator.representation;

import xtc.translator.translation.CppPrinter;
import xtc.tree.Node;

public class CallExpressionPiece implements CppPrintable{

	public Node baseNode;
	
	public CallExpressionPiece(Node baseNode) {
		this.baseNode = baseNode;
	}
	
	@Override
	public void printCpp(CppPrinter cp) {
		cp.pln(baseNode.toString());
		
	}

}
