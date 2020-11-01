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

Java 8이전 `Type`인 인터페이스를 반환 하는 정적 메서드가 필요하면 `Types`라는 (인스턴스 불가) 동반 클래스(Companion Class)를 만들어 정의하는것이 관례

- Collection 인터페이스의 하위 타입의 객체를 반환하는 정적메서드 Collections
