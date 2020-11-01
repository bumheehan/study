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
  - 상황에 따라 WeakHashMap 사용
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

      

