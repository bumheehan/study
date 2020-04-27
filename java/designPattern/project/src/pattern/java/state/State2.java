package pattern.java.state;

public class State2 implements State {
	private static State state;
	
	private State2() {
		
	}
	
	public static synchronized State getInstance() {
		if(state==null) state = new State2();
		return state;
	}
			
	public void method1(Context ctx) {
		System.out.println("State1");
		ctx.setState(State1.getInstance());
	}
	public void method2(Context ctx) {
		System.out.println("변화X");
		
	}
	public void method3(Context ctx) {
		System.out.println("State3");
		ctx.setState(State3.getInstance());

	}
}
