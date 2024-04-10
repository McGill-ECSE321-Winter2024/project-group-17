<template>
    <div style="padding-right: 7%; width: 70%;" id="customer-registrations-main-body">
        <p style="font-weight: bold; font-size: 25px;">REGISTRATIONS</p>
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
            buttonState: false,
            // state 1 = ascending date, 2 = descending date, 3 = ascending name, 4 = descending name
            sortState: "1"
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
            // Sets registrations to contain only past registrations
            try {
                await AXIOS.get("/customerAccounts/1/registrations").then(response => {
                    this.registrations = [];
                    let regs = response.data.registrations;
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
                    this.sortRegistrations();
                });
            } catch (e) {
                alert("Failed to get registrations!" + e);
                return;
            }
        },

        async filterUpcomingRegistrations(){
            // Sets registration list to only contain upcoming sessions
            try {
                await AXIOS.get("/customerAccounts/1/registrations").then(response => {
                    this.registrations = [];
                    let regs = response.data.registrations;
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
                    this.sortRegistrations();
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
        this.filterUpcomingRegistrations();
        this.sortRegistrations();
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