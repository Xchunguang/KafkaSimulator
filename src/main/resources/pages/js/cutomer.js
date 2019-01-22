var closeCustomr = function(){
    kafkaController.closeCustomer();
}
var scrollStatus = true;

var addMes = function(str){
    var ele = $("#cutomerDiv");
    ele.html($("#cutomerDiv").html() + "<p>"+str+"</P>");
    if(scrollStatus){
        ele.animate({scrollTop: ele.children().length * 22 + 'px'}, 0);
    }
}
var targetScroll = function(){
    scrollStatus = !scrollStatus;
}
var clearInfo = function(){
    var ele = $("#cutomerDiv");
    ele.html("");
}