package xtc.translator.translation;

import java.util.List;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

import java.io.File;
import java.io.IOException;

import xtc.translator.representation.Argument;
import xtc.translator.representation.CallExpressionPiece;
import xtc.translator.representation.ClassVisitor;
import xtc.translator.representation.CompilationUnit;
import xtc.translator.representation.ConstructorVisitor;
import xtc.translator.representation.CppPrintable;
import xtc.translator.representation.FieldMap;
import xtc.translator.representation.FieldVisitor;
import xtc.translator.representation.IPiece;
import xtc.translator.representation.ImplementationVisitor;
import xtc.translator.representation.Method;
import xtc.translator.representation.MethodMaps;
import xtc.translator.representation.MethodVisitor;
import xtc.translator.representation.PostfixPiece;
import xtc.translator.representation.SourceObject;
import xtc.translator.representation.VariableVisitor;

import xtc.parser.ParseException;
import xtc.tree.Node;
import xtc.tree.Visitor;

/**
 * Top level class encapsulating all underlying data extracted from relavent
 * java source. Should act as a starting point for subsequent c++ translation.
 */
public class Collector extends Visitor {

	public ArrayList<File> packDirs;
	public ArrayList<String> imports;
	public ArrayList<String> packages;
	public List<ClassVisitor> classes;
	public ClassVisitor mainClass;
	public ArrayList<String> typeList;

	/**
	 * a list of classes that are used this should come from
	 * visitQualifiedIdentifiers in ImplementationVisitor
	 */
	public ArrayList<String> usedClasses;
	public HashMap<String, String> usedClassesDict;

	public Collector(List<CompilationUnit> compilationUnits) {

		this.imports = new ArrayList<String>();
		this.classes = new ArrayList<ClassVisitor>();

		for (CompilationUnit compilationUnit : compilationUnits) {
			for (ClassVisitor classVisitor : compilationUnit.getClassVisitors())
				this.classes.add(classVisitor);
		}

		this.packages = new ArrayList<String>();
		this.mainClass = null;
		this.packDirs = new ArrayList<File>();
		this.typeList = new ArrayList<String>();

	}

	/**
	 * Main method to begin collection
	 * 
	 */
	public void collect() throws IOException, ParseException {	
		// sort classes
		this.sortClasses();
		
		// Make simple type list for consulting
		makeTypeList();
		
		// assign super classes to classVisitors
		this.assignSuperClass();
		
		// get overloaded methods
		this.generateMethodOverload();

		// Use assigned super classes to create implementation
		// map of all methods in class hierarchy (vtable precursor)
		this.createImplementationMap();

		// sort the list of classes for printing
		// this.sort();

		// Create variable map for each method
		this.createVariableMaps();

		// Process method calls for proper overloading
		this.processCallExpression();
		
		// Process postfix pieces, which require special rules
		this.processPostfixPieces();
		
		// Process general pieces, which require certain post processing
		this.processPieces();
		
		// Inherit relevant fields in subclasses
		this.inheritFields();
		
		// HACKY
		// Initialize inherited values in subclasses constructors
		this.initilizeInheritedValues();
	}
	
	private void inheritFields() {
		for (ClassVisitor cv : classes) {
			inheritFields(cv.getSuperClass(), cv);
		}
	}
	
	// Inherits all fields of superclasses
	private void inheritFields(ClassVisitor current, ClassVisitor original) {
		if (current == null) {
			return;
		} else {
			inheritFields(current.getSuperClass(), original);
			for (FieldVisitor f : current.getFieldList()) {
				if (!f.isStatic) {
					original.getFieldList().add(f);
					original.getInheritedFields().add(f);
				}
			}
		}
				
	}
	
	private void sortClasses() {
		//TODO implement real sort, this one is a hack.
		ClassVisitor prev = null;
		ClassVisitor current = null;
		ArrayList<ClassVisitor> ordered = new ArrayList<ClassVisitor>();
		
		for (ClassVisitor c : classes) {
			current = c;
			if (prev != null && current.getIdentifier().equals(prev.getExtension())) {
				int i1 = classes.indexOf(prev);
				int i2 = classes.indexOf(current);
				ordered.add(i1, current);
				ordered.add(i2, prev);
			}	
			prev = current;
		}
		classes = ordered;	
	}
	
	
	/**
	 * Entry point to recursive method to assign superclasses.
	 */
	private void assignSuperClass() {
		for (ClassVisitor cv : classes) {
			assignSuperClass(cv);
		}
	}
	
	/**
	 * Recursively assigns superclasses to classes based on extension string.
	 * @param current
	 */
	private void assignSuperClass(ClassVisitor current) {
		
		if (current == null) {
			return;
		} else {		
			if (current.getExtension().equals("Object")) {
				current.setSuperClass(new SourceObject());
			} else {
				for (ClassVisitor cv : classes) {
					if (current.getExtension().equals(cv.getIdentifier())) {
						current.setSuperClass(cv.copy());
					}
				}
			}
			assignSuperClass(current.getSuperClass());
		}		
	}

