# Jackson 

자주 쓰이는데 가끔 헤맬때가 있어서 정리함

- 어노테이션
- ObjectMapper 옵션

용어

- Serialize : 객체 => Json
- Deserialize : json => 객체

참고

- https://www.baeldung.com/jackson-annotations
- https://www.baeldung.com/jackson-object-mapper-tutorial

## 어노테이션



### Jackson Serialization Annotations

- @**JsonAnyGetter** 

  - 설명 :  Map에 사용하고 MapName : {MapValue}가 아닌 MapValue만 표시

  - 옵션

    - enabled : override 된곳에서 해당 기능 사용안할때 enabled :false 하면됨

  - 위치 : 메소드

  - 예제

      - ```
        결과
        {
        //JsonAnyGetter 없는 Map
        "normalMap":{"normalMapKey":"normalMapValue"},
        //JsonAnyGetter 있는 Map
        "testMapKey":"testMapValue"
        }
        ```

  - 의견 : Map flat하게 표시할때 사용하는 것 같음

- @**JsonGetter** 

  - 설명 : 없을시 일반 getter 사용 , 특정 필드값을  해당 메소드로 가져올때 사용

  - 위치 : 메소드

  - 옵션

    - value : Serialize 할 필드명

  - 예제

      - ```
         @JsonGetter("testField")
         public String getTestField2() {
             testField = "JsonGetter" + testField;
             return testField;
         }
         결과
         {"normalField":"normalField","testField":"JsonGettertestField"}
        ```

- @**JsonPropertyOrder** : 필드 순서 변경, 없을 시 필드 선언 순서 

  - 옵션

    - value String[] : 필드 순서

    - alphabetic : 알파벳 순 정렬

      - `@JsonPropertyOrder(alphabetic=true)`

  - 예제 
    -   ```
    	//결과
        {"testField":"JsonGettertestField","normalField":"normalField"}
        ```

  - 의견 : 결과값에 영향은 없기 때문에 별로 사용안할 것 같음

- @**JsonRawValue**

  - 설명 : 기존 Json은 String format(`\n` , `\"`) 으로 되어있는 값을  그대로 표현할지, 파싱할지에 대한 어노테이션

  - 위치 : 필드

  - 예제

  	- ```
    JsonRawValue
      normalField  : 없는 것
    testField : 있는 것
      
      {"normalField":"{\n  \"attr\":false\n}","testField":{
      "attr":false
      }}
      
      //{를 [로 바꿔서 테스트해봤는데 serialize는 됨, js에서는 에러발생
      {"normalField":"[\n  \"attr\":false\n}","testField":[
      "attr":false
      }}
      ```
    
    

- @**JsonValue**

  - 설명 : 해당 메소드를 통해 직렬화 시킴

  - 위치 : 메소드

  - 예제

    - ```
      @JsonValue
      public String printJson() {
      	return "JsonValue Annotation";
      }
      
      결과 
      "JsonValue Annotation"
      ```

- @**JsonRootName**
  - 설명 : Root Field로 감쌈

  - 위치 : 클래스

  - 조건 : ObjectMapper에서 SerializationFeature.WRAP_ROOT_VALUE 설정 필요

  - 예제

      - ```
        @JsonRootName("root")
        public class JsonRootNameModel {
            private String normalField;
            private String testField;
            ...
        }

        om.enable(SerializationFeature.WRAP_ROOT_VALUE);
        om.writeValueAsString(model);

        결과
        {"root":{"normalField":"normalField","testField":"testField"}}
        ```

    

