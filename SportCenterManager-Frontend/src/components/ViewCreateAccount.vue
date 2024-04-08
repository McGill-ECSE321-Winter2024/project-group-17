<template>
  <div class="backdrop">
    <div class="signin">
      <h1> SIGN IN </h1>
      <div class="form-group">
        <label for="name">Name:</label>
        <input type="text" id="name" v-model="name" placeholder="Enter your name">
      </div>
      <div class="form-group">
        <label for="email">Email:</label>
        <input type="email" id="email" v-model="email" placeholder="Enter your email">
      </div>
      <div class="form-group">
        <label for="password">Password:</label>
        <input type="password" id="password" v-model="password" placeholder="Enter your password">
      </div>

      <div class="button-group">
        <button class="btn btn-cancel" @click="cancelSignin"> Cancel </button>
        <button class="btn btn-sign" @click="toggleSignin"> Sign In </button>
      </div>
    </div>
  </div>
</template>

<script> 

export default {
  data(){
    return {
      toggleSignin: false,
      name: '',
      email: '',
      password: '',
    };
  },

    methods: {
    toggleSignin() {
      this.toggleSignin == !this.toggleSignin
    },
    cancelSignin() {
      this.toggleSignin = false;
    },

    async createAccount() {
      const customerAccount = {
        name: this.name,
        email: this.email,
        password: this.password,
      };
      try {
        const response = await client.post("/customerAccounts", customerAccount);
        this.$router.push('/customerAccounts')
      }
      catch (e) {
        console.log(e);
      }
    },

  }
}
</script>

<style>
  .signin {
    width: 500px;
    height: 500px;
    padding: 20px;
    margin: 150px auto;
    background: white;
    border-radius: 10px;
    font-weight: bold;
    font-size: 25px;
    position: relative;
  }

  .backdrop {
    top: 0;
    position: fixed;
    background: rgba(0,0,0,0.5);
    width: 100%;
    height: 100%;
  }

  h1 {
    border-bottom: 1px solid #ddd;
    display: inline-block;
    padding-bottom: 10px;
    font-weight: bold;
    font-size: 25px;
    text-align: center;
  }

  .button-group {
    position: absolute;
    bottom: 20px;
    left: 50%;
    transform: translateX(-50%);
    display: flex;
    justify-content: space-around;
    align-items: center;
    }

  .btn-cancel, .btn-sign {
    width: 150px;
    height: 40px;
    font-weight: bold;
    font-size: 16px;
    margin: 0 5px;
  }

  .form-group {
    margin-top: 30px;
    margin-bottom: 15px;
  }

  label {
    display: block;
    margin-bottom: 5px;
    font-size: 16px;
  }

  input {
    width: calc(100% - 24px);
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

</style>
