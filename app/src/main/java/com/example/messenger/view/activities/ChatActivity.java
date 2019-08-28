package com.example.messenger.view.activities;

import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Color;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.example.messenger.R;
import com.example.messenger.viewmodel.ChatViewModel;

public class ChatActivity extends AppCompatActivity {

    private ChatViewModel viewModel;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_chat);

        init();
    }

    protected void onStart()
    {
        super.onStart();
        viewModel.active(true);
    }

    protected void onStop()
    {
        super.onStop();
        viewModel.active(false);
    }

    protected void onResume()
    {
        super.onResume();
        viewModel.active(true);
    }

    private void init()
    {
        String username = (String)getIntent().getExtras().get("username");
        String displayName = (String)getIntent().getExtras().get("displayName");

        viewModel = new ChatViewModel(this, username, displayName);
        setTitle(getChatsDisplayName());
        Toolbar toolbar = (Toolbar) findViewById(R.id.chatActivityToolbar);
        setSupportActionBar(toolbar);
        LinearLayout layout = findViewById(R.id.chatActivityToolbarContainer);
        layout.setVisibility(View.INVISIBLE);
        // sets buttons
        setSendMessageButton();
        setAttachFileBtn();
        SetToolbarButtons();

        // sets messages list
        ListView listView = findViewById(R.id.messagesListView);
        listView.setAdapter(viewModel.getMsgArrayAdapter());
        listView.setSelection(viewModel.getMsgArrayAdapter().getCount() - 1);
        listView.setOnItemClickListener(adapterOnItemClickListener());

        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            finish();
        }
    });
    }



    private String getChatsDisplayName()
    {
        return viewModel.getCurrentChat().getDisplayName();
    }
    private void setSendMessageButton()
    {
        ImageButton sendBtn = findViewById(R.id.sendMessageButton);
        sendBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                TextView messageBox = findViewById(R.id.messageBoxTextView);
                viewModel.newMessage(messageBox.getText().toString());
                messageBox.setText("");
            }
        });
    }

    private void setAttachFileBtn()
    {
        ImageButton btn = findViewById((R.id.attachFileBtn));
        btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent i = new Intent(
                        Intent.ACTION_PICK,
                        android.provider.MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
                startActivityForResult(i, 1);
            }
        });
    }

    @Override
    public boolean onPrepareOptionsMenu(Menu menu) {
        menu.clear();
        if (viewModel.isGroup()) {
            getMenuInflater().inflate(R.menu.group_chat_menu, menu);
            if(viewModel.isAdmin()) {
                MenuItem item = menu.findItem(R.id.inviteToGroupMenuItem);
                item.setVisible(true);
            }
        }
        getMenuInflater().inflate(R.menu.chat_menu, menu);
        return true;
    }


    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle item selection
        switch (item.getItemId()) {
            case R.id.inviteToGroupMenuItem:
                viewModel.loadGroupInvitation();
                return true;
            case  R.id.leaveGroupMenuItem:
                //viewModel.quiteGroup();
                return true;
            case R.id.groupDetailsMenuItem:
                viewModel.loadGroupDetails();
                return true;
            case R.id.cleanChatMenuItem:
                viewModel.cleanChat();
            case R.id.shutOffChatMenuItem:
                viewModel.silenceChat();
            default:
                return super.onOptionsItemSelected(item);
        }
    }

    private void SetToolbarButtons()
    {
        ImageView remove = findViewById(R.id.removeMessageToolbarItem);
        remove.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                viewModel.removeSelectedMessages();
            }
        });

        ImageView forward = findViewById(R.id.forwardMessageToolbaritem);
        forward.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                viewModel.forwardMessage();
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
                    view.setBackgroundColor(Color.TRANSPARENT);
                }
                toolbarVisibility();
            }
        };
    }

    private void toolbarVisibility()
    {
        if(viewModel.atLeastOneSelected())
        {
            LinearLayout layout = findViewById(R.id.chatActivityToolbarContainer);
            setTitle("");
            layout.setVisibility(View.VISIBLE);
        }
        else {
            LinearLayout layout = findViewById(R.id.chatActivityToolbarContainer);
            layout.setVisibility(View.INVISIBLE);
            setTitle(getChatsDisplayName());
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        viewModel.selectFileFromGallery(requestCode, resultCode, data);
    }

    @Override
    public void onRequestPermissionsResult(int requestCode,
                                           String permissions[], int[] grantResults) {
        switch (requestCode) {
            // access to gallery permission
            case 1: {
                if (!(grantResults.length > 0
                        && grantResults[0] == PackageManager.PERMISSION_GRANTED && grantResults[1] == PackageManager.PERMISSION_GRANTED)) {
                    Toast.makeText(this,"Permission denied to access your location.", Toast.LENGTH_SHORT).show();
                }
            }
        }
    }
}