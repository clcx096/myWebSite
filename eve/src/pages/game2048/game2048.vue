<template>
  <div id="m2048">
    <header>
      <h1>2048</h1>
    </header>
    <button class="newGame" @click="newGame()">
      <b>开始新游戏</b>
    </button>
    <div class="score-area">
      <div class="score current">
        得分：<span>{{score.current}}</span>
      </div>
      <div class="score top">
        最高分：<span>{{score.top}}</span>
      </div>
      <p></p>
    </div>
    <mu-flex class="game-content" justify-content="between" align-items="center" direction="column"
             @touchstart="touchstart()" @touchmove="touchmove()" @touchend="touchend()">
      <mu-flex class="row" v-for="(row,index) in list" :key="index" justify-content="between">
        <div class="unit" :class="unit.value>524288? `t_large t${unit.value}` : `t${unit.value}`" :title="unit.position"
             v-for="(unit,index2) in row" :key="index2"
             @click="clickUnit(unit)">{{unit.value}}
        </div>
      </mu-flex>
    </mu-flex>
    <mu-dialog title="GAME OVER" width="360" :open.sync="gameOverDialog">
      最终得分：{{score.current}}
      <mu-button slot="actions" flat color="primary" @click="closeGameOverDialog">Close</mu-button>
    </mu-dialog>
  </div>
</template>

