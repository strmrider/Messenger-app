package com.example.messenger.models.messages;

abstract public class MessageBody {
    MessageType type;

    MessageBody (MessageType type)
    {
        this.type = type;
    }
    public  MessageType getType()
    {
        return type;
    }

    abstract public String getText();
    abstract public String xml();
}