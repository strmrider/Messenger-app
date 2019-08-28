package com.example.messenger.models.chats;


import com.example.messenger.models.Contacts.Contact;

public class GroupMember {
    public String username;
    public boolean isAdmin;
    public Contact contact;

    public GroupMember(String username, boolean isAdmin)
    {
        this.username = username;
        this.isAdmin = isAdmin;
    }
}
