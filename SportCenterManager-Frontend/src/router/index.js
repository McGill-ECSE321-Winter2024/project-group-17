import Vue from 'vue'
import Router from 'vue-router'
import Hello from '@/components/Hello'
import BillingInformation from '@/components/BillingInformation'

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
    }
  ]
})
