var httpRequestUrl = "http://" + serverUrl;
var layerIndex;
var layerObj;
function createChatRoom(ele) {
    var toSessionId = $(ele).attr("sessionId");
    var sessionId = localStorage.getItem("sessionId");
    var data = {sessionId:sessionId||'',toSessionId:toSessionId||'',command:"40"};
    $.post(httpRequestUrl + "/room/create",JSON.stringify(data),function (resp) {
        if (!resp.success){
            alert(resp.msg);
            return;
        }
        layer.open({
            id:"GROUP" + resp.msg,
            type: 2,
            title: $(ele).text(),
            maxmin: true,
            shade: false,
            area: ['600px', '400px'],
            zIndex: layer.zIndex,
            content: ['../static/chat_window.html','no'],
            success:function (layero, index) {
                // 打开窗口后回调
                // console.info(ws); 查询聊天记录
                // layer.setTop(layero); // 再次点击时置顶
                layerIndex = index;
                layerObj = layero;

                var iframeWin = window[layero.find('iframe')[0]['name']];
                //得到iframe页的窗口对象，执行iframe页的方法：iframeWin.method();
                data["groupId"] = resp.msg;
                iframeWin.initSessionGroupId(data);
            },
            end: function(){
                // 关闭时会触发
                // layer.tips('Hi', '#about', {tips: 1});
                layerIndex = null;
            }
        });
    });
}

