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
	public int compareTo(Object arg0) {
		// TODO Auto-generated method stub
		return 0;
	}

	@Override
	public String toString() {
		return "Arguments [arguments=" + arguments + "]";
	}

}
