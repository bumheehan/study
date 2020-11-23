
public class Wrapper {

  public static void main(String[] args) {
    Integer a = Integer.valueOf(1);
    Integer b = Integer.valueOf("1");
    Integer c = a + b;
    Integer d = Integer.valueOf(2);
    System.out.println(System.identityHashCode(a));
    System.out.println(System.identityHashCode(b));
    System.out.println(System.identityHashCode(c));
    System.out.println(System.identityHashCode(d));
    Integer g = Integer.valueOf(128);
    Integer h = Integer.valueOf(128);
    System.out.println(System.identityHashCode(g));
    System.out.println(System.identityHashCode(h));
    Integer e = 127;
    Integer f = 127;
    System.out.println(System.identityHashCode(e));
    System.out.println(System.identityHashCode(f));
    System.out.println("My Integer");
    MyInteger aa = MyInteger.valueOf(1);
    MyInteger bb = MyInteger.valueOf("1");
    System.out.println(System.identityHashCode(aa));
    System.out.println(System.identityHashCode(bb));
    MyInteger gg = MyInteger.valueOf(128);
    MyInteger hh = MyInteger.valueOf(128);
    System.out.println(System.identityHashCode(gg));
    System.out.println(System.identityHashCode(hh));
    MyInteger ee = MyInteger.valueOf(127);
    MyInteger ff = MyInteger.valueOf(127);
    System.out.println(System.identityHashCode(ee));
    System.out.println(System.identityHashCode(ff));
  }
}


class MyInteger {

  int i = 0;
  static MyInteger[] cache = new MyInteger[128];

  private MyInteger(int i) {
    this.i = i;
  }

  public static MyInteger valueOf(int i) {
    if (i > 127) {
      return new MyInteger(i);
    } else if (cache[i] == null) {
      cache[i] = new MyInteger(i);
    }
    return cache[i];
  }

  public static MyInteger valueOf(String i) {
    return valueOf(Integer.parseInt(i));
  }
}
