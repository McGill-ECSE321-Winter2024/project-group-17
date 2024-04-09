<template>
    <div>
        <h1>Modify Course</h1>
        <h2>{{ this.name }}</h2>
        <div class="input-container">
            <input type="text" class="input-style" placeholder="Description" v-model="description" />
            <input type="text" class="input-style" placeholder="Cost Per Session" v-model="costPerSession" />
        </div>
        <button class ="modify-btn" @click="modifyCourse()" v-bind:disabled="isModifyButtonDisabled">Modify</button>
        <button class ="clear-btn" @click="clearInputs()">Clear</button>
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
            name: null,
            description: null,
            costPerSession: null
        };
    },
    async created() {
        try {
            const response = await client.get('/courses/' + this.$route.params.courseId);
            this.name = response.data.name;
            this.description = response.data.description;
            this.costPerSession = response.data.costPerSession;
        }
        catch (e) {
            if (e.response && e.response.data) {
                alert(e.response.data.message); 
            } 
            else {
                alert('An error occurred while modifying the course.'); 
            }
        }
    },
    methods: {
        async modifyCourse() {
            const courseToModify = {
                name: this.name,
                description: this.description,
                costPerSession: this.costPerSession
            };
            try {
                await client.put('/courses/' + this.$route.params.courseId, courseToModify);
                this.clearInputs();
                this.navigateToCourses();
            }
            catch (e) {
                if (e.response && e.response.data) {
                    alert(e.response.data.message); 
                } else {
                    alert('An error occurred while modifying the course.'); 
                }
            }
        },
        clearInputs() {
            this.description = null;
            this.costPerSession = null;
        },
        navigateToCourses() {
            this.$router.push('/courses')
        }
    },
    computed: {
        isModifyButtonDisabled() {
            return (
                !this.description && !this.costPerSession
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

.input-container {
  display: flex;
  flex-direction: column;
  align-items: center;
  justify-content: center;
  height: 20vh; 
}

.input-style {
  margin-bottom: 10px; 
  padding: 10px;
  width: 200px;
  border: 1px solid #ccc;
  border-radius: 5px;
}
</style>