package com.example.messenger.models.Contacts;


import com.example.messenger.models.requests.RequestType;

public class ContactUpdate {
    public ContactsEnum.UpdateOperation operation;
    public String displayName;
    public String secondString;
    public String contactsUsername;

    ContactUpdate(ContactsEnum.UpdateOperation operation, String secondString, String contactsUsername)
    {
        this.operation = operation;
        this.secondString = secondString;
        this.contactsUsername = contactsUsername;
    }


    public String getRequest()
    {
        String xml = "<request type='" + RequestType.CONTACTS_LIST_UPDATE.getValue() + "'>" +
                "<operation>" + operation.getValue() + "</operation>";
        xml += "<contact>" + contactsUsername + "</contact>";
        if (operation == ContactsEnum.UpdateOperation.CONTACTS_REPLACED)
            xml += "<former>" + secondString + "</former>";
        xml += "</request>";
        return xml;
    }
}
