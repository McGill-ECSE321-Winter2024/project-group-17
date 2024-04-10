import Vue from 'vue'
import Router from 'vue-router'
import Hello from '@/components/Hello'
import Courses from '@/components/Courses'
import CreateCourse from '@/components/CreateCourse'
import ModifyCourse from '@/components/ModifyCourse'
import AccountView from '@/components/AccountView'
import ViewCustomerRegistrations from '@/components/ViewCustomerRegistrations'
import BillingInformation from '@/components/BillingInformation'
import Register from '@/components/Register'
import ApproveCourse from '@/components/ApproveCourse'

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
    }
  ]
})
