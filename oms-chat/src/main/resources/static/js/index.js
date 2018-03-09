// var serverUrl = "47.75.15.228:8888";
var serverUrl = "localhost:8888";
var webSocketUrl = serverUrl + "/websocket/chat";
var ws;
function initWebsocket() {
    if (window.WebSocket){
        ws = new WebSocket("ws:" + webSocketUrl);
        ws.onopen = function (event) {
            var item = localStorage.getItem("sessionId");
            var msg = {command:"1",sessionId:item || ''};
            ws.send(JSON.stringify(msg));
        };
        ws.onmessage = function (event) {
            var jsonMsg = JSON.parse(event.data);
            switch (jsonMsg.command){
                // webSocket初始化返回
                case "1":
                    initWebSocketResult(jsonMsg);
                    break;
                // ONLINE_EVENT,OFFLINE_EVENT
                case "11":
                    chatMessage(jsonMsg.msg);
                    break;
                case "20":
                case "21":
                    onlineOfflineNotifyMessage(jsonMsg);
                    break;
            }
        };
        ws.onclose = function (event) {
            alert("连接已断开,确认后重新连接!");
        };
    }else {
        alert("您的浏览器不支持websocket,请升级!");
    }
}

function chatMessage(jsonMsg) {
    var label = jsonMsg.self ? "rightMessageLabel" : "leftMessageLabel";

    var chatData = "<div class='"+label+"'>";
    chatData += "<span class='msgClass'>" + jsonMsg.msg + "</span><br/>";
    chatData += "<span class='nickNameClass'>" + jsonMsg.nickName + "</span>";
    chatData += "<span class='dateTimeClass'>" + jsonMsg.dateTime + "</span>";
    chatData += "</div>";

    var messageContent =  document.getElementById('messageContent');
    messageContent.innerHTML +=  chatData;

    messageContent.scrollTop = messageContent.scrollHeight;
}

function onlineOfflineNotifyMessage(jsonMsg) {
    if (jsonMsg.success){
        $("#titleText").text("聊天室(在线" + jsonMsg.count + ")");
    }
}

function initWebSocketResult(jsonMsg) {
    localStorage.setItem("sessionId",jsonMsg.sessionId);

    if (jsonMsg.msg && jsonMsg.msg.length > 0){
        jsonMsg.msg.forEach(chatMessage);
    }
    onlineOfflineNotifyMessage(jsonMsg);
}


function sendMessage() {
    if (ws){
        var messageObj = $("#message");
        if (messageObj.val() == ''){
            return;
        }
        var sessionId = localStorage.getItem("sessionId");
        var msg = {msg:messageObj.val(),command:"10",sessionId:sessionId || ''};
        ws.send(JSON.stringify(msg));
        messageObj.val('');
    }
}
document.onkeydown = function(event){
    var e = event || window.event;
    if(e && e.keyCode == 13){
        sendMessage();
    }
};
initWebsocket();