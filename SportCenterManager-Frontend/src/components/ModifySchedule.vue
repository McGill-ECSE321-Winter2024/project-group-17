<template>
    <div style="padding-right: 7%; width: 80%;" id="customer-registrations-main-body">
        <p style="font-weight: bold; font-size: 25px;">MODIFY CENTER SCHEDULE</p> 
        <div class = "content">
            <div class = "time-div">
                <p> Opening Time: </p>
                <div>
                    <b-form-timepicker  v-model="OpeningHour" locale="en"></b-form-timepicker>
                </div>
            </div>      
            <div class = "time-div">
                <p> Closing Time: </p>
                <div>
                    <b-form-timepicker  v-model="ClosingHour" locale="en"></b-form-timepicker>
                </div>
            </div>
            <button class = "update-btn" @click ="modifySchedule()"> Update Schedule </button>
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
    name: 'ModifySchedule',

    data() {
        return {
            OpeningHour: null,
            ClosingHour: null
        };
    },

    methods: {
        async modifySchedule() {
            const newSchedule ={
                openingHour: this.OpeningHour,
                closingHour: this.ClosingHour
            };
            
            try {
                console.log(this.OpeningHour);
                console.log(this.ClosingHour);
                await client.post('/schedule', newSchedule);
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
            this.OpeningHour = null;
            this.ClosingHour = null;
        },
    },
    computed: {
        isUpdateButtonDisabled() {
            return (
                !this.openingHour && !this.closingHour
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
</style>