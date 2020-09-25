package com.wisewolf.njmschool;


import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.os.Build;

public class NotificationHelper {
    private Context mContext;
    private int NOTIFICATION_ID = 1;
    private Notification mNotification;
    private NotificationManager mNotificationManager;
    private PendingIntent mContentIntent;
    private CharSequence mContentTitle;
    Notification.Builder builder ;
    String name="";
    public NotificationHelper(Context context, String downloadFileName)
    {
        mContext = context;
        this.name=downloadFileName;
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            builder = new Notification.Builder(mContext,"1");
        }
        else {
            builder = new Notification.Builder(mContext);
        }
    }

    /**
     * Put the notification into the status bar
     */
    public void createNotification() {
        //get the notification manager
        mNotificationManager = (NotificationManager) mContext.getSystemService(Context.NOTIFICATION_SERVICE);


        //create the notification
        int icon = android.R.drawable.stat_sys_download;
        //TODO
        CharSequence tickerText = "mContext.getString(R.string.download_ticker);"; //Initial text that appears in the status bar
        long when = System.currentTimeMillis();
        mNotification = new Notification(icon, tickerText, when);

        //create the content which is shown in the notification pulldown
        //TODO
       // mContentTitle = mContext.getString(R.string.content_title);
        mContentTitle="";
        //Full title of the notification in the pull down
        CharSequence contentText = "0% complete"; //Text of the notification in the pull down

        //you have to set a PendingIntent on a notification to tell the system what you want it to do when the notification is selected
        //I don't want to use this here so I'm just creating a blank one
        Intent notificationIntent = new Intent();
        mContentIntent = PendingIntent.getActivity(mContext, 0, notificationIntent, 0);


        builder.setAutoCancel(false);
        builder.setContentTitle(mContentTitle);
        builder.setSmallIcon(R.drawable.logo_njms);
        builder.setContentIntent(mContentIntent);
        builder.setOngoing(true);
        builder.setOnlyAlertOnce(true);

        builder.setSubText("Download has started ...");
          //API level 16
        builder.setNumber(100);
        builder.build();

        mNotification = builder.getNotification();
        mNotificationManager.notify(NOTIFICATION_ID, mNotification);

        //add the additional content and intent to the notification
       // mNotification.setLatestEventInfo(mContext, mContentTitle, contentText, mContentIntent);

        //make this notification appear in the 'Ongoing events' section
        mNotification.flags = Notification.FLAG_ONGOING_EVENT;

        //show the notification
        mNotificationManager.notify(NOTIFICATION_ID, mNotification);
    }

    /**
     * Receives progress updates from the background task and updates the status bar notification appropriately
     * @param percentageComplete
     */
    public void progressUpdate(int percentageComplete) {
        //build up the new status message
        CharSequence contentText = percentageComplete + "% complete";
        //publish it to the status bar
        builder.setAutoCancel(false);
        builder.setOnlyAlertOnce(true);
        builder.setContentTitle(mContentTitle);
        builder.setSmallIcon(R.drawable.logo_njms);
        builder.setContentIntent(mContentIntent);
        builder.setOngoing(true);
builder.setContentText(contentText);
        builder.setSubText(name);
        //API level 16
        builder.setNumber(100);
        builder.build();

        mNotification = builder.getNotification();
        mNotificationManager.notify(NOTIFICATION_ID, mNotification);
    }

    /**
     * called when the background task is complete, this removes the notification from the status bar.
     * We could also use this to add a new ‘task complete’ notification
     */
    public void completed()    {
        //remove the notification from the status bar
        mNotificationManager.cancel(NOTIFICATION_ID);
    }
}