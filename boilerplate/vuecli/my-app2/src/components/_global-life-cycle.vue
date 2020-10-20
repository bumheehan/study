<template>
  <ul>
    <li
      v-for="(item, index) in lifeCycle"
      v-bind:key="index"
      v-bind:class="item.status ? 'on' : 'off'"
    >{{ item.name }}</li>
  </ul>
</template>

<script>
export default {
  name: "lifeCycle",
  data: function() {
    return {
      lifeCycle: {
        beforeCreate: {
          name: "beforeCreate",
          status: false
        },
        created: {
          name: "created",
          status: false
        },
        beforeMount: {
          name: "beforeMount",
          status: false
        },
        mounted: {
          name: "mounted",
          status: false
        },
        beforeUpdate: {
          name: "beforeUpdate",
          status: false
        },
        updated: {
          name: "updated",
          status: false
        },
        beforeDestroy: {
          name: "beforeDestroy",
          status: false
        },
        destroyed: {
          name: "destroyed",
          status: false
        }
      }
    };
  },
  beforeCreate: async function() {
    //method 선언전
    // await this.sleep(1000);
    console.log("beforeCreate");
    console.log("Observe Data");
    console.log("Init Event");
    //data 선언전
    //this.lifeCycle.beforeCreate.status = true;
  },
  created: async function() {
    await this.sleep(1000);
    console.log("created");
    console.log("el,template 옵션 확인");

    this.lifeCycle.created.status = true;
  },
  methods: {
    sleep: function(ms) {
      return new Promise(resolve => {
        setTimeout(resolve, ms);
      });
    }
  }
};
</script>

<style>
.on {
  color: blue;
}
.off {
  color: red;
}
</style>
