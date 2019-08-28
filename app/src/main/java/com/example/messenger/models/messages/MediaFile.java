package com.example.messenger.models.messages;

import android.os.Environment;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;

public class MediaFile {
    private String extension;
    private byte[] media;

    // from income message from server
    public MediaFile(byte[] media)
    {
        this.media = media;
    }

    // for sent message
    public MediaFile(String path)
    {
        extension = extractExtension(path);
        File file = new File(path);
        try {
            FileInputStream is = new FileInputStream(file);
            media = new byte[(int)file.length()];
            int count = 0;
            try {

                ByteArrayOutputStream buffer = new ByteArrayOutputStream();
                while ((count = is.read(media, 0, media.length)) != -1) {
                    buffer.write(media, 0, count);
                }
                buffer.flush();
                media = buffer.toByteArray();
                is.close();
                String str = new String(media, "ISO-8859-1");
            }
            catch (IOException e)
            {
                e.printStackTrace();
            }
        }
        catch (FileNotFoundException e)
        {
            e.printStackTrace();
        }

        createDirectory(Environment.getExternalStoragePublicDirectory
                (Environment.DIRECTORY_DCIM) + "/Messenger/");
    }

    private void createDirectory(String path)
    {
        File directory = new File(path);
        if (!directory.exists()) {
            if (!directory.mkdirs()) {
            }
        }
    }

    public void setExtension(String extension)
    {
        this.extension = extension;
    }
    public String getExtension()
    {
        return extension;
    }

    public byte[] getMedia(){
        return media;
    }

    private String extractExtension(String path)
    {
        String ext = "";
        if (path.charAt(path.length()-3) != '.')
            ext += path.charAt(path.length()-3);
        ext += path.charAt(path.length()-2);
        ext += path.charAt(path.length()-1);

        return ext;
    }

    public void writeMediaToFile(String path)
    {
        extension = extractExtension(path);
        File file = new File(path);
        try {
            FileOutputStream os = new FileOutputStream(file);
            //os.write(media, 0, media.length);
            os.write(media, 0, media.length);
            os.flush();
            os.close();
        }
        catch (FileNotFoundException e)
        {
            e.printStackTrace();
        }
        catch (IOException e)
        {
            e.printStackTrace();
        }
    }
}