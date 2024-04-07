<template>
    <div id ="navBar">
        <nav class = "nav">
            <div class = "navbar-left">
                <a class = "navbar-home" href = '#/home'> Home </a>
            </div>
            <div class = "navbar-right">
                <a class = "navbar-courses" href="#/courses"> Courses </a>
                <a class = "navbar-account" href = '#'> Account </a>
            </div>
        </nav>
    </div>
</template> 

<style>
.nav{
    height:40px;
    background-color: black;
    color:white;
    display: flex;
    justify-content:space-between;
    align-items: center;
    padding-left: 2%;
    padding-right: 2%;
}
.navbar-courses{
    margin-right:10px;
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