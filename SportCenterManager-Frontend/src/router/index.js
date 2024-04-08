import Vue from 'vue'
import Router from 'vue-router'
import Hello from '@/components/Hello'
import Courses from '@/components/Courses'
import CreateCourse from '@/components/CreateCourse'
import CreateSession from '@/components/CreateSession'
import ModifySession from '@/components/ModifySession'
import DeleteSession from '@/components/DeleteSession'
import GetSession from '@/components/GetSession'

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
    }
  ]
})
