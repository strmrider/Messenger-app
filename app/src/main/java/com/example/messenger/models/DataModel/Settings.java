package com.example.messenger.models.DataModel;

public class Settings {
    private static String HostIP = ("192.168.14.23");
    private static int HostPort = 6800;
    private static String DatabaseName = "messenger";

    public static String getHostIP()
    {
        return HostIP;
    }

    public static int getHostPort()
    {
        return HostPort;
    }

    public static String getDBName()
    {
        return DatabaseName;
    }
}
