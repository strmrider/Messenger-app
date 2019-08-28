package com.example.messenger.models.messages;

import android.Manifest;
import android.app.Activity;
import android.content.pm.PackageManager;
import android.os.Build;
import android.os.Environment;
import android.support.v4.app.ActivityCompat;

import com.example.messenger.view.activities.MainActivity;

import java.io.File;

public class MediaMessage extends MessageBody {
    private String text;
    private String path;
    private MediaFile mediaFile;
    private boolean isPath;
    private String extension;

    // for sent message or from database
    public MediaMessage(MessageType type, String text, String path, boolean loadFile)
    {
        super(type);
        this.text = text;
        this.path = path;
        if(loadFile) {
            isPath = false;
            mediaFile = new MediaFile(path);
            extension = mediaFile.getExtension();
        }
        else {
            isPath = true;
        }
    }

    // for income message from server
    public MediaMessage(MessageType type, String text, String extension, byte[] media)
    {
        super(type);
        this.text = text;
        mediaFile = new MediaFile(media);
        this.extension = extension;
        mediaFile.setExtension(extension);
        isPath = false;
    }

    byte[] getMedia()
    {
        return mediaFile.getMedia();
    }

    public String getPath()
    {
        return path;
    }

    public String getFullPath()
    {
        return path;
    }

    public String getExtension()
    {
        return extension;
    }

    private String getSuitablePath(String filename)
    {
        String path = Environment.getExternalStorageDirectory() + "/Messenger/media/";
        if (type == MessageType.IMAGE)
            path += "images/";
        else if (type == MessageType.VIDEO)
            path += "videos/";
        else if (type == MessageType.AUDIO)
            path += "audio/";
        path += filename;

        return path;
    }

    void writeToFile(String filename)
    {
        File directory = new File(getSuitablePath(filename));

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M && MainActivity.getContext().checkSelfPermission
                (Manifest.permission.WRITE_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions((Activity)MainActivity.getContext(),
                    new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE}, 100);
            if (!directory.exists()) {
                if (!directory.mkdirs()) {
                    System.out.println("(MediaMessage/writeToFile)- failed to create directory...");
                }
            }
        }
        this.path = (directory.getPath());
        mediaFile.writeMediaToFile(path);
        mediaFile = null;
        isPath = true;
    }

    public String getText()
    {
        return text;
    }

    @Override
    public String xml() {
        return ("<body>" +
                "<text>" + text + "</text>" +
                "<extension>" + mediaFile.getExtension() + "</extension>" +
                "</body>");
    }
}