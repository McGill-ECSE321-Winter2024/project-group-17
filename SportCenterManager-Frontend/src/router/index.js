import Vue from 'vue'
import Router from 'vue-router'
import Hello from '@/components/Hello'
import Courses from '@/components/Courses'
import CreateCourse from '@/components/CreateCourse'
import ModifyCourse from '@/components/ModifyCourse'
import AccountView from '@/components/AccountView'
import BillingInformation from '@/components/BillingInformation'
import Register from '@/components/Register'
import ViewLogin from '@/components/ViewLogin.vue'
import RegistrationConfirmation from '@/components/RegisterConfirmation'
import ViewCustomerRegistrations from '@/components/ViewCustomerRegistrations'

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
      path: '/courses/modify/:courseId',
      name: 'ModifyCourse',
      component: ModifyCourse
    },
    {
      path: '/courses/sessions/register',
      name: 'Register',
      component: Register
    },
    {
      path: '/courses/sessions/register/confirmation',
      name: 'RegistrationConfirmation',
      component: RegistrationConfirmation,
      beforeEnter: (to, from, next) => {
        if (localStorage.registerAuthenticated) {
          localStorage.registerAuthenticated = undefined;
          next();
        } else {
          next({path: "/home"});
        }
      }
    },
    {
      path: '/customerAccount',
      name: 'AccountView',
      component: AccountView,
      children: [
        {
          path: 'billing',
          component: BillingInformation
        },
        {
          path: 'registrations',
          component: ViewCustomerRegistrations
        }
      ]
    }, 
    {
      path: '/login',
      name: 'Login',
      component: ViewLogin
    },

  ]
})
