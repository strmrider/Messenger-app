package com.example.messenger.interfaces;

import com.example.messenger.models.chats.Chat;
import com.example.messenger.models.messages.Message;
import com.example.messenger.models.messages.MessageStatusUpdate;

import java.util.ArrayList;

public interface ChatViewModelListener {
    void callAddChatByMessage(String username);
    ArrayList<Message> callGetSelectedMessages();
    boolean callIsActive();
    Chat callGetCurrentChat();
    void callAddNewMessage(Message message);
    void callUpdateStatus(MessageStatusUpdate status);
    void callSetCurrentChat(Chat chat);

}
