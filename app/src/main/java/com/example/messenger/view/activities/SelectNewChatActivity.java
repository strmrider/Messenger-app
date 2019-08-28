package com.example.messenger.view.activities;


import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;

import com.example.messenger.R;
import com.example.messenger.viewmodel.SelectNewChatViewModel;

public class SelectNewChatActivity extends AppCompatActivity {

    private SelectNewChatViewModel viewModel;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_select_new_chat);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        setTitle("Select contact");

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
        viewModel = new SelectNewChatViewModel(getApplicationContext());
        ListView listView = findViewById(R.id.contactsListView);
        listView.setAdapter(viewModel.getContactsSelectionArrayAdapter());
        listView.setOnItemClickListener(getAdapterViewListener());
    }

    private AdapterView.OnItemClickListener getAdapterViewListener()
    {
        return new AdapterView.OnItemClickListener()
        {
            public void onItemClick(AdapterView<?> parent, View view, int position, long id)
            {
                viewModel.startChat(position);
                finish();
            }
        };
    }

}
