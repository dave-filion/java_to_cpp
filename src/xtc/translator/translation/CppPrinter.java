package xtc.translator.translation;

import java.io.Writer;

import xtc.translator.representation.ClassVisitor;
import xtc.tree.Printer;

public class CppPrinter extends Printer{

	public CppPrinter(Writer out) {
		super(out);
	}
	
	public void writeHeader(ClassVisitor classVisitor) {
		
		pln(classVisitor.toString());
		
	}
	
	

}
