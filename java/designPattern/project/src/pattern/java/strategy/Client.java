package pattern.java.strategy;

public class Client {

	public static void main(String[] args) {
		// TODO Auto-generated method stub
		
		Context ctx =new Context();
		ctx.setStrategy(new ConcreteStrategy1());
		ctx.ContextMethod();
		
		ctx.setStrategy(new ConcreteStrategy2());
		ctx.ContextMethod();
		
	}

}
