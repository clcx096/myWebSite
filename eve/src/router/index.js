import Vue from 'vue'
import VueRouter from 'vue-router'

Vue.use(VueRouter)

const routes = [
  {
    path: '/about',
    name: 'About',
    // route level code-splitting
    // this generates a separate chunk (about.[hash].js) for this route
    // which is lazy-loaded when the route is visited.
    component: () => import(/* webpackChunkName: "about" */ '../views/About.vue')
  },
  {
    path: '/',
    redirect: '/home'
  },
  {
    path: '/home',
    component: () => import('../pages/Home.vue'),
    meta:{
      title:'lcx\'s website'
    }
  },
  {
    path: '/letter',
    component: () => import('../pages/letter/letter.vue'),
    meta:{
      title:'a-z的情书'
    }
  },
  {
    path: '/game2048',
    component: () => import('../pages/game2048/game2048.vue'),
    meta:{
      title:'2048小游戏'
    }
  },
]

const router = new VueRouter({
  // 配置路由和组件之间的关系
  // mode: 'history',
  base: process.env.BASE_URL,
  routes
})

export default router
