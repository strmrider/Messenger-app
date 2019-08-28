package com.example.messenger.models.database;

public enum DatabaseFields {
    ID, SENDER, RECIPIENT, TEXT, SENDING_DATE, RECEIVING_DATE, TYPE, STATUS, PATH, MEMBERS;

    public static int geIntValueOf(DatabaseFields field)
    {
        switch(field)
        {
            case ID:
                return 0;
            case SENDER:
                return 1;
            case RECIPIENT:
                return 2;
            case TEXT:
                return 3;
            case SENDING_DATE:
                return 4;
            case RECEIVING_DATE:
                return 5;
            case TYPE:
                return 6;
            case STATUS:
                return 7;
            case PATH:
                return 8;
            default:
                return -1;
        }
    }
}
