package pattern.java.singleton;

public class Singleton {
	
	private static Singleton single = null;
	
	//private를 사용함으로써 외부에서 객체생성 보호
	private Singleton() {
		System.out.println("객체가 생성 되었습니다.");
	}
	
	//다중 쓰레드에서 여러개 생성될수 있기때문에 동기화 필수
	public static synchronized Singleton getInstance() {
		if(single==null) {
			single = new Singleton();
		}
		return single ;
	}
	
}
