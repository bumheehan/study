package pattern.java.strategy.ex;

public class Client {

	public static void main(String[] args) {
		// TODO Auto-generated method stub
		
		Phone ctx =new Phone();
		ctx.setAlramStrategy(new RingStrategy());
		ctx.alram();
		
		ctx.setAlramStrategy(new VibrationStrategy());
		ctx.alram();
		
	}

}
