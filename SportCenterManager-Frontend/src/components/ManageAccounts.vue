<template>
    <div style ="padding-right: 7%; width: 70%;" id="manage-accounts-main-body">
        <p style = "font-weight: bold; font-size:25px;">MANAGE ACCOUNTS</p>
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
            InstbuttonStateOn: false
        };
    },
    
    methods: {

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
                    this.accounts.push(accounts);                })

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
        }

    },
}
</script>