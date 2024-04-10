<template>
    <div class = "homepage-component">
        <div id = "banner">
            <div id = "left">
                <h1> Welcome. </h1>
                <div id = "buttons">
                    <a class = "home_button" href="#/courses"> Explore Courses </a>
                    <a class = "home_button" href="#/registration"> Register </a>
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
                        <p> 4. Choose a session that fits your scheudle. </p>
                        <p> 5. Register!</p>
                    
                </div>
            </div>

            <div id = "second_block">
                <div id ="content_right">
                    <h2> About the Center </h2>
                    <p>Lorem ipsum dolor sit amet, consectetur adipiscing elit, sed do eiusmod tempor incididunt ut labore et dolore magna aliqua. Ut enim ad minim veniam, quis nostrud exercitation ullamco laboris nisi ut aliquip ex ea commodo consequat. Duis aute irure dolor in reprehenderit in voluptate velit esse cillum dolore eu fugiat nulla pariatur. Excepteur sint occaecat cupidatat non proident, sunt in culpa qui officia deserunt mollit anim id est laborum. </p>
                    <h3> Opening Hours </h3>
                </div>
            </div>

        </div>
    </div>
</template> 
<script>
    
</script>
<style>
    a:hover{
        background-color: #ffffff;
        color:black;
        text-decoration: none;
    }
    
   
   
    #banner{
        color:white;
        background-image: url("../assets/basketball-933173_1280.jpg");
        background-color: black;
        background-position:center;
        background-repeat: no-repeat;
        background-size:cover;
        height:90vh; 
        display:flex;
        justify-content:flex-end;
        margin-top:-10vw;
    }
    #left{
        text-align: center;
        position:absolute;
        top: 47%;
        left:80%;
        transform: translate(-50%,-50%);
        display:flex;
        flex-direction:column;
        justify-content: center;
        width: 40vw;
    }
    #buttons{
        display:flex;
        flex-direction: row;
        justify-content:center;
        width:100%;
    }
    .home_button{
        padding:8px;
        padding-top:4px;
        margin: 2%;
        border-color: white;
        border-style: solid;
    }

    #content{
        margin-top:5vw;
        margin-left:10vw;
        margin-right:10vw;
        padding-bottom: 5vw;
        display: flex;
        flex-direction:column;
        row-gap: 5vw ;
    }
    #first_block{
        display:flex;
        justify-content: flex-start;
        width:100%;
        align-content:left;
    }
    #content_left{
        width: 45vw;
        text-align: left;
    }
    #second_block{
        display:flex;
        justify-content:flex-end;
        width:100%;
        text-align:right;
        margin-top: 5vw;
    }
    #content_right{
        width:45vw;
        text-align: right;
    }
</style>