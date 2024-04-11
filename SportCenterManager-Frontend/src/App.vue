<script>
import NavBar from './components/NavBar.vue';
import Header from './components/Header.vue';
import axios from 'axios';
import config from '../config';

  const frontendUrl = 'http://' + config.dev.host + ':' + config.dev.port;
  const backendUrl = 'http://' + config.dev.backendHost + ':' + config.dev.backendPort;

  const client = axios.create({
  baseURL: backendUrl,
  headers: { 'Access-Control-Allow-Origin': frontendUrl }
});

export default {
  name: 'app',
  components: {
    NavBar,
    Header
  },
  /*
  async created() {
    const owner = {
        name: 'owner', 
        password: 'Password$',
      };
    try {
      await client.post("/ownerAccount", owner);
    }
    catch (error) {
      console.log(error.response.data.message);
    }
  },
  */
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
