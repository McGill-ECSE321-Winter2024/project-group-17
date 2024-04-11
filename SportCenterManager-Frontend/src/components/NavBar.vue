<template>
    <div id="navBar">
        <nav class="nav">
            <div class="navbar-left">
                <a class="navbar-home" href='#/home'> Home </a>
            </div>
            <div class = "navbar-right"> 
                <a class = "navbar-courses" href="#/courses"> Courses |</a>
                <template v-if = 'status !== null'>
                    <a v-if = 'status === "Customer"' class = "navbar-account" href = '#/myAccount'>My Account |</a>
                    <a v-else-if = "status === 'Owner'" class = "navbar-account" href= '#/myAccount'>My Account |</a>
                    <a v-else-if = "status === 'Instructor'" class = "navbar-account" href = '#/myAccount'>My Account |</a>
                    <a class = "navbar-account" href = '#/authen' @click="logout">Logout</a>
                </template>
                <template v-else>
                    <a class = "navbar-account" href = '#/authen'>Log In/ Sign Up</a>
                </template>
            </div>
        </nav>
    </div>
</template> 
<script>
    import axios from 'axios';
    import config from '../../config';

    const frontendUrl = 'http://' + config.dev.host + ':' + config.dev.port;
    const backendUrl = 'http://' + config.dev.backendHost + ':' + config.dev.backendPort;

    const client = axios.create({
    baseURL: backendUrl,
    headers: { 'Access-Control-Allow-Origin': frontendUrl }
});
    export default{
        name:"status",
        data(){
            return{
                status: null,
            };
        },
        async created(){
            this.status = localStorage.getItem("Status");
            this.$root.$on('logged-in', () => {
                this.status = localStorage.getItem("Status");
            });
        },
        methods: {
            async logout() {
                try {
                    const response = await client.post('/logout');
                    localStorage.clear();
                    this.status = null;
                }
                catch (error) {
                    console.log(error.response.data.message); 
                }
            }
        }
    }
    
</script>
<style>
.navbar-courses{
    margin-right:2px;
}
.nav{
    height:40px;
    background-color: black;
    color:white;
    display: flex;
    justify-content:space-between;
    align-items: center;
    padding-left: 1%;
    padding-right: 1%;
}

a:link{
    color:white;
}
a:active{
    color:white;
}
a:visited{
    color:white;
}
a:hover{
    color:white;
}
</style>