- @**JsonSerialize**
  - 설명 : Custom Serializer로 사용

  - 위치 : 필드

  - 옵션

    - using : Class<T> 
      - StdSerializer 상속하여 serialize를 구현한 클래스

  - 예제

      - ```java
        public class CustomZondDateTimeSerializer extends StdSerializer<ZonedDateTime> {

            private static DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd-MM-yyyy hh:mm:ss");

            public CustomZondDateTimeSerializer() {
                this(null);
            }
            public CustomZondDateTimeSerializer(Class<ZonedDateTime> t) {
                super(t);
            }
            @Override
            public void serialize(ZonedDateTime value, JsonGenerator gen, SerializerProvider provider) throws IOException {
                String date = value.format(formatter);
                gen.writeString(date);
            }
        }

        @Builder
        @Getter
        public class JsonSerializeModel {

            private ZonedDateTime normalField;

            @JsonSerialize(using = CustomZondDateTimeSerializer.class)
            private ZonedDateTime testField;

            public static JsonSerializeModel getInstance() {

            return builder().normalField(ZonedDateTime.now()).testField(ZonedDateTime.now()).build();
            }

        }

        //결과
        {"normalField":
            {"offset":{"totalSeconds":32400,"id":"+09:00","rules"
            ....
            }},
         "testField":"11-12-2020 07:49:46"}
        ```

  - 의견 : 복잡하고 Dateformat은 @JsonFormat으로 사용하는게 더 편함,  사용안할듯



### Jackson Deserialization Annotations

- @**JsonCreator**
  - 설명 : deserialization 할때 해당 생성자를 통해서 함. 항상 @JsonProperty 사용, 없으면 필드명이 같더라도 에러

  - 위치 : 생성자만 가능 메소드 불가

  - 예제 

    - ```java
      public class JsonCreatorModel {
          private String normalField;
          private String testField;
          private Map<String, String> normalMap;
          private Map<String, String> testMap;
          
          @JsonCreator
          public JsonCreatorModel(
              @JsonProperty("normalField") String nf, 
              @JsonProperty("testField") String tf,
      	    @JsonProperty("normalMap") Map<String, String> nm, 
              @JsonProperty("testMap") Map<String, String> tm) {
              
              tf = "JsonCreator 메소드";
              
              this.normalField = nf;
              this.testField = tf;
              this.normalMap = nm;
              this.testMap = tm;
          }
      }
      
      결과
      JsonCreatorModel(normalField=normalField, testField=JsonCreator 메소드, normalMap={normalMapKey=normalMapValue}, testMap={testMapKey=testMapValue})
      ```

- @**JacksonInject**
  - 설명 : 해당 필드는 Json으로부터 deserialize하는게 아니라 ObjectMapper에 저장된 값들을 Inject 시켜주는 기능

  - 위치 : 필드

  - 옵션

    - values : 저장소에서 불러올 key 값, 동일한 타입 가져오거나, key값으로 가져옴, 동일한 필드명으로는 inject 되지않음

  - 예제 

    - ```
      @ToString
      @Getter
      @Setter
      public class JsonInjectModel {
      
          private String normalField;
      
          @JacksonInject("testField")//testField 명시안해줄 경우 에러
          private String testField;
      
          private Map<String, String> normalMap;
      
          @JacksonInject("testMap")
          private Map<String, String> testMap;
      
      }
      
      public static void main(String[] args) {
          println("JsonInject 예제");
          //
          InjectableValues.Std injectableValues = new InjectableValues.Std();
          injectableValues.addValue("testField", "JsonInject");
          Map<String, String> map = new HashMap<>();
          map.put("testMAP", "JsonInject");
          injectableValues.addValue("testMap", map);
          om.setInjectableValues(injectableValues);
          deserialize(JsonInjectModel.class);
          println("------------------------------------");
      }
      ```

  - 의견 : 많이 사용안할것 같음
  
  

- @**JsonAnySetter**
  - 설명 : 남은 필드를 Map에 담을때 사용

  - 위치 : 메소드

  - 예제

    - ```java
      @ToString
      public class JsonAnySetterModel {
          private String normalField;
          private Map<String, String> testMap = new HashMap<>();
          public String getNormalField() {
      		return normalField;
          }
          public void setNormalField(String normalField) {
      		this.normalField = normalField;
          }
          public Map<String, String> getTestMap() {
      		return testMap;
          }
          @JsonAnySetter
          public void add(String key, String value) {
      		testMap.put(key, value);
          }
      }
      //결과
      In : {"normalField":"normalField","testField":"testField"}
      Out : JsonAnySetterModel(normalField=normalField, testMap={testField=testField})
      ```

  - 의견 : new HashMap<>() 안해줄경우 에러발생, 필수 데이터 받고 옵션을 Map으로 받을때 사용하면 좋을것 같음

