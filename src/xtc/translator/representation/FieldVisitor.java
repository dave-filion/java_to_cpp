package xtc.translator.representation;
import java.util.ArrayList;
import java.util.List;

import xtc.tree.Node;
import xtc.tree.GNode;
import xtc.tree.Visitor;

public class FieldVisitor extends Visitor {

    public List<String> modifiers;
    //public String visibility;
    public String variableType;
    public String variableName;

    public String variableValue; //for handling if a variable is set
    // I'm not sure what dimensions can be, so far just seems to be for arrays
    // For now it is an int, representing the dimensionality of the array 
    public int dimension;
    public List<String> concreteDimensions;
    

    // WE WILL NEED TO HANDLE IF A VARIABLE IS SET IE: INT X = 2 (THIS IS HANDLED NOW)
    // WE WILL NEED TO HANDLE IF A CLASS VARIABLE THAT IS AN ARRAY HAS BEEN ASSIGNED VALUES (THIS HAS MOST LIKELY BEEN HANDLED)

    public FieldVisitor() {
        super();
        modifiers = new ArrayList<String>();
//        visibility = "";
        
	      // assume that the variable is not an array
	      dimension = 0;
	      concreteDimensions = new ArrayList<String>();
    }

    // ie public, static, protected
    // this should be working correctly since visitModifier only has one child
    public void visitModifier(GNode n) {
//        if (n.getString(0).equals("public")) {
//            this.visibility = "public";
//        } else if (n.getString(0).equals("private")) {
//            this.visibility = "private";
//        } else if (n.getString(0).equals("protected")) {
//            this.visibility = "protected";
//        } else {
        if (n.getString(0).equals("static")) {
            this.modifiers.add(n.getString(0));
            //System.out.println("Updated modifiers: " + this.modifiers.toString());
        }
    }

    // ie String, int, double
    // this should be working
    public void visitType(GNode n) {
	    this.variableType = n.getNode(0).getString(0); // method chaining hahahahahahahha
	    //System.out.println("The type: " + this.variableType);

	    // loop through children excluding variableType
	    // so far just seems to be for visitDimensions
	    skipEldest(n);
    }

    // If it's an array, get the number of dimensions
    // most likely this works
    public void visitDimensions(GNode n) {
	    this.dimension = n.size();
	    //System.out.println("Dimension of field variable: " + this.dimension);	
    }

    // A variable's name as well as its value
    // I'm completely skipping the 2nd child, maybe that might bite me in the ass later
    public void visitDeclarator(GNode n) {
	    //System.out.println("Adding new Declarator: " + n.getString(0));
	    this.variableName = n.getString(0);

	    // Check to see if Declarator's third child has been assigned
	    if (GNode.test(n.getNode(2))) {
	      Object grandChild = n.getNode(2).get(0);
	      if (GNode.test(grandChild) ){
		      // it's an array (most likely), so visit rest of children
		      skipEldest(n);
	      }
	      else {
		      // the value has probably been assigned so record it
		      this.variableValue = n.getNode(2).getString(0);
		      //System.out.println("Assigned value: " + this.variableValue);
	      }
	    }
    }

    // Since I probably filtered out the other cases, if it gets to 
    // visitIntegerLiteral, it probably means that it's for concreteDimensions
    // BUT THIS MIGHT BE BUGGY NOT SURE OKAY
    public void visitIntegerLiteral(GNode n) {
	    this.concreteDimensions.add(n.getString(0));
	    //System.out.println("Updated concrete dimensions: " + concreteDimensions.toString());
    }

    /**
     * Catch-all visit.
     */
    public void visit(Node n) {
        for (Object o : n)
            if (o instanceof Node)
                dispatch((Node) o);
    }

    // Dispatch on a nodes children, skipping the eldest child
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
    
    public String toString() {
      String representation = "";
      for (String modifier : modifiers) {
        representation = representation + modifier + " ";
      }
      representation += variableType + " " + variableName;
      return representation;
    }

}