<template>
    <div>
        <h1>Create Course</h1>
        <div class="input-container">
            <input type="text" class="input-style" placeholder="Name" v-model="name" />
            <input type="text" class="input-style" placeholder="Description" v-model="description" />
            <input type="text" class="input-style" placeholder="Cost Per Session" v-model="costPerSession" />
        </div>
        <button class ="create-btn" @click="createCourse()" v-bind:disabled="isCreateButtonDisabled">Create</button>
        <button class ="clear-btn" @click="clearInputs()">Clear</button>
        <button class ="create-btn" @click="closeModal()">Cancel</button>
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
    name: 'CreateCourse',
    data() {
        return {
            name: null,
            description: null,
            costPerSession: null
        };
    },
    methods: {
        async createCourse() {
            const courseToCreate = {
                name: this.name,
                description: this.description,
                costPerSession: this.costPerSession
            };
            try {
                const response = await client.post('/courses', courseToCreate);
                this.clearInputs();
                if (this.isOwner) {
                    await client.put('/courses/' + response.data.id + '/approve');
                }
                this.closeModal();
            }
            catch (e) {
                if (e.response && e.response.data) {
                    alert(e.response.data.message); 
                } 
                else {
                    alert('An error occurred while creating the course.'); 
                }
            }
        },
        clearInputs() {
            this.name = null;
            this.description = null;
            this.costPerSession = null;
        },
        closeModal() {
            this.$emit('close');
        }
    },
    computed: {
        isCreateButtonDisabled() {
            return (
                !this.name || !this.description || !this.costPerSession
            );
        },
        isOwner(){
            return localStorage.getItem("Status") === "Owner";
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
  height: 30vh; 
}

.input-style {
  margin-bottom: 10px;
  padding: 10px;
  width: 200px;
  border: 1px solid #ccc;
  border-radius: 5px;
}
</style>
