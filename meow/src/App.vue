<template>
    <div id="app">
        <div class="content">
            <router-view v-wechat-title='$route.meta.title'/>
        </div>
        <footer>
            <p>孤勇之后，世界尽在眼前</p>
            <p>2020 © lcx</p>
        </footer>
    </div>
</template>
<script>
    import VueRouter from './router';

    export default {
        name: "app",
        data() {
            return {}
        },
        mounted() {
            // console.log(VueRouter;
            let parentPath = this.getParent(this.$route.path);
            if (this.$route.path !== parentPath) {
                this.$router.push({path: parentPath, query: this.$route.query});
            }
            // cookie设置为永不过期
            this.$cookies.config(-1, '/');
        },
        watch: {
            // 监听,当路由发生变化的时候执行
            $route(to) {
                // console.log(to);
                let parentPath = this.getParent(to.path);
                if (to.path !== parentPath) {
                    this.$router.push({path: parentPath, query: this.$route.query});
                }
            }
        },
        methods: {
            // 获取路由中注册的父级目录
            getParent(currPath) {
                let routes = VueRouter.options.routes;
                let flag = false;
                for (let i = 0; i < routes.length; i++) {
                    if (routes[i].path === currPath) {
                        flag = true;
                        break;
                    }
                }
                if (flag) {
                    return currPath;
                } else {
                    let arr = currPath.split("/");
                    arr.pop();
                    let parentPath = arr.join("/");
                    if (parentPath === "") {
                        parentPath = "/";
                    }
                    return this.getParent(parentPath);
                }
            },
        }
    }
</script>
<style lang="less">
    @import './assets/css/base.less';

    #app {
        font-family: Avenir, Helvetica, Arial, sans-serif;
        -webkit-font-smoothing: antialiased;
        -moz-osx-font-smoothing: grayscale;
        text-align: center;
        height: 100%;
        width: 100%;

        .content {
            height: 94%;
            width: 100%;
        }

        footer {
            font-size: 12px;
            font-family: cursive, Avenir, Helvetica, Arial, sans-serif;

            p {
                margin: 0;
            }
        }
    }
</style>
