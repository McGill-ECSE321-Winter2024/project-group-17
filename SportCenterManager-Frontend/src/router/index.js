import Vue from 'vue'
import Router from 'vue-router'
import Hello from '@/components/Hello'
import Courses from '@/components/Courses'
import CreateCourse from '@/components/CreateCourse'
import ViewCustomerRegistrations from "@/components/ViewCustomerRegistrations"
import ViewLogin from '@/components/ViewLogin.vue';
import ViewCreateAccount from '@/components/ViewCreateAccount.vue';

Vue.use(Router)

export default new Router({
  routes: [
    {
      path: '/',
      name: 'Hello',
      component: Hello
    },
    {
      path: '/courses',
      name: 'Courses',
      component: Courses
    },
    {
      path: '/courses/create',
      name: 'CreateCourse',
      component: CreateCourse
    },
    {
      path: "/customerAccount/registrations",
      name: "UserRegistrations",
      component: ViewCustomerRegistrations
    },
    {
      path: '/login',
      name: 'Login',
      component: ViewLogin
    },
    {
      path: '/login/customerAccount',
      name: 'CreateAccount',
      component: ViewCreateAccount
    }

  ]
})
