package xtc.translator.representation;

import java.util.List;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

import xtc.translator.translation.CppPrinter;
import xtc.tree.GNode;
import xtc.tree.Node;
import xtc.tree.Visitor;

/**
 * Visitor subclass which visits a single java Class, and extracts all neccesary
 * data from it, to be processed later.
 * 
 */
public class ClassVisitor extends BaseVisitor implements Cloneable {

	private Node sourceNode;
	private String identifier;
	private List<String> extensions;
	private String extension;
	private ClassVisitor superClass;
	private List<String> modifiers;
	private List<ConstructorVisitor> constructorList;
	private List<FieldVisitor> fieldList;
	private List<MethodVisitor> methodList;
	private Map<String, String> implementationMap;
	private Map<String, List<Method>> overloadMap;
	private ArrayList<FieldVisitor> inheritedFields;
	private String packageName;
	private List<String> imports;

	private ArrayList<String> usedClasses;
	

	public ClassVisitor() {
		this.extension = "";
		this.extensions = new ArrayList<String>();
		this.modifiers = new ArrayList<String>();
		this.fieldList = new ArrayList<FieldVisitor>();
		this.constructorList = new ArrayList<ConstructorVisitor>();
		this.methodList = new ArrayList<MethodVisitor>();
		this.implementationMap = new HashMap<String, String>();
		this.superClass = null;

		this.usedClasses = new ArrayList<String>();
		this.imports = new ArrayList<String>();
		this.inheritedFields = new ArrayList<FieldVisitor>();
		this.overloadMap = new HashMap<String, List<Method>>();		
	}

	/**
	 * Assign identifier to the class and then recurse through nodes
	 * 
	 * @param n
	 */
	public void visitClassDeclaration(GNode n) {
		for (Object o : n) {

			// record the class name if o is a string
			if (o instanceof String) {
				this.identifier = (String) o;
			}

			// otherwise if o is a node, dispatch it
			if (GNode.test(o)) {
				dispatch(GNode.cast(o));
			}
		}

		// if after all of this ^^^ and we haven't assigned an extension, the
		// class extends object
		// not sure if this is the best place for it but it works
		if (extensions.size() == 0) {
			extension = "Object";
			extensions.add("Object");
		}
	}

	public void visitModifiers(GNode n) {
		for (int i = 0; i < n.size(); i++) {
			String modifier = n.getGeneric(0).getString(0);
			modifiers.add(modifier);
		}
	}

	/**
	 * This was a really inelegant solution to getting extensions I changed it a
	 * bit
	 */
	public void visitExtension(GNode n) {
		Node identifier = n.getGeneric(0).getGeneric(0);
		for (int i = 0; i < identifier.size(); i++) {
			extension = identifier.getString(i);
			extensions.add(identifier.getString(i));
			usedClasses.add(extension);
		}
	}

	// parse through the constructor
	public void visitConstructorDeclaration(GNode n) {
		ConstructorVisitor constructorVisitor = new ConstructorVisitor(n);
		constructorVisitor.dispatch(n);
		this.constructorList.add(constructorVisitor);
	}

	public void visitFieldDeclaration(GNode n) {
		FieldVisitor fieldVisitor = new FieldVisitor(this);
		fieldVisitor.dispatch(n);
		this.fieldList.add(fieldVisitor);
	}

	/**
	 * If a Method Declaration is visited, create a new MethodVisitor, and
	 * dispatch it to collect all method information from the node. Add it to
	 * the method list when finished.
	 * 
	 */
	public void visitMethodDeclaration(GNode n) {
		MethodVisitor methodVisitor = new MethodVisitor(n);
		methodVisitor.dispatch(n);

		this.methodList.add(methodVisitor);
		this.usedClasses.addAll(methodVisitor.getUsedClasses());
	}

	
	public void getVTable(CppPrinter cp) {
		getVTable(cp, this, this);
	}
		
