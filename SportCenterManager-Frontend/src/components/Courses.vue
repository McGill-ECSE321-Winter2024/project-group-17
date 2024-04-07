<template>
   <div>
    <div class="background" style="position: relative; width: 100%; height: 50vh; overflow: hidden;">
      <img src="../assets/courses-bg.jpg" style="position: absolute; left: 0; width: 100%; height: 100%; object-fit: cover;">
      <div div class="text-overlay" style="position: absolute; top: 50%; left: 50%; transform: translate(-50%, -50%);">COURSES</div>
    </div>
    <div class="grid-container">
      <div v-for="course in courses" class="grid-item">
        <h3>{{ course.name }}</h3>
        <h4>${{ course.costPerSession }}</h4>
        <p>{{ course.description }}</p>
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
  data() {
    return {
      courses: []
    };
  },
  async created() {
    try {
      const response = await client.get('/courses');
      this.courses = response.data.courses;
    }
    catch (e) {
      console.log(e);
    }
  }
};
</script>

<style scoped>
.background {
  position: relative;
  display: inline-block;
}

.text-overlay {
  font-size: 96px;
  color: white;
  font-weight: bold;
}

h3 {
  font-weight: bold;
}
p {
  font-size: 18px;
  text-align: center;
  color: black;
}
.grid-container {
  display: grid;
  grid-template-columns: repeat(3, 1fr); /* 3 columns with equal width */
  grid-gap: 10px; /* Gap between grid items */
}

.grid-item {
  background-color: #ffffff;
  padding: 20px;
  text-align: center;
}
</style>
