package com.example.messenger.models.Contacts;

import android.content.Context;
import android.database.ContentObserver;
import android.os.Handler;
import android.provider.ContactsContract;

import com.example.messenger.models.DataModel.DataModel;


public class ContactsObserver extends ContentObserver {

    private Context context;

    public ContactsObserver(Context context)
    {
        super(new Handler());
        this.context = context;
        this.context.getContentResolver().registerContentObserver
                (ContactsContract.Contacts.CONTENT_URI, true, this);
    }


    @Override
    public void onChange(boolean selfChange) {
        super.onChange(selfChange);
        DataModel.getModel().refreshContactsList();
    }

}
