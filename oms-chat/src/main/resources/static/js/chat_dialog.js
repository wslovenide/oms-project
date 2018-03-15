
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
        var groupId = resp.msg;


    });
}
