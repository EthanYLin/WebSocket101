
const uid = new Date().getMinutes().toString() + new Date().getSeconds().toString();
const userName = 'user' + uid;
document.getElementById("from").innerHTML = "你的用户名: " + userName;

// 配置socket.io的地址与端口
var socket = io('http://52.20.215.172:9092', {
    query: {
        "uid": uid
    }
});

// 连接成功处理
socket.on('connect', function() {
    document.getElementById('msg').innerHTML += 'Connected to server.<br>';
});

// 加入房间
function joinRoom(){
    let roomName = prompt('请输入房间名：');
    socket.emit('join', roomName);
}

// 离开房间
function leaveRoom(){
    let roomName = prompt('请输入房间名：');
    socket.emit('leave', roomName);
}

// 收到消息处理
socket.on('chat', function(data) {
    document.getElementById('msg').innerHTML += `${data.type} message from ${data.fromUserName} : ${data.message}<br>`
});

// 发送消息
function sendMessage() {
    let to = document.getElementById('to').value;
    let msgType = document.querySelector('input[name="msg-type"]:checked').value;
    let message = "room-msg-" + new Date().toLocaleTimeString();
    socket.emit('chat', {fromUserName: userName, type: msgType, to: to, message: message});
}

