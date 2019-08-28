package com.example.messenger.models.others;

import com.example.messenger.models.Contacts.ContactUpdate;
import com.example.messenger.models.Contacts.ContactsList;

public class Profile {
    private String username;
    private String status;
    private ContactsList contactsList;

    public Profile(String username, String status, ContactsList contactsList)
    {
        this.username = username;
        this.status = status;
        contactsList = null;
    }

    public Profile(String username, String status)
    {
        this.username = username;
        this.status = status;
        contactsList = null;
    }

    public String getUsername()
    {
        return username;
    }

    public void setContactsList(ContactsList contactsList)
    {
        this.contactsList = contactsList;
    }

    public ContactsList getContactsList()
    {
        return contactsList;
    }

    public ContactUpdate refreshContactsList()
    {
        return contactsList.refresh();
    }

    public String getStatus()
    {
        return status;
    }

    public void setStatus(String status)
    {
        this.status = status;
    }
}
