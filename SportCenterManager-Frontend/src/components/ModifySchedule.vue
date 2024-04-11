<template>
    <div style="padding-right: 7%; width: 80%;" id="customer-registrations-main-body">
        <p style="font-weight: bold; font-size: 25px;">MODIFY CENTER SCHEDULE</p> 
        <div class = "content">
            <p> Current Schedule: {{ curr_open }} to {{ curr_close }}</p>
            <div class = "time-div">
                <p> Opening Time: </p>
                <div>
                    <b-form-timepicker  v-model="openingHour" locale="en"></b-form-timepicker>
                </div>
            </div>      
            <div class = "time-div">
                <p> Closing Time: </p>
                <div>
                    <b-form-timepicker  v-model="closingHour" locale="en"></b-form-timepicker>
                </div>
            </div>
            <button class = "update-btn" @click ="modifySchedule()" v-bind:disabled="isUpdateButtonDisabled"> Update Schedule </button>
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
    //headers: { 'Access-Control-Allow-Origin': frontendUrl}
});

export default {
    name: 'ModifySchedule',

    data() {
        return {
            curr_open: null,
            curr_close: null,
            openingHour: null,
            closingHour: null
        };
    },
    beforeMount(){
        try{
            current = client.get('/schedule');
            this.curr_open = current.openingHour();
            this.curr_close = current.closingHour();
        }catch(e){
            this.curr_open = "N/A";
            this.curr_close = "N/A";
        }
    },
    methods: {
        async modifySchedule() {
            const newSchedule ={
                openingHour: this.openingHour,
                closingHour: this.closingHour
            };
            
            try {
                console.log(this.openingHour);
                console.log(this.closingHour);
                await client.post('/schedule', newSchedule);
                this.curr_open = this.openingHour;
                this.curr_close = this.closingHour;
                this.clearInputs(); 
            }
            catch (e) {
                if (e.response && e.response.data) {
                    alert(e.response.data.message); 
                } else {
                    alert('An error occurred while modifying the schedule.'); 
                }
            }
        },
        clearInputs() {
            this.openingHour = null;
            this.closingHour = null;
        },
    },
    computed: {
        isUpdateButtonDisabled() {
            return (
                !this.openingHour || !this.closingHour
            );
        }
  
    }
       
};

</script>
<style>
    .content{
        display:flex;
        flex-direction: column;
        width:100%;
    }
    .time-div{
        width: 90%;
        max-width: 500px;
        display:flex;
        margin-top: 2vw;
        flex-direction: row;
        justify-content:space-between;
    }
    .update-btn{
        width: 90%;
        max-width: 500px;
        margin-top: 2vw;
        background-color: white;
    }
    .update-btn:hover{
        background-color:black;
        color:white;
    }
    .update-btn:disabled,update-btn[disabled]{
        color:grey;
        background-color: white;
    }
</style>