<script>
  export default {
    name: "m2048",
    data() {
      return {
        list: [
          [{position: "11", value: '2'},
            {position: "12", value: '4'},
            {position: "13", value: '8'},
            {position: "14", value: '16'},],

          [{position: "21", value: '32'},
            {position: "22", value: '64'},
            {position: "23", value: '128'},
            {position: "24", value: '256'},],

          [{position: "31", value: '512'},
            {position: "32", value: '1024'},
            {position: "33", value: '2048'},
            {position: "34", value: '4096'},],

          [{position: "41", value: '8192'},
            {position: "42", value: '16384'},
            {position: "43", value: '32768'},
            {position: "44", value: '65536'},],
        ],
        fingerLocation: {
          startX: '',
          startY: '',
          endX: '',
          endY: '',
        },
        score: {
          current: 0,
          top: 0,
        },
        currentUnit: {},
        gameOverFlag: false,
        gameOverDialog: false,
      }
    },
    mounted: function () {
      let _this = this;
      _this.newGame();
      // 监听键盘事件
      document.onkeydown = function () {
        let key = window.event.keyCode;
        // console.log(key);
        if (key === 37) {
          _this.leftEvent();
        } else if (key === 38) {
          _this.upEvent();
          // _this.keyEvents();
        } else if (key === 39) {
          _this.rightEvent();
        } else if (key === 40) {
          _this.downEvent();
        }
      };
      // 从cookie中获取最高分
      let top = _this.$cookies.get("m2048_top_score");
      if (top !== null) {
        _this.score.top = top;
      }
    },
    methods: {
      closeGameOverDialog() {
        this.gameOverDialog = false;
      },
      // 开始新游戏
      newGame() {
        let _this = this;
        _this.setAllUnitBlank();
        _this.setUnit();
        _this.setUnit();
        // _this.setUnit();
        // _this.setUnit();
        // _this.setUnit();
        // _this.setUnit();
        // console.log("-------------------------------")
        // let arr = [[{"position":"41","value":2},{"position":"31","value":""},{"position":"21","value":""},{"position":"11","value":""}],[{"position":"42","value":2},{"position":"32","value":2},{"position":"22","value":""},{"position":"12","value":""}],[{"position":"43","value":2},{"position":"33","value":""},{"position":"23","value":2},{"position":"13","value":""}],[{"position":"44","value":""},{"position":"34","value":2},{"position":"24","value":""},{"position":"14","value":""}]];
        // let arr=[[{"position":"41","value":"2"},{"position":"31","value":""},{"position":"21","value":""},{"position":"11","value":"8"}],[{"position":"42","value":"8"},{"position":"32","value":""},{"position":"22","value":"2"},{"position":"12","value":""}],[{"position":"43","value":""},{"position":"33","value":"32"},{"position":"23","value":""},{"position":"13","value":"16"}],[{"position":"44","value":""},{"position":"34","value":""},{"position":"24","value":"4"},{"position":"14","value":"2"}]]
        // _this.setAll(arr);
        _this.gameOverFlag = false;
        _this.score.current = 0;
      },
      clickUnit(unit) {
        console.log(unit);
        // if (unit.value === ''){
        //     unit.value = 2;
        // }
      },
      // 将所有格子设为空
      setAllUnitBlank() {
        let _this = this;
        _this.list.map(item => {
          item.map(unit => {
            unit.value = '';
          })
        })
      },
      // 获得所有为空的格子
      getBlankUnits() {
        let _this = this;
        let units = [];
        _this.list.map(item => {
          item.map(unit => {
            if (unit.value === '') {
              units.push(unit.position);
            }
          })
        });
        return units;
      },
      // 随机填充一个格子（2或4）
      setUnit() {
        let _this = this;
        let blankUnits = _this.getBlankUnits();
        let max = blankUnits.length;
        let index = _this.randomNum(0, max - 1);//避免取不到最后一个值
        let nowPosition = blankUnits[index];
        _this.list.map(item => {
          item.map(unit => {
            if (unit.position === nowPosition) {
              let value = Math.random() > 0.9 ? 4 : 2;
              unit.value = value;
              // console.log("剩余空格数="+max,"下标="+index,"位置="+nowPosition,"值"+value);
            }
          })
        });
      },
      // 重新渲染所有格子
      setAll(list) {
        let _this = this;
        list.map(item => {
          item.map(unit => {
            _this.getCurrentUnit(unit.position);
            _this.currentUnit.value = unit.value;
          })
        })
      },
      // 获得一个随机数
      randomNum(minNum, maxNum) {
        switch (arguments.length) {
          case 1:
            return parseInt(Math.random() * minNum + 1, 10);
          case 2:
            return parseInt(Math.random() * (maxNum - minNum + 1) + minNum, 10);
          default:
            return 0;
        }
      },
      // 监听触摸屏幕
      touchstart() {
        //阻止默认事件（长按的时候出现复制）
        event.preventDefault();
        this.fingerLocation.endY = '';
        this.fingerLocation.endY = '';
        this.fingerLocation.startX = event.changedTouches[0].pageX;
        this.fingerLocation.startY = event.changedTouches[0].pageY;
        // console.log(this.fingerLocation)
      },
      // 监听手指移动
      touchmove() {
        event.preventDefault();
        this.fingerLocation.endX = event.changedTouches[0].pageX;
        this.fingerLocation.endY = event.changedTouches[0].pageY;
      },
      // 监听手指释放
      touchend() {
        event.preventDefault();
        // console.log(this.fingerLocation);
        if (this.fingerLocation.endY === '' || this.fingerLocation.endY === '') {
          return;
        }
        let xLength = this.fingerLocation.endX - this.fingerLocation.startX;
        let yLength = this.fingerLocation.endY - this.fingerLocation.startY;
        // console.log(xLength,yLength);
        if (Math.abs(xLength) < 10 && Math.abs(yLength) < 10) {
          return;
        }
        if (Math.abs(yLength) >= Math.abs(xLength)) {
          // 上下移动
          if (yLength < 0) {
            this.upEvent();
          } else {
            this.downEvent();
          }
        } else {
          // 左右
          if (xLength > 0) {
            this.rightEvent();
          } else {
            this.leftEvent();
          }
        }
      },
      // 方向事件
      leftEvent() {
        this.doOnce("left");
      },
      upEvent() {
        this.doOnce("up");
      },
      rightEvent() {
        this.doOnce("right");
      },
      downEvent: function () {
        this.doOnce("down");
      },
      // 进行一次滑动
      doOnce(direction) {
        let _this = this;
        // 0.判断游戏是否结束
        if (!_this.hasNextStep()) {
          _this.gameOverFlag = true;
          _this.gameOverDialog = true;
          return;
        }
        // 1.首次对齐
        let list = _this.getDirectionalList(direction);
        _this.tidy(list);
        _this.setAll(list);
        // 2.合并
        _this.add(list);
        _this.setAll(list);
        // 3.再次对齐
        _this.tidy(list);
        _this.setAll(list);
        // 4.随机填充一个新的格子
        _this.setUnit();
      },
      // 判断游戏是否还可以进行下一步
      hasNextStep() {
        let _this = this;
        let blankUnits = _this.getBlankUnits();
        if (blankUnits.length === 0) {
          for (let i = 1; i <= 4; i++) {
            for (let j = 1; j <= 4; j++) {
              let position = "" + i + j;
              _this.getCurrentUnit(position);
              let val1 = _this.currentUnit.value;
              if (i - 1 >= 1) {
                position = "" + (i - 1) + j;
                _this.getCurrentUnit(position);
                let val2 = _this.currentUnit.value;
                if (val1 === val2) {
                  return true;
                }
              }
              if (i + 1 <= 4) {
                position = "" + (i + 1) + j;
                _this.getCurrentUnit(position);
                let val2 = _this.currentUnit.value;
                if (val1 === val2) {
                  return true;
                }
              }
              if (j - 1 >= 1) {
                position = "" + i + (j - 1);
                _this.getCurrentUnit(position);
                let val2 = _this.currentUnit.value;
                if (val1 === val2) {
                  return true;
                }
              }
              if (j + 1 <= 4) {
                position = "" + i + (j + 1);
                _this.getCurrentUnit(position);
                let val2 = _this.currentUnit.value;
                if (val1 === val2) {
                  return true;
                }
              }
            }
          }
          return false;
        }
        return true;
      },
      // 得到某个坐标位置的格子元素
      getCurrentUnit(position) {
        let _this = this;
        _this.list.map(row => {
          row.map(unit => {
            if (unit.position === position) {
              _this.currentUnit = unit;
            }
          })
        })
      },
      // 获取某个方向的更便于处理的数组
      getDirectionalList(direction) {
        let _this = this;
        let list = [];
        if (direction === 'down') {
          for (let row = 1; row <= 4; row++) {
            let currLine = [];
            for (let line = 4; line >= 1; line--) {
              let position = "" + line + row;
              _this.getCurrentUnit(position);
              currLine.push(_this.currentUnit);
            }
            list.push(currLine)
          }
        } else if (direction === 'up') {
          for (let row = 1; row <= 4; row++) {
            let currLine = [];
            for (let line = 1; line <= 4; line++) {
              let position = "" + line + row;
              _this.getCurrentUnit(position);
              currLine.push(_this.currentUnit);
            }
            list.push(currLine)
          }
        } else if (direction === 'left') {
          for (let line = 1; line <= 4; line++) {
            let currLine = [];
            for (let row = 1; row <= 4; row++) {
              let position = "" + line + row;
              _this.getCurrentUnit(position);
              currLine.push(_this.currentUnit);
            }
            list.push(currLine)
          }
        } else if (direction === 'right') {
          for (let line = 1; line <= 4; line++) {
            let currLine = [];
            for (let row = 4; row >= 1; row--) {
              let position = "" + line + row;
              _this.getCurrentUnit(position);
              currLine.push(_this.currentUnit);
            }
            list.push(currLine)
          }
        }
        return list;
      },
      // 对齐某个方向的格子
      tidy(list) {
        for (let i = 0; i < list.length; i++) {
          for (let j = 0; j < list[i].length; j++) {
            if (list[i][j].value === '') {
              // 当前位置为空，向手势来的方向找非空值
              for (let k = j + 1; k < list[i].length; k++) {
                if (list[i][k].value !== '') {
                  // console.log(JSON.parse(JSON.stringify(list[i][j])),JSON.parse(JSON.stringify(list[i][k])))
                  list[i][j].value = list[i][k].value;
                  list[i][k].value = '';
                  break;//不能缺失
                }
              }
            }
          }
        }
      },
      // 合并某个方向的格子
      add(list) {
        let _this = this;
        for (let i = 0; i < list.length; i++) {
          for (let j = 0; j < list[i].length - 1; j++) {
            if (list[i][j].value !== '' && list[i][j].value == list[i][j + 1].value) {
              // 当前位置有值，向手势来的方向找下一个位置的值
              list[i][j].value *= 2;
              list[i][j + 1].value = '';
              _this.score.current += list[i][j].value;
              if (_this.score.current > _this.score.top) {
                _this.score.top = _this.score.current;
                _this.$cookies.set("m2048_top_score", _this.score.top);
              }
            }
          }
        }
      },
    }
  }
