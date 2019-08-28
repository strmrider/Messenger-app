package com.example.messenger.models.requests;

public enum RequestType {
    NONE (0),
    NEW_MESSAGE (11),
    MESSAGE_SENT_APPROVAL (12),
    MESSAGE_RECEIVED_APPROVAL (13),
    MESSAGE_READ_APPROVAL (14),
    GROUP_INVITATION (41),
    GROUP_MEMBER_REMOVAL (42),
    GROUP_REMOVAL_NOTE (43),
    GROUP_MEMBER_ADDITION (44),
    PROFILE_IMAGE_UPDATE (50),
    STATUS_UPDATE (51),
    PROFILE_UPDATE(55),
    GROUP_ADMIN_APPOINTMENT(45),
    CONTACTS_LIST_UPDATE(60),
    CONTACTS_APPROVAL(70),
    BINARY_REQUEST(100),
    NEW_ACCOUNT(200);


    private int type;
    RequestType(int type)
    {
        this.type = type;
    }

    public int getValue()
    {
        return type;
    }

    public static RequestType getTypePerInt(int type)
    {
        switch (type)
        {
            case 11:
                return NEW_MESSAGE;
            case 12:
                return MESSAGE_SENT_APPROVAL;
            case 13:
                return MESSAGE_RECEIVED_APPROVAL;
            case 41:
                return GROUP_INVITATION;
            case 42:
                return GROUP_MEMBER_REMOVAL;
            case 44:
                return GROUP_MEMBER_ADDITION;
            case 45:
                return GROUP_ADMIN_APPOINTMENT;
            case 55:
                return PROFILE_UPDATE;
            case 70:
                return CONTACTS_APPROVAL;
            case 100:
                return BINARY_REQUEST;
            default:
                return NONE;
        }
    }
}