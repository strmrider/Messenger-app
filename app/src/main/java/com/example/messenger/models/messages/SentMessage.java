package com.example.messenger.models.messages;

import com.example.messenger.models.requests.RequestType;

import java.io.UnsupportedEncodingException;

public class SentMessage extends Message {

    public SentMessage(){};

    public SentMessage (String id,
                        String sender,
                        String recipient,
                        MessageBody body,
                        MessageDate date)
    {
        this.id = id;
        this.sender = sender;
        this.recipient = recipient;
        this.sendingDate = date;
        this.receivingDate = date;
        castBody(body);
        this.status = MessageStatus.PENDING;
        actualContactPerson = recipient;
        this.fromGroup = isGroup(recipient);
    }

    private String getSizeFormat(String msg)
    {
        int size = msg.length();
        String sizeFormat = "";
        String sizeInStr = Integer.toString(size);
        for(int i=0; i<(5-sizeInStr.length()); i++)
            sizeFormat += "0";

        sizeFormat += sizeInStr;
        return sizeFormat;
    }

    public String xml() {
        String s =  "<request type='"+ RequestType.NEW_MESSAGE.getValue()+"'>" +
                "<message type='" + MessageType.getTypeAsInt(body.getType()) + "'>" +
                "<id>" + id + "</id>" +
                "<sender>" + sender + "</sender>"+
                "<recipient>" + recipient + "</recipient>" +
                "<sent>" + sendingDate.getDate() + "</sent>" +
                this.body.xml() +
                "</message>" +
                "</request>";

        if (getType() != MessageType.TEXT)
        {
            MediaMessage mediaMessage = (MediaMessage)body;
            String xmlSize = getSizeFormat(s);
            try {
                s += new String(mediaMessage.getMedia(), "ISO-8859-1");
            }
            catch (UnsupportedEncodingException e)
            {
                s += "XXXXXX";
                e.printStackTrace();
            }
            s = ("M"+xmlSize + s);
        }
        return s;
    }
}