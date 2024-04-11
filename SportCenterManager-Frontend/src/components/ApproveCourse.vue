<template>
    <div>
        <p style="font-weight: bold; font-size: 25px; text-align: left;">REQUESTED COURSES</p>
        <div v-if="courses.length === 0">
            <p>No requested courses available.</p>
        </div>
        <div v-else>
            <div v-for="course in courses" :key="course.id" class="course-box">
                <h3>{{ course.name }}</h3>
                <p>{{ course.description }}</p>
                <p>${{ course.costPerSession }}/session</p>
                <button @click="approveCourse(course.id)">Approve</button>
                <button @click="rejectCourse(course.id)">Reject</button>
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
    headers: { 'Access-Control-Allow-Origin': frontendUrl}
});
export default {
  name: 'ApproveCourse',
  data() {
    return {
      courses: []
    };
  },
  async created(){
    try {
      const response = await client.get('/courses/nonApproved');
      this.courses = response.data.courses;
    }
    catch (e){
      alert(e.response.data.message);
    }
  },
  methods: {
    async approveCourse(course_id){
      try {
        await client.put(`/courses/${course_id}/approve`);
        this.courses = this.courses.filter(course => course.id !== course_id);
      }
      catch (e){
        alert(e.response.data.message);
      }
    },
    async rejectCourse(courseId){
      try {
        await client.delete('/courses/' + courseId);
        this.courses = this.courses.filter(course => course.id !== courseId);
      }
      catch (e) {
        alert(e.response.data.message);
      }
    }
  }
}
</script>

<style>
.course-box {
  border: 1px solid #ccc;
  padding: 10px;
  margin-bottom: 10px;
}
</style>