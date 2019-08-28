package com.example.messenger.models.Contacts;


import com.example.messenger.view.activities.MainActivity;
import com.example.messenger.models.DataModel.DataModel;
import com.example.messenger.models.requests.RequestType;

import java.util.ArrayList;
import java.util.Hashtable;

import static com.example.messenger.models.Contacts.ContactsEnum.UpdateOperation.CONTACT_DETAILS_CHANGE;


/**
 * Created by t on 10/07/2018.
 */

public class ContactsList {
    private Hashtable<String, Contact> registeredContacts;
    private Hashtable<String, Contact> unregisteredContacts;

    private ContactsObserver contactsObserver;

    public ContactsList(ContactsEnum loadFrom)
    {
       if (loadFrom == ContactsEnum.LOAD_FROM_PHONE_CONTACTS)
           loadFromPhone();
       else if(loadFrom == ContactsEnum.LOAD_FROM_DATABASE)
           loadFromDatabase();
    }

    private void loadFromDatabase()
    {

    }

    private void loadFromPhone()
    {
        registeredContacts = new Hashtable<String, Contact>();
        unregisteredContacts =
                (new PhoneContactsExtractor(MainActivity.getContext())).toHashTable();
    }

    public void register(ArrayList<Contact> contacts)
    {
        Contact contact;
        for (int i=0; i<contacts.size(); i++)
        {
            contact = unregisteredContacts.get(contacts.get(i).getUsername());
            if (contact != null) {
                contact.setStatus(contacts.get(i).getStatus());
                registeredContacts.put(contact.getUsername(), contact);
                unregisteredContacts.remove(contact.getUsername());
            }
        }
    }

    @SuppressWarnings("unchecked")
    private static <T> String serialize(T contactsHash)
    {
        Contact contact;
        StringBuilder stringBuilder = new StringBuilder();
        Hashtable<String, Contact> contacts = (Hashtable<String, Contact>)contactsHash;
        ArrayList<Contact> contactsList = new ArrayList<>();
        contactsList.addAll(contacts.values());
        for (int i=0; i<contacts.size(); i++)
        {
            stringBuilder.append("<contact>");
            stringBuilder.append(contactsList.get(i).getUsername());
            stringBuilder.append("</contact>");
        }
        return stringBuilder.toString();
    }

    public String serialize(ContactsEnum list)
    {
        StringBuilder stringBuilder = new StringBuilder();
        if (list == ContactsEnum.REGISTERED) {
            String opener = "<contacts size='" + registeredContacts.size() + "'>";
            stringBuilder.append(opener);
            stringBuilder.append(serialize(registeredContacts));
        }
        else if ( list == ContactsEnum.UNREGISTERED) {
            String opener = "<contacts size='" + unregisteredContacts.size() + "'>";
            stringBuilder.append(opener);
            stringBuilder.append(serialize(unregisteredContacts));
        }
        else if (list == ContactsEnum.BOTH_LISTS)
        {
            String opener = "<contacts size='" + registeredContacts.size() + unregisteredContacts.size() + "'>";
            stringBuilder.append(opener);
            opener = "<registered size='" + registeredContacts.size() + "'>";
            stringBuilder.append(opener);
            stringBuilder.append(serialize(registeredContacts));
            stringBuilder.append("</registered>");

            opener = "<unregistered size='" + unregisteredContacts.size() + "'>";
            stringBuilder.append(opener);
            stringBuilder.append(serialize(unregisteredContacts));
            stringBuilder.append("</unregistered>");

        }
        else
            return null;
        stringBuilder.append("</contacts>");

        return stringBuilder.toString();
    }

    /************************************
     * Contacts list updates
     ************************************/

    private ContactUpdate updateNewContact(ArrayList<Contact> phoneContacts)
    {
        String username;
        for (int i=0; i<phoneContacts.size(); i++)
        {
            username = phoneContacts.get(i).getUsername();
            if (getContact(username) == null)
            {
                unregisteredContacts.put(username, phoneContacts.get(i));
                return new ContactUpdate(ContactsEnum.UpdateOperation.CONTACT_ADDED,
                        phoneContacts.get(i).getDisplayName(), username);
            }
        }
        return null;
    }

