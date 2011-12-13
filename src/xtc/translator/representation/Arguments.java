package xtc.translator.representation;

import java.util.ArrayList;
import java.util.List;

public class Arguments implements Comparable {

	private List<String> arguments;
	
	public Arguments() {
		this.arguments = new ArrayList<String>();
	}
	
	public void addArgument(String type){
		this.arguments.add(type);
	}
	
	public List<String> getArguments() {
		return arguments;
	}

	public void setArguments(List<String> arguments) {
		this.arguments = arguments;
	}

	@Override
	public int compareTo(Object other) {
		
		if (other == null) {
			return -1;
		} else {
			
			List<String> otherArgs = ((Arguments)other).getArguments();
			
			// If the sizes are different, they are not the same
			if (otherArgs.size() != arguments.size()) {
				return -1;
			}
			
			for (int i = 0; i < this.arguments.size(); i++) {
				
				String o = otherArgs.get(i);
				if (! o.equals(arguments.get(i))) {
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
