
// 配置socket.io的地址与端口
var socket = io('http://localhost:9092');

// 连接成功处理
socket.on('connect', function() {
    document.getElementById('msg').innerHTML += 'Connected to server.<br>';
});

// 收到消息处理
socket.on('roomChat', function(data) {
    document.getElementById('msg').innerHTML += `room chat from ${data.userName} : ${data.message}<br>`
});
socket.on('broadcast', function(data) {
    document.getElementById('msg').innerHTML += `broadcast from ${data.userName} : ${data.message}<br>`
});


// 发送消息到房间
function sendMessageToRoom() {
    let userName = document.getElementById('username').value;
    let roomName = document.getElementById('room').value;
    let message = "room-msg-" + new Date().toLocaleTimeString();
    socket.emit('roomChat', { userName: userName, message: message, roomName: roomName });
}

// 发送广播消息
function sendBroadcast() {
    let userName = document.getElementById('username').value;
    let message = "broadcast-" + new Date().toLocaleTimeString();
    socket.emit('broadcast', { userName: userName, message: message, roomName: 'broadcast' });
}

// 加入房间
function joinRoom(){
    let roomName = document.getElementById('room').value;
    socket.emit('join', {roomName: roomName});
}

// 离开房间
function leaveRoom(){
    let roomName = document.getElementById('room').value;
    socket.emit('leave', {roomName: roomName});
}