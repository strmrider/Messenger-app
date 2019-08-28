package com.example.messenger.viewmodel.RegistrationViewModel;

import android.content.Context;

import com.example.messenger.models.Contacts.ContactsList;
import com.example.messenger.models.DataModel.DataModel;
import com.example.messenger.models.DataModel.Settings;
import com.example.messenger.models.database.DatabaseInitiator;
import com.example.messenger.models.installation.DirectoriesInit;
import com.example.messenger.models.network.MainSocket;
import com.example.messenger.models.requests.RequestParser;
import com.example.messenger.models.requests.RequestType;

import java.util.ArrayList;

public class RegistrationActivityViewModel {
    private Context context;
    private ContactsList contactsList;
    private MainSocket server;
    private RequestParser parser;

    public RegistrationActivityViewModel(Context context)
    {
        this.context = context;
    }

    public void install(String username, String password)
    {
        DatabaseInitiator databaseInitiator =
                new DatabaseInitiator(this.context, "messenger");
        DirectoriesInit directoriesInit = new DirectoriesInit();

        databaseInitiator.initial();
        databaseInitiator.settings(username, password);
        directoriesInit.init();
        server = new MainSocket(Settings.getHostIP(), Settings.getHostPort(), username);
        server.sendDataToServer(accountRegistrationRequest(username, password));
        server.disconnect();
    }

    private String accountRegistrationRequest(String username, String password)
    {
        return "<request type='" + RequestType.NEW_ACCOUNT.getValue() +"'>" +
                "<username>" + username + "</username>" +
                "<password>" + password + "</password>" +
                "</request>";
    }

    public void changeAccount(String username, String password)
    {
        DataModel.getModel();
    }

}
