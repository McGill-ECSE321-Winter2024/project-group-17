<template>
    <div class="background" style="position: relative; width: 100%; height: 30vh; overflow: hidden;">
        <img :src="pageAsset" style="position: absolute; left: 0; width: 100%; height: 100%; object-fit: cover;">
        <div div class="text-overlay"
            style="position: absolute; top: 50%; left: 50%; transform: translate(-50%, -50%);">{{ pageName }}</div>
    </div>

</template>

<script>
export default {
    computed: {
        pageName() {
            if (this.$route.path === '/') {
                return "LOGIN";
            }
            if (this.$route.path === '/home') {
                return "HOME";
            }
            if (this.$route.path.includes('/customerAccount')) {
                return "MY ACCOUNT";
            }
            if (this.$route.path.includes('/register')) {
                return "REGISTRATION";
            }
            if (this.$route.path.includes('/courses')) {
                return "COURSES";
            }
        },
        pageAsset() {
            if (this.$route.path === '/') {
                return require("@/assets/login-bg.png")
            }
            if (this.$route.path === '/home') {
                return require("@/assets/placeholder.png");
            }
            if (this.$route.path.includes('/customerAccount')) {
                return require("@/assets/my-account-bg.jpg");
            }
            if (this.$route.path.includes('/register')) {
                return require("@/assets/registration-bg.png");
            }
            if (this.$route.path.includes('/courses')) {
                return require("@/assets/courses-bg.jpg");
            }
        }
    }
}
</script>

<style>
.background {
    position: relative;
    display: inline-block;
}

.text-overlay {
    font-size: 96px;
    color: white;
    font-weight: bold;
}
</style>