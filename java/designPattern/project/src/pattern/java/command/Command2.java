package pattern.java.command;

public class Command2 implements Command {

	private Receiver re;
	
	public Command2(Receiver re) {
		this.re=re;
	}
	
	@Override
	public void execute() {
		// TODO Auto-generated method stub
		re.off();
	}


}
