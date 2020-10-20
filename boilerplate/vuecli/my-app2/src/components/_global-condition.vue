<template>
  <div>
    <input type="checkbox" v-model="seen" />
    <input type="checkbox" v-model="seen2" />
    <span>seen : {{ seen }}, seen2 : {{ seen2 }}</span>
    <br />
    <span v-if="seen">IF</span>
    <span v-else-if="seen2">ELSE IF</span>
    <span v-else>ELSE</span>
  </div>
</template>

<script>
export default {
  name: "condition",
  data: function() {
    return {
      seen: true,
      seen2: true
    };
  }
};
</script>

<style></style>
