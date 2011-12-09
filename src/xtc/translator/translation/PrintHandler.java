package xtc.translator.translation;

import java.io.FileWriter;
import java.io.IOException;
import java.util.List;

import xtc.translator.representation.ClassVisitor;

public class PrintHandler {

	private List<ClassVisitor> classVisitors;
	
	public PrintHandler(List<ClassVisitor> classVisitors) {
		this.classVisitors = classVisitors;
	}
	
	public void printAllHeaders() throws IOException {
		
		for (ClassVisitor classVisitor : classVisitors) {
			
			// Make new printer printing to outfile
			CppPrinter cp = new CppPrinter(new FileWriter("out/" + getFullClassName(classVisitor) + ".h"));
			
			// Imports
			System.out.println(classVisitor.getImports());
			for (String imp : classVisitor.getImports()) {
				cp.p("#include ").p("\"").p(imp + ".h").pln("\"");
			}
			
			String[] namespaces = splitPackageName(classVisitor.getPackageName());
						
			// Open namespaces
			for (String name : namespaces) {
				cp.p("namespace ").p(name).pln("{");		
			}
			
			// Declare data model.
			cp.p("struct ").p(prepend(classVisitor.getIdentifier())).pln(";");
			
			// Declare Vtable
			cp.p("struct ").p(prepend(vtDec(classVisitor.getIdentifier()))).pln(";");
			
			// Typedef definition
			cp.p("typedef ").p(smartPtrTo(classVisitor.getIdentifier())).p(" ").p(classVisitor.getIdentifier()).pln(";");
			
			// MANY MANY THINGS GO HERE
			//
			
			// Close namespaces
			for (String name : namespaces) {
				cp.indentLess().pln("}");
			}
			
			// Flush buffers
			cp.flush();
		}
		
	}
	
	public void printAllImplementations() throws IOException {
		
		for (ClassVisitor classVisitor : classVisitors) {
			
			CppPrinter cppPrinter = new CppPrinter(new FileWriter("out/" + getFullClassName(classVisitor) + ".cc"));
			
			
			
			cppPrinter.flush();
		}
		
	}
	
	private String periodToColons(String word) {
		return word.replace(".", "::");
	}
	
	private String getFullClassName(ClassVisitor classVisitor) {
		return classVisitor.getPackageName() + "." + classVisitor.getIdentifier();
	}
	
	private String[] splitPackageName(String packageName) {
		String[] pieces = packageName.split("[.]");
		
		return pieces;
	}
	
	protected static String __isa() {
        return "Class __isa";
    }

    protected static String __class() {
        return "static Class __class()";
    }
        
    protected static String scope(String left, String right){
        return left + "::" + right;
    }
    
    protected static String reference(String object){
        return "&" + object;
    }

    protected static String prepend(String out) {
        return "__" + out;
    }

    protected static String vtDec(String identifier) {
        return prepend(identifier) + "_VT";
    }

    protected static String ptrTo(String object) {
        return object + star();
    }

    protected static String newLine() {
        return "\n";
    }

    protected static String space() {
        return " ";
    }

    protected static String end() {
        return ";";
    }

    protected static String openBlock() {
        return "{";
    }

    protected static String closeBlock() {
        return "}";
    }

    protected static String openParen() {
        return "(";
    }

    protected static String closeParen() {
        return ")";
    }

    protected static String star() {
        return "*";
    }
    
    protected static String smartPtrTo(String object) {
      return "__rt::Ptr<" + object + ">";
    }


}
