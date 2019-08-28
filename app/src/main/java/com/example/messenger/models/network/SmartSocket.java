package com.example.messenger.models.network;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.net.Socket;

public class SmartSocket {
    private Socket socket;

    SmartSocket(Socket socket)
    {
        this.socket = socket;
    }

    public Socket getSocket()
    {
        return socket;
    }

    private String getSizeFormat(String msg)
    {
        int size = msg.length();
        String sizeFormat = "";
        String sizeInStr = Integer.toString(size);
        for(int i=0; i<(5-sizeInStr.length()); i++)
            sizeFormat += "0";

        sizeFormat += sizeInStr;
        return sizeFormat;
    }

    void send(String msg)
    {
        String dataSize = getSizeFormat(msg);
        String fullMsg = dataSize + msg;
        try
        {
            DataOutputStream out = new DataOutputStream(socket.getOutputStream());
            out.flush();
            out.write(fullMsg.getBytes(),0, fullMsg.length());
        }
        catch (IOException e)
        {
            // handle exception
            e.printStackTrace();
        }
    }

    private int extractSizeFromBuffer(byte[] buffer, int buffSize)
    {
        String sizeInStr = "";
        boolean start = false;
        for(int i=0; i<buffSize; i++)
        {
            if(buffer[i] != 0)
                start = true;
            if(buffer[i] == 0 && !start)
                continue;
            else
                sizeInStr += (char)buffer[i];
        }

        return Integer.parseInt(sizeInStr);
    }

    private int receiveDataSize()
    {
        try {
            byte[] buffer = new byte[5];
            DataInputStream in = new DataInputStream(socket.getInputStream());
            in.read(buffer);

            return extractSizeFromBuffer(buffer, 5);
        }
        catch (IOException e)
        {
            // handle exception
            e.printStackTrace();
        }
        return -1;
    }

    public String receive()
    {
        try {
            int dataSize = receiveDataSize();
            byte[] buffer = new byte[dataSize];
            DataInputStream in = new DataInputStream(socket.getInputStream());
            in.read(buffer);
            return new String(buffer, "UTF-8");
        }
        catch (IOException e)
        {
            // handle exception
            e.printStackTrace();
        }
        return null;
    }
}
