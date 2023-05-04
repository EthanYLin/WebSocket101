
// 配置socket.io的地址与端口
var socket = io('http://localhost:9092');

// 连接成功处理
socket.on('connect', function() {
    document.getElementById('msg').innerHTML += 'Connected to server.<br>';
});

// 收到消息处理
socket.on('chat', function(data) {
    document.getElementById('msg').innerHTML += `message from ${data.userName} : ${data.message}<br>`
});

function sendMessage() {
    var message = "test-message";
    socket.emit('chat', { userName: "test-user", message: message });
}