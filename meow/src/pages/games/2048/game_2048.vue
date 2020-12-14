<template>
    <div id="m2048">
        <MeowHeader :header-obj="headerObj"/>
        <div class="btns">
            <mu-button round ripple :disabled="withdrawDisabled" color="#8e7a66" @click="withdraw()" v-if="revocable">
                <span class="iconfont icon-game-redo"/>
                <span class="btn-text">撤回</span>
            </mu-button>
            <mu-button round ripple @click="newGame()" color="#8e7a66">
                <span class="iconfont icon-game-renew"/>
                <span class="btn-text">新游戏</span>
            </mu-button>
        </div>
        <div class="score-area">
            <div class="score current">
                <span class="word">当前得分：</span>
                <span class="num">{{score.current}}</span>
                <span class="temp animate__animated" :class="score.onScoreAdd ? 'animate__fadeOutUp' : ''">{{score.temp>0?'+'+score.temp:''}}</span>
            </div>
            <div class="score top">
                <span class="word">历史最高：</span>
                <span class="num">{{score.top}}</span>
            </div>
        </div>
        <mu-flex class="game-content" justify-content="between" align-items="center" direction="column"
                 @touchstart="touchstart()" @touchmove="touchmove()" @touchend="touchend()">
            <mu-flex class="row" v-for="(row,index) in list" :key="index" justify-content="between">
                <!-- unit-box 设置每个格子的初始背景色 -->
                <div class="unit-box" v-for="(unit,index2) in row" :key="index2" @click="clickUnit(unit)">
                    <span class="unit animate__animated" :class="getUnitClass(unit)" :title="unit.position" v-show="!unit.slide">{{unit.value}}</span>
                    <span class="unit unit-copy animate__animated" :class="getSlideUnitClass(unit)" :title="unit.position" v-show="unit.slide">{{unit.value}}</span>
                </div>
            </mu-flex>
        </mu-flex>
        <mu-dialog title="GAME OVER" width="360" :open.sync="gameOverDialog">
            最终得分：{{score.current}}
            <mu-button slot="actions" flat color="primary" @click="closeGameOverDialog">Close</mu-button>
        </mu-dialog>
        <div class="game-introduce">
            <p>使相同方向上的数字相加</p>
            <p>网页端可使用方向键或WASD</p>
            <p>手机端可使用手指滑动游戏面板区域</p>
            <p v-if="wowFunction"><i>ps.点击空白格子有惊喜</i></p>
        </div>
    </div>
</template>

