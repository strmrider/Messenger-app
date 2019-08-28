package com.example.messenger.models.Contacts;

public class Contact {
    private String username;
    private String displayName;
    private String status;

    public Contact (String username, String displayName, String status)
    {
        this.username = username;
        this.displayName = displayName;
        this.status = status;
    }

    public Contact (String username, String displayName)
    {
        this.username = username;
        this.displayName = displayName;
    }

    public Contact (String username)
    {
        this.username = username;
        this.displayName = username;
        status = "Unavailable";
    }

    public String getUsername() {
        return username;
    }

    public String getDisplayName() {
        return displayName;
    }

    public String getStatus()
    {
        return status;
    }

    public void setUsername(String username)
    {
        this.username = username;
    }

    public void setDisplayName(String displayName)
    {
        this.displayName = displayName;
    }

    public void setStatus(String status)
    {
        this.status = status;
    }

     String serialize()
    {
        return "<contact>" + username + "<contact>";
    }

     boolean cmp(Contact contact)
    {
        return (!username.equals(contact.getUsername()) ||
                !displayName.equals(contact.getDisplayName()));
    }

    public void copy(Contact contact)
    {
        if (contact != null) {
            username = contact.getUsername();
            displayName = contact.displayName;
            status = contact.getStatus();
        }
    }
}
