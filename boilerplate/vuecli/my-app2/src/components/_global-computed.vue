<template>
  <div>
    <span>기본 예제1</span>
    <br />
    <input v-model="message" type="text" />
    <br />
    <span>원본 메세지 : {{ message }}</span>
    <br />
    <span>뒤집힌 메세지 : {{ reversedMessage }}</span>

    <br />
    <br />
    <br />
    <span>
      Computed는 특정 변수의 getter를 참조하기 때문에 어떠한 변수가 변경이 되면
      바로 변경됨(렌더링)- 캐싱기능 , 그러나 Date.now() 같이 아무곳도 의존하지
      않으면 절대로 업데이트 안됨
    </span>
    <br />
    <span>Date.now : {{ now }}</span>
  </div>
</template>

<script>
export default {
  name: "computed",
  data: function() {
    return {
      message: "안녕하세요"
    };
  },
  computed: {
    reversedMessage: function() {
      return this.message
        .split("")
        .reverse()
        .join("");
    },
    now: function() {
      return Date.now();
    }
  }
};
</script>

<style></style>
