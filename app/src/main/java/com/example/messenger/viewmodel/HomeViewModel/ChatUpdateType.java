package com.example.messenger.viewmodel.HomeViewModel;

public enum ChatUpdateType {
    SET_UNREAD(0),
    INCREASE_UNREAD(1),
    LAST_MESSAGE (2),
    CHANGE_DISPLAY_NAME (3),
    ADD_MEMBERS_TO_GROUP (4),
    REMOVE_MEMBERS_FROM_GROUP(5),
    CLEAN_MESSAGES(6);

    private int type;
    ChatUpdateType(int type)
    {
        this.type = type;
    }

    public int getValue()
    {
        return type;
    }
}