	private void getVTable(CppPrinter cp, ClassVisitor classVisitor, ClassVisitor original) {
		if (classVisitor == null) {
			// do nothing
		} else {
			getVTable(cp, classVisitor.getSuperClass(), original);
			
            for (MethodVisitor m : classVisitor.getMethodList()) {
                if (!m.isOverride() || !m.isStatic())
                	cp.indent().p(m.getMethodPointer(original)).pln(";");
            }
		}
	}


	public ClassVisitor copy() {
		try {
			ClassVisitor clone = (ClassVisitor) this.clone();

			List<MethodVisitor> clonedMethodList = new ArrayList<MethodVisitor>();

			// Clone methodList
			for (MethodVisitor m : methodList) {
				clonedMethodList.add(m.copy());
			}

			clone.methodList = clonedMethodList;

			return clone;
		} catch (CloneNotSupportedException e) {
			e.printStackTrace();
			return null;
		}
	}

	
	
	@Override
	public String toString() {
		return "ClassVisitor [identifier=" + identifier + ", superClass="
				+ superClass + ", fieldList=" + fieldList
				+ ", implementationMap=" + implementationMap + ", packageName="
				+ packageName + "extends: " + this.extension + "]";
	}

	public String getIdentifier() {
		return identifier;
	}

	public void setIdentifier(String identifier) {
		this.identifier = identifier;
	}

	public String getExtension() {
		return extension;
	}

	public void setExtension(String extension) {
		this.extension = extension;
	}

	public List<String> getExtensions() {
		return extensions;
	}

	public void setExtensions(List<String> extensions) {
		this.extensions = extensions;
	}

	public ClassVisitor getSuperClass() {
		return superClass;
	}

	public void setSuperClass(ClassVisitor superClass) {
		this.superClass = superClass;
	}

	public List<String> getModifiers() {
		return modifiers;
	}

	public void setModifiers(List<String> modifiers) {
		this.modifiers = modifiers;
	}

	public List<ConstructorVisitor> getConstructorList() {
		return constructorList;
	}

	public void setConstructorList(List<ConstructorVisitor> constructorList) {
		this.constructorList = constructorList;
	}

	public List<FieldVisitor> getFieldList() {
		return fieldList;
	}

	public void setFieldList(List<FieldVisitor> fieldList) {
		this.fieldList = fieldList;
	}

	public List<MethodVisitor> getMethodList() {
		return methodList;
	}

	public void setMethodList(List<MethodVisitor> methodList) {
		this.methodList = methodList;
	}

	public Map<String, String> getImplementationMap() {
		return implementationMap;
	}

	public void setImplementationMap(Map<String, String> implementationMap) {
		this.implementationMap = implementationMap;
	}

	public ArrayList<String> getUsedClasses() {
		return usedClasses;
	}

	public String getPackageName() {
		return packageName;
	}

	public void setPackageName(String packageName) {
		this.packageName = packageName;
	}

	public List<String> getImports() {
		return imports;
	}

	public void setImports(List<String> imports) {
		this.imports = imports;
	}
	
	
	public String makeFullIdentifier() {
		return packageName + "." + identifier;
	}
	
	public String getFullIdentifier() {
		String full = packageName + "." + "__" + identifier;
		full = full.replace(".", "::");
		return full;
	}
	
	public String getFullIdentifierPointer() {
		String full = packageName + "." + identifier;
		full = full.replace(".", "::");
		return full;		
	}

	public Node getSourceNode() {
		return sourceNode;
	}

	public void setSourceNode(Node sourceNode) {
		this.sourceNode = sourceNode;
	}

	public Map<String, List<Method>> getOverloadMap() {
		return overloadMap;
	}

	public void setOverloadMap(Map<String, List<Method>> overloadMap) {
		this.overloadMap = overloadMap;
	}


	public ArrayList<FieldVisitor> getInheritedFields() {
		return inheritedFields;
	}

	public void setInheritedFields(ArrayList<FieldVisitor> inheritedFields) {
		this.inheritedFields = inheritedFields;
	}
	
	

}
