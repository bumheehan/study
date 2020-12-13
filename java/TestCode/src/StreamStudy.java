import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

class Human {
  String gender;
  int age;
  String name;

  Human(String gender, int age, String name) {
    this.gender = gender;
    this.age = age;
    this.name = name;
  }
}


public class StreamStudy {
  static class AAA {
    static void println(Object o) {
      System.out.println(o);
    }
  }

  public static void main(String[] args) {

    // stream -> 흐름 -> 똑같은 자료형을 모아놓은 구조 -> 배열(array),컬렉션
    // ? int byte 객체
    List<String> list = new ArrayList<>();
    list.add("A-1");
    list.add("B-2");
    list.add("C");
    list.add("A");
    list.add("A");
    list.add("F");
    list.add("G");

    String[] array = {"A", "B", "C", "D", "E", "F", "G"};
    // 컬랙션 스트림화 // 중간형 // 최종형

    // distinct
    long count = list.stream().count();
    System.out.println(count);
    count = list.stream().distinct().count();
    System.out.println(count);
    count = list.stream().filter((X) -> {
      return true;
    }).count();
    System.out.println(count);
    count = list.stream().filter((X) -> {
      return "A".equals(X);
    }).count();
    System.out.println(count);

    List<Human> listH = new ArrayList<>();
    listH.add(new Human("남자", 31, "한범희"));
    listH.add(new Human("여자", 28, "빙은지"));
    listH.add(new Human("남자", 34, "신경식"));
    listH.add(new Human("여자", 25, "빙혜지"));
    listH.add(new Human("여자", 33, "한지희"));
    listH.add(new Human("여자", 16, "오로라"));
    listH.add(new Human("남자", 10, "컴퓨터"));

    // filter , count
    count = listH.stream().filter((h) -> h.age < 20).count();
    System.out.println(count);
    count = listH.stream().filter((h) -> "여자".equals(h.gender)).count();
    System.out.println(count);
    String name = "한범희";
    count = listH.stream().filter((h) -> h.name.equals(name)).count();
    System.out.println(count);

    // findAny는 순서와 관계없이 먼저 찾아지는 객체
    System.out.println(listH.stream().findAny().get().name);
    System.out.println(listH.stream().parallel().findAny().get().name);

    // findFirst 순서의 첫번째
    System.out.println(listH.stream().findFirst().get().name);

    count = listH.stream().limit(4).count();
    System.out.println(count);
    // listH.stream().peek((X) -> {
    // X.name = X.name + "신";
    // }).forEach(X -> System.out.println(String.format("이름 %s, 나이 %d", X.name, X.age)));

    // forEach
    listH.stream().forEach(X -> {
      System.out.println(X.name);
    });

    // for (Human h : listH) {
    // System.out.println(h.name);
    // }
    // listH.forEach(X -> {
    // System.out.println(X.name);
    // });

    listH.stream().filter(s -> s.gender.equals("남자")).forEach(X -> {
      X.age++;
      System.out.println(X.age);
    });

    Arrays.stream(array).forEach(System.out::println);
    Arrays.stream(array).forEach(x -> System.out.println(x));

    // Arrays.stream(array).forEach(x -> StreamStudy.AAA.println(x));
    // Arrays.stream(array).forEach(StreamStudy.AAA::println);
    System.out.println("map 공부");
    listH.stream().map(x -> x.name).forEach(System.out::println);
    listH.stream().map(x -> {
      List<String> ret = new ArrayList<String>();
      ret.add(x.name);
      return ret;
    }).forEach(System.out::println);


    Map<String, Human> map = new HashMap<>();
    map.put("나", new Human("남자", 31, "한범희"));
    map.put("은지", new Human("여자", 28, "빙은지"));

    map.forEach((x, y) -> System.out.print(String.format("키 %s , 이름 %s\n", x, y.name)));


    // 람다식
    // Supplier => void => Y , ()->{return Y;}, ()->Y
    // Consumer => X => void , (X)->{}
    // Function => X => Y , (X)->{return Y;}, (X)->Y
    // Predicate => X => Y(boolean), (X)->{return true;}, (X)->true


  }
}
