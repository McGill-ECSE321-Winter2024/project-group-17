<template>
    <div>
        <h1>Modify Session</h1>
        <div class="input-container">
            <input type="text" class="input-style" placeholder="Start time" v-model="start" />
            <input type="text" class="input-style" placeholder="End time" v-model="end" />
            <label for="datepicker">Select a date:</label>
            <input type="date" id="datepicker" name="datepicker" v-model="date">
            <input type="text" class="input-style" placeholder="Instructor Id" v-model="instructor" />
            <input type="text" class="input-style" placeholder="Course Id" v-model="course" />
            <input type="text" class="input-style" placeholder="Session id" v-model="session" />
        </div>
        <button class ="modify-btn" @click="modifySession()" v-bind:disabled="isModifyButtonDisabled">Modify</button>
        <button class ="clear-btn"  @click="clearInputs()">Clear</button>
        <button class ="cancel-btn" @click="navigateToSessions()">Cancel</button>
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
            session: null,
            start: null,
            end: null,
            date: null,
            instructor: null,
            course: null
        };
    },


    methods: {
        async modifySession() {
            const sessionToModify = {
                id: this.session,
                startTime: this.start,
                endTime: this.end,
                date: this.date,
                instructorId: this.instructor,
                courseId: this.course
            };
            try {
                await client.put(`/courses/${this.course}/sessions/${this.session}`, sessionToModify);
                this.clearInputs();
                this.navigateToSessions();
            }
            catch (e){
                alert(e.response.data.message);
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
            this.$router.push(`/session`)
        }
    },
    computed: {
        isModifyButtonDisabled() {
            return (
                !this.start || !this.end || !this.date || !this.instructor || !this.course || !this.session
            );
        }
    }
};
</script>
<style>
h1 {
    position: relative;
}
.modify-btn {
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
.cancel-btn{
   border: none;
   color: white;
   background-color: black;
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
