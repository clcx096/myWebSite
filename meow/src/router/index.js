import Vue from 'vue'
import VueRouter from 'vue-router'

Vue.use(VueRouter);

// 自定义路由
let customRoutes = [
    {
        path: '/',
        redirect: '/home'
    },
    {
        path: '/home',
        component: () => import('@/pages/Home.vue'),
        meta: {
            title: '喵meow'
        }
    },
    {
        path: '/game',
        component: () => import('../pages/games/game.vue'),
        meta: {
            title: 'meow小游戏'
        },
        subRoutes: [
            {
                path: '/2048',
                component: () => import('../pages/games/2048/game_2048.vue'),
                meta: {
                    title: '2048小游戏'
                },
            },
        ]
    },
];

let routes = [];
parseRoutes("", customRoutes);
// console.log(customRoutes, routes);

const router = new VueRouter({
    // mode: 'history', //添加该属性后地址栏不会出现hash,但打包后访问会出错
    base: process.env.BASE_URL,
    routes
});

/**
 * 将自定义路由解析成符合vue要求的样子
 * @param parentPath 父级路径
 * @param customRoutes 自定义的路由列表
 */
function parseRoutes(parentPath, customRoutes) {
    for (let i = 0; i < customRoutes.length; i++) {
        // 拼接完整路径
        customRoutes[i].path = parentPath + customRoutes[i].path;
        let arr = customRoutes[i].subRoutes;
        if (arr !== undefined && arr instanceof Array && arr.length > 0) {
            parseRoutes(customRoutes[i].path, arr);
        }
        routes.push(customRoutes[i]);
    }
}

export default router;
