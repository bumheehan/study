package pattern.java.state;

public class Context {
	
	private State state;
	
	public Context() {
		state = State1.getInstance();
		System.out.println("State1");
	}
	
	public void request1() {
		state.method1(this);
	}
	public void request2() {
		state.method2(this);
	}
	public void request3() {
		state.method3(this);
	}
	
	public void setState(State state) {
		this.state=state;
	}
	

}
