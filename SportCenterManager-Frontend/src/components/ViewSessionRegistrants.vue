<template>
    <div>
        <h1>Registrants for {{ courseName }} - {{ instructorName }}</h1>
        <div class="grid-container">
            <div v-for="registrant in registrants" :key="registrant.id" class="grid-item">
                <span>{{ registrant.name }} || {{ registrant.email }}</span> <button
                    @click="removeRegistrant(registrant.id)">REMOVE</button>
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
    headers: { 'Access-Control-Allow-Origin': frontendUrl }
});
export default {
    name: 'ViewSessionRegistrants',
    data() {
        return {
            courseId: this.$route.params.courseId,
            sessionId: this.$route.params.sessionId,
            courseName: this.$route.params.courseName,
            instructorName: this.$route.params.instructorName,
            registrants: []
        };
    },
    async created() {
        try {
            const response = await client.get('/courses/' + this.courseId + '/sessions/' + this.sessionId + '/registrations/customers');
            this.registrants = response.data.customers;
        }
        catch (e) {
            console.log(e.response.data.message);
        }
    },
    methods: {
        async removeRegistrant(registrantId) {
            try {
                await client.delete('/courses/' + this.courseId + '/sessions/' + this.sessionId + '/registrations/' + registrantId);
                const response = await client.get('/courses/' + this.courseId + '/sessions/' + this.sessionId + '/registrations/customers');
                this.registrants = response.data.customers;
            }
            catch (e) {
                console.log(e);
            }
        }
    }
};
</script>

<style>
.grid-container {
    display: grid;
    grid-template-columns: repeat(3, 1fr);
    grid-gap: 10px;
    margin-left: 20%;
    margin-right: 20%;
}

.grid-item {
    background-color: rgb(63, 154, 202);
    padding: 20px;
    text-align: center;
    border-radius: 10px;
    width: fit-content;
}

.grid-item span {
    font-size: 18px;
    margin: 0;
    color: white;
}

button {
    background-color: rgb(73, 172, 225);
    color: white;
    border: none;
    border-radius: 5px;
    padding: 5px;
    margin-left: 10px;
    cursor: pointer;
}
</style>