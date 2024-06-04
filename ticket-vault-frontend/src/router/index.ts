import { createRouter, createWebHistory } from 'vue-router'
import TicketSearchView from '@/views/TicketSearchView.vue'

const router = createRouter({
  history: createWebHistory(import.meta.env.BASE_URL),
  routes: [
    {
      path: '/',

      component: TicketSearchView,
      children: [
        {
          path: '',
          name: 'tickets',
          component: () => import('@/views/TicketView.vue')
        },
        { 
          path: ':id', 
          name: 'ticket',
          component: () => import('@/views/TicketView.vue'),
          props: true
        },
      ]

      // route level code-splitting
      // this generates a separate chunk (home.[hash].js) for this route
      // which is lazy-loaded when the route is visited.
      // component: () => import('../views/TicketView.vue')
    },
    {
      path: '/import',
      name: 'import',
      component: () => import('../views/ImportView.vue')
    }
  ]
})

export default router
