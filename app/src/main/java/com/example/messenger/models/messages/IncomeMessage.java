package com.example.messenger.models.messages;

import com.example.messenger.models.DataModel.DataModel;
import com.example.messenger.models.requests.RequestType;

public class IncomeMessage extends Message {

    public IncomeMessage(){};

    public IncomeMessage(String id,
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

        // contact person would be the group rather than the actual sender
        if(fromGroup)
            actualContactPerson = recipient;
        else
            actualContactPerson = sender;
    }

    public void writeToFile()
    {
        if  (getType() != MessageType.TEXT)
        {
            MediaMessage mediaMessage = (MediaMessage)body;
            mediaMessage.writeToFile(id + "." + mediaMessage.getExtension());
        }
    }

    public String approvalRequest()
    {
        return "<request type='" + RequestType.MESSAGE_RECEIVED_APPROVAL.getValue() + "'>" +
                "<id>" + id + "</id>" +
                "<sender>" + DataModel.getUsername() + "</sender>" +
                "<recipient>"+ sender +"</recipient>" +
                "<received>"+receivingDate.getDate()+"</received>" +
                "</request>";
    }

}