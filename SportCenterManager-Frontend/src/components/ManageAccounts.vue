<template>
    <div style ="padding-right: 7%; width: 70%;" id="manage-accounts-main-body">
        <p style = "font-weight: bold; font-size:25px;">MANAGE ACCOUNTS</p>

        <div id = "create">
            <p style = "font-weight:bold;font-size:20px;text-align:left;margin-left:0;padding-left:0;"> Create Instructor Account</p>
            <div class = "field">
                <p>Name: </p><input type="text" v-model="inst_name">
            </div>
            <div class ="field">
                <p>Email: </p><input type ="text" v-model="inst_email">
            </div>
            <div class ="field">
                <p>Password: </p><input type ="password" v-model="inst_password">
            </div>
            <button type ="button" @click="createInstructor()" id="create-btn">Create</button>
        </div>
        <p style = "font-weight:bold;text-align:left; font-size:20px;"> Delete Accounts</p>
        <div style="margin-top:3%;" id="manage-accounts-filter-btns">
            <button class="state-btn" id = "instructors" @click="toggleAcc()" v-bind:disabled="!InstbuttonStateOn" style="margin-right: 2%;" type="button">Instructors</button>
            <button class="state-btn" id = "instructors" @click="toggleAcc()" v-bind:disabled="InstbuttonStateOn" style="margin-right: 2%;" type="button">Customers</button>
        </div>
        <div id = "accounts-list">
            <div class = "acc-entry" v-for="account in accounts">
                <div :class ="'acc-name'">
                    <p style = "font-size:18px; font-weight:bold;">
                        <a style ="color:black;">{{ account.name }}</a>
                    </p>
                    <button type ="button" @click="deleteAccount(account)">DELETE</button>
                </div>
            </div>
        </div>
        
    </div>
</template>

<script>
import axios from "axios";
import config from "../../config";

const frontendUrl = 'http://' + config.dev.host + ':' + config.dev.port
const backendUrl = 'http://' + config.dev.backendHost + ':' + config.dev.backendPort

const AXIOS = axios.create({
  baseURL: backendUrl,
  headers: { 'Access-Control-Allow-Origin': frontendUrl }
})

export default {
    data(){
        return {
            accounts: [],
            InstbuttonStateOn: false,
            inst_name: "",
            inst_email:"",
            inst_password:""
        };
    },
    async created(){
        try{
            await AXIOS.get("/instructorAccounts").then(response=>{
                this.accounts = response.data.accounts
            })
        }
        catch(e){
            console.log(e);
        }
    },
    
    methods: {
        async createInstructor(){
            const newInstructor={
                name: this.inst_name,
                password: this.inst_password,
                email: this.inst_email
            };
            try{
                await AXIOS.post("/instructorAccounts",newInstructor).then(reponse=>{
                    this.accounts.push(response.data);
                    this.clearInputs();
                                    
                })
                
            }catch(e){
                console.log(e);
            }

        },

        async toggleAcc(){
            if (this.InstbuttonStateOn) {
              this.retrieveInstructors().then(this.InstbuttonStateOn=!this.InstbuttonStateOn)  
              
            } else {
                this.retrieveCustomers().then(this.InstbuttonStateOn=!this.InstbuttonStateOn)  
            }
        },
        async retrieveInstructors(){
            await AXIOS.get("/instructorAccounts").then(reponse=>{
                    this.accounts=[];
                    let accounts = response.data;
                    this.accounts.push(accounts);                
                })
        },
        async retrieveCustomers(){
            await AXIOS.get("/customerAccounts").then(response=> {
                    this.accounts=[];
                    let accs = response.data;
                    this.accounts.push(accs.data);                })
        },

        async deleteAccount(account){
            try {
                if(this.InstbuttonStateOn){
                    await AXIOS.delete("/instructorAccounts/" + account.id ).then(response => {
                    this.getAccounts();
                    })
                }
                else{
                    await AXIOS.delete("/customerAccounts/"+account.id).then(response =>{
                        this.getAccounts();
                    })
                }
                
            } catch (e){
                alert("Failed to cancel registration" + e);
            }
        },
        clearInputs(){
            this.inst_email = null;
            this.inst_name = null;
            this.inst_password = null;
        }
    }
}
</script>
<style>
    p{
        margin-top: 5px;
    }
    .field{
        display:flex;
        flex-direction: row;
        justify-content:space-between;
    }
    input{
        height: 2em;
        margin: 5px;
    }
    #create{
        margin-bottom: 5vw;
        display:flex;
        flex-direction: column;
        justify-content:flex-start;
        width:100%;
        max-width:300px;
    }
    #create-btn{
        width:50%;
        background-color: white;
        margin-top: 2vw;
        color:black;
    }
    #create-btn:hover{
        background-color:black;
        color:white;
    }
    
</style>