</script>

<style scoped lang="less">
  #m2048 {
    width: 100%;
    // 新游戏按钮
    .newGame {
      width: 120px;
      height: 30px;
      line-height: 30px;
      border: none;
      border-radius: 5px;
      background-color: #8e7a66;
      color: #fff;
      font-size: 15px;
    }

    // 得分
    .score-area {
      .score {
        display: inline-block;
        margin-top: 10px;
        margin-right: 20px;
        font-weight: bold;
      }
    }

    // 游戏面板
    .game-content {
      margin: 50px auto 0;
      background-color: #bbada0;
      border-radius: 5px;
      width: 330px;
      height: 330px;
      padding: 10px;
      box-sizing: border-box;

      .row {
        width: 310px;

        .unit {
          width: 70px;
          height: 70px;
          border-radius: 5px;
          background-color: #ccc0b2;
          color: #fff;
          text-align: center;
          line-height: 70px;
        }

        .t_large {
          background-color: #3c3a32;
        }

        .t2, .t4, .t8 {
          font-size: 56px;
        }

        .t2 {
          background-color: #efe5d9;
          color: #7b7168;
        }

        .t4 {
          background-color: #ece0ca;
          color: #7b7168;
        }

        .t8 {
          background-color: #f2b179;
        }

        .t16, .t32, .t64 {
          font-size: 46px;
        }

        .t16 {
          background-color: #f59565;
        }

        .t32 {
          background-color: #f07f61;
        }

        .t64 {
          background-color: #f65d3b;
        }

        .t128, .t256, .t512 {
          font-size: 38px;
        }

        .t128 {
          background-color: #edce71;
        }

        .t256 {
          background-color: #eece5f;
        }

        .t512 {
          background-color: #e9c75a;
        }

        .t1024, .t2048, .t4096, .t8192 {
          font-size: 28px;
        }

        .t1024 {
          background-color: #e9c75a;
        }

        .t2048 {
          background-color: #edc22e;
        }

        .t4096 {
          background-color: #8e7d3a;
        }

        .t8192 {
          background-color: #6b5d26;
        }

        .t16384, .t32768, .t65536 {
          font-size: 22px;
        }

        .t16384 {
          background-color: #3c3200;
        }

        .t32768 {
          background-color: #211a00;
        }

        .t65536 {
          background-color: #2b2b2b;
        }

        .t131072, .t262144, .t524288 {
          font-size: 18px;
        }

        .t131072 {
          background-color: #7b7b7b;
        }

        .t262144 {
          background-color: #545454;
        }

        .t524288 {
          background-color: #1b1a15;
        }

        .t1048576, .t2097152, .t4194304, .t8388608 {
          font-size: 16px;
        }
      }
    }
  }
</style>
