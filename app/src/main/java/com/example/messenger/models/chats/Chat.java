package com.example.messenger.models.chats;


import com.example.messenger.models.messages.LastMessage;

public class Chat {
    // chat username is the phone number of the counterpart
    private String username;
    private String displayName;
    private LastMessage lastMessage;
    private int unread;
    private boolean isGroup;
    private boolean silenced;

    // light version; for temporary usage before new chat is added to chats list
    public Chat(String username, String displayName)
    {
        this.username = username;
        this.displayName = displayName;
        silenced = false;
    }

    public Chat(String username, String displayName, LastMessage lastMessage,
                boolean group, int unread, boolean silenced)
    {
        this.username = username;
        this.displayName = displayName;
        this.lastMessage = lastMessage;
        this.unread = unread;
        this.isGroup = group;
        this.silenced = silenced;
    }

    public String getUsername(){
        return username;
    }

    public String getDisplayName(){
        return displayName;
    }

    public void setUnread(int unread)
    {
        this.unread = unread;
    }

    public void setLastMessage(LastMessage message)
    {
        lastMessage = message;
    }

    public void setDisplayName(String displayName)
    {
        this.displayName = displayName;
    }

    public int getUnread()
    {
        return unread;
    }

    public LastMessage getLastMessage()
    {
        return lastMessage;
    }

    public boolean isGroup()
    {
        return isGroup;
    }

    public int compareDates(Chat chat)
    {
        if(chat.lastMessage == null)
            return 1;
        else if (lastMessage == null)
            return -1;
        else
            return lastMessage.sendingDate.compare(chat.lastMessage.sendingDate);
    }


    public void setSilenced(boolean status)
    {
        silenced = status;
    }

    public boolean isSilenced()
    {
        return silenced;
    }
}
