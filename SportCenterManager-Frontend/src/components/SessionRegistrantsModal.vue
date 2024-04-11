<template>
    <div class="modal" @click="$emit('close')">
        <div class="modal-content" @click.stop="">
            <p style="font-weight: bold;">Registrants</p>    
            <b-table striped hover :items="registrants" :fields="fields">
                <template v-slot:cell(actions)="{ item }">
                    <b-button type="button" size="sm" @click="cancelRegistration(item)">Cancel Registration</b-button>
                </template>
            </b-table>
        </div>
    </div>
</template>

<script>
import axios from "axios";
import config from "../../config";

const frontendUrl = 'http://' + config.dev.host + ':' + config.dev.port
const backendUrl = 'http://' + config.dev.backendHost + ':' + config.dev.backendPort

const AXIOS = axios.create({
  baseURL: backendUrl,
  headers: { 'Access-Control-Allow-Origin': frontendUrl }
})

export default {
    props: {
        sessionId : Number,
        courseId: Number,
        isOpen: {
            type: Boolean,
            default: false
        }
    },

    data() {
        return {
            registrants: [],
            fields: [
            {key: "id", thClass: 'd-none', tdClass: 'd-none'},    
            "name", "email", "actions"]
        };
    },
    
    methods: {
        async getRegistrants() {
            try {
                await AXIOS.get("/courses/" + this.courseId + "/sessions/" + this.sessionId + "/registrations/customers").then(response => {
                    this.registrants = [];
                    let regs = response.data.customers;
                    for (let i = 0; i < regs.length; i++) {
                        this.registrants.push({
                            name: regs[i].name,
                            email: regs[i].email,
                            id: regs[i].id
                        });
                    }
                });
            } catch (e) {
                alert(e)
            }
        },
        
        async cancelRegistration(item){
            console.log(item);
            try {
                await AXIOS.delete("/courses/" + this.courseId + "/sessions/" + this.sessionId + "/registrations/" + item.id).then(response => {
                    this.getRegistrants();
                })
            } catch (e){
                alert("Failed to cancel registration" + e);
            }
        },
    },   

    beforeMount(){
        this.getRegistrants();
    }
}

</script>

<style scoped>
.modal {
  position: fixed;
  top: 0;
  left: 0;
  width: 100%;
  height: 100%;
  background-color: rgba(0, 0, 0, 0.5); /* Semi-transparent black background */
  display: flex;
  justify-content: center;
  align-items: center;
}

.modal-content {
  background-color: white;
  padding: 20px;
  box-shadow: 0 0 10px rgba(0, 0, 0, 0.3); /* Drop shadow effect */
}

</style>