	private void createImplementationMap() {
		for (ClassVisitor classVisitor : classes) {
			createImplementationMap(classVisitor, classVisitor);
		}
	}

	/**
	 * Recursive Helper method, recurses through class hierarchy, from Object
	 * downward, and updates the implementation table of the original
	 * classVisitor with the latest overridden implementation of a method with a
	 * particular identifier.
	 * 
	 * @param currentClass
	 * @param original
	 */
	private void createImplementationMap(ClassVisitor currentClass,
			ClassVisitor original) {
		if (currentClass == null) {
			return;
		} else {
			createImplementationMap(currentClass.getSuperClass(), original);

			for (MethodVisitor m : currentClass.getMethodList()) {
				// If key is already in map, it means newer method is overriding
				// older one
				if (original.getImplementationMap().containsKey(
						m.getIdentifier())) {
					m.setOverride(true);
				}
				original.getImplementationMap().put(m.getIdentifier(),
						currentClass.getFullIdentifier());
			}
		}
	}


	private void generateMethodOverload() {

		for (ClassVisitor classVisitor : classes) {
			ClassVisitor currentClass = classVisitor;
						
			while (currentClass != null) {
				for (MethodVisitor m : currentClass.getMethodList()) {
					Method method = new Method(m.getIdentifier(),
							m.getReturnType());

					// set if method is static
					method.isStatic = m.isStatic();

					List<Argument> paramTypes = new ArrayList<Argument>();

					for (Map<String, String> param : m.getParameters()) {
						paramTypes.add(new Argument(param.get("type"), null));
					}

					method.getArguments().setArguments(paramTypes);

					method.generateOverloadedIdentifier();

					// if method is already there, add it to the list
					if (classVisitor.getOverloadMap().containsKey(method.getIdentifier())) {
						// Check to see if method is already there
						boolean add = true;
						
						for (Method checkMethod : classVisitor.getOverloadMap().get(method.getIdentifier())) {
							// If specific method is already there, then a subclass must implement it,
							// so don't add it to the list.
							if (checkMethod.getOverloadedIdentifier().equals(method.getOverloadedIdentifier())) {
								add = false;
							} 
						}						
						
						if (add){
							classVisitor.getOverloadMap().get(method.getIdentifier()).add(method);						
						}
						
					} else {
						List<Method> methodList = new ArrayList<Method>();
						methodList.add(method);
						classVisitor.getOverloadMap().put(method.getIdentifier(), methodList);
					}

					if (!m.getIdentifier().equals("main"))
						if (!currentClass.getIdentifier().equals("Object"))
							m.setIdentifier(method.getOverloadedIdentifier());
				}
				currentClass = currentClass.getSuperClass();
			}

			MethodMaps.addMethodMapForClass(classVisitor.getIdentifier(),
					classVisitor.getOverloadMap());

		}

		// do source object
		SourceObject source = new SourceObject();

		for (MethodVisitor m : source.getMethodList()) {
			Method method = new Method(m.getIdentifier(), m.getReturnType());

			// set if method is static
			method.isStatic = m.isStatic();

			List<Argument> paramTypes = new ArrayList<Argument>();

			for (Map<String, String> param : m.getParameters()) {
				paramTypes.add(new Argument(param.get("type"), null));
			}

			method.getArguments().setArguments(paramTypes);

			// if method is already there, add it to the list
			if (source.getOverloadMap().containsKey(method.getIdentifier())) {
				source.getOverloadMap().get(method.getIdentifier()).add(method);
			} else {
				List<Method> methodList = new ArrayList<Method>();
				methodList.add(method);
				source.getOverloadMap().put(method.getIdentifier(), methodList);
			}

			if (!m.getIdentifier().equals("main"))
				m.setIdentifier(method.getIdentifier());
		}

		MethodMaps.addMethodMapForClass(source.getIdentifier(),
				source.getOverloadMap());

	}

	private void createVariableMaps() {

		for (ClassVisitor classVisitor : classes) {

			for (MethodVisitor m : classVisitor.getMethodList()) {

				VariableVisitor vv = new VariableVisitor();

				// Put field variables into it
				for (FieldVisitor fv : classVisitor.getFieldList()) {
					vv.getVariableMap().put(fv.getVariableName(), fv.getVariableType());
					FieldMap.fieldmap.put(fv.getVariableName(), fv);
				}
				

				// get the rest of the variables in method scope
				vv.dispatch(m.getBaseNode());

				// set methods variable map
				m.setVariableMap(vv.getVariableMap());
			}
		}
	}

