package com.example.messenger.models.requests;

import com.example.messenger.models.Contacts.Contact;

import java.util.ArrayList;

class ContactsUpdateRequests extends Request {

    ContactsUpdateRequests() {}

    void parseProfileImageUpdate(String request)
    {
        BinaryRequest binaryRequest = new BinaryRequest(request);
        String xmlSegment = binaryRequest.getRequestSegment();

        try {
            org.w3c.dom.Document xmlDoc = loadXMLFromString(xmlSegment);
            // save binary part on device
        }
        catch (Exception e)
        {
            e.printStackTrace();
        }
    }

    private void parseProfileImageUpdate(BinaryRequest binaryRequest)
    {
        //BinaryRequest binaryRequest = new BinaryRequest(request);
        String xmlSegment = binaryRequest.getRequestSegment();

        try {
            org.w3c.dom.Document xmlDoc = loadXMLFromString(xmlSegment);
            // save binary part on device
        }
        catch (Exception e)
        {
            e.printStackTrace();
        }
    }

    Contact parseStatusUpdate(String request)
    {
        try {
            org.w3c.dom.Document xmlDoc = loadXMLFromString(request);
            String username = xmlDoc.getElementsByTagName("username").item(0).getTextContent();
            String status = xmlDoc.getElementsByTagName("status").item(0).getTextContent();
            return new Contact(username, null, status);
        }
        catch (Exception e)
        {
            e.printStackTrace();
        }

        return null;
    }

    Contact parseFullProfileUpdate(BinaryRequest binaryRequest)
    {
        parseProfileImageUpdate(binaryRequest);
        return parseStatusUpdate(binaryRequest.getRequestSegment());
    }

    ArrayList<Contact> parseRegisteredContacts(String request)
    {

        try {
            org.w3c.dom.Document xmlDoc = loadXMLFromString(request);
            int size = Integer.parseInt(
                    xmlDoc.getElementsByTagName("contacts").item(0).getAttributes().item(0).getTextContent());
            ArrayList<Contact> contacts = new ArrayList<>();
            for (int i=0; i<size; i++)
            {
                contacts.add(new Contact(
                        xmlDoc.getElementsByTagName("username").item(i).getTextContent(),
                        "",
                        xmlDoc.getElementsByTagName("status").item(i).getTextContent()));
            }
            return contacts;

        }
        catch (Exception e)
        {
            e.printStackTrace();
        }

        return null;
    }
}
