package com.example.messenger.models.messages;

import com.example.messenger.models.DataModel.DataModel;

public class Message {
    public String id;
    public String sender;
    public String recipient;
    public MessageDate sendingDate;
    public MessageDate receivingDate;
    MessageBody body;
    public boolean fromGroup;
    public MessageStatus status;

    public String actualContactPerson; // the contact in the contact list

    public  Message(){};

    public Message(String id,
                   String sender,
                   String recipient,
                   MessageBody body,
                   MessageDate sendingDate,
                   MessageDate receivingData,
                   MessageStatus status,
                   boolean fromGroup)
    {
        this.id = id;
        this.sender = sender;
        this.recipient = recipient;
        this.sendingDate = sendingDate;
        this.receivingDate = receivingData;
        castBody(body);
        this.status = status;
        this.fromGroup = fromGroup;
    }

    protected void castBody(MessageBody body)
    {
        switch (body.type)
        {
            case TEXT:
                this.body = (TextMessage)body;
                break;
            case IMAGE:
                this.body = (MediaMessage)body;
                break;
        }
    }

     boolean isGroup(String username) {
        return (username.length() >= 3 && username.charAt(0) == '0' && username.charAt(1) == '0'
                && username.charAt(2) == '0');
    }

    public MessageType getType()
    {
        return body.type;
    }

    public MessageBody getBody()
    {
        return body;
    }

    public LastMessage toLastMessage()
    {
        return new LastMessage(id, sender, body, sendingDate, status, fromGroup);
    }

    public SentMessage toSentMessage()
    {
        return new SentMessage(id, sender, recipient, body, sendingDate);
    }

    public SentMessage toSentMessage(String recipient)
    {
        return new SentMessage(id, DataModel.getUsername(), recipient, body, sendingDate);
    }

    public void sample()
    {
        id = "123";
        sender = "sender1";
        recipient = "recipient1";
        sendingDate = new MessageDate();
        receivingDate = new MessageDate();
        body = (TextMessage)new TextMessage("text1");
        fromGroup = false;
        status = MessageStatus.PENDING;
        actualContactPerson = recipient;
    }

    public String state()
    {
        return "<IncomeMessage>" +
                "<id>" + id +"<id>" +
                "<recipient>" + recipient + "</recipient>" +
                "<sendingDate>" + sendingDate.getDate() + "</sendingDate>" +
                "<body>" +  body.getText() +"</body>" +
                "<group>" + fromGroup + "</group>" +
                "<status>" + status + "</status>";
    }

    public String messageText()
    {
        return body.getText();
    }
}