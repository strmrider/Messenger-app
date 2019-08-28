package com.example.messenger.models.network;

import android.os.AsyncTask;

public class SendDataThread extends AsyncTask<String, Void, Void> {

    private SocketSender socketSender;
    SendDataThread(SocketSender socketSender)
    {
        this.socketSender = socketSender;
    }

    @Override
    protected Void doInBackground(String... params) {
        int l = params[0].length();
        this.socketSender.send((String)params[0]);
        return null;
    }
}
