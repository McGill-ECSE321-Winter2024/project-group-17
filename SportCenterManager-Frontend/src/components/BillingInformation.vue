<template>
    <div id="customer-billing-component">
        <div id="customer-billing-body">
            <p style="font-weight: bold; font-size: 25px; text-align: left;">PAYMENT INFORMATION</p>
            <div id="billing-information-box">
                <div id="billing-table">
                    <p class="item" style="grid-area: name;">Name:<span class="error"
                            :class="{ 'state-el': !isNameEmpty }">*</span></p>
                    <input type="text" v-model="name" :disabled="disabled" style="grid-area: name-input;">

                    <p class="item" style="grid-area: address;">Address:<span class="error"
                            :class="{ 'state-el': !isAddressEmpty }">*</span></p>
                    <input type="text" v-model="address" :disabled="disabled" style="grid-area: address-input;">

                    <p class="item" style="grid-area: country;">Country:<span class="error"
                            :class="{ 'state-el': !isCountryEmpty }">*</span></p>
                    <input type="text" v-model="country" :disabled="disabled" style="grid-area: country-input;">

                    <p class="item" style="grid-area: cardNumber;">Card Number:<span class="error"
                            :class="{ 'state-el': !isCardEmpty }">*</span></p>
                    <input type="text" maxlength="16" v-model="cardNumber" :disabled="disabled"
                        style="grid-area: cardNumber-input;">

                    <p class="item" style="grid-area: cvc;">CVC:<span class="error"
                            :class="{ 'state-el': !isCvcEmpty }">*</span></p>
                    <input type="text" maxlength="3" v-model="cvc" :disabled="disabled" style="grid-area: cvc-input;">

                    <p class="item" style="grid-area: expirationDate;">Expiration Date:<span class="error"
                            :class="{ 'state-el': !isExpirationDateEmpty }">*</span></p>
                    <input type="date" v-model="expirationDate" :disabled="disabled"
                        style="grid-area: expirationDate-input;">
                </div>
                <div id="billing-information-edit">
                    <button id="submit-btn" :class="{ 'state-el': disabled }"
                        @click="submitBillingInformation">Submit</button>
                    <button id="clear-btn" :class="{ 'state-el': disabled }"
                        @click="clearBillingInformation">Clear</button>
                    <button id='edit-btn' @click="editBillingInformation">Edit</button>
                </div>
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
    data() {
        return {
            name: null,
            address: null,
            country: null,
            cardNumber: null,
            cvc: null,
            expirationDate: null,
            disabled: true
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
        editBillingInformation() {
            const btn = document.getElementById("edit-btn");
            if (btn.innerHTML === "Cancel") {
                this.clearBillingInformation();
            }
            this.swapButtons();
        },
        async submitBillingInformation() {
            const billingInformation = {
                name: this.name,
                address: this.address,
                country: this.country,
                cardNumber: this.cardNumber,
                cvc: this.cvc,
                expirationDate: this.expirationDate
            };
            if (this.checkInput()) {
                try {
                    await client.put("/customerAccounts/15752/billingInformation", billingInformation);
                    this.swapButtons();
                }
                catch (e) {
                    console.log(e);
                }
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
        },
        swapButtons() {
            this.disabled = !this.disabled;
            const editBtn = document.getElementById("edit-btn");
            editBtn.innerHTML = this.disabled ? "Edit" : "Cancel";
        },
        checkInput() {
            if (this.isEmpty()) {
                alert("Please fill in all required fields.");
                return false;
            }
            return true;
        },
        isEmpty() {
            return this.isNameEmpty || this.isAddressEmpty || this.isCountryEmpty || this.isCardEmpty || this.isCvcEmpty || this.isExpirationDateEmpty;
        }
    },
    computed: {
        isNameEmpty() {
            return this.name === null || this.name === "";
        },
        isAddressEmpty() {
            return this.address === null || this.address === "";
        },
        isCountryEmpty() {
            return this.country === null || this.country === "";
        },
        isCardEmpty() {
            return this.cardNumber === null || this.cardNumber === "";
        },
        isCvcEmpty() {
            return this.cvc === null || this.cvc === "";
        },
        isExpirationDateEmpty() {
            return this.expirationDate === null || this.expirationDate === "";
        }
    }
}
</script>

<style>
#customer-billing-component {
    width: 100%;
    height: 100%;
}

#billing-information-box {
    margin: 0% 30% 0% 0%;
}

#billing-table {
    display: grid;
    grid-template-columns: 25% 75%;
    grid-row-gap: 10px;
    grid-column-gap: 10px;
    grid-template-areas: "name name-input" "address address-input" "country country-input" "cardNumber cardNumber-input" "cvc cvc-input" "expirationDate expirationDate-input";
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

#edit-btn,
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

.error {
    color: red;
}

.state-el {
    visibility: hidden;
}
</style>