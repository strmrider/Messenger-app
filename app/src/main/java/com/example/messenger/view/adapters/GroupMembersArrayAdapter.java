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
import com.example.messenger.models.DataModel.DataModel;
import com.example.messenger.models.chats.GroupMember;

import java.util.ArrayList;

public class GroupMembersArrayAdapter extends ArrayAdapter<GroupMember> {

    private ArrayList<GroupMember> membersList;
    private Context context;

    public GroupMembersArrayAdapter(Context context, int resource, ArrayList<GroupMember> membersList)
    {
        super(context, resource, membersList);
        this.context = context;
        this.membersList = membersList;
    }

    @NonNull
    public View getView(int position, View convertView, ViewGroup parent)
    {
        GroupMember member = this.membersList.get(position);
        Contact contact = member.contact;
        if (contact == null)
            contact = new Contact(this.membersList.get(position).username);
        LayoutInflater inflater = (LayoutInflater) context.getSystemService(Activity.LAYOUT_INFLATER_SERVICE);
        convertView = inflater.inflate(R.layout.contact_list_item, null);

        TextView contactName = convertView.findViewById(R.id.contactNameTextView);
        TextView status = convertView.findViewById(R.id.contactStatusTextView);
        if (member.isAdmin) {
            TextView adminTitle = convertView.findViewById(R.id.isAdminTextView);
            adminTitle.setVisibility(View.VISIBLE);
            adminTitle.setText("Admin");
        }
        if(member.username.equals(DataModel.getUsername()))
            contactName.setText("You");
        else
            contactName.setText(contact.getDisplayName());
        status.setText(contact.getStatus());

        return convertView;
    }
}
