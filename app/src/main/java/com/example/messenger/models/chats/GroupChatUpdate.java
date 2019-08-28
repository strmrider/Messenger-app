package com.example.messenger.models.chats;

import com.example.messenger.models.requests.RequestType;

public class GroupChatUpdate {
    private String username;
    private GroupMembersList membersList;
    private RequestType type;

    public GroupChatUpdate(String username, GroupMembersList membersList, RequestType type)
    {
        this.username = username;
        this.membersList = membersList;
        this.type = type;
    }

    public String getUsername()
    {
        return username;
    }

    public GroupMembersList getMembersList()
    {
        return membersList;
    }

    public RequestType getType()
    {
        return type;
    }
}
