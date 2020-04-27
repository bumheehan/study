package pattern.java.command;

public class Command1 implements Command {
	
	private Receiver re;
	
	public Command1(Receiver re) {
		this.re=re;
	}
	
	@Override
	public void execute() {
		// TODO Auto-generated method stub
		re.on();
	}

}
