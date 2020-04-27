package pattern.java.strategy;

public class Context {
	
	Strategy str;
	
	void setStrategy(Strategy str){
		System.out.println("Change Strategy");
		this.str=str;
	}
	
	void ContextMethod() {
		str.strategyMethod();
	}
}
