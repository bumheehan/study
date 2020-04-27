# 람다식 (Lambda Expression)

메소드 : 객체지향에서는 객체의 행위나 동작이나 행동을 의미하는 용어로 사용 , 함수와 같은 의미지만 클래스 안에 있어야하는 제약조건이 추가
함수 : 람다식을통해 메소드가 하나의 독립적인 기능을 하게 되면서 함수용어 사용

## 람다식 문법

1. 기존 구조

    ```java
    int max(int a, int b){
        return a>b?a:b;
    }
    ```

2. 람다식 변환1 : 반환타입, 메소드 이름 제거 `->` 표현

    ```java
    (int a, int b)->{
        return a>b?a:b;
    }
    ```

3. 람다식 변환2 : retrun을 Expression으로 대체, ; 제거

    ```java
    (int a, int b)->a>b?a:b
    ```

4. 람다식 변환3 : 매개변수 타입제거

    ```java
    (a,b)->a>b?a:b
    ```

5. 람다식 변환4 :매개변수 하나일때 괄호 생략

    ```java
    a->a*a
    ```

### 함수형 인터페이스

- Java 개념상 람다식도 어딘가 익명 클래스 내의 메소드 이다. => 익명 클래스 = > Functional Interface 로 정의
- 함수형 인터페이스는 하나의 추상메소드만 정의 = >람다식 일치해야하기 때문에
- @FunctionalInterface으로 정의

함수형 인터페이스 타입의 매개변수와 반환타입

```java
@FunctionalInterface
interface MyFuction {
    void myMethod();
}
```

위와 같이 정의 되어있을때

MyFuction f = ()->System.out.println("myMethod") ;
와 같이 추상메소드를 람다식으로 구현한 객체를 가질 수 있음

=> 즉 람다식도 메소드를 통해 이동이 가능!

예제

```java
package xyz.bumbing.lambda;

public class Ex1 {

    public static void main(String[] args) {

        Ex1Interface f1 = () -> System.out.println("1");
        Ex1Interface f2 = new Ex1Interface() {
            @Override
            public void ex1Function() {
            System.out.println("2");
            }
        };
        Ex1Interface f3 = Ex1Class.getFunction();

        Ex1Class.run(f1);
        Ex1Class.run(f2);
        Ex1Class.run(f3);
        Ex1Class.run(() -> System.out.println("4"));
    }
}

@FunctionalInterface
interface Ex1Interface {

    void ex1Function();

}

class Ex1Class {
    public static Ex1Interface getFunction() {
        Ex1Interface f = () -> System.out.println("3");
        return f;
    }

    public static void run(Ex1Interface f) {
        f.ex1Function();
    }
}

```

### 형변환

- 람다식은 익명 객체로 `외부클래스이름$$Lambda$번호`로 이름이 지정됨
- 람다식은 함수형 인터페이스에만 형변환 가능하고, Object로 형변환 가능하지 않음 !  - > Object로 형변환 시킬려면 함수형 인터페이스로 우선 형변환 뒤에 Object가능

- 기본 구조, 생략 가능

```java
MyFuction f = (MyFuction) (()->{});
```

- 안됨

```java
Object f = (Object) (()->{});
```

- 가능

```java
Object f = (Object)(MyFuction) (()->{});
```

### 외부변수 참조 람다식

- 람다식 내에서 사용되는 변수는 상수로 변환

```java
package xyz.bumbing.lambda;

public class Ex2 {

    public void run(int i) { // (final i)

        int j = 100; // final j
        // j = 200; error
        // i = 100; error
        Ex2Interface f1 = () -> {
            System.out.println("i : " + i);
            System.out.println("j : " + j);
        };

        f1.ex2Function();
    }

    public static void main(String[] args) {
        new Ex2().run(10);
        // i : 10
        // j : 100

    }
}

@FunctionalInterface
interface Ex2Interface {

    void ex2Function();

}
```

## java.util.function 라이브러리

- 대부분 메서드 타입이 비슷하기 때문에 자주 사용하는 함수형 인터페이스 라이브러리가 있음

