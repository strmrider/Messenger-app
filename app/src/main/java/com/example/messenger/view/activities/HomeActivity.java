package com.example.messenger.view.activities;

import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.AppCompatDrawableManager;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;

import com.example.messenger.R;
import com.example.messenger.models.DataModel.DataModel;
import com.example.messenger.viewmodel.HomeViewModel.HomeViewModel;

import static com.example.messenger.view.activities.MainActivity.getContext;


public class HomeActivity extends AppCompatActivity {

    private HomeViewModel viewModel;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        setTitle("Messenger");
        init();
    }

    private void init()
    {
        viewModel = new HomeViewModel(this);
        ListView list = findViewById(R.id.chatsListView);
        list.setAdapter(viewModel.getChatsListAdapter());

        list.setOnItemClickListener(adapterOnItemClickListener());

        setNewChatButton();
    }

    @Override
    public boolean onPrepareOptionsMenu(Menu menu) {
        menu.clear();
        getMenuInflater().inflate(R.menu.home_activity_menu, menu);
        return true;
    }

    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle item selection
        switch (item.getItemId()) {
            case R.id.createGroupMenuItem:
                viewModel.startContactsSelection();
                return true;
            case R.id.profileMenuItem:
                viewModel.startEditProfileActivity();
                return true;
            case R.id.settingsMenuItem:
                viewModel.startSettingsActivity();
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }

    private void setNewChatButton()
    {
        FloatingActionButton fab = findViewById(R.id.newChatButton);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                viewModel.startSelectNewChatActivity();
            }
        });

    }

    private AdapterView.OnItemClickListener adapterOnItemClickListener()
    {
        return new AdapterView.OnItemClickListener()
        {
            public void onItemClick(AdapterView<?> parent, View view, int position, long id)
            {
                viewModel.startChat(position);
            }
        };
    }

}
