import Vue from 'vue'
import App from './App.vue'
import router from './router'
import store from './store'

// title
import VueWechatTitle from 'vue-wechat-title';
Vue.use(VueWechatTitle);

//muse-ui
import MuseUI from 'muse-ui';
import 'muse-ui/dist/muse-ui.css';
Vue.use(MuseUI);

// cookie
import VueCookies from 'vue-cookies';
Vue.use(VueCookies);

// animate动画
import animate from 'animate.css';
Vue.use(animate);

// elementUI
import ElementUI from 'element-ui';
import 'element-ui/lib/theme-chalk/index.css';
Vue.use(ElementUI);

// 屏蔽浏览器插件
Vue.config.devtools = false;

Vue.config.productionTip = false;

new Vue({
  router,
  store,
  render: h => h(App)
}).$mount('#app');
