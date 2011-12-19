package xtc.translator.representation;

import xtc.translator.translation.CppPrinter;

public class BlockPiece implements CppPrintable{

	public enum Type {
		OPEN, CLOSE
	}
	
	public Type type;
	
	public BlockPiece(String symbol) {
		if ("{".equals(symbol)) {
			this.type = Type.OPEN;
		} else if ("}".equals(symbol)) {
			this.type = Type.CLOSE;
		}
	}
	
	
	@Override
	public void printCpp(CppPrinter cp) {
		if (type == Type.OPEN) {
			cp.pln("{");
		} else if (type == Type.CLOSE) {
			cp.pln("}");
		} else {
			System.out.println("BLOCK PIECE ERROR");
		}
	}
}
