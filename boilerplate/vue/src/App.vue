<template>
  <p id="app">
    {{ message }}
  </p>
</template>

<script>
export default {
  data() {
    return {
      message: "Hello World",
    };
  },
};
</script>

<style>
#app {
  color: blue;
}
</style>
