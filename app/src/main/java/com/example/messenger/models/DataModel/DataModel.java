package com.example.messenger.models.DataModel;
import android.content.Context;

import com.example.messenger.interfaces.ChatViewModelListener;
import com.example.messenger.models.Contacts.ContactUpdate;
import com.example.messenger.models.Contacts.ContactsEnum;
import com.example.messenger.models.Contacts.ContactsList;
import com.example.messenger.models.chats.Chat;
import com.example.messenger.models.chats.GroupChat;
import com.example.messenger.models.chats.GroupChatUpdate;
import com.example.messenger.models.database.DatabaseInitiator;
import com.example.messenger.models.messages.IncomeMessage;
import com.example.messenger.models.messages.MessageStatusUpdate;
import com.example.messenger.models.messages.MessageType;
import com.example.messenger.models.others.Profile;
import com.example.messenger.models.Contacts.Contact;
import com.example.messenger.models.database.Database;
import com.example.messenger.models.network.MainSocket;
import com.example.messenger.models.requests.RequestParser;
import com.example.messenger.models.requests.RequestType;
import com.example.messenger.view.activities.MainActivity;

import java.util.ArrayList;
import java.util.Random;

public class DataModel {
    private static String username;
    private static DataModel instance = null;

    private MainSocket server;
    private Database database;
    private Context context;
    private Profile profile;
    private RequestParser parser;
    private ArrayList<Contact> recentSelectedContacts;
    private Listeners listeners;

    private DataModel(MainSocket mainSocket){
        this.context = MainActivity.getContext();

        listeners = new Listeners();
        database = new Database(Settings.getDBName(), context);
        profile = database.selectProfile();
        username = profile.getUsername();
        if (mainSocket == null)
            server = new MainSocket(Settings.getHostIP(), Settings.getHostPort(), username);
        else
            server = mainSocket;

        profile = new Profile(username, database.getProfileStatus(), database.getContactsList() );
        //profile.setContactsList(database.getContactsList());
        parser = new RequestParser();

    }


    /* Binder runs on its own thread and may use DataModel (i.e receiving pending request from server)
     * and, as a result, other models, while they're still null, which may cause a null exception.
     * Running binding method outside of the constructor after the models are created and referenced
     * by a variable, solves that issue.
     */
    public void bindToServer()
    {
        if (!server.isBinding())
            server.bind();
    }

    public static DataModel getModel()
    {
        if(instance == null)
            instance = new DataModel(null);
        return instance;
    }

    public static String getUsername()
    {
        return username;
    }

    public Profile getProfile()
    {
        return profile;
    }

    public Listeners getListeners()
    {
        return listeners;
    }

    public MainSocket getServer()
    {
        return server;
    }

    public ArrayList<Contact> getRecentSelectedContacts()
    {
        return recentSelectedContacts;
    }

    public Database getDatabase()
    {
        return database;
    }

    public void setSelectedContacts(ArrayList<Contact> selectedContacts)
    {
        recentSelectedContacts = selectedContacts;
    }

    public String randomID(String receiver)
    {
        int num = new Random().nextInt(10000) + 1;
        int num2 = new Random().nextInt(50000) + 10000;

        return username + "_" + receiver + ((num%num2) * num2 + num);
    }

    public void initContacts()
    {
        registerContacts();
        refreshContactsList();
    }

    @SuppressWarnings("unchecked")
    private void registerContacts()
    {
        ContactsList contactsList = new ContactsList(ContactsEnum.LOAD_FROM_PHONE_CONTACTS);
        server.sendDataToServer(ContactsRegistrationRequest(username, contactsList));
        //String response = server.receive();

        //ArrayList<Contact> contacts = (ArrayList<Contact>)parser.parseRequest(response);
        //contactsList.register(contacts);
        database.insertContacts(contactsList);
        profile.setContactsList(contactsList);
    }

    private String ContactsRegistrationRequest(String username, ContactsList contactsList)
    {
        return "<request type='" + RequestType.CONTACTS_APPROVAL.getValue() +"'>" +
                "<username>" + username + "</username>" +
                contactsList.serialize(ContactsEnum.UNREGISTERED) +
                "</request>";
    }

