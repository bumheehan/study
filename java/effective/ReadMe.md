# Effective Java

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

