<template>
    <div id="customer-account-navigation-bar">
        <router-link to="/myAccount">
            <button class="acc-nav-btn state-btn" v-bind:disabled="infoState" style="margin-bottom: 2%;" type="button"
                @click="toggleInfo">Account Information</button>
        </router-link>

        <router-link v-if="isCustomer" to="/myAccount/billing">
            <button class="acc-nav-btn state-btn" v-bind:disabled="billingState" style="margin-bottom: 2%;"
                type="button" @click="toggleBilling">Payment Information</button>
        </router-link>

        <router-link to="/myAccount/registrations">
            <button class="acc-nav-btn state-btn" v-bind:disabled="registrationState"
                style="margin-bottom: 2%; text-align: left;" type="button"
                @click="toggleRegistration">{{ registrationLabel }}</button>
        </router-link>

        <router-link v-if="isOwner" to="/myAccount/approve">
            <button class="acc-nav-btn state-btn" v-bind:disabled="approveState"
                style="margin-bottom: 2%; text-align: left;" type="button" @click="toggleRequestedCourses">Requested
                Courses</button>
        </router-link>

        <router-link v-if="isOwner" to="/myAccount/modifySchedule">
            <button class="acc-nav-btn state-btn" v-bind:disabled="modifyScheduleState"
                style="margin-bottom: 2%; text-align: left;" type="button" @click="toggleModifySchedule">Modify
                Schedule</button>
        </router-link>

        <router-link v-if="isOwner" to="/myAccount/manageAccounts">
            <button class="acc-nav-btn state-btn" v-bind:disabled="manageAccounts"
                style="margin-bottom: 2%; text-align: left;" type="button" @click="toggleManageAccounts">Manage
                Accounts</button>
        </router-link>
    </div>
</template>

<script>
export default {
    data() {
        // Update state based on route
        if (this.$route.path === '/myAccount' || this.$route.path === '/myAccount/') {
            return {
                infoState: true,
                billingState: false,
                registrationState: false,
                approveState: false,
                manageAccounts: false,
                modifyScheduleState: false
            }
        } else if (this.$route.path === '/myAccount/billing' || this.$route.path === '/myAccount/billing/') {
            return {
                infoState: false,
                billingState: true,
                registrationState: false,
                approveState: false,
                manageAccounts: false,
                modifyScheduleState: false
            }
        } else if (this.$route.path === '/myAccount/registrations' || this.$route.path === '/myAccount/registrations/') {
            return {
                infoState: false,
                billingState: false,
                registrationState: true,
                approveState: false,
                manageAccounts: false,
                modifyScheduleState: false
            }
        } else if (this.$route.path === '/myAccount/approve' || this.$route.path === '/myAccount/approve/') {
            return {
                infoState: false,
                billingState: false,
                registrationState: false,
                approveState: true,
                modifyScheduleState: false,
                manageAccounts: false
            }
        }
        else if (this.$route.path === '/myAccount/modifySchedule' || this.$route.path === '/myAccount/modifySchedule/') {
            return {
                infoState: false,
                billingState: false,
                registrationState: false,
                approveState: false,
                modifyScheduleState: true,
                manageAccounts: false
            }
        }
        else if (this.$route.path === '/myAccount/manageAccounts' || this.$route.path === '/myAccount/manageAccounts') {
            return {
                infoState: false,
                billingState: false,
                registrationState: false,
                approveState: false,
                modifyScheduleState: false,
                manageAccounts: true
            }
        }
    },
    methods: {
        toggleInfo() {
            this.infoState = true;
            this.billingState = false;
            this.registrationState = false;
            this.approveState = false;
            this.modifyScheduleState = false;
            this.manageAccounts = false;
        },
        toggleBilling() {
            this.infoState = false;
            this.billingState = true;
            this.registrationState = false;
            this.approveState = false;
            this.modifyScheduleState = false;
            this.manageAccounts = false;
        },
        toggleRegistration() {
            this.infoState = false;
            this.billingState = false;
            this.registrationState = true;
            this.approveState = false;
            this.modifyScheduleState = false;
            this.manageAccounts = false;
        },
        toggleRequestedCourses() {
            this.infoState = false;
            this.billingState = false;
            this.registrationState = false;
            this.approveState = true;
            this.modifyScheduleState = false;
            this.manageAccounts = false;
        },
        toggleModifySchedule() {
            this.infoState = false;
            this.billingState = false;
            this.registrationState = false;
            this.approveState = false;
            this.modifyScheduleState = true;
            this.manageAccounts = false;
        },
        toggleManageAccounts() {
            this.infoState = false;
            this.billingState = false;
            this.registrationState = false;
            this.approveState = false;
            this.modifyScheduleState = false;
            this.manageAccounts = true
        }
    },
    computed: {
        isCustomer() {
            return localStorage.getItem('Status') === 'Customer';
        },
        isOwner() {
            return localStorage.getItem('Status') === 'Owner';
        },
        isInstructor(){
            return localStorage.getItem('Status') === 'Instructor';
        },
        registrationLabel(){
            if (this.isCustomer) {
                return "Registrations";
            } else if (this.isInstructor){
                return "Supervised Sessions";
            } else if (this.isOwner){
                return "Manage Registrations";
            }
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