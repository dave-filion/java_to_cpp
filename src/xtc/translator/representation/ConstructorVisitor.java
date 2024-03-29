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
		out += "__" + this.getIdentifier() + "(";

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
					ImplementationVisitor iv = new ImplementationVisitor();
					
					iv.dispatch((Node) o);
					this.setImplementation(iv.getImplementation());
					this.setImplementationVisitor(iv);
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