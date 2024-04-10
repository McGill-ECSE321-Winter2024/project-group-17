<template>
    <div>
        <h1>Delete Session</h1>
        <div class="input-container">
            <input type="text" class="input-style" placeholder="Session Id" v-model="id" />
            <input type="text" class="input-style" placeholder="Course Id" v-model="course" />
        </div>
        <button class ="delete-btn" @click="deleteSession()" v-bind:disabled="isDeleteButtonDisabled">Delete</button>
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
    name: 'ModifySession',
    data() {
        return {
            sessions: [],
            id: null,
            course: null
        };
    },


    methods: {
        async deleteSession() {
            const sessionToDelete = {
                id: this.id,
                course: this.course
            };
            try {
                await client.delete('/courses/{course_id}/sessions/{session_id}', sessionToDelete);
                this.clearInputs();
                this.navigateToSessions();
            }
            catch (e) {
                alert("Failed to delete session. " + e);
            }
        },
        clearInputs() {
            this.id= null,
            this.course= null
        },
        navigateToSessions() {
            this.$router.delete('/courses/{course_id}/sessions/{session_id}')
        }
    },
    computed: {
        isDeleteButtonDisabled() {
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
.delete-btn {
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
