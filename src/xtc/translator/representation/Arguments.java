package xtc.translator.representation;

import java.util.ArrayList;
import java.util.List;

public class Arguments implements Comparable {

	private List<Argument> arguments;
	
	public Arguments() {
		this.arguments = new ArrayList<Argument>();
	}
	
	public void addArgument(String type, String value){
		this.arguments.add(new Argument(type, value));
	}
	
	public List<Argument> getArguments() {
		return arguments;
	}

	public void setArguments(List<Argument> arguments) {
		this.arguments = arguments;
	}
	
	@Override
	public int compareTo(Object other) {
		
		if (other == null) {
			return -1;
		} else {
			List<Argument> otherArgs = ((Arguments)other).getArguments();
			
			// If the sizes are different, they are not the same
			if (otherArgs.size() != arguments.size()) {
				return -1;
			}
			
			for (int i = 0; i < this.arguments.size(); i++) {
				String o = otherArgs.get(i).type;
				if (! o.equals(arguments.get(i).type)) {
					return -1;
				}	
			}	
			return 0;
		}	
	}

	@Override
	public String toString() {
		return "Arguments [arguments=" + arguments + "]";
	}

}
