# `Effective Java

효율적으로 자바 코딩
간단하게 정리 => 이해 못하는 내용 다음 회차에서 확인

## 객체 생성과 파괴

### 1. 생성자 대신 정적 팩터리 메서드 고려

- 생성자보다 메서드 이름으로 객체 타입 유추 가능

```java
new BigInteger(int,int,Random)
BigInteger.probablePrime(bitLength, rnd)
```

- 호출 될때마다 새로운 인스턴스 생성 안해도됨
  - 불변 클래스는 호출 될 때마다 생성한 인스턴스 캐싱하여 재활용

```java
public static Boolean.valueOf(boolean b){
    //객체 생성 안함
    return b? Boolean.TRUE : Boolean.FALSE;
}
```

- 반환 타입의 하위 타입 객체를 반환할 수 있는 능력이 있다.

  - Java 8이전 `Type`인 인터페이스를 반환 하는 정적 메서드가 필요하면 `Types`라는 (인스턴스 불가) 동반 클래스(Companion Class)를 만들어 정의하는것이 관례
  - Collection 인터페이스의 하위 타입의 객체를 반환하는 동반 클래스Collections

  - Java 8 이후 인터페이스에서 정적 메서드 가질 수 있어 동반 클래스 둘 필요가 없음

- 매개 변수에 따라 다른 클래스의 객체를 반환 가능

  - 매개변수가에 따라서 성능상 좋은 구현체 생성  가능

- 상황에 따른 메소드 이름

  - from : 매개변수 받아서 인스턴스 반환

    ```
    Date d = Date.from(instant);
    ```

  - of : 여러 개배변수를 받아 적절한 타입 반환

    ```
    Set<Rank> faceCards = EnumSet.of(JACK,QUEEN,KING);
    ```

  - instance or getInstance : 매개변수로 명시한 인스턴스를 반환하지만, 같은 인스턴스임을 보장안함 (새 인스턴스 or 싱글턴)

    ```
    StackWalker luke = StackWalker.getInstance(options);
    ```

  - valueOf : from ,of의 더 자세한 버전

    ```
    BigInteger prime = BigInteger.valueOf(Integer.MAX_VALUE);
    ```

  - create or newCreate : getInstance랑 같은데 매번 새로운 인스턴스를 줌

    ```
    Object newArray = Array.newInstance(classObject,arrayLen);
    ```

  - getType : getInstance와 같으나 생성할 클래스가아닌 다른 클래스에 팩터리 메서드를 정의할 때 사용

    ```
    FileStore fs = Files.fetFileStore(path);
    ```

  - newType : newInstance와 같으나 생성할 클래스가아닌 다른 클래스에 팩터리 메서드를 정의할 때 사용

    ```
    BufferedReader br = Files.newBufferedReader(path);
    ```

  - type: getType와 new Type의 간결한 버전

    ```
    List<Complaint> litany =Collections.list(legacyLitany);
    ```

    

#### 핵심 정리

정적 팩터리 메서드와 public 생성자는 각자 쓰임새가 있으니 서로 장단점을 알고 사용, 무조건 public 생성자 사용 습관 고치자



### 2. 생성자 매개변수가 많으면 빌더 사용

빌더사용에 대한 내용으로  lombok builder 사용

#### 핵심 정리

생성자나 정적 팩터리가 처리해야 할 매개변수가 많다면 빌더 패턴을 선택, 간결하고 읽고 쓰기 쉬움

### 3.  private 생성자나 열거타입으로 싱글턴을 보증하라



### 4. 인스턴스화를 막으려거든 private 생성자를 사용하라

몇 가지 인스턴스가 필요없는 클래스가 있다. 이러한 클래스는 private 으로 생성자를 만들어 주지 않을 시 컴파일러가 자동으로 public 생성자를 생성한다. 따라서 private 생성자를 사용을 해야한다.

- 정적 메소드만 가지는 클래스 
  - Arrays, Collections 등
- final 클래스와 관련한 메서드 모아놓은 클래스
  - 정적 멤버만 담은 유틸리티 클래스는 인스턴스가 의미없음



```java
public class UtilityClass{
	// 기본 생성자가 만들어지는 것을 막는다.
	private UtilityClass(){
		// 혹여 클래스 내부에서 생성할 수도 있으니 에러 발생 
		throw new AssertionError();
	}
	... 

}
```



###  5. 자원을 직접 명시하지 말고 의존 객체 주입을 사용하라

의존 객체 주입은 `생성자`에 파라메터로 특정 객체를 주는 방법, 단순한 방법인데 해당 패턴에 이름이 의존 객체 주입임

```java
public class SpellChecker{
    private final Lexicon dictionary;
    
    public SpellChecker(Lexicon dictionary){
        this.dictionary = Object.requireNonNull(dictionary);
    }
    
}
```



#### 핵심 정리

- 클래스가 내부적으로 하나 이상의 자원에 의존하고, `그 자원이 클래스 동작에 영향` 준다면 싱글턴과 정적 유틸리티 클래스는 사용하지 않는 것이 좋다. 이 자원들을 클래스가 직접 생성도 하면 안된다. 필요한 자원을 생성자나 빌더에 넘겨주자.



### 6. 불필요한 객체 생성을 피하라 ★

불변객체는 언제든 재사용 할 수 있다.

String 은 불변객체로 "" 로 만들어진 String은 같은 가상머신 안에서 같은 문자열일때 모두 같은 객체를 재사용함이 보장된다(JLS,3.10.5)

그러나 new Stiring("B")으로 생성될 경우 새로이 String 인스턴스를 생성, 같은 "B"

- 정적 팩토리 메소드 사용하여 불필요한 객체 생성을 피하기

- 생산 비용이 비싼 객체는 캐싱해서 반복적으로 사용하는것이 좋음 

  - 내부 코드를 알아야 해결가능 여러 예제를 보면서 외워야함

  - ```java
    static boolean isRomanNumeral(String s){
    	return s.match("~~~정규식~~");
    }
    //메소드 실행마다 정규식으로 Pattern 인스턴스를 생성
    
    //해당 Pattern을 캐싱시킴
    private static final Pattern ROMAN =Pattern.compile("~~정규식~~");
    
    //캐싱된 Pattern 인스턴스 사용, 위의 방식보다 6.5배빠름
    static boolean isRomanNumeral(String s){
    	return ROMAN.matcher(s).matches();
    }
    ```

- 오토박싱 잘못된 사용금지, 래퍼클래스보단 기본형으로 사용하고 의도치 않은 오토박싱 있는지 확인

  - ```java
    private static long sum(){
        //Long -> long 으로 변환하면 내컴퓨터에서 5배 이상 빨라짐
    	Long sum =0L;
    	for(long i = 0;i<= Integer.MAX_VALUE;i++){
    		//매번 쓸뎅벗는 Long 객체가 생성
    		sum+=i;
    	}
    	return sum;
    }
    ```



- `가벼운 객체`를 재사용하기 위해 나만의 객체풀을 만들지 말자. -> 요즘 GC가 잘 최적화 되어있어서 직접 객체 풀을 만든것보다 훨씬 빠름 
  - `무거운 객체`는 객체풀 만드는게 나을때가 있다. 
    - DB는 커넥션에 대한 생산비용이 높아 커넥션 풀을 사용

- 불필요한 객체생성은 성능만 영향주지만 , 잘못된 재사용은 버그를 생성한다.



### 7. 다 쓴 객체 참조를 해제하라

- 메모리 누수를 피하기 위해 다 쓴 객체 null 참조 -> GC 처리
  - null 참조하는 방법은 예외적인것 , 변수를 유효범위(scope) 밖으로 밀어내는 것이 일반적
  - 이점 잘못 참조할때 nullpointerException 발생
- 다 쓴 캐시는 이따금 청소 필요
  - 상황에 따라 `WeakHashMap `사용
    - Map value가 null이 되면 삭제



### 8.  finalizer cleaner 사용을 피하라

- finalizer 
  - 해당 객체가 GC의 대상이 될때 호출
  - java9+부터 Deprecated 
  - java 9부터 cleaner 지원
- cleaner 
  - finalizer 와 같은 기능이고 사용방법이 다름
  - 사용방법은 인터넷 확인



- 특정 상황아니면 사용금지
  - 해당 메소드를 실행한다고 바로 삭제되는것을 보장하지않음
  -  finalizer는 성능이슈가 이씅ㅁ
- 특정상황 
  - JNI
  - off-Heap 메모리 사용시

### 9. try-finally 보다는 try-with-resources를 사용하라

- AutoCloseable 을 구현한 클래스만 사용가능

- 자바 라이브러리는 close() 사용하는 자원이 많다. 

  - InputStream ,OutputStream ,java.sql.Connection ...

  - 각 클래스마다 close 하는 이유가다름

    - 파일스트림 같은 경우 close하지 않을때 다른 쓰레드가 lock 걸릴 수 있음

      



## 3 장 모든 객체의 공통 메서드



### 10. equals는 일반 규약을 지켜 재정의하라

equals는 잘못 재정의 하면 끔직한 결과를 초래함, 가장 쉬운길은 아예 재정의 하지않는것

아래 상황 중 하나에 해당되면 재정의 않는게 최선

- 각 인스턴스가 본질적으로 고유하다
  - 값을 표현하는게아니라 동작하는 객체를 표현하는 클래스
  - 예) Thread
- 인스턴스틔 논리적 동치성(Locgical Equality)을 검사할 일이 없다.
- 상위 클래스에서 재정의한 equals가 하위 클래스에도 딱 들어 맞는다.
  - Set 구현체는 AbstractSet이 구현한 equals를 상속받아 사용
  - List는 AbstactList Map은 AbstractMap

#### 규약 : null이 아닌 객체

- 반사성(reflexivity)  : x.equals(x) 는 true
- 대칭성(symmetry) : x.equals(y) 가 true 면 y.equals(x) 도 true
- 추이상(transitivity) : x.equals(y) 가 true고 y.equals(z) true면 x.equals(z)도 true
- 일관성(consistency) : x.equals(y)를 반복해서 호출하면 항상 true, false 
- null-아님 : null이 아닌 참조값 x에 대해, x.equals(null)은 false 다

#### 양질의 equals 메서드 구현방법

1. == 연산자를 사용해 입력이 자기 자신의 참조인지 확인한다.
2. instanceof 연산자로 입력이 올바른 타입인지 확인
3. 입력을 올바른 타입으로 형변환
4. 핵심필드 일치하는지 검사
   - double, float은 Float.compare로 비교
   - 배열의 모든 요소가 핵심 일때 Arrays.equals



```
@Override public boolean equals(Object o){
	if(o==this)
		return true;
    if(!(o instanceof Type))
    	return false;
    Type t = (Type) o;
    return t.member1 == member1 && t.member2 == member2;
}
```



#### 핵심정리

- 꼭 필요한 경우가아니면 equals 재정의 X
- 재정의해야 할 때는 그 클래스의 핵심 필드 모두 빠짐없이 다섯가지 규약을 지키며 비교



### 11. equals를 재정의하려거든 hashCode도 재정의하라



Object 명세 

- equals에 사용되는 정보(멤버? )가 변경되지 않았다면 hashCode도 일관성있게 같은값을 반환
  - app다시실행시 달라질 수 있음
- equals(Object)가 같다고 판단하면 두 객체 hashCode도 같아야함 --> 중요
- equals(Object)가 다르다고 판단했어도 hashCode가 다른값일 필요는없다. 
  - 성능상 다른게 좋음



#### 해시코드 만드는법

1. int result = fieldHashCode  선언
2. 각 필드마다 해시 값 계산법
   - 기본타입 Type.hashcode(f)
   - 참조타입 hashCode
     - 필드값이 null 이면 0
     - 같은클래스를 사용할경우 재귀할 수 있으니 조심
   - 배열 , 각원소를 필드 처럼다루는데 핵심원소가 아니면 제외
     - 핵심원소가 하나도없다면 0
3. result = 31 * result +fieldHashCode ; 공식으로 필드 이터레이터 , 
4. result 반환



#### 핵심

IDE에서 자동으로 만들어줌

필요할때 사용



### 13 toString을 항상 재정의하라

- toString 규약 : 모든 하위 클래스에서 이 메서드를 재정의하라

- ###### 해당 객체의 정보를 담고 있는게 좋다, 정보가 많으면 요약해서 구현

#### 핵심정리

- 모든 구현 클래스에서는 toString을 정의
- 상위 클래스에서 알맞게 제정의한건 예외
- 재정의한 클래스는 디버깅 쉽게해줌
- toString은 객체의 명확한 정보를 읽기 좋은 형태로 반환!



### 14 clone 재정의는 주의해서 진행

- Cloneable 인터페이스를 구현해야 clone시 ColoneNotSupportedException이 발생 안함
  - clone override 시 super.clone 사용하는데 super.clone 사용안할 시 Cloneable 구현안해도됨
