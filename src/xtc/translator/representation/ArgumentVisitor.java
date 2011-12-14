package xtc.translator.representation;

import java.util.List;
import java.util.Map;

import xtc.tree.GNode;
import xtc.tree.Node;
import xtc.tree.Visitor;

public class ArgumentVisitor extends Visitor {

	private Arguments arguments;
	private Map<String, String> variableMap;
	private Map<String, List<Method>> methodMap;

	public ArgumentVisitor(Map<String, String> variableMap,
			Map<String, List<Method>> methodMap) {
		arguments = new Arguments();
		this.variableMap = variableMap;
		this.methodMap = methodMap;
	}

	public void visitArguments(GNode n) {
		System.out.println("Arguments look like this: " + n);
		visit(n);
	}

	public void visitStringLiteral(GNode n) {
		arguments
				.addArgument("String", "__rt::literal(" + n.getString(0) + ")");
	}

	public void visitPrimaryIdentifier(GNode n) {
		String type = variableMap.get(n.getString(0));
		arguments.addArgument(type, n.getString(0));
	}

	public void visitIntegerLiteral(GNode n) {
		arguments.addArgument("int", n.getString(0));
	}

	public void visitCallExpression(GNode n) {
		System.out.println("Call expression " + n);

		Argument arg = new Argument();
		
		// get caller
		Node caller = n.getNode(0);

		// recurse into caller for chained methods
		if (caller != null) {
			dispatch(caller);

			if (caller.getName().equals("PrimaryIdentifier")) {
				arg.value += caller.getString(0);
			} else if (caller.getName().equals("ThisExpression")) {
				arg.value += "__this";
			}
		} else {
			arg.value += "__this";
		}

		// get arguments here
		Node arguments = n.getNode(3);
		ArgumentVisitor argumentVisitor = new ArgumentVisitor(variableMap,
				methodMap);
		argumentVisitor.dispatch(arguments);

		// check methodMap
		String name = n.getString(2);

		// get method list for name
		List<Method> methodList = methodMap.get(name);

		if (methodList == null) {
			// TODO: throw exception
			System.out.println("COULDNT FIND METHOD " + name);
		} else {
			for (Method method : methodList) {

				// check for method with matching arguments
				if (method.getArguments().compareTo(
						argumentVisitor.getArguments()) == 0) {

					// TODO: need to check method type to see whether to go
					// thru v pointer or statically
					if (method.isStatic) {
						arg.value += "." + method.getOverloadedIdentifier();
					} else {
						arg.value += "->__vptr ->"
								+ method.getOverloadedIdentifier();
					}

					arg.type = method.getReturnType();
				}
			}
		}

		// Print arguments
		arg.value += "(";
		arg.value += "__this";

		// TODO: fix this horrible get.get thing
		for (Argument innerArg : argumentVisitor.getArguments().getArguments()) {
			arg.value += ",";
			arg.value += innerArg.value;
		}

		arg.value += ")";
		this.arguments.addArgument(arg.type, arg.value);
	}

	public void visitNewClassExpression(GNode n) {
		System.out.println("New Class Expression " + n);
		String type = n.getNode(2).getString(0);

		ArgumentVisitor av = new ArgumentVisitor(variableMap, methodMap);
		av.dispatch(n.getNode(3));

		Argument arg = new Argument(null, null);
		arg.type = type;
		arg.value = "new __" + type;
		arg.value += "(";
		for (Argument a : av.getArguments().getArguments()) {
			arg.value += a.value;
		}
		arg.value += ")";

		arguments.addArgument(arg.type, arg.value);
	}

	public Arguments getArguments() {
		return arguments;
	}

	public void setArguments(Arguments arguments) {
		this.arguments = arguments;
	}

	public void visit(Node n) {
		for (Object o : n) {
			if (o instanceof Node)
				dispatch((Node) o);
		}
	}

}
