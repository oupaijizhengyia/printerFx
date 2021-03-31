var events = require('events');
// 创建 eventEmitter 对象
var eventEmitter = new events.EventEmitter();
eventEmitter.onchange

let i = 0;
eventEmitter.on("times",function () {
    console.log("第" + i + "次执行");
    i++;
})

eventEmitter.once("once",function () {
    console.log("我只执行一次")
})

setInterval(function () {
    eventEmitter.emit("times")
},1000)

setInterval(function () {
    eventEmitter.emit("once")
},1000)

