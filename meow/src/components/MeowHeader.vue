<template>
    <div id="MoewHeader">
        <mu-appbar style="width: 100%;" color="primary">
            <mu-button icon slot="left" @click="back()">
                <span class="iconfont icon-back"/>
            </mu-button>
            {{headerObj.title}}
            <mu-button flat slot="right" @click="toHome()">
                <span class="iconfont icon-home"/>
            </mu-button>
        </mu-appbar>
    </div>
</template>

<script>
    export default {
        name: "MeowHeader",
        props: {
            headerObj:{
                type: Object
            }
        },
        methods:{
            back(){
                this.$router.go(-1);
            },
            toHome(){
                this.$router.push('/home');
            },
        }
    }
</script>

<style scoped>

</style>
