package xtc.translator.representation;

import xtc.translator.translation.CppPrinter;
import xtc.tree.Node;

/**
 * This is the abstract base class of all the implementation pieces
 * @author dave
 *
 */
public class IPiece implements CppPrintable{

	public Node baseNode;
	public String representation;
	
	public IPiece(Node baseNode, String representation) {
		this.baseNode = baseNode;
		this.representation = representation;
	}
	
	@Override
	public void printCpp(CppPrinter cp) {
		cp.p(representation);
	}
	
	
}
