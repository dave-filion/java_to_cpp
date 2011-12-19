package xtc.translator.translation;

import java.io.FileWriter;
import java.io.IOException;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;

import xtc.translator.representation.ClassVisitor;
import xtc.translator.representation.ConstructorVisitor;
import xtc.translator.representation.CppPrintable;
import xtc.translator.representation.FieldVisitor;
import xtc.translator.representation.ImplementationVisitor;
import xtc.translator.representation.MethodVisitor;

public class PrintHandler {

	private List<ClassVisitor> classVisitors;
	private String classPath;
	
	public PrintHandler(List<ClassVisitor> classVisitors, String classPath) {
		this.classVisitors = classVisitors;
		this.classPath = classPath;
	}
		
	public void printAllHeaders() throws IOException {
		
		// Make new printer printing to outfile
		CppPrinter cp = new CppPrinter(new FileWriter("out/" + classPath + ".h"));

		//Pragma once
		cp.pln("#pragma once");

		// Imports
		this.printStandardImports(cp);

		cp.pln().pln("using namespace java::lang;");
		
		// Forward declarations
		for (ClassVisitor classVisitor : classVisitors) {
						
			//TODO : put namespace stuff in method
			String[] namespaces = splitPackageName(classVisitor.getPackageName());
			// Open namespaces
			for (String name : namespaces) {
				cp.p("namespace ").p(name).pln("{");		
				cp.incr();
				cp.pln();
			}
			
			// Declare data model.
			cp.p("struct ").p(prepend(classVisitor.getIdentifier())).pln(";");
			
			// Declare Vtable
			cp.p("struct ").p(vtDec(classVisitor.getIdentifier())).pln(";");
			
			// Typedef definition
			cp.p("typedef ").p(smartPtrTo(classVisitor.getIdentifier())).p(" ").p(classVisitor.getIdentifier()).pln(";");

			// Close namespaces
			for (String name : namespaces) {
				cp.decr();
				cp.pln("}");
			}
		}
		
		for (ClassVisitor classVisitor : classVisitors) {
			
			
			String[] namespaces = splitPackageName(classVisitor.getPackageName());					
			// Open namespaces
			for (String name : namespaces) {
				cp.p("namespace ").p(name).pln("{");		
				cp.incr();
				cp.pln();
			}
						
			// Print data structure
			cp.pln();
			this.printDataStructure(cp, classVisitor);
			
			// Print vTable 
			cp.pln();
			this.printVTable(cp, classVisitor);
			
			// Close namespaces
			for (String name : namespaces) {
				cp.decr();
				cp.pln("}");
			}
			
			// Flush buffers
			cp.flush();
		}
		
	}
	
