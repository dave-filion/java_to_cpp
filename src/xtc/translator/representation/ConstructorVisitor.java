package xtc.translator.representation;
import java.util.Map;

import xtc.tree.Node;
import xtc.tree.GNode;

public class ConstructorVisitor extends MethodVisitor {

	public ConstructorVisitor(GNode sourceNode) {
		super(sourceNode);
	}
	
	public String getSignature(){
		String out = "";
		out += this.getIdentifier() + "(";

		int i = 0;
		
		// Print parameter types
		for ( Map<String, String> p : this.getParameters() ) {
		    if (i != 0)
		    	out += ",";
			out += p.get("type");
			i++;
		}

		out += ")";
		
		return out;
		
	}

	public void visitConstructorDeclaration(GNode n) {
		for (Object o : n) {
			if (o instanceof String) {
				this.setIdentifier((String) o);
			}

			if (o instanceof Node) {
				if (((Node) o).getName() == "Block") {
					ImplementationVisitor iv = new ImplementationVisitor() {
					  // handle ThisExpressions differently in constructors
					  // this is an anonymous class I think
					  public void visitThisExpression(GNode n) {
					    add("this", n);
					    visit(n);
          	}
					};
					iv.dispatch((Node) o);
					this.setImplementation(iv.getImplementation());
				}

				if (((Node) o).getName() == "Modifiers") {
					dispatch((Node) o);
				}

				if (((Node) o).getName() == "FormalParameters") {
					dispatch((Node) o);
				}
			}
		}
	}		

}