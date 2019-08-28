package com.example.messenger.models.DataModel;


import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.support.v4.app.NotificationCompat;
import android.support.v4.app.TaskStackBuilder;

import com.example.messenger.R;
import com.example.messenger.models.messages.LastMessage;
import com.example.messenger.view.activities.ChatActivity;

import org.jetbrains.annotations.NotNull;

class Notification {
    private Context context;
    private NotificationCompat.Builder noteBuilder;

    Notification(Context context, LastMessage message)
    {
        this.context = context;
        noteBuilder = new NotificationCompat.Builder(context);
        displayNote(message);
    }

    private void setProperties(LastMessage message)
    {
        noteBuilder.setSmallIcon(R.drawable.ic_stat_name);
        noteBuilder.setContentTitle(message.actualContactPerson);
        noteBuilder.setContentText(message.getText());
    }

    private void setClickAction(LastMessage message)
    {
        Intent intent = new Intent(context, ChatActivity.class);
        intent.putExtra("username", message.sender);
        intent.putExtra("displayName", message.actualContactPerson);
        TaskStackBuilder taskStackBuilder = TaskStackBuilder.create(context);
        taskStackBuilder.addParentStack(ChatActivity.class);

        taskStackBuilder.addNextIntent(intent);
        PendingIntent resultPendingIntent =
                taskStackBuilder.getPendingIntent(0, PendingIntent.FLAG_UPDATE_CURRENT);
        noteBuilder.setContentIntent(resultPendingIntent);
        noteBuilder.setAutoCancel(true);
    }

    private void displayNote(LastMessage message)
    {
        setProperties(message);
        setClickAction(message);

        NotificationManager notificationManager =
                (NotificationManager)context.getSystemService(Context.NOTIFICATION_SERVICE);

        try {
            notificationManager.notify(0, noteBuilder.build());
        }
        catch (NullPointerException e)
        {
            e.printStackTrace();
        }
    }
}
