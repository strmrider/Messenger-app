package com.example.messenger.view.adapters;


import android.app.Activity;
import android.content.Context;
import android.support.annotation.NonNull;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import com.example.messenger.R;
import com.example.messenger.models.Contacts.Contact;

import java.util.ArrayList;

public class ContactsSelectionArrayAdapter extends ArrayAdapter<Contact>
{
    private ArrayList<Contact> contactsList;
    private Context context;
    private int action;

    public ContactsSelectionArrayAdapter(Context context, int resource,
                                         ArrayList<Contact> contactList, int action)
    {
        super(context, resource, contactList);
        this.context = context;
        this.contactsList = contactList;
        this.action = action;
    }

    @NonNull
    public View getView(int position, View convertView, ViewGroup parent)
    {
        Contact contact = this.contactsList.get(position);
        LayoutInflater inflater = (LayoutInflater) context.getSystemService(Activity.LAYOUT_INFLATER_SERVICE);
        if(action == 0)
            convertView = inflater.inflate(R.layout.contact_list_item, null);
        else
            convertView = inflater.inflate(R.layout.invited_contact_list_item, null);

        TextView contactName = convertView.findViewById(R.id.contactNameTextView);
        contactName.setText(contact.getDisplayName());

        return convertView;
    }
}
