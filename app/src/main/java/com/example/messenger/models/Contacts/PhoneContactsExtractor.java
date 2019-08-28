package com.example.messenger.models.Contacts;

import android.Manifest;
import android.app.Activity;
import android.content.Context;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.os.Build;
import android.provider.ContactsContract;
import android.support.v4.app.ActivityCompat;

import java.util.ArrayList;
import java.util.Hashtable;


class PhoneContactsExtractor {
    private ArrayList<Contact> contacts = new ArrayList<>();
    private Context context;

    PhoneContactsExtractor(Context context)
    {
        this.context = context;
        setContacts();
    }

    private void setContactsInArrayList()
    {
        Cursor phones = context.getContentResolver().query(
                ContactsContract.CommonDataKinds.Phone.CONTENT_URI,
                null,null,null, null);
        if (phones != null) {
            while (phones.moveToNext()) {
                String name = phones.getString(
                        phones.getColumnIndex(ContactsContract.CommonDataKinds.Phone.DISPLAY_NAME));
                String phoneNumber = phones.getString(
                        phones.getColumnIndex(ContactsContract.CommonDataKinds.Phone.NUMBER));
                phoneNumber = phoneNumber.replaceAll("\\D+", "");
                contacts.add(new Contact(phoneNumber, name));
            }
            phones.close();
        }
    }

    private void setContacts() {
        if (Build.VERSION.SDK_INT >= 23 &&
                context.checkSelfPermission(Manifest.permission.READ_CONTACTS)
                        != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions((Activity)context,
                    new String[]{Manifest.permission.READ_CONTACTS}, 100);
        }
        else {
            setContactsInArrayList();
        }
    }

    ArrayList<Contact> getContacts()
    {
        return contacts;
    }

    Hashtable<String, Contact> toHashTable()
    {
        Hashtable<String, Contact> hashTable = new Hashtable<>();

        for (int i=0; i<contacts.size(); i++)
            hashTable.put(contacts.get(i).getUsername(), contacts.get(i));

        return hashTable;
    }
}
