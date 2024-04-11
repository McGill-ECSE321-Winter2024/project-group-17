<template>
    <div style="padding-right: 7%; width: 70%;" id="customer-registrations-main-body">
        <p style="font-weight: bold; font-size: 25px;" v-if="isCustomer()">REGISTRATIONS</p>
        <p style="font-weight: bold; font-size: 25px;" v-if="isInstructor()">SUPERVISED SESSIONS</p>
        <p style="font-weight: bold; font-size: 25px;" v-if="isOwner()">MANAGE REGISTRATIONS</p>
        <div style="display: flex; justify-content: space-between; flex-direction: row;">
            <div style="margin-top: 3%;" id="customer-registrations-filter-btns">
                <button class="state-btn" id="upcoming-registrations-btn" @click="toggleRegistration()" v-bind:disabled="!buttonState" style="margin-right: 2%;" type="button">Upcoming</button>
                <button class="state-btn" id="past-registrations-btn" @click="toggleRegistration()" v-bind:disabled="buttonState" style="margin-right: 2%;" type="button">Past</button>
            </div>
            <div style="margin-top: 3%; display:inline-block; width: 55%">
                <span style="white-space: nowrap;"> Sort By: </span>
                <select @change="sortRegistrations()" v-model="sortState" style="border-top: none; border-left: none; border-right: none; border-color: black;">
                    <option value="1" sortState >Ascending Date</option>
                    <option value="2">Descending Date</option>
                    <option value="3">Ascending Course Name</option>
                    <option value="4">Descending Course Name</option>
                </select>
            </div>
        </div>
        <div id="customer-registrations-list">
            <div class="customer-registration-entry" v-for="registration in registrations">
                <SessionRegistrantsModal v-if="openModal === registration.session.id" :courseId="registration.session.course.id" :sessionId="registration.session.id" :isOpen="openModal === registration.session.course.id" @close="closeViewRegistrantsModal()"/>
                <div :class="'customer-registration-entry-text'">
                    <p style="font-size: 18px; font-weight: bold;">
                        <a style="color: black;">Course: {{ registration.session.course.name}}</a>
                    </p>
                    <p>Date/Time: {{ registration.session.date }} -- {{ registration.session.startTime }} to {{ registration.session.endTime }}</p>
                    <p v-if="isInstructor() || isOwner()" @click="viewRegistrants(registration.session.id)" style="cursor: pointer; width: 38%"><u>View Registrants</u></p>
                    <p v-if="isCustomer()">Instructor: {{ registration.session.instructor.name }}</p>
                </div>
                <button type="button" @click="cancelRegistration(registration)" v-if="!buttonState && isCustomer()">CANCEL</button>
            </div>
        </div>
    </div>
</template>

<script>
import axios, { Axios } from "axios";
import config from "../../config";
import SessionRegistrantsModal from "./SessionRegistrantsModal.vue";

const frontendUrl = 'http://' + config.dev.host + ':' + config.dev.port
const backendUrl = 'http://' + config.dev.backendHost + ':' + config.dev.backendPort

const AXIOS = axios.create({
  baseURL: backendUrl,
  headers: { 'Access-Control-Allow-Origin': frontendUrl }
})

