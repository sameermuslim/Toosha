package com.sameer.tashbih.alarm;

import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.content.res.Resources;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.media.RingtoneManager;
import android.os.Build;

import androidx.core.app.NotificationCompat;
import androidx.core.content.ContextCompat;

import com.sameer.tashbih.MainActivity;
import com.sameer.tashbih.R;

import static android.app.Notification.DEFAULT_SOUND;
import static android.app.Notification.DEFAULT_VIBRATE;

// NOTIFICATION CLASS
// managing the Notification and Channels

public class NotificationUtils {

    private static final String NOTIFICATION_CHANNEL_ID = "TOOSHA CHANNEL";
    private static final int REMINDER_PENDING_INTENT_ID = 123;

    // This method return PendingIntent so we can ues it to get the notification id
    public static PendingIntent contentIntent(Context context) {

        Intent startActivityIntent = new Intent(context, MainActivity.class);
        startActivityIntent.putExtra("id", 1);
        return PendingIntent.getActivity(context, REMINDER_PENDING_INTENT_ID, startActivityIntent, PendingIntent.FLAG_UPDATE_CURRENT);

    }

    // this method gives to us Bitmap of given id  so we can use it on notification large icon
    private static Bitmap largeIcon(Context context, int bigId) {
        Resources res = context.getResources();
        Bitmap largeIcon = BitmapFactory.decodeResource(res, bigId);
        return largeIcon;
    }

    // this static method call from onReceive class to show the specific notification
    public static void showNotification(String message, int bigID, Context context) {
        // use StringBuilder because we have more  customizable String
        StringBuilder str = new StringBuilder();
        str.append(context.getString(R.string.prayer_time));
        str.append(" ");
        str.append(message);
        str.append(" ");
        str.append(context.getString(R.string.minut_to));

        NotificationCompat.Builder builder = new NotificationCompat.Builder(context, context.getPackageName())
                .setColor(ContextCompat.getColor(context, R.color.colorPrimary)) // setting color
                .setLargeIcon(largeIcon(context, bigID))        //setting large icon
                .setSmallIcon(R.drawable.maamla_logo)           // setting small icon
                .setContentTitle(message)                       // title
                .setContentText(str)                            // content
                .setStyle(new NotificationCompat.BigTextStyle().bigText(str))    // this is style is for show ing mor content
                .setPriority(NotificationCompat.PRIORITY_HIGH)      // setting priority of show ing notification
                .setContentIntent(NotificationUtils.contentIntent(context))         // in onClick on notification where should go.
                .setSound(RingtoneManager.getDefaultUri(RingtoneManager.TYPE_ALARM)) // setting ringtone of alarm
                .setDefaults(DEFAULT_SOUND | DEFAULT_VIBRATE)
                .setAutoCancel(true);                           // cancel onSwipe notification
        //initializing NOTIFICATION MANAGER
        NotificationManager notificationManager = (NotificationManager) context.getSystemService(Context.NOTIFICATION_SERVICE);

        // asking for build Version  for handling notification channel
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            builder.setChannelId(context.getPackageName());
        }
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            // Initializing and setting Notification Channel
            NotificationChannel channel = new NotificationChannel(
                    context.getPackageName(),
                    NOTIFICATION_CHANNEL_ID,
                    NotificationManager.IMPORTANCE_HIGH
            );
            if (notificationManager != null) {
                // creating the channel
                notificationManager.createNotificationChannel(channel);
            }
        }

        //finally notify/creating the notification
        notificationManager.notify(REMINDER_PENDING_INTENT_ID, builder.build());
    }

}