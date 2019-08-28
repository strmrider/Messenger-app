package com.example.messenger.view.activities;

import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Environment;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.ImageButton;
import android.widget.TextView;

import com.example.messenger.R;
import com.example.messenger.viewmodel.ProfileViewModel;

public class ProfileActivity extends AppCompatActivity {

    private ProfileViewModel viewModel;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_profile);
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        setTitle("Profile");

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
        viewModel = new ProfileViewModel(this);
        setProfileContent();
        setDoneButton();
        setProfileImageButton();
    }

    private void setProfileContent()
    {
        ImageButton profileImage = findViewById(R.id.profileImageButton);
        String profileImagePath = Environment.getExternalStorageDirectory() +
                "/StormMessenger/profiles/images/" + viewModel.getUsername() + ".png";
        Bitmap bmImg = BitmapFactory.decodeFile(profileImagePath);
        profileImage.setImageBitmap(bmImg);
        TextView statusTextEdit = findViewById(R.id.statusEditText);
        statusTextEdit.setText(viewModel.getStatus());
    }

    private void setDoneButton()
    {
        FloatingActionButton doneBtn = findViewById(R.id.saveProfileSettingsButton);
        doneBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                TextView statusTextEdit = findViewById(R.id.statusEditText);
                viewModel.done(statusTextEdit.getText().toString());
            }
        });
    }

    private void setProfileImageButton()
    {
        ImageButton btn = findViewById((R.id.profileImageButton));
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
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        viewModel.selectFileFromGallery(requestCode, resultCode, data);

        ImageButton profileImage = findViewById(R.id.profileImageButton);
        Bitmap bmImg = BitmapFactory.decodeFile(viewModel.getSelectedFile());
        profileImage.setImageBitmap(bmImg);
    }
}
