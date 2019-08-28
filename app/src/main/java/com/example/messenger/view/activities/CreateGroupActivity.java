package com.example.messenger.view.activities;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.GridView;
import android.widget.TextView;

import com.example.messenger.R;
import com.example.messenger.viewmodel.CreateGroupViewModel;

public class CreateGroupActivity extends AppCompatActivity {

    private CreateGroupViewModel viewModel;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_create_group);

        init();
    }

    private void init()
    {
        viewModel = new CreateGroupViewModel(this);
        GridView gridView = findViewById(R.id.invitedContactsGridView);
        gridView.setAdapter(viewModel.getContactsSelectionArrayAdapter());

        setCreateGroupButton();
    }

    private void setCreateGroupButton()
    {
        Button btn = findViewById(R.id.createGroupButton);
        btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                TextView groupsName = findViewById(R.id.newGroupNameTextView);
                String name = groupsName.getText().toString();
                viewModel.createGroup(name);
                finish();
            }
        });
    }
}
