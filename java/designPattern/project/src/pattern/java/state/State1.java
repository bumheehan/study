package pattern.java.state;

public class State1 implements State {
	private static State state;
	
	private State1() {
		
	}
	
	public static synchronized State getInstance() {
		if(state==null) state = new State1();
		return state;
	}
			
	public void method1(Context ctx) {
		System.out.println("변화X");
		
	}
	public void method2(Context ctx) {
		System.out.println("State2");
		ctx.setState(State2.getInstance());
		
	}
	public void method3(Context ctx) {
		System.out.println("State3");
		ctx.setState(State3.getInstance());

	}
}