	public void printAllImplementations() throws IOException {
		
		CppPrinter cp = new CppPrinter(new FileWriter("out/" + classPath + ".cc"));

		// Print standard imports
		printStandardImports(cp);
		cp.pln("#include \"System.h\"");
		// Include header
		cp.pln("#include \"" + classPath + ".h\"").pln();
		
		// Print using declarations
		for (ClassVisitor classVisitor : classVisitors) {
			cp.p("using ").p(classVisitor.getFullIdentifier()).pln(";");
			cp.p("using ").p(classVisitor.getFullIdentifierPointer()).pln(";");
		}
		cp.pln();
		
		for (ClassVisitor classVisitor : classVisitors) {
						
			String[] namespaces = splitPackageName(classVisitor.getPackageName());
			// Open namespaces
			for (String name : namespaces) {
				cp.p("namespace ").p(name).pln("{");		
				cp.incr();
				cp.pln();
			}

			for (ConstructorVisitor con : classVisitor.getConstructorList()) {
				// Print constructor
				// TODO: FIX CONSTRUCOR PRINTING
				cp.p(prepend(classVisitor.getIdentifier())).p("::")
						.p(prepend(classVisitor.getIdentifier()));
				cp.p("() : __vptr(&__vtable)").pln();
				ImplementationVisitor iv = con.getImplementationVisitor();
				if (iv == null) {
					System.out.println("NO CONSTRUCTOR FOR " + classVisitor);
				} else {
					for (CppPrintable p : iv.getCppPrintList()) {
						p.printCpp(cp);
					}					
				}
			}
			

			// the fields
			for ( FieldVisitor f : classVisitor.getFieldList()) {
				if (f.isStatic)
					cp.p(f.forImplementation()).pln();
			}

			
			// Print destructor
			cp.p("void ").p(prepend(classVisitor.getIdentifier())).p("::").p("__delete");
			cp.p("(").p(prepend(classVisitor.getIdentifier())).p("*").p(" __this)").pln("{");
			cp.pln("delete __this;");
			cp.pln("}");
			cp.pln();
			
			// Print all methods
			for (MethodVisitor m : classVisitor.getMethodList()) {
				// Print method signature
				// print return type
				cp.p(m.getReturnType()).p(" ");
				
				// print de-scoped method identifier
				cp.p(prepend(classVisitor.getIdentifier())).p("::").p(m.getIdentifier());

				if (m.getIdentifier().equals("main")) {
					// Special case main to have no arguments
					cp.p("()").pln();

				} else {
					
					if (!m.isStatic()){
					// print parameters with implicit this
					cp.p(" (").p(classVisitor.getIdentifier() + " __this")
							.p(m.parametersToString()).p(")").pln();
					} else {
						cp.p(" (").p(m.parametersToStringNoComma()).p(")").pln();
					}
				}

				for (CppPrintable p : m.getImplementationVisitor().getCppPrintList()) {
					p.printCpp(cp);
				}
				
				cp.pln();
			}
			
			// Print internal class accessor
			cp.p("Class ").p(prepend(classVisitor.getIdentifier()));
			cp.p("::__class() {").pln();
			cp.p("static Class k = new __Class(__rt::literal(");
			//TODO: print full name here
			String fullName = "\"" + classVisitor.getPackageName() + "." + classVisitor.getIdentifier() + "\"";
			cp.p(fullName);
			//print superclass
			cp.p("), ").p(prepend(classVisitor.getSuperClass().getIdentifier()));
			cp.p("::__class());").pln();
			cp.p("return k;").pln();
			cp.p("}").pln();
			
			// Invoke vtable constructor
			cp.p(vtDec(classVisitor.getIdentifier()));
			cp.p(" ").p(prepend(classVisitor.getIdentifier()));
			cp.p("::__vtable;").pln();
			
			// Close namespaces
			for (String name : namespaces) {
				cp.decr();
				cp.pln("}");
			}

						
			cp.flush();
		}
		
	}
	
	public void printMainFile(String mainClass) throws IOException {
		
		CppPrinter cp = new CppPrinter(new FileWriter("out/main.cc"));
		ClassVisitor main = null;
		
		//TODO: this is inefficient, but i don't know how else to do it.
		for (ClassVisitor cv : classVisitors) {
			if ((cv.getIdentifier() + ".java").equals(mainClass)) {
				main = cv;
			}
		}
		
		// Print standard imports
		printStandardImports(cp);

		//TODO: Include all headers
		cp.p("#include \"").p(classPath + ".h").p("\"").pln();
		
		cp.p("int main(){").pln();
		
		//Declare new object
		//TODO: need to instantiate single object
		String[] namespaces = splitPackageName(main.getPackageName());
		for (String namespace : namespaces ) {
			cp.p(namespace).p("::");
		}
		//TODO: this needs to change as well
		cp.p(prepend(main.getIdentifier())).p("::main()").p(";").pln();
		
		cp.p("}").pln();
		
		cp.flush();
		
	}
	
	private void printDataStructure(CppPrinter cp, ClassVisitor classVisitor) {
		cp.p("struct ").p(prepend(classVisitor.getIdentifier())).pln("{");
		cp.incr();
		// vptr
		cp.p(vtDec(classVisitor.getIdentifier())).p("*").p(" ").p("__vptr").pln(";");
		
		// the constructor
		for (ConstructorVisitor con : classVisitor.getConstructorList()) {
			cp.p(con.getSignature()).pln(";");
		}
		
		// the fields
		for ( FieldVisitor f : classVisitor.getFieldList()) {
			cp.p(f.forHeader()).pln();
		}
		
		// the destructor
		cp.p("static void __delete(").p(prepend(classVisitor.getIdentifier()) + "*").p(")").pln(";");
		
		for ( MethodVisitor m : classVisitor.getMethodList() ) {
			cp.p(m.getSignature(classVisitor)).pln(";");
		}
		
		// __class function
		cp.p("static Class __class()").pln(";");
		
		// the vtable for the object
		cp.p("static ").p(vtDec(classVisitor.getIdentifier())).p(" ").p("__vtable").pln(";");
		cp.decr();
		cp.pln("};");
	}
	
