

public class SingletonTest {

    public static void main(String[] args) {
	// A���
	SingleA a = SingleA.INSTANCE;
	a.print();

	// B ���
	SingleB b = SingleB.getInstance();
	b.print();
	SingleB bb = SingleB.getInstance();
	bb.print();

	// C���
	SingleC c = SingleC.INSTANCE;
	c.print();
    }

}

class SingleA {

    public static final SingleA INSTANCE = new SingleA();

    private SingleA() {
	System.out.println("SingleA ����");
    }

    public void print() {
	System.out.println(String.format("SingleA(%s) print", INSTANCE));
    }

}

class SingleB {

    private static SingleB INSTANCE;

    private SingleB() {
	System.out.println("SingleB ����");
    }

    public static SingleB getInstance() {

	if (INSTANCE == null) {
	    synchronized (SingleB.class) {
		// �ٽ��ѹ� üũ
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

//���Ͽ��� enum���� �̱��� ����, �ٸ� ����Ұ������� ������
enum SingleC {
    INSTANCE;

    public void print() {
	System.out.println(String.format("SingleC(%s) print", INSTANCE));
    }

}