    private ContactUpdate updateRemoveContact(ArrayList<Contact> phoneContacts)
    {
        Hashtable<String, Contact> newRegisteredContacts = new Hashtable<String, Contact>();
        Hashtable<String, Contact> newUnregisteredContacts = new Hashtable<String, Contact>();
        Contact contact;

        for (int i=0; i<phoneContacts.size(); i++)
        {
            contact = phoneContacts.get(i);
            if (registeredContacts.get(contact.getUsername()) != null) {
                newRegisteredContacts.put(contact.getUsername(), contact);
                registeredContacts.remove(contact.getUsername());
            }
            else if (unregisteredContacts.get(contact.getUsername()) != null) {
                newUnregisteredContacts.put(contact.getUsername(), contact);
                unregisteredContacts.remove(contact.getUsername());
            }
        }

        String removedContact = toArrayList(ContactsEnum.BOTH_LISTS).get(0).getUsername();
        registeredContacts = newRegisteredContacts;
        unregisteredContacts = newUnregisteredContacts;

        // returns the only one that has left
        return new ContactUpdate(ContactsEnum.UpdateOperation.CONTACT_REMOVED,
                "", removedContact);
    }

    private ContactUpdate getChangeIfExist(Contact firstContact, Contact secContact)
    {
        if(!firstContact.cmp(secContact)) {
            return new ContactUpdate(CONTACT_DETAILS_CHANGE, firstContact.getUsername(),
                    secContact.getUsername());
        }
        else
            return null;
    }

    private ContactUpdate reinsertContact(Hashtable<String, Contact> newRegisteredContacts,
                                          Hashtable<String, Contact> newUnregisteredContacts, Contact contact)
    {
        ContactUpdate contactUpdate = null;
        Contact tmp;
        String newUsername = "";

        if ( (tmp = registeredContacts.get(contact.getUsername())) != null) {
            newRegisteredContacts.put(contact.getUsername(), contact);
            contactUpdate = getChangeIfExist(tmp, contact);
            registeredContacts.remove(contact.getUsername());
        }
        else if ( (tmp = unregisteredContacts.get(contact.getUsername())) != null) {
            newUnregisteredContacts.put(contact.getUsername(), contact);
            contactUpdate = getChangeIfExist(tmp, contact);
            unregisteredContacts.remove(contact.getUsername());
        }
        // if null, not exist, treats it as new contact
        else
        {
            newUnregisteredContacts.put(contact.getUsername(), contact);
            newUsername = contact.getUsername();
        }

        if (newUsername.length() > 0)
            return new ContactUpdate(ContactsEnum.UpdateOperation.CONTACTS_REPLACED,
                    toArrayList(ContactsEnum.BOTH_LISTS).get(0).getUsername(), newUsername);
        else
            return contactUpdate;
    }

    private ContactUpdate updateExistedContact(ArrayList<Contact> phoneContacts)
    {
        Hashtable<String, Contact> newRegisteredContacts = new Hashtable<String, Contact>();
        Hashtable<String, Contact> newUnregisteredContacts = new Hashtable<String, Contact>();
        Contact contact;
        ContactUpdate result;
        ContactUpdate contactUpdate = null;

        for (int i=0; i<phoneContacts.size(); i++)
        {
            contact = phoneContacts.get(i);
            result = reinsertContact(newRegisteredContacts, newUnregisteredContacts, contact);
            if (result != null)
            {
                contactUpdate = result;
            }

        }

        registeredContacts = newRegisteredContacts;
        unregisteredContacts = newUnregisteredContacts;
        if (contactUpdate != null)
            return contactUpdate;
        else
            return  null;
    }

