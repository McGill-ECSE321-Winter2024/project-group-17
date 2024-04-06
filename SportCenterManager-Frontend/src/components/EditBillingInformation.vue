<template>
    <div>
        <div id="billing-information-heading">
            <h1>Payment Information</h1>
        </div>

        <div id="billing-information-box">
            <table>
                <tr>
                    <td class="item">Name:</td>
                    <td><input type="text" v-model="name"></td>
                </tr>
                <tr>
                    <td class="item">Address:</td>
                    <td><input type="text" v-model="address"></td>
                </tr>
                <tr>
                    <td class="item">Country:</td>
                    <td><input type="text" v-model="country"></td>
                </tr>
                <tr>
                    <td class="item">Card Number:</td>
                    <td><input type="text" v-model="cardNumber"></td>
                </tr>
                <tr>
                    <td class="item">CVC:</td>
                    <td><input type="number" v-model="cvc"></td>
                </tr>
                <tr>
                    <td class="item">Expiration Date:</td>
                    <td><input type="date" v-model="expirationDate"></td>
                </tr>
            </table>
            <div id="billing-information-edit">
                <button @click="editBillingInformation">Submit</button>
                <button @click="clearBillingInformation">Clear</button>
            </div>
        </div>
    </div>
</template>

<script>
import axios from 'axios'
import config from '../../config'

const frontendUrl = 'http://' + config.dev.host + ':' + config.dev.port;
const backendUrl = 'http://' + config.dev.backendHost + ':' + config.dev.backendPort;

const client = axios.create({
    baseURL: backendUrl,
    headers: { 'Access-Control-Allow-Origin': frontendUrl }
})

export default {
    name: 'BillingInformation',
    data() {
        return {
            name: null,
            address: null,
            country: null,
            cardNumber: null,
            cvc: null,
            expirationDate: null,
        }
    },
    async created() {
        try {
            const response = await client.get("/customerAccounts/15752/billingInformation");
            this.name = response.data.name;
            this.address = response.data.address;
            this.country = response.data.country;
            this.cardNumber = response.data.cardNumber;
            this.cvc = response.data.cvc;
            this.expirationDate = response.data.expirationDate;
        }
        catch (e) {
            console.log(e);
        }
    },
    methods: {
        async editBillingInformation() {
            const billingInformation = {
                name: this.name,
                address: this.address,
                country: this.country,
                cardNumber: this.cardNumber,
                cvc: this.cvc,
                expirationDate: this.expirationDate
            };
            try {
                await client.put("/customerAccounts/15752/billingInformation", billingInformation);
                this.$router.push("/billingInformation");
            }
            catch (e) {
                console.log(e);
            }
        },
        async clearBillingInformation() {
            const response = await client.get("/customerAccounts/15752/billingInformation");
            this.name = response.data.name;
            this.address = response.data.address;
            this.country = response.data.country;
            this.cardNumber = response.data.cardNumber;
            this.cvc = response.data.cvc;
            this.expirationDate = response.data.expirationDate;
        }
    }
}
</script>

<style>
#billing-information-heading {
    padding: 0 0 10px 0;
}

h1 {
    text-align: left;
    margin: 10% 30% 0%;
}

#billing-information-box {
    border: 1px solid black;
    margin: 0% 30% 0% 30%;
    width: flex;
}

table {
    width: 100%;
}

td {
    text-align: right;
    padding: 10px;
}

.item {
    width: 20%;
}

td>input {
    width: 100%;
    padding: 5px;
    border-radius: 5px;
    border: 1px solid #ccc;
    margin: 5px 0;
    box-sizing: border-box;
}

#billing-information-edit {
    padding: 10px 0px 10px 0px;
}

button {
    margin: 0% 5% 0% 5%;
    padding: 5px;
    background-color: #4CAF50;
    color: white;
    width: 20%;
    border: none;
    border-radius: 5px;
    cursor: pointer;
}
</style>