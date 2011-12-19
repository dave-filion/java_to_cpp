package xtc.translator.representation;

import xtc.tree.Visitor;
import xtc.tree.Node;
import xtc.tree.GNode;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

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
	public static HashMap<String, String> varValues = new HashMap<String, String>();

	private List<CppPrintable> cppPrintList;
	private ArrayList<CallExpressionPiece> callExpressions;
	private ArrayList<PostfixPiece> postfixPieces;
	private List<IPiece> ipieces;

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

		cppPrintList = new ArrayList<CppPrintable>();
		callExpressions = new ArrayList<CallExpressionPiece>();
		postfixPieces = new ArrayList<PostfixPiece>();
		ipieces = new ArrayList<IPiece>();
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
		addLn("{", n);
		visit(n);
		addLn("}", n);
	}
	
	public void visitNewClassExpression(GNode n) {
		add("new ", n);
		add("__", n);

		String identifier = n.getNode(2).getString(0);

		// Node arguments = n.getNode(3);
		add(identifier, n);
		add("( ", n);

		dispatch(n.getNode(3));

		add(" )", n);
	}

	// SWITCH STATEMENTS START
	public void visitSwitchStatement(GNode n) {
		addLn("switch (" + n.getGeneric(0).getString(0) + ") {", n);
		// visit other children
		skipEldest(n);
		addLn("}", n);
	}

	public void visitCaseClause(GNode n) {
		addLn("case " + n.getGeneric(0).getString(0) + ":", n);
		skipEldest(n);
	}

	public void visitBreakStatement(GNode n) {
		addLn("break;", n);
	}

	public void visitDefaultClause(GNode n) {
		addLn("default: ", n);
		visit(n);
	}

	// SWITCH STATEMENTS END

	// DO WHILE LOOPS START
	public void visitDoWhileStatement(GNode n) {
		addLn("do", n);
		// in a do while loop, the youngest child is special
		skipYoungest(n);
		add("while (", n);
		dispatch(n.getGeneric(n.size() - 1));
		addLn(");", n);
	}

	// DO WHILE LOOPS END

	// FUNNY RETURN STATEMENT STUFF START
	public void visitAdditiveExpression(GNode n) {
		add(n.getString(1), n);
	}

	public void visitMultiplicativeExpression(GNode n) {
		dispatch(n.getNode(0));
		add(" " + n.getString(1) + " ", n);
		dispatch(n.getNode(2));
	}

	// FUNNY RETURN STATEMENT STUFF END

	public void visitQualifiedIdentifier(GNode n) {
		/*
		 * for (Object o : n) { if (o instanceof String) { add((String) o +
		 * " "); } }
		 */
		for (int i = 0; i < n.size(); i++) {
			String identifier = n.getString(i);
			add(identifier + " ", n);
			usedClasses.add(identifier);
		}
	}
	
	public void visitLogicalNegationExpression(GNode n) {
	   add("!", n);
	   visit(n);
	}

	public void visitLogicalAndExpression(GNode n) {
		dispatch(n.getNode(0)); // bob2
		add(" && ", n);
		dispatch(n.getNode(1));
	}

	public void visitLogicalOrExpression(GNode n) {
		dispatch((Node) n.get(0));
		add(" || ", n);
		dispatch((Node) n.get(1));
	}

	public void visitWhileStatement(GNode n) {
		add("while (", n);
		dispatch((Node) n.get(0)); // while condition
		add(")", n);
		dispatch((Node) n.get(1)); // attached block
	}

	public void visitForStatement(GNode n) {
		add("for", n);
		visit(n);
	}

	public void visitBasicForControl(GNode n) {
		add("(", n);
		for (Object o : n) {
			if (o instanceof Node) {
				if (((Node) o).getName() == "Declarators") {
					dispatch((Node) o);
					add(";", n);
				} else if (((Node) o).getName() == "RelationalExpression") {
					dispatch((Node) o);
					add(";", n);
				} else {
					dispatch((Node) o);
				}
			}
			// add(";");
		}
		add(")", n);
	}

	public void visitDeclarators(GNode n) {
		visit(n);
	}

	public void visitEqualityExpression(GNode n) {
		add("(", n);
		for (Object m : n) {
			if (m instanceof Node) {
				Node child = (Node) m;
				dispatch(child);
			}
			if (m instanceof String) {
				add((String) m, n);
			}
		}
		add(")",n);
	}

	public void visitRelationalExpression(GNode n) {
		for (Object o : n) {
			if (o instanceof Node) {
				dispatch((Node) o);
			} else if (o instanceof String) {
				add((String) o, n); // print operator
			}
		}
	}

	public void visitExpressionList(GNode n) {
		for (Object o : n) {
			if (o instanceof Node) {
				dispatch((Node) o);
			} else if (o instanceof String) {
				add((String) o, n); // Print operator
			}
		}
	}

	public void visitPostfixExpression(GNode n) {
		System.out.println("POSTFIX EXPRESSION -> " + n);

		PostfixPiece p = new PostfixPiece(n);
		cppPrintList.add(p);
		postfixPieces.add(p);
	}

	public void visitUnaryExpression(GNode n) {
		for (Object o : n) {
			if (o instanceof Node) {
				dispatch((Node) o);
			} else if (o instanceof String) {
				add((String) o, n); // Print operator
			}
		}
	}

	public void visitExpressionStatement(GNode n) {
		visit(n);
		addLn(";", n);
	}

	public void visitExpression(GNode n) {
		for (Object o : n) {
			if (o instanceof Node) {
				dispatch((Node) o);
			} else if (o instanceof String) {
				add((String) o, n); // Print operator
			}
		}
	}

	public void visitConditionalStatement(GNode n) {
		add(getIndent(), n);
		add("if (", n);
		dispatch((Node) n.get(0));
		add(")", n);
		dispatch((Node) n.get(1));
		Node elseStatement = (Node) n.get(2);
		if (elseStatement != null) {
			add("else ", n);
			dispatch((Node) n.get(2));
		}
	}

	public void visitArguments(GNode n) {
		// loop through children, record arguments
		// separated by commas
		if (n.size() > 0) {
			dispatch(n.getGeneric(0));
			if (n.size() > 1) {
				for (int i = 1; i < n.size(); i++) {
					add(" , ", n);
					dispatch(n.getGeneric(i));
				}
			}
		}
	}

	public void visitDeclarator(GNode n) {
		String declaratorName = n.getString(0);
		add(declaratorName, n);

		// The commented out code doesn't work in the case where
		// nothing is assigned to a declarator
		// add(" = ");
		// visit(n);

		if (GNode.test(n.get(2))) {
			add(" = ", n);
			dispatch(n.getGeneric(2));

			// We want to get the declared variable's name and type!
			String varName = n.getString(0);
			String varType = evaluateClass(n.getGeneric(2));
			varTypeDict.put(varName, varType);
			varValues.put(varName, varType);
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
			if (!n.getName().equals("NullLiteral"))
				return n.getString(0);
			else
				return "seriously?";
		}
		// uh oh we screwed up :(:(:(
		else {
			return "UH OH ITS A CALL EXPRESSION";
		}
	}

	public void visitCallExpression(GNode n) {

		CallExpressionPiece cep = new CallExpressionPiece(n);

		this.cppPrintList.add(cep);
		this.callExpressions.add(cep);
	}

	private void addCheckNotNull(String name) {
		addLn("__rt::checkNotNull(" + name + ");", null);
	}

	public void visitSuperExpression(GNode n) {
		add("super", n);
		// add(".");
		visit(n);
	}

	public void visitSelectionExpression(GNode n) {
		// Visits the first part, prints it, puts the dot, then prints the rest
		// changed this from :: to ->
		add((String) n.get(1), n);
	}

	public void visitThisExpression(GNode n) {
		// use the runtime __this
		add("__this", n);
		visit(n);
	}

	public void visitPrimaryIdentifier(GNode n) {
		String word = (String) n.get(0);

		add(word, n);
		visit(n);
	}
	
	public void visitInstanceOfExpression(GNode n) {
		add(null, n);
	}

	public void visitModifiers(GNode n) {
		visit(n);
	}

	public void visitModifier(GNode n) {
		for (Object o : n) {
			if (o instanceof String) {
				add((String) o, n);
			} else if (o instanceof Node) {
				dispatch((Node) o);
			}
		}
	}

	public void visitVoidType(GNode n) {
		add("void", n);
	}

	public void visitFieldDeclaration(GNode n) {
		add(getIndent(), n);
		visit(n);
		addLn(";", n);
	}

	public void visitType(GNode n) {
	    // for arrays
	    if (GNode.test(n.get(1)) ) {
	        add("__rt::Ptr<__rt::Array<", n);
        }
		visit(n);
		if (GNode.test(n.get(1)) ) {
		    add("> >", n);
		}
	}
	
	// for arrays
	public void visitNewArrayExpression(GNode n) {
	    add(" new __rt::Array<", n);
	    dispatch(n.getGeneric(0));
	    add(">(", n);
	    dispatch(n.getGeneric(1));
	    add(")", n);
	}
	
	// for arrays
	public void visitSubscriptExpression(GNode n) {
	    add("(*", n);
	    dispatch(n.getGeneric(0));
	    add(")[", n);
	    dispatch(n.getGeneric(1));
	    add("]", n);
	}

	public void visitPrimitiveType(GNode n) {
		// add((String) n.get(0) + " ");
		add((String) n.get(0) + " ", n);
	}

	public void visitIntegerLiteral(GNode n) {
		add((String) n.get(0), n);
	}

	public void visitReturnStatement(GNode n) {
		add("return ", n);
		visit(n);
		addLn(";", n);
	}
	
	public void visitNullLiteral(GNode n) {
		add("__rt::null()", n);
	}

	public void visitStringLiteral(GNode n) {
		add("__rt::literal(" + n.getString(0) + ")", n);
	}

	public void visitBooleanLiteral(GNode n) {
		for (Object o : n) {
			if (o instanceof String) {
				add((String) o, n);
			}
		}
	}
	
	public void visitCastExpression(GNode n) {
		System.out.println("CAST -> " + n);
		add("__rt::java_cast(", n);
		visit(n);
		add(")", n);
	}
	
	public void visitBasicCastExpression(GNode n) {
		add("(", n);
		visit(n.getNode(0));
		add(") ", n);
		visit(n.getNode(2));
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
	protected void add(String addition, Node baseNode) {
		// implementation += addition;
		IPiece i = new IPiece(baseNode, addition);
		cppPrintList.add(i);
		ipieces.add(i);
	}

	private void removeLastComma() {
		implementation = implementation.substring(0,
				implementation.length() - 1);
	}

	private void addLn(String addition, Node baseNode) {
		add(addition + "\n", baseNode);
	}

	// Dispatch on a node's children, skipping the eldest child
	private void skipEldest(Node n) {
		if (n.size() > 0) {
			for (int i = 1; i < n.size(); i++) {
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
			for (int i = 0; i < (n.size() - 1); i++) {
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

	public List<CppPrintable> getCppPrintList() {
		return cppPrintList;
	}

	public void setCppPrintList(List<CppPrintable> pieces) {
		this.cppPrintList = pieces;
	}

	public ArrayList<CallExpressionPiece> getCallExpressions() {
		return callExpressions;
	}

	public void setCallExpressions(
			ArrayList<CallExpressionPiece> callExpressions) {
		this.callExpressions = callExpressions;
	}

	public void setUsedClasses(ArrayList<String> usedClasses) {
		this.usedClasses = usedClasses;
	}

	public void setVarTypeDict(HashMap<String, String> varTypeDict) {
		this.varTypeDict = varTypeDict;
	}

	public ArrayList<PostfixPiece> getPostfixPieces() {
		return postfixPieces;
	}

	public void setPostfixPieces(ArrayList<PostfixPiece> postfixPieces) {
		this.postfixPieces = postfixPieces;
	}

	public List<IPiece> getIpieces() {
		return ipieces;
	}

	public void setIpieces(List<IPiece> pieces) {
		this.ipieces = pieces;
	}

}