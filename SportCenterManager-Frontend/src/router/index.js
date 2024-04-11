import Vue from 'vue'
import Router from 'vue-router'
import Hello from '@/components/Hello'
import Courses from '@/components/Courses'
import CreateSession from '@/components/CreateSession'
import ModifySession from '@/components/ModifySession'
import Sessions from '@/components/Sessions'
import AccountView from '@/components/AccountView'
import BillingInformation from '@/components/BillingInformation'
import Register from '@/components/Register'
import ApproveCourse from '@/components/ApproveCourse'
import ViewLogin from '@/components/ViewLogin.vue'

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
      path: '/courses/sessions/create/:courseId',
      name: 'CreateSession',
      component: CreateSession
    }
    ,
    {
     path: '/courses/sessions/modify/:courseId/:sessionId/:instructorId',
     name: 'ModifySession',
     component: ModifySession
     },
     {
     path: '/courses/sessions/:courseId',
     name: 'Sessions',
     component: Sessions
    },
    {
      path: '/courses/sessions/register',
      name: 'Register',
      component: Register
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
        /*{
          path: 'registrations',
          component: ViewCustomerRegistrations
        },*/
        {
          path: 'approve',
          component: ApproveCourse
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
