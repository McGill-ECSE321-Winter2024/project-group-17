<template>
    <div class="homepage-component">
        <div id="banner">
            <div id="left">
                <h1> Welcome. </h1>
                <div id="buttons">
                    <a class="home_button" href="#/courses"> Courses </a>
                    <a class="home_button" href="#/authen"> Register </a>
                </div>
            </div>
        </div>
        <div id="content">
            <div id="first_block">
                <div id="content_left">
                    <h2>How to Register</h2>

                    <p> 1. Make an account. </p>
                    <p> 2. Fill in your billing information. </p>
                    <p> 3. Browse our offered courses. </p>
                    <p> 4. Choose a session that fits your schedule. </p>
                    <p> 5. Register!</p>

                </div>
            </div>

            <div id="second_block">
                <div id="content_right">
                    <h2> About the Center </h2>
                    <p> Accessible by metro, bus, car and walking, the centre is located in the bustling neighborhood of Milton Park. With passionate instructors, a diversity of courses available and sessions offered at all times of the day, there is something for everyone!  </p>
                    <h3> Opening Hours </h3>
                    <p> {{ openingHours }} to {{ closingHours }}</p>
                </div>
            </div>

        </div>
    </div>
</template>
<script>
import axios from "axios";
import config from "../../config";
const frontendUrl = 'http://' + config.dev.host + ':' + config.dev.port;
const backendUrl = 'http://' + config.dev.backendHost + ':' + config.dev.backendPort;

const client = axios.create({
    baseURL: backendUrl,
    headers: { 'Access-Control-Allow-Origin': frontendUrl }
});
export default {
    name: "Schedule",
    data() {
        return {
            openingHours: null,
            closingHours: null
        };
    },

    async created() {
        try {
            const response = await client.get('/schedule');
            this.openingHours = response.data.openingHours;
            this.closingHours = response.data.closingHours;
        }
        catch (e) {
            console.log(e);
        }
    }
}
</script>

<style>
a:hover {
    background-color: #ffffff;
    color: black;
    text-decoration: none;
}

#banner {
    color: white;
    background-image: url("../assets/basketball-933173_1280.jpg");
    background-color: black;
    background-position: center;
    background-repeat: no-repeat;
    background-size: cover;
    height: 110vh;
    display: flex;
    justify-content: flex-end;
    margin-top: -10vw;
    box-shadow: 0px 2px 5px -1px gray;
    overflow: hidden;
}

#left {
    text-align: center;
    position: absolute;
    top: 47%;
    left: 80%;
    transform: translate(-50%, -50%);
    display: flex;
    flex-direction: column;
    justify-content: center;
    width: 18%;
    min-width: 200px;
}
h1{
    font-size:  calc(24px + 4vw);
}

#buttons {
    display: flex;
    flex-direction: row;
    justify-content: center;
    width: 100%;
}

.home_button {
    padding: 2vw;
    padding-top: 4px;
    padding-bottom: 4px;
    margin: 5%;
    border-color: white;
    border-style: solid;
    font-size:(18px+2vw);
}

#content {
    margin-top: 5vw;
    margin-left: 10vw;
    margin-right: 10vw;
    padding-bottom: 5vw;
    display: flex;
    flex-direction: row-reverse;
    width: 90vw;
    column-gap: 8vw;
}

#first_block {
    display: flex;
    justify-content: flex-start;
    width: 100%;
    align-content: left;
}

#content_left {
    width: 100%;
    text-align: left;
}

#second_block {
    display: flex;
    justify-content: flex-end;
    width: 100%;
    text-align: left;
}

#content_right {
    width: 100%;
    text-justify:inter-word;
}
</style>