<template>
    <div>
        <h1>Find a Session</h1>
        <div class="input-container">
            <input type="text" class="input-style" placeholder="Session Id" v-model="id" />
            <input type="text" class="input-style" placeholder="Course Id" v-model="course" />
        </div>
        <button class ="find-btn" @click="findSession()" v-bind:disabled="isFindButtonDisabled">Find Session</button>
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
    name: 'FindSession',
    data() {
        return {
            sessions: [],
            id: null,
            course: null
        };
    },


    methods: {
        async findSession() {
            const sessionToFind = {
                id: this.id,
                course: this.course
            };
            try {
                await client.get('/courses/{course_id}/sessions/{session_id}', sessionToFind);
                this.clearInputs();
                this.navigateToSessions();
            }
            catch (e) {
                alert("Failed to find session. " + e);
            }
        },
        clearInputs() {
            this.id= null,
            this.course= null
        },
        navigateToSessions() {
            this.$router.get('/courses/{course_id}/sessions/{session_id}')
        }
    },
    computed: {
        isFindButtonDisabled() {
            return (
                !this.id || !this.course
            );
        }
    }
};
</script>
<style>
h1 {
    position: relative;
}
.find-btn {
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
