<template>
  <ol>
    <list-component
      v-for="(item, index) in addList"
      v-bind:key="index"
      v-bind:title="item"
      v-bind:component-name="item"
    ></list-component>
  </ol>
</template>

<script>
import ListComponent from "./list-component.vue";

export default {
  components: {
    ListComponent
  },
  name: "index-list",
  props: {
    addList: Array
  }
};
</script>

<style></style>
