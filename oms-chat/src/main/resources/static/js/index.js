var serverUrl = "47.75.15.228:8888";
// var serverUrl = "localhost:8888";
var webSocketUrl = serverUrl + "/websocket/chat";
var ws;
function initWebsocket() {
    if (window.WebSocket){
        ws = new WebSocket("ws:" + webSocketUrl);
        ws.onopen = function (event) {
            var sessionId = localStorage.getItem("sessionId");
            var msg = {command:"1",sessionId:sessionId || ''};
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
            chatMessageDispatch(jsonMsg.msg,'true');
            break;
        // ONLINE_EVENT,OFFLINE_EVENT
        case "20":
        case "21":
            onlineOfflineNotifyMessage(jsonMsg);
            break;
        case "31":
            queryChatHistoryResp(jsonMsg);
            break;
    }
}

function createChatMessageDiv(jsonMsg) {
    var label = jsonMsg.self ? "rightMessageLabel" : "leftMessageLabel";
    var chatData = "<div class='"+label+"'>";
    chatData += "<span class='nickNameClass' sessionId='"+jsonMsg.sessionId+"' nickName='"+jsonMsg.nickName+"' onclick='createChatRoom(this);'>" + jsonMsg.nickName + ":</span>";
    chatData += "<span class='msgClass'>" + jsonMsg.msg + "</span>";
    chatData += "<span class='dateTimeClass'>(<label title='" + jsonMsg.date + "'>" + jsonMsg.time + "</label>)</span>";
    chatData += "</div>";
    return chatData;
}

function chatMessageDispatch(jsonMsg,isRealTimeChat) {
    var chatMessageDiv = $(createChatMessageDiv(jsonMsg));
    publicOrChatBoxMessage(jsonMsg.groupId,chatMessageDiv);

    if (isRealTimeChat && isRealTimeChat == 'true' && !jsonMsg.self){
        var data = "<div style='cursor: pointer;' sessionId='"+jsonMsg.sessionId+"' nickName='"+jsonMsg.nickName+"' onclick='createChatRoom(this);'>你有新的消息,点击查看</div>";
        layer.msg(data, {
            id:"1",
            offset: 't',
            anim: 6,
            time: 7000
        });
    }
}

function queryChatHistoryResp(jsonMsg) {
    if (jsonMsg.success){
        if (jsonMsg.msg && jsonMsg.msg.length > 0){
            jsonMsg.msg.forEach(chatMessageDispatch);
        }
    }else {
        alert(jsonMsg.msg);
    }
}

function onlineOfflineNotifyMessage(jsonMsg) {
    if (jsonMsg.success){
        $("#titleText").text("聊天室(在线" + jsonMsg.count + ")");
        var tipMsg = "[" + jsonMsg.msg.nickName + "]" + (jsonMsg.command == "21" ? "退出房间" : "进入房间");
        var messageDiv = $("<div class='onlineOfflineTip'>" + tipMsg + "</div>");
        publicOrChatBoxMessage(jsonMsg.msg.groupId,messageDiv);
        showCurrentOnlineUser(jsonMsg.ext);
    }
}


function publicOrChatBoxMessage(groupId,chatMessageDiv) {
    var groupEle = $("#" + groupId);
    if(groupEle.length > 0){
        // public room
        $(chatMessageDiv).appendTo(groupEle);
        groupEle.scrollTop(groupEle[0].scrollHeight);
    }else {
        if (layerObj){
            var iframeWin = window[layerObj.find('iframe')[0]['name']];
            //得到iframe页的窗口对象，执行iframe页的方法：iframeWin.method();
            iframeWin.chatMessage(chatMessageDiv);
        }
    }
}


function initWebSocketResult(jsonMsg) {
    if (jsonMsg.success){
        localStorage.setItem("sessionId",jsonMsg.sessionId);
        localStorage.setItem("groupId",jsonMsg.groupId);
        $("div.content").attr("id",jsonMsg.groupId);

        if (jsonMsg.msg && jsonMsg.msg.length > 0){
            jsonMsg.msg.forEach(chatMessageDispatch);
        }
        $("#titleText").text("聊天室(在线" + jsonMsg.count + ")");

        showCurrentOnlineUser(jsonMsg.ext);

    }else {
        alert(jsonMsg.msg);
    }
}

function showCurrentOnlineUser(onlineArray) {
    if (onlineArray && onlineArray.length > 0){
        var divs = "";
        for (var i = 0; i < onlineArray.length; i++){
            var div = "<div class='onlineLabel' sessionId='"+onlineArray[i].sessionId+"' nickName='"+onlineArray[i].nickName+"' title='当前在线' onclick='createChatRoom(this);'>";
            div += onlineArray[i].nickName;
            div += "</div>";
            divs += div;
        }
        var onlineContent = $("div.onlineInfoContent");
        onlineContent.html('');
        $(divs).appendTo(onlineContent);
    }
}


function sendWebSocketMessage(message) {
    if (ws){
        ws.send(JSON.stringify(message));
    }
}

function sendMessage() {
    var messageObj = $("#message");
    if (messageObj.val() == ''){
        return;
    }
    var sessionId = localStorage.getItem("sessionId");
    var groupId = $("div.content").attr("id");
    var msg = {msg:messageObj.val(),command:"10",sessionId:sessionId || '',groupId:groupId||''};
    messageObj.val('');
    sendWebSocketMessage(msg);
}


document.onkeydown = function(event){
    var e = event || window.event;
    if(e && e.keyCode == 13){
        sendMessage();
    }
};
initWebsocket();