- 얕은 복사 vs 깊은 복사
  - 참조형 배열은 얕은 복사
  - 2차원 이상 배열도 얕은복사(참조기때문에)
    - 2차원 배열에서 내부객체 생성할때 기본생성자가 없을시 어떻게 복사함 ? => 일반화가 안됨 => 이런 문제들 때문에 참조형 Collection나 Array 복사할때는 Collection, Array만 복사되고 내부까지 복사하려면 직접 구현해야하` copied.setData(original.getData());`
      - 뇌피셜
  - 기본형 배열은 상관없음

#### 핵심정리

- Array복사 빼고는 clone쓰지말자, 직접 내부객체 복사기능 구현하는게 더 안전한것으로 판단됨

### 15 Comparable 구현할지 고려하라

Comparable 규약

- 이 객체가 주어진 객체보다 작으면 음의 정수(-1), 같으면 0 , 크면 양의 정수(1)를 반환
- 이 객체와 비교할 수 없는 타입은 ClassCastException 발생
- sgn(x.compareTo(y)) == -sgn(y.compareTo(x))

- 동치성 중요 !, (x.compareTo(y)==0) == (x.equals(y)) ,  다를경우 "이 클래스의 순서는 equals 메서드와 일관되지 않다" 표시

  - HashSet은 equals으로 동치성확인, TreeSet은 compareTo로 확인, 몇가지 컬렉션은 compareTo로 확인함 => 두 가지 다 작동하기위해 equals나 compareTo 가 같게 하는게 중요하다
  - 동치성 다른 클래스 BigDecimal 
    - new BigDecimal("1.00").equals(new BigDecimal("1.0")) == false 임 
    - TreeSet에 두 객체 넣을 경우 한개의 객체만 들어감, TreeSet은 CompareTo로 비교하기  때문에
    - HashSet은 두개의 원소가 모두 들어감

- compareTo 를 구현할때 기본타입 래퍼클래스의  정적 compare 사용, 

- 구현할때는 중요한 필드부터 순서를 맞춘다

  - ```java
    public int compareTo(A a){
    	int result = Short.compare(this.i,a.i); // 가장 중요한 필드
    	if(result==0){
    		result = Short.compare(this.j,a.j); // 두번째 중요한 필드
    		if(result==0){
    			result= Short.compare(this.k,a.k); // 세번째 중요한 필드
    		}
    	}
    	return result;
    }
    ```

- Comparator : ? 

#### 핵심 정리

- 순서가 필요한 클래스는 Comparable 구현!





## 4장 클래스와 인터페이스

### 15 클래스와 멤버의 접근 권한을 최소화하라

- private 잘 쓰자

- public static final 배열은 불변 리스트로 구현

  - ```
    //배열 변경가능그래서 private으로 구현
    private static final Thing[] PRIVATE_VALUES={...}
    //불변리스트로 구현
    public static final List<Thing> VALUES= Collections.unmodifiableList(Arrays.asList())
    ```

  - 



### 16 Public 클래스에서는 Public 필드가 아닌 접근자 메서드를 사용하라

- public 클래스는 절대 가변 필드를 노출 해서는 안됨! , 불변 필드는 덜 위험하지만 좋지는 않음

### 17 변경 가능성을 최소화 하라

#### 불변 클래스 

인스턴스 내부 값을 수정 할 수 없는 클래스, BigInteger, BigDecimal, String 등 있음

##### 생성방법

- Setter(변경자 메서드) 제공 하지 않는다.
- 클래스 확장할 수 없도록 한다
  - 예로 final 사용
- 모든 필드를 final로 선언
- 모든 필드 private 선언
  - public final 도 불변 객체지만 이 필드를 사용하는 시스템에서 다음 릴리즈 할때 변경이 불가능하여 권하지 않음
- 자신 외에는 내부의 가변 컴포넌트에 접근할 수 없도록 한다.
  - 클래스 내에 가변 클래스 사용하면, 불변 클래스가 될 수 없다.
  - 방어적 복사를 사용

##### 불변 클래스 특징

- 불변 객체는 근본적으로 쓰레드에 안전하여 따로 동기화 필요없음
- 불변 객체는 자유롭게 공유할 수 있음은 물론, 불변 객체끼리는 내부 데이터를 공유 할 수 있다.
  - BigInteger 는 부호와 크기를 따로 저장, negete 메서드는 크기가 같고 부호만 다른 BigInteger를 생성하지만 원본 인스턴스가 가리키는 원본 배열 그대로 가리킴
- 불변객체는 실패 원자성을 제공
  - 실패원자성이랑 트랜잭션처럼 에러날경우 원소가 이전과 같은지에 대한것
- 단점으로 값이 다르면 반드시 독립된객체로 생성
  - 큰 인스턴스 생성시 단점이 더 커짐, 가변은 내부 필드만 변경해줘도됨 
  - String은 StringBuilder 같은 가변 동반 클래스가 있음



#### 결론

- 불변을 보장 하려면 자신을 상속 하지 못하게 해야함
  - 간단한 방법 final 클래스
  - 더 간단한 방법 추천 : 모든 생성자를 private으로 두고 public 정적 팩터리 메서드 사용
    - valueOf 등
- 불변으로 만들수 없는  클래스라도 변경할 수 있는 부분을 최소한으로 줄이자.
- 다른 합단한 이유가 없다면 모든 필드는 private final



### 18 상속보다는 컴포지션을 사용 !!!!

##### 상속

- Is a 관계에만 사용
  - 자바 기본 라이브러리에 있는 Stack이나, Properties도 IS-A 관계를 위반한 클래스이다. Stack IS A Vector 관계가 성립하지 않는다. 하지만 문제를 바로잡기에는 너무 늦어버려서 바꿀 수 없게 되었다.
- 만약 하위 클래스의 패키지가 상위 클래스와 다르고 상위 클래스가 확장을 고려해서 설계되지 않았다면, 상속을 다시 고려해보아야 한다.
  - 상위 클래스 변경되면 하위 클래스 문제발생 가능성있음
- 상속을 고려한 클래스는 문서화 필요
  - 클래스 내부에서 스스로의 메서드를 어떻게 사용하는지 문서로 남겨야한다.(어떤 순서로 호출하는지, 각각의 호출 결과는 어떻게 되는지)

##### 컴포지션

- Has a 관계
- 기존 클래스가 새로운 클래스의 구성요소  & 여러 객체를 붙여서 한 객체를 구성하는 것을 의미



##### 상속에서 코드 순서  , 햇갈릴까봐 적어놓음

- subclass.addAll -> superclass.addAll -> subclass.add -> superclass.add

- superclass.addAll 실행시 add를 실행하는데 이는 subclass의 add를 실행

```java

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
      /*
      	결과
      	addall
        add
        Sub add
        Sub addAll
      */
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

```



위 코드와 같이 상속으로 코드를 구성하게되면 super 클래스에서 sub 클래스의 override된 메소드를 사용할 수 있어서 재대로 사용하지 않을 경우 위험함

- 내가 상위 하위 클래스를 모두 개발한 개발자라면 문제가 없지만 다른 패키지를 상속할 경우 해당 패키지에서 클래스 수정을 하면 내 시스템에 문제가 발생할 수 있다. 따라서 다른 패키지는 되도록 상속 하지 말고 사용하고 싶을 경우 컴포지션을 사용하자.



컴포지션 내용에 두 가지 항목이 있음

- 전달클래스 + 컴포지션 = 넓은 의미로 위임(delegation)
- 래퍼클래스 다른 클래스 인스턴스를 감싸고(wrap)있는 클래스



##### 전달 클래스 + 래퍼클래스

- 특정 구현된 Set을 받아 구성요소로 두고 해당 기능을 똑같이 사용
- 전달클래스를 상속 받아 사용할 경우 , 상속으로 인한 취약성을 보완 할 수 있다.

```java
public class ForwardingSet<E> implements Set<E> {
	private final Set<E> s;

	public ForwardingSet(Set<E> s) {
		this.s = s;
	}
	public void clear() {
		s.clear();
	}
	public boolean contains(Object o) {
		return s.contains(o);
	}
	public boolean isEmpty() {
		return s.isEmpty();
	}
	public int size() {
		return s.size();
	}
	public Iterator<E> iterator() {
		return s.iterator();
	}
	public boolean add(E e) {
		return s.add(e);
	}
	public boolean remove(Object o) {
		return s.remove(o);
	}
	...
}
public class InstrumentedSet<E> extends ForwardingSet<E> {
	private int addCount = 0;

	public InstrumentedSet(Set<E> s) {
		super(s);
	}

	@Override
	public boolean add(E e) {
		addCount++;
		return super.add(e);
	}

	@Override
	public boolean addAll(Collection<? extends E> c) {
		addCount += c.size();
		return super.addAll(c);
	}

	public int getAddCount() {
		return addCount;
	}

}
```



##### 상속 코드 

- HashSet의 addAll은 add를 사용하기때문에 두 번씩 더해짐

```java
public class InstrumentedHashSet<E> extends HashSet<E> {
	private int addCount = 0;
	public InstrumentedHashSet() {
	}

	public InstrumentedHashSet(int initCap, float loadFactor) {
		super(initCap, loadFactor);
	}

	@Override
	public boolean add(E e) {
		addCount++;
		return super.add(e);
	}

	@Override
	public boolean addAll(Collection<? extends E> c) {
		addCount += c.size();
		return super.addAll(c); //super.addAll에서 add를 사용!
	}

	public int getAddCount() {
		return addCount;
	}
}
```



##### 단순 래퍼클래스 

- httpServletRequest 클래스 등 자주 래퍼스 만든는 클래스들이 있음
- 래퍼클래스 메모리 문제 거의 없다고함 

```java
public class InstrumentedSet<E> {
	private int addCount = 0;
	private final Set<E> s;
	
	public InstrumentedSet(Set<E> s) {
		this.s=s;
	}

	public boolean add(E e) {
		addCount++;
		return s.add(e);
	}

	public boolean addAll(Collection<? extends E> c) {
		addCount += c.size();
		return s.addAll(c);
	}

	public int getAddCount() {
		return addCount;
	}

}
```



#### 결론 : 상속의 취약점을 피하려면 컴포지션과 전달을 사용하자.



### 19 상속을 고려해 설계하고 문서화하라. 그러지 않았다면 상속을 금지하라

상속용 클래스는 재정의할 수 있는 메서드들을 내부적으로 어떻게 이용하는지 문서로 남겨야한다.

`재정의할 수 있는 메서드` : public , protected 중 final이 아닌 모든 메서드

API 문서 끝 `Implementation Requirements` 로 시작 하는 절 : 메서드 내부 동작방식을 설명하는 곳

- @implSpec 태그를 붙여주면 자바독 도구가 생성해줌

- ```
  AbstractCollection 자바 문서
  
  isEmpty
  public boolean isEmpty()
  Returns true if this collection contains no elements.
  Specified by:
  	isEmpty in interface Collection<E>
  Implementation Requirements:
  	This implementation returns size() == 0.
  Returns:
  	true if this collection contains no elements
  
  ```

상속용 클래스는 반드시 하위 클래스를 만들어 검증 필요!!

상속용 클래스의 생성자는 직 , 간접적으로 재정의 기능 메소드를 호출 해서는 안된다.

- 하위 클래스 생성자보다 먼저 실행되므로 잘못된 작동을 할 확률이 높다.



#### 결론

- 상속시 문서화 
- 상속할 클래스만 상속으로사용, 상속용으로만들지 않은 클래스는 상속하지 말것!

### 20  추상 클래스 보다는 인터페이스를 우선하라

다중 인터페이스 extends 로 받아 새로운 인터페이스 생성 가능

```
public interface Singer{
	void sing(Song s);
}

public interface Songwriter{
	Song compose(int chartPosition);
}

public interface SingerSongwriter extends Singer,Songwriter{
	void strum();
}
```



추상 골격 구현(skeletal implementation) 

- 디폴트 메서드 구현
- 중요 메서드는 구현 안됨 (abstract)
- 관례상 이름 AbstractInterface
  - AbstractMap, AbstractList, AbstractSet ...



##### default메소드

- 인터페이스가 default키워드로 선언되면 메소드가 구현될 수 있다. 또한 이를 구현하는 클래스는 default메소드를 오버라이딩 할 수 있다.

```
    public interface Calculator {
        public int plus(int i, int j);
        public int multiple(int i, int j);
        default int exec(int i, int j){      //default로 선언함으로 메소드를 구현할 수 있다.
            return i + j;
        }
    }
```

### 결론

- 일반적 다중 구현용 타입으로는 인터페이스가 가장 적합  ,
  - 뭔말이지
