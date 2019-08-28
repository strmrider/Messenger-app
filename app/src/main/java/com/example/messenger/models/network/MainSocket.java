package com.example.messenger.models.network;

import android.os.AsyncTask;

import java.io.DataInputStream;
import java.io.IOException;
import java.net.Socket;
import java.util.concurrent.ExecutionException;

public class MainSocket {

    private int port;
    private String ip;
    private String username;
    private Socket socket;
    private SocketSender socketSender;
    private SocketReceiver socketReceiver;
    private Binder binder;

    public MainSocket(String ip, int port, String username)
    {
        this.port = port;
        this.ip = ip;
        this.username = username;
        binder = null;
        socket = connect();
        socketSender = getSocketSender();
        socketReceiver = getSocketReceiver();
    }

    private Socket connect()
    {
        Connection con = new Connection(ip, port, username);
        try {
            return con.execute().get();
        }
        catch (InterruptedException e)
        {
            e.printStackTrace();
        }
        catch (ExecutionException e)
        {
            e.printStackTrace();
        }
        return null;
    }

    public void disconnect()
    {
        try {
            socket.close();
        }
        catch (IOException e)
        {
            e.printStackTrace();
        }
    }

    private SocketSender getSocketSender()
    {
        try{
            return new SocketSender(socket.getOutputStream());
        }
        catch (IOException e)
        {
            e.printStackTrace();
        }
        return null;
    }

    private SocketReceiver getSocketReceiver()
    {
        try {
            return new SocketReceiver(new DataInputStream(socket.getInputStream()));
        }
        catch (IOException e)
        {
            e.printStackTrace();
        }

        return null;
    }

    public void bind()
    {
        binder = null;
        try{
            binder = new Binder(socket.getInputStream());
        }
        catch (IOException e)
        {
            e.printStackTrace();
        }
        catch(NullPointerException e)
        {
            e.printStackTrace();
        }

        try {
            if(binder != null)
                binder.execute(username);
        }
        catch(NullPointerException e)
        {
            e.printStackTrace();
        }
    }

    public void unbind()
    {
        binder.cancel(true);
        binder = null;
    }

    public boolean isBinding()
    {
        if (binder == null)
            return false;
        else
            return true;
    }

    public String receive()
    {
        if(socketReceiver != null)
        {
            ReceiveDataThread receiveDataThread = new ReceiveDataThread(socketReceiver);
            try {
                return receiveDataThread.executeOnExecutor(AsyncTask.THREAD_POOL_EXECUTOR).get();
            }
            catch (InterruptedException e)
            {
                e.printStackTrace();
            }
            catch (ExecutionException e)
            {
                e.printStackTrace();
            }
        }

        return null;
    }

    public void sendDataToServer(String data)
    {
        if(socketSender == null)
        {
            // throw exception
        }
        else
        {
            SendDataThread sendMsgThread = new SendDataThread(socketSender);
            sendMsgThread.executeOnExecutor(AsyncTask.THREAD_POOL_EXECUTOR, data);
        }
    }
}
