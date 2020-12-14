<template>
  <div id='letter'>
    <header>
      <img :src='require("@/assets/images/az_header.png")'>
    </header>

    <div class="word-list">
      <ul class="words">
        <li v-for="word in wordArr" :key="word.content" @click="wordClick(word)" class="word">
          <img :src="require('@/assets/images/az/'+word.content+'1.png')" v-if="!word.active">
          <img :src="require('@/assets/images/az/'+word.content+'2.png')" v-else>
        </li>
      </ul>
    </div>
  </div>
</template>
3
<script>
  export default {
    name:'letter',
    data(){
      return{
        wordArr:[
          {content:'a',active:false},
          {content:'b',active:false},
          {content:'c',active:false},
          {content:'d',active:false},
          {content:'e',active:false},
          {content:'f',active:false},
          {content:'g',active:false},
          {content:'h',active:false},
          {content:'i',active:false},
          {content:'j',active:false},
          {content:'k',active:false},
          {content:'l',active:false},
          {content:'m',active:false},
          {content:'n',active:false},
          {content:'o',active:false},
          {content:'p',active:false},
          {content:'q',active:false},
          {content:'r',active:false},
          {content:'s',active:false},
          {content:'t',active:false},
          {content:'u',active:false},
          {content:'v',active:false},
          {content:'w',active:false},
          {content:'x',active:false},
          {content:'y',active:false},
          {content:'z',active:false},
        ],
        ajson:{
          "title": "Aware",
          "text": [
            {"content":"Aware，意识到，感觉到。"},
            {"content":"意识，很虚幻的事情，仿佛是茫茫然地滋生出一些想法，很多时候，或许我不知道为什么会这样，但是我确信我应该要这样。就好像开心的时候，可能感觉到每一多云都在愉快地打着招呼、每一个涟漪的泛起都充满欢喜、鸟儿在树上吱吱咋咋的叫声也似乎都蕴含别样的深意；烦闷的时候，却会感觉天空也和内心一般愁云密布、水花哗啦啦地嘚瑟着他们无忧无虑的旅途、甚至连喜鹊的啼鸣都染着乌鸦般的黑色……"},
            {"content":"而此刻的我，到底意识到了什么呢？似乎是意识到每一首情歌都在写着我。事实当然不是这样，那些歌并非为我而写，只不过“人同此心，心同此理”罢了。故事都是一样的，只不过主角不同，时间地点也略有差异，但是不变的是心中对那一份美好的向往。有人说“世界上能够成为永恒的东西只有两样——爱与死。”而我既然已经注定了会迎来后者的永恒，不如再来一份凑成双吧。而最圆满的无非是在爱里死去，是啊，在爱人的怀里，哪里还顾得上生死呢？"},
            {"content":"诚然，我的意识与你的意识不相干，人类文明还没进步到那个可以在人的思想间建立联系而让人意识相通的地步，但是人向来是渴望美好的，自古以来皆如此。而我也是，和我的祖辈们一样，希望着改变，期盼着更美好。你在，是美好，是在荒山里遇见一朵花的盛开、是在夏林里聆听一阵风的呼吸；你来，是更美好，是饥饿时的饱餐、是困闷时的酣眠。"},
            {"content":"这是我的意识，关于你的意识。"}
          ]
        },
        dialogShow: false,
        currentLetter:{
          title:'',
          content:'',
        },
      }
    },
    mounted() {
    },
    methods:{
      wordClick(word){
        // 按钮激活
        let _this = this;
        let old = word.active;
        this.wordArr.map((item, index) => {
          item.active = false;
        })
        word.active = !old;
        // 获取数据
        this.ajson = require('../../assets/data/az/'+word.content+'.json');
        // 组装数据
        this.currentLetter.title = this.ajson.title;
        let contentHtml = '<div style="padding: 2px;font-size: 18px">';
        this.ajson.text.map((item, index) => {
          contentHtml += '<p style="text-align: left;text-indent: 2em">' + item.content + '</p>'
        })
        contentHtml += '</div>';
        this.currentLetter.content = contentHtml;
        // 弹窗
        this.$dialog({
          title: _this.currentLetter.title,
          lockBgScroll: true,
          content: contentHtml,
          noFooter: true,
          closeCallback: function () {
            _this.wordArr.map((item, index) => {
              item.active = false;
            })
          }
        });
        // _this.$alert(contentHtml, _this.currentLetter.title, {
        //   okLabel: '知道了'
        // }).then(() => {
        //   _this.$toast.message('提示信息');
        // });
      },
    }
  }
</script>

<style lang="less" scoped>
  #letter{
    header{
      width: 100%;
      text-align: center;
      img{
        width: 80%;
      }
    }

    //列表样式
    div.word-list{
      margin-top: 10px;
      ul.words{
        width: 100%;
        text-align: left;
        li.word{
          width: 25%;
          border-radius: 50%;
          display: inline-block;
          box-sizing: border-box;
          img{
            width: 100%;
          }
        }
      }
    }
  }
</style>

