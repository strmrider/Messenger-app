package com.example.messenger.models.database;


import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

import com.example.messenger.models.Contacts.Contact;
import com.example.messenger.models.Contacts.ContactUpdate;
import com.example.messenger.models.Contacts.ContactsEnum;
import com.example.messenger.models.Contacts.ContactsList;
import com.example.messenger.models.DataModel.DataModel;
import com.example.messenger.models.chats.Chat;
import com.example.messenger.models.chats.ChatsList;
import com.example.messenger.models.chats.GroupChat;
import com.example.messenger.models.messages.LastMessage;
import com.example.messenger.models.messages.MediaMessage;
import com.example.messenger.models.messages.Message;
import com.example.messenger.models.messages.MessageBody;
import com.example.messenger.models.messages.MessageDate;
import com.example.messenger.models.messages.MessageStatus;
import com.example.messenger.models.messages.MessageStatusUpdate;
import com.example.messenger.models.messages.MessageType;
import com.example.messenger.models.messages.MessagesList;
import com.example.messenger.models.messages.TextMessage;
import com.example.messenger.models.others.Profile;

import java.util.ArrayList;

import static android.content.Context.MODE_PRIVATE;

public class Database {
    private SQLiteDatabase database;
    private DatabaseInitiator initiator;

    public Database(String dbName, Context context)
    {
        this.database = context.openOrCreateDatabase(dbName, MODE_PRIVATE, null);
    }

    public Profile selectProfile()
    {
        String username="";
        String status = "";
        Cursor cursor = database.rawQuery("select * from Settings", null);
        if (cursor.getCount() > 0)
        {
            cursor.moveToFirst();
            do {
                username =cursor.getString(0);
                status = cursor.getString(2);
            }while (cursor.moveToNext());

        }
        cursor.close();

        return new Profile(username, status, getContactsList());
    }

    private boolean isGroup(String username) {
        return !DataModel.getUsername().equals(username);
        /*return (username.length() >= 3 && username.charAt(0) == '0' && username.charAt(1) == '0'
                && username.charAt(2) == '0');*/
    }

    public void initDatabase()
    {
        initiator.initial();
    }

    public void insertContacts(ContactsList contacts)
    {
        insertContacts(contacts.toArrayList(ContactsEnum.REGISTERED), true);
        insertContacts(contacts.toArrayList(ContactsEnum.UNREGISTERED), false);
    }

    private void insertContacts(ArrayList<Contact> contacts, boolean registered)
    {
        for (int i=0; i<contacts.size(); i++)
            insertContact(contacts.get(i), registered);
    }

    private void insertContact(Contact contact, boolean registered)
    {
        ContentValues insertValues = new ContentValues();
        insertValues.put("username", contact.getUsername());
        insertValues.put("displayName", contact.getDisplayName());
        insertValues.put("status", contact.getStatus());
        insertValues.put("registered", (registered ? 1 : 0));

        database.insert("Contacts", null, insertValues);
    }

    public void addNewChat(String username, String displayName)
    {
        ContentValues insertValues = new ContentValues();
        insertValues.put("username", username);
        insertValues.put("displayName", displayName);
        insertValues.put("unread", 0);
        insertValues.put("silenced", 0);
        database.insert("Chats", null, insertValues);
    }

    public void addNewGroup(GroupChat groupChat)
    {
        ContentValues insertValues = new ContentValues();
        insertValues.put("username", groupChat.getUsername());
        insertValues.put("displayName", groupChat.getDisplayName());
        insertValues.put("unread", 0);
        insertValues.put("silenced", 0);
        insertValues.put("members", groupChat.serializeForDB());
        database.insert("Chats", null, insertValues);
    }

