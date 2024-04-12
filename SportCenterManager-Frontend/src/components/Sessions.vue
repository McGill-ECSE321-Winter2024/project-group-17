<template>
    <div>
        <div class="createbtn-container" v-if="isOwner">
            <button @click="navigateToCreate" class="createbtn btn">Create Session</button>
        </div>

        <p style="font-weight: bold; font-size: 48px; text-align: left; margin-left: 200px; margin-bottom: 0px;">{{ name }}</p>
        <p style="font-style: italic; font-size: 24px; text-align: left; margin-left: 200px;margin-bottom: 10px;">${{ costPerSession }}/session</p>
        <p style="font-size: 18px; text-align: left; margin-left: 200px;">{{ description }}</p>

        <div v-if="sessions.length === 0">
            <p>No sessions currently available.</p>
        </div>
    
        <div class="session-grid">
            <div v-for="session in sessions" :key="session.id" class="session-box">
                <h3>{{ instructors[session.instructor.id] }}</h3>
                
                <p>{{ session.startTime }} - {{ session.endTime }}</p>
                <p>{{ session.date }}</p>
                
                <div v-if="isCustomer">
                    <router-link :to="{ name: 'Register', params: { sessionId: session.id, courseId: courseId} }">
                        <button class="btn">REGISTER</button>
                    </router-link>
                </div>
                
                <div v-if="isInstructor && instructors[session.instructor.id] === 'TBD'">
                    <router-link :to="{ name: 'SuperviseSession', params: {sessionId: session.id, courseId: courseId}}">
                        <button class="btn">SUPERVISE</button>
                    </router-link>
                    
                </div>
                
                <div v-if="isOwner || isInstructor">
                    <router-link :to="{ name: 'ModifySession', params: { sessionId: session.id, courseId: courseId, instructorId: session.instructor.id} }">
                        <button class="btn">MODIFY</button>
                    </router-link>
                    <button @click="confirmDeletion(session.id)">DELETE</button>  
                </div>
            </div>
        </div>
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
    name: 'Sessions',
    data() {
        return {
            name: null,
            description: null,
            costPerSession: null,
            sessions: [],
            instructors: {},
            courseId: this.$route.params.courseId
        };
    },
    async created() {
        this.$set(this.instructors, 0, 'TBD');
        try {
            const response = await client.get('/courses/' + this.$route.params.courseId);
            this.name = response.data.name;
            this.description = response.data.description;
            this.costPerSession = response.data.costPerSession;
        }
        catch (e) {
            alert(e.response.data.message);
        }
        try {
            const response = await client.get('/courses/' + this.$route.params.courseId + '/sessions');
            this.sessions = response.data.sessions;
        }
        catch (e) {
            alert(e.response.data.message);
        }
        this.sessions.forEach(session => {
            this.findInstructor(session.instructor.id);
        });
    },
    methods: {
        async findInstructor(instructorId){
            if (instructorId !== 0){
                try {
                    const response = await client.get('/instructorAccounts/' + instructorId);
                    const name = response.data.name;
                    this.$set(this.instructors, instructorId, name);
                } catch (error) {
                    console.error('Error fetching instructor name:', error.response.data.message);
                }    
            }
             
        },
        navigateToCreate(){
            this.$router.push('/courses/sessions/create/' + this.$route.params.courseId);
        },
        async confirmDeletion(sessionId){
            if (window.confirm('Are you sure you want to delete this session?')) {
                await this.performDeletion(sessionId);
            } else {}
            },
        async performDeletion(sessionId){
            try {
                await client.delete('/courses/' + this.$route.params.courseId + '/sessions/' + sessionId);
                this.sessions = this.sessions.filter(session => session.id !== sessionId);
            }
            catch (e){
                alert(e.response.data.message);
            }
            }
    },
    computed: {
        isOwner() {
            return localStorage.getItem("Status") === 'Owner';
        },
        isInstructor(){
            return localStorage.getItem("Status") === 'Instructor';
        },
        isCustomer(){
            return localStorage.getItem("Status") === 'Customer';
        }
    }
}
</script>

<style>
.session-grid {
  display: grid;
  grid-template-columns: repeat(2, 1fr);
  grid-gap: 20px;
  margin-left: 200px;
  margin-right: 200px;
}

.session-box {
  border: 1px solid #ccc;
  padding: 10px;
}

.btn {
  background-color: #007bff;
  color: white;
  border: none;
  padding: 5px 10px;
  cursor: pointer;
}

.btn:hover {
  background-color: #0056b3;
}

.createbtn{
  background-color: black;
  color: white;
  border-radius: 10px;
}

.createbtn-container{
  text-align: right;
  margin-right: 20px;
}
</style>