- **@JsonSetter**

  - 설명 : 기본 Setter 대신 사용

  - 위치 : 메소드

  -  예제

    - ```java
      public class JsonSetterModel {
          private String normalField;
          private String testField;
          public String getNormalField() {
      		return normalField;
          }
          public void setNormalField(String normalField) {
      		this.normalField = normalField;
          }
          public String getTestField() {
      		return testField;
          }
          public void setTestField(String testField) {
      		this.testField = testField;
          }
          @JsonSetter(value = "testField")
          public void testSetter(String testField) {
      		this.testField = "TestSetter " + testField;
          }
      }
      
      //결과
      In : {"normalField":"normalField","testField":"testField"}
      Out : JsonSetterModel(normalField=normalField, testField=TestSetter testField)
      ```

- **@JsonDeserialize**

  - 설명 : Custom Deserializer 사용

  - 위치 : 필드

  - 예제

    - ```java
      //Deserializer 생성
      public class CustomZondDateTimeDeserializer extends StdDeserializer<ZonedDateTime> {
      
          private static DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd-MM-yyyy HH:mm:ss");
      
          public CustomZondDateTimeDeserializer() {
      		this(null);
          }
      
          public CustomZondDateTimeDeserializer(Class<ZonedDateTime> t) {
      		super(t);
          }
      
          @Override
          public ZonedDateTime deserialize(JsonParser p, DeserializationContext ctxt)
      	    throws IOException, JsonProcessingException {
              LocalDateTime local = LocalDateTime.parse(p.getText(), formatter);
              return local.atZone(ZoneId.of("Asia/Seoul"));
          }
      
      }
      
      //Model
      @ToString
      @Setter
      public class JsonDeserializeModel {
      
          @JsonDeserialize(using = CustomZondDateTimeDeserializer.class)
          private ZonedDateTime testField;
      
      }
      //결과
      In : {"testField":"13-12-2020 06:49:02"}
      Out : JsonDeserializeModel(testField=2020-12-13T06:49:02+09:00[Asia/Seoul])
      ```

  - 의견 : 이것도 잘 사용 안할것 같음



- @**JsonAlias**

  - 설명 : Alias와 같은 이름의 필드를 Deserialize함 , 중복 될 경우 마지막에 해당하는 값을 Deserialize함

  - 위치 : 필드

  - 예제 : 

    - ```
      @ToString
      @Getter
      @Setter
      public class JsonAliasModel {
          private String normalField;
          @JsonAlias(value = { "test_1", "test1" })
          private String testField;
      }
      
      //
      In_1 : {"normalField":"normalField","test1":"testField"}
      In_2 : {"normalField":"normalField","test_1":"testField"}
      In_3 : {"normalField":"normalField","testField":"testField"}
      모두 같은 결과
      Out : JsonAliasModel(normalField=normalField, testField=testField)
      
      중복
      In : {"normalField":"normalField","test1":"11","testField":"22"}
      Out : JsonAliasModel(normalField=normalField, testField=22)
      ```

  - 의견 : 필드명이 여러가지 케이스로 들어올때( randCase, RandCase,Rand_Case 등) 사용할 것 같음

    - 기본적으로 필드명과  동일한 값만 deserialize함, 첫문자 대문자만 달라도 에러발생 -> 이때 Alias사용하면 될듯 어떤건 소문자로주고 어떤건 대문자로줄때



###  **Jackson Property Inclusion Annotations**

