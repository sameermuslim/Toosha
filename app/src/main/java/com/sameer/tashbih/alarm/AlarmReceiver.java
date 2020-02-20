package com.sameer.tashbih.alarm;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.util.Log;

import com.sameer.tashbih.data.TooshaDbHelper;
import com.sameer.tashbih.utils.TooshaUtils;

import static android.content.ContentValues.TAG;

public class AlarmReceiver extends BroadcastReceiver {
    @Override
    public void onReceive(Context context, Intent intent) {
        // get id of notification
        if (intent.getIntExtra("bigID", 20) == 10) {
            Log.d(TAG, "onReceive: the Zkir update is run");
            //initialize DB
            TooshaDbHelper tooshaDbHelper = new TooshaDbHelper(context);
            //Updating the Azkars in 12:00
            tooshaDbHelper.updateAzkar(context);
        long newAmal  =  tooshaDbHelper.insertNewAmal(context, TooshaUtils.getToday());
            Log.d(TAG, "onReceive: in 12:00 new amal add "+newAmal);
        } else {
            // show notification
            NotificationUtils.showNotification(intent.getStringExtra("time"), intent.getIntExtra("bigID", 3), context);
        }

    }
}