    public ContactsList getContactsList()
    {
        ArrayList<Contact> registered = new ArrayList<>();
        ArrayList<Contact> unregistered = new ArrayList<>();
        Contact contact;
        Cursor cursor = database.rawQuery("select * from Contacts", null);
        if (cursor.getCount() > 0)
        {
            cursor.moveToFirst();
            do {
                contact = new Contact(cursor.getString(0), cursor.getString(1),
                        cursor.getString(2));
                // if registered
                if (cursor.getInt(3) > 0)
                    registered.add(contact);
                else
                    unregistered.add(contact);
            }while (cursor.moveToNext());

        }
        cursor.close();
        return new ContactsList(registered, unregistered);

    }

    public String selectGroupMembers(String groupUsername)
    {
        ArrayList<String> membersList = null;
        String membersStr = "";
        Cursor cursor = database.rawQuery("select members from Chats where " +
                "username='"+ groupUsername +"'", null);

        if (cursor.getCount() > 0)
        {
            cursor.moveToFirst();
            membersStr = cursor.getString(0);
            //membersList = GroupMembersProcessing.strToArray(membersStr);
        }

        cursor.close();
        return membersStr;
    }


    public void updateGroupMembers(String groupUsername, String members)
    {
        database.execSQL
                ("update Chats set members='"+ members +"' where username ='" + groupUsername + "'");
    }

    public void insertMessage(Message message)
    {
        ContentValues insertValues = new ContentValues();
        insertValues.put("id", message.id);
        insertValues.put("sender", message.sender);
        insertValues.put("receiver", message.recipient);
        insertValues.put("text", message.messageText());
        insertValues.put("sendingDate", message.sendingDate.getDate());
        insertValues.put("receivingData", message.receivingDate.getDate());
        insertValues.put("type", MessageType.getTypeAsInt(message.getType()));
        insertValues.put("status", MessageStatus.getStatusAsInt(message.status));
        if(message.getType() != MessageType.TEXT) {
            MediaMessage mediaMessage = (MediaMessage)message.getBody();
            insertValues.put("path", mediaMessage.getFullPath());
        }

        database.insert("Messages", null, insertValues);
    }


    private Message readMessage(Cursor cursor)
    {
        String text = cursor.getString(DatabaseFields.geIntValueOf(DatabaseFields.TEXT));
        MessageType type = MessageType.getTypePerInt(
                cursor.getInt(DatabaseFields.geIntValueOf(DatabaseFields.TYPE)));
        MessageStatus status = MessageStatus.getTypePerInt(
                cursor.getInt(DatabaseFields.geIntValueOf(DatabaseFields.STATUS)));

        boolean group = isGroup(cursor.getString(DatabaseFields.geIntValueOf(DatabaseFields.RECIPIENT)));

        String id = cursor.getString(DatabaseFields.geIntValueOf(DatabaseFields.ID));
        String sender = cursor.getString(DatabaseFields.geIntValueOf(DatabaseFields.SENDER));
        String recipient = cursor.getString(DatabaseFields.geIntValueOf(DatabaseFields.RECIPIENT));
        MessageDate sending = new MessageDate(cursor.getString(DatabaseFields.geIntValueOf(DatabaseFields.SENDING_DATE)));
        MessageDate recv = new MessageDate(cursor.getString(DatabaseFields.geIntValueOf(DatabaseFields.RECEIVING_DATE)));

        MessageBody body;
        if(type != MessageType.TEXT)
        {
            String path = cursor.getString(DatabaseFields.geIntValueOf(DatabaseFields.PATH));
            body = new MediaMessage(type, text, path, false);
        }
        else
            body  = new TextMessage(text);


        return new Message(id, sender, recipient, body, sending, recv, status, group);
    }

    private LastMessage getChatLastMessage(String username, String chatUsername, boolean isGroup)
    {
        Cursor result;
        if(isGroup)
            result = database.rawQuery("select * from Messages where " +
                    "receiver='"+chatUsername+"'", null);
        else
            result = database.rawQuery("select * from Messages where (sender='" +
                    chatUsername + "' and receiver='"+username+"')or (receiver='"+
                    chatUsername+"' " + "and sender='"+ username+"')", null);

        result.moveToLast();
        if(result.getCount() > 0) {
            return readMessage(result).toLastMessage();
        }
        result.close();
        return null;
    }

