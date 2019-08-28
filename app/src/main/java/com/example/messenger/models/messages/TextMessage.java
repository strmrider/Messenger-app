package com.example.messenger.models.messages;

public class TextMessage extends MessageBody {
    private String text;

    public TextMessage(String text)
    {
        super(MessageType.TEXT);
        this.text = text;
    }

    public String getText()
    {
        return text;
    }

    public String xml()
    {
        return "<body><text>" + text + "</text></body>";
    }
}
