package pattern.java.singleton;

public class Client extends Thread{
	
	public static void main(String[] args) {
	
		//동기화 안할경우 문제점	
		System.out.println("getInstance 동기화X ---------------------------");
		for (int i = 0; i < 5; i++) {
			new Client() {
				@Override
				public void run() {
					Singleton2 single2 = Singleton2.getInstance();
					System.out.println(single2.toString());
				}
				
			}.start();
		}
		
		try {
			Thread.sleep(10);
		} catch (InterruptedException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		//동기화 할경우
		System.out.println("getInstance 동기화O ---------------------------");
		for (int i = 0; i < 5; i++) {
			new Client() {
				@Override
				public void run() {
					Singleton single = Singleton.getInstance();
					System.out.println(single.toString());
				}
				
			}.start();
		}
	}
}