    public MessagesList getMessagesList(String username, String contactUsername)
    {
        MessagesList messagesList = new MessagesList();
        String firstCond = "(receiver ='" + contactUsername + "')";
        String secondCond = "(receiver ='" + username + "' and sender='" + contactUsername + "')";
        Cursor result = database.rawQuery(
                "select * from Messages where" + firstCond + "or" + secondCond, null);

        result.moveToFirst();
        if(result.getCount()>0) {
            do {
                messagesList.add(readMessage(result));
            } while (result.moveToNext());
        }
        result.close();

        return messagesList;
    }

    public ChatsList getChatsList(String username)
    {
        ChatsList chatsList = new ChatsList();
        Cursor result = database.rawQuery("select * from Chats", null);
        boolean isGroup;
        String chatUsername;
        if(result.getCount() >0) {
            result.moveToFirst();
            do {
                chatUsername = result.getString(0);
                if(chatUsername.equals(username))
                    continue;
                isGroup = isGroup(chatUsername);
                if (isGroup)
                {
                    chatsList.add(
                            new GroupChat(
                                    chatUsername,
                                    result.getString(1),
                                    getChatLastMessage(username, chatUsername, true),
                                    true,
                                    result.getInt(2),
                                    result.getString(3),
                                    result.getInt(4) == 1
                            )
                    );
                }
                else
                    chatsList.add(
                            new Chat(
                                    chatUsername,
                                    result.getString(1),
                                    getChatLastMessage(username, chatUsername, false),
                                    false,
                                    result.getInt(2),
                                    result.getInt(4) == 1
                            )
                    );
            } while (result.moveToNext());
        }

        result.close();
        return chatsList;
    }

    public void updateChatSilence(String chatName, boolean silence)
    {
        int silenced = silence ? 1 : 0;
        database.execSQL
                ("update Chats set silenced="+ silenced +" where username ='" + chatName + "'");
    }

    public void updateMessageStatus(MessageStatusUpdate statusUpdate)
    {
        database.execSQL
                ("update Messages set status='"+ MessageStatus.getStatusAsInt(statusUpdate.status)
                        +"' where id ='" + statusUpdate.id + "'");
    }

    public void removeMessage(String id)
    {
        database.execSQL("delete from Messages where id='"+ id + "'");
    }

    public String getProfileStatus()
    {
        Cursor result = database.rawQuery("select * from Settings" , null);
        result.moveToFirst();

        String status =  result.getString(1);
        result.close();
        return status;
    }

    /////////////////////////////////
    private void addContact(String username, String displayName)
    {
        ContentValues insertValues = new ContentValues();
        insertValues.put("username", username);
        insertValues.put("displayName", displayName);
        insertValues.put("status", "");
        insertValues.put("registered", 0);

        database.insert("Contacts", null, insertValues);
    }

    private void replaceContact(ContactUpdate contactUpdate)
    {
        database.execSQL("delete from Contacts where username='"+ contactUpdate.secondString + "'");
        addContact(contactUpdate.contactsUsername, contactUpdate.displayName);
    }

    public void updateContact(ContactUpdate contactUpdate)
    {
        switch (contactUpdate.operation)
        {
            case CONTACT_ADDED:
                addContact(contactUpdate.contactsUsername, contactUpdate.displayName);
                break;
            case CONTACT_REMOVED:
                database.execSQL("delete from Contacts where username='"+ contactUpdate.contactsUsername + "'");
                break;
            case CONTACTS_REPLACED:
                replaceContact(contactUpdate);
                break;
            case CONTACT_DETAILS_CHANGE:
                database.execSQL
                        ("update Contacts set displayName='"+ contactUpdate.displayName + "'" +
                                "where username ='" + contactUpdate.contactsUsername + "'");
                break;
        }
    }

    public void cleanMessages(String username)
    {
        database.execSQL("delete from Messages where sender='"+ username + "' " +
                "or receiver ='" + username+ "'");
    }
}
