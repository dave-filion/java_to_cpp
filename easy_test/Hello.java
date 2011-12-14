public class Hello {

	public void hello(){
		System.out.println("Hey there");
	}

	public void hello(String name) {
		System.out.println(name);
	}

	public static void main(String[] args) {
		Hello h = new Hello();
		h.hello();
		h.hello("dave");
	}
}
