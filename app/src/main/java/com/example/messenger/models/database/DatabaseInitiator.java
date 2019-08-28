package com.example.messenger.models.database;

import android.content.ContentValues;
import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteException;


import static android.content.Context.MODE_PRIVATE;

public class DatabaseInitiator {
    private SQLiteDatabase database;

    public DatabaseInitiator(SQLiteDatabase database) {
        this.database = database;
    }

    public DatabaseInitiator(Context context, String dbName) {
        database = context.openOrCreateDatabase(dbName, MODE_PRIVATE, null);
    }

    public void initial() {
        dropTables();
        createTables();
    }

    private void dropTables() {
        database.execSQL("DROP TABLE IF EXISTS Chats");
        database.execSQL("DROP TABLE IF EXISTS Messages");
        database.execSQL("DROP TABLE IF EXISTS Contacts");
        database.execSQL("DROP TABLE IF EXISTS Settings");
    }

    private void createTables() {
        database.execSQL("CREATE TABLE IF NOT EXISTS Chats(username VARCHAR, " +
                "displayName VARCHAR, " +
                "unread INTEGER," +
                "members VARCHAR," +
                "silenced INT);");

        database.execSQL("CREATE TABLE IF NOT EXISTS Messages( " +
                "id VARCHAR, " +
                "sender VARCHAR, " +
                "receiver VARCHAR, " +
                "text VARCHAR, " +
                "sendingDate VARCHAR, " +
                "receivingData VARCHAR, " +
                "type INT, " +
                "status INT, " +
                "path VARCHAR);");

        database.execSQL("CREATE TABLE IF NOT EXISTS Contacts(username VARCHAR, " +
                "displayName VARCHAR, " +
                "status VARCHAR, " +
                "registered INT);");

        cleanSettings();
    }

    private void cleanSettings()
    {
        database.execSQL("DROP TABLE IF EXISTS Settings");
        database.execSQL("CREATE TABLE IF NOT EXISTS settings(username VARCHAR, " +
                "status VARCHAR, " +
                "password VARCHAR);");
    }

    public void settings(String username, String password)
    {
        cleanSettings();
        ContentValues insertValues = new ContentValues();
        insertValues.put("username", username);
        insertValues.put("password", password);
        insertValues.put("status", "Default status");

        database.insert("Settings", null, insertValues);
    }


    public static boolean checkDataBase(Context context, String dbName)
    {
        boolean result;
        String path = context.getDatabasePath(dbName).getAbsolutePath();
        try {
            SQLiteDatabase database =
                    SQLiteDatabase.openDatabase(path, null,
                            SQLiteDatabase.OPEN_READWRITE);
            if (database != null) {
                result = true;
                database.close();
            }
            else
                result = false;
        }
        catch (SQLiteException e)
        {
            return false;
        }

        return result;
    }

}
