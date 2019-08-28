package com.example.messenger.models.requests;


import org.w3c.dom.Node;
import org.xml.sax.InputSource;

import java.io.StringReader;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;

public class RequestParser {
    private GroupRequests groupRequests;
    private MessagesRequests messagesRequests;
    private ContactsUpdateRequests contactsUpdateRequests;
    private RequestType type;

    public RequestParser()
    {
        groupRequests = new GroupRequests();
        messagesRequests = new MessagesRequests();
        contactsUpdateRequests = new ContactsUpdateRequests();
    }

    private org.w3c.dom.Document loadXMLFromString(String xml) throws Exception
    {
        DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();
        DocumentBuilder builder = factory.newDocumentBuilder();
        InputSource is = new InputSource(new StringReader(xml));
        return builder.parse(is);
    }

    private RequestType getRequestType(String request)
    {
        if (request.charAt(0) == 'M')
            //return RequestType.NEW_MESSAGE;
            return RequestType.BINARY_REQUEST;

        org.w3c.dom.Document xmlDoc;
        int action = -1;
        try{
            xmlDoc = loadXMLFromString(request);
            Node node = xmlDoc.getElementsByTagName("request").item(0);
            action = Integer.parseInt(node.getAttributes().item(0).getTextContent());
        }
        catch (Exception e)
        {
            e.printStackTrace();
        }

        return RequestType.getTypePerInt(action);
    }

    // returns last parsed type
    public RequestType getType()
    {
        return type;
    }

    public Object parseRequest(String request)
    {
        BinaryRequest binaryRequest = null;
        type = getRequestType(request);
        switch (type)
        {
            case BINARY_REQUEST:
                binaryRequest = new BinaryRequest(request);
                type = getRequestType(binaryRequest.getRequestSegment());
                if(type == RequestType.NEW_MESSAGE)
                    return  messagesRequests.incomeMessage(binaryRequest);
                else if (type == RequestType.PROFILE_IMAGE_UPDATE) {
                    // uncertain need tp be checked
                    contactsUpdateRequests.parseProfileImageUpdate(binaryRequest.toString());
                    return null;
                }
                else
                    return contactsUpdateRequests.parseFullProfileUpdate(binaryRequest);

            case NEW_MESSAGE:
                return messagesRequests.incomeMessage(request);
            case MESSAGE_SENT_APPROVAL:
            case MESSAGE_RECEIVED_APPROVAL:
            case MESSAGE_READ_APPROVAL:
                return messagesRequests.messageStatus(request);
            case GROUP_INVITATION:
                return groupRequests.groupInvitation(request);
            case GROUP_MEMBER_REMOVAL:
            case GROUP_MEMBER_ADDITION:
            case GROUP_ADMIN_APPOINTMENT:
                return groupRequests.updateGroupMembers(request);
            case STATUS_UPDATE:
                return contactsUpdateRequests.parseStatusUpdate(request);
            case CONTACTS_APPROVAL:
                return contactsUpdateRequests.parseRegisteredContacts(request);
            default:
                return null;
        }
    }
}