    public void refreshContactsList()
    {
        ContactUpdate updatedContact = profile.refreshContactsList();
        if(updatedContact != null)
        {
            database.updateContact(updatedContact);
            server.sendDataToServer(updatedContact.getRequest());
        }
    }

    // add chat to home activity by new sent message
    public void addChatByMessage(String username)
    {
        if (listeners.getHomeViewModelListener().callGetChat(username) == null)
        {
            Contact contact = profile.getContactsList().getContact(username);
            String displayName;
            if (contact != null)
                displayName = profile.getContactsList().getContact(username).getDisplayName();
            else
                displayName = username;

            database.addNewChat(username, displayName);
           listeners.getHomeViewModelListener().callAddNewChat(
                    new Chat(username, displayName, null,
                            false,0, false));
        }
    }

    /**********************************
     * Requests from server handlers
     ********************************/
    public void handleRequest(String request){
        Object parsedResult = parser.parseRequest(request);
        RequestType type = parser.getType();

        switch (type)
        {
            case NEW_MESSAGE:
                incomeMessage((IncomeMessage)parsedResult);
                break;
            case MESSAGE_SENT_APPROVAL:
            case MESSAGE_RECEIVED_APPROVAL:
            case MESSAGE_READ_APPROVAL:
                updateStatusChange((MessageStatusUpdate)parsedResult);
                break;
            case GROUP_INVITATION:
                groupInvitation((GroupChat)parsedResult);
                break;
            case GROUP_MEMBER_ADDITION:
            case GROUP_MEMBER_REMOVAL:
            case GROUP_ADMIN_APPOINTMENT:
                updateChatMembers((GroupChatUpdate)parsedResult);
                break;
            case CONTACTS_APPROVAL:
                contactRegistration((ArrayList<Contact>)parsedResult);
                break;
            case STATUS_UPDATE:
                updateProfileStatus((Contact)parsedResult);
                break;
        }
    }

    private void incomeMessage(IncomeMessage message)
    {
        if(message.getType() != MessageType.TEXT)
            message.writeToFile();

        database.insertMessage(message);
        addChatByMessage(message.actualContactPerson);
        int unread = 0;
        ChatViewModelListener chatListener = listeners.getChatViewModelListener();
        if(chatListener != null && chatListener.callIsActive() &&
                chatListener.callGetCurrentChat().getUsername().
                        equals(message.actualContactPerson))
        {
            chatListener.callAddNewMessage(message);
        }
        else {
            unread = 1;
            Chat chat =
                    listeners.getHomeViewModelListener().callGetChat(message.actualContactPerson);
            if(chat != null && !chat.isSilenced())
                new Notification(context, message.toLastMessage());
        }
        listeners.getHomeViewModelListener().callUpdateChatFromIncomeMessage(
                message.actualContactPerson, message.toLastMessage(), unread);
        if (!message.fromGroup)
            server.sendDataToServer(message.approvalRequest());
    }

    private void updateStatusChange(MessageStatusUpdate statusUpdate)
    {
        database.updateMessageStatus(statusUpdate);
        ChatViewModelListener listener = listeners.getChatViewModelListener();
        if(
                listener != null &&
                        listener.callIsActive() &&
                        listener.callGetCurrentChat().getUsername().equals(statusUpdate.chat)) {
            listener.callUpdateStatus(statusUpdate);
        }
    }

    private void groupInvitation(GroupChat groupChat)
    {
        database.addNewGroup(groupChat);
        listeners.getHomeViewModelListener().callAddNewChat(groupChat);
    }

    private void updateChatMembers(GroupChatUpdate groupChatUpdate)
    {
        GroupChat groupChat = (GroupChat)listeners.getHomeViewModelListener().callGetChat
                (groupChatUpdate.getUsername());
        groupChat.update(groupChatUpdate);
        database.updateGroupMembers(groupChatUpdate.getUsername(),
                groupChatUpdate.getMembersList().serializeForDB());
    }

    private void updateProfileStatus(Contact contact)
    {
        profile.getContactsList().getContact(contact.getUsername()).setStatus(contact.getStatus());
    }
    private void contactRegistration(ArrayList<Contact> contacts)
    {
        profile.getContactsList().register(contacts);
    }
}
