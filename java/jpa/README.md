# JPA : Java Persistence API

## 설명 및 특징

- JPA는 ORM 표준 기술로 Hibernate , OpenJPA, EclipseLink, TopLink Essentials과 같은 구현체가 있고 이에 표준 인터페이스가 바로 JPA이다.
- 영속성

```
영속성(persistence)이란?

데이터를 생성한 프로그램의 실행이 종료되더라도 사라지지 않는 데이터의 특성을 의미한다.

영속성은 파일 시스템, 관계형 테이터베이스 혹은 객체 데이터베이스 등을 활용하여 구현한다.

영속성을 갖지 않는 데이터는 단지 메모리에서만 존재하기 때문에 프로그램을 종료하면 모두 잃어버리게 된다.

출처: https://choong0121.tistory.com/entry/영속성-이란 [내일의 나를 만드는 방법]
```

## Dependency

*  spring-boot-starter-data-jpa가 spring-boot-starter-jdbc를 의존하고 있어서 jpa만 등록하면 jdbc는 자동으로 의존됨

```
<dependency>
    <groupId>org.springframework.boot</groupId>
    <artifactId>spring-boot-starter-data-jpa</artifactId>
</dependency>
```

## JPA 어노테이션 

- Entity : DB의 데이터를 주고 받기 위한 클래스, Domain
- Id : PK 지정
- GeneratedValue : 자동증분 칼럼 ,3가지 전략
  1. Auto
  2. Sequence
  3. 
- Transient : JPA엔진은 값을 무시(저장X)
- Table
  @Entity로 선언된 클래스 이름은 실제 데이터베이스의 테이블명과 일치하는 것을 매핑, 다를경우 @Table(name="")으로 매핑해야함
- Column
  안 넣어줘도 괜찮은데 DB컬럼명과 변수명이 다를 경우 사용, @Column(name="")

## JPA 클래스

- JpaRepository : 기본 CRUD뿐만아니라 커스텀 메서드 구현이 필요한 프록시 클래스를 적용하기 위해 쓰임

- **JpaRepository<Entity, IdType>**

  ```java
  public interface MemberRepository extends JpaRepository<Member, Long> {
  
  }
  ```

- 메소드

  - 기본 메소드

    #### save()

    ```
    레코드 저장(insert,update)
    ```

    #### findOne(IdType var)

    ```
    기본키로 레코드 한건 찾기
    ```

    #### findAll()

    ```
    전체 레코드 불러오기, sort,pageable기능 
    ```

    #### count()

    ```
    레코드 개수
    ```

    #### delete()

    ```
    레코드 삭제
    ```

    #### CrudRepository  

    ```java
    @NoRepositoryBean
    public interface CrudRepository<T, ID extends Serializable> extends Repository<T, ID> {
        <S extends T> S save(S var1);	#단일 저장
        <S extends T> Iterable<S> save(Iterable<S> var1); #여러개 저장
     
        T findOne(ID var1); #하나 찾기
        Iterable<T> findAll(); #전체 찾기
        Iterable<T> findAll(Iterable<ID> var1); #여러개 ID 전체 찾기
     
        boolean exists(ID var1); #ID 존재여부
        long count(); #전체 개수
     
        void delete(ID var1); # 특정 ID 삭제
        void delete(T var1); # 특정 객체 삭제
        void delete(Iterable<? extends T> var1); # 여러개 객체삭제
        void deleteAll(); # 전체 삭제
    }
    ```

    

  - 검색 메소드 규칙

    #### *findByXX* 

    ```
    XX는 엔티티 속성 
    ```

    #### *Like / NotLike* 

    ```
    like 검색 "findByXXLike "는 XX속성을 like검색 한다는 의미
    ```

    #### *StartingWith / EndingWith*

    ```
    지정된 텍스트로 시작하거나 끝나는 것을 검색, findByNameStartingWith("A")이라면, name의 값이
    "A"로 시작하는 항목을 검색한다.
    ```

    #### *IsNull / IsNotNull* 

    ```
    값이 null이 거나, 혹은 null이 아닌 것을 검색한다. 인수는 필요없다. "findByNameIsNull()"이라면, name의
    값이 null의 것만 검색한다.
    ```

    #### *True / False* 

    ```
    부울 값으로 true 인 것, 혹은 false 인 것을 검색한다. 인수는 필요없다. "findByCheckTrue()"이라면, check라는 항목이
    true 인 것만을 검색한다.
    ```

    #### *Before / After*

    ```
    시간 값으로 사용한다. 인수에 지정한 값보다 이전의 것, 혹은 이후 것을 검색한다. "findByCreateBefore(new Date())"라고 
    하면,create라는 항목의 값이 현재보다 이전의 것만을 찾는다 (create가 Date 인 경우).
    ```

    #### *LessThan / GreaterThan*

    ```
    숫자 값으로 사용한다. 그 항목의 값이 인수보다 작거나 큰 것을 검색한다. "findByAgeLessThan(20)"이라면, age의 값이 
    20보다 작은 것을 찾는다.
    ```

    #### *Between*

    ```
    두 값을 인수로 가지고 그 두 값 사이의 것을 검색한다. 예를 들어, "findByAgeBetween(10, 20)"라고 한다면 age 값이 
    10 이상 20 이하 인 것을 검색한다. 수치뿐만 아니라 시간의 항목 등에도 사용할 수 있다.
    ```

     [참고 Link](https://araikuma.tistory.com/329)

## 하이버네이트

- 특징

  - 자바기반  ORM(Object-Relational  Mapping)
  - JPA (Java Persistence API) : Java의 ORM의 표준 스펙 , Hibernate는 구현체

- 자바타입

  ![Alt](/Wiki/images/hibernate1.PNG)
  ![Alt](/Wiki/images/hibernate2.PNG)

- 프로퍼티

  - DB 연결

    ```
    hibernate.dialect = org.hibernate.dialect.PostgreSQLDialect # 
    jdbc.url = jdbc:postgresql://localhost:5432/데이터베이스이름 
    jdbc.username=postgres
    jdbc.password = XXX
    ```

  - Hibernate 설정 (보통 이것만 사용)

    ```
    # sql보여줌
    spring.jpa.properties.hibernate.show_sql=true 
    #한줄이던거 이쁘게보여줌
    spring.jpa.properties.hibernate.format_sql=true 
    #sql 이외 추가정보
  spring.jpa.properties.hibernate.use_sql_comments=true 
    # 바인딩 보여줌
    logging.level.org.hibernate.type.descriptor.sql=trace 
    ```
    
    

  