    // refresh to the updated contacts list on the device itself, relevant only to the phone contacts
    public ContactUpdate refresh()
    {
        ArrayList<Contact> phoneContacts =
                (new PhoneContactsExtractor(MainActivity.getContext())).getContacts();
        int numOfContacts = registeredContacts.size() + unregisteredContacts.size();
        // new contact
        if (phoneContacts.size() > numOfContacts)
            return updateNewContact(phoneContacts);
        // contact removed
        else if (phoneContacts.size() < numOfContacts)
            return updateRemoveContact(phoneContacts);
        else if (phoneContacts.size() == numOfContacts)
            return updateExistedContact(phoneContacts);
        else
            return null;
    }

    public void updateStatus(String username, String status)
    {
        Contact contact = getContact(username);
        if (contact != null)
            contact.setStatus(status);
    }

    ///////////////////////////////////////////////////////////////////////////////

    public ContactsList(ArrayList<Contact> registered, ArrayList<Contact> unregistered)
    {
        this.registeredContacts = arrToHashtable(registered);
        this.unregisteredContacts = arrToHashtable(unregistered);
        contactsObserver = new ContactsObserver(MainActivity.getContext());
    }

    private Hashtable<String, Contact> arrToHashtable(ArrayList<Contact> contactsArray)
    {
        Hashtable<String, Contact> hashTable = new Hashtable<>();

        for (int i=0; i<contactsArray.size(); i++)
            hashTable.put(contactsArray.get(i).getUsername(), contactsArray.get(i));

        return hashTable;
    }

    public Contact getContact(String username)
    {
        Contact contact = (Contact)registeredContacts.get(username);
        if (contact != null)
            return contact;
        else
        {
            contact = (Contact)unregisteredContacts.get(username);
            return contact;
        }
    }

    private boolean inList(ArrayList<String> contactsList, String username)
    {
        for (int i=0; i<contactsList.size(); i++)
        {
            if(contactsList.get(i).equals(username))
                return true;
        }
        return false;
    }

    private String serialize()
    {
        ArrayList<Contact> contacts = toArrayList();
        StringBuilder xml = new StringBuilder();
        String opener = "<contacts size='" + contacts.size() + "'>";
        xml.append(opener);
        for (int i=0; i<contacts.size(); i++)
            xml.append(contacts.get(i).serialize());
        xml.append("</contacts>");
        return xml.toString();
    }

    public ArrayList<Contact> toArrayList()
    {
        ArrayList<Contact> fullContacts = new ArrayList<Contact>((registeredContacts.values()));
        //fullContacts.addAll(appContacts.values());


        return fullContacts;
    }

    public ArrayList<Contact> toArrayList(ContactsEnum list)
    {
        ArrayList<Contact> arrayList = new ArrayList<Contact>();
        if (list == ContactsEnum.REGISTERED)
            arrayList.addAll(registeredContacts.values());
        else if (list == ContactsEnum.UNREGISTERED)
            arrayList.addAll(unregisteredContacts.values());
        else
        {
            arrayList.addAll(registeredContacts.values());
            arrayList.addAll(unregisteredContacts.values());
        }

        return arrayList;

    }


    public ArrayList<Contact> getSharedContacts(ArrayList<String> contacts)
    {
        ArrayList<Contact> sharedContacts = new ArrayList<>();
        Contact contact;

        for (int i=0; i<contacts.size(); i++) {
            contact = getContact(contacts.get(i));
            if (contact != null)
                sharedContacts.add(getContact(contacts.get(i)));
            else
                sharedContacts.add(new Contact(contacts.get(i)));
        }

        return sharedContacts;
    }

    public ArrayList<Contact> eliminateSharedContacts(ArrayList<String> contacts)
    {
        ArrayList<Contact> newContactsList = new ArrayList<>();
        ArrayList<Contact> fullContactsList = toArrayList();

        for (int i=0; i<fullContactsList.size(); i++) {
            if ( !inList(contacts, fullContactsList.get(i).getUsername()) )
                newContactsList.add(fullContactsList.get(i));
        }

        return newContactsList;
    }

    public String xml()
    {
        return "<request type='"+ RequestType.CONTACTS_LIST_UPDATE+"'>" +
                "<sender>" + DataModel.getUsername() + "</sender>" +
                serialize() +
                "</request>";
    }
}