	private void processCallExpression() {

		for (ClassVisitor classVisitor : classes) {

			for (MethodVisitor m : classVisitor.getMethodList()) {
				ImplementationVisitor i = m.getImplementationVisitor();
				
				if (i == null) {
					System.out.println("No implementationVisitor for " + m.getIdentifier());
				} else {
					for (CallExpressionPiece c : m.getImplementationVisitor().getCallExpressions()) {
						c.setMethodMap(classVisitor.getOverloadMap());
						c.setVariableMap(m.getVariableMap());
						c.processNode();
					}
				}
			}
		}
	}
	
	private void processPostfixPieces() {
		
		for (ClassVisitor classVisitor : classes) {
			for (MethodVisitor m : classVisitor.getMethodList()) {
				ImplementationVisitor i = m.getImplementationVisitor();
				
				if (i == null) {
					System.out.println("No implementationVisitor for " + m.getIdentifier());
				} else {
					for (PostfixPiece c : m.getImplementationVisitor().getPostfixPieces()) {
						c.setVariableMap(m.getImplementationVisitor().getVarTypeDict());
						c.processNode();
					}
				}
			}
		}		
	}
	
	private void initilizeInheritedValues() {
		for (ClassVisitor classVisitor : classes) {
			for (ConstructorVisitor con : classVisitor.getConstructorList()) {
				List<CppPrintable> pieces = con.getImplementationVisitor().getCppPrintList();
				pieces.remove(pieces.size() - 1); //remove last brace;
				for (FieldVisitor fv : classVisitor.getInheritedFields()) {
					pieces.add(new IPiece(null, fv.variableName + " =  0;"));
				}
				
				pieces.add(new IPiece(null, "}\n"));
				con.getImplementationVisitor().setCppPrintList(pieces);
			}
		}
	}
	
	private void processPieces() {
		
		for (ClassVisitor classVisitor: classes) {
			for (MethodVisitor m : classVisitor.getMethodList()) {
				ImplementationVisitor i = m.getImplementationVisitor();
				
				if (i == null) {
					System.out.println("No implementationVisitor for " + m.getIdentifier());
				} else {					
					for (IPiece p : i.getIpieces()) {		
						
						Node n = p.getBaseNode();
						if (n.getName().equals("PrimaryIdentifier")) {
							FieldVisitor fv = FieldMap.fieldmap.get(p.getRepresentation());
							if (fv != null)
								p.setRepresentation("__" + fv.parent.getIdentifier() + "::" + p.getRepresentation());
						}					
						
						if (n.getName().equals("SelectionExpression")) {
							String primaryId = n.getNode(0).getString(0);							
							String field = n.getString(1);
							
							if (typeList.contains(primaryId))
								p.setRepresentation("(__" + primaryId + "::" + field + ")");							
							else
								p.setRepresentation("(" + primaryId + "->" + field + ")");
						}
						
						if (n.getName().equals("InstanceOfExpression")) {
							String primary = n.getNode(0).getString(0);
							String other = n.getNode(1).getNode(0).getString(0);
							p.setRepresentation(primary + " -> __vptr -> getClass( " + primary + ") -> __vptr -> isInstance( " + primary + " -> __vptr -> getClass(), new __" + other + " ())" );
						}
						
						if (n.getName().equals("AdditiveExpression")) {
							
							String real = concat("", n, null);;							
							real = real.replaceAll("\"", "");
							real = "\"" + real + "\"";
							real = "__rt::literal(" + real + ")";
							p.setRepresentation(real);

						}
						
						if (p.representation.contains("short")) {
							p.setRepresentation(p.getRepresentation().replace("short", "int"));
						}
					}
				}
			}
		}
	}
	
	public static String concat(String result, Node n, Map<String, String> variableMap) {
		
		if (n.getNode(0).getName().equals("StringLiteral")) {
			String first = n.getNode(0).getString(0);
			String second = null;
			// special concatination of characters
			if (n.getNode(2).getName().equals("CharacterLiteral")){
				second = n.getNode(2).getString(0);
				second = second.replace("'", "\"");
			}else {
				second = n.getNode(2).getString(0);
			}
			return first + second;
		} else if(n.getNode(0).getName().equals("PrimaryIdentifier")){
			String first = variableMap.get(n.getNode(0).getString(0));
			String second = n.getNode(2).getString(0);
			return first + second;			
		} else {
			result = concat(result, n.getNode(0), variableMap);
			result += n.getNode(2).getString(0);
			return result;
		}
		
	}
	
	public void makeTypeList() {
		for (ClassVisitor cv : classes) {
			typeList.add(cv.getIdentifier());
		}
		
		typeList.add("Object");
		typeList.add("String");
		typeList.add("Class");
	}
	
}
