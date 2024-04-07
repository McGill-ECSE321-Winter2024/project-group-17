<template>
    <div class="background" style="position: relative; width: 100%; height: 30vh; overflow: hidden;">
        <img src="../assets/my-account-bg.jpg"
            style="position: absolute; left: 0; width: 100%; height: 100%; object-fit: cover;">
        <div div class="text-overlay"
            style="position: absolute; top: 50%; left: 50%; transform: translate(-50%, -50%);">MY ACCOUNT</div>
    </div>
</template>

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