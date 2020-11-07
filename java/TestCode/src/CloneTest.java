public class CloneTest {

    int i;

    public static void main(String[] args) {

	CloneTest cloneTest = new CloneTest();
	cloneTest.i = 10;
	try {
	    cloneTest.clone();
	} catch (CloneNotSupportedException e) {
	    System.out.println("cloneable 구현 X");
	}

	C1 c1 = new C1();
	c1.i = 10;
	c1.ir = new int[2];
	c1.cr = new C1[1];
	c1.cr[0] = new C1();
	c1.cr[0].i = 10;
	c1.irr = new int[2][];
	c1.irr[0] = new int[0];

	C1 shallowCopy = c1.shallowCopy();
	C1 deepCopy = c1.deepCopy();

	System.out.println("--I--");
	// i
	c1.i = 2;
	System.out.println("i =" + shallowCopy.i);
	System.out.println("i =" + deepCopy.i);

	System.out.println("--IR--");
	// ir
	c1.ir[0] = 10;
	System.out.println("c1 ir =" + shallowCopy.ir);
	System.out.println("scopy ir =" + shallowCopy.ir);
	System.out.println("dcopy ir =" + deepCopy.ir);
	// c1 , scopy는 같은 객체로 c1변경시 scopy만 변경됨
	System.out.println("c1 ir[0] =" + shallowCopy.ir[0]);
	System.out.println("scopy ir[0] =" + shallowCopy.ir[0]);
	System.out.println("dcopy ir[0] =" + deepCopy.ir[0]);

	System.out.println("--CR--");
	// Cr
	System.out.println("scopy cr =" + shallowCopy.cr);
	System.out.println("dcopy cr =" + deepCopy.cr);
	// 배열은 다른 객체지만 내부 함수는 같은 객체 => 참조형 배열은 얕은복사
	System.out.println("scopy cr[0] =" + shallowCopy.cr[0]);
	System.out.println("dcopy cr[0] =" + deepCopy.cr[0]);

	// C1에서 clone 정의하고 C2 에서 clone 정의안할시 C2로 복사되는지 체크
	C2 c2 = new C2();
	c2.i = 10;
	c2.j = 20;
	c2.ir = new int[2];
	c2.cr = new C1[1];
	c2.irr = new int[2][];
	Object deepCopyC2;
	try {
	    deepCopyC2 = c2.clone();
	    C2 cc = (C2) deepCopyC2;
	    System.out.println(cc.j);
	} catch (CloneNotSupportedException e) {
	    System.out.println("cloneable 구현 X");
	}

    }
}

class C1 implements Cloneable {
    int i;
    int[] ir;
    C1[] cr;
    int[][] irr;

    @Override
    protected Object clone() throws CloneNotSupportedException {
	return super.clone();
    }

    public C1 shallowCopy() {
	C1 copy = null;
	try {
	    copy = (C1) this.clone();
	} catch (CloneNotSupportedException e) {
	    System.out.println("cloneable 구현 X");
	}
	return copy;
    }

    public C1 deepCopy() {
	C1 copy = null;
	try {
	    copy = (C1) this.clone();
	    copy.ir = this.ir.clone();
	    copy.irr = this.irr.clone();
	    copy.cr = this.cr.clone();
	} catch (CloneNotSupportedException e) {
	    System.out.println("cloneable 구현 X");
	}
	return copy;
    }

    // string2차원테스트

}

class C2 extends C1 {
    int j;

}
