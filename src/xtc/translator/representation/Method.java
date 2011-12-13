package xtc.translator.representation;

import java.util.ArrayList;
import java.util.List;

public class Method {

	private String identifier;
	private String overloadedIdentifier;
	private String returnType;
	private Arguments arguments; //have to be in order
	
	public Method(String identifier, String returnType) {
		this.identifier = identifier;
		this.returnType = returnType;
		this.overloadedIdentifier = null;
		this.arguments = new Arguments();
	}
	
	public void generateOverloadedIdentifier(){
		// generate overloaded identifier based on argument types
		
		// If there are arguments, prepend them to the identifier
		if (! arguments.getArguments().isEmpty()) {
			
			this.overloadedIdentifier = identifier;
			
			for (String type : arguments.getArguments()) {
				this.overloadedIdentifier += "_" + type;
			}
			
		} else {
			// Otherwise, just set it as the identifier
			this.overloadedIdentifier = identifier;
		}
		
	}
	
	public String toString(){
		return "(" + returnType + ") " + overloadedIdentifier + "( " + arguments + " )";
	}

	public String getIdentifier() {
		return identifier;
	}

	public void setIdentifier(String identifier) {
		this.identifier = identifier;
	}

	public String getOverloadedIdentifier() {
		return overloadedIdentifier;
	}

	public void setOverloadedIdentifier(String overloadedIdentifier) {
		this.overloadedIdentifier = overloadedIdentifier;
	}

	public String getReturnType() {
		return returnType;
	}

	public void setReturnType(String returnType) {
		this.returnType = returnType;
	}

	public Arguments getArguments() {
		return arguments;
	}

	public void setArguments(Arguments arguments) {
		this.arguments = arguments;
	}
	
}
