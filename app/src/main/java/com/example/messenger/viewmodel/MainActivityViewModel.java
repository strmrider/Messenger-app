package com.example.messenger.viewmodel;

import android.content.Context;
import android.content.Intent;

import com.example.messenger.models.DataModel.DataModel;
import com.example.messenger.models.DataModel.Settings;
import com.example.messenger.models.database.DatabaseInitiator;
import com.example.messenger.view.activities.HomeActivity;
import com.example.messenger.view.activities.RegistrationActivity;
import com.example.messenger.viewmodel.RegistrationViewModel.RegistrationAction;

public class MainActivityViewModel {
    private DataModel dataModel;
    private boolean isInstalled;
    private Context context;

    public MainActivityViewModel(Context context)
    {
        this.context = context;
        isInstalled = DatabaseInitiator.checkDataBase(context, Settings.getDBName());
        if(isInstalled)
            runHomeActivity();
        else
            runRegistrationActivity();
    }

    private void runHomeActivity()
    {
        dataModel = DataModel.getModel();
        Intent intent = new Intent(context, HomeActivity.class);
        context.startActivity(intent);
    }


    private void runRegistrationActivity()
    {
        Intent intent = new Intent(context, RegistrationActivity.class);
        intent.putExtra("action", RegistrationAction.Register);
        context.startActivity(intent);
    }
}
