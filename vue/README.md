# Vue

https://github.com/vuejs/vue-devtools

vue route 설치

## 디렉티브

- v-once : 일회성
- v-html : html 출력, XSS 취약점 문제로 자주사용X
- v-bind:[attributeName] : {{}}는 태그 속성으로 사용 불가능, 그때 v-bind 사용, 변수 속성에 넣기
  - null, undefinced, false 일 경우 속성은 포함 안됨
  - v-bind:href="variable"
  - 약어 :[attributeName]
- {{}} : 삼항연산자, 간단한 계산 자바스크립트 메소드 가능 표현식,if X

- v-if : if문

  - v-else : else
  - v-else-if
  - v-if="boolean" 태그 보임 및 안보임 - if, else 태그 재사용할때 있음(빠름) -> 즉 같은 input 사용할수도있음 -> key 입력

    ```html
    <template v-if="loginType === 'username'">
      <label>사용자 이름</label>
      <input placeholder="사용자 이름을 입력하세요" />
    </template>
    <template v-else>
      <label>이메일</label>
      <input placeholder="이메일 주소를 입력하세요" />
    </template>

    --- 다음과 같이 사용

    <template v-if="loginType === 'username'">
      <label>사용자 이름</label>
      <input placeholder="사용자 이름을 입력하세요" key="username-input" />
    </template>
    <template v-else>
      <label>이메일</label>
      <input placeholder="이메일 주소를 입력하세요" key="email-input" />
    </template>
    ```

- v-show : if랑 기능은 비슷한데 CSS display =none으로 안보이게함

- v-on:[eventName] : 이벤트 처리

  - v-on:click="method"
  - v-on:submit.prevent="onSubmit" => event.preventDefault()
  - 약어 @[eventName]

- v-for :조건문
  - v-for="item in items" 로사용
  - "(value,name,index) in object" , "value in object" 로도 사용가능
  - "n in 10" 도 가능
  - key속성 필수 , 문자열이나 숫자
  ```html
  <div v-for="item in items" v-bind:key="item.id">
    <!-- content -->
  </div>
  ```

## 문법

- computed : 계산 메소드 정의 , 캐싱되어 관련된 변수가 변경됐을시 렌더링
  - get /set 사용
  - 종속성을 정의할때 사용
- method : 렌더링시 항상 함수 실행.
- watch : 변경됨을 감지했을때 실행 - callback
  - 첫번째인자 new data, 두번째 인자 old data

### 배열 렌더링 트리거

- 트리거 메소드
  - push()
  - pop()
  - shift()
  - unshift()
  - splice()
  - sort()
  - reverse()
- 하면 안됨 list[1]=x 이렇게 바꾸면 안됨
  - Vue.set(vm.items, indexOfItem, newValue) 다음과 같이 사용

### 속성 감지

- 기본적으로 속성 추가 및 삭제는 자바스크립트한계로 감지 못함

```javascript
var vm = new Vue({
  data: {
    a: 1,
  },
});
// `vm.a` 는 반응형입니다.

vm.b = 2;
// `vm.b` 는 반응형이 아닙니다.
```

- Vue.set(vm.userProfile, 'age', 27) 로 추가

### 이벤트

```javascript
<div id="example-3">
  <button v-on:click="say">say</button>
  <button v-on:click="say('what')">say2</button>
  <button v-on:click="say('what',$event)">say3</button>
</div>;

var vm = new Vue({
  el: "#example",
  method: {
    say: function () {},
    say2: function (msg) {},
    say3: function (msg, event) {}, // 원본 event 받기
  },
});
```

#### 이벤트 수식어

.stop
.prevent
.capture
.self
.once
.passive

```html
<!-- 클릭 이벤트 전파가 중단됩니다 -->
<a v-on:click.stop="doThis"></a>

<!-- 제출 이벤트가 페이지를 다시 로드 하지 않습니다 -->
<form v-on:submit.prevent="onSubmit"></form>

<!-- 수식어는 체이닝 가능합니다 -->
<a v-on:click.stop.prevent="doThat"></a>

<!-- 단순히 수식어만 사용할 수 있습니다 -->
<form v-on:submit.prevent></form>

<!-- 이벤트 리스너를 추가할 때 캡처모드를 사용합니다 -->
<!-- 즉, 내부 엘리먼트를 대상으로 하는 이벤트가 해당 엘리먼트에서 처리되기 전에 여기서 처리합니다. -->
<div v-on:click.capture="doThis">...</div>

<!-- event.target이 엘리먼트 자체인 경우에만 트리거를 처리합니다 -->
<!-- 자식 엘리먼트에서는 안됩니다 -->
<div v-on:click.self="doThat">...</div>
```

#### 키 코드

```html
<input v-on:keyup.13="submit" />
<!--
.enter
.tab
.delete (“Delete” 와 “Backspace” 키 모두를 캡처합니다)
.esc
.space
.up
.down
.left
.right
등 있음
  -->

<input @:keyup.13="submit" />

<!-- Alt 또는 Shift와 함께 눌린 경우에도 실행됩니다. -->
<button @click.ctrl="onClick">A</button>

<!-- Ctrl 키만 눌려있을 때만 실행됩니다. -->
<button @click.ctrl.exact="onCtrlClick">A</button>

<!-- 아래 코드는 시스템 키가 눌리지 않은 상태인 경우에만 작동합니다. -->
<button @click.exact="onClick">A</button>
```

### Model

