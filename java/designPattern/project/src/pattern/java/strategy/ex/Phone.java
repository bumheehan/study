package pattern.java.strategy.ex;

public class Phone {
	
	AlramStrategy str;
	
	void setAlramStrategy(AlramStrategy str){
		this.str=str;
	}
	
	void alram() {
		str.alram();
	}
}
