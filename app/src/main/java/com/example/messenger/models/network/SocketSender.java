package com.example.messenger.models.network;

import java.io.DataOutputStream;
import java.io.IOException;
import java.io.OutputStream;

class SocketSender {
    private DataOutputStream dataOutputStream;

    SocketSender(OutputStream outputStream)
    {
        this.dataOutputStream = new DataOutputStream(outputStream);
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
            DataOutputStream out = new DataOutputStream(dataOutputStream);
            out.flush();
            out.writeBytes(fullMsg);
        }
        catch (IOException e)
        {
            // handle exception
            e.printStackTrace();
        }
    }
}
