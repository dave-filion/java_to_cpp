package xtc.translator.representation;
import java.util.ArrayList;

public class SourceObject extends ClassVisitor{

	public SourceObject(){
		super();
		this.setIdentifier("Object");
		
		
		// Unfortunately, this needs to be hard-coded, as it explicitly creates 
		// MethodVisitor for java.lang.Object
		MethodVisitor hashCode = new MethodVisitor("hashCode", "int32_t");
		MethodVisitor equals   = new MethodVisitor("equals", "bool");
		MethodVisitor getClass = new MethodVisitor("getClass", "Class");
		MethodVisitor toString = new MethodVisitor("toString", "String");
		
		equals.addParameter("Object", "object");
		
		this.setMethodList(new ArrayList<MethodVisitor>());
		this.getMethodList().add(hashCode);
		this.getMethodList().add(equals);
		this.getMethodList().add(getClass);
		this.getMethodList().add(toString);
				
		this.setSuperClass(null);
	}
	
	public String getFullIdentifier(){
		return "java::lang::__Object";
	}

	public String getFullIdentifierPointer(){
		return "java::lang::Object";
	}

	
}
