package com.sameer.tashbih.utils;

import android.app.AlarmManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Build;
import android.os.VibrationEffect;
import android.os.Vibrator;
import android.util.Log;
import android.view.Gravity;
import android.view.View;
import android.view.Window;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AlertDialog;

import com.sameer.tashbih.R;
import com.sameer.tashbih.alarm.AlarmReceiver;

import java.text.DateFormat;
import java.util.Calendar;

import static android.content.ContentValues.TAG;
import static android.view.View.inflate;

public class TooshaUtils {

    public static long getWeekAgoDate() {
        Calendar weekBefore = Calendar.getInstance();
        weekBefore.add(Calendar.DAY_OF_YEAR, -6);
        weekBefore.set(Calendar.HOUR_OF_DAY, 0);
        weekBefore.set(Calendar.MINUTE, 0);
        weekBefore.set(Calendar.SECOND, 0);
        weekBefore.set(Calendar.MILLISECOND, 0);
        return weekBefore.getTimeInMillis();

    }

    public static long getToday() {
        Calendar toDay = Calendar.getInstance();
        toDay.set(Calendar.HOUR_OF_DAY, 0);
        toDay.set(Calendar.MINUTE, 0);
        toDay.set(Calendar.SECOND, 0);
        toDay.set(Calendar.MILLISECOND, 0);
        return toDay.getTimeInMillis();
    }

    public static long getMonthAgoDate() {
        Calendar weekBefore = Calendar.getInstance();
        weekBefore.add(Calendar.DAY_OF_YEAR, -30);
        weekBefore.set(Calendar.HOUR_OF_DAY, 0);
        weekBefore.set(Calendar.MINUTE, 0);
        weekBefore.set(Calendar.SECOND, 0);
        weekBefore.set(Calendar.MILLISECOND, 0);
        return weekBefore.getTimeInMillis();

    }


    public static void set_ch_data(CheckBox checkBox, int value) {
        if (value == 1) {
            checkBox.setChecked(true);
        } else {
            checkBox.setChecked(false);
        }
    }

    public static void set_rb_data(RadioButton jamat, RadioButton moqim, RadioButton qaza, int currentValue, RadioGroup radioGroup) {
        switch (currentValue) {

            case 1:
                jamat.setChecked(true);
                break;
            case 2:
                moqim.setChecked(true);
                break;
            case 3:
                qaza.setChecked(true);
                break;
            case 0:
                clearing_wightes(radioGroup);

        }
    }

    public static void clearing_wightes(RadioGroup radioGroup) {
        // clearing radio boxes

        radioGroup.clearCheck();


    }


    // Utils
    public static int get_ch_data(CheckBox checkBox) {
        if (checkBox.isChecked()) {
            return 1;
        } else {
            return 0;
        }
    }

    public static int get_rb_data(RadioButton rbJamat, RadioButton rbMoqim, RadioButton rbQaza) {
        if (rbJamat.isChecked()) {
            return 1;
        } else if (rbMoqim.isChecked()) {

            return 2;
        } else if (rbQaza.isChecked()) {
            return 3;
        } else {
            return 0;
        }

    }

    public static int imageCheckBox(CheckBox checkBox) {
        int state = 0;
        if (checkBox.isChecked()) {
            checkBox.setChecked(false);
        } else if (!checkBox.isChecked()) {
            checkBox.setChecked(true);
            state = 1;
        }
        return state;
    }

    public static long getToday(Calendar date) {
        date.set(Calendar.HOUR_OF_DAY, 0);
        date.set(Calendar.MINUTE, 0);
        date.set(Calendar.SECOND, 0);
        date.set(Calendar.MILLISECOND, 0);
        return date.getTimeInMillis();

    }

    public static void vibrate(Vibrator vibrator, int amount) {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            vibrator.vibrate(VibrationEffect.createOneShot(amount, VibrationEffect.DEFAULT_AMPLITUDE));
        } else {
            vibrator.vibrate(amount);
        }
    }

    public static void showDialog(Context context, String title, String content) {
        AlertDialog.Builder mBuilder = new AlertDialog.Builder(context);
        View mView = inflate(context, R.layout.dialog_message_box, null);
        Button mClose = (Button) mView.findViewById(R.id.close_dialog_Btn);
        TextView mTitle = (TextView) mView.findViewById(R.id.dialog_message_title);
        mTitle.setText(title);
        TextView mContent = (TextView) mView.findViewById(R.id.dialog_message_content);
        mBuilder.setView(mView);
        mContent.setText(content);
        final AlertDialog dialog = mBuilder.create();
        Window window = dialog.getWindow();
        window.setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        window.setGravity(Gravity.CENTER);
        window.getAttributes().windowAnimations = R.style.DialogAnimation;
        dialog.show();
        mClose.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                dialog.dismiss();
            }
        });

    }

    public static void setupdateAlarm(Context context) {
        final Intent my_intent = new Intent(context, AlarmReceiver.class);
        my_intent.putExtra("bigID", 10);

        Calendar calendar = Calendar.getInstance();
        calendar.set(Calendar.HOUR_OF_DAY, 0);
        calendar.set(Calendar.MINUTE, 0);
        calendar.set(Calendar.SECOND, 0);
        Log.d(TAG, "setClockTime: " + DateFormat.getDateTimeInstance(DateFormat.SHORT, DateFormat.MEDIUM).format(calendar.getTimeInMillis()));


        AlarmManager alarmManager = (AlarmManager) context.getSystemService(Context.ALARM_SERVICE);
        Log.d(TAG, "setClockTime: " + DateFormat.getDateTimeInstance(DateFormat.SHORT, DateFormat.MEDIUM).format(calendar.getTimeInMillis()));

        //create a pending intent that delay the intent
        //until the specified calender time
        PendingIntent pendingIntent = PendingIntent.getBroadcast(context, 10,
                my_intent, PendingIntent.FLAG_UPDATE_CURRENT);
        if (alarmManager != null) {
            alarmManager.cancel(pendingIntent);
        }

        if (calendar.before(Calendar.getInstance())) {
            calendar.add(Calendar.DATE, 1);
        }


//                //set the alarm manager
//                alarmManager.set(AlarmManager.RTC_WAKEUP,calendar.getTimeInMillis(),
//                        pendingIntent);

        ///....
        alarmManager.setInexactRepeating(AlarmManager.RTC_WAKEUP, calendar.getTimeInMillis(),
                AlarmManager.INTERVAL_DAY, pendingIntent);
        //.........
        Log.d(TAG, "setClockTime: " + DateFormat.getDateTimeInstance(DateFormat.SHORT, DateFormat.MEDIUM).format(calendar.getTimeInMillis() - 900000));

        Toast.makeText(context, "alarm added " + DateFormat.getDateTimeInstance(DateFormat.SHORT, DateFormat.MEDIUM).format(calendar.getTimeInMillis()), Toast.LENGTH_SHORT).show();


        ///....
        alarmManager.setInexactRepeating(AlarmManager.RTC_WAKEUP, calendar.getTimeInMillis(),
                AlarmManager.INTERVAL_DAY, pendingIntent);
        //.........

    }

    public String formatDate(long date) {
        return String.format("%1$tA %1$tb %1$td %1$tY at %1$tI:%1$tM %1$Tp", date);

    }

}
