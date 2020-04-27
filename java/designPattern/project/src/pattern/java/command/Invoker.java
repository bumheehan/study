package pattern.java.command;

//호출자
public class Invoker {
	private Command com;
	
	public void setCommand(Command com){
		this.com=com;
	}
	public void invoke() {
		com.execute();
	}
}
