package xtc.translator.representation;
import xtc.tree.Visitor;
import xtc.tree.Node;
import xtc.tree.GNode;

import java.util.*;

/**
 * A visitor that stores relevant information for a particular method which will
 * later be used to generate C++ vtable information
 */
public class MethodVisitor extends Visitor implements Cloneable{

	private Node baseNode;
    private String identifier;
    private List<Map<String, String>> parameters;
    private List<String> modifiers;
    private String returnType;
    private boolean isOverride;
    private String implementation;
    private boolean isStatic;
    private ImplementationVisitor implementationVisitor;
    private Map<String, String> variableMap;
	  
    private ArrayList<String> usedClasses;
	
    public MethodVisitor(Node baseNode) {
		super();

		this.baseNode = baseNode;
		modifiers = new ArrayList<String>();
		parameters = new ArrayList<Map<String, String>>();
		this.isOverride = false;	
		this.usedClasses = new ArrayList<String>();	
        this.isStatic = false;
        this.variableMap = null;
	}
	
	public MethodVisitor(String identifier, String returnType) {
	    super();
	    
	    this.identifier = identifier;
	    this.returnType = returnType;
	    
	    modifiers = new ArrayList<String>();
	    parameters = new ArrayList<Map<String, String>>();
	    this.isOverride = false;
	    
	    this.usedClasses = new ArrayList<String>();
	}

	public void visit(Node n) {
		for (Object o : n) 
			if (o instanceof Node) dispatch((Node) o);
	}

	/**
	 * Pull the method declaration apart, and get all the pieces for translation
	 * 
	 **/
	public void visitMethodDeclaration(GNode n) {

		for (Object o : n) {
			// If String, must be the method identifier
			if (o instanceof String) {
				this.identifier = (String) o;
			}

			if (o instanceof Node) {
				// If Block, its the method implementation
				if (((Node) o).getName() == "Block") {
					ImplementationVisitor iv = new ImplementationVisitor();
					iv.dispatch((Node) o);
					this.setImplementation(iv.getImplementation());
					this.usedClasses.addAll(iv.getUsedClasses());
					this.implementationVisitor = iv;
				}

				// If modifiers... collect modifiers...
				if (((Node) o).getName() == "Modifiers") {
					dispatch((Node) o);
				}

				// If parameters... collect parameters...
				if (((Node) o).getName() == "FormalParameters") {
					dispatch((Node) o);
				}

				// Get return type
				if (((Node) o).getName() == "Type") {
					dispatch((Node) o);
				} else if (((Node) o).getName() == "VoidType") {
					returnType = "void";
				}
			}
		}

	}

	public void visitFormalParameter(GNode n) {
		HashMap<String, String> param = new HashMap<String, String>();

		for (Object o : n) {

			// String indicates variable name
			if (o instanceof String) {
				param.put("name", (String) o);
			}

			// FIXME: This needs to be able to handle qualified/primitive type
			// plus Dimension if it is an array
			if (o instanceof Node) {
				if (((Node) o).getName() == "Type") {
					Node innerType = (Node) ((Node) o).get(0);
					String type = (String) innerType.get(0);
					param.put("type", type);
				}
			}
		}

		parameters.add(param);
	}

	public void visitType(GNode n) {
		for (Object o : n) {

			if (o instanceof Node) {
				dispatch((Node) o);
			}

		}
	}

	public void visitQualifiedIdentifier(GNode n) {
		for (Object o : n) {

			if (o instanceof String) {
				returnType = (String) o;
			}
		}
	}

	public void visitPrimitiveType(GNode n) {
		for (Object o : n) {

			if (o instanceof String) {
				returnType = (String) o;
			}
		}

	}

	public void visitModifier(GNode n) {
		for (Object o : n) {
			// If String, add to modifier list
			if (o instanceof String) {
                
                //if static set isStatic to true
                if (o.equals("static")) {
                    this.isStatic = true;
                }
                    
				modifiers.add((String) o);
			}
		}
	}

	/**
	 * Helper method to add parameter to parameter list.
	 * 
	 */
	public void addParameter(String type, String name) {
		HashMap<String, String> map = new HashMap<String, String>();
		map.put("type", type);
		map.put("name", name);
		parameters.add(map);
	}

	public String getSignature(ClassVisitor implicitThis) {
		String out = "";
		out += "static " + this.returnType + " " + this.identifier + "(";
		
		if (this.identifier.equals("main")) {
			// do nothing
		} else {

			out += implicitThis.getIdentifier();

			// Print parameter types
			for (Map<String, String> p : this.parameters) {
				out += ", ";
				out += p.get("type");
			}

		}
		out += ")";

		return out;
	}
	
	public String parametersToString(){
		
		String out = "";
		
		for (Map p : this.parameters) {
			out += ",";
			out += p.get("type");
			out += " ";
			out += p.get("name");
		}
		
		return out;
	}

	public String getConstructorPtr() {
		return "CONSTR PTR";
	}

	@Override
    public String toString() {
	    return identifier;
	}

    public String getMethodPointer(ClassVisitor implicitThis) {
		String out = "";

		out += this.returnType + " " + "(" + "*" + this.identifier + ")" + "(";

		// Implicit this...
		out += implicitThis.getIdentifier();

		// Print parameter types
		for (Map<String, String> p : this.parameters) {
			out += ", ";

			out += p.get("type");
		}

		out += ")";
		return out;
	}
    
    public boolean isOverride() {
        return isOverride;
    }

    public void setOverride(boolean isOverride) {
        this.isOverride = isOverride;
    }

    //getter for isStatic private field
    public boolean isStatic() {
        return isStatic;
    }
	
	public MethodVisitor copy(){
	    MethodVisitor clone;
        try {
            clone = (MethodVisitor)this.clone();
            return clone;
        } catch (CloneNotSupportedException e) {
            e.printStackTrace();
            return null;
        }
	}
	
    public List<Map<String, String>> getParameters() {
        return parameters;
    }

    public void setParameters(List<Map<String, String>> parameters) {
        this.parameters = parameters;
    }

    public List<String> getModifiers() {
        return modifiers;
    }

    public void setModifiers(List<String> modifiers) {
        this.modifiers = modifiers;
    }

    public String getReturnType() {
        return returnType;
    }

    public void setReturnType(String returnType) {
        this.returnType = returnType;
    }

    public String getIdentifier() {
        return identifier;
    }

    public void setIdentifier(String identifier) {
        this.identifier = identifier;
    }

    public String getImplementation() {
        return implementation;
    }

    public void setImplementation(String implementation) {
        this.implementation = implementation;
    }
    
    public List<String> getUsedClasses() {
      return this.usedClasses;
    }

	public ImplementationVisitor getImplementationVisitor() {
		return implementationVisitor;
	}

	public void setImplementationVisitor(ImplementationVisitor implementationVisitor) {
		this.implementationVisitor = implementationVisitor;
	}

	public void setStatic(boolean isStatic) {
		this.isStatic = isStatic;
	}

	public void setUsedClasses(ArrayList<String> usedClasses) {
		this.usedClasses = usedClasses;
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

	
}