<template>
    <div>
        <h1>Create Session</h1>
        <div class="input-container">
            <input type="text" class="input-style" placeholder="Start time" v-model="start" />
            <input type="text" class="input-style" placeholder="End time" v-model="end" />
            <label for="datepicker">Select a date:</label>
            <input type="date" id="datepicker" name="datepicker" v-model="date">
            <input type="text" class="input-style" placeholder="Instructor Id" v-model="instructor" />
            <input type="text" class="input-style" placeholder="Course Id" v-model="course" />
        </div>
        <button class ="create-btn" @click="createSession()" v-bind:disabled="isCreateButtonDisabled">Create</button>
        <button class ="clear-btn"  @click="clearInputs()">Clear</button>
        <button class ="modify-btn" @click="navigateToModifySessions()">Modify</button>
        <button class ="delete-btn" @click="navigateToDeleteSessions()">Delete</button>
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

    methods: {
        async createSession() {
            const sessionToCreate = {
                startTime: this.start,
                endTime: this.end,
                date: this.date,
                instructorId: this.instructor,
                courseId: this.course
            }
            try {
                console.log(this.instructor);
                console.log(this.course);
                await client.post(`/courses/${this.course}/sessions`, sessionToCreate);
                this.clearInputs();
                this.navigateToSessions();
            }
            catch(e){
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
        navigateToModifySessions(){
          this.$router.push('session/modify')
        },
        navigateToDeleteSessions(){
          this.$router.push('session/delete')
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
.modify-btn{
   border: none;
   color: white;
   background-color: black;
   padding: 10px 20px;
   border-radius: 5px;
}
.delete-btn{
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
