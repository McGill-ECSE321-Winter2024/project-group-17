<template>
    <div id="customer-billing-component">
        <p style="font-weight: bold; font-size: 25px;">PAYMENT INFORMATION</p>
        <div id="billing-information-box">
            <div id="billing-table">
                <p class="item" style="grid-area: name;">Name:</p>
                <input type="text" v-model="name" :disabled="disabled" style="grid-area: name-input;">

                <p class="item" style="grid-area: address;">Address:</p>
                <input type="text" v-model="address" :disabled="disabled" style="grid-area: address-input;">

                <p class="item" style="grid-area: country;">Country:</p>
                <input type="text" v-model="country" :disabled="disabled" style="grid-area: country-input;">

                <p class="item" style="grid-area: cardNumber;">Card Number:</p>
                <input type="text" v-model="cardNumber" :disabled="disabled" style="grid-area: cardNumber-input;">

                <p class="item" style="grid-area: cvc;">CVC:</p>
                <input type="number" v-model="cvc" :disabled="disabled" style="grid-area: cvc-input;">

                <p class="item" style="grid-area: expirationDate;">Expiration Date:</p>
                <input type="date" v-model="expirationDate" :disabled="disabled"
                    style="grid-area: expirationDate-input;">
            </div>
            <div id="billing-information-edit">
                <button id="submit-btn" @click="editBillingInformation">Submit</button>
                <button id="clear-btn" @click="clearBillingInformation">Clear</button>
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
                this.$router.push("/customerAccount/billing");
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

#billing-information-box {
    border: 0px;
    margin: 0% 30% 0% 0%;
}

.item {
    width: 100%;
    text-align: right;
    padding: 10px;
}

input {
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

#submit-btn,
#clear-btn {
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