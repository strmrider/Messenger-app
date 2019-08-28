package com.example.messenger.models.chats;

import com.example.messenger.models.Contacts.Contact;
import com.example.messenger.models.DataModel.DataModel;
import com.example.messenger.models.messages.LastMessage;
import com.example.messenger.models.requests.RequestType;

import java.util.ArrayList;

public class GroupChat extends Chat {

    private GroupMembersList members;

    // light version, holds only name and display name
    public GroupChat(String username, String displayName)
    {
        super(username, displayName, null, true, 0, false);
        members = null;
    }
    // new group from server
    public GroupChat(String username, String displayName, GroupMembersList members)
    {
        super(username, displayName, null, true, 0, false);
        this.members = members;
    }

    // new created group
    public GroupChat(String username, String displayName, ArrayList<Contact> newMembers)
    {
        super(username, displayName, null, true, 0, false);
        members = new GroupMembersList(DataModel.getUsername(), newMembers);
    }

    public GroupChat(String username, String displayName, LastMessage lastMessage,
                     boolean group, int unread, String serializedMembers, boolean silenced)
    {
        super(username, displayName, lastMessage, group, unread, silenced);
        members = new GroupMembersList(serializedMembers);
    }

    public GroupMembersList getMembers()
    {
        return members;
    }

    public boolean isMemberAdmin(String username)
    {
        return members.isMemberAdmin(username);
    }

    private String serializeGivenMembers(ArrayList<String> membersList)
    {
        StringBuilder serialization = new StringBuilder("");
        String str;

        for(int i=0; i<membersList.size(); i++)
        {
            str = "<member admin='false'>" + membersList.get(i) + "</member>";
            serialization.append(str);
        }

        return serialization.toString();
    }


    public void addMembers(ArrayList<Contact> newMembers, boolean asAdmin)
    {
        members.addMembers(newMembers, asAdmin);
    }

    public void addMembers(ArrayList<String> newMembers)
    {
        members.addMembers(newMembers);
    }

    public void removeMembers(ArrayList<String> removedMembers)
    {
        members.removeMembers(removedMembers);
    }

    public void update(GroupChatUpdate groupChatUpdate)
    {
        if (groupChatUpdate.getType() == RequestType.GROUP_MEMBER_REMOVAL)
            members.eliminate(groupChatUpdate.getMembersList());
        else if (groupChatUpdate.getType() == RequestType.GROUP_MEMBER_ADDITION)
            members.addMembers(groupChatUpdate.getMembersList());
        else if (groupChatUpdate.getType() == RequestType.GROUP_ADMIN_APPOINTMENT)
        {
            members.appointAdmins(groupChatUpdate.getMembersList().getList(true));
        }
    }

    public String serializeForDB()
    {
        return members.serializeForDB();
    }

    // invite user to the group

    public String invitationRequest(ArrayList<Contact> invitedList)
    {
        ArrayList<String> invited = new ArrayList<>(invitedList.size());
        for(int i=0; i<invitedList.size(); i++)
            invited.add(invitedList.get(i).getUsername());

        return invitationRequest(invited, false);
    }

    public String invitationRequest(ArrayList<String> invitedList, boolean asAdmin)
    {
        return "<request type='" + RequestType.GROUP_INVITATION.getValue() + "'>" +
                "<sender>" + DataModel.getUsername() + "</sender>" +
                "<members size='"+invitedList.size()+"'>" +
                serializeGivenMembers(invitedList) +
                "</members>" +
                "<group>" +
                "<username>" + getUsername() + "</username>" +
                "<display>" + getDisplayName() + "</display>" +
                //members.serialize() +
                "</group>" +
                "</request>";
    }

    public String invitationRequest(String username, boolean asAdmin)
    {
        ArrayList<String> invitedList = new ArrayList<>();
        invitedList.add(username);
        return invitationRequest(invitedList, asAdmin);
    }

    // remove use from group's member list (including self user)
    public String removeMembersRequest(String removedUsername)
    {
        ArrayList<String> removedList = new ArrayList<>();
        removedList.add(removedUsername);
        return removeMembersRequest(removedList);
    }

    public String removeMembersRequest(ArrayList<String> removedMembers)
    {
        return "<request type='" + RequestType.GROUP_MEMBER_REMOVAL.getValue() + "'>" +
                "<sender>" + DataModel.getUsername() + "</sender>" +
                "<members size='"+removedMembers.size()+"'>" +
                serializeGivenMembers(removedMembers) +
                "</members>" +
                "<group>" +
                "<username>" + getUsername() + "</username>" +
                "</group>" +
                "</request>";
    }

    // make another member to group's admin
    public String appointAdminRequest(ArrayList<String> appointedMembers)
    {
        return "<request type='" + RequestType.GROUP_ADMIN_APPOINTMENT.getValue() + "'>" +
                "<sender>" + DataModel.getUsername() + "</sender>" +
                "<members size='"+appointedMembers.size()+"'>" +
                serializeGivenMembers(appointedMembers) +
                "</members>" +
                "<group>" +
                "<username>" + getUsername() + "</username>" +
                "</group>" +
                "</request>";
    }

    public String repr()
    {
        return  "<groupChat>" +
                "<username>" + getUsername() + "</username>" +
                "<display>" + getDisplayName() + "</display>" +
                members.serialize(true) +
                "</group>" +
                "</request>";
    }
}
