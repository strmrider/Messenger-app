package com.example.messenger.models.installation;


import android.Manifest;
import android.app.Activity;
import android.content.pm.PackageManager;
import android.os.Environment;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;

import com.example.messenger.view.activities.MainActivity;

import java.io.File;

public class DirectoriesInit {

    public DirectoriesInit(){};

    public void init()
    {
        askPermission();
    }

    private void askPermission()
    {
        if (ContextCompat.checkSelfPermission(MainActivity.getContext(),
                Manifest.permission.WRITE_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED) {
            if (ActivityCompat.shouldShowRequestPermissionRationale(
                    (Activity)MainActivity.getContext(), Manifest.permission.WRITE_EXTERNAL_STORAGE))
            {
                System.out.println("showing explanation");
            }
            else {
                ActivityCompat.requestPermissions((Activity)MainActivity.getContext(),
                        new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE}, 100);
            }
        }
        else {
            setFolders();
        }
    }

    private void createFolder(String path)
    {
        File directory = new File(path);
        if (!directory.exists()) {
            System.out.println("(MediaMessage/writeToFile)- path os: " + path);
            System.out.println("(MediaMessage/writeToFile)- no directory, creating now...");
            if (!directory.mkdirs()) {
                System.out.println("(MediaMessage/writeToFile)- failed to create directory...");
            }
        }
    }

    private void setFolders() {
        String directory = Environment.getExternalStorageDirectory() + "/Messenger";
        createFolder(directory);
        String profiles = directory + "/profiles/images";
        createFolder(profiles);
        directory += "/Media";
        createFolder(directory);
        String images = directory + "/images";
        String videos = directory + "/videos";
        String audio = directory + "/audio";
        createFolder(images);
        createFolder(videos);
        createFolder(audio);
    }
}
