<template>
  <div v-if="showCreateAccount" class="backdrop">
    <div class="signin">
      <span class="close-icon" @click="cancelSignin">&#10006;</span>
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
        <button class="btn btn-sign" @click="createAccount"> Sign In </button>
      </div>
    </div>
  </div>
</template>

<script> 

export default {
  data(){
    return {
      name: '',
      email: '',
      password: '',
      showCreateAccount: true
    };
  },

    methods: {

    cancelSignin() {
      this.showCreateAccount = !this.showCreateAccount;
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

<style scoped>
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
    z-index: 9999;
  }

  .backdrop {
    top: 0px;
    left: 0px;
    position: fixed;
    background: rgba(0,0,0,0.5);
    width: 100%;
    height: 100%;
    z-index: 999;
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
    border: none; 
    outline: none; 
    background-color: #162938; 
    color: #fff;
    cursor: pointer;
  }

  .btn-cancel:hover {
    background-color: #e0e0e0;
    color: #162938;
  }
  .btn-sign:hover {
    background-color: #e0e0e0;
    color: #162938;
  }

  .form-group {
    margin-top: 30px;
    margin-bottom: 15px;
    border-bottom: 1px solid #162938;
    text-align: left;
  }

  label {
    display: block;
    font-size: 16px;
    margin-top: 4%;

  }

  input {
    width: calc(100% - 10px);
    padding: 5px;
    font-size: 14px;
    border-radius: 5px;
    border: none;
    outline: none;
    box-sizing: border-box;
    transition: border-color 0.3s ease;
  }

  input:focus {
    outline: none;
    border-color: #6a5acd;
  }
  
  .close-icon {
    position: absolute;
    top: 10px;
    right: 10px;
    font-size: 20px;
    cursor: pointer;
  }

</style>

