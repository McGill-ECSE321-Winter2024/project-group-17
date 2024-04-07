<template>
   <div>
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
