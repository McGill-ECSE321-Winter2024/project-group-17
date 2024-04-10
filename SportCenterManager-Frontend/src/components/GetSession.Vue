<template>
  <div>
    <h1>Find Sessions for Course</h1>
    <div class="input-container">
      <input type="text" class="input-style" placeholder="Course Id" v-model="course" />
      <button class="find-btn" @click="created()" :disabled="isFindButtonDisabled">Find Sessions</button>
    </div>
    <h2>Sessions</h2>
    <table v-if="sessions.length > 0">
      <thead>
        <tr>
          <th>Start Time</th>
          <th>End Time</th>
          <th>Date</th>
        </tr>
      </thead>
      <tbody>
        <tr v-for="session in sessions" :key="session.id">
          <td>{{ session.startTime }}</td>
          <td>{{ session.endTime }}</td>
          <td>{{ session.date }}</td>
        </tr>
      </tbody>
    </table>
    <div v-else>
      No sessions found.
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
  name: 'FindSession',
  data() {
    return {
      sessions: [],
      course: null
    };
  },
  methods: {
    async created() {
      try {
        const response = await client.get(`/courses/${this.course}/sessions`);
        this.sessions = response.data.sessions;
      } catch (e) {
        console.error("Failed to find sessions:", e);
        alert("Failed to find sessions.");
      }
    },
    isFindButtonDisabled() {
      return !this.course;
    }
  }
};
</script>

<style>
h1 {
    position: relative;
}
.find-btn {
    border: none;
    color: white;
    background-color: black;
    padding: 10px 20px;
    border-radius: 5px;
}
.clear-btn {
    color: black;
    background-color: white;
    padding: 10px 20px;
    border-radius: 5px;
}
.input-container {
  display: flex;
  flex-direction: column;
  align-items: center;
  justify-content: center;
  height: 50vh;
}
td,
th {
    padding: 0.5em;
    border: 1px solid black;
}
.input-style {
  margin-bottom: 10px;
  padding: 10px;
  width: 200px;
  border: 1px solid #ccc;
  border-radius: 5px;
}
</style>
