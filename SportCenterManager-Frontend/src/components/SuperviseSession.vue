<template>
    <div id="supervise-body" style="padding-top: 3%; padding-left: 5%;">
            <div id="supervise-text">
                <p class="supervise-course-desc-text">Course Description: <br> {{ this.session.course.description }}</p>
                <p class="supervise-course-desc-text" style="margin-top: 1%;">Cost: {{ this.session.course.costPerSession }}$/Session</p>
                
                <p class="supervise-session-desc-text" style="margin-top: 3%;">You are about to supervise to a session for the <b> {{ this.session.course.name }} </b> course</p> 
                <p class="supervise-session-desc-text"> <b>Date/Time:</b> {{ this.session.date }} -- {{ this.session.startTime }} to {{ this.session.endTime }}</p> 
            </div>
            <div id="supervise-buttons">
                <button type="button" class="supervise-btns" @click="$router.go(-1)">BACK</button>
                <button type="button" class="supervise-btns" v-bind:disabled="!confirmEnabled" @click="supervise()">CONFIRM</button>
            </div>
        </div>
</template>

<script>
</script>

<style>
.supervise-course-desc-text{
    font-size: 17px;
    text-align: left;
}

.supervise-session-desc-text{
    font-size: 17px;
    text-align: left;
    margin: 0%;
}

#supervise-buttons {
  display: flex;
  justify-content: space-between;
  flex-direction: row;
  align-items: top center;
  padding-top: 8%;
  margin-left: 22%;
  width: 50%;
  height: 100%;
}

.supervise-btns {
    background-color: white;
    color: #000000;
    width: 40%;
    height: 10%;
}
</style>