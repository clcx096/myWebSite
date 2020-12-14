<template>
    <div id="game">
        <MeowHeader :header-obj="headerObj"/>
        <div class="games">
            <mu-list toggle-nested>
                <mu-list-item button :ripple="true" nested class="animate__animated animate__bounceInLeft"
                              :open="open === 'send'" @toggle-nested="open === arguments[0] ? 'send' : ''">
                    <mu-list-item-action>
                        <span class="iconfont icon-folder"/>
                    </mu-list-item-action>
                    <mu-list-item-title>2048</mu-list-item-title>

                    <mu-list-item button :ripple="true" to="/game/2048?type=direct" slot="nested"
                                  class="animate__animated animate__fadeInDown">
                        <mu-list-item-action>
                            <span class="iconfont icon-item"/>
                        </mu-list-item-action>
                        <mu-list-item-title>莽就完事了</mu-list-item-title>
                    </mu-list-item>
                    <mu-list-item button :ripple="true" to="/game/2048?type=normal" slot="nested"
                                  class="animate__animated animate__fadeInDown">
                        <mu-list-item-action>
                            <span class="iconfont icon-item"/>
                        </mu-list-item-action>
                        <mu-list-item-title>三思而后行</mu-list-item-title>
                    </mu-list-item>
                    <mu-list-item button :ripple="true" to="/game/2048?type=wow" slot="nested"
                                  class="animate__animated animate__fadeInDown">
                        <mu-list-item-action>
                            <span class="iconfont icon-item"/>
                        </mu-list-item-action>
                        <mu-list-item-title>有备而来，不讲武德</mu-list-item-title>
                    </mu-list-item>
                </mu-list-item>
            </mu-list>

<!--            <mu-list v-for="item in list" :key="item.title" toggle-nested nested>-->
<!--                <mu-list-item button :ripple="false" nested class="animate__animated animate__bounceInLeft"-->
<!--                              :open="item.open==='send'" @toggle-nested="item.open === arguments[0] ? 'send' : ''"-->
<!--                                @click="clickItem(item)">-->
<!--                    <mu-list-item-action>-->
<!--                        <span class="iconfont icon-folder"/>-->
<!--                    </mu-list-item-action>-->
<!--                    <mu-list-item-title>{{item.title}}</mu-list-item-title>-->
<!--                </mu-list-item>-->
<!--                <mu-list-item button :ripple="false" class="animate__animated animate__fadeInDown" slot="nested"-->
<!--                              v-for="sub in item.subs" :key="sub.title" :to="sub.path">-->
<!--                    <mu-list-item-action>-->
<!--                        <span class="iconfont icon-item"/>-->
<!--                    </mu-list-item-action>-->
<!--                    <mu-list-item-title>{{sub.title}}</mu-list-item-title>-->
<!--                </mu-list-item>-->
<!--            </mu-list>-->
        </div>
    </div>
</template>

<script>
    import MeowHeader from "@/components/MeowHeader";
    export default {
        name: "game",
        components: {MeowHeader},
        data(){
            return{
                headerObj: {
                    title:"Game",
                },
                open: 'send',
                game: {
                    title:'2048',
                    open: '',
                    subs:[
                        {
                            title:'莽就完事了',
                            path:'/game/2048/v2?type=direct',
                        },
                        {
                            title:'三思而后行',
                            path:'/game/2048/v2',
                        },
                        {
                            title:'有备而来，不讲武德',
                            path:'/game/2048/v2?type=wow',
                        },
                    ],
                },
                list: [{
                    title:'2048',
                    open: false,
                    subs:[
                        {
                            title:'莽就完事了',
                            path:'/game/2048/v2?type=direct',
                        },
                        {
                            title:'三思而后行',
                            path:'/game/2048/v2',
                        },
                        {
                            title:'有备而来，不讲武德',
                            path:'/game/2048/v2?type=wow',
                        },
                    ],
                }],
            }
        },
        methods:{
            clickItem(item){
                item.open = !item.open;
            }
        }
    }
</script>

<style scoped>

</style>
