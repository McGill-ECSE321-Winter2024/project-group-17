<template>
    <div id="account-info-component">
        <div id="account-info-body">
            <p style="font-weight: bold; font-size: 25px; text-align: left;">MY ACCOUNT</p>
            <div id="account-information-box">
                <div id="account-table">
                    <p class="item" style="grid-area: name;">Name:<span class="error"
                            :class="{ 'state-el': !isNameEmpty, 'disabled': disabled }">*</span></p>
                    <input type="text" v-model="name" :disabled="disabled" style="grid-area: name-input;">
                    <p class="error" style="grid-area: name-error;"
                        :class="{ 'state-el': isNameValid, 'disabled': disabled }"> {{ nameError }}</p>

                    <!-- Only show the email address field for non-customer statuses -->
                    <p v-if="!isOwner" class="item" style="grid-area: email;">Email Address:<span class="error"
                            :class="{ 'state-el': !isEmailEmpty, 'disabled': disabled }">*</span></p>
                    <input v-if="!isOwner" type="text" v-model="email" :disabled="disabled"
                        style="grid-area: email-input;">
                    <p v-if="!isOwner" class="error" style="grid-area: email-error;"
                        :class="{ 'state-el': isEmailValid, 'disabled': disabled }"> {{ emailError }}</p>

                    <p class="item" style="grid-area: password;">Password:<span class="error"
                            :class="{ 'state-el': !isPasswordEmpty, 'disabled': disabled }">*</span>
                    </p>
                    <input type="text" :disabled="disabled" :value="formatPassword" @input="updatePassword"
                        style="grid-area: password-input;">
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
            email: null,
            password: null,
            disabled: true,
            status: null,
            id: null
        }
    },
    async created() {
        try {
            this.status = localStorage.getItem('Status');
            this.id = localStorage.getItem('Id');
            if (this.status == 'Customer') {
                const response = await client.get('/customerAccounts/' + this.id);
                this.name = response.data.name;
                this.email = response.data.email;
                this.password = response.data.password;
            }

            else if (this.status == 'Instructor') {
                const response = await client.get('/instructorAccounts/' + this.id);
                this.name = response.data.name;
                this.email = response.data.email;
                this.password = response.data.password;
            }
            else if (this.status == 'Owner') {
                const response = await client.get('/ownerAccount');
                this.name = response.data.name;
                this.password = response.data.password;
            }
        }
        catch (e) {
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
                email: this.email,
                password: this.password,
            };
            if (this.checkInput()) {
                try {
                    if (this.status === 'Owner') {
                        await client.put("/ownerAccount", accountInformation);
                    }
                    else if (this.status === 'Instructor') {
                        await client.put("/instructorAccounts/" + this.id, accountInformation);
                    }
                    else if (this.status === 'Customer') {
                        await client.put("/customerAccounts/" + this.id, accountInformation);
                    }
                    this.swapButtons();
                }
                catch (e) {
                    console.log(e);
                }
            }
        },
        async clearAccountInformation() {
            if (this.status === 'Owner') {
                const response = await client.get("/ownerAccount");
                this.name = response.data.name;
                this.password = response.data.password;
            }
            else if (this.status === 'Instructor') {
                const response = await client.get("/instructorAccounts/" + this.id);
                this.name = response.data.name;
                this.email = response.data.email;
                this.password = response.data.password;
            }
            else if (this.status === 'Customer') {
                const response = await client.get("/customerAccounts/" + this.id);
                this.name = response.data.name;
                this.email = response.data.email;
                this.password = response.data.password;
            }
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
            if (!this.isValid()) {
                alert("Please correct the errors in the form.");
                return false;
            }
            return true;
        },
        isEmpty() {
            if (this.status === 'Owner') {
                return this.isNameEmpty || this.isPasswordEmpty;
            }
            else {
                return this.isNameEmpty || this.isEmailEmpty || this.isPasswordEmpty;
            }
        },
        isValid() {
            if (this.status === 'Owner') {
                return this.isNameValid && this.isPasswordValid;
            }
            else {
                return this.isNameValid && this.isEmailValid && this.isPasswordValid;
            }
        },
        updatePassword(event) {
            this.password = event.target.value;
        },
    },
    computed: {
        isNameEmpty() {
            return this.name === null || this.name === "";
        },
        isEmailEmpty() {
            return this.email === null || this.email === "";
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
        isEmailValid() {
            if (this.isEmailEmpty) {
                this.emailError = "Email address is required.";
                return false;
            }
            this.emailError = "";
            return true;
        },
        isPasswordValid() {
            if (this.isPasswordEmpty) {
                this.passwordError = "Password is required.";
                return false;
            }
            this.passwordError = "";
            return true;
        },
        isOwner() {
            return this.status === 'Owner';
        },
        formatPassword() {
            if (this.disabled) {
                return this.password ? this.password.replace(/./g, '*') : '';
            }
            return this.password;
        }
    }
}
</script>

<style>
#account-info-component {
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
    grid-template-areas: "name name-input" ". name-error" "email email-input" ". email-error" "password password-input" ". password-error";
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