- @**JsonIgnoreProperties** ★

  - 설명 : Jackson에서 serialize/deserialize 할때 무시할 필드를 마킹

    - Deserialize : **Json에서**(객체 필드아님) 마킹된 필드를 제외
      - 모델에 A, B 필드가 있고, Json이 A,B,C 필드가 들어온다고 할 때  C필드를 제외 시키고 Deserialize할때 사용. C 필드 ignore 안하고 진행하면 unknown  필드 에러 발생, 즉 특정 deserialize에 필요없는 Json 필드명을 알때 values로 사용하고 모를때 ignoreUnknown =true 사용
    - Serialize : 객체에서 해당 필드를 제외하고 Json 만듦

  - 위치 : 클래스

  - 옵션 : 

    - values(String[]) :  serialize/deserialize 할때 무시할 필드
    - ignoreUnknown : Deserialize시 모르는 필드가 들어올때 제외하고 진행하면 true 옵션 사용

  - 예제 : 

    - ```
      @Getter
      @Setter
      @ToString
      @JsonIgnoreProperties(value = { "testField" })
      public class JsonIgnorePropertiesModel {
      
          private String normalField;
          private String testField;
      
      }
      
      //Deserialize
      In : {"normalField":"normalField","testField","testField"}
      Out : JsonIgnorePropertiesModel(normalField=normalField, testField=null)
      
      In_1 : {"normalField":"normalField","testField1","testField"}
      Out : Unrecognized field "testField1" 
      
      //Serialize
      In : JsonIgnorePropertiesModel(normalField=normalField, testField=testField)
      Out : {"normalField":"normalField"}
      ```

    - ```
      @Getter
      @Setter
      @ToString
      @JsonIgnoreProperties(ignoreUnknown = true)
      public class JsonIgnorePropertiesModel {
          private String normalField;
          private String testField;
      }
      //Deserialize
      In : {"normalField":"normalField", "testField","testField", "testField1","testField1"}
      Out : JsonIgnorePropertiesModel(normalField=normalField, testField=testField)
      //Serialize
      In : JsonIgnorePropertiesModel(normalField=normalField, testField=testField)
      Out : {"normalField":"normalField","testField","testField"}
      ```

  - 의견 : ignoreUnknown 때문에 많이 사용, ignore할때는 필드 어노테이션이 명확해보임

- @**JsonIgnore** ★

  - 설명 : Deserialize/Serialize 시 무시할 필드 설정

  - 위치 : 필드

  - 예제

    - ```
      @Getter
      @Setter
      @ToString
      public class JsonIgnoreModel {
      
          private String normalField;
      
          @JsonIgnore
          private String testField;
      
      }
      //Deserialize
      In : {"normalField":"normalField", "testField","testField"}
      Out : JsonIgnoreModel(normalField=normalField, testField=null)
      //Serialize
      In : JsonIgnoreModel(normalField=normalField, testField=testField)
      Out : {"normalField":"normalField"}
      ```

  - 의견 : 많이 사용

- @**JsonIgnoreType** 

  - 설명 : 해당 타입이 사용되는 모든곳에서 무시

  - 위치 : 클래스?

  - 예제

    - ```
      @Getter
      @Setter
      @ToString
      public class JsonIgnoreTypeModel {
          private String normalField;
          private TestType testType;
      }
      
      @Getter
      @Setter
      @ToString
      @JsonIgnoreType
      class TestType {
          private String test1;
          private String test2;
      }
      
      In :{"normalField":"normalField","testType":{"test1":"test1","test2":"test2"}} 
      Out : JsonIgnoreTypeModel(normalField=normalField)
      
      없을시
      In :{"normalField":"normalField","testType":{"test1":"test1","test2":"test2"}} 
      Out : JsonIgnoreTypeModel(normalField=normalField, testType=TestType(test1=test1, test2=test2))
      ```

  - 의견 : Json 용이 아닌 타입에 사용

