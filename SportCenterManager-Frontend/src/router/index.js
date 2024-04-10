import Vue from 'vue'
import Router from 'vue-router'
import Hello from '@/components/Hello'
import Courses from '@/components/Courses'
import CreateCourse from '@/components/CreateCourse'
import CreateSession from '@/components/CreateSession'
import ModifySession from '@/components/ModifySession'
import DeleteSession from '@/components/DeleteSession'
import GetSession from '@/components/GetSession'
import ModifyCourse from '@/components/ModifyCourse'
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
      path: '/session',
      name: 'CreateSession',
      component: CreateSession
    }
    ,
    {
     path: '/session/modify',
     name: 'ModifySession',
     component: ModifySession
     },
     {
      path: '/session/delete',
      name: 'DeleteSession',
      component: DeleteSession
     },
     {
     path: '/session/find',
     name: 'FindSession',
     component: GetSession
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
        {
          path: 'registrations',
          component: ViewCustomerRegistrations
        },
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