- 복잡한 인터페이스라면 구현하는 수고를 덜어주는 골격 구현을 함께 제공 하는 방법이 좋다.
- 가능한 한 디폴트 메서드로 제공하여 그 인터페이스를 구현한 모든 곳에서 활용하도록 하는것이 좋다.
- 구현상의 제약으로 어려우면 골격 구현 추상 클래스로 제공하자.



### 21 인터페이스는 구현하는 쪽을 생각해 설계하라



- 기존에는 인터페이스에 메서드를 추가하면 구현체가 에러발생했다.(구현을 안했기때문에) 자바 8부터 디폴트 메서드가 나와서 인터페이스에 디폴트메서드를 추가 하더라도 구현체에 컴파일에러가 발생하지 않는다.
- 디폴트 메서드가 새롭게 인터페이스를 만드는데 유용하지만 기존 시스템에 추가하는 것은 꼭 필요한 경우가 아니라면 하지않는게 좋다.
  -  기존 구현체와 충돌나지 않을지 심사숙고하자 
  - 예로 나온것이 동기화 관련된 컬렉션인데 나중에 추가된 디폴트 메서드는 동기화 하지 않아 기존 구현의 의미를 깨뜨림
- 인터페이스 잘쓰자 라는 주제



### 22 인터페이스는 타입을 정의하는 용도로만 사용하라

- 상수 인터페이스 사용금지
  - Integer.MAX_VALUE 같이 특정 클래스나 인터페이스에 심히 연관된 상수는 괜찮음
  - 상수는 내부구현에 해당됨
  - 인터페이스에 넣으면 내부 구현을 API로 노출하는샘
  - 필요하면 상수 클래스 생성



### 23 태그 달린 클래스보다는 클래스 계층 구조를 활용하라

- 태그달린 -> 주석달린 클래스 즉 한 클래스안에 여러가지 기능이 있는 클래스
  - 예) 원과 사각형 기능을 가진 클래스로 radius 필드는 원, length ,height 필드는 사각형을 위한 필드 

- 계층 구조로 Figure 추상 클래스를 상속한 Circle , Ractangle 서브 클래스  형태가 더 좋다.
- 단일 책임 원칙



### 24 멤버클래스는 되도록 static으로 만들라

- 멤버클래스 => class 안 멤버로 class 있는것
- 내부 동작에 의해 변경이되는 클래스가 아닐경우(바깥 인스턴스와 독립적으로 존재) static class로 내부클래스를 생성하자
- static 을 안붙일 경우 내부 클래스는 바깥 인스턴스와 연결된다.
  - 인스턴스 안에 연결되기 때문에 인스턴스가 쓸데없는 메모리를 가짐
- static 없는 내부클래스를 잘못 참조할경우 메모리 누수가 생길수 있다.

#### 결론

- 중첩 클래스 4개 있음
- 메서드 밖에서도 사용하고 메서드 안에 정의하기 힘들면 멤버 클래스로 선언
  - 바깥 인스턴스와 독립적이면 static 아니면 non-static으로 선언

### 25 톱레벨 클래스는 한 파일에 하나만 담으라

#### 결론

하나의 자바파일(.java)에는 하나의 클래스만 넣어야 한다.



## 제네릭

### 26 로(raw) 타입은 사용하지 마라



Raw 타입 

- List<E> 에서 List가 raw 타입이라고함, 제네릭빼고 사용하는 구조

- List는 List<Object> 를 받을 수도 있고, List<String>도 받을 수 있다.

  - List != List<Object>, List<Object>가 더 하위 
  - 파라메터로 List에 List<String> 를 받아지지만 List<Object>에 List<String> 는 안된다. => List<Object>가 더 타입안정성이있다.

- List를 사용하면 어떤 List가 오더라도 받아진다. => 형변환 에러 가능성이 높아짐

- List<Object>는 모든 타입을 받겠다고 컴파일러에 명확히 전달

- 어떤 타입인지 알 필요없는 값을 파라메터로 받을 경우, 로타입 보단 비 한정적 와일드 카드를 사용하자.

  - 비한정적 와일드 카드(unbounded wildcard type) : ?

    - Set<?> 과같이 사용
    - 어떤 타입이라도 담을 수 있는 가장 범용적인 매개변수화 된 컬렉션
    - 로타입은 아무 원소나 넣을 수 있지만 Collection<?> 는 null 이외에 어떤 원소도 넣을 수 없다.

  - ```
    public void test(List A, List B){}
    public void test(List<?> A, List<?> B){}
    ```

- 로타입 사용처

  - instanceof 로타입 , A istanceof Set (O)
  - 로타입.class , List<?>.class (X)

- 로타입은 제네릭 이전 코드의 호환성때문에 남아있음

### 27 비검사 경고를 제거하라

- 컴파일시 경고 제거
  - -Xlint:uncheked
- 경고를 제거할 수 없지만 타입이 안전하다고 확신하면 @SuppressWarnings("unckecked")사용
  - 클래스 전체에도 사용할 수있지만 가능한한 좁은 범위 (메소드, 지역 변수)에서 사용하자



### 28 배열보단 리스트 사용하라

- 타입 안정성
  - Sub[] 는 Super[]의 하위 타입이 된다. 

  - List<Sub> 는 List<Super>의 하위 타입이 아니다.

  - ```
    // 런타입 에러
    Object[] objectArray =new Long[1];
    objectArray[0]="당연 에러";//ArrayStoreException 
    
    //컴파일 에러
    List<Object> ol =new ArrayList<Long>(); // 호환되지 않음
    ol.add("당연 에러");
    ```

- 배열은 런타임에도 자신이 담기로한 원소타입을 인지하고 확인한다. (위 코드에서 추가할때 ArrayStoreException 발생) 반면 제네릭은 타입정보가 런타임에는 소거 된다. 원소타입을 컴파일에서만 검사한다.

- 배열 실체화 (reify)

### 29 이왕이면 제네릭 타입으로 만들라

- 클라이언트에서 직접 형변환하는 것보다 제네릭을 쓰는게 더 안전하다.

### 30 이왕이면 제네릭 메소드로 만들라

- 제네릭 메소드 쓰라는 이야기인데, 제네릭에 대해서 따로정리함

### 31 한정적 와일드카드를 사용해 api 유연성을 높여라

- <? extends SuperClass> : SuperClass의 특정 자식으로 생각하고 코딩
- <?>  : 파라메터만 가능하고 추가 안됨 
- <? super SubClass> : SubClass의 부모 클래스(?)로 생각하고 코딩
  - <E extends Comparabe<? super E>> 
    - Comparable<? super E> : E의 super 클래스에서 Comparable 이 생성되었을 수 도 있어서 이런 형태로 사용

```
    public void doSomething(List<? extends MyClass> list) {
    	//MyClass의 자식은 MyClass로 형변가능
        for (MyClass e : list) { // Ok
            System.out.println(e);
        }
    }

    public void doSomething(List<? extends MyClass> list) {
    	//MyClass인스턴스를 MyClass 자식 컬렉션에 추가 못함
        list.add(new MyClass()); // Compile Error
    }

    public void doSomething(List<? super MyClass> list) {
    	//MyClass의 부모가 Object라고 할 경우, Object 컬렉션을 MyClass로 형변 불가능(강제 X)
        for (MyClass e : list) { // Compile Error
            System.out.println(e);
        }
    }

    public void doSomething(List<? super MyClass> list) {
    	//MyClass의 부모가 Object라고 할 경우, MyClass 인스턴스는 Object로 형변 가능
        list.add(new MyClass()); // Ok
    }
```



- 사용자가 와일드카드 타입을 신경써야 한다면 잘못된 API일 확률이 높다.

- PECS : producer-extends , consumer-super 
  - 생산자라면 < ? extends T> 소비자라면 <? super T> 사용
  - 오라클은 In Out으로 설명, 
    -  `copy(src, dest)`라는 메소드가 있다고 하자. 여기서 src는 데이터를 복사할 데이터를 제공하므로(생산) **In** 인자가 되고 dest는 다른 곳에서 사용할 데이터를 받아들이므로(소비) **Out** 인자가 되므로 In의 경우 **extends** 키워드를 사용하고 Out의 경우는 **super**를 사용하라고 한다.
  - 논리가 이해가 잘 안감
  - Comparable, Comparator는 모두 소비자라고함,.. 



### 32 제네릭과 가변인수를 함께 쓸 때는 신중하라

