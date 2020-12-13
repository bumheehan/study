import java.util.Arrays;
import java.util.EnumMap;
import java.util.Map;
import java.util.Set;
import java.util.stream.Collectors;

public class GenericTest implements Comparable<GenericTest> {

  // 메소드 제네릭
  @SuppressWarnings("unchecked")
  public static <E, T extends E> T change(E s1) {
    if (s1 == null) {
      throw new IllegalArgumentException();
    }
    return (T) s1;
  }

  enum APPLE {
    A, B
  };

  public static void main(String[] args) {
    Map<APPLE, String> enummap = new EnumMap<>(APPLE.class);
    enummap.put(APPLE.A, "A입니다.");
    enummap.put(APPLE.B, "B입니다.");

    enummap.forEach((k, v) -> System.out.println(v));
    // A입니다.
    // B입니다.
    EnumMap<APPLE, Set<APPLE>> collect = Arrays.stream(APPLE.values()).collect(
        Collectors.groupingBy(p -> p, () -> new EnumMap<>(APPLE.class), Collectors.toSet()));



  }

  @Override
  public int compareTo(GenericTest arg0) {
    // TODO Auto-generated method stub
    return 0;
  }
}