- @**JsonInclude**

  - 설명 : serialize 할때 empty/null/default등 `특정 데이터` 제외 시키기

  - 위치 : 클래스

  - 옵션

    - value

      - Include.NON_NULL : null 제외
      - Include.NON_EMPTY : NON_NULL+문자열 0, 컬렉션 0, 배열 0 

      - Include.NON_DEFAULT : empty는 제외된다. ( 4. NON_EMPTY 참고 ) primitive 타입이 디폴트 값이면 제외한다. (int / Integer : 0 , boolean / Boolean : false 등) Date의 timestamp가 0L이면 제외한다.

  - 예제

    - ```
      @Getter
      @Setter
      @ToString
      @JsonInclude(value = Include.NON_EMPTY)
      public class JsonIncludeModel {
      
          private String normalField;
          private String testField;
      
      }
      
      //결과
      In : JsonIncludeModel(normalField=normalField, testField=)
      Out : {"normalField":"normalField"}
      ```

- @**JsonAutoDetect**

  - 설명 : Jackson은 기본적 `public field` 만 Serialization/Deserialization 가능, field가 public이 아니면 `public getter/setter`  를 사용한다. 이런 기본설정을  `@JsonAutoDetect` 를 통해 변경

  - 위치 : 클래스

  - 옵션 : 

    - fieldVisibility : Visibility에 따른 접근제어자만 사용가능 (Field)

    - getterVisibility : Visibility에 따른 접근제어자만 사용가능(Getter)

    - setterVisibility : Visibility에 따른 접근제어자만 사용가능(Setter)

    - Visibility

      - ```
         ANY, //all kinds of access modifiers 
         NON_PRIVATE, //other than 'private' 
         PROTECTED_AND_PUBLIC, //only 'protected' and 'public'
         PUBLIC_ONLY,//public only
         NONE, //no access modifiers;  to explicitly disable auto-detection for fields or methods
         DEFAULT;//default rules
        ```

  - 예제 :

    - ```
      @ToString
      @JsonAutoDetect(fieldVisibility = Visibility.PUBLIC_ONLY, setterVisibility = Visibility.PUBLIC_ONLY)
      public class JsonAutoDetectModel {
      
          public String normalField;
          private String testField;
      
          public void setTestField(String testField) {
      	this.testField = testField;
          }
      }
      
      //결과
      fieldVisibility = Visibility.PUBLIC_ONLY만 할 경우 
      testField는 Jackson에서 제외되므로 testField Unrecognized Error 발생
      
      In : {"normalField":"normalField","testField":"testField"}
      Out : JsonAutoDetectModel(normalField=normalField, testField=testField)
      ```

  - 의견 : 기본 룰 사용만해도 충분하다고 생각함

  - 참고 : https://www.logicbig.com/tutorials/misc/jackson/json-auto-detect.html



### Jackson Polymorphic Type Handling Annotations

Input 값에 따라서 다른 서브 클래스로 Deserialize 해야하는 경우 사용

3가지 사용 클래스가 있음

- *@JsonTypeInfo* – indicates details of what type information to include in serialization
- *@JsonSubTypes* – indicates sub-types of the annotated type
- *@JsonTypeName* – defines a logical type name to use for annotated class

사용 안할 것 같아서 정리안함



### Jackson General Annotations



- @JsonProperty

  - 설명 : 해당 어노테이션을 사용하여 non-standard한  getter/setter를 다룰 수 있다.

  - 위치 : 생성자 파라메터/ 메소드

  - 예제

    - ```
      @ToString
      @NoArgsConstructor
      @AllArgsConstructor
      @Builder
      public class JsonPropertyModel {
      
          private String normalField;
      
          private String testField;
      
          public String getNormalField() {
      		return normalField;
          }
          public void setNormalField(String normalField) {
      		this.normalField = normalField;
          }
          @JsonProperty("testField")
          public String customGetterTestField() {
              System.out.println("customGetter");
              return testField;
          }
          @JsonProperty("testField")
          public void customSetterTestField(String testField) {
              System.out.println("customSetter");
              this.testField = testField;
          }
      }
      
      //결과
      //deserialize
      In : {"normalField":"normalField","testField":"testField"}
      Out : 
      customSetter
      JsonPropertyModel(normalField=normalField, testField=testField)
      
      //serialize
      In : JsonPropertyModel(normalField=normalField, testField=testField)
      Out : 
      customGetter
      {"normalField":"normalField","testField":"testField"}
      ```

  - 의견 : 매우중요

- @**JsonFormat** , Spring에서 지원하는 @**DateTimeFormat** 

  - 설명 : Date 포맷 처리하여 serialize/deserialize , JsonFormat은 Date 타입만 가능하고 LocalDateTime 등은 처리가 안된다. DateTimeFormat은 LocalDateTime 등 사용 가능하다.

  - 위치 : 필드

  - 예제

    - ```
      public class JsonFormatModel {
      
          @JsonFormat(shape = Shape.STRING, pattern = "dd-MM-yyyy HH:mm:ss")
          public Date testField;
      
      }
      //deserialize
      In : {"testField":"14-12-2020 12:13:14"}
      Out : JsonFormatModel(testField=Mon Dec 14 21:13:14 KST 2020)
      //serialize
      In : JsonFormatModel(testField=Mon Dec 14 02:17:03 KST 2020)
      Out : {"testField":"13-12-2020 17:17:03"}
      ```

  - 두 어노테이션 모두 많이 사용할 것 같음

- @**JsonUnwrapped**

  - 설명 : Json Object구조를 Flat하게 표현

  - 위치 : 필드

  - 예제

    - ```
      //deserialize
      //정상
      In : {"normalField":"normalField", "t1":"t1","t2":"t2"}
      Out : JsonUnwrappedModel(normalField=normalField, testVV=JsonUnwrappedModel.TestVV(t1=t1, t2=t2))
      
      //비정상 Unwrapped 사용하여 flat하게 줘야함
      In : {"normalField":"normalField", "testVV" : {"t1":"t1","t2":"t2"} }
      Out : JsonUnwrappedModel(normalField=normalField, testVV=JsonUnwrappedModel.TestVV(t1=null, t2=null))
      
      //serialize
      In : JsonUnwrappedModel(normalField=normalField, testVV=JsonUnwrappedModel.TestVV(t1=t1, t2=t2))
      Out : {"normalField":"normalField","t1":"t1","t2":"t2"}
      ```

  - 의견 : 알아둬야할 것 같음

- @**JsonView**

  - 설명 : 특정 상태마다 Json 보여주는 필드가 다를때 사용

  - 위치 : 필드

  - 예제

    - ```
      public class Views {
          public static class Public {  }
          public static class Internal extends Public {  }
      }
      
      public class JsonViewModel {
          @JsonView(Views.Public.class)
          public int id;
          @JsonView(Views.Public.class)
          public String itemName;
          @JsonView(Views.Internal.class)
          public String ownerName;
      }
      
      //사용법 readerWithView/writerWithView한 다음 read/write 해야 적용, 계속 적용되지 않고 일시적 적용
      //deserialize
      JsonViewModel read = om.readerWithView(Views.Public.class).readValue(json, JsonViewModel.class);
      println(read);
      //In : "{"id":1,"itemName":"item","ownerName":"owner"}"
      //Out : JsonViewModel(id=1, itemName=item, ownerName=null)
      
      //serialize
      String write = om.writerWithView(Views.Public.class)
      .writeValueAsString(new JsonViewModel(1, "item", "owner"));
      println(write);
      //In : JsonViewModel(id=1, itemName=item, ownerName=owner)
      //Out : {"id":1,"itemName":"item"}
      
      ```

  - 의견 : 권한마다 다르게 주는 Json일때 무조건 사용할 듯함 . writeView 만 있으면 될듯함

- **@JsonManagedReference, @JsonBackReference**

- **@JsonIdentityInfo**

- **@JsonFilter**



### **Custom Jackson Annotation**

### **Jackson MixIn Annotations**

### **Disable Jackson Annotation**

