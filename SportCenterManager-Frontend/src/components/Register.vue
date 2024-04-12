<template>
    <div id="register-components">
        <div id="register-body" style="padding-top: 3%; padding-left: 5%;">
            <div id="register-text">
                <p class="register-course-desc-text" id="course-desc" >Course Description: <br> {{ this.session.course.description }}</p>
                <p class="register-course-desc-text" id="course-cost" style="margin-top: 1%;">Cost: {{ this.session.course.costPerSession }}$/Session</p>
                
                <p class="register-session-desc-text" id="course-name" style="margin-top: 3%;">You are about to register to a session for the <b> {{ this.session.course.name }} </b> course</p> 
                <p class="register-session-desc-text" id="session-date-time" > <b>Date/Time:</b> {{ this.session.date }} -- {{ this.session.startTime }} to {{ this.session.endTime }}</p> 
                <p class="register-session-desc-text" id="session-instructor-contact"> <b>Instructor:</b> {{ this.session.instructor.name }} - {{ this.session.instructor.email }}</p>
            </div>
            <div id="register-buttons">
                <button type="button" class="register-btns" @click="$router.go(-1)">BACK</button>
                <button type="button" class="register-btns" v-bind:disabled="!confirmEnabled" @click="register()">CONFIRM</button>
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
    
    data () {
        return {
            session: {
                course: {
                    name: undefined
                },
                instructor: {
                    name: undefined,
                    email: undefined
                },
                startTime: undefined,
                endTime: undefined,
                date: undefined
            },
            confirmEnabled: true,
        };
    }, 

    methods: {
        async getSession(){
            try {
                await AXIOS.get("/courses/" + this.$route.params.courseId + "/sessions/" + this.$route.params.sessionId).then(response => {
                    this.session = response.data
                });
            } catch (e) {
                alert("Failed to get session!" + e);
                return;
            }
        },

        async register(){
            if (this.hasBillingInfo()) {
                await AXIOS.put("/courses/" + this.session.course.id + "/sessions/" + this.session.id + "/registrations/" + localStorage.getItem("Id")).then(response => {
                    localStorage.setItem("registerAuthenticated", true);
                    this.$router.push({name: "RegistrationConfirmation", params: {courseId: this.session.course.id, sessionId: this.session.id}});
                }).catch(response => {
                    alert(response.response.data.message)
                })
                this.confirmEnabled = false;
            } else {
                alert("Please add billing information before registering!")
            }  
        },
        hasBillingInfo() {
            return localStorage.getItem("hasBilling") === "true";
        }
    },
    beforeMount() {
        this.getSession();
    }
}

</script>

<style scoped>
.background {
    position: relative;
    display: inline-block;
}

.text-overlay {
    font-size: 96px;
    color: white;
    font-weight: bold;
}

.register-course-desc-text{
    font-size: 17px;
    text-align: left;
}

.register-session-desc-text{
    font-size: 17px;
    text-align: left;
    margin: 0%;
}

#register-buttons {
  display: flex;
  justify-content: space-between;
  flex-direction: row;
  align-items: top center;
  padding-top: 8%;
  margin-left: 22%;
  width: 50%;
  height: 100%;
}

.register-btns {
    background-color: white;
    color: #000000;
    width: 40%;
    height: 10%;
}

.register-btns:disabled {
    color: gray;
}

</style>
