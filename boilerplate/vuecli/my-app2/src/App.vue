<template>
  <div id="app">
    <index-list v-bind:add-list="addList"></index-list>
  </div>
</template>

<script>
import IndexList from "./components/index-list.vue";
import { GlobalList } from "./components/global-component-loader";

export default {
  name: "App",
  components: {
    IndexList
  },
  data: function() {
    return {
      addList: GlobalList
    };
  }
};
</script>

<style></style>
