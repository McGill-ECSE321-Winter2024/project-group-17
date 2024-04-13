<template>
    <div id="supervise-body" style="padding-top: 3%; padding-left: 5%;">
            <div id="supervise-text">
                <p class="supervise-course-desc-text">Course Description: <br> {{ this.session.course.description }}</p>
                <p class="supervise-course-desc-text" style="margin-top: 1%;">Cost: {{ this.session.course.costPerSession }}$/Session</p>
                
                <p class="supervise-session-desc-text" style="margin-top: 3%;">You are about to supervise a session for the <b> {{ this.session.course.name }} </b> course</p> 
                <p class="supervise-session-desc-text"> <b>Date/Time:</b> {{ this.session.date }} -- {{ this.session.startTime }} to {{ this.session.endTime }}</p> 
            </div>
            <div id="supervise-buttons">
                <button type="button" class="supervise-btns" @click="$router.go(-1)">BACK</button>
                <button type="button" class="supervise-btns" @click="supervise()">CONFIRM</button>
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
    name: 'ModifyCourse',
    data() {
        return {
            courseId: this.$route.params.courseId, 
            sessionId: this.$route.params.sessionId,
            instructorId: localStorage.getItem("Id"),
            session: {
                course: {
                    name: ''
                },
                instructor: {
                    name: '',
                    email: ''
                },
                startTime: '',
                endTime: '',
                date: ''
            }
        };
    },
    methods: {
        async supervise(){
            try {
                await client.put('/courses/' + this.courseId + '/sessions/' + this.sessionId + '/instructor/' + this.instructorId);
                this.$router.go(-1);
            }
            catch (e) {
                alert(e.response.data.message)
            }

        }
    },
    async created() {
        try {
            const response = await client.get('/courses/' + this.courseId + '/sessions/' + this.sessionId);
            this.session = response.data;
        }
        catch (e) {
            alert(e.response.data.message);
        }
    }
}
</script>

<style>
.supervise-course-desc-text{
    font-size: 17px;
    text-align: left;
}

.supervise-session-desc-text{
    font-size: 17px;
    text-align: left;
    margin: 0%;
}

#supervise-buttons {
  display: flex;
  justify-content: space-between;
  flex-direction: row;
  align-items: top center;
  padding-top: 8%;
  margin-left: 22%;
  width: 50%;
  height: 100%;
}

.supervise-btns {
    background-color: white;
    color: #000000;
    width: 40%;
    height: 10%;
}
</style>