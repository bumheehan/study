<template>
  <li>
    <button type="button" v-on:click="click">{{ title }}</button>
    <keep-alive>
      <component v-bind:is="componentName" v-if="seen">
        <p>Default content</p>
      </component>
    </keep-alive>
  </li>
</template>

<script>
export default {
  name: "list-component",
  props: {
    title: String,
    componentName: String
  },
  data: function() {
    return {
      seen: false
    };
  },
  methods: {
    click: function() {
      this.seen = !this.seen;
    }
  }
};
</script>

<style></style>
