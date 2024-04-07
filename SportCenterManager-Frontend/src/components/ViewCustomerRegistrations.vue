<template>
    <div id="customer-registrations-component">
        <div id="customer-registrations-body">
            <div style="padding-right: 8%; width: 70%;" id="customer-registrations-main-body">
                    <p style="font-weight: bold; font-size: 25px;">REGISTRATIONS</p>
                <div style="margin-top: 3%;" id="customer-registrations-filter-btns">
                    <button class="state-btn" id="past-registrations-btn" @click="filterPastRegistrations()" v-bind:disabled="buttonState" style="margin-right: 2%;" type="button">Past</button>
                    <button class="state-btn" id="upcoming-registrations-btn" @click="filterUpcomingRegistrations()" v-bind:disabled="!buttonState" style="margin-right: 2%;" type="button">Upcoming</button>
                </div>
                <div id="customer-registrations-list">
                    <div class="customer-registration-entry" v-for="registration in registrations">
                        <div :class="'customer-registration-entry-text'">
                        <p style="font-size: 18px; font-weight: bold;">
                            <a style="color: black;">Course: {{ registration.session.course.name}}</a>
                        </p>
                        <p>Date/Time: {{ registration.session.date }} -- {{ registration.session.startTime }}</p>
                        <p>Instructor: {{ registration.session.instructor.name }}</p>
                        </div>
                        <button type="button" @click="cancelRegistration(registration)">CANCEL</button>
                    </div>
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
            registrations: [],
            buttonState: true
        };
    },
    
    methods: {

        toggleRegistration(){
            this.buttonState = !this.buttonState;
        },

        async filterPastRegistrations(){
            try {
                await AXIOS.get("/customerAccounts/15752/registrations").then(response => {
                    this.registrations = [];
                    let regs = response.data.registrations;
                    let currentDate = new Date();
                    for (let i = 0; i < regs.length; i++){
                        if (Date.parse(regs[i].session.date) < currentDate) {
                            this.registrations.push(regs[i]);
                        }

                    }
                    this.toggleRegistration();
                });
            } catch (e) {
                alert("Failed to filter" + e);
                return;
            }
        },

        async filterUpcomingRegistrations(){
            try {
                await AXIOS.get("/customerAccounts/15752/registrations").then(response => {
                    this.registrations = [];
                    let regs = response.data.registrations;
                    let currentDate = new Date();
                    for (let i = 0; i < regs.length; i++){
                        if (Date.parse(regs[i].session.date) >= currentDate) {
                            this.registrations.push(regs[i]);
                        }

                    }
                    this.toggleRegistration();
                });
            } catch (e) {
                alert("Failed to filter" + e);
                return;
            }
        },

        async getRegistrations(){
            try {
                await AXIOS.get("/customerAccounts/15752/registrations").then(response => {
                    this.registrations = response.data.registrations;
                });
            } catch (e) {
                alert("Failed to get registrations" + e);
                return;
            }
        },
        async cancelRegistration(registration){
            try {
                await AXIOS.delete("/courses/" + registration.session.course.id + "/sessions/" + registration.session.id + "/registrations/" + registration.customer.id).then(response => {
                    this.getRegistrations();
                })
            } catch (e){
                alert("Failed to cancel registration" + e);
            }
        }

    },

    beforeMount(){
        this.getRegistrations();
    }
}
</script>

<style>

#customer-registrations-component {
  display: flex;
  justify-content: center;
  flex-direction: column;
  align-items: center;
  width: 100%;
  height: 100%;
}

#customer-registrations-body {
  display: flex;
  justify-content: space-between;
  flex-direction: row;
  align-items: top center;
  width: 100%;
}

#customer-registrations-list {
    display: flex;
    justify-content: left baseline;
    align-items: left baseline;
    flex-direction: column;
}

.customer-registration-entry {
    border-radius: 25px;
    border: 2px solid #000000;
    margin-top: 20px;
    padding-top: 2%;
    padding-bottom: 2%;
    padding-left: 5%;
    padding-right: 5%;
    width: 100%;
    height: 30%;
    display: flex;
    flex-direction: row;
    justify-content: space-between;
    align-items: left baseline;
}

.customer-registration-entry-texts {
    display: flex;
    flex-direction: row;
    justify-content: left;
    align-items: left baseline;
    text-align: left;
}

#customer-registrations-filter-btns {
    display: flex;
    justify-content: left baseline;
    flex-direction: row;
    align-items: left baseline;
    width: 70%;
}

#customer-registrations-top-btns {
    display: flex;
    justify-content: left baseline;
    flex-direction: row;
    align-items: left baseline;
}

.state-btn:enabled {
    background-color: white;
    color: #000000;
}

.state-btn:disabled {
    background-color: #000000;
    color: white;
}

table {
  border: 1px solid black;
  align-self: stretch;
}

th,
td {
  border: 1px solid black;
  padding: 5px;
}

p {
    margin: 0;
    text-align: left;
}

a {
    text-align: left;
}

</style>