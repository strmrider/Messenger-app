package com.example.messenger.models.network;

import android.os.AsyncTask;

import com.example.messenger.models.DataModel.DataModel;

import java.io.DataInputStream;
import java.io.InputStream;

public class Binder extends AsyncTask<String, Void, Void> {

    private SocketReceiver receiver;
    private DataModel dataModel;
    Binder(InputStream inputStream)
    {
        dataModel = DataModel.getModel();
        DataInputStream dataInputStream = new DataInputStream(inputStream);
        this.receiver = new SocketReceiver(dataInputStream);
    }

    protected void onCancelled()
    {

    }

    @Override
    protected Void doInBackground(String... params)
    {
        String data = null;
        while (true)
        {
            data = receiver.receive();
            if(data == null)
                break;
            else if(data.equals("OUT-123"))
                break;
            else {
                dataModel.handleRequest(data);
            }
        }
        return null;
    }
}
