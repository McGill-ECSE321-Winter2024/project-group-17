<template>
<div id="confirmation-container" style="background-color: white; padding-top: 3%;">
    <img src="@/assets/checkmark.png">
    <p style="font-size: 20px; font-weight: bold; margin-top: 1%; text-align: center">You have successfully registered for this session!</p>
    <p style="text-align: center;">You will shortly receive an email of confirmation.</p>
    <hr>
    <div id="session-details" style="text-align: left; margin-left: 30%;">
        <p placeholder=""><b>Course: </b>{{ session.course.name }} </p>
        <p placeholder=""><b>Date: </b> {{ session.date }}</p>
        <p placeholder=""><b>Time: </b> {{ session.startTime}} - {{session.endTime }}</p>
        <p placeholder=""><b>Instructor: </b> {{ session.instructor.name }}</p>
        <p placeholder=""><b>Instructor Email: </b> {{ session.instructor.email }}</p>
    </div>
    <button type="button" style="background-color: white; color: black; width: 15%; margin-top: 3%;" @click="$router.push({name:'Sessions', params: {courseId: session.course.id}})">BACK TO SESSIONS</button>
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
            confirmEnabled: true
        };
    }, 

    methods: {
        async getSession(){
            try {
                await AXIOS.get("/courses/" + this.$route.params.courseId + "/sessions/" + this.$route.params.sessionId).then(response => {
                    this.session = response.data;
                });
            } catch (e) {
                alert("Failed to get session!" + e);
                return;
            }
        }
    },

    beforeMount() {
        this.getSession();
    }
}
</script>

<style scoped>

p {
    margin: 0%;
}

</style>