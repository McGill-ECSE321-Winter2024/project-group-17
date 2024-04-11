<template>
    <div id="customer-account-component">
        <div id="customer-account-body">
            <p style="font-weight: bold; font-size: 25px; text-align: left;">PAYMENT INFORMATION</p>
            <div id="account-information-box">
                <div id="account-table">
                    <p class="item" style="grid-area: name;">Name:<span class="error"
                        :class="{ 'state-el': !isNameEmpty, 'disabled': disabled }">*</span></p>
                    <input type="text" v-model="name" :disabled="disabled" style="grid-area: name-input;">
                    <p class="error" style="grid-area: name-error;"
                        :class="{ 'state-el': isNameValid, 'disabled': disabled }"> {{ nameError }}</p>

                    <!-- Only show the email address field for non-customer statuses -->
                    <p v-if="status !== 'Owner'" class="item" style="grid-area: address;">Email Address:<span class="error"
                        :class="{ 'state-el': !isAddressEmpty, 'disabled': disabled }">*</span></p>
                    <input v-if="status !== 'Owner'" type="text" v-model="address" :disabled="disabled" style="grid-area: address-input;">
                    <p v-if="status !== 'Owner'" class="error" style="grid-area: address-error;"
                        :class="{ 'state-el': isAddressValid, 'disabled': disabled }"> {{ addressError }}</p>

                    <p class="item" style="grid-area: password;">Password:<span class="error"
                        :class="{ 'state-el': !isPasswordEmpty, 'disabled': disabled || status === 'Owner' }">*</span></p>
                    <input type="text" v-model="password" :disabled="disabled || status === 'Owner'" style="grid-area: password-input;">
                    <p class="error" style="grid-area: password-error;"
                        :class="{ 'state-el': isPasswordValid, 'disabled': disabled }"> {{ passwordError }}</p>
                </div>
                <div id="account-information-edit">
                    <button id="submit-btn" :class="{ 'state-el': disabled }"
                        @click="submitAccountInformation">Submit</button>
                    <button id="clear-btn" :class="{ 'state-el': disabled }"
                        @click="clearAccountInformation">Clear</button>
                    <button id='edit-btn' @click="editAccountInformation">Edit</button>
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
            password: null,
            disabled: true,
            status: localStorage.getItem('Status'),
            id: localStorage.getItem('id')
        }
    },
    async created() {
        try {
            if(this.status=='Customer'){
                const response = await client.post('/customerAccounts/' + this.id);
                this.name = response.data.name;
                this.address = response.data.address;
                this.password = response.data.password;
            }
            
            if(this.status=='Instructor'){
                const response = await client.post('/instructorAccounts/' + this.id);
                this.name = response.data.name;
                this.address = response.data.address;
                this.password = response.data.password;
            }
            if(this.status=='Owner'){
                const response = await client.post('/ownerAccount');
                this.name = response.data.name;
                this.password = response.data.password;
            }
        }
        catch(e){
            console.log(e);
        }
    },
    methods: {
        editAccountInformation() {
            const btn = document.getElementById("edit-btn");
            if (btn.innerHTML === "Cancel") {
                this.clearAccountInformation();
            }
            this.swapButtons();
        },
        async submitAccountInformation() {
            const accountInformation = {
                name: this.name,
                address: this.address,
                password: this.password,
            };
            if (this.checkInput()) {
                try {
                    await client.put("/customerAccounts/15752/billingInformation", accountInformation);
                    this.swapButtons();
                }
                catch (e) {
                    console.log(e);
                }
            }
        },
        async clearAccountInformation() {
            const response = await client.get("/customerAccounts/15752/billingInformation");
            this.name = response.data.name;
            this.address = response.data.address;
            this.password = response.data.password;
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
            if (!this.isNameValid || !this.isAddressValid || !this.isPasswordValid ) {
                alert("Please correct the errors in the form.");
                return false;
            }
            return true;
        },
        isEmpty() {
            return this.isNameEmpty || this.isAddressEmpty || this.isPasswordEmpty;
        }
    },
    computed: {
        
        isNameEmpty() {
            return this.name === null || this.name === "";
        },
        isAddressEmpty() {
            return this.address === null || this.address === "";
        },
        isPasswordEmpty() {
            return this.password === null || this.password === "";
        },
        isNameValid() {
            if (this.isNameEmpty) {
                this.nameError = "Name is required.";
                return false;
            }
            this.nameError = "";
            return true;
        },
        isAddressValid() {
            if (this.isAddressEmpty) {
                this.addressError = "Address is required.";
                return false;
            }
            this.addressError = "";
            return true;
        },
        isPasswordValid() {
            if (this.isPasswordEmpty) {
                this.passwordError = "Password is required.";
                return false;
            }
            this.passwordError = "";
            return true;
        }
        
    }
}
</script>

<style>
#customer-account-component {
    width: 100%;
    height: 100%;
}

#account-information-box {
    margin: 0% 30% 0% 0%;
}

#account-table {
    display: grid;
    grid-template-columns: 25% 75%;
    grid-row-gap: 10px;
    grid-column-gap: 10px;
    grid-template-areas: "name name-input" ". name-error" "address address-input" ". address-error" "password password-input" ". password-error" "cardNumber cardNumber-input" ". card-error" "cvc cvc-input" ". cvc-error" "expirationDate expirationDate-input" ". date-error";
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

#account-information-edit {
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
    text-align: left;
}

.error.disabled {
    display: none;

}

.state-el {
    display: none;
}
</style>