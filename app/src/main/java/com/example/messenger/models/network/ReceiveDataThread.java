package com.example.messenger.models.network;

import android.os.AsyncTask;

public class ReceiveDataThread extends AsyncTask<Void, Void, String> {

    private SocketReceiver socketReceiver;
    ReceiveDataThread(SocketReceiver socketReceiver)
    {
        this.socketReceiver = socketReceiver;
    }

    @Override
    protected String doInBackground(Void... params)
    {
        return socketReceiver.receive();
    }
}
