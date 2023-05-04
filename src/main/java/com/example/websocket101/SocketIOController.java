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

    private void log_client(SocketIOClient client){
        System.out.println("-------START------");
        System.out.println("Client: " + client.getSessionId());
        System.out.println("Namespace: " + client.getNamespace().getName());
        System.out.println("Rooms: " + String.join(", ", client.getAllRooms()));
        System.out.println("-------END------");
    }

    @OnConnect
    public void onConnect(SocketIOClient client) {
        log_client(client);
        // System.out.println("Client connected: " + client.getSessionId());
    }

    @OnDisconnect
    public void onDisconnect(SocketIOClient client) {
        System.out.println("Client disconnected: " + client.getSessionId());
    }

    @OnEvent("chat")
    public void onChat(SocketIOClient client, ChatObject chatObject) {
        System.out.println("Received chat message: " + chatObject.getMessage());
        server.getBroadcastOperations().sendEvent("chat", chatObject);
    }

}
