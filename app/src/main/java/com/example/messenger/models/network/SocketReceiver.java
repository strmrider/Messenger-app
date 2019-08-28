package com.example.messenger.models.network;

import java.io.DataInputStream;
import java.io.IOException;

public class SocketReceiver {
    private DataInputStream dataInputStream;
    SocketReceiver(DataInputStream dataInputStream)
    {
        this.dataInputStream = dataInputStream;
    }

    private int extractSizeFromBuffer(String buffer)
    {
        String sizeInStr = "";
        boolean start = false;
        for(int i=0; i<buffer.length(); i++)
        {
            if(buffer.charAt(i) != 0)
                start = true;
            if(buffer.charAt(i) == 0 && !start)
                continue;
            else
                sizeInStr += (char)buffer.charAt(i);
        }

        return Integer.parseInt(sizeInStr);
    }

    private int receiveDataSize()
    {
        try {
            byte[] buffer = new byte[5];
            //DataInputStream in = new DataInputStream(this.dataInputStream);
            dataInputStream.read(buffer);
            //String bufferStr = new String(buffer, "UTF8");

            return extractSizeFromBuffer(new String(buffer, "UTF8"));
        }
        catch (IOException e)
        {
            // handle exception
            e.printStackTrace();
        }
        catch ( NumberFormatException e)
        {
            e.printStackTrace();
        }
        return -1;
    }

    public void close()
    {
        try {
            this.dataInputStream.close();
        }
        catch (IOException e)
        {
            e.printStackTrace();
        }
    }

    public String receive()
    {
        try {
            int dataSize = receiveDataSize();
            byte[] buffer = null;
            // avoiding invalid data size due connection closing
            if(dataSize > 0) {
                buffer = new byte[dataSize];
                //DataInputStream in = new DataInputStream(this.dataInputStream);
                dataInputStream.readFully(buffer);
            }
            if(buffer == null)
                return null;
            else
                return new String(buffer, "ISO-8859-1");
        }
        catch (IOException e)
        {
            // handle exception
            e.printStackTrace();
        }
        return null;
    }
}