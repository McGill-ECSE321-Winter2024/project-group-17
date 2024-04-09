import Vue from 'vue'
import Router from 'vue-router'
import Hello from '@/components/Hello'
import Courses from '@/components/Courses'
import CreateCourse from '@/components/CreateCourse'
import AccountView from '@/components/AccountView'
import ViewCustomerRegistrations from '@/components/ViewCustomerRegistrations'
import BillingInformation from '@/components/BillingInformation'
import Register from '@/components/Register'
import RegistrationConfirmation from '@/components/RegisterConfirmation'

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
      path: '/courses/sessions/register',
      name: 'Register',
      component: Register,
      props: true
    },
    {
      path: '/courses/sessions/register/confirmation',
      name: 'RegistrationConfirmation',
      component: RegistrationConfirmation,
      props: true
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
