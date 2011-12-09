package xtc.translator.representation;
import xtc.tree.Visitor;
import xtc.tree.Node;
import xtc.tree.GNode;

import java.util.ArrayList;
import java.util.HashMap;

/**
 * A Visitor that visits and stores the implementation details of a method.
 * 
 * Catches everything inside of a block
 */
public class ImplementationVisitor extends Visitor {

	public int indent;
	private String implementation;
	private ArrayList<String> usedClasses;
	// key is variable, value is variable type
	private HashMap<String, String> varTypeDict;

	public ImplementationVisitor() {
		super();

		// Set indent at nothing initially
		indent = 0;

		// Implementation is empty initially
		implementation = "";
		
		// A list of classes found in visitQualifiedIdentifier
		usedClasses = new ArrayList<String>();
		
		// A table that holds values of key
		varTypeDict = new HashMap<String, String>();
	
	}

	public void visit(Node n) {
		for (Object o : n) {
			if (o instanceof Node)
				dispatch((Node) o);
		}
	}

	public String getImplementation() {
		return implementation;
	}

	public void setImplementation(String implementation) {
		this.implementation = implementation;
	}

	public void visitBlock(GNode n) {
		addLn("{");
		indent++;
		visit(n);
		indent--;
		add(getIndent());
		addLn("}");
	}

	public void visitNewClassExpression(GNode n) {
		add("new ");
		add("__");
				
		String identifier = n.getNode(2).getString(0);
		
		//Node arguments = n.getNode(3);
		add(identifier);
		add("( ");

		dispatch(n.getNode(3));

		add(" )");
		//System.out.println("\n\n" + implementation);
	}

  // SWITCH STATEMENTS START
  public void visitSwitchStatement(GNode n) {
    addLn("switch (" + n.getGeneric(0).getString(0) + ") {");
    // visit other children
    skipEldest(n);
    addLn("}");
  } 
  public void visitCaseClause(GNode n){
    addLn("case " + n.getGeneric(0).getString(0) + ":");
    skipEldest(n);
  }
  public void visitBreakStatement(GNode n){
    addLn("break;");
  }
  public void visitDefaultClause(GNode n){
    addLn("default: ");
    visit(n);
  }
  // SWITCH STATEMENTS END
  
  // DO WHILE LOOPS START
  public void visitDoWhileStatement(GNode n) {
    addLn("do");
    // in a do while loop, the youngest child is special
    skipYoungest(n);
    add("while (");
    dispatch(n.getGeneric(n.size() - 1));
    addLn(");");
  }
  // DO WHILE LOOPS END
  
  // FUNNY RETURN STATEMENT STUFF START
  public void visitAdditiveExpression(GNode n) {
    dispatch(n.getNode(0));
    add(" " + n.getString(1) + " ");
    dispatch(n.getNode(2));
  }
  public void visitMultiplicativeExpression(GNode n) {
    dispatch(n.getNode(0));
    add(" " + n.getString(1) + " ");
    dispatch(n.getNode(2));
  }
  // FUNNY RETURN STATEMENT STUFF END
  
	public void visitQualifiedIdentifier(GNode n) {
	  /*
		for (Object o : n) {
			if (o instanceof String) {
				add((String) o + " ");
			}
		}
		*/
		for (int i=0; i<n.size(); i++) {
		  String identifier = n.getString(i);
		  add(identifier + " ");
		  usedClasses.add(identifier);
		}
	}

	public void visitLogicalAndExpression(GNode n) {
		dispatch(n.getNode(0)); // bob2
		add(" && ");
		dispatch(n.getNode(1));
	}

	public void visitLogicalOrExpression(GNode n) {
		dispatch((Node) n.get(0));
		add(" || ");
		dispatch((Node) n.get(1));
	}

	public void visitWhileStatement(GNode n) {
		add(getIndent());
		add("while (");
		dispatch((Node) n.get(0)); // while condition
		add(")");
		dispatch((Node) n.get(1)); // attached block
	}

	public void visitForStatement(GNode n) {
		add(getIndent());
		add("for");
		visit(n);
	}

    public void visitBasicForControl(GNode n) {
		add("(");
		for (Object o : n) {
			if (o instanceof Node) {
                if (((Node)o).getName() == "Declarators") {
					dispatch((Node)o);
					add(";");
                } else if (((Node)o).getName() == "RelationalExpression") {
                    dispatch((Node)o);
					add(";");
                } else {
                    dispatch((Node) o);
                }
			}
			// add(";");
		}
		add(")");
	}

	public void visitDeclarators(GNode n) {
		visit(n);
	}

	public void visitEqualityExpression(GNode n) {
		for (Object m : n) {
			if (m instanceof Node) {
				Node child = (Node) m;
				dispatch(child);
			}
			if (m instanceof String) {
				add((String) m);
			}
		}
	}

