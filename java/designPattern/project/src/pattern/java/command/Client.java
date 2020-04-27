package pattern.java.command;

public class Client {

	public static void main(String[] args) {
		// TODO Auto-generated method stub
		//호출자 객체
		Invoker inv = new Invoker();
		//Command1,2에서 excute 메서드 구현에 필요한 클래스, 수신자 클래스
		Receiver re = new Receiver();
		//Command 인터페이스가 구현된 클래스들
		Command com1 = new Command1(re);
		Command com2 = new Command2(re);
		
		inv.setCommand(com1);
		inv.invoke();

		inv.setCommand(com2);
		inv.invoke();
		
		inv.setCommand(com1);
		inv.invoke();
	}	

}
