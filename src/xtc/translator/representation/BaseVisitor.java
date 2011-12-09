package xtc.translator.representation;

import xtc.tree.Node;
import xtc.tree.Visitor;

public class BaseVisitor extends Visitor{

	/**
	 * Catch-all visit.
	 */
    public void visit(Node n) {
        for (Object o : n)
            if (o instanceof Node)
                dispatch((Node) o);
    }

}
