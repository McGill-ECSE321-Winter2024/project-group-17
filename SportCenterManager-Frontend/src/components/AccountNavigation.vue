<template>
    <div id="customer-account-navigation-bar">
        <router-link to="/customerAccount">
            <button class="acc-nav-btn state-btn" v-bind:disabled="infoState" style="margin-bottom: 2%;" type="button"
                @click="toggleInfo">Account Information</button>
        </router-link>

        <router-link to="/customerAccount/billing">
            <button class="acc-nav-btn state-btn" v-bind:disabled="billingState" style="margin-bottom: 2%;"
                type="button" @click="toggleBilling">Payment Information</button>
        </router-link>

        <router-link to="/customerAccount/registrations">
            <button class="acc-nav-btn state-btn" v-bind:disabled="registrationState"
                style="margin-bottom: 2%; text-align: left;" type="button"
                @click="toggleRegistration">Registrations</button>
        </router-link>
    </div>
</template>

<script>
export default {
    data() {
        // Update state based on route
        if (this.$route.path === '/customerAccount' || this.$route.path === '/customerAccount/') {
            return {
                infoState: true,
                billingState: false,
                registrationState: false
            }
        } else if (this.$route.path === '/customerAccount/billing' || this.$route.path === '/customerAccount/billing/') {
            return {
                infoState: false,
                billingState: true,
                registrationState: false
            }
        } else if (this.$route.path === '/customerAccount/registrations' || this.$route.path === '/customerAccount/registrations/') {
            return {
                infoState: false,
                billingState: false,
                registrationState: true
            }
        }
    },
    methods: {
        toggleInfo() {
            this.infoState = true;
            this.billingState = false;
            this.registrationState = false;
        },
        toggleBilling() {
            this.infoState = false;
            this.billingState = true;
            this.registrationState = false;
        },
        toggleRegistration() {
            this.infoState = false;
            this.billingState = false;
            this.registrationState = true;
        }
    }
}
</script>

<style>
.state-btn:enabled {
    background-color: white;
    color: #000000;
}

.state-btn:disabled {
    background-color: #000000;
    color: white;
}

#customer-account-navigation-bar {
    display: flex;
    flex-direction: column;
    justify-content: left;
    align-items: left;
    text-align: left;
    width: 100%;
    height: 100%;
    padding-left: 3%;
    padding-top: 3%;
    grid-area: nav;
}

.acc-nav-btn {
    background-color: white;
    color: #000000;
    border-top: none;
    border-left: none;
    border-right: none;
    text-align: left;
    width: 100%;
}
</style>