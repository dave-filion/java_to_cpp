package goodbye;

import hello.Hello;

public class Goodbye extends Hello{
	
	public String returnString(){
		return "Hi";
	}
	
	public int returnInt(String word) {
		return 42;
	}
	
	public String returnString(int something) {
		return "Hi 2";
	}
	
	public void chainTest(){
		this.returnString(4);
		String something = this.returnString(returnInt("Hi There"));
	}
	
	public static void main(String[] args) {
		Goodbye b = new Goodbye();
		b.chainTest();
	}
}
