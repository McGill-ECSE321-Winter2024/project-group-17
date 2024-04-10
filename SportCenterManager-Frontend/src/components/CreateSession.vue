<template>
    <div>
        <h1>Create Session</h1>
        <div class="input-container">
            <input type="text" class="input-style" placeholder="Start time" v-model="start" />
            <input type="text" class="input-style" placeholder="End time" v-model="end" />
            <input type="text" class="input-style" placeholder="Date" v-model="date" />
            <input type="text" class="input-style" placeholder="Instructor Id" v-model="instructor" />
            <input type="text" class="input-style" placeholder="Course Id" v-model="course" />
        </div>
        <button class ="create-btn" @click="createSession()" v-bind:disabled="isCreateButtonDisabled">Create</button>
        <button class ="clear-btn"  @click="clearInputs()">Clear</button>
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
            instructor: null,
            course: null
        };
    },

    /*async created() {
            try {
                const response = await client.get("/courses/{course_id}/sessions");
                this.sessions = response.data.sessions
            }
            catch (e) {
                alert("Failed to create session. " + e);
            }
    },*/

    methods: {
        async createSession() {
            const sessionToCreate = {
                start: this.start,
                end: this.end,
                date: this.date,
                instructor: this.instructor,
                course: this.course
            };
            try {
                await client.post('/courses/{course_id}/sessions', sessionToCreate);
                this.clearInputs();
                this.navigateToSessions();
            }
            catch (e) {
                alert("Failed to create session. " + e);
            }
        },
        clearInputs() {
            this.start= null,
            this.end= null,
            this.date= null,
            this.instructor= null,
            this.course= null
        },
        navigateToSessions() {
            this.$router.push('/courses/{course_id}/sessions')
        }
    },
    computed: {
        isCreateButtonDisabled() {
            return (
                !this.start || !this.end || !this.date || !this.instructor || !this.course
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
  height: 50vh;
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
