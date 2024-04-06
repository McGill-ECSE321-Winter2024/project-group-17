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
                    <td><input type="text"></td>
                </tr>
                <tr>
                    <td class="item">Country:</td>
                    <td><input type="text"></td>
                </tr>
                <tr>
                    <td class="item">Card Number:</td>
                    <td><input type="text"></td>
                </tr>
                <tr>
                    <td class="item">CVC:</td>
                    <td><input type="number"></td>
                </tr>
                <tr>
                    <td class="item">Expiration Date:</td>
                    <td><input type="date" name="expiration"></td>
                </tr>
            </table>
            <div id="billing-information-edit">
                <button>Edit</button>
                <button>Clear</button>
            </div>
        </div>
    </div>
</template>

<script>
import axios from 'axios'
import config from '../../config'

const client = axios.create({
    baseURL: config.dev.backendUrl
})

export default {
    name: 'BillingInformation',
    data() {
        return {
            billingInformation: {
                name: 'John Doe',
                address: '',
                country: '',
                cardNumber: '',
                cvc: '',
                expirationDate: ''
            }
        }
    },
    async created() {
        name: this.billingInformation.name
    },
    methods: {
        async 
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