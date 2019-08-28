package com.example.messenger.view.activities;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toolbar;

import com.example.messenger.R;
import com.example.messenger.viewmodel.RegistrationViewModel.RegistrationAction;
import com.example.messenger.viewmodel.RegistrationViewModel.RegistrationActivityViewModel;

public class RegistrationActivity extends AppCompatActivity {
    private RegistrationAction action;
    private RegistrationActivityViewModel viewModel;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_registration);
        android.support.v7.widget.Toolbar toolbar = (android.support.v7.widget.Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        setTitle("Register account");
        action = (RegistrationAction)getIntent().getExtras().get("action");
        viewModel = new RegistrationActivityViewModel(getApplicationContext());
        setRegisterBtn();
    }

    private void setRegisterBtn()
    {
        Button button = findViewById(R.id.registerBtn);
        if(action == RegistrationAction.Register)
            button.setText("Create account");
        else if (action == RegistrationAction.ChangeAccount)
            button.setText("Sign in");
        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                register();
            }
        });
    }

    private void register()
    {
        EditText usernameInput = findViewById(R.id.usernameRegistration);
        EditText passwordInput = findViewById(R.id.passwordRegistration);
        String username = usernameInput.getText().toString();
        String password = passwordInput.getText().toString();

        if(action == RegistrationAction.Register) {
            viewModel.install(username, password);
            Intent intent = new Intent(getApplicationContext(), HomeActivity.class);
            startActivity(intent);
        }
        else if (action == RegistrationAction.ChangeAccount)
            viewModel.changeAccount(username, password);
    }
}
