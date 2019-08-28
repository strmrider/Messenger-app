package com.example.messenger.models.requests;


import com.example.messenger.models.chats.GroupChat;
import com.example.messenger.models.chats.GroupChatUpdate;
import com.example.messenger.models.chats.GroupMembersList;

import org.w3c.dom.Node;

class GroupRequests extends Request {

    GroupRequests() {}

    private GroupMembersList parseMembers(org.w3c.dom.Document xmlDoc)
    {
        GroupMembersList membersList = new GroupMembersList();
        int size = Integer.parseInt(
                xmlDoc.getElementsByTagName("members").item(0).getAttributes().item(0).getTextContent());
        Node node;
        boolean isAdmin;

        for(int i=0; i<size; i++)
        {
            node = xmlDoc.getElementsByTagName("member").item(i);
            isAdmin = Boolean.valueOf(node.getAttributes().item(0).getTextContent());
            membersList.add(node.getTextContent(), isAdmin);
        }

        return membersList;
    }

    GroupChat groupInvitation(String request)
    {
        org.w3c.dom.Document xmlDoc;
        try {
            xmlDoc = loadXMLFromString(request);
            String username = xmlDoc.getElementsByTagName("username").item(0).getTextContent();
            String display = xmlDoc.getElementsByTagName("display").item(0).getTextContent();
            return new GroupChat(username, display, parseMembers(xmlDoc));
        }
        catch(Exception e)
        {
            e.printStackTrace();
        }
        return  null;
    }

    GroupChatUpdate updateGroupMembers(String request)
    {
        org.w3c.dom.Document xmlDoc;
        try {
            xmlDoc = loadXMLFromString(request);
            String username = xmlDoc.getElementsByTagName("username").item(0).getTextContent();
            RequestType type = RequestType.getTypePerInt(Integer.parseInt(xmlDoc.getElementsByTagName
                    ("request").item(0).getAttributes().item(0).getTextContent()));

            return new GroupChatUpdate(username, parseMembers(xmlDoc), type);
        }
        catch(Exception e)
        {
            e.printStackTrace();
        }
        return  null;
    }
}
