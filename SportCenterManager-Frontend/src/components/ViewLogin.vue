<template>
  <div id="login-component">
    <div id="login-body">
      <div id="login-form">
        <div class="form-group">
          <label for="email">Email:</label>
          <input type="email" v-model="email" placeholder="Enter your email">
        </div>
        <div class="form-group">
          <label for="password">Password:</label>
          <input type="password" v-model="password" placeholder="Enter your password">
        </div>
        <div style="margin-bottom: 10px; font-size: 14px;">
          Don't have an account? <a href="#" @click="goToCreateAccount" style="font-weight: bold;">Register</a>
          <div v-if="showCreateAccount">
            <createAccount />
          </div>
        </div>
        <button class="btn btn-login" @click="login">Login</button>
      </div>
    </div>
  </div>
</template>
  
<script>
import axios from 'axios';
import config from '../../config';
import createAccount from '@/components/ViewCreateAccount.vue'

const frontendUrl = 'http://' + config.dev.host + ':' + config.dev.port;
const backendUrl = 'http://' + config.dev.backendHost + ':' + config.dev.backendPort;

const client = axios.create({
  baseURL: backendUrl,
  headers: { 'Access-Control-Allow-Origin': frontendUrl }
});

export default {
  components: {createAccount}, 
  data() {
    return {
      email: '',
      password: '',
      showCreateAccount: false
    };
  },
  methods: {
    async login() {
      const login = {
        email: this.email,
        password: this.password
      };
      try {
        const response = await client.post('/', login);

        const id = response.data.id;
        
        if (login.email === "owner@sportcenter.com") {
          localStorage.setItem('Status', 'Owner');
          localStorage.setItem('Id', id);
        }
        else if (login.email.endsWith("@sportcenter.com")) {
          localStorage.setItem('Status', 'Instructor');
          localStorage.setItem('Id', id);
        }
        else {
          localStorage.setItem('Status', 'Customer');
          localStorage.setItem('Id', id);
        }
 
        this.email = '';
        this.password = '';

        this.$router.push('/home')
      } 
      catch (error) {
        if (error.response && error.response.data) {
          alert(error.response.data.message); 
        } 
        else {
          alert('An error occurred while Login in.'); 
        }
      }
    },
    goToCreateAccount() {
      this.showCreateAccount = !this.showCreateAccount;
    },
  },
};
</script>

<style scoped>
  #login-component {
    display: flex;
    justify-content: center;
    flex-direction: column;
    align-items: center;
    width: 100%;
    height: 100%;
  }
  #login-body {
    display: flex;
    justify-content: center;
    flex-direction: column;
    align-items: center;
    width: 100%;
    height: 100%;
  }
  #login-form {
    width: 400px;
  }
  .form-group {
    margin-bottom: 20px;
    width: 100%;
    height: 70px;
    border-bottom: 2px solid #162938;
    margin: 30px 0;
    text-align: left;

  }
  label {
    display: block;
    margin-bottom: 5px;
    font-size: 16px;
    font-weight: bold;
  }

  input {
    width: 100%;
    top: 8%;
    font-size: 14px;
    background: transparent; 
    border: none;
    outline: none;
    box-sizing: border-box;
    transition: border-color 0.3s ease;
  }

  .btn-login {
    width: 100%;
    height: 40px;
    font-weight: bold;
    font-size: 16px;
    border: none; 
    outline: none; 
    background-color: #162938; 
    transition: background-color 0.3s ease; 
    color: #fff;
    cursor: pointer;

  }

  .btn-login:hover {
    background-color: #e0e0e0;
    color: #162938;
  }
</style>
