package com.example.messenger.models.messages;

import java.util.ArrayList;

public class MessagesList {
    private ArrayList<Message> messages;

    public MessagesList()
    {
        messages = new ArrayList<>();
    }

    public void add(Message message)
    {
        messages.add(message);
    }

    public void remove(String id)
    {
        int index = getMessagePerIndex(id);
        if(index > -1)
            messages.remove(index);
    }

    public ArrayList<Message> getArrayList()
    {
        return messages;
    }

     int getMessagePerIndex(String id)
    {
        for(int i=0; i<messages.size(); i++)
        {
            if (messages.get(i).id.equals(id))
                return i;

        }

        return -1;
    }

    public Message getMessage(int index)
    {
        return messages.get(index);
    }

    public Message getMessage(String id)
    {
        int index = getMessagePerIndex(id);
        if(index > -1)
            return messages.get(index);
        else
            return null;
    }

    public void changeStatus(MessageStatusUpdate statusUpdate)
    {
        for(int i=0; i<messages.size(); i++)
        {
            if (messages.get(i).id.equals(statusUpdate.id)) {
                messages.get(i).status = statusUpdate.status;
            }

        }
    }

    public void remove(int index)
    {
        messages.remove(index);
    }

    public void clean()
    {
        messages.clear();
    }

}