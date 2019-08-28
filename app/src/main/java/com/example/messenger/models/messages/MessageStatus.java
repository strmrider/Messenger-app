package com.example.messenger.models.messages;

public enum MessageStatus {
    NONE,
    PENDING,
    SENT,
    RECEIVED,
    READ,
    INCOME;

    public static  MessageStatus getPerRequest(int type)
    {
        switch (type)
        {
            case -1:
                return INCOME;
            case 0:
                return PENDING;
            case 12:
                return SENT;
            case 13:
                return RECEIVED;
            case 14:
                return READ;
            default:
                return NONE;
        }
    }

    public static MessageStatus getTypePerInt(int type)
    {
        switch (type)
        {
            case -1:
                return INCOME;
            case 0:
                return PENDING;
            case 1:
                return SENT;
            case 2:
                return RECEIVED;
            case 3:
                return READ;
            default:
                return NONE;
        }
    }

    public static int getStatusAsInt(MessageStatus status)
    {
        switch (status)
        {
            case INCOME:
                return -1;
            case PENDING:
                return 0;
            case SENT:
                return 1;
            case RECEIVED:
                return 2;
            case READ:
                return 3;
            default:
                return -1;
        }
    }
}