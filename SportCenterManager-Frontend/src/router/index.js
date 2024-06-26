import Vue from 'vue'
import Router from 'vue-router'
import Courses from '@/components/Courses'
import CreateSession from '@/components/CreateSession'
import ModifySession from '@/components/ModifySession'
import Sessions from '@/components/Sessions'
import AccountView from '@/components/AccountView'
import BillingInformation from '@/components/BillingInformation'
import Register from '@/components/Register'
import Home from '@/components/Home'
import ApproveCourse from '@/components/ApproveCourse'
import ViewLogin from '@/components/ViewLogin.vue'
import RegistrationConfirmation from '@/components/RegisterConfirmation'
import ViewCustomerRegistrations from '@/components/ViewCustomerRegistrations'
import AccountInformation from '@/components/AccountInformation'
import ManageAccounts from '@/components/manageAccounts'
import SuperviseSession from '@/components/SuperviseSession.vue'
import ModifySchedule from '@/components/ModifySchedule'

Vue.use(Router)

export default new Router({
  routes: [
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
      path: '/courses/sessions/create/:courseId',
      name: 'CreateSession',
      component: CreateSession
    },
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
      path: '/courses/sessions/register/:coursedId/:sessionId',
      name: 'Register',
      component: Register
    },
    {
      path: '/courses/sessions/register/:courseId/:sessionId/confirmation',
      name: 'RegistrationConfirmation',
      component: RegistrationConfirmation,
      beforeEnter: (to, from, next) => {
        if (localStorage.getItem("registerAuthenticated") != null) {
          localStorage.setItem("registerAuthenticated", undefined);
          next();
        } else {
          next({path: "/home"});
        }
      }
    },
    {
      path: '/myAccount',
      name: 'AccountView',
      component: AccountView,
      children: [
        {
        path:'/',
        component: AccountInformation
        },
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
        },
        {
          path:'modifySchedule',
          component: ModifySchedule
        },
        {
          path: 'manageAccounts',
          component: ManageAccounts
        }
      ]
    },
    {
      path: '/authen',
      name: 'Login',
      component: ViewLogin
    },
    {
      path: '/courses/sessions/supervise/:courseId/:sessionId',
      name: 'SuperviseSession',
      component: SuperviseSession
    }
  ]
})
