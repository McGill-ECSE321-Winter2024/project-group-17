import Vue from 'vue'
import Router from 'vue-router'
import Hello from '@/components/Hello'
import Courses from '@/components/Courses'
import CreateCourse from '@/components/CreateCourse'
import AccountView from '@/components/AccountView'
import ViewCustomerRegistrations from '@/components/ViewCustomerRegistrations'
import BillingInformation from '@/components/BillingInformation'
import Register from '@/components/Register'
import Home from '@/components/Home'

Vue.use(Router)

export default new Router({
  routes: [
    {
      path: '/',
      name: 'Hello',
      component: Hello
    },
    {
      path: '/home',
      name: 'Home',
      component: Home
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
        }
      ]
    }
  ]
})
