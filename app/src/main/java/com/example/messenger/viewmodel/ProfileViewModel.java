package com.example.messenger.viewmodel;

import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.net.Uri;
import android.provider.MediaStore;

import com.example.messenger.models.DataModel.DataModel;
import com.example.messenger.models.messages.MediaFile;
import com.example.messenger.models.requests.RequestType;

import java.io.UnsupportedEncodingException;

import static android.app.Activity.RESULT_OK;

public class ProfileViewModel {
    private String username;
    private String status;
    private Context context;
    private DataModel dataModel;
    private String selectedFile;

    public ProfileViewModel(Context context)
    {
        dataModel = DataModel.getModel();
        this.context = context;
        this.username = DataModel.getUsername();
        this.status = dataModel.getProfile().getStatus();
    }

    public void selectFileFromGallery(int requestCode, int resultCode, Intent data)
    {
        if (requestCode == 1 && resultCode == RESULT_OK && null != data) {
            Uri selectedImage = data.getData();
            String[] filePathColumn = {MediaStore.Images.Media.DATA};

            Cursor cursor = context.getContentResolver().query(selectedImage,
                    filePathColumn, null, null, null);
            cursor.moveToFirst();

            int columnIndex = cursor.getColumnIndex(filePathColumn[0]);
            selectedFile = cursor.getString(columnIndex);
            cursor.close();
        }
    }

    public String getSelectedFile()
    {
        return selectedFile;
    }

    private RequestType getRequestType(String status)
    {
        boolean statusChanged = !this.status.equals(status);

        if (statusChanged && selectedFile != null)
            return RequestType.PROFILE_UPDATE;
        else if (statusChanged)
            return RequestType.STATUS_UPDATE;
        else if (selectedFile != null)
            return RequestType.PROFILE_IMAGE_UPDATE;
        else
            return null;
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

    private String updateRequest(String status)
    {
        RequestType type = getRequestType(status);
        if (type == null)
            return null;

        String request = "<request type='"+type.getValue()+"'>";
        request += "<username>" + username + "</username>";
        if (type == RequestType.PROFILE_UPDATE || type == RequestType.STATUS_UPDATE)
            request += ("<status>" + status + "</status>");
        request += "</request>";

        String xmlSize = getSizeFormat(request);
        if(selectedFile != null)
        {
            MediaFile mediaFile = new MediaFile(selectedFile);
            try {
                request += new String(mediaFile.getMedia(), "ISO-8859-1");
            }
            catch (UnsupportedEncodingException e)
            {
                e.printStackTrace();
            }
            request = ("M"+xmlSize + request);
        }
        return request;
    }

    public void done(String status)
    {
        String request = updateRequest(status);
        dataModel.getProfile().setStatus(status);
        dataModel.getServer().sendDataToServer(request);
    }

    public String getUsername()
    {
        return DataModel.getUsername();
    }

    public String getStatus()
    {
        return dataModel.getProfile().getStatus();
    }
}