	public void visitRelationalExpression(GNode n) {
		for (Object o : n) {
			if (o instanceof Node) {
				dispatch((Node) o);
			} else if (o instanceof String) {
				add((String) o); // print operator
			}
		}
	}

	public void visitExpressionList(GNode n) {
		for (Object o : n) {
			if (o instanceof Node) {
				dispatch((Node) o);
			} else if (o instanceof String) {
				add((String) o); // Print operator
			}
		}
	}

	public void visitPostfixExpression(GNode n) {
		for (Object o : n) {
			if (o instanceof Node) {
				dispatch((Node) o);
			} else if (o instanceof String) {
				add((String) o); // Print operator
			}
		}
	}

	public void visitUnaryExpression(GNode n) {
		for (Object o : n) {
			if (o instanceof Node) {
				dispatch((Node) o);
			} else if (o instanceof String) {
				add((String) o); // Print operator
			}
		}
	}
	
	public void visitExpressionStatement(GNode n) {
		add(getIndent());
		visit(n);
		addLn(";");
	}

	public void visitExpression(GNode n) {
		for (Object o : n) {
			if (o instanceof Node) {
				dispatch((Node) o);
			} else if (o instanceof String) {
				add((String) o); // Print operator
			}
		}
	}

	public void visitConditionalStatement(GNode n) {
		add(getIndent());
		add("if (");
		dispatch((Node) n.get(0));
		add(")");
		dispatch((Node) n.get(1));
		Node elseStatement = (Node) n.get(2);
		if (elseStatement != null) {
			add(getIndent());
			add("else ");
			dispatch((Node) n.get(2));
		}
	}

	public void visitArguments(GNode n) {
	  // loop through children, record arguments
	  // separated by commas
	  if (n.size() > 0) {
	    dispatch(n.getGeneric(0));
	    if (n.size() > 1) {
	      for (int i=1; i<n.size(); i++) {
	        add(" , ");
	        dispatch(n.getGeneric(i));
	      }
	    }
	  }
	}
	
	public void visitDeclarator(GNode n) {
		String declaratorName = n.getString(0);
		add(declaratorName);
		
		// The commented out code doesn't work in the case where
		// nothing is assigned to a declarator
		//add(" = ");
		//visit(n);
		
		if ( GNode.test(n.get(2))  ) {
		  add (" = ");
		  dispatch(n.getGeneric(2));
		  
		  // We want to get the declared variable's name and type!
		  String varName = n.getString(0);
		  String varType = evaluateClass(n.getGeneric(2));
		  varTypeDict.put(varName, varType);
		}
	}
	
	private String evaluateClass(GNode n) {
	  // TDL: work with method chaining
	  if (n.hasName("NewClassExpression")) {
	    return n.getGeneric(2).getString(0);
	  }
	  // WARNING: THIS IS INCREDIBLY LAZY CODING!!!!!!!!!!!
	  // IT SHOULD BE UPDATED IN THE FUTURE!!!!
	  else if (n.hasName("NewArrayExpression")) {
	    // String will be of the form numDimensions_Type
	    // ie 323_int
	    return n.getGeneric(1).size() + "_" + n.getGeneric(0).getString(0);
	  }
	  // otherwise type is n's name without the word Literal
	  else if (n.getName().contains("Literal")) {
	    return n.getName().substring(0,(n.getName().length()-7));
	  }
	  // uh oh we screwed up :(:(:(
	  else {
	    return "UH OH ITS A CALL EXPRESSION";
	  }
	}

  // helper method for method chaining
  private String callExpressionHelper(GNode n) {
    String name = "";
    // find the primary identifier
    if (GNode.test(n.get(0))) {
      if (n.getGeneric(0).hasName("PrimaryIdentifier")) {
        // add object name to the start
        name = n.getGeneric(0).getString(0);
        add(name);
      }
      else if (n.getGeneric(0).hasName("CallExpression")) {
        name = callExpressionHelper(n.getGeneric(0));
      }
      // don't think this will ever get called
      else if (n.getGeneric(0).hasName("ThisExpression")) {
        name = "__this";
        add(name);
      }
    }
    
    //then act as a visitCallExpression
    String methodName = n.getString(2);
		add("->__vptr->");
		add(methodName);
		add("(");
		// implicit this
		add(name);
		add(",");
		// dispatch over arguments
		for (Object o : n.getNode(3)){
		   dispatch((Node) o);
		   add(",");
		}
		removeLastComma();
		add(")");
		return name;
  }
  
