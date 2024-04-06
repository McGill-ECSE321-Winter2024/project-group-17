import Vue from 'vue'
import Router from 'vue-router'
import Hello from '@/components/Hello'
import BillingInformation from '@/components/BillingInformation'
import Courses from '@/components/Courses'
import CreateCourse from '@/components/CreateCourse'

Vue.use(Router)

export default new Router({
  routes: [
    {
      path: '/',
      name: 'Hello',
      component: Hello
    },
    {
      path: '/billinginformation',
      name: 'BillingInformation',
      component: BillingInformation
    },{
      path: '/courses',
      name: 'Courses',
      component: Courses
    },
    {
      path: '/courses/create',
      name: 'CreateCourse',
      component: CreateCourse
    }
  ]
})
