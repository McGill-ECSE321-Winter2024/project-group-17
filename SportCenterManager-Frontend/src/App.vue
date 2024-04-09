<script>
import NavBar from './components/NavBar.vue';
import Header from './components/Header.vue';

export default {
  name: 'app',
  components: {
    NavBar,
    Header
  },
  computed: {
    isAccountView() {
      return this.$route.path.includes('/customerAccount');
    }
  }
}

</script>

<template>
  <div id="app">
    <NavBar />
    <Header />

    <router-view></router-view>

  </div>

</template>


<style>
#app {
  font-family: 'Avenir', Helvetica, Arial, sans-serif;
  -webkit-font-smoothing: antialiased;
  -moz-osx-font-smoothing: grayscale;
  text-align: center;
  color: #2c3e50;
}
</style>
