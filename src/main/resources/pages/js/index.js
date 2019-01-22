var obj = {
    kafkaAddress:'',
    port:9092,
    topic:'',
    partNum:1,
    sendInfo:'',
    sendNum:-1,
    sendTime:1000
}
var scrollStatus = true;

var targetScroll = function(){
    scrollStatus = !scrollStatus;
}
var send = function(){

    obj.kafkaAddress = $("#kafkaAddress").val();
    obj.port = $("#port").val();
    obj.topic = $("#topic").val();
    obj.partNum = $("#partNum").val();
    obj.sendInfo = $("#sendInfo").val();
    obj.sendNum = $("#sendNum").val();
    obj.sendTime = $("#sendTime").val();

    kafkaController.startSend(JSON.stringify(obj));
}
var getObj = function(str){
    if(str.length > 0){
        var curObj = JSON.parse(str);
        if(curObj.kafkaAddress){
            obj.kafkaAddress = curObj.kafkaAddress;
            $("#kafkaAddress").val(obj.kafkaAddress);
        }
        if(curObj.port){
            obj.port = curObj.port;
            $("#port").val(obj.port);
        }
        if(curObj.topic){
            obj.topic = curObj.topic;
            $("#topic").val(obj.topic);
        }
        if(curObj.partNum){
            obj.partNum = curObj.partNum;
            $("#partNum").val(obj.partNum);
        }
        if(curObj.sendInfo){
            obj.sendInfo = curObj.sendInfo;
            $("#sendInfo").val(obj.sendInfo);
        }
        if(curObj.sendNum){
            obj.sendNum = curObj.sendNum;
            $("#sendNum").val(obj.sendNum);
        }
        if(curObj.sendTime){
            obj.sendTime = curObj.sendTime;
            $("#sendTime").val(obj.sendTime);
        }
    }
}

var stop = function(){
    kafkaController.stopSend();
}

var appendValue = function(str){
    str = JSON.stringify(str);
    var ele = $("#resultPane");
    ele.html($("#resultPane").html() + "<p>" +str+ "</p>");
    if(scrollStatus){
        ele.animate({scrollTop: ele.children().length * 22 + 'px'}, 10);
    }
}
var clearResult = function(){
    var ele = $("#resultPane");
    ele.html("");
}
var openCutomer = function(){
    obj.kafkaAddress = $("#kafkaAddress").val();
    obj.port = $("#port").val();
    obj.topic = $("#topic").val();
    obj.partNum = $("#partNum").val();
    obj.sendInfo = $("#sendInfo").val();
    obj.sendNum = $("#sendNum").val();
    obj.sendTime = $("#sendTime").val();

    kafkaController.openCustomer(JSON.stringify(obj));
}