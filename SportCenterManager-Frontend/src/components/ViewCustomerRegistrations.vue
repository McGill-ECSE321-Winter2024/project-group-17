<template>
    <div style="padding-right: 7%; width: 70%;" id="customer-registrations-main-body">
            <p style="font-weight: bold; font-size: 25px;">REGISTRATIONS</p>
        <div style="margin-top: 3%;" id="customer-registrations-filter-btns">
            <button class="state-btn" id="upcoming-registrations-btn" @click="toggleRegistration()" v-bind:disabled="!buttonState" style="margin-right: 2%;" type="button">Upcoming</button>
            <button class="state-btn" id="past-registrations-btn" @click="toggleRegistration()" v-bind:disabled="buttonState" style="margin-right: 2%;" type="button">Past</button>
        </div>
        <div id="customer-registrations-list">
            <div class="customer-registration-entry" v-for="registration in registrations">
                <div :class="'customer-registration-entry-text'">
                <p style="font-size: 18px; font-weight: bold;">
                    <a style="color: black;">Course: {{ registration.session.course.name}}</a>
                </p>
                <p>Date/Time: {{ registration.session.date }} -- {{ registration.session.startTime }} to {{ registration.session.endTime }}</p>
                <p>Instructor: {{ registration.session.instructor.name }}</p>
                </div>
                <button type="button" @click="cancelRegistration(registration)" v-if="!buttonState">CANCEL</button>
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
            // if buttonState is false, then filtering by past registrations, else filtering upcoming registrations 
            buttonState: false
        };
    },
    
    methods: {

        toggleRegistration(){
            if (this.buttonState) {
                this.filterUpcomingRegistrations().then(this.buttonState = !this.buttonState);
            } else {
                this.filterPastRegistrations().then(this.buttonState = !this.buttonState);
            }
        },

        async filterPastRegistrations(){
            try {
                await AXIOS.get("/customerAccounts/1/registrations").then(response => {
                    this.registrations = [];
                    let regs = response.data.registrations;
                    let currentDate = new Date();
                    for (let i = 0; i < regs.length; i++){
                        if (Date.parse(regs[i].session.date) < currentDate) {
                            this.registrations.push(regs[i]);
                        }

                    }
                });
            } catch (e) {
                alert("Failed to get registrations!" + e);
                return;
            }
        },

        async filterUpcomingRegistrations(){
            try {
                await AXIOS.get("/customerAccounts/1/registrations").then(response => {
                    this.registrations = [];
                    let regs = response.data.registrations;
                    let currentDate = new Date();
                    for (let i = 0; i < regs.length; i++){
                        if (Date.parse(regs[i].session.date) >= currentDate) {
                            this.registrations.push(regs[i]);
                        }

                    }
                });
            } catch (e) {
                alert("Failed to get registrations!" + e);
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
        this.filterUpcomingRegistrations();
    }
}
</script>

<style>

#customer-registrations-main-body {
  display: flex;
  justify-content: space-between;
  flex-direction: row;
  align-items: top center;
  width: 100%;
  height: 100%;
}

#customer-registrations-list {
    margin-top: 2%;
    display: flex;
    justify-content: left baseline;
    align-items: left baseline;
    flex-direction: column;
}

.customer-registration-entry {
    border-radius: 25px;
    border: 2px solid #000000;
    margin-top: 1%;
    padding-top: 1.5%;
    padding-bottom: 1.5%;
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