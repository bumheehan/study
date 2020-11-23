import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;
import java.util.Random;

public class GenericTest implements Comparable<GenericTest> {

  // 메소드 제네릭
  @SuppressWarnings("unchecked")
  public static <E, T extends E> T change(E s1) {
    if (s1 == null) {
      throw new IllegalArgumentException();
    }
    return (T) s1;
  }

  public static void main(String[] args) {
    
    InheritTest tt= change(new SubClass());
    
    InheritTest tt = <InheritTest,SubClass> change(new SubClass());

  }

  @Override
  public int compareTo(GenericTest arg0) {
    // TODO Auto-generated method stub
    return 0;
  }
}
