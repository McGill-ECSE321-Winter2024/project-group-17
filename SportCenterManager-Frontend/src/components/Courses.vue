<template>
   <div>
     <div class="createbth-container">
      <button @click="openCreateModal" class="createbtn">Create Course</button>
    </div>
    <CreateCourseModal :is-open="isCreateModalOpen" @close="closeCreateModal" />
    <div class="search-bar-container">
      <input type="text" v-model="searchTerm" placeholder="Search by course name" class="search-bar" />
    </div>
    <div class="grid-container">
      <div v-for="course in filteredCourses" :key="course.id" class="grid-item">
        <h3>{{ course.name }}</h3>
        <h4>${{ course.costPerSession }}/session</h4>
        <p>{{ course.description }}</p>
        <div class="dropdown-container">
          <div class="dropdown">
            <button @click="toggleDropdown(course.id)" class="dropbtn">&#8942;</button>
            <div v-if="isOpen[course.id]" class="dropdown-content">
              <button @click="openModifyModal" class="dropdown-item">Modify</button>
              <ModifyCourseModal :is-open="isModifyModalOpen" :courseId="course.id" @close="closeModifyModal" />
              <button @click="confirmDeletion(course.id)" class="dropdown-item deletebtn">Delete</button>
            </div>
          </div>
        </div>
      </div>
    </div>
  </div>
</template>

<script>
import axios from "axios";
import config from "../../config";
import CreateCourseModal from './CreateCourseModal.vue';
import ModifyCourseModal from './ModifyCourseModal.vue';

const frontendUrl = 'http://' + config.dev.host + ':' + config.dev.port;
const backendUrl = 'http://' + config.dev.backendHost + ':' + config.dev.backendPort;

const client = axios.create({
    baseURL: backendUrl,
    headers: { 'Access-Control-Allow-Origin': frontendUrl}
});
export default {
  components: {
    CreateCourseModal,
    ModifyCourseModal
  },
  data() {
    return {
      courses: [],
      isOpen: {},
      isCreateModalOpen: false,
      isModifyModalOpen: false,
      searchTerm: ''
    };
  },
  async created() {
    try {
      const response = await client.get('/courses/approved');
      this.courses = response.data.courses;
    }
    catch (e) {
      alert(e.response.data.message);
    }
  },
  methods: {
    openCreateModal() {
      this.isCreateModalOpen = true;
    },
    closeCreateModal() {
      this.isCreateModalOpen = false;
    },
    openModifyModal() {
      this.isModifyModalOpen = true;
    },
    async closeModifyModal() {
      this.isModifyModalOpen = false;
      try {
        const response = await client.get('/courses/approved');
        this.courses = response.data.courses;
      }
      catch (e) {
        alert(e.response.data.message);
      }
    },
    toggleDropdown(courseId) {
      this.$set(this.isOpen, courseId, !this.isOpen[courseId]);
      if (this.isOpen[courseId]) {
        document.addEventListener('click', (event) => this.closeDropdown(event, courseId));
      }
    },
    closeDropdown(event, courseId) {
      const dropdown = document.querySelector(`#dropdown-${courseId}`);
      if (!dropdown.contains(event.target)) {
        this.isOpen[courseId] = false;
        document.removeEventListener('click', this.closeDropdown);
      }
    },
    async confirmDeletion(courseId){
      if (window.confirm('Are you sure you want to delete this course?')) {
        await this.performDeletion(courseId);
      } else {}
    },
    async performDeletion(courseId){
      try {
        await client.delete('/courses/' + courseId);
        this.courses = this.courses.filter(course => course.id !== courseId);
      }
      catch (e){
        alert(e.response.data.message);
      }
    }
  },
  computed: {
    filteredCourses() {
      return this.courses.filter(course =>
        course.name.toLowerCase().includes(this.searchTerm.toLowerCase())
      );
    }
  }
};

</script>

<style scoped>
h3 {
  font-weight: bold;
  color: white;
  background-color: rgb(73, 172, 225);
}
h4{
  font-style: italic;
  color: white;
}
p {
  font-size: 18px;
  text-align: center;
  color: white;
}
.grid-container {
  display: grid;
  grid-template-columns: repeat(3, 1fr); 
  grid-gap: 10px;
  margin-left: 40px; 
  margin-right: 40px;
}

.grid-item {
  background-color: rgb(63, 154, 202);
  padding: 20px;
  text-align: center;
  border-radius: 10px;
}

.dropbtn {
  background-color: rgb(51, 120, 156);
  color: white;
  padding: 1px 5px 5px 5px;
  border: none;
  cursor: pointer;
  font-size: 16px; 
  border-radius: 100px;
}

.dropdown-container{
  text-align: right;
}

.dropdown {
  position: relative;
  display: inline-block;
  text-align: center;
}

.dropdown-content {
  display: none;
  position: absolute;
  background-color: #f9f9f9;
  min-width: 160px;
  z-index: 1;
}

.dropdown-content a {
  color: black;
  padding: 12px 16px;
  text-decoration: none;
  display: block;
}

.dropdown .dropdown-content {
  display: block;
}

.deletebtn{
  background-color: #f9f9f9;
  border: none;
  color: rgb(255, 0, 0);
}

.createbtn{
  background-color: black;
  color: white;
  border-radius: 10px;
}

.createbth-container{
  text-align: right;
  margin-right: 20px;
}

.dropdown-item {
  display: block;
  padding: 10px;
  transition: background-color 0.3s; 
}

.dropdown-item:hover {
  background-color: #bebebe; 
}
.search-bar-container{
  text-align: left;
  margin-left: 50px;
}
.search-bar {
  margin-bottom: 10px;
  padding: 5px;
  width: 200px;
}
</style>
