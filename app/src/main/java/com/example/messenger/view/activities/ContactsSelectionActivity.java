package com.example.messenger.view.activities;

import android.graphics.Color;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;

import com.example.messenger.R;
import com.example.messenger.models.chats.GroupChat;
import com.example.messenger.viewmodel.ContactsSelectionViewModel;

public class ContactsSelectionActivity extends AppCompatActivity {

    private ContactsSelectionViewModel viewModel;
    private int action;
    private GroupChat group;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_contacts_selection);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
        init();

    }

    private void init()
    {
        action = Integer.parseInt((String)getIntent().getExtras().get("action"));
        if(action == 1)
        {
            String displayName = (String)getIntent().getExtras().get("displayName");
            group = new GroupChat((String)getIntent().getExtras().get("username"), displayName);

            viewModel = new ContactsSelectionViewModel(this, group.getUsername(), action);
            setTitle("Select contacts");
            getSupportActionBar().setSubtitle("Invite contacts to " + displayName);
        }
        else if (action == 3)
        {
            String username  = (String)getIntent().getExtras().get("username");
            viewModel = new ContactsSelectionViewModel(this, username, action);
            setTitle("Select contact");
        }
        else {
            viewModel = new ContactsSelectionViewModel(this);
            setTitle("Select contacts");
        }

        ListView listView = findViewById(R.id.contactsSelectionListView);
        listView.setAdapter(viewModel.getContactsSelectionArrayAdapter());
        listView.setOnItemClickListener(adapterOnItemClickListener());
        setDoneButton();

    }

    private void setDoneButton()
    {
        FloatingActionButton fab = findViewById(R.id.doneSelectionFloatingbutton);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                viewModel.done();
            }
        });
    }

    private AdapterView.OnItemClickListener adapterOnItemClickListener()
    {
        return new AdapterView.OnItemClickListener()
        {
            public void onItemClick(AdapterView<?> parent, View view, int position, long id)
            {
                viewModel.selectMember(position);
                if (viewModel.isSelected(position)) {
                    view.setBackgroundColor(Color.rgb(224, 224, 224));
                }
                else {
                    view.setBackgroundColor(Color.WHITE);
                }
            }
        };
    }

}
