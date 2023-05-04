package com.example.websocket101;

import com.corundumstudio.socketio.SocketIOClient;
import com.corundumstudio.socketio.SocketIOServer;
import com.corundumstudio.socketio.annotation.OnConnect;
import com.corundumstudio.socketio.annotation.OnDisconnect;
import com.corundumstudio.socketio.annotation.OnEvent;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.ModelAndView;

import java.util.stream.Collectors;

@Controller
public class SocketIOController {

    @Autowired
    private SocketIOServer server;

    private void log_client(SocketIOClient client, String msg){
        System.out.println("-------START------");
        System.out.println("Client: " + client.getSessionId());
        System.out.println("Msg:" + msg);
        System.out.println("Namespace: " + client.getNamespace().getName());
        System.out.println("Rooms: " + String.join(", ", client.getAllRooms()));
        System.out.println("-------END------");
    }

    @OnConnect
    public void onConnect(SocketIOClient client) {
        log_client(client, "connected.");
        // System.out.println("Client connected: " + client.getSessionId());
    }

    @OnDisconnect
    public void onDisconnect(SocketIOClient client) {
        System.out.println("Client disconnected: " + client.getSessionId());
    }

    @OnEvent("join")
    public void onJoin(SocketIOClient client, ChatObject chatObject) {
        client.joinRoom(chatObject.getRoomName());
        log_client(client, "join " + chatObject.getRoomName());
    }

    @OnEvent("leave")
    public void onLeave(SocketIOClient client, ChatObject chatObject) {
        client.leaveRoom(chatObject.getRoomName());
        log_client(client, "leave " + chatObject.getRoomName());
    }

    @OnEvent("roomChat")
    public void onRoomChat(SocketIOClient client, ChatObject chatObject) {
        System.out.println("Received from " + chatObject.getRoomName() + " message: " + chatObject.getMessage());
        server.getRoomOperations(chatObject.getRoomName()).sendEvent("roomChat", chatObject);
    }

    @OnEvent("broadcast")
    public void onBroadcast(SocketIOClient client, ChatObject chatObject) {
        System.out.println("Received broadcast message: " + chatObject.getMessage());
        server.getBroadcastOperations().sendEvent("broadcast", chatObject);
    }

}
