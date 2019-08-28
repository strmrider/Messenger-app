package com.example.messenger.models.messages;

public class MessageStatusUpdate {
    public String id;
    public MessageStatus status;
    public String chat;

    public MessageStatusUpdate(String id, String chat, MessageStatus status)
    {
        this.id = id;
        this.chat = chat;
        this.status = status;
    }
}
