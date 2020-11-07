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

