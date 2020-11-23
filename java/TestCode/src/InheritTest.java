public class InheritTest {


  public void add() {
    System.out.println("add");
  }

  public void addAll() {
    System.out.println("addall");
    add();
  }

  public static void main(String[] args) {
    SubClass subClass = new SubClass();
    subClass.addAll();

  }
}


class SubClass extends InheritTest {

  @Override
  public void add() {
    super.add();
    System.out.println("Sub add");
  }

  @Override
  public void addAll() {
    super.addAll();
    System.out.println("Sub addAll");



  }
}

