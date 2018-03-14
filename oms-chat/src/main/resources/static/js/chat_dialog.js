
var httpRequestUrl = "http://" + serverUrl;
function createChatRoom(ele) {
    var toSessionId = $(ele).attr("sessionId");
    var sessionId = localStorage.getItem("sessionId");
    var data = {sessionId:sessionId||'',toSessionId:toSessionId||'',command:"40"};

    $.post(httpRequestUrl + "/room/create",JSON.stringify(data),function (resp) {
        console.info(resp);
    });
}
