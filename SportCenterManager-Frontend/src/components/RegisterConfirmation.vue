<template>
<div id="confirmation-container" style="background-color: white; padding-top: 3%;">
    <img src="@/assets/checkmark.png">
    <p style="font-size: 20px; font-weight: bold; margin-top: 1%;">You have successfully registered for this session!</p>
    <p>You will shortly receive an email of confirmation.</p>
    <hr>
    <div id="session-details" style="text-align: left; margin-left: 30%;">
        <p><b>Course: </b>{{ session.course.name }} </p>
        <p><b>Date: </b> {{ session.date }}</p>
        <p><b>Time: </b> {{ session.startTime}} - {{session.endTime }}</p>
        <p><b>Instructor: </b> {{ session.instructor.name }}</p>
        <p><b>Instructor Email: </b> {{ session.instructor.email }}</p>
    </div>
    <button type="button" style="background-color: white; color: black; width: 15%; margin-top: 3%;" @click="$router.push({name:'Sessions'})">BACK TO SESSIONS</button>
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

const PROPS = defineProps({
    sessionId: Number,
    courseId: Number
})

export default {
    data () {
        return {
            customer: undefined,
            session: {
                course: undefined,
                instructor: undefined,
                startTime: undefined,
                endTime: undefined,
                date: undefined
            },
            confirmEnabled: true
        };
    }, 

    methods: {
        async getSession(){
            try {
                await AXIOS.get("/courses/" + PROPS.courseId + "/sessions/" + PROPS.sessionId).then(response => {
                    this.session = response.data
                });
            } catch (e) {
                alert("Failed to get session!" + e);
                return;
            }
        }
    },

    beforeMount() {
        // Temporary placeholder
        this.getSession();
        this.customer = {
            id: 1
        };
    }
}
</script>

<style>

p {
    margin: 0%;
}

</style>