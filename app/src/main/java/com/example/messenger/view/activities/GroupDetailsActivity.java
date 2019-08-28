package com.example.messenger.view.activities;

import android.graphics.Color;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.ContextMenu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;

;
import com.example.messenger.R;
import com.example.messenger.viewmodel.GroupDetailsViewModel;

public class GroupDetailsActivity extends AppCompatActivity {

    private GroupDetailsViewModel viewModel;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_group_details);

        init();
    }

    private void init()
    {
        String groupUsername = (String)getIntent().getExtras().get("username");
        viewModel = new GroupDetailsViewModel(this, groupUsername);

        ListView membersListView = findViewById(R.id.membersListView);
        membersListView.setAdapter(viewModel.getGroupMembersArrayAdapter());
        registerForContextMenu(membersListView);

        setSupportActionBar((Toolbar)findViewById(R.id.groupDetailsToolbar));

        LinearLayout layout = findViewById(R.id.groupDetailsToolbarContainer);
        layout.setVisibility(View.INVISIBLE);
        setToolbar();
        membersListView.setOnItemClickListener(adapterOnItemClickListener());

        setTitle("Group details");

    }

    @Override
    public void onCreateContextMenu(ContextMenu menu, View v, ContextMenu.ContextMenuInfo menuInfo) {
        super.onCreateContextMenu(menu, v, menuInfo);
        AdapterView.AdapterContextMenuInfo info = (AdapterView.AdapterContextMenuInfo) menuInfo;
        if (!viewModel.myself(info.position)) {
            if (viewModel.isAdmin())
                getMenuInflater().inflate(R.menu.group_member_admin_contextmenu, menu);
            getMenuInflater().inflate(R.menu.group_member_context_menu, menu);
        }
    }

    @Override
    public boolean onContextItemSelected(MenuItem item) {
        AdapterView.AdapterContextMenuInfo info = (AdapterView.AdapterContextMenuInfo) item.getMenuInfo();
        int index = info.position;
        viewModel.selectMember(info.position);
        switch (item.getItemId())
        {
            case R.id.removeMemberMenucontextItem:
                viewModel.removeMembers();
                break;
            case R.id.sendMessageContextmenuItem:
                viewModel.startChat(index);
                break;
            case R.id.addContactMenucontextItem:
                viewModel.addToContacts(index);
                break;
        }
        return true;
    }


    private AdapterView.OnItemClickListener adapterOnItemClickListener()
    {
        return new AdapterView.OnItemClickListener()
        {
            public void onItemClick(AdapterView<?> parent, View view, int position, long id)
            {
                if(!viewModel.myself(position) && !viewModel.isAdmin(position)) {
                    viewModel.selectMember(position);
                    ImageView imageChecked = view.findViewById(R.id.contactItemCheckedImage);
                    if (imageChecked.getVisibility() == view.INVISIBLE) {
                        imageChecked.setVisibility(View.VISIBLE);
                        view.setBackgroundColor(Color.rgb(224, 224, 224));
                    } else {
                        imageChecked.setVisibility(View.INVISIBLE);
                        view.setBackgroundColor(Color.WHITE);
                    }
                    toolbarVisibility();
                }
            }
        };
    }

    private void toolbarVisibility()
    {
        if(viewModel.atLeastOneSelected())
        {
            LinearLayout layout = findViewById(R.id.groupDetailsToolbarContainer);
            setTitle("");
            layout.setVisibility(View.VISIBLE);
        }
        else {
            LinearLayout layout = findViewById(R.id.groupDetailsToolbarContainer);
            layout.setVisibility(View.INVISIBLE);
        }
    }

    private void setToolbar()
    {
        Toolbar toolbar = findViewById(R.id.groupDetailsToolbar);
        ImageButton imageButton = toolbar.findViewById(R.id.removeMembersToolbarImageButton);
        imageButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                viewModel.removeMembers();
            }
        });
        toolbarVisibility();
    }
}