	public void visitCallExpression(GNode n) {		
		String name = "";
		String methodName = n.getString(2);

		if (GNode.test(n.get(0))) {
		  Node first = n.getGeneric(0);
			if (first.hasName("PrimaryIdentifier")){
				name = first.getString(0);
				add(name);
			} else if (first.hasName("SelectionExpression")) {
				Node identifier = first.getNode(0);
				name = identifier.getString(0);
				if (!name.equals("System"))
					dispatch(n.getNode(0));
			} else if (first.hasName("CallExpression")) {
			  //method chaining
			  name = callExpressionHelper(n.getGeneric(0));
			} else if (first.hasName("ThisExpression")) {
			  name = "__this";
			  add(name);
			}
		}
		// Special case of print and println
		if (name.equals("System") && methodName.equals("println")) {
			add("std::cout");
			Node arguments = n.getNode(3);
			for (Object o : arguments) {
				add(" << ");
				dispatch((Node)o);
			}
			add(" << std::endl");
		} else if (name.equals("System") && methodName.equals("print")) {
			add("std::cout");
			add(" << ");
			Node arguments = n.getNode(3);
			for (Object o : arguments) {
				dispatch((Node)o);
				//add(" << ");
			}
		} else if (!name.isEmpty()) {
		  //I (Howard Jing) commented this line out because it caused problems
		  //with int a = hello.add(x,y)
		  //Can someone tell me why it goes here?
			//addCheckNotNull(name);
			add("->__vptr->");
			add(methodName);
			add("(");
			// implicit this
			add(name);
			add(",");
			// dispatch over arguments
			for (Object o : n.getNode(3)){
			   dispatch((Node) o);
			   add(",");
			}
			removeLastComma();
			add(")");
		} else {
			add(methodName);
			add("(");
			dispatch(n.getNode(3));
			add(")");
		}
	}

	private void addCheckNotNull(String name){
		addLn("__rt::checkNotNull(" + name + ");");
	}
	
	public void visitSuperExpression(GNode n) {
		add("super");
		// add(".");
		visit(n);
	}

	public void visitSelectionExpression(GNode n) {
		// Visits the first part, prints it, puts the dot, then prints the rest
		// changed this from :: to ->
		visit(n);
		add("->");
		add((String) n.get(1));
	}

	public void visitThisExpression(GNode n) {
	  // use the runtime __this
		add("__this");
		visit(n);
	}

	public void visitPrimaryIdentifier(GNode n) {
		String word = (String) n.get(0);

		add(word);
		visit(n);
	}

	public void visitModifiers(GNode n) {
		visit(n);
	}

	public void visitModifier(GNode n) {
		for (Object o : n) {
			if (o instanceof String) {
				add((String) o);
			} else if (o instanceof Node) {
				dispatch((Node) o);
			}
		}
	}

	public void visitVoidType(GNode n) {
		add("void");
	}

	public void visitFieldDeclaration(GNode n) {
		add(getIndent());
		visit(n);
		addLn(";");
	}

	public void visitType(GNode n) {
		visit(n);
	}

	public void visitPrimitiveType(GNode n) {
		//add((String) n.get(0) + " ");
		add((String) n.get(0) + " ");
	}

	public void visitIntegerLiteral(GNode n) {
		add((String) n.get(0));
	}

	public void visitReturnStatement(GNode n) {
		add("return ");
		visit(n);
		addLn(";");
	}

	public void visitStringLiteral(GNode n) {
		for (Object o : n) {
			if (o instanceof String) {
				add((String) o);
			}
		}
	}

	public void visitBooleanLiteral(GNode n) {
		for (Object o : n) {
			if (o instanceof String) {
				add((String) o);
			}
		}
	}

	public String getIndent() {
		String indentOut = "";

		for (int i = 0; i < indent; i++) {
			indentOut += "   ";
		}
		return indentOut;
	}

	/**
	 * Protected method to add new part to implementation representation
	 * 
	 **/
	protected void add(String addition) {
		implementation += addition;
	}
	
	private void removeLastComma(){
	   implementation = implementation.substring(0, implementation.length() - 1);
	}

	private void addLn(String addition) {
		add(addition + "\n");
	}
	
  // Dispatch on a node's children, skipping the eldest child
  private void skipEldest(Node n) {
    if (n.size() > 0) {
      for (int i=1; i<n.size(); i++) {
	      Object child = n.get(i);
	      if (GNode.test(child)) {
	        dispatch(GNode.cast(child));
	      }
      }
    }
  }
  
  // Dispatch on a node's children, skipping the youngest child
  private void skipYoungest(Node n) {
    if (n.size() > 0) {
      for (int i=0; i<(n.size() - 1); i++) {
	      Object child = n.get(i);
	      if (GNode.test(child)) {
	        dispatch(GNode.cast(child));
	      }
      }
    }
  }
  
  public ArrayList<String> getUsedClasses() {
    return usedClasses;
  }

  public HashMap<String, String> getVarTypeDict() {
    return varTypeDict;
  }
}