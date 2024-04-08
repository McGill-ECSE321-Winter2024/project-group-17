<template>
    <div id="customer-account-component">
        <div id="customer-account-body">
            <AccountNavigation />
            <router-view id="body"></router-view>
        </div>
    </div>
</template>

<script>
import AccountNavigation from "./AccountNavigation.vue";

export default {
    components: {
        AccountNavigation
    }
}
</script>

<style>
#body {
    grid-area: body;
    padding-left: 5%;
}

#customer-account-body {
    padding-top: 3%;
    display: grid;
    grid-template-columns: 25% 75%;
    grid-template-areas: "nav body";
}
</style>