package pattern.java.state;

public class State3 implements State {
	private static State state;
	
	private State3() {
		
	}
	
	public static synchronized State getInstance() {
		if(state==null) state = new State3();
		return state;
	}
			
	public void method1(Context ctx) {
		System.out.println("State1");
		ctx.setState(State1.getInstance());
		
		
	}
	public void method2(Context ctx) {
		System.out.println("State2");
		ctx.setState(State2.getInstance());
		
	}
	public void method3(Context ctx) {
		System.out.println("변화X");

	}
}
