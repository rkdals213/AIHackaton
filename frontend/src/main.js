import Vue from 'vue'
import App from './App.vue'
import store from './store'
import vuetify from './plugins/vuetify';
import router from './router'
import VueApexCharts from 'vue-apexcharts'
import vuetifyLoader from "vuetify-vuejs-loader";
import VueCookies from 'vue-cookies'

Vue.use(VueCookies)

Vue.use(vuetifyLoader);
Vue.use(VueApexCharts)
Vue.component('apexchart', VueApexCharts)
Vue.config.productionTip = false

new Vue({
  store,
  vuetify,
  router,
  render: h => h(App)
}).$mount('#app')
