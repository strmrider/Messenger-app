package com.example.messenger.interfaces;


import com.example.messenger.models.chats.Chat;
import com.example.messenger.models.messages.LastMessage;
import com.example.messenger.viewmodel.HomeViewModel.ChatUpdateType;

public interface HomeViewModelListener {
    void callUpdateChatDisplayName(String username, String newDisplayName);
    Chat callGetChat(String chatUsername);
    void callUpdateChatFromIncomeMessage(String chatUsername, LastMessage lastMessage, int unread);
    void callAddNewChat(Chat chat);
    void callStartChat(int index);
    void callUpdateChatProperties(String chatUsername, Object o, ChatUpdateType type);
}
