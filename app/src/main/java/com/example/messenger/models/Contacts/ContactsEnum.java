package com.example.messenger.models.Contacts;

public enum ContactsEnum {
    REGISTERED,
    UNREGISTERED,
    BOTH_LISTS,
    LOAD_FROM_DATABASE,
    LOAD_FROM_PHONE_CONTACTS;

    public enum UpdateOperation
    {
        CONTACT_ADDED(1),
        CONTACT_REMOVED(2),
        CONTACTS_REPLACED(3),
        CONTACT_DETAILS_CHANGE(4);

        private int type;
        UpdateOperation(int type)
        {
            this.type = type;
        }

        public int getValue()
        {
            return type;
        }
    }
}