![자바의 와일드카드 서브타이핑](http://happinessoncode.com/images/java-generic-and-variance-1/Java_wildcard_subtyping.svg)

- | **공변성(covariant)**          | T’가 T의 서브타입이면, C<T’>는 C<T>의 서브타입이다. |
  | ------------------------------ | --------------------------------------------------- |
  | **반공변성(contravariant)**    | T’가 T의 서브타입이면, C<T>는 C<T’>의 서브타입이다. |
  | **무변성 \| 불변 (invariant)** | C<T>와 C<T’>는 아무 관계가 없다.                    |

- 배열은 공변임  Super 클래스, Sub 클래스, 
  
  - Super[] 는 Sub[] 의 슈퍼타입
- 제네릭은 불변임
  
  - List<Super> 와 List<Sub>는 서로 관계 X

#### 핵심정리

가변인수와 제네릭은 궁합이 좋지않음, 타입이 안전하다면 @SafeVarargs 사용

```
    static void dangerous(List<String>... stringLists) {
        List<Integer> integers = Collections.singletonList(41);
        //가변인수는 배열-> 공변 -> Object[]로 변경가능
        Object[] objects = stringLists;
        objects[0] = integers;              //힙 오염 발생
        String s = stringLists[0].get(0);   //ClassCastException
    }
    
    //Parent Class - Child Class (Parent 클래스를 상속)
    //Parent[] parents - Child[] children (상속 관계 - 공변)
    //List<Parent> parents - List<Child> children (상속 관계가 아님 - 불공변)
```



### 33 타입 안전 이종 컨테이너를 고려하라

타입 안전 이종 컨테이너 : 키값이 Class<T>를 주어 타입간 안전을 보장

- String.class == Class<String>
- 예제코드

```
public class Favorites{
    private Map<Class<?>, Object> favorites = new HashMap<>();
    public  <T> void putFavorite(Class<T> type, T instance){
        //favorites.put(Objects.requireNonNull(type), instance);
        //런타임 안전성 추가
        favorites.put(Objects.requireNonNull(type), type.cast(instance));
    }
    public  <T> T getFavorite(Class<T> type){
        return type.cast(favorites.get(type));
    }
}
```



## 6장 열거 타입과 애너테이션



### 34 int 상수 대신 열거 타입을 사용하라



- 아래가 int 상수

```
public static final int APPLE_FUJI = 0;
public static final int APPLE_PIPPIN = 1;
public static final int APPLE_GRANNY_SMITH =2;
public static final int  ...
```

- 열거형

```
public enum Apple {FUJI,PIPPIN,GRANNY_SMITH}
public enum Orange ...
```



- 열거형은 싱글톤
- 열거형은 근본적으로 불변이라 모든 필드 final

#### 핵심정리

- 열거형이 정수 상수보다 좋다. 더 읽기 쉽고 안전 강력
- 메서드 추가가능
- 상수별로 다르게 작동할경우 switch 문 대신 상수별 메서드 구현을 사용하자



### 35 ordinal 메서드 대신 인스턴스 필드를 사용하라

- ordinal 나중 추가 삭제에 위험

- ordinal 대신 인스턴스 필드사용

- ```
  enum Ensemble{
      SOLO(1), DUET(2), TRIPLE_QUARTET(12);
      
      private final int numberOfMusicians;
      Ensemble(int size){ this.numberOfMusicians = size; }
      public int numberOfMusicians(){ return numberOfMusicians; }
      
  }
  ```

- 

### 36 비트 필드 대신 EnumSet을 사용하라

```
public class Text{
  // 구닥다리 기법
  public static final int STYLE_BOLD = 1 << 0; // 1
  public static final int STYLE_ITALIC = 1 << 1; // 2
  public static final int STYLE_UNDERLINE = 1 << 2; // 4
  public static final int STYLE_STRIKETHROUGH = 1 << 3; // 5

  // 매개변수 styles는 0개 이상의 STYLE_ 상수를 비트별 OR 한 값
  public void applyStyle(int styles){...}
}
```

- text.applyStyles(STYLE_BOLD|STYLE_ITALIC); 메소드를 사용하면 겹치지 않게 사용할 수 있다.
- 하지만 더 나은 대안이 EnumSet임
  - EnumSet 내부는 비트 백터로 구현 => 비트 필드에 비견되는 성능을 보여줌
  - removeAll, retainAll 같은 대량 작업은 효율적으로 처리가능하게 구현

```
public class Text {
    public enum Style {BOLD, ITALIC, UNDERLINE, STRIKETHROUGH}

    // 어떤 Set을 넘겨도 되나, EnumSet이 가장 좋다.
    public void applyStyles(Set<Style> styles) {
        System.out.printf("Applying styles %s to text%n",
                Objects.requireNonNull(styles));
    }

    // 사용 예
    public static void main(String[] args) {
        Text text = new Text();
        text.applyStyles(EnumSet.of(Style.BOLD, Style.ITALIC));
    }
}
```

- 자바 9까진 불변 EnumSet이없다고함 
  - Collections.unmodifiableSet으로 감싸야함



### 37 ordinal 인덱싱 대신 EnumMap 을 사용하라

ordinal 절대쓰지말자

EnumMap특징

- Enum 타입을 키값으로 갖음

- 생성시 Enum.class가 필요함

  - new EnumMap<K,V>(Class<K>

  - ```
        Map<APPLE, String> enummap = new EnumMap<>(APPLE.class);
        enummap.put(APPLE.A, "A입니다.");
        enummap.put(APPLE.B, "B입니다.");
    
        enummap.forEach((k, v) -> System.out.println(v));
        // 결과
        // A입니다.
        // B입니다.
        
        //stream grouping시 다른 Map으로 생성되기때문에 enummap으로 생성하려면 아래코드와 같이 만들어야함
        //grouping시 enummap으로 생성
        EnumMap<APPLE, Set<APPLE>> collect = Arrays.stream(APPLE.values()).collect(
            Collectors.groupingBy(p -> p, () -> new EnumMap<>(APPLE.class), Collectors.toSet()));
    ```




### 38 확장할 수 있는 열거 타입이 필요하면 인터페이스를 사용하라

```
public interface Operation {
    double apply(double x, double y);
}

public enum BasicOperation implements Operation {

    PLUS("+") {
        @Override
        public double apply(double x, double y) {
            return x + y;
        }
    },
    MINUS("-") {
        @Override
        public double apply(double x, double y) {
            return x - y;
        }
    },
    TIMES("*") {
        @Override
        public double apply(double x, double y) {
            return x * y;
        }
    },
    DIVIDE("/") {
        @Override
        public double apply(double x, double y) {
            return x / y;
        }
    };

    private final String symbol;

    BasicOperation(String symbol) {
        this.symbol = symbol;
    }
}

public enum ExtendedOperation implements Operation {

    EXP("^") {
        @Override
        public double apply(double x, double y) {
            return Math.pow(x, y);
        }
    },
    REMAINDER("%") {
        @Override
        public double apply(double x, double y) {
            return x % y;
        }
    };

    private final String symbol;

    ExtendedOperation(String symbol) {
        this.symbol = symbol;
    }
}
```

#### 핵심정리

- 열거타입 자체는 확장 불가
- 인터페이스와 인터페이스를 구현하는 기본열거타입을 함께 사용해 같은 효과 가능



### 39 명명 패턴보다 애너테이션을 사용하라

- 일반적으로 도구 제작자아니고선 애너테이션 만들일은 없다.
- 자바개발자라면 기본 애너테이션 사용하자

### 40 @Override 애너테이션을 일관되게 사용하라

- @Override 애너테이션 쓰면 override 된지 알 수 있음 ,  부모에 없는메소드에 @Override하면 에러 발생
- equals(Object object)를 재정의하고 싶은데 equals(A a)으로 Overloading 하는 실수를 했다면 @Override에서 에러가 발생, 하지만 @Override를 안쓸경우 모르고 지나칠 수가 있다.

### 41 정의하려는 것이 타입이라면 마커 인터페이스를 사용하라

마커 인터페이스 : 자신을 구현하는 클래스가 특정 속성을 가짐을 표시하는 인터페이스

마커 애너테이션 : 위와 같은 의미

- 대표 마커 인터페이스 Serializable
- 마커에너테이션보다 좋은 부분 :
  - 마커인터페이스는 구현한 인스턴스들을 구분(인터페이스로) 할 수 있다.
  - 마커 애너테이션으로 런타임에 발견될 오류를 컴파일 과정에서 잡을 수 있다.
  - **마커 인터페이스에 해당하는 객체를 매개변수로 받는 메서드를 작성할 필요 있을때는 마커인터페이스 사용, 그외 모두 마커 애너테이션 사용**



## 람다와 스트림



### 42 익명클래스보단 람다 사용하라

- 익명 클래스보단 람다 사용
- **람다는 이름이 없고 문서화도 못함, 코드 자체동작이 명확히 설명되지 않거나 코드 줄 수가 많아지면 람다를 쓰지 말아야한다.**
- 람다는 한 줄일때 가장 좋고 세줄 넘어가면 가독성이 심하게 나빠짐

- 람다에서 this는 바깥 인스턴스, 익명클래스에서 this 는 익명 클래스 인스턴스 자신을 가리킴



### 43 람다보다는 메서드 참조를 사용하라

- `(x,y)->x+y` 대신 `Integer::sum`을 사용하면 간결하고 명확해짐
- `Function.identity()` 대신 `(x)->x` 가 명확함
- 가독성 차이가 있어서 자주 쓰는 메서드는 메서드 참조가 낫고, 그게아니면 람다가 쓰자



- 자주 쓰는 유형
  - 정적
    - Integer::parseInt
      - str -> Integer.parseInt(str)
  - 한정적(인스턴스)
    - Instant.now()::isAfter
      - Instant then = Instant.now();
      - t -> then.isAfter(t)
  - 비한정적(인스턴스)
    - String::toLowerCase
      - str -> str.toLowerCase()
  - 클래스 생성자
    - TreeMap<K,V>::new
      - () -> new TreeMap<K,V>()
  - 배열 생성자
    - int[]::new 
      - len -> new int[len]

### 44  표준 함수형 인터페이스를 사용하라

- 표준 함수형 인터페이스
  - UnaryOperator<T> 	
    - T 타입 받아서 T 타입 으로 나감
    - String::toLowerCase
  - BinaryOperator<T>
    - T타입 두개 받아서 T타입 하나로 나감
    - (T t1, T t2) -> T t
    - BigInteger::add
  - Predicate<T> 
    - T-> boolean
  - Function<T,R>
    - T->R
  - Supplier<T>
    - ()->T
  - Consumer<T>
    - (T)->{}

- 필요하면 직접 Functional Interface 직접 설계



### 45 스트림은 주의해서 사용하라



- 기존코드는 스프림으로 사용하도록 리팩터링하되, 새 코드가 더 나아보일때만 반영
- 스트림 사용하면 안되는 상황
  - 코드 블록안에서 지역변수를 읽고 수정할 수 있다. 하지만 람다에서는 final인 변수만 읽을 수 있다.
  - 코드 블록에서는 return , break, continue 문으로 블록 바깥의 반복문을 종료하거나 반복해서 건너뛸 수 있다. -> 람다 불가능
    - 기존 메서드 선언에서 Checked Exception 예외 던질 수 있는데 람다는 불가능하다고함. 확인 필요
  - 한데이터가 파이프라인의 여러 단계를 통과 할때 이 데이터의 각 단계에서의 값들에 동시에 접근하기는 어려운 경우다. ?
    - 이중 for문 그 이상인건 스트림하지말자
  - 위의 일을 수행한다면 람다 금지
- 스트림 적용하기에 좋은 로직
  - 원소들의 시퀀스를 일관되게 변환한다.
  - 원소들의 시퀀스를 필터링한다. 
  - 원소들의 시퀀스를 하나의 연산을 사용해 결합한다. (더하기, 연결하기, 최솟값 등)
  - 원소들의 컬렉션에 모은다.(공통된 속성으로)
  - 원소들의 시퀀스에서 특정 조건을 만족하는 원소를 찾는다.
- 스트림 for중에 어떤게 나은지모르면 둘다 해보고 더 나은쪽 선택하라



### 46 스트림에서는 부작용 없는 함수를 사용하라



- forEach 연산은 스트림 계산 결과를 보고할 때만 사용하고, 계산하는 데는 쓰지 말자
  - 물론 가끔 스트림 계산결과를 기존 컬렉션에 추가하는 등의 다른 용도로 쓸 수 있다.
- 스트림 잘 사용하려면 Collector를 잘 알아야한다.
  - Collector 설명은 인터넷에서보자

### 47  반환 타입으로는 스트림보다 컬렉션이다



- 멱집합 관련 처리가 처음봐서 정리
  - 이진법으로 처리가능
  - a,b,c 의 멱집합 {}, {a},{b},{c},{a,b},{a,c},{b,c},{a,b,c}
  -  원소 0 0 0 부터 1 1 1 까지 총 2^n-1 개
  - 최대 처리원소 integer인경우 2^31-1(Integer MAX_VALUE)로 제한이라 총 30개정도 원소까지 이방식으로 처리가능  

- 원소 시퀀스를 반환할때는 스트림으로 처리하기를 원하는사람과,  반복문으로 처리하기를 원하는 사람이 있다는것을 떠올리고 양쪽다 만족시키기자
- 반환은 List로 하고 원소가 적으면 ArrayList , 원소가크면 전용 컬렉션만들어서 사용
- 컬렉션을 반환이 힘들면 Iterable과 스트림중 더 자연스러운 것을 반환



### 48 병렬화는 주의해서 적용하라

- 병렬화 효과가 좋은 타입
  - ArrayList
  - HashMap
  - HashSet
  - ConcurrentHashMap
  - 배열
  - int 범위
  - long 범위

- Stream.iterate 또는 limit은 병렬화 X

- 적합 메소드

  - reduce
  - min,max,count,sum
  - anyMatch,AllMatch,noneMatch
  - 가변축소(mutable reduction)는 해당 안됨

- 병렬 스트림을 사용하기위한 조건

  - 원소들의 간섭X 
  - 동기화 X
  - 등 찾아보기 
  - 보통 모든 계산하여 마지막까지 원소 하나가 다른원소에 영향을 받지않고 독립적으로 작동할때 사용하는것이 좋아보임

- 무작위 랜덤 스트림 

  - ThreadLocalRandom
  - SplittableRandom
  - Random은 모든 연산을 동기화한다고해서 병렬에서는 절대 사용금지

  





## 8장 메서드



### 49 매개변수가 유효한지 검사하라

- 오류는 가능 한 빨리 잡아야 한다. (발생한 곳에서)

- **매서드 초기에 매개변서 null 체크 , 혹은 잘못된 데이터  유효성 체크**
  - 보통 에러
    - IllegalArgumentException
    - IndexOutOfBoundsException
    - NullPointerException
  - 매개변수가 잘못 됐을때 던지는 예외도 함께 기술 !
- 자바 9 에서는 `requireNonNull` 이외에 `checkFromIndexSize`, `checkFromToIndex`, `checkIndex` 검사 메서드 추가
  - null체크 이외에 잘 안쓸듯



### 50 적시에 방어적 복사본을 만들라

- Date는 불변 객체가 아니라서 외부에서 데이터 변경가능 , 사용 금지

  - ```
    import java.util.Date;
    
    class Period {
        private final Date start;
        private final Date end;
    
        public Period(Date start, Date end) {
            if(start.compareTo(end) > 0) {
                throw new IllegalArgumentException(start + " after " + end);
            }
            this.start = start;
            this.end = end;
        }
        public Date start() { return start; }
        public Date end() { return end; }
        // ... 생략
    }
    
    class Item50Test {
        public void someMethod() {
            Date start = new Date();
            Date end = new Date();
            Period period = new Period(start, end);
    
            // deprecated method
            // period의 내부를 수정했다.
            end().setMonth(3);
        }
    
        public static void main(String[] args) {
            Item50Test main = new Item50Test();
            main.someMethod();
        }
    }   
    ```

- ZonedDateTime/ LocalDateTime으로 대체 - 불변

- **복사본 데이터를 만들어 완전 무결성으로 해야함 **

  - ```
    class Period {
        private final Date start;
        private final Date end;
    
        public Period(Date start, Date end) {
            this.start = new Date(start.getTime());
            this.end = new Date(end.getTime());
    
            if(start.compareTo(end) > 0) {
                throw new IllegalArgumentException(start + " after " + end);
            }
        }
        public Date start() { 
            return new Date(start.getTime());
        }
        public Date end() { 
            return new Date(end.getTime());
        }
        // ... 생략
    }
    ```

  - 



#### 핵심 정리

- 클래스가 클라이언트로부터 받은 혹은 클라이언트로 반환하는 구성요소가 **가변** 이라면 그 요소는 반드시 방어적으로 복사해야 한다.!!!!!!!!

- 복사비용이크거나 (대형객체) 요소를 잘못 수정할 일이 없다면 방어적 복사를 수행하는 대신 해당 구성요소를 수정했을



### 51 매서드 시그니처를 신중히 설계하라

- 매서드 이름을 신중히 짓자
  - 같은 패키지에 속한 다른 이름들과 일관되게 짓는 게 최우선 목표
  - 긴 이름은 피하자
  - 커뮤니티에서 널리 받아들여지는 이름을 사용
- 편의 메서드를 너무 많이 만들지 말자
  - 메서드 많은 클래스는 공부하기어렵다.

- 매개변수 목록은 짦게 유지하자 4개 이하가 좋다.
  - **같은타입 매개변수가 여러개 나오는 것은 특히 해롭다.**
  - 과하게 긴 매개변수 목록을 짧게 줄여주는 3가지 기술
    1. 여러 매서드로 쪼갠다. ->메서드가 많아 질 수 있지만 직교성(orthogonality, 성분끼리 상관 x) 을 높여 오히려 메서드 수를 줄여주는 효과도 있다.
    2. 매개변수 여러 개를 묶어주는 static class를 생성한다.
    3. 매개변수가 많을때 builder 패턴을 사용
- 매개변수 타입으로 클래스보다 인터페이스가 더 낫다.
- **boolean 보단 원소 두 개 짜리 열거 타입이 낫다.**



### 52 다중정의(Overloading)는 신중히 사용하라

- 예제를 통해서 보는게 빠름

  - ```java
    public class CollectionClassifier {
        public static String classify(Set<?> s) {
            return "Set";
        }
    
        public static String classify(List<?> lst) {
            return "List";
        }
    
        public static String classify(Collection<?> c) {
            return "Unknown Collection";
        }
    
        public static void main(String[] args) {
            Collection<?>[] collections = {
                    new HashSet<>(),
                    new ArrayList<>(),
                    new HashMap<String, String>().values()
            };
    
            for (Collection<?> c : collections)
                System.out.println(classify(c));
        }
    }
    ```

  - 결과 Unknown Collection 만 세번 나옴 

  - 컴파일과정에서 어느 메서드 호출할지 결정난다고 그러는데 그것보다 Collection을 넣었으니 Unknown Collection이 나오는거 아닐가... 뭔가 당연한데 아니라고하니 이상함

  - 아래같이 정의 하는게 좋다고함 하나에서 나눠지는 방향

  - ```java
    //하나만 정의하고 안에서 타입에 따라 다르게 진행
    public static String classify(Collection<?> c) {
    		return c instanceof Set ? "Set" : c instanceof List ? "List" : "Unknown Collection";
    }
    ```

- Overriding 매커니즘

  - ```
    class Wine {
        String name() {
            return "wine";
        }
    }
    
    class SparklingWine extends Wine {
        @Override
        String name() {
            return "sparkling wine";
        }
    }
    
    class Champagne extends SparklingWine {
        @Override
        String name() {
            return "champagne";
        }
    }
    
    public class Overriding {
        public static void main(String[] args) {
            Wine[] wines = {
                    new Wine(), new SparklingWine(), new Champagne()
            };
            for (Wine wine : wines)
                System.out.println(wine.name());
        }
    }
    ```

  - 결과 : wine / sparkling wine / champagne 순으로 나옴 

- 다중정의 실수 사례

  - ```java
    public class SetList {
    
        public static void main(String[] args) {
            Set<Integer> set = new TreeSet<>();
            List<Integer> list = new ArrayList<>();
    
            for (int i = -3; i < 3; i++) {
                set.add(i);
                list.add(i);
            }
    
            for (int i = 0; i < 3; i++) {
                set.remove(i);
                list.remove(i);
            }
            System.out.println(set + " " + list);
        }
    }
    ```

  - 원했던 결과는 [-3,-2,-1] [-3,-2,-1] 이었는데 [-3,-2,-1] [-2,0,2] 가 출력

  - remove(int index) , remove(Object object)는 서로 다른 의미 , 앞에꺼는 인덱스를 삭제, 뒤에 꺼는 값을 삭제. 위의 경우에는 Integer로 형변환하여 사용

  - ```
     list.remove((Integer)i);
    ```

### 53 가변 인수는 신중히 사용하라



- 가변 변수는 배열을 생성

- 모든 값을 가변변수로 두어 매개변수 유효성 검사할 바엔 하나 분리

  - ```
    static int min(int... args) {
            if (args.length == 0)
                throw new IllegalArgumentException("인수가 1개 이상 필요합니다.");
            int min = args[0];
            for (int i = 1; i < args.length; i++)
                if (args[i] < min)
                    min = args[i];
            return min;
        }
    ```

  - 잘한것 같지 만 지저분함

  - ```
    static int min(int firstArg, int... remainingArgs) {
            int min = firstArg;
            for (int arg : remainingArgs)
                if (arg < min)
                    min = arg;
            return min;
        }
    ```

- 보통 5개 미만으로 95% 사용한다고할때 5개 정의

  - ```
    public void foo(){}
    public void foo(int a1){}
    public void foo(int a1,int a2){}
    public void foo(int a1,int a2,int a3){}
    public void foo(int a1,int a2,int a3,int... rest){}
    ```



### 54 null이 아닌, 빈 컬렉션이나 배열을 반환하라

- **반환을 null로 하면 사용하는 메서드에서 null 체크를 함**

  - ```java
    package com.github.sejoung.codetest.methods;
    
    import java.util.ArrayList;
    import java.util.List;
    
    public class NullCollectionsTest {
    
        private final static List<Cheese> cheesesInStock = new ArrayList<>();
    
    
        public List<Cheese> getCheeses() {
            return cheesesInStock.isEmpty() ? null : new ArrayList<>(cheesesInStock);
        }
    
        public static void main(String[] args) {
            NullCollectionsTest shop = new NullCollectionsTest();
            cheesesInStock.add(Cheese.STILTON);
    
            List<Cheese> cheeses = shop.getCheeses();
    
            if(cheeses != null && cheeses.contains(Cheese.STILTON)){
                System.out.println("좋았어 바로 그거야");
            }
        }
    }
    ```

  - null 리턴하면  `cheeses != null ` 체크를 해야함 

  - ```java
    //리스트
    public List<Cheese> getCheeses() {
        return cheesesInStock.isEmpty() ? Collections.emptyList() : new ArrayList<>(cheesesInStock);
        //아래처럼 계속 객체를 생성하는것보다 빈거면 빈 불변객체를 내보내는게 최적화에 좋다.
        //return new ArrayList<>(cheesesInStock);
        
    }
    //배열
    public Cheese[] getCheeses() {
        return cheesesInStock.toArray(new Cheese[0]);
    }
    ```

  - 



### 55 옵셔널 반환은 신중히 하라

- Optional 반환 메서드에서 null 반환은 절대금지. 
- `orElse` `orElseThrow` 등을 사용하여 유용하게 사용
- **컬렉션,스트림, 배열, 옵셔널 같은 컨테이너 타입은 옵셔널로 감싸면 안된다.!!!!**
- 기본형 OptionalInt, OptionalLong, OptionalDouble 타입이 있다.



#### 핵심정리

- 값을 반환하지 못할 가능성이 있고, 호출할 때마다 반환값이 없을 가능성을 염두에 둬야 하는 메서드라면 옵셔널을 반환 해야 할 상황 일 수 있다. 
- 하지만 옵셔널 반환에는 성능 저하가 뒤따르니 , 성능에 민감한 메서드라면 null을 반환하거나 예외를 던지는 편이나을 수 있다. 
- 그리고 옵셔널을 반환값 이외의 용도로 쓰는 경우는 매우 드물다



### 56 공개된 API 요소에는 항상 문서화 주석을 작성하라

- javadoc 사용
  - @throws : 예외 암시적 기술
  - @param : 매개변수 기술
  - @return : 반환타입 태그
  - {@code XXXX} : 코드용 폰트로 렌더링 및 HTML 메타문자 < 기호등 사용가능
    - 여러줄 일경우 <pre> {@code} </pre> 으로 사용
  - @implSpec : 해당 메서드와 하위 클래스 사이의 계약을 설명 ? , 그냥 메서드 설명
  - 
- 메서드용 문서화
  - 메서드와 클라이언트 사이의 규약을 명료하게 기술
  - **메서드가 어떻게 동작하는지 가아니라 무엇을 하는지 기술!**



- 더 문서화에 관련된 내용이 있음, 나중에 보기





## 일반적인 프로그래밍 원칙





### 57 지역변수의 범위를 최소화하라

- 지역변수의 범위를 줄이는 가장 강력한 기법은 `가장 처음 쓰일 때 선언 하기`다.

  - 먼곳에서 미리 선언하면 가독성이 떨어진다. => 변수 실행 시점에 초기값 ,타입이 기억안날 수 있다.

- 모든 지역변수는 선언과 동시에 초기화

  - try-catch문의 예외

    - 초기화 과정에서 에러 발생할 수 있다면 try 안에서 해야함

    - 기존에 하던대로 앞에서 null 값주고 try 안에서 처리

    - ```
      List<String> list = null
      try{
      	list = ...
      }
      ...
      ```

- while 대신 for를 써라

  - 반복문 뒤에 변수값을 써야하는게 아니라면 while보단 for가 좋다.

- 해당 챕터 특별한 내용은 없음



### 58 전통적인 for 문 보다는 for-each 문을 사용하라

- 사용못하는 조건만 정리함
  - 파괴적인 필터링(destructive filtering) : 컬렉션 순회하면서 원소 제거
    - removeIf로 해결
  - 변형(transforming) : 리스트나 배열 순회 하면서 그 원소값 일부 혹은 전체를 교체한다면 리스트 반복자나 인덱스를 사용해야함
  - 병렬 반복 : 여러 컬렉션을 병렬로 순회해야 한다면 각각의 반복자와 인덱스 변수를 사용해 엄격하고 명시적으로 제어해야 한다.
- for-each문은 Iterable 인터페이스를 구현한 객체라면 무엇이든 순회 가능



#### 핵심

- 순회 삭제 : removeIf
- 인덱스 필요 => for-each X

### 59 라이브러리를 익히고 사용하라

- Java 7부터는 Random 대신 ThreadLocalRandom 으로 대체
  - 빠름
  - 병렬 스트림에서는 SplittableRandom 을 사용
- 표준라이브러리 이점
  - 코드 작성한 전문가 지식과 여러분보다 앞서 사용한 다른 프로그래머들의 경험을 활용 가능
  - 핵심적인 일과 크게 관련 없는 문제를 핵결하느라 시간 허비 안해도됨
  - 따로 노력안해도 성능이 지속해서 개선
  - 기능이 점점 발전
  - 여러분이 작성한 코드가 많은 사람에게 낯익은 코드가 된다. => 유지보수, 가독성 등 이점
- 표준라이브러리가 있는데 만들어 사용하는 이유
  - 해당 기능이 있는지 모름
  - 메이저 릴리즈마다 주요 기능 확인 필요
    - 예 java9 에서 transferTo 추가됨
- 자바 개발자라면 java.lang , java.util, java.io는 익숙 해야함

### 60 정확한 답이 필요하면 float와 double은 피하라

- 부동소수점연산이라 근사치로 계산되도록 설계함 따라서 정확한 답을 원할때는 double, float 타입은 사용금지

- 금융계산에는 BigDecimal, int , long 만 사용

- BigDecimal 단점
  - 기본타입보다 쓰기 불편하고 느림
  - 단발성계산이라면 느린거는 괜찮음

### 61 박싱된 기본 타입보다는 기본 타입을 사용하라

- 박싱된 기본타입에 == 는 주소값 비교
- 박싱된 기본타입  초기값 null
  - null을 언박싱하면 NullPointerException 뜸
- 제네릭쓸때만 사용

### 62 다른 타입이 적절하다면 문자열 사용을 피하라

- 문자열은 열거타입을 대신하기에 적합하지 않다.
- 문자열을 혼합 타입을 대신하기에 적합하지 않다.



- 다른 타입을 확인하기 위해 문자열 파싱하여 비교함 



### 63 문자열 연결은 느리니 주의하라

- 문자열 연결은 모두 Stringbuilder로 변경됨 (자바 1.4?)

- 하지만 순회(for) 안에 문자열 더하기를 넣으면 StringBuilder도 여러개 생성됨 아래와 같은일이 없어야함

  - ```
    String AA = ""
    for(int i =0;i<100;i++){
    	AA+=i;
    }
    //컴파일
    for(int i =0;i<100;i++){
    	(new StringBuilder(String.valueOf(AA))).append(i).toString();  
    }
    
    //대체
    StringBuilder AA = new StringBuilder();
    for(int i =0;i<100;i++){
    	AA.append(i);  
    }
    ```



### 64 객체는 인터페이스를 사용해 참조하라

- `Set<Integer> set = new HashSet<>();` 다음과 같이 사용

- 의존성 낮춰 성능에 맞는 구현클래스 변경 가능

  - HashSet -> LinkedHashSet

- 인터페이스가 아닌 참조 

  - 적합한 인터페이스 없으면 클래스 참조
    - String/BigInteger 같은 경우
  - 적합한 인터페이스 없으면 추상 클래스 참조
    - OutputStream 같은 경우
  - 인터페이스에 없는 특별한 메서드를 제공하는 클래스
    - PriorityQueue 같은경우 Queue 에없는 comparator 메서드가 있음
    - 이런 추가 메소드 사용할 일 없으면 Queue로 사용

  

### 65 리플렉션보다는 인터페이스를 사용하라

- 리플렉션을 사용하면 임의의 클래스에 접글할 수 있다. 
- 이점
  - 컴파일 당시 없던 클래스도 이용가능
  - 어노테이션 사용가능
- 단점
  - 컴파일러가 주는 타입 검사 이점을 하나도 누릴 수 없다.
  - 리플렉션을 이용하면 코드가 지저분하고 장황해진다.
  - 성능이 떨어진다.
    - 일반 메서드 호출보다 느리다. 책에서 저자 테스트해본결과 11배 느리다고함

- 아주 제한된 형태로만 사용해야 그 단점을 피하고 이점만 취할 수 있다.

  - **리플렉션은 인스턴스 생성에만 사용하고 이렇게 만든 인스턴스는 인터페이스나 상위 클래스로 참조해 사용하자!!!**

  - ```java
    public static void main(String[] args) {
        
        // 클래스 이름을 Class 객체로 변환
        Class<? extends Set<String>> cl = null;
        try {
            cl = (Class<? extends Set<String>>) Class.forName(args[0]); //비검사 형변환
        } catch (ClassNotFoundException e) {
            fatalError("클래스를 찾을 수 없습니다.");
        }
        
        // 생성자를 얻는다.
        Constructor<? extends Set<String>> cons = null;
        try {
            cons = cl.getDeclaredConstructor();
        } catch (NoSuchMethodException e) {
            fatalError("매개변수 없는 생성자를 찾을 수 없습니다.");
        }
         
        //집합의 인스턴스를 만든다.
        Set<String> s = null;
        try {
            s = cons.newInstance();
        } catch (IllegalAccessException e) {
            fatalError("생성자에 접근할 수 없습니다.");
        } catch (InstantiationException e) {
            fatalError("클래스를 인스턴스화할 수 없습니다.");
        } catch (InvocationTargetException e) {
            fatalError("생성자가 예외를 던졌습니다: " + e.getCause());
        } catch (ClassCastException e) {
            fatalError("Set을 구현하지 않은 클래스입니다.");
        }
        
        //생성한 집합을 사용한다.
        s.addAll(Arrays.asList(args).subList(1, args.length));
        System.out.println(s);
    }
    
    private static void fatalError(String msg) {
        System.err.println(msg);
        System.exit(1);
    }
    ```

  - 

### 66 네이티브 메서드는 신중히 사용하라

- 성능목적으로 네이티브메서드 사용하는 것은 거의 권장안함
  - 성능상 정말 빠른 네이티브 라이브러리만 사용
- 단점
  - 디버깅 어려움
  - 주의하지 않으면 속도가 오히려 떨어짐
  - 메모리 오류
  - 이식성 낮음



### 67 최적화는 신중히 하라

모든 사람이 마음 깊이 새겨야 할 최적화 격언 세 대를 소개한다.

```
맹목적인 어리석음을 포함해 그 어떤 핑계보다 효율성이라는 이름 아래 행해진 컴퓨팅 죄악이 더 많다 (심지어 효율을 높이지도 못하면서)

- 윌리엄 울프(Wulf72)

(전체의 97% 정도인) 자그마한 효율성은 모두 잊자. 섣부른 최적화가 만악의 근원이다. - 도널드 크누스(Knuth74)

최적화를 할 때는 다음 두 규칙을 따르라.

첫 번째, 하지마라.
두 번째, (전문가 한정) 아직 하지 마라. 다시 말해, 완전히 명백하고 최적화되지 않은 해법을 찾을 때까지는 하지 마라.
- M.A 잭슨 (Jackson75)
```



- 성능 때문에 견고한 구조를 희생하지 말자. 빠른 프로그램보다는 좋은 프로그램을 작성하라
  - 좋은 프로그램은 정보 은닉 원칙을 따르므로 개별 구성요소의 내부를 독립적으로 설계할 수 있다.
- 아키텍처의 결함이 성능을 제한하는 상황이라면 시스템 전체를 다시 작성하지 않고는 해결하기 불가능 할 수 있다. 완성된 틀을 변경하려다보면 유지보수하거나 개선하기 어려운 꼬인 구조의 시스템이 만들어지기 쉽다.
- 성능문제는 설계 단계에서 반드시 염두해야함. 
- 성능을 제한하는 설계를 피하라
  - 완성 후 컴포넌트 끼리, 혹은 외부 시스템과의 소통 방식이 변경하기 어려움
    - API, 네트워크 프로토콜, 영구 저장용 데이터 포맷 등이 대표적
- API를 설계할 때 성능에 주는 영향을 고려하라
  - public 타입을 가변으로 만들면 불필요한 방어적 복사를 수없이 유발할 수 있다.
  - 컴포지션으로 해결할 수 있음에도 상속방식으로 설계한 public 클래스는 상위 클래스에 영원히 종속되면 그 성능 제약까지도 물려받게 된다.
  - 인터페이스도 있는데 굳이 구현타입을 사용하는 것 역시 좋지 않다.
  - 특정 구현체에 종속하게되어 더 좋은 구현체가 나오더라도 이용하지 못하게 된다.



- 최적화를 꼭 할 경우 전후 성능을 측정하라
- 프로파일링 도구는 최적화 노력을 어디에 집중해야 할지 찾는 데 도움을 준다.

#### 핵심정리

- 좋은프로그램을 작성하다보면 성능은 따라오게 마련
- 하지만 성능요소가 포함된(API, 네트워크 프로토콜, 영구 저장용 데이터 포맷 등이 대표적) 시스템 설계할때 성능을 염두해야한다.
- 구현을 완료하면 성능측정 충분히 빠르면 그걸로끝 아니면 프로파일링 도구로 원인지점 찾고 최적화
- 알고리즘을 잘못 골랐다면 저 수준 최적화는 아무리 해봐야 소용이 없다.



### 68 일반적으로 통용되는 명명 규칙을 따르라

- 패키지는 도메인 이름 역순
  - 표준 라이브러리는 java,javax로 시작
- 패키지각 요소는 8글자 이하의 짦은 단어
  - utilities 보단 util
  - 여러단어로 구성된 이름이라면 awt처름 첫글자 사용
- 클래스(열거, 애너테이션 포함), 인터페이스은 하나이상 단어고 각 단어는 대문자로 시작
- 메소드, 필드 첫문자 소문자로 사용
- 상수필드 모두 대문자 , _로 띄어쓰기 사용
- 지역변수 약어 사용가능
- 매개변수는 메서드 설명 문서에  까지 등장하는 만큼 일반 지역변수보다는 신경을 써야 한다.
- 타입 매개변수
  - 임의타입 T,
  - 원소타입 E
  - 맵 키 K , 값 V
  - 예외 X
  - 메서드 반환 타입 R
  - 그외 임의 타입의 시퀀스에는 T,U,V  혹은 T1,T2,T3 사용
- 객체를 생성할 수 있는 클래스의 이름은 보통 단수 명사, 명사구 사용
- 객체를 생성할 수 없는 클래스는 복수 명사
  - Collectors, Collections 등
- 어떤 동작을 수행하는 메서드의 이름은 동사(목적어 포함), 동사구로 짓는다.
  - append, drawImage
- boolean 값을 반환 하면 보통 is, 드물게 has로 시작
- 반환타입이 boolean이 아닌경우 get  + 동사구
- 객체 타입이 변경되는 메서드 toType
  - toString , toArray 등
- 객체의 내용이 다른 뷰로 보여주는 메소드는 asType 사용
  - asList 등
- 기본타입 반환하는 메서드는 보통 typeValue로 지음
  - intValue 등
- 정적 팩터리는 다양하지만 아래 경우를 많이 사용
  - from, of, valueOf, instance, getInstance, newInstance, getType,newType



## 예외



### 69 예외는 진짜 예외 상황에만 사용하라

- 예외는 예외상황에만 사용, 제어 흐름용으로 사용하면 안됨
- 잘 설계된 API라면 클라이언트가 정상적인 제어 흐름에서 예외를 사용할 일이 없게 해야 한다.
  - 상태 의존적 메서드를 제공하는 클래스는 '상태검사' 메서드도 함께 제공해야함
    - 예 Iterator의 next,와 hasNext
  - 상태 검사 메서드 대신 사용하는 방법은 올바르지 않은 상태일때 null 혹은 빈 옵셔널을 반환하는 방법
- 상태검사 메서드, 옵셔널, 특정 값 중 하나를 선택하는 지침
  1. 외부 동기화 없이 여러 쓰레드가 동시에 접근할 수 있거나 외부 요인으로 상태가 변할 수 있는 경우는 옵셔널이나 특정값을 사용한다. 상태검사메서드와 상태의존적메서드 호출 사이에 객체의 상태가 변할 수 있기 때문에
  2. 성능이 중요한 상황에서 상태검사 메서드가 상태의존적메서드의 작업 일부를 중복 수행 한다면 옵셔널이나 특정값을 선택한다.
  3. 다른 모든 경우엔 상태검사 메서드 방식이 조금 더 낫다고 할 수 있다. 가독성 및 오류찾기 쉬움, 상태검사메서드 호출을 깜빡 잊었다면 상태 의존적 메서드가 예외를 던져 버그를 확실히 드러낼것

### 70. 복구할 수 있는 상황에는 검사 예외를, 프로그래밍 오류에는 런타임 예외를 사용하라.

복구 = 예외처리로 생각하자. 에러발생 할 경우 에러처리를 흐름 복구, 회복이라고 한다.

- 호출하는 쪽에서 예외처리하리라 여겨지는 상황이라면 checked exception을 사용
  - checked exception과 unchecked exception을 구분하는 기본 규칙 
  - checked exception는 호출자가 그 예외를 catch나 throws처리하는것을 강제함(컴파일 에러) , 따라서 그 메서드를 호출 했을때 발생할 수 있는 유력한 결과임을 API 사용자에게 알려주는것이다. 달리말하면 API 사용자가 그 상황에서 회복해내라고 요구하는 것
    - IOException , SQLException 등
  - unchecked exception은 error와 runtime exception이 있다. 이 둘은 동작측면에서 다르지 않고, **이 둘은 프로그램에서 잡을 필요가 없거나 혹은 통상적으로 는 잡지 말아야한다.** 이 둘의 에러가 발생했다는건 복구가 불가능하거나 더 실행해봐야 득보다는 실이 많다는 뜻이다.
- 프로그래밍 오류를 나타낼 때는 런타임 예외를 사용하자.
  - 런타임예외 대부분은 전제조건을 만족하지 못할 때 발생
  - 전제조건위배란, 클라이언트가 해당 API의 명세에 기록된 제약을 지키지 못했다는 뜻
  - 예로 배열 인덱스는 0~n-1 ( n은 배열크기 )사이여야한다. 아니면 ArrayIndexOutOfBoundsException 발생
  - 이상의 조건에서 문제가 하나 있다면 , `복구할 수 있는 상황`인지 `프로그래밍오류`인지가 명확히 구분이 안됨
    - 예로 말도안되는 크기의 배열을 할당해 자원고갈이 발생한건지, 진짜 자원이 부족해서 발생한문제인지 모름
    - API 설계자 판단으로 복구가능하면 Checked , 불가능하면 Runtime을 사용하자
    - **어떤문제인지  확신이 어렵다면 Runtime Exception이 좋다.**
- 에러는 보통 JVM의 자원부족, 불변식깨짐 등 더 이상 수행을 계속할 수 없는 상황
  - Error 클래스를 상속해 하위 클래스 만드는 일은 자제
  - **즉 우리가 구현하는 Unchecked Throwable은 모두 RuntimeException 하위 클래스 여야한다. **
  - AssertionError 이외에 Error를 throw 문으로 직접 던지는 일도 없어야 한다.
- Exception , RuntimeException, Error 를 상속하지 않는 Throwable을 만들 수 있지만 이로울게 없으니 절대로 사용하지 말자.ㅔ



- **예외처리할때 복구가 가능하면 Checked Exception, 복구 불가능이면 Runtime Exception을 사용하자 ** 



### 71 필요 없는 검사 예외 사용은 피하라

- Checked Exception 은 잘 사용하면 프로그램 질이 높아짐
  
  - 프로그래머가 처리하여 안전성을 높임
  
- Checked Exception을 과하게 사용하면 오히려 쓰기 불편한 API가 된다.

- **Checked Exception을 사용하는 메서드는 스트림안에서 `직접` 사용할 수 없기 때문에 자바 8부터 부담이 커짐**

- Checked Exception 사용지침

  - API를 재대로 사용해도 발생할 수 있는 에러

  - 프로그래머가 의미 있는 조치를 취할 수 있는 경우

  - ```
    } catch(TheCheckedException e){
    	e.printStackTrace(); // 
    	System.exit(1);
    }
    
    } catch(TheCheckedException e){
    	throw new AssertionError(); //일어날 수 없다.
    }
    ```

  - 위와 같은 상황 아니라면 Runtime Exception으로 설계

- 리펙토링 두 가지 방법

  - **Checked Exception을 피하는 가장 쉬운 방법은 적절한 결과 타입을 담은 옵셔널을 반환하는 것이다.**

    - 이 방식의 단점은 예외 발생한 이유를 담을 수 없다.

  - Checked Exception 메소드를 두개로 나눠 Runtime Exception 으로 변경

    - 이 방식은 여러 쓰레드에서 상태를 변경할때 위험한 방법

    - ```
      //리펙토링 전, action은 checked exception 발생
      try {
      	obj.action(args);
      }catch(TheCheckedException e){
      	...// 예외 상황에 대처한다.
      }
      
      // 리펙토링 후
      
      if(obj.actionPermitted(args)){
      	obj.action(args);
      }else{
         ...//예외상황 대처
      }
      
      // 쓰레드가 중단되길 원하면 한줄로 끝
      obj.action(args);
      
      ```

      

### 72 표준 예외를 사용하라

- Java library는 대부분 API에서 쓰기에 충분한 수의 예외를 제공한다.
- 표준 예외
  - IllegalArgumenetException
    - 호출자가 파라메터를 부적절한 값을 넘길때 발생
    - 예 : 반복횟수에 음수를 건넴
    - 가장 많이 재사용되는 예외
  - IllegalStateException 
    - 객체(this)의 상태가 호출된 메서드를 수행하기에 적합하지 않음
    - 예 : 초기화되지 않은 객체
  - NullPointerException
    - Null값을 허용하지 않는 메소드에서 null을 보내면 IllegalArgumentException대신 NullPointerException을 던짐
  - IndexOutOfBoundsException
    - 어떤 시퀀스의 허용 범위를 넘는 값을 건넬 때도 IllegalArgumentException 보단 IndexOutOfBoundsException 던짐
  - ConcurrentModificationException
    - 단일쓰레드 사용할려고 설계한 객체를 여러쓰레드가 동시에 수정하려고할때 발생
  - UnsupportedOperationException
    - 클라이언트가 요청한 동작을 대상 객체가 지원하지 않을 때 던짐
    - 예 : List 구현체 중 추가만 하는 구현체가 있을 경우, remove 사용하면 해당 에러 발생
- **Exception,RuntimeException,Throwable,Error는 직접 재사용하지말자, 이들은 추상클래스라고 생각하자.**
- IllegalArgumenetException , IllegalStateException  규칙
  - 위 두 예외를 선택하기 힘들때가 있다. 예로 카드 덱을 리스트로 가지고 있고 덱에서 주어진 파라메터만큼 뽑는 메서드를 제공할때, 덱의 수보다 더 많은 값을 요구하면 어떤에러를 발생시킬지 고민이다.
    - 메서드를 수행못하는 원인이 this 상태에 있는건지, 파라메터가 잘못된건지 모를때 
  - **파라메터가 값이 무엇이든 어차피 실패했을 거라면 IllegalStateException  , 그렇지 않으면 IllegalArgumenetException  사용한다.**

### 73 추상화 수준에 맞는 예외를 던지라

- 메서드가 고수준 처리할때 저수준 예외를 처리하지않고 바깥으로 전파해버리면 고수준 메서드를 다루는데 저수준 예외처리를 해야한다.

- 이 문제를 피하려면 **상위 계층에서는 저수준 예외를 잡아 자신의 추상화 수준에 맞는 예외롤 바꿔 던져야한다. 이를 예외 번역(Exception Translation)이라 한다.**

- ```
  try{
  
  }catch (LowerLevelException e){
  	throw new HigherLevelException(...);
  }
  ```

- **예외를 번역할때 저수준 예외가 디버깅에 도움이 된다면 예외연쇄(exception chaning)을 사용하는 게 좋다.**

  - ```
    }catch (LowerLevelException e){
    	throw new HigherLevelException(e);
    }
    ```

- 무턱대고 예외를 전파하는 것보다야 예외 번역이 우수한 방법이지만 그렇다고 남용해서는 안된다.

  - 저수준 메서드가 반드시 성공하도록 하여 아래계층에서는 예외가 발생하지 않도록 하는것이 최선-
  - 또는 상위 계층 메서드에서 매개변수 값을 아래계층에 넘기기전에 미리 검사하는 방법으로 이 목적을 달성 할 수 있다.

### 74 메서드가 던지는 모든 예외를 문서화하라

- 검사 예외는 항상 따로따로 선언하고, 각 예외가 발생하는 상황을 자바독 @throws 태그를 사용하여 정확히 문서화하자.
  - **공통 상위 클래스 ,즉 Exception나 Throwable을 던진다고 선언해서는 안 된다.**
  - Unchecked Exception이나 CheckedException 모두 @throws 태그에 문서화한다.
  - Unchecked Exception은 메서드의 throws 목록에 넣지 말자. 그래야 @throws 태그에 있고 throws에 없으면 Unchecked Exception, 둘 다 있으면 Checked Exception 인지 알 수 있다.

- 모든 메서드에서 공통되게 던지는 예외가 있다면 class 설명에 추가하는 방법도 있다.

 

### 75 예외의 상세 메시지에 실패 관련 정보를 담으라

- 예외를 잡지못할경우 예외객체의 toString 값을 표현하는것이 stack trace 이다.

- 예외의 toString값에 실패 원인에 관한 정보를 가능한 한 많이 담아 반환하는 일은 아주 중요하다,!!!!!!

- **실패 순간 포착하려면 발생한 예외에 관여된 모든 매개변수와 필드의 값을 실패 메시지에 담아야 한다.**

  - IndexOutOfBoundsException 의 경우 범위의 최대, 최소, 입력된 인덱스값을 모두 담아야한다.

  - 다음처럼 구현하는게 좋았을것이다.

  - ```
        	/**
             * IndexOutOfBoundsException을 생성한다
             * @param lowerBound 인덱스의 최솟값
             * @param upperBound 인덱스의 최댓값 + 1
             * @param index 인덱스의 실젯값
             */
          public IndexOutOfBoundsException(int lowerBound, int upperBound, int index)
          	{
               // 실패를 포착하는 상세 메시지를 생성한다.
               super(String.format("최솟값: %d , 최댓값: %d, 인덱스: %d", lowerBound, upperBound, index));
               
               // 프로그램에서 이용할 수 있도록 실패 정보를 저장해둔다.
               this.lowerBound = lowerBound;
               this.upperBound = upperBound;
               this.index = index;
        	}
    ```

- 예외 메세지는 장황할 필요가 없다. 최종 사용자에게 보여지는 메세지가아니라 개발자가 소스코드를 보면서 확인하는 것이라 가독성이 좋게 보여주기만 하면된다.

### 76 가능한 한 실패 원자적으로 만들라

- **호출된 메서드가 실패하더라도 해당 객체는 메서드 호출 전 상태를 유지해야 한다. 이러한 특성을 실패 원자적 이라고 한다.**
- 실패원자적 만드는 방법
  - 불변 객체로 설계
  - 가변 객체일때는 작업 수행전 파라메터 유효성 검사하는 방법
  - 객체의 임시 복사본에서 작업한다음 성공하면 원래객체와 교체하는 방법
    - 리스트를 배열로 교체하여 작업한다음 리스트로 덮어씌움 , 성능까지 좋음
  - 작업도중 발생하는 실패를 가로채는 복구코드를 작성하여 작업 전 상태로 되돌리는 방법
    - 주로 디스크 기반의 내구성을 보장해야하는 자료구조에서 쓰임
- 실패원자성을 만드는데 비용이 클 경우 안할 수도 있지만 최대한 하는게 좋다.

### 77 예외를 무시하지 말라

- try-catch로 잡고 아무것도 안하는짓은 하지말자.

- catch 블록을 비워두면 예외가 존재할 이유가 없다.

- 예외를 무시해도 될때가 있다. 

  - FileInputStream을 닫을때  파일의 상태를 변경하지 않았으니 복구할 것이 없으며, 닫는다는건 필요한 정보를 다 읽었다는 뜻이니 남은 작업을 중단할 이유도 없다.

  - 어쨌든 예외를 무시하기로 했다면 catch문안에 로그를 남기고 예외 변수를 ignored로한다.

    - ```
      catch(XXXException ignored){
      	//log
      }
      ```

- 오류를 무시하지않고 바깥으로 전파만되게 남겨도 디버깅을 할 수있다.



## 동시성

몇 가지 동시성에 필요한 지식 추가

#### CPU 캐시와 메모리 

[참조](https://parkcheolu.tistory.com/14)

보통, CPU가 메인 메모리로의 접근을 필요로 할 때, CPU는 메인 메모리의 일부분을 CPU 캐시로 읽어들일다(RAM -> Cache). 그리고 이 캐시의 일부분을 자신의 내부 레지스터로 다시 읽어들이고(Cache -> CPU Registers), 이 읽어들인 데이터로 명령을 수행한다. 후에 이 데이터를 다시 메인 메모리에 저장(writing)하기 위해서는 데이터를 읽어들일 때의 과정을 역순으로 밟는다. 작업 결과를 레지스터에서 캐시로 보내고, 적절한 시점에 캐시에서 메인 메모리로 보낸다.

캐시가 데이터를 메인 메모리로 보내는 적절한 시점이란, CPU가 캐시 메모리에 다른 데이터를 저장해야 할 때이다. CPU 캐시는 자신의 메모리에 데이터를 한 번 저장할 때(at a time), 메모리의 일부에 데이터를 저장해둘 수 있고, 또 일부분만을 보내는(flush) 일도 가능하다. 즉 캐시가 데이터를 읽거나 쓸 때, 반드시 한번에 캐시 메모리의 모든 데이터를 처리하지 않아도 된다는 말이다. 보통 캐시는 '캐시 라인(cache lines)' 이라고 불리는 작은 메모리 블록에 데이터를 갱신한다. 캐시 메모리에는 한 줄 이상의 캐시 라인을 읽어들일 수 있고, 반대로 한 줄 이상의 캐시 라인을 메인 메모리로 보낼(저장할) 수도 있다.



- **내가 잘못 알고 있던 부분이 항상 메모리에 저장될 것이라고 생각했음**

  - 예로

    ```
    Thread main 
    	i = 0;
    	Thread A & B start
    Thread A
    	while(1초마다)
    		i++;
    Thread B 
    	while(1초마다)
    		System.out.print(i);
    ```

    A 스레드가 i++할때 메인메소드에 저장하지 않고 CPU 캐시에서 처리중일때 B 스레드는 계속 0을 읽을 수 있다. 즉non-volatile일 경우 최신값에 대해서 보장을 못함

### 78 공유 중인 가변 데이터는 동기화해 사용하라.

- 동기화(synchronized)는 배타적 실행뿐만 아니라 스레드 사이의 안정적인 통신에 꼭 필요하다.

- Thread.stop 메서드는 완전하지 않아 사용하지말자 .!

  - 아래와 같이 코딩하면 백그라운드 스레드가 메인스레드가 변수를 변경한 것을 언제 보게될지 보장할 수없다.

  - ```
    아래와 같이 코딩하면 1초 후에 끝날 것이라고 판단하지만 JVM 최적화로 영원히 안끝날수있다.
    public class StopThread {
        private static boolean stopRequested;
        public static void main(String[] args) throws InterruptedException {
            Thread backgroundThread = new Thread(() -> {
                int i = 0;
                while(!stopRequested) {
                    i++;
                }
            });
            backgroundThread.start();
            TimeUnit.SECONDS.sleep(1);
            stopRequested = true;
        }
    }
    //JVM 이 최적화하여 다음과 같이 변할 수 있다. 그러면 끝나지 않음
    new Thread(()->{
    	int i=0;
    	if(!stopRequested){
    		while(true)
    			i++;
    	}
    })
    ```

  - 다음과 같이 공유자원을 동기화해 접근하면 이 문제를 해결할  수 있다.

  - ```
    public class StopThread {
        private static boolean stopRequested;
        private static synchronized void requestStop() {
            stopRequested = true;
        }
       
        private static synchronized boolean stopRequested() {
            return stopRequested;
        }
        public static void main(String[] args) throws InterruptedException {
            Thread backgroundThread = new Thread(() -> {
                int i = 0;
                while(!stopRequested()) {
                    i++;
                }
            });
            backgroundThread.start();
            TimeUnit.SECONDS.sleep(1);
            stopRequested();
        }
    }
    ```

  - **쓰기 읽기 모두 동기화 하지 않으면 동작을 보장하지 않는다.**

  - 반복문에서 매번 동기화하는 비용이 클경우 대안으로 volatile으로 선언

    - volatile은 배타적 수행과는 상관없고 항상 최근에 기록된 값을 읽게 됨을 보장

  - ```
    public class StopThread {
    
        private static volatile boolean stopRequested;
    
        public static void main(String[] args) throws InterruptedException {
    
            Thread backgroundThread = new Thread(() -> {
                int i = 0;
                while(!stopRequested) {
                    i++;
                }
            });
    
            backgroundThread.start();
    
            TimeUnit.SECONDS.sleep(1);
            stopRequested = true;
        }
    }
    ```

- 가변데이터는 공유하지말고 불변데이터만 공유하자 ,즉 가변데이터는 단일 스레드 에서만 사용하자.

  - 가변데이터를 공유할 경우 synchronized 사용하자

​	

#### 개인 정리

- volatile 변수는 변수의 write가 원자성을 보장한 상태에서 변수의 최신 데이터 read를 보장한다. 이를 가시성(visibility) 보장한다고한다. 즉 다시말해서 한 쓰래드에서 volatile 변수의 값을 읽고 쓰고, 다른 쓰래드에서는 오직 변수 값을 읽기만 할 경우, 그러면 읽는 쓰래드에서는 volatile 변수의 가장 최근에 쓰여진 값을 보는 것을 보장할 수 있습니다.
- volatile은  write의 병행제어는 보장하지 않는다.
- write & read를 보장하는것이 synchronized 이다.
- CPU 캐시와 메모리에 대해서 잘 모르고 있었기에 아래 정리함



### 79 과도한 동기화는 피해라

- 관련된 코드가 많은 파트로 직접보는것을 권함

- **synchronized 블록 안의 메소드를 클라이언트에 양도하면 안된다.**

  - ```
    API 구성
    method A(parameter a){
    	synchronized {
            a.method B 실행
        }
    }
    
    클라이언트가 method B를 구현
    ```

  - 위와 같이 method A 내부 synchronized 블록에서 a의 method B를 실행하면 이 부분은 클라이언트에게 제어를 양도한 것이다.

  - synchronized 블록을 만들때는 제어 양도를 하면 안된다.!

  - 잘못할 경우 교착상태빠지거나 데이터가 회손될 수 있다.

- CopyOnWriteArrayList는 수정은 드물고 순회만 빈번히 일어나는 관찰자 리스트 용도로 좋다.

- **기본 규칙은 동기화 블록은 가능한 한 일을 적게 하는 것이다.**

- StringBuffer 동기화 버전, StringBuilder non 동기화

- Random 동기화 , ThreadLocalRandom non 동기화

- 사실 동기화 프로그래밍을 당장 많이 다루지는 않을 것 같아서 간단하게 요약함, 자세한건 병행제어 책을 읽자.

### 80 스레드보다는 실행자(Executor), 테스크(Task), 스트림을 애용하라.

#### Executor에 관한 설명이 우선

- 참고 
  - https://javacan.tistory.com/entry/134
    - Executor에 대해서 잘 설명이 되있다.
    - 대부분 복사해옴

- new Thread 보다 Executor를 쓰는 이유

  - ```
    while(true) {
        request = acceptRequest();
        Runnable requestHandler = new RequestHandler(request);
        new Thread(requestHandler).start();
    }
    ```

  - 위 코드가 논리적으로 문제점은 없지만, 다음과 같은 성능상의 문제점을 안고 있다.

    - 소규모의 많은 요청이 들어올 경우 쓰레드 생성 및 종료에 따른 오버헤드가 발생한다.
    - 생성되는 쓰레드 개수에 제한이 없기 때문에 OutOfMemoryError가 발생할 수 있다.
    - 많은 수의 쓰레드가 실행될 경우, 쓰레드 스케줄링에 따른 오버헤드가 발생한다.

- 관련 클래스 구조

  - ![img](https://t1.daumcdn.net/tistoryfile/fs15/32_tistory_2008_12_09_05_48_493d8816427ce?x-content-disposition=inline)

  - Executor 인터페이스:
    제공된 작업(**Runnable 구현체**)을 실행하는 객체가 구현해야 할 인터페이스. 이 인터페이스는 작업을 제공하는 코드와 작업을 실행하는 메커니즘의 사이의 커플링을 제거해준다.

  - ExecutorService 인터페이스:
    Executor의 **라이프사이클을 관리할 수 있는 기능**을 정의하고 있다. Runnable 뿐만 아니라 **Callable**을 작업으로 사용할 수 있는 메소드가 추가로 제공된다.

    - Executor의 라이프 사이클을 관리

      - void shutdown():
        셧다운 한다. 이미 Executor에 제공된 작업은 실행되지만, 새로운 작업은 수용하지 않는다.
      - List<Runnable> shutdownNow():
        현재 실행중인 모든 작업을 중지시키고, 대기중인 작업을 멈추고, 현재 실행되기 위해 대기중인 작업 목록을 리턴한다.
      - boolean isShutdown():
        Executor가 셧다운 되었는 지의 여부를 확인한다.
      - boolean isTerminated():
        셧다운 실행 후 모든 작업이 종료되었는 지의 여부를 확인한다.
      - boolean awaitTermination(long timeout, TimeUnit unit):
        셧다운을 실행한 뒤, 지정한 시간 동안 모든 작업이 종료될 때 까지 대기한다. 지정한 시간 이내에서 실행중인 모든 작업이 종료되면 true를 리턴하고, 여전히 실행중인 작업이 남아 있다면 false를 리턴한다.

    - Callable을 작업으로 사용하기 위한 메소드

      - ```
        // Callable 구현
        public class CallableImpl implements Callable<Integer> {
            public Integer call() throws Exception {
                // 작업 처리
                return result;
            }
        }
        
        ExecutorService executor = Executors.newFixedThreadPool(THREADCOUNT);
        …
        //submit으로 실행
        Future<Integer> future = executor.submit(new CallableImpl());
        Integer result = future.get();
        ```

      -  Future.get() 메소드는 Callable.call() 메소드의 실행이 완료될 때 까지 블록킹

    - ThreadPoolExecutor는 ExecutorService의 하위 클래스

      - Executors에 없는 Executor를 사용할려면 직접 ThreadPoolExecutor을 생성해도된다. 
      - 생성자 옵션
        - corePoolSize : 풀 사이즈를 의미한다. 최초 생성되는 스레드 사이즈이며 해당 사이즈로 스레드가 유지된다. 해당 Job의 맞게 적절히 선택해야 한다. 많다고 성능이 잘나오는 것도 아니고 적다고 안나오는 것도 아니다. 충분히 테스트하면서 적절한 개수를 선택해야 한다.
        - maximumPoolSize : 해당 풀에 최대로 유지할 수 있는 개수를 의미한다. 이 역시 Job에 맞게 적절히 선택해야 한다.
        - keepAliveTime : corePoolSize보다 스레드가 많아졌을 경우 maximumPoolSize까지 스레드가 생성이 되는데 keepAliveTime 시간만큼 유지했다가 다시 corePoolSize 로 유지되는 시간을 의미한다. (그렇다고 무조건 maximumPoolSize까지 생성되는 건 아니다.)
        - unit : keepAliveTime 의 시간 단위를 의미한다.
        - workQueue : corePoolSize보다 스레드가 많아졌을 경우, 남는 스레드가 없을 경우 해당 큐에 담는다.

    - Spring을 사용한다면 `ThreadPoolTaskExecutor`를 살펴보는 것도 좋다. 내부 구현은 `ThreadPoolExecutor`로 구현되어 있다. ThreadPoolExecutor 보다 조금 더 간편하며, 추가적인 return 타입도 있다.

      ```
      @Bean
      public Executor threadPoolTaskExecutor() {
          ThreadPoolTaskExecutor executor = new ThreadPoolTaskExecutor();
          executor.setCorePoolSize(10);
          executor.setQueueCapacity(100);
          executor.setMaxPoolSize(30);
          executor.set...
          return executor;
      }
      ```

  - ScheduledExecutorService:
    지정한 **스케쥴**에 따라 작업을 수행할 수 있는 기능이 추가되었다.

  - Executors 유틸리티 클래스

    - ExecutorService newFixedThreadPool(int nThreads)
      최대 지정한 개수 만큼의 쓰레드를 가질 수 있는 쓰레드 풀을 생성한다. 실제 생성되는 객체는 ThreadPoolExecutor 객체이다.
    - ScheduledExecutorService newScheduledThreadPool(int corePoolSize)
      지정한 개수만큼 쓰레드가 유지되는 스케줄 가능한 쓰레드 풀을 생성한다. 실제 생성되는 객체는 ScheduledThreadPoolExecutor 객체이다.
    - ExecutorService newSingleThreadExecutor()
      하나의 쓰레드만 사용하는 ExecutorService를 생성한다.
    - ScheduledExecutorService newSingleThreadScheduledExecutor()
      하나의 쓰레드만 사용하는 ScheduledExecutorService를 생성한다.
    - ExecutorService newCachedThreadPool()
      필요할 때 마다 쓰레드를 생성하는 쓰레드 풀을 생성한다. 이미 생성된 쓰레드의 경우 재사용된다. 실제 생성되는 객체는 ThreadPoolExecutor 객체이다.

- 사용 방법

  - ```
    //상황에 알맞는 ExecutorService 생성
    ExecutorService exec = Executors.newSingleThreadExecutor();
    
    //runnable 실행
    exec.execute(() -> {
    	    System.out.println(Thread.currentThread());
    	});
    	
    //callable 실행
    Future<String> fut = exec.submit(() -> Thread.currentThread().toString());
    
    //Executor 종료
    exec.shutdown();
    ```

- 작은 프로그램이나 가벼운 서버라면 newCashedThreadPool이 일반적으로 좋은 선택

  - CashedThreadPool은 요청받은 태스크들이 큐에 쌓이지 않고 즉시 스레드에 위임돼 실행된다. 가용된 스레드가 없다면 하나 생성한다 , 서버가 무겁다면 CPU 이용률이 100%까지 올라간다.

- 무거운 서버라면 newFixedThreadPool이나 완전히 통제 가능한 ThreadPoolExecutor를 직접 사용한다.

- 자바 7부터 fork-join태스크를 지원, ForkJoinTask는 ForkJoinPool이라는 특별한 ExecutorService를 실행해주고 ForkJoinTask의 인스턴스는 작은 하위 태스크로 나뉠 수 있고 ForkJoinPool을 구성하는 스레드들이 이 태스크들을 처리하며, 일을 먼저 끝낸 스레드는 다른 스레드의 남은 태스크를 가져와 대신 처리할 수도 있다.

  - 스트림의 병행제어가 이 태스크를 사용한다. 따라서 포크조인태스크에 적당한 스트림은 빠르게 처리되지만 포크조인에 적절하지않은 태스크는 오히려 오래걸릴 수 있다.

  





### 81 wait와 notify보다는 동시성 유틸리티를 애용하라

- wait이나 notify는 사용하기 어려우니 고수준 동시성 유틸리티를 사용하자.
- java.util.concurrent 고수준 유틸리티는 세 가지 범주로 나뉨
  - Executor  : 80 챕터에서 설명
  - Concurrent Collection : 아래다룸
  - Synchronizer : 아래다룸

#### Concurrent Collection

- Concurrent Collection은 List,Queue,Map 같은 표준 컬렉션을 Concurent를 가미해 구현한 고성능 컬렉션이다. 높은 동시성을 도달하기 위해 각자의 내부에서 동기화를 수행
- Concurrent Collection에서 Concurrent를 무력화 하는 것은 불가능하며, 외부에서 락을 추가로 사용하면 오히려 속도가 느려짐.
- **Concurrent 컬렉션은 동기화한 컬렉션을 낡은 유산으로 만들었음, Collections.synchronizedMap 보단 ConcurrentHashMap사용하는것이 훨씬 좋다.**
- Queue를 확장한 BlockingQueue 는 take라는 메서드가있는데 이 메서드는 첫번째원소를 가져오지만 큐가 비었다면 새로운 원소가 올때까지 blocking 상태로 있는다.
  - 하나 이상의 producer 스레드가 큐에 put 하고, 하나 이상의 consumer가 큐에 있는 작업을 꺼내 처리하는 형태
  - ThreadPoolExecutor를 포함한 대부분 ExecutorService에서 사용한다.



#### Synchronizer

- 동기화 장치는 스레드가 다른 스레드를 기다릴 수 있게 하여 서로 작업을 조율할 수 있게 해줌
- 