v-model 로 input, textarea 양방향 데이터 바인딩

```html
<input v-model="message" placeholder="여기를 수정해보세요" />
<p>메시지: {{ message }}</p>

<span>여러 줄을 가지는 메시지:</span>
<p style="white-space: pre-line">{{ message }}</p>
<br />
<textarea v-model="message" placeholder="여러줄을 입력해보세요"></textarea>
<!--
사용금지
<textarea>{{ text }}</textarea>
-->

<!--체크박스 여러 배열도 가능-->
<div id="example-3">
  <input type="checkbox" id="jack" value="Jack" v-model="checkedNames" />
  <label for="jack">Jack</label>
  <input type="checkbox" id="john" value="John" v-model="checkedNames" />
  <label for="john">John</label>
  <input type="checkbox" id="mike" value="Mike" v-model="checkedNames" />
  <label for="mike">Mike</label>
  <br />
  <span>체크한 이름: {{ checkedNames }}</span>
</div>

<!--Radio 배열 X -->
<input type="radio" id="one" value="One" v-model="picked" />
<label for="one">One</label>
<br />
<input type="radio" id="two" value="Two" v-model="picked" />
<label for="two">Two</label>
<br />
<span>선택: {{ picked }}</span>

<!--Select-->
<select v-model="selected">
  <option disabled value="">Please select one</option>
  <option>A</option>
  <option>B</option>
  <option>C</option>
</select>
<span>선택함: {{ selected }}</span>

<!--다중 Select-->
<select v-model="selected" multiple>
  <option>A</option>
  <option>B</option>
  <option>C</option>
</select>
<br />
<span>Selected: {{ selected }}</span>
```

#### model 수식어

- .lazy : 이벤트 이후 동기화
- .number : 타입이 number(자동 형변환)
- .trim

## Component

Vue.component(이름,)

- 이름 표기법

  - DOM에 바로 쓸 때는 케밥 표기법만 가능, 그냥 케밥표기법 기준
  - 케밥 표기법 <my-component-name>
  - 파스칼 표기법 <MyComponentName>

- 전역 등록 사용법 두가지

```javascript
Vue.component("component-a", {
  /* ... */
});
Vue.component("component-b", {
  /* ... */
});
Vue.component("component-c", {
  /* ... */
});

new Vue({ el: "#app" });

---(
  <div id="app">
    <component-a></component-a>
    <component-b></component-b>
    <component-c></component-c>
  </div>
);
```

- require.context 써서 자주쓰는 컴포넌트 전역으로 등록

```javascript
import Vue from "vue";
import upperFirst from "lodash/upperFirst";
import camelCase from "lodash/camelCase";

const requireComponent = require.context(
  // 컴포넌트들이 있는 폴더
  "./components",
  // 하위 폴더까지 포함할 지 여부
  false,
  // 기본 컴포넌트를 찾는데 사용할 정규표현식
  /Base[A-Z]\w+\.(vue|js)$/
);

requireComponent.keys().forEach((fileName) => {
  // 컴포넌트 설정 가져오기
  const componentConfig = requireComponent(fileName);

  // 컴포넌트의 파스칼표기법 이름 가져오기
  const componentName = upperFirst(
    camelCase(
      // 폴더 위치와 무관하게 파일이름 추출
      fileName
        // Remove the "./_" from the beginning
        .replace(/^\.\/_/, "")
        // Remove the file extension from the end
        .replace(/\.\w+$/, "")
        // Split up kebabs
        .split("-")
        // Upper case
        .map((kebab) => kebab.charAt(0).toUpperCase() + kebab.slice(1))
        // Concatenated
        .join("")
    )
  );

  // 컴포넌트를 전역적으로 등록
  Vue.component(
    componentName,
    // `export default`를 이용한 컴포넌트는 `.default`로 컴포넌트
    // 옵션을 추출하고 그렇지 않은 컴포넌트는 모듈의 루트를 호출
    componentConfig.default || componentConfig
  );
});
```

- 지역 등록 사용법

```javascript
var ComponentA = {
  /* ... */
};
var ComponentB = {
  /* ... */
};
var ComponentC = {
  /* ... */
};

new Vue({
  el: "#app",
  components: {
    "component-a": ComponentA,
    "component-b": ComponentB,
  },
});
```

- 모듈 시스템

```javascript
import ComponentA from "./ComponentA";
import ComponentC from "./ComponentC";

export default {
  components: {
    ComponentA,
    ComponentC,
  },
  // ...
};
```

### Props

```javascript
Vue.component("my-component", {
  props: {
    // 기본 타입 체크 (`Null`이나 `undefinded`는 모든 타입을 허용합니다.)
    propA: Number,
    // 여러 타입 허용
    propB: [String, Number],
    // 필수 문자열
    propC: {
      type: String,
      required: true,
    },
    // 기본값이 있는 숫자
    propD: {
      type: Number,
      default: 100,
    },
    // 기본값이 있는 오브젝트
    propE: {
      type: Object,
      // 오브젝트나 배열은 꼭 기본값을 반환하는
      // 팩토리 함수의 형태로 사용되어야 합니다.
      default: function () {
        return { message: "hello" };
      },
    },
    // 커스텀 유효성 검사 함수
    propF: {
      validator: function (value) {
        // 값이 항상 아래 세 개의 문자열 중 하나여야 합니다.
        return ["success", "warning", "danger"].indexOf(value) !== -1;
      },
    },
  },
});
```
