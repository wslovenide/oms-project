// var serverUrl = "47.75.15.228:8888";
var serverUrl = "localhost:8888";
var webSocketUrl = serverUrl + "/websocket/chat";
var ws;
function initWebsocket() {
    if (window.WebSocket){
        ws = new WebSocket("ws:" + webSocketUrl);
        ws.onopen = function (event) {
            var sessionId = localStorage.getItem("sessionId");
            var groupId = localStorage.getItem("groupId");
            var msg = {command:"1",sessionId:sessionId || '',groupId:groupId || ''};
            ws.send(JSON.stringify(msg));
        };
        ws.onmessage = function (event) {
            var jsonMsg = JSON.parse(event.data);
            if (!jsonMsg.success){
                alert(jsonMsg.msg);
                return;
            }
            dispatchResponseMsg(jsonMsg);
        };
        ws.onclose = function (event) {
            alert("连接已断开,确认后重新连接!");
        };
    }else {
        alert("您的浏览器不支持websocket,请升级!");
    }
}

function dispatchResponseMsg(jsonMsg) {
    switch (jsonMsg.command){
        // webSocket初始化返回
        case "1":
            initWebSocketResult(jsonMsg);
            break;
        // chat message
        case "11":
            chatMessage(jsonMsg.msg);
            break;
        // ONLINE_EVENT,OFFLINE_EVENT
        case "20":
        case "21":
            onlineOfflineNotifyMessage(jsonMsg);
            break;
    }
}

function chatMessage(jsonMsg) {
    var label = jsonMsg.self ? "rightMessageLabel" : "leftMessageLabel";
    var chatData = "<div class='"+label+"'>";
    chatData += "<span class='msgClass'>" + jsonMsg.msg + "</span><br/>";
    chatData += "<span class='nickNameClass' onclick='createChatRoom(this);' sessionid='"+ jsonMsg.sessionId +"'>" + jsonMsg.nickName + "</span>";
    chatData += "<span class='dateTimeClass'><label title='" + jsonMsg.date + "'>" + jsonMsg.time + "</label></span>";
    chatData += "</div>";

    $(chatData).appendTo("#PUBLIC_GROUP");
    $("#messageContent").scrollTop($("#messageContent")[0].scrollHeight);
}

function onlineOfflineNotifyMessage(jsonMsg) {
    if (jsonMsg.success){
        $("#titleText").text("聊天室(在线" + jsonMsg.count + ")");
        var tipMsg = "[" + jsonMsg.msg.nickName + "]" + (jsonMsg.command == "21" ? "退出房间" : "进入房间");
        $("<div class='onlineOfflineTip'>" + tipMsg + "</div>").appendTo("#PUBLIC_GROUP");
        $("#PUBLIC_GROUP").scrollTop($("#PUBLIC_GROUP")[0].scrollHeight);
    }
}

function initWebSocketResult(jsonMsg) {
    if (jsonMsg.success){
        localStorage.setItem("sessionId",jsonMsg.sessionId);
        localStorage.setItem("groupId",jsonMsg.groupId);

        if (jsonMsg.msg && jsonMsg.msg.length > 0){
            jsonMsg.msg.forEach(chatMessage);
        }
        $("#titleText").text("聊天室(在线" + jsonMsg.count + ")");
    }else {
        alert(jsonMsg.msg);
    }
}


function sendMessage() {
    if (ws){
        var messageObj = $("#message");
        if (messageObj.val() == ''){
            return;
        }
        var sessionId = localStorage.getItem("sessionId");
        var groupId = localStorage.getItem("groupId");
        var msg = {msg:messageObj.val(),command:"10",sessionId:sessionId || '',groupId:groupId||''};
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