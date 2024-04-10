<template>
    <div id ="navBar">
        <nav class = "nav">
            <div class = "navbar-left">
                <a class = "navbar-home" href = '#/home'> Home </a>
            </div>
            <div class = "navbar-right">
                <a class = "navbar-courses" href="#/courses"> Courses |</a>
                <a v-if = 'status === "Customer"' class = "navbar-account" href = '#/customerAccount'>My Account </a>
                <a v-else-if = "status === 'Owner'" class = "navbar-account" href= '#/ownerAccount'>My Account</a>
                <a v-else-if = "status === 'Instructor'" class = "navbar-account" href = '#/instructorAccount'>My Account</a>
                <a v-else class = "navbar-account" href = '#/authen'>Log In/ Sign Up</a>
            </div>
        </nav>
    </div>
</template> 
<script>
    import axios from "axios";
    export default{
        name:"status",
        data(){
            return{
                status: null
            };

        },
        async created(){
            this.status = localStorage.getItem("Status")
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