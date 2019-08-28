package com.example.messenger.view.activities;

import android.content.Context;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;

import com.example.messenger.R;
import com.example.messenger.models.DataModel.DataModel;
import com.example.messenger.viewmodel.MainActivityViewModel;

public class MainActivity extends AppCompatActivity {
    static MainActivity activity;
    private DataModel dataModel;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        activity = this;
        MainActivityViewModel viewModel = new MainActivityViewModel(getApplicationContext());
    }

    public static Context getContext()
    {
        return activity;
    }
}
