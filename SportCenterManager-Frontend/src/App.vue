<template>
  <div id="app">
    <router-view></router-view>
  </div>
</template>

<script>
import Vue from 'vue';
import VueRouter from 'vue-router';
import Login from './components/ViewLogin.vue';

Vue.use(VueRouter);
const routes = [
  { path: '/login', component: Login },
];

const router = new VueRouter({
  routes
});

export default {
  name: 'App',
  router
};

</script>

<style> 
#app {
  font-family: 'Avenir', Helvetica, Arial, sans-serif;
  -webkit-font-smoothing: antialiased;
  -moz-osx-font-smoothing: grayscale;
  text-align: center;
  color: #2c3e50;
}
</style>
