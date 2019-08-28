package com.example.messenger.models.messages;

public enum MessageType {
    TEXT,
    IMAGE,
    VIDEO,
    AUDIO,
    NONE;

    public static MessageType getTypePerInt(int type)
    {
        switch (type)
        {
            case 1:
                return TEXT;
            case 2:
                return IMAGE;
            default:
                return NONE;
        }
    }

    public static int getTypeAsInt(MessageType type)
    {
        switch (type)
        {
            case TEXT:
                return 1;
            case IMAGE:
                return 2;
            case VIDEO:
                return 3;
            case AUDIO:
                return 4;
            default:
                return -1;
        }
    }
}