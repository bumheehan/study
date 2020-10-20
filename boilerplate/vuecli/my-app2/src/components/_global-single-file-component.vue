<template>
  <div>
    <span>반복문 1</span>
    <ul>
      <li v-for="(item, index) in fruitList" v-bind:key="index">{{ item }}</li>
    </ul>

    <span>반복문 2</span>
    <ul>
      <li v-for="item in fruitList2" v-bind:key="item.id">{{ item.value }}</li>
    </ul>
  </div>
</template>

<script>
export default {
  name: "iterator",
  data: function() {
    return {
      fruitList: ["apple", "banana", "grape"],
      fruitList2: {
        apple: {
          id: 1,
          value: "apple"
        },
        banana: {
          id: 2,
          value: "banana"
        },
        grape: {
          id: 3,
          value: "grape"
        }
      }
    };
  }
};
</script>

<style></style>
