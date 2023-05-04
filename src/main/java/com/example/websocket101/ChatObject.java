package com.example.websocket101;

public class ChatObject {

    private String userName;
    private String message;
    private String roomName;

    public String getRoomName() {
        return roomName;
    }
    public void setRoomName(String roomName) {
        this.roomName = roomName;
    }
    public String getUserName() {
        return userName;
    }
    public void setUserName(String userName) {
        this.userName = userName;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public ChatObject() {
    }

    public ChatObject(String userName, String message, String roomName) {
        this.userName = userName;
        this.message = message;
        this.roomName = roomName;
    }


    @Override
    public String toString() {
        return String.format("ChatObject{userName='%s', message='%s', room='%s'}",
                userName, message, roomName);
    }
}