	private void printStandardImports(CppPrinter cp) {
		cp.pln("#include <stdint.h>");
		cp.pln("#include <string>");
		cp.pln("#include <iostream>");
		cp.pln("#include <cstring>");
		cp.pln("#include \"ptr.h\"");
		cp.pln("#include \"java_lang.h\"");
	}
		
	private void printVTable(CppPrinter cp, ClassVisitor classVisitor) {
		cp.p("struct ").p(vtDec(classVisitor.getIdentifier())).pln(" {");
		cp.incr();
		cp.pln();
		
		// Print isa method
		cp.p("Class __isa;").pln();
		
		// Print delete method
		cp.p("void ").p("(*__delete)(").p(prepend(classVisitor.getIdentifier()));
		cp.p("*);").pln();
		
		
		// Recurse over all methods (even inherited)
		printVTable(cp, classVisitor, classVisitor);
		
		// Generate constructor
		cp.pln();
		cp.pln(vtDec(classVisitor.getIdentifier()) + "()");
		cp.pln(": __isa(" + prepend(classVisitor.getIdentifier()) + "::__class()),");
		cp.p("__delete(&" + prepend(classVisitor.getIdentifier()) + "::__delete)");
		
		printVTableConstructor(cp, classVisitor, classVisitor);
		
		cp.pln("{ }");
		
		
		cp.decr();
		cp.pln("};");
	}
	
	private void printVTable(CppPrinter cp, ClassVisitor classVisitor, ClassVisitor original) {
		if (classVisitor == null) {
			// do nothing
		} else {
			printVTable(cp, classVisitor.getSuperClass(), original);
            for (MethodVisitor m : classVisitor.getMethodList()) {
                if (!m.isOverride() && !m.isStatic())
                	cp.p(m.getMethodPointer(original)).pln(";");
            }
		}
	}

	private static void printVTableConstructor(CppPrinter cp,
			ClassVisitor cv,
			ClassVisitor original) {

		if (cv == null) {
			// do nothing
		} else {
			printVTableConstructor(cp, cv.getSuperClass(), original);

			for (int i = 0; i < cv.getMethodList().size(); i++) {

				MethodVisitor m = cv.getMethodList().get(i);
				
				// exclude main
				if (!m.isOverride() && !m.isStatic()) {
					// comma from previous line
					cp.pln(",");

					String classPointer = original.getImplementationMap().get(m.getIdentifier());

					if (!classPointer.equals(original.getIdentifier())) {
						// If class pointer is not class, we must cast it
						cp.p(cast(m, original, classPointer));
					} else {
						// otherwise, reference original classes implementation
						cp.p(m.getIdentifier()
								+ openParen()
								+ reference(scope(classPointer,
										m.getIdentifier())) + closeParen());

					}
				}
			}
		}
	}

	private String periodToColons(String word) {
		return word.replace(".", "::");
	}
	
	private String getFullClassName(ClassVisitor classVisitor) {
		return classVisitor.getPackageName() + "." + classVisitor.getIdentifier();
	}
	
	private String getFullImportName(ClassVisitor classVisitor, String importName) {
		return classVisitor.getPackageName() + "::" + importName;
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
      return "__rt::Ptr<" + prepend(object) + ">";
    }

	private static String cast(MethodVisitor m, ClassVisitor original,
			String classPointer) {
		String out = "";
		out += m.getIdentifier() + openParen() + openParen()
				+ m.getReturnType() + "(*)" + openParen()
				+ original.getIdentifier() + ",";
		// Include original parameters
		for (Map<String, String> p : m.getParameters()) {
			out += p.get("type") + ",";
		}

		out = removeLastComma(out);

		out += closeParen() + closeParen()
				+ reference(scope(classPointer, m.getIdentifier()))
				+ closeParen();

		return out;
	}
	
    protected static String removeLastComma(String string){
        // remove last comma
        return string.substring(0, string.length() - 1);

    }

}
