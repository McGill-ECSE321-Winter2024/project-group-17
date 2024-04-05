<template>
   <div>
    <p>COURSES</p>
    <div class="grid-container">
      <div v-for="course in courses" :key="course.id" class="grid-item">{{ course.name }}</div>
    </div>
  </div>
</template>

<script>
export default {
  data() {
    return {
      courses: [
        { id: 1, name: 'Item 1' },
        { id: 2, name: 'Item 2' },
        { id: 3, name: 'Item 3' },
        { id: 4, name: 'Item 4' },
        { id: 5, name: 'Item 5' },
        { id: 6, name: 'Item 6' }
      ]
    };
  }
};
</script>

<style scoped>
p {
  font-weight: bold;
  font-size: 18px;
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
