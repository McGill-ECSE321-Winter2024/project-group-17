<template>
    <div>
        <h1>Create Session</h1>
        <p style="font-weight: bold; font-size: 24px;">{{ this.name }}</p>
        <div class="input-container">
            <label for="start-time">Select a start time:</label>
            <input type="time" class="input-style" id="start-time" v-model="start" step="1" />
            <label for="end-time">Select an end time:</label>
            <input type="time" class="input-style" id="end-time" v-model="end" step="1" />
            <label for="end-time">Select a date:</label>
            <input type="date" class="input-style" id="date" v-model="date" />
            <select class="input-style" v-model="instructor">
                <option value="">Select an instructor</option>
                <option v-for="instructor in instructors" :key="instructor.id" :value="instructor.id">{{ instructor.name }}</option>
            </select>
        </div>
        <button class ="create-btn" @click="createSession()" v-bind:disabled="isCreateButtonDisabled">Create</button>
        <button class ="clear-btn"  @click="clearInputs()">Clear</button>
        <button class ="create-btn"  @click="navigateToSessions()">Cancel</button>
    </div>
</template>

<script>
import axios from "axios";
import config from "../../config";

const frontendUrl = 'http://' + config.dev.host + ':' + config.dev.port;
const backendUrl = 'http://' + config.dev.backendHost + ':' + config.dev.backendPort;

const client = axios.create({
    baseURL: backendUrl,
    headers: { 'Access-Control-Allow-Origin': frontendUrl}
});

export default {
    name: 'CreateSession',
    data() {
        return {
            sessions: [],
            start: null,
            end: null,
            date: null,
            instructor: '',
            name: null,
            instructors: []
        };
    },

    async created() {
            try {
                const response = await client.get("/courses/" + this.$route.params.courseId);
                this.name = response.data.name;
            }
            catch (e) {
                alert(e.response.data.message);
            }
            try {
                const response = await client.get("/instructorAccounts");
                this.instructors = response.data.instructors;
            }
            catch (e) {
                alert(e.response.data.message);
            }
    },

    methods: {
        async createSession() {

            if (this.instructor === '') {
                this.instructor = -1;
            }

            const sessionToCreate = {
                startTime: this.start,
                endTime: this.end,
                date: this.date,
                instructorId: this.instructor,
                courseId: this.$route.params.courseId  
            };
            try {
                await client.post(`/courses/` + this.$route.params.courseId + `/sessions`, sessionToCreate);
                this.clearInputs();
                this.navigateToSessions();
            }
            catch (e) {
                alert(e.response.data.message);
            }
        },
        clearInputs() {
            this.start= null,
            this.end= null,
            this.date= null,
            this.instructor= null
        },
        navigateToSessions() {
            this.$router.push('/courses/sessions/' + this.$route.params.courseId);
        }
    },
    computed: {
        isCreateButtonDisabled() {
            return (
                !this.start || !this.end || !this.date
            );
        }
    }
};
</script>
<style>
h1 {
    position: relative;
}
.create-btn {
    border: none;
    color: white;
    background-color: black;
    padding: 10px 20px;
    border-radius: 5px;
}
.clear-btn {
    color: black;
    background-color: white;
    padding: 10px 20px;
    border-radius: 5px;
}
.input-container {
  display: flex;
  flex-direction: column;
  align-items: center;
  justify-content: center;
  height: 70vh;
}
td,
th {
    padding: 0.5em;
    border: 1px solid black;
}
.input-style {
  margin-bottom: 10px;
  padding: 10px;
  width: 200px;
  border: 1px solid #ccc;
  border-radius: 5px;
}
</style>
