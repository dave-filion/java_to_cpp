package representation;

import java.util.ArrayList;
import java.util.List;

public class Method {

	public String identifier;
	public String overloadedIdentifier;
	public String returnType;
	public List<String> argumentTypes; //have to be in order
	
	public Method(String identifier, String returnType) {
		this.identifier = identifier;
		this.returnType = returnType;
		this.overloadedIdentifier = null;
		this.argumentTypes = new ArrayList<String>();
	}
	
	public void generateOverloadedIdentifier(){
		// generate overloaded identifier based on argument types
		
		// If there are arguments, prepend them to the identifier
		if (! argumentTypes.isEmpty()) {
			
			this.overloadedIdentifier = identifier;
			
			for (String type : argumentTypes) {
				this.overloadedIdentifier += "_" + type;
			}
			
		} else {
			// Otherwise, just set it as the identifier
			this.overloadedIdentifier = identifier;
		}
		
		System.out.println("Overloaded name for " + identifier + " is " + overloadedIdentifier);
	}
	
	public String toString(){
		return "(" + returnType + ") " + identifier + "( " + argumentTypes + " )";
	}
	
}
