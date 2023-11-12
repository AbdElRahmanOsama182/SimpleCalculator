<template>
    <div class="Calculator">
        <div class="screen">
            <div id="top">{{ expression }}</div>
            <div id="bottom">{{ currNum }}</div>
        </div>
        <button @click="singleOperation('percent')" class="opBtn">%</button>
        <button @click="clear" class="opBtn">CE</button>
        <button @click="clear" class="opBtn">C</button>
        <button @click="deleting" class="opBtn">&#9003;</button>
        <button @click="singleOperation('fraction')" class="opBtn">&sup1;&#8725;&#8339;</button>
        <button @click="singleOperation('square')" class="opBtn">x²</button>
        <button @click="singleOperation('squareRoot')" class="opBtn">√x</button>
        <button @click="doubleOperation('divide','÷')" class="opBtn">÷</button>
        <button @click="appendNum('7')">7</button>
        <button @click="appendNum('8')">8</button>
        <button @click="appendNum('9')">9</button>
        <button @click="doubleOperation('multiply','&times;')" class="opBtn">&times;</button>
        <button @click="appendNum('4')">4</button>
        <button @click="appendNum('5')">5</button>
        <button @click="appendNum('6')">6</button>
        <button @click="doubleOperation('subtract','&minus;')" class="opBtn">&minus;</button>
        <button @click="appendNum('1')">1</button>
        <button @click="appendNum('2')">2</button>
        <button @click="appendNum('3')">3</button>
        <button @click="doubleOperation('add','+')" class="opBtn">+</button>
        <button @click="singleOperation('negate')"><sup>+</sup>&#8725;<sub>&minus;</sub></button>
        <button @click="appendNum('0')">0</button>
        <button @click="appendNum('.')">.</button>
        <button @click="equal" class="opBtn">=</button>
    </div>
</template>

<script>
export default {
    name: 'Calculator',
    data() {
        return {
            expression: '',
            currNum: '',
            firstOperand: '',
            secondOperand: '',
            opertion: '',
            operator: false,
            operatorDone: false,
            done: false,
            prevSingle: false,
            temp: '',
        }
    },
    methods: {
        clear() {
            this.expression = '';
            this.currNum = '';
            this.firstOperand = '';
            this.secondOperand = '';
            this.opertion = '';
            this.operator = false;
            this.operatorDone = false;
            this.prevSingle = false;
            this.done = false;
        },
        deleting() {
            if(this.currNum==='E' || this.done){
                this.clear();
            } 
            if ((this.currNum === '' && this.operator)||this.operatorDone) {
                this.temp = this.firstOperand;
                this.clear();
                this.currNum = this.temp;
            }
            else this.currNum = this.currNum.slice(0, -1);
        },
        appendNum(num) {
            if(this.currNum==='E') this.clear();
            if(this.done||this.prevSingle) {
                this.clear();
            }
            if(this.operatorDone) {
                this.firstOperand = this.currNum;
                this.currNum = '';
                this.operatorDone = false;
            }
            if (num === '.' && this.currNum.includes('.')) return;
            
            if (this.currNum === '0') this.deleting();
            if (num === '.' && this.currNum === '') this.currNum = '0';
            this.currNum += num;
        },

        async singleOperation(op) {
            if (this.currNum !== '' && this.currNum !== 'E' && !this.operatorDone) {
                await fetch(`http://localhost:8081/`+op+`/`+this.currNum, {
                    method: 'get',
                }).then(res => {
                    return res.text();
                }).then(data => {
                    this.currNum = data;
                });
                if (this.done) {
                    this.temp = this.currNum;
                    this.clear();
                    this.currNum = this.temp;
                }
                this.prevSingle = true;
            }
        },
        async doubleOperation(op,opSymbol) {
            if (this.currNum === '' || this.currNum === 'E') return;
            if (this.done) {
                this.temp = this.currNum;
                this.clear();
                this.currNum = this.temp;
            }
            if (!this.operator) {
                this.prevSingle = false;
                this.firstOperand = this.currNum;
                this.expression = this.expression + ' ' + this.firstOperand + ' ' + opSymbol;
                this.opertion = op;
                this.operator = true;
                this.currNum = '';
            } else if (!this.operatorDone){
                this.prevSingle = false;
                this.secondOperand = this.currNum;
                await fetch(`http://localhost:8081/`+this.firstOperand+`/`+this.opertion+`/`+this.secondOperand, {
                    method: 'get',
                }).then(res => {
                    return res.text();
                }).then(data => {
                    this.currNum = data;
                });
                if (this.currNum !== 'E') {
                    this.expression = this.expression + ' ' + this.secondOperand + ' ' + opSymbol;
                }
                else {
                    this.expression = this.expression + ' ' + this.secondOperand;
                }
                this.firstOperand = this.currNum;
                this.opertion = op;
                this.operator = true;
                this.operatorDone = true;
            }
        },
        equal(){
            if (!this.done && this.currNum !== '' && this.currNum !== 'E' && !this.operatorDone) {
                if (this.operator) {
                    this.secondOperand = this.currNum;
                    this.expression = this.expression + ' ' + this.secondOperand;
                    fetch(`http://localhost:8081/`+this.firstOperand+`/`+this.opertion+`/`+this.secondOperand, {
                        method: 'get',
                    }).then(res => {
                        return res.text();
                    }).then(data => {
                        this.currNum = data;
                    });
                }
                this.done = true;
            }
        }
    },
}

</script>

<style>
    .Calculator {
        
        display: grid;
        grid-template-columns: repeat(4, 1fr);
        grid-auto-rows: minmax(50px, auto);
        grid-gap: 5px;
        padding: 40px 20px 20px 20px;
        border: 1px solid black;
        border-radius: 20px;
        width: 300px;
        background-color: rgb(0, 0, 0);
    }
    .screen {
        display: flex;
        flex-direction: column;
        grid-column: 1 / -1;
        grid-row: 1 / 3;
        border: 5px solid rgb(43, 42, 44);
        border-radius: 10px;
        margin-bottom: 10px;
        text-align: right;
        background-color: rgb(78, 77, 80);
        word-wrap: break-word;
    }

    #top {
        flex: 1.5;
        padding: 5px 10px 0px 10px;
        text-align: left;
        font-size: 1.5em;
    }
    #bottom {
        flex: 2;
        font-size: 2em;
        color: whitesmoke;
        padding: 0 10px;
    }
    .opBtn {
        background-color: rgb(39, 97, 151);
    }
    .opBtn:hover {
        background-color: rgba(223, 123, 0, 0.853);
    }
    button {
        border: 5px solid rgb(43, 42, 44);
        border-radius: 10px;
        padding: 5px;
        font-size: 1.5em;
    }
    
    button:hover {
        background-color: lightgray;
        cursor: pointer;
    }
</style>