export default {
    components:{
        SessionRegistrantsModal
    },

    data(){
        return {
            registrations: [],
            // if buttonState is false, then filtering by past registrations, else filtering upcoming registrations 
            buttonState: false,
            // state 1 = ascending date, 2 = descending date, 3 = ascending name, 4 = descending name
            sortState: "1",
            openModal : 0
        };
    },

    methods: {
        isCustomer(){
            return localStorage.getItem("permission") === "customer";
        },

        isInstructor(){
            return localStorage.getItem("permission") === "instructor";
        },

        isOwner(){
            return localStorage.getItem("permission") === "owner";
        },

        toggleRegistration(){
            this.filterRegistrations().then(this.buttonState = !this.buttonState);
        },

        async filterRegistrations(){
            // Sets registrations to contain only past registrations
            this.registrations = [];
            if (localStorage.getItem("permission") === "customer") {
                try {
                    await AXIOS.get("/customerAccounts/" + localStorage.getItem("userId") + "/registrations").then(response => {
                        this.registrations = [];
                        if (this.buttonState) {
                            this.filterPastRegistrations(response.data.registrations);
                        } else {
                            this.filterUpcomingRegistrations(response.data.registrations)
                        }
                        this.sortRegistrations();
                    });
                } catch (e) {
                    alert("Failed to get registrations!" + e);
                    return;
                }
            } else if (localStorage.getItem("permission") === "instructor") {
                try {
                    await AXIOS.get("/instructor/" + localStorage.getItem("userId") + "/sessions").then(response => {
                        let sessions = [];
                        for (let i = 0; i < response.data.sessions.length; i++){
                            sessions.push({
                                session: response.data.sessions[i],
                            });
                        }
                        if (this.buttonState){
                            this.filterPastRegistrations(sessions);
                        } else {
                            this.filterUpcomingRegistrations(sessions);
                        }
                        this.sortRegistrations();
                    });
                } catch (e) {
                    alert(e.response.data.message);
                }
            } else if (localStorage.getItem("permission") === "owner") {
                try {
                    await AXIOS.get("/courses").then(async response => {
                        for (let i = 0; i < response.data.courses.length; i++){
                            await AXIOS.get("/courses/" + response.data.courses[i].id + "/sessions").then(innerResponse => {
                                let sessions = [];
                                for (let i = 0; i < innerResponse.data.sessions.length; i++){
                                    sessions.push({
                                        session: innerResponse.data.sessions[i],
                                    });
                                }
                                if (this.buttonState){
                                    this.filterPastRegistrations(sessions);
                                } else {
                                    this.filterUpcomingRegistrations(sessions);
                                }
                            })
                        }
                        this.sortRegistrations();
                    });
                } catch (e) {
                    alert(e.response.data.message);
                }
            }
        },

        filterPastRegistrations(regs) {
            let currentDate = new Date();
            for (let i = 0; i < regs.length; i++){
                if (Date.parse(regs[i].session.date) < currentDate) {
                    this.registrations.push(regs[i]);
                } else if (Date.parse(regs[i].session.date) == currentDate) {
                    if (Date.parse(regs[i].session.endTime) < currentDate) {
                        this.registrations.push(regs[i]);
                    }
                }
            }
        },

        filterUpcomingRegistrations(regs){
            let currentDate = new Date();
            for (let i = 0; i < regs.length; i++){
                if (Date.parse(regs[i].session.date) > currentDate) {
                    this.registrations.push(regs[i]);
                } else if (Date.parse(regs[i].session.date) == currentDate.getDate()) {
                    if (Date.parse(regs[i].session.endTime) >= currentDate) {
                        this.registrations.push(regs[i]);
                    }
                }

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
        },

        viewRegistrants(num){
            this.openModal = parseInt(num);
        },

        closeViewRegistrantsModal(){
            this.openModal = 0;
        },

        sortRegistrations(){
            this.registrations = this.sortRegistrationsHelper(this.registrations);
        },

        sortRegistrationsHelper(arr){
    
            if (arr.length <= 1) {
                return arr;
            }

            let mid = Math.floor(arr.length / 2);

            let left = this.sortRegistrationsHelper(arr.slice(0, mid));
            let right = this.sortRegistrationsHelper(arr.slice(mid));

            return this.merge(left, right);
        },

        merge(left, right){
            // Merges elmts in left and right depending on current sort state
            let sortedArr = [];
            while (left.length && right.length){
                if (this.sortState === "1") {
                    if (Date.parse(left[0].session.date) < Date.parse(right[0].session.date)){
                        sortedArr.push(left.shift());
                    } else if (Date.parse(left[0].session.date) == Date.parse(right[0].session.date)) {
                        if (Date.parse(left[0].session.startTime) < Date.parse(left[0].startTime)){
                            sortedArr.push(left.shift());
                        }
                    } else {
                        sortedArr.push(right.shift());
                    }
                } else if (this.sortState === "2") {
                    if (Date.parse(left[0].session.date) > Date.parse(right[0].session.date)){
                        sortedArr.push(left.shift());
                    } else if (Date.parse(left[0].session.date) == Date.parse(right[0].session.date)) {
                        if (Date.parse(left[0].session.startTime) > Date.parse(left[0].startTime)){
                            sortedArr.push(left.shift());
                        }
                    } else {
                        sortedArr.push(right.shift());
                    }
                } else if (this.sortState === "3") {
                    if (left[0].session.course.name.localeCompare(right[0].session.course.name) <= 0){
                        sortedArr.push(left.shift());
                    } else {
                        sortedArr.push(right.shift());
                    }
                } else {
                    if (left[0].session.course.name.localeCompare(right[0].session.course.name) >= 0){
                        sortedArr.push(left.shift());
                    } else {
                        sortedArr.push(right.shift());
                    }
                }
            }

            while (left.length) {
                sortedArr.push(left.shift());
            }

            while (right.length) {
                sortedArr.push(right.shift());
            }
            
            return sortedArr;
        }
    },

    beforeMount(){
        localStorage.setItem("userId", 1);
        localStorage.setItem("permission", "customer");
        this.filterRegistrations();
        this.sortRegistrations();
    }
}
</script>

<style scoped>

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