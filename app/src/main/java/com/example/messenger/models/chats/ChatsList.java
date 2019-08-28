package com.example.messenger.models.chats;

import java.util.ArrayList;

public class ChatsList {
    private ArrayList<Chat> chats;

    public ChatsList()
    {
        chats = new ArrayList<>();
    }

    private void swap(int firstIndex, int secIndex)
    {
        Chat tmpContactPerson = chats.get(firstIndex);
        chats.set(firstIndex, chats.get(secIndex));
        chats.set(secIndex,tmpContactPerson);
    }

    private int partition(int low, int high)
    {
        Chat pivot = chats.get(high);
        int i = (low-1); // index of smaller element
        for (int j=low; j<high; j++)
        {
            if (chats.get(j).compareDates(pivot) >= 0)
            {
                i++;
                swap(i, j);
            }
        }
        swap(i+1, high);

        return i+1;
    }

    private void sort(int low, int high)
    {
        if (low < high)
        {
            int pi = partition(low, high);

            sort(low, pi-1);
            sort(pi+1, high);
        }
    }

    public Chat getChatPerUsername(String username)
    {
        for (int i = 0; i < chats.size(); i++) {
            if(chats.get(i).getUsername().equals(username))
                return chats.get(i);
        }

        return null;
    }

    public void add(Chat newChat)
    {
        chats.add(newChat);
    }

    public ArrayList<Chat> getArrayList()
    {
        return chats;
    }
}
