package com.example.messenger.models.requests;


import com.example.messenger.models.DataModel.DataModel;
import com.example.messenger.models.messages.IncomeMessage;
import com.example.messenger.models.messages.MediaMessage;
import com.example.messenger.models.messages.MessageBody;
import com.example.messenger.models.messages.MessageDate;
import com.example.messenger.models.messages.MessageStatus;
import com.example.messenger.models.messages.MessageStatusUpdate;
import com.example.messenger.models.messages.MessageType;
import com.example.messenger.models.messages.TextMessage;

import org.w3c.dom.Node;

class MessagesRequests extends Request{
    private BinaryRequest binaryRequest;
    private org.w3c.dom.Document xmlDoc;

    MessagesRequests(){}

    private MessageBody setMessageBody(MessageType type, String text)
    {
        MessageBody body;
        if(type == MessageType.TEXT)
            body = new TextMessage(text);
        else {
            String extension = xmlDoc.getElementsByTagName("extension").item(0).getTextContent();
            body = new MediaMessage(type, text, extension, binaryRequest.getBinarySegment());
        }

        return body;
    }


    IncomeMessage incomeMessage(BinaryRequest binaryRequest)
    {
        this.binaryRequest = binaryRequest;
        try {
            xmlDoc = loadXMLFromString(binaryRequest.getRequestSegment());
            return parseIncomeMessage();
        }
        catch (NullPointerException e)
        {
            e.printStackTrace();
        }
        catch (Exception e)
        {
            e.printStackTrace();
        }
        return null;
    }

    IncomeMessage incomeMessage(String request)
    {
        try {
            xmlDoc = loadXMLFromString(request);
            return parseIncomeMessage();
    }
        catch (NullPointerException e)
        {
            e.printStackTrace();
        }
        catch (Exception e)
        {
            e.printStackTrace();
        }
        return null;
    }

    private IncomeMessage parseIncomeMessage()
    {
        String id = xmlDoc.getElementsByTagName("id").item(0).getTextContent();
        String sender = xmlDoc.getElementsByTagName("sender").item(0).getTextContent();
        String recipient = xmlDoc.getElementsByTagName("recipient").item(0).getTextContent();
        Node node = xmlDoc.getElementsByTagName("message").item(0);
        MessageType type = MessageType.getTypePerInt(
                Integer.parseInt(node.getAttributes().item(0).getTextContent()));
        String text = xmlDoc.getElementsByTagName("text").item(0).getTextContent();
        MessageDate sendingDate =
                new MessageDate(xmlDoc.getElementsByTagName("sent").item(0).getTextContent());
        MessageDate receivingDate = new MessageDate();

        return new IncomeMessage(id, sender, recipient, setMessageBody(type, text), sendingDate,
                receivingDate, MessageStatus.INCOME, !DataModel.getUsername().equals(recipient));
    }

    MessageStatusUpdate messageStatus(String request)
    {
        org.w3c.dom.Document xmlDoc;
        try {
            xmlDoc = loadXMLFromString(request);
            MessageStatus status = MessageStatus.getPerRequest(Integer.parseInt
                    (xmlDoc.getElementsByTagName("status").item(0).getTextContent()));
            String id = xmlDoc.getElementsByTagName("id").item(0).getTextContent();
            String chat = xmlDoc.getElementsByTagName("chat").item(0).getTextContent();

            return new MessageStatusUpdate(id, chat, status);
        }
        catch(Exception e)
        {
            e.printStackTrace();
        }
        return  null;
    }
}
