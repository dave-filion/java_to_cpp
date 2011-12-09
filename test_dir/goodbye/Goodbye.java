package goodbye;

import hello.Hello;

public class Goodbye extends Hello{

	public void sayHi() {
		System.out.println("Bye!");
	}

	public void sayBye(String name) {
		System.out.println(name);
	}
	
	public void sayBye(){
		System.out.println("BYE");
	}
}
