package pattern.java.singleton;

public class Singleton2 {
	
	private static Singleton2 single = null;
	
	//private를 사용함으로써 외부에서 객체생성 보호
	private Singleton2() {
		System.out.println("객체가 생성 되었습니다.");
	}
	
	public static Singleton2 getInstance() {
		if(single==null) {
			single = new Singleton2();
		}
		return single ;
	}
	
}
