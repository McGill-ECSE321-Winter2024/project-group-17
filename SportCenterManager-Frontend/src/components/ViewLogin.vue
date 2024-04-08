<template>
  <div id="login-component">
    <div id="login-header">
      <figure class="image">
        <p style="margin-top: 20%; margin-bottom: 20%;">LOGIN</p>
      </figure>
    </div>
    <div id="login-body">
      <div id="login-form">
        <div class="form-group">
          <label for="email">Email:</label>
          <input type="email" id="email" v-model="email" placeholder="Enter your email">
        </div>
        <div class="form-group">
          <label for="password">Password:</label>
          <input type="password" id="password" v-model="password" placeholder="Enter your password">
        </div>
        <div style="margin-bottom: 10px; font-size: 14px;">
          Don't have an account? <a href="#" @click="goToCreateAccount" style="font-weight: bold;">Register</a>
        </div>
        <button class="btn btn-login" @click="login">Login</button>
      </div>
    </div>
  </div>
</template>
  
  <script>
export default {
    data() {
        return {
            email: '',
            password: '',
        };
    },
    methods: {
        async login() {
            try {
                // Implement login logic here (e.g., send login request to server)
                console.log('Login clicked');
            } catch (error) {
                console.error('Failed to login:', error);
                alert('Failed to login. Please try again.');
            }
        },
        goToCreateAccount() {
      this.$router.push('/login/customerAccount');
      },
    },
};
</script>
  
  


  <style>
  #login-component {
    display: flex;
    justify-content: center;
    flex-direction: column;
    align-items: center;
    width: 100%;
    height: 100%;
  }
  #login-header {
    background-color: #000000;
    width: 100%;
    padding: 6%;
    height: 100%;
    display: flex;
    justify-content: center;
    flex-direction: column;
    align-items: center;
    color: white;
    font-weight: bold;
    font-size: 50px;
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
    width: 300px;
  }
  .form-group {
    margin-bottom: 20px;
  }
  label {
    display: block;
    margin-bottom: 5px;
    font-size: 16px;
    font-weight: bold;
  }
  input {
    width: 100%;
    padding: 8px;
    font-size: 14px;
    border-radius: 5px;
    border: 1px solid #ccc;
    box-sizing: border-box;
    transition: border-color 0.3s ease;
  }
  input:focus {
    outline: none;
    border-color: #6a5acd;
  }
  .btn-login {
    width: 100%;
    height: 40px;
    font-weight: bold;
    font-size: 16px;
    border: none; 
    outline: none; 
    background-color: #ffffff; 
    transition: background-color 0.3s ease; 
  }

  .btn-login:hover {
    background-color: #e0e0e0; 
  }
  </style>