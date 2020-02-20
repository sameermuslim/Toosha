package com.sameer.tashbih.alarm;

import android.annotation.TargetApi;
import android.app.Notification;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.ContextWrapper;
import android.content.Intent;
import android.content.res.Resources;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Build;

import androidx.core.app.NotificationCompat;
import androidx.core.content.ContextCompat;

import com.sameer.tashbih.MainActivity;
import com.sameer.tashbih.R;
    // NOTIFICATION CLASS
    //
public class NotificationHelper extends ContextWrapper {

    public static final String CHANNEL_ID ="THE PRAY_TIME";
    public static final String CHANNEL_NAME="Prayer Time";
    private static final int REMINDER_PENDING_INTENT_ID = 123;
    NotificationManager mManager;
    public NotificationHelper(Context base) {
        super(base);
        if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.O) {
            createChannel();
            getManager();
        }
    }
    @TargetApi(Build.VERSION_CODES.O)
    private void createChannel(){
           NotificationChannel prayTimeChannel = new NotificationChannel(CHANNEL_ID,CHANNEL_NAME, NotificationManager.IMPORTANCE_HIGH);
            prayTimeChannel.enableVibration(true);
            prayTimeChannel.setLightColor(R.color.colorPrimary);
            prayTimeChannel.enableLights(true);
            prayTimeChannel.setLockscreenVisibility(Notification.VISIBILITY_PRIVATE);
            mManager.createNotificationChannel(prayTimeChannel);

    }
    public NotificationManager getManager(){
        if (mManager==null)
        {
            mManager=(NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE);
        }
        return mManager;
    }
    public NotificationCompat.Builder getPrayTimeNotification(String title,String message)
    {
            return new NotificationCompat.Builder(getApplicationContext(),CHANNEL_ID)
                    .setColor(ContextCompat.getColor(getApplicationContext(),R.color.colorPrimary))
                    // .setLargeIcon(largeIcon(context))
                    .setSmallIcon(R.drawable.maamla_logo)
                    .setContentTitle(title)
                    .setContentText(message)
                    .setLargeIcon(largeIcon(getApplicationContext()))
                    .setContentIntent(NotificationUtils.contentIntent(getApplicationContext()))
                    .setAutoCancel(true);


    }
    private static Bitmap largeIcon(Context context){
        Resources res = context.getResources();

        Bitmap largeIcon= BitmapFactory.decodeResource(res , R.drawable.sport_icon);
        return largeIcon;
    }
    public static PendingIntent contentIntent(Context context) {

        Intent startActivityIntent = new Intent(context, MainActivity.class);
        startActivityIntent.putExtra("id",1);

        return PendingIntent.getActivity(context, REMINDER_PENDING_INTENT_ID, startActivityIntent, PendingIntent.FLAG_UPDATE_CURRENT);

    }
}
