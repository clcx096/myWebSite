<template>
    <div id="home">
        <MeowHeader :header-obj="headerObj"/>
        <div class="pages">
            <mu-list>
                <mu-list-item :button="true" :ripple="true" to="/game" class="animate__animated animate__bounceInLeft">
                    <mu-list-item-action>
                        <span class="iconfont icon-game"/>
                    </mu-list-item-action>
                    <mu-list-item-title>小游戏合集</mu-list-item-title>
                </mu-list-item>
            </mu-list>
        </div>
    </div>
</template>

<script>
    import MeowHeader from "@/components/MeowHeader";
    export default {
        name: "Home",
        components: {MeowHeader},
        data(){
            return{
                headerObj: {
                    title:"Home",
                }
            }
        }
    }
</script>

<style scoped lang="less">

</style>
