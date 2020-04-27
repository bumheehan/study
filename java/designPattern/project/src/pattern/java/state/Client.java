package pattern.java.state;

public class Client {
	
	
	public static void main(String[] args) {
		// TODO Auto-generated method stub
		Context ctx = new Context();
		ctx.request1();
		ctx.request2();
		ctx.request2();
		ctx.request3();
		ctx.request3();
	}


}
