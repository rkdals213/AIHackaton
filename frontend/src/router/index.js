import Vue from 'vue'
import VueRouter from 'vue-router'
import Home from '../views/Home.vue'
import Join from '@/components/Join.vue'
import Login from '@/components/Login.vue'
import Payed from '@/views/Payed.vue'
import MyInfo from '@/views/MyInfo'
import CardList from '@/views/CardList'
import MyPick from '@/views/MyPick'
import Next from '@/components/Next'
import Recommend from '@/components/Recommend'

Vue.use(VueRouter)

  const routes = [
  {
    path: '/',
    name: 'Home',
    component: Home
  },
  {
    path: '/about',
    name: 'About',
    // route level code-splitting
    // this generates a separate chunk (about.[hash].js) for this route
    // which is lazy-loaded when the route is visited.
    component: () => import(/* webpackChunkName: "about" */ '../views/About.vue')
  },
  {
    path:'/join',
    name: 'Join',
    component: Join
  },
  {
    path: '/login',
    name: 'Login',
    component: Login,
  },
  {
    path: '/Payed',
    name: 'Payed',
    component: Payed,
  },
  {
    path: '/MyInfo',
    name: 'MyInfo',
    component: MyInfo,
  },
  {
    path: '/cardlist',
    name: 'CardList',
    component: CardList,
  },
  {
    path: '/MyPick',
    name: 'MyPick',
    component: MyPick,
  },
  {
    path: '/next',
    name: 'Next',
    component: Next,
  },
  {
    path: '/recommend',
    name: 'Recommend',
    component: Recommend,
  }
]

const router = new VueRouter({
  mode: 'history',
  base: process.env.BASE_URL,
  routes
})

export default router
