package hello;

import goodbye.Goodbye;
import goodbye.deepGoodbye.DeepGoodBye;

public class Hello {

public void sayHi() {
	System.out.println("Hi");
}

public void sayHi(String name) {
	System.out.println("HIII");
}

public static void main(String[] args) {
	System.out.println("Hello world");
	Goodbye g = new Goodbye();
	g.chainTest();
}

public String toString(){
return "HELLO";
}

}

