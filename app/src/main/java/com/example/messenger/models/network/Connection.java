package com.example.messenger.models.network;

import android.os.AsyncTask;

import java.io.IOException;
import java.net.Socket;

/**
 * Created by t on 02/06/2018.
 *  Connection thread; makes connection with the server and returns the socket object wich is the
 *  stream with the server
 */

public class Connection extends AsyncTask<Void, Void, Socket> {

    private String address;
    private int port;
    private Socket socket;
    private String username;

    Connection(String address, int port, String user)
    {
        this.address = address;
        this.port = port;
        socket = null;
        this.username = user;
    }

    private void setConnection()
    {
        try {
            socket = new Socket(this.address, this.port);
            SmartSocket smartSocket = new SmartSocket(socket);
            smartSocket.send(username);
        }
        catch (IOException e)
        {
            // handle exception
            e.printStackTrace();
        };
    }

    @Override
    protected Socket doInBackground(Void... params) {
        setConnection();
        return this.socket;
    }
}
