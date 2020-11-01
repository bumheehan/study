

public class SingletonTest {

    public static void main(String[] args) {
	// A방법
	SingleA a = SingleA.INSTANCE;
	a.print();

	// B 방법
	SingleB b = SingleB.getInstance();
	b.print();
	SingleB bb = SingleB.getInstance();
	bb.print();

	// C방법
	SingleC c = SingleC.INSTANCE;
	c.print();
    }

}

class SingleA {

    public static final SingleA INSTANCE = new SingleA();

    private SingleA() {
	System.out.println("SingleA 생성");
    }

    public void print() {
	System.out.println(String.format("SingleA(%s) print", INSTANCE));
    }

}

class SingleB {

    private static SingleB INSTANCE;

    private SingleB() {
	System.out.println("SingleB 생성");
    }

    public static SingleB getInstance() {

	if (INSTANCE == null) {
	    synchronized (SingleB.class) {
		// 다시한번 체크
		if (INSTANCE == null) {
		    INSTANCE = new SingleB();
		}
	    }
	}

	return INSTANCE;
    }

    public void print() {
	System.out.println(String.format("SingleB(%s) print", INSTANCE));
    }

}

//단일원소 enum으로 싱글톤 생성, 다른 상속할게있으면 사용못함
enum SingleC {
    INSTANCE;

    public void print() {
	System.out.println(String.format("SingleC(%s) print", INSTANCE));
    }

}