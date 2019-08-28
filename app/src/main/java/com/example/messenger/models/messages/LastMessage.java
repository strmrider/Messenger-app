package com.example.messenger.models.messages;

public class LastMessage extends Message {

    LastMessage(String id, String sender, MessageBody body, MessageDate sendingDate,
                MessageStatus status, boolean isGroup)
    {
        this.id = id;
        this.sender = sender;
        this.status = status;
        this.body = body;
        this.sendingDate = sendingDate;
        this.fromGroup = isGroup;
    }

    public String getText()
    {
        switch (getType())
        {
            case TEXT:
                return body.getText();
            case IMAGE:
                return "Image";
            case AUDIO:
                return "Audio";
            case VIDEO:
                return  "Video";
            default:
                return "";
        }
    }
}