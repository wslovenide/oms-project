
var httpRequestUrl = "http://" + serverUrl;
function createChatRoom(ele) {
    var toSessionId = $(ele).attr("sessionId");
    var sessionId = localStorage.getItem("sessionId");
    var data = {sessionId:sessionId||'',toSessionId:toSessionId||'',command:"40"};
    $.post(httpRequestUrl + "/room/create",JSON.stringify(data),function (resp) {
        if (!resp.success){
            alert(resp.msg);
            return;
        }
        layer.ready(function(){
            layer.open({
                id:resp.msg,
                type: 2,
                title: $(ele).text(),
                maxmin: true,
                shade: false,
                area: ['600px', '400px'],
                content: ['../static/chat_window.html','no'],
                end: function(){
                    // 关闭时会触发
                    // layer.tips('Hi', '#about', {tips: 1});
                }
            });
        });
    });
}

