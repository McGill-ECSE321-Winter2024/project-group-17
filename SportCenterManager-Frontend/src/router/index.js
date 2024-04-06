import Vue from 'vue'
import Router from 'vue-router'
import Hello from '@/components/Hello'
import ViewCustomerRegistrations from "@/components/ViewCustomerRegistrations"

Vue.use(Router)

export default new Router({
  routes: [
    {
      path: '/',
      name: 'Hello',
      component: Hello
    },
    {
      path: "/customerAccount/registrations",
      name: "UserRegistrations",
      component: ViewCustomerRegistrations
    }
  ]
})
