<template>
    <div class="modal" v-if="isOpen">
      <div class="modal-content">
        <!-- Add the CreateCourse component inside the modal -->
        <CreateCourse @close="closeModal" />
      </div>
    </div>
</template>
  
<script>
  import CreateCourse from './CreateCourse.vue';
  
  export default {
    components: {
      CreateCourse
    },
    props: {
      isOpen: {
        type: Boolean,
        default: false
      }
    },
    methods: {
      closeModal() {
        // Emit an event to notify the parent component to close the modal
        this.$emit('close');
      }
    }
  };
</script>
  
<style scoped>
.modal {
  position: fixed;
  top: 0;
  left: 0;
  width: 100%;
  height: 100%;
  background-color: rgba(0, 0, 0, 0.5); /* Semi-transparent black background */
  display: flex;
  justify-content: center;
  align-items: center;
}

.modal-content {
  background-color: white;
  padding: 20px;
  border-radius: 10px;
  box-shadow: 0 0 10px rgba(0, 0, 0, 0.3); /* Drop shadow effect */
}
</style>
  