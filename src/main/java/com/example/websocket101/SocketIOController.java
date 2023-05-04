package com.example.websocket101;

import com.corundumstudio.socketio.SocketIOClient;
import com.corundumstudio.socketio.SocketIOServer;
import com.corundumstudio.socketio.annotation.OnConnect;
import com.corundumstudio.socketio.annotation.OnDisconnect;
import com.corundumstudio.socketio.annotation.OnEvent;
import org.apache.commons.collections4.BidiMap;
import org.apache.commons.collections4.bidimap.DualHashBidiMap;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.ModelAndView;

import java.util.HashMap;
import java.util.Map;
import java.util.Objects;
import java.util.UUID;
import java.util.stream.Collectors;

@Controller
public class SocketIOController {

    @Autowired
    private SocketIOServer server;

    //存储SessionID和UserName的映射关系
    BidiMap<UUID, String> clientMap = new DualHashBidiMap<>();

    //TODO: 实际由UserService提供该服务
    private String getUserNameFromUid(Long uid){
        return "user" + uid;
    }

    private ChatObject errorMsg(String userName, String message){
        return new ChatObject("system", ChatObject.Type.PRIVATE, userName, message);
    }

    private void log_client(SocketIOClient client, String msg){
        System.out.println("-------START------");
        System.out.println("Client: " + clientMap.get(client.getSessionId()));
        System.out.println("SessionID: " + client.getSessionId());
        System.out.println("Msg:" + msg);
        System.out.println("Rooms: " + String.join(", ", client.getAllRooms()));
        System.out.println("-------END------");
    }

    @OnConnect
    public void onConnect(SocketIOClient client) {
        String uid = client.getHandshakeData().getSingleUrlParam("uid");
        String userName = getUserNameFromUid(Long.parseLong(uid));
        //TODO: check if userName is null
        clientMap.put(client.getSessionId(), userName);
        log_client(client, "connected.");
    }

    @OnDisconnect
    public void onDisconnect(SocketIOClient client) {
        System.out.println("Client disconnected: " + client.getSessionId());
    }

    @OnEvent("join")
    public void onJoin(SocketIOClient client, String roomName) {
        client.joinRoom(roomName);
        log_client(client, "join " + roomName);
    }

    @OnEvent("leave")
    public void onLeave(SocketIOClient client, String roomName) {
        client.leaveRoom(roomName);
        log_client(client, "leave " + roomName);
    }

    @OnEvent("chat")
    public void onChat(SocketIOClient client, ChatObject chatObject) {
        String from = clientMap.get(client.getSessionId());
        log_client(client, "msg " + chatObject.getType() + " to " + chatObject.getTo() + " : " + chatObject.getMessage());

        if(chatObject.isSomeNull()){
            log_client(client, "some properties of ChatObject is null");
            client.sendEvent("chat", errorMsg(from, "some properties of ChatObject is null"));
            return;
        }
        if(! from.equals(chatObject.getFromUserName())){
            log_client(client, "fromUserName in ChatObject is not equal to the one in Session");
            client.sendEvent("chat", errorMsg(from, "fromUserName in ChatObject is not equal to the one in Session"));
            return;
        }
        if(chatObject.getType() == ChatObject.Type.ROOM && ! client.getAllRooms().contains(chatObject.getTo())){
            log_client(client, "you are not in this room");
            client.sendEvent("chat", errorMsg(from, "you are not in this room"));
            return;
        }
        if(chatObject.getType() == ChatObject.Type.PRIVATE && ! clientMap.containsValue(chatObject.getTo())){
            log_client(client, "the user you want to chat with is not online");
            client.sendEvent("chat", errorMsg(from, "the user you want to chat with is not online"));
            return;
        }

        switch (chatObject.getType()) {
            case PRIVATE -> server.getClient(clientMap.getKey(chatObject.getTo())).sendEvent("chat", chatObject);
            case ROOM -> server.getRoomOperations(chatObject.getTo()).sendEvent("chat", chatObject);
            case BROADCAST -> server.getBroadcastOperations().sendEvent("chat", chatObject);
        }

    }

}
