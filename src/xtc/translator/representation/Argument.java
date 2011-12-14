package xtc.translator.representation;

public class Argument {

	public String type;
	public String value;
	
	public Argument(String type, String value) {
		this.type = type;
		this.value = value;
	}
	
	public String toString(){
		return "Argument : " + type + " : " + value;
	}
}
