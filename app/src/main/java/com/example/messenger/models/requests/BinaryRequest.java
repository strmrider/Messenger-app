package com.example.messenger.models.requests;

import java.io.UnsupportedEncodingException;

/**
 * split request with binary code, split and save components
 */

class BinaryRequest {
    private StringBuilder request;
    private byte[] binary;

    BinaryRequest(String request)
    {
        this.request = new StringBuilder();
        splitMediaRequest(request);
    }

    private int extractSizeFromBuffer(String request, int buffSize)
    {
        String sizeInStr = "";
        boolean start = false;
        for(int i=1; i<=buffSize; i++)
        {
            if(request.charAt(i) != 0)
                start = true;
            if(request.charAt(i) == 0 && !start)
                continue;
            else
                sizeInStr += (char)request.charAt(i);
        }

        return Integer.parseInt(sizeInStr);
    }

    private void splitMediaRequest(String request)
    {
        StringBuilder binarySegment = new StringBuilder();


        int size = extractSizeFromBuffer(request, 5);
        for(int i=0; i<size; i++)
            this.request.append(request.charAt(i+6));

        for(int i=size+6; i<request.length(); i++)
            binarySegment.append(request.charAt(i));

        try {
            binary = binarySegment.toString().getBytes("ISO-8859-1");
        }
        catch (UnsupportedEncodingException e)
        {
            e.printStackTrace();
        }
    }

    String getRequestSegment()
    {
        return request.toString();
    }

    byte[] getBinarySegment()
    {
        return binary;
    }
}