<script>
    import MeowHeader from "@/components/MeowHeader";

    export default {
        name: "m2048",
        components: {MeowHeader},
        data() {
            return {
                headerObj: {
                    title: "2048",
                },
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
                    current: 0, //当前得分
                    top: 0, //最高分
                    temp: 0, //本次得分
                    onScoreAdd: true, //控制分数累加动画
                    isNewRecord: false, //是否为新的记录
                    topCookieName: '2048_top_score',//cookie里存的属性名称
                    toast2048: false,
                },
                currentUnit: {},
                lastUnits: '', //上一步的状态
                gameInitFlag: false, //游戏是否初始化
                gameOverFlag: false, //游戏是否结束
                gameOverDialog: false, //游戏结束弹框
                revocable: true, //是否显示撤回按钮
                withdrawDisabled: true, //撤回按钮是否可点击
                wowFunction: false, //作弊功能是否开启
            }
        },
        mounted: function () {
            let _this = this;
            _this.setAllUnitBlank();
            // 监听键盘事件
            document.onkeydown = function () {
                let key = window.event['keyCode'];
                if (key === 37 || key === 65) {
                    _this.leftEvent();
                } else if (key === 38 || key === 87) {
                    _this.upEvent();
                } else if (key === 39 || key === 68) {
                    _this.rightEvent();
                } else if (key === 40 || key === 83) {
                    _this.downEvent();
                }
            };
            let type = _this.$route.query.type;
            if (type === 'wow') {
                _this.wowFunction = true;
                _this.gameInitFlag = true;
                _this.score.topCookieName = '2048_top_score_wow';
            } else if (type === 'direct') {
                _this.score.topCookieName = '2048_top_score_direct';
                _this.revocable = false;
                _this.newGame();
                _this.gameInitFlag = true;
            }
            // 从cookie中获取最高分
            let top = _this.$cookies.get(_this.score.topCookieName);
            if (top !== null) {
                _this.score.top = top;
            }
        },
        methods: {
            getUnitClass(unit) {
                let animateAction = unit.animate||unit.addFlag ? `animate__zoomIn` : ``;
                // if (unit.addFlag){
                //     animateAction = 'animate__heartBeat';
                // }
                let num = unit.value > 524288 ? `t_large t${unit.value}` : `t${unit.value}`;
                return animateAction + ' ' + num;
            },
            getSlideUnitClass(unit) {
                let slideAction = '';
                switch (unit.disappear) {
                    case 10:
                        slideAction = 'animate__fadeOutDown';
                        break;
                    case -10:
                        slideAction = 'animate__fadeOutUp';
                        break;
                    case 1:
                        slideAction = 'animate__fadeOutRight';
                        break;
                    case -1:
                        slideAction = 'animate__fadeOutLeft';
                        break;
                    default:
                        slideAction = ''
                }
                let num = unit.value > 524288 ? `t_large t${unit.value}` : `t${unit.value}`;
                return slideAction + ' ' + num;
            },
            closeGameOverDialog() {
                this.gameOverDialog = false;
            },
            // 开始新游戏
            newGame() {
                let _this = this;
                if (_this.score.current > _this.score.top) {
                    _this.score.top = _this.score.current;
                    _this.$cookies.set(_this.score.topCookieName, _this.score.top);
                }
                _this.setAllUnitBlank();
                _this.setUnit();
                _this.setUnit();
                // 测试game over的数据
                // let str = '[[{"position":"41","value":8,"animate":true},{"position":"31","value":128,"animate":true},{"position":"21","value":4,"animate":true},{"position":"11","value":"","animate":true}],[{"position":"42","value":8,"animate":true},{"position":"32","value":16,"animate":true},{"position":"22","value":8,"animate":true},{"position":"12","value":2,"animate":true}],[{"position":"43","value":32,"animate":true},{"position":"33","value":128,"animate":true},{"position":"23","value":32,"animate":true},{"position":"13","value":8,"animate":true}],[{"position":"44","value":2,"animate":true},{"position":"34","value":8,"animate":true},{"position":"24","value":16,"animate":true},{"position":"14","value":4,"animate":true}]]';
                // _this.setAll(JSON.parse(str));
                _this.gameInitFlag = true;
                _this.score.current = 0;
                _this.score.toast2048 = false;
                _this.gameOverFlag = false;
                _this.withdrawDisabled = true;
                _this.score.isNewRecord = false;
            },
            clickUnit(unit) {
                // console.log(JSON.stringify(unit));
                if (unit.value === '') {
                    if (this.wowFunction) {
                        setTimeout(() => {
                            unit.animate = true;
                            unit.value = 2;
                            setTimeout(() => {
                                unit.animate = false;
                            }, 0);
                        }, 0);
                    }
                }
            },
            // 将所有格子设为空
            setAllUnitBlank() {
                let _this = this;
                _this.list.map(item => {
                    item.map(unit => {
                        unit.value = '';
                        unit.slide = false;
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
                            // 动画控制
                            unit.animate = false;
                            setTimeout(() => {
                                unit.animate = true;
                                unit.value = value;
                            }, 0);
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
                let clickEvent = false;
                if (this.fingerLocation.endY === '' || this.fingerLocation.endY === '') {
                    clickEvent = true;
                }
                let xLength = this.fingerLocation.endX - this.fingerLocation.startX;
                let yLength = this.fingerLocation.endY - this.fingerLocation.startY;
                // console.log(xLength,yLength);
                if (Math.abs(xLength) < 10 && Math.abs(yLength) < 10) {
                    clickEvent = true;
                }
                if (clickEvent) {
                    this.getCurrentUnit(event.target['title']);
                    this.clickUnit(this.currentUnit);
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
                if (!_this.gameInitFlag) {
                    return;//游戏尚未开始
                }
                // 0. 判断游戏是否结束
                if (_this.gameOverFlag) {
                    _this.gameOverDialog = true;
                    return;
                }
                // 1.首次对齐
                let list = _this.getDirectionalList(direction);
                let _lastUnitsData = _this.lastUnits;
                _this.lastUnits = JSON.stringify(list); //记录数据以备撤回
                _this.tidy(list);
                let tidyFlag = false;
                if (JSON.stringify(list) === _this.lastUnits) {
                    tidyFlag = true;
                }
                _this.setAll(list);
                // 2.合并
                _this.add(list);
                let addFlag = false;
                if (_this.score.temp === 0) {
                    addFlag = true;
                }
                // 不能对齐也不能合并
                if (tidyFlag && addFlag) {
                    _this.lastUnits = _lastUnitsData;
                    return;
                }
                /**
                 * 因为add里用了500毫秒延迟表现合并动画，所以这里需要500ms之后再次合并
                 */
                // setTimeout(()=>{
                    // _this.setAll(list);
                    // 3.再次对齐
                    _this.tidy(list);
                    _this.setAll(list);

                    // 4.随机填充一个新的格子
                    _this.setUnit();
                    /**
                     * 因为setUnit里用了延迟函数体现动画效果，所以这里也需要延迟
                     * 否则会导致hasNextStep里getBlankUnits数据获取出错
                     */
                    setTimeout(() => {
                        // 5.判断游戏是否还可以进行下一步
                        if (!_this.hasNextStep()) {
                            _this.gameOverFlag = true;
                            _this.gameOverDialog = true;
                            if (_this.score.current > _this.score.top) {
                                _this.score.top = _this.score.current;
                                _this.$cookies.set(_this.score.topCookieName, _this.score.top);
                                _this.score.isNewRecord = true;
                            }
                        }
                    }, 0);
                    // 6.可撤回
                    _this.withdrawDisabled = false;
                // },500);
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
                // let tidyUnits = [];
                for (let i = 0; i < list.length; i++) {
                    for (let j = 0; j < list[i].length; j++) {
                        if (list[i][j].value === '') {
                            // 当前位置为空，向手势来的方向找非空值
                            for (let k = j + 1; k < list[i].length; k++) {
                                if (list[i][k].value !== '') {
                                    list[i][j].value = list[i][k].value;
                                    list[i][k].value = '';
                                    // tidyUnits.push({
                                    //     disappearPosition: list[i][k].position,
                                    //     alivePosition: list[i][j].position,
                                    // });
                                    break;//不能缺失
                                }
                            }
                        }
                    }
                }
                // 对齐动画
                // tidyUnits.map(item=>{
                //    // console.log("对齐",item);
                // });
            },
            // 合并某个方向的格子
            add(list) {
                let _this = this;
                _this.score.temp = 0;
                _this.score.addUnits = [];
                for (let i = 0; i < list.length; i++) {
                    for (let j = 0; j < list[i].length - 1; j++) {
                        if (list[i][j].value !== '' && list[i][j].value === list[i][j + 1].value) {
                            // 当前位置有值，向手势来的方向找下一个位置的值
                            list[i][j].value *= 2;
                            list[i][j + 1].value = '';
                            _this.score.temp += list[i][j].value;
                            if (list[i][j].value === 2048 && !_this.score.toast2048) {
                                _this.$message({
                                    showClose: true,
                                    message: '恭喜你，得到了2048',
                                    type: 'success',
                                    duration: 1500,
                                    center: true,
                                });
                                _this.score.toast2048 = true;
                            }
                            // 记录一组需要合并的格子
                            _this.score.addUnits.push({
                                disappearPosition: list[i][j+1].position + '',
                                alivePosition:list[i][j].position,
                                aliveValue: list[i][j].value*2,
                            });
                            // j++;//加入动画时添加这一句;下一个位置已经合并到前一个位置了
                        }
                    }
                }
                // 合并动画
                // _this.score.addUnits.map(item =>{
                //     // 获取要消失的格子
                //     // _this.getCurrentUnit(item.disappearPosition);
                //     // // 显示要消失的区域，隐藏默认区域
                //     // _this.currentUnit.slide = true;
                //     // // 消失动画
                //     // setTimeout(()=>{
                //     //     _this.getCurrentUnit(item.disappearPosition);
                //     //     _this.currentUnit.disappear = item.alivePosition - item.disappearPosition;
                //     //     setTimeout(()=>{
                //     //         _this.getCurrentUnit(item.disappearPosition);
                //     //         _this.currentUnit.disappear = '';
                //     //         // 消失动画 end 隐藏消失区域，展示默认区域
                //     //         setTimeout(()=>{
                //     //             _this.getCurrentUnit(item.disappearPosition);
                //     //             _this.currentUnit.slide = false;
                //     //             _this.currentUnit.value = '';
                //     //         },380);
                //     //     },0);
                //     // },0);
                //
                //     // 消失，不加动画
                //     _this.getCurrentUnit(item.disappearPosition);
                //     _this.currentUnit.value = '';
                //
                //     // 合并后的格子产生的动画
                //     // 1.获取合并的格子位置
                //     _this.getCurrentUnit(item.alivePosition);
                //     _this.currentUnit.addFlag = false;
                //     _this.currentUnit.alive = item.alivePosition - item.disappearPosition;
                //     setTimeout(() => {
                //         _this.getCurrentUnit(item.alivePosition);
                //         _this.currentUnit.addFlag = true;
                //         _this.currentUnit.value = item.aliveValue;
                //     }, 0);
                // });

                // 计分
                if (_this.score.temp > 0) {
                    _this.score.current += _this.score.temp;
                }
                // 加分动画
                _this.score.onScoreAdd = false;
                setTimeout(() => {
                    _this.score.onScoreAdd = true;
                }, 0);
            },
            // 撤回
            withdraw() {
                let _this = this;
                _this.setAll(JSON.parse(_this.lastUnits));
                _this.withdrawDisabled = true;
                _this.lastUnits = '';
                _this.gameOverFlag = false;
                // 减少分数
                _this.score.current -= _this.score.temp;
                if (_this.score.isNewRecord) {
                    _this.score.top -= _this.score.temp;
                }
            },
        }
    }
</script>

<style scoped lang="less">
    #m2048 {
        width: 100%;
        // 按钮
        .btns button {
            margin: 20px;

            .btn-text {
                margin-left: 5px;
            }
        }

        // 得分
        .score-area {
            .score {
                display: inline-block;
                margin-top: 10px;
                margin-right: 20px;
                font-size: 16px;
                position: relative;

                .word {
                    font-size: 14px;
                }

                .num {
                    font-weight: bold;
                }

                .temp {
                    position: absolute;
                    top: .9em;
                    left: 4.7em;
                    font-size: .9em;
                    background-color: #d6bfab;
                    border-radius: 1em;
                    padding: 0 .2em;
                }
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

                .unit-box {
                    width: 70px;
                    height: 70px;
                    box-sizing: border-box;
                    border-radius: 5px;
                    background-color: #ccc0b2;
                    color: #fff;
                    display: flex;
                    justify-content: center; /* 水平居中 */
                    align-items: center; /* 垂直居中 */
                    position: relative;
                    .unit {
                        width: 100%;
                        height: 100%;
                        border-radius: inherit;
                        line-height: 70px;
                    }
                    .unit-copy{
                        position: absolute;
                        border: 1px solid #3a8ee6;
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

        // 介绍
        .game-introduce {
            margin-top: 30px;

            p {
                margin: .3em 0;
            }
        }
    }
</style>