| 함수형 인터페이스  | 메서드                        | 설명                                             |
| ------------------ | ----------------------------- | ------------------------------------------------ |
| java.lang.Runnable | X->void run()->X              | 매개변수 X, 반환 X                               |
| Supplier<T>        | X->T get()->T                 | 매개변수 X, 반환 T                               |
| Consumer<T>        | T->void accept(T t)->X        | 매개변수 T , 반환 X                              |
| Function<T,R>      | T->R apply(T t)->R            | 매개변수 T, 반환 R, 일반적 함수                  |
| Predicate<T>       | T->boolean test(T t)->boolean | 매개변수 T, 반환 boolean, 조건식 표현하는데 사용 |

### Predicate<T> 사용방법

- 수학에서 조건식을 predicate라고 함 
Predicate<String>

```java
package xyz.bumbing.lambda;

import java.util.function.Predicate;

public class Ex3 {

    public static void main(String[] args) {
        Predicate<String> isEmptyStr = s -> s == null || s.length() == 0;

        String S = "";
        String S2 = null;
        String S3 = "a";

        System.out.println(isEmptyStr.test(S));
        System.out.println(isEmptyStr.test(S2));
        System.out.println(isEmptyStr.test(S3));

    }
}

```

### 매개변수가 두개인 함수형 인터페이스

- 접두사 Bi 가 붙음 두개 이상의 함수형 인터페이스가 필요하면 직접 만들어서 사용

| 함수형 인터페이스 | 메서드                              | 설명                                               |
| ----------------- | ----------------------------------- | -------------------------------------------------- |
| BiConsumer<T,U>   | T,U->void accept(T t,U u)->X        | 매개변수 T U , 반환 X                              |
| BiFunction<T,U,R> | T,U->R apply(T t,U u)->R            | 매개변수 T U, 반환 R, 일반적 함수                  |
| BiPredicate<T,U>  | T U->boolean test(T t,U u)->boolean | 매개변수 T U, 반환 boolean, 조건식 표현하는데 사용 |


```java
@FunctionalInterface
interface TriFunction<T,U,V,R>{
    R apply(T t,U u,V v);
}
```

### 매개변수 타입과 반환 타입이 같은 경우

| 함수형 인터페이스 | 메서드                   | 설명                   |
| ----------------- | ------------------------ | ---------------------- |
| UnaryOperator<T>  | T->T apply(T t)->T       | 매개변수 T  , 반환 T   |
| BinaryOperator<T> | T,T->T apply(T t,T t)->T | 매개변수 T,T  , 반환 T |

### 컬렉션 프레임워크와 함수형 인터페이스

- 컬렉션 프레임워크의 인터페이스에 함수형 인터페이스 사용하는 다수의 디폴트 메서드가 추가됨

| 인터페이스 | 메서드                                      | 설명                              |
| ---------- | ------------------------------------------- | --------------------------------- |
| Collection | boolean removeIf(Predicate<E> filter)       | 조건에 맞는 요소를 삭제           |
| List       | void replaceAll(UnaryOperator<E> operator   | 모든 요소를 변환하여 대체         |
| Iterable   | void forEach(Consumer<T> action)            | 모든요소에 작업 action 수행       |
| Map        | V compute(K key,BiFunction<K,V,V> f)        | 지정된 키의 값에 작업 f를 수행    |
| Map        | V computeIfAbsent(K key,Function<K,V> f)    | 키가 없으면, 작업 f 수행 후 추가  |
| Map        | V computePresent(K key,BiFunction<V,V,V> f) | 지정된 키가 있을 때 , 작업 f 수행 |
| Map        | V merge(K key,V value,BiFunction<V,V,V> f)  | 모든 요소에 병합작업 f를 수행     |
| Map        | boolean removeIf(Predicate<E> filter)       | 조건에 맞는 요소를 삭제           |
| Collection | boolean removeIf(Predicate<E> filter)       | 조건에 맞는 요소를 삭제           |