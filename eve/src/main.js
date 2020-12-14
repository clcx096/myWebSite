import Vue from 'vue'
import App from './App.vue'
import './registerServiceWorker'
import router from './router'

//NutUI
import NutUI from '@nutui/nutui';
import '@nutui/nutui/dist/nutui.css';
NutUI.install(Vue);

// title
import VueWechatTitle from 'vue-wechat-title'
Vue.use(VueWechatTitle);

//muse-ui
import MuseUI from 'muse-ui';
import 'muse-ui/dist/muse-ui.css';
Vue.use(MuseUI);

// cookie
import VueCookies from 'vue-cookies';
Vue.use(VueCookies);

Vue.config.productionTip = false;
// 屏蔽浏览器插件
Vue.config.devtools = false;

new Vue({
  router,
  render: h => h(App)
}).$mount('#app')
