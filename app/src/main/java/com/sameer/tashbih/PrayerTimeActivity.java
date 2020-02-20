package com.sameer.tashbih;

import android.app.Activity;
import android.app.AlarmManager;
import android.app.PendingIntent;
import android.app.TimePickerDialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Typeface;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.TextView;
import android.widget.TimePicker;
import android.widget.Toast;

import com.getkeepsafe.taptargetview.TapTarget;
import com.getkeepsafe.taptargetview.TapTargetView;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.sameer.tashbih.alarm.AlarmReceiver;
import com.sameer.tashbih.utils.GuidUtils;
import com.sameer.tashbih.utils.LocalUtils;
import com.tomerrosenfeld.customanalogclockview.CustomAnalogClock;

import java.text.DateFormat;
import java.util.Calendar;

public class PrayerTimeActivity extends BaseActivity implements View.OnClickListener {

    private static final String TAG = "Prayer TimeActivity";
    TextView txtDate, txtTime;
    private int mYear, mMonth, mDay, mHour, mMinute;
    private SharedPreferences sharedPreferences;
    private SharedPreferences.Editor shareEditor;
    private CustomAnalogClock customAnalogAsr, customAnalogTime, customAnalogFajir, customAnalogZohor, customAnalogMaqhrib, customAnalogEsha, customAnalogJuma;
    private AlarmManager alarmManager;
    private PendingIntent pendingIntent;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
       // GuidUtils.datePicker();
        sharedPreferences = getApplicationContext().getSharedPreferences("Prayers", MODE_PRIVATE);

        long fajerTime = sharedPreferences.getLong("FajerTime", 0);
        long zohorTime = sharedPreferences.getLong("ZohorTime", 0);
        long aserTime = sharedPreferences.getLong("AsirTime", 0);
        long maghribTime = sharedPreferences.getLong("MaghribTime", 0);
        long eshaTime = sharedPreferences.getLong("EshaTime", 0);
        long jumaTime = sharedPreferences.getLong("jumaTime", 0);


      //  Toast.makeText(this, "" + aserTime, Toast.LENGTH_SHORT).show();

        setContentView(R.layout.activity_prayer_time);
        customAnalogFajir = findViewById(R.id.analog_clockFajir);
        customAnalogFajir.setTime(aserTime);
        customAnalogZohor = findViewById(R.id.analog_clockZohor);
        customAnalogAsr = findViewById(R.id.analog_clockAsr);
        customAnalogMaqhrib = findViewById(R.id.analog_clockMaqhrib);
        customAnalogEsha = findViewById(R.id.analog_clockEsha);
        customAnalogJuma = findViewById(R.id.analog_clockJuma);
        customAnalogTime = findViewById(R.id.analog_clockTime);
        customAnalogFajir.setScale(0.5f);
        customAnalogZohor.setScale(0.5f);
        customAnalogAsr.setScale(0.5f);
        customAnalogMaqhrib.setScale(0.5f);
        customAnalogEsha.setScale(0.5f);
        customAnalogJuma.setScale(0.5f);


        txtTime = findViewById(R.id.zohorText);


        customAnalogFajir.setOnClickListener(this);
        customAnalogZohor.setOnClickListener(this);
        customAnalogAsr.setOnClickListener(this);
        customAnalogMaqhrib.setOnClickListener(this);
        customAnalogEsha.setOnClickListener(this);
        customAnalogJuma.setOnClickListener(this);

        Calendar now = Calendar.getInstance();
        now.set(Calendar.HOUR, 0);
        now.set(Calendar.MINUTE, 23);
        now.set(Calendar.SECOND, 0);
        now.set(Calendar.HOUR_OF_DAY, 0);
        // customAnalogClock.setAutoUpdate(true);
        // customAnalogClock.setFace(R.drawable.clo);
        customAnalogFajir.init(this, R.drawable.clo, R.drawable.hours, R.drawable.themin, 0, false, false);
        // customAnalogFajir.setTime(now.getTimeInMillis());

        customAnalogZohor.init(this, R.drawable.clo, R.drawable.hours, R.drawable.themin, 0, false, true);
        customAnalogAsr.init(this, R.drawable.clo, R.drawable.hours, R.drawable.themin, 0, false, true);
        customAnalogMaqhrib.init(this, R.drawable.clo, R.drawable.hours, R.drawable.themin, 0, false, true);
        customAnalogEsha.init(this, R.drawable.clo, R.drawable.hours, R.drawable.themin, 0, false, true);
        customAnalogJuma.init(this, R.drawable.clo, R.drawable.hours, R.drawable.themin, 0, false, true);
        customAnalogTime.init(this, R.drawable.clo, R.drawable.hours, R.drawable.themin, 0, false, true);
        customAnalogTime.setAutoUpdate(true);

        customAnalogFajir.setTime(fajerTime);
        customAnalogZohor.setTime(zohorTime);
        customAnalogAsr.setTime(aserTime);
        customAnalogMaqhrib.setTime(maghribTime);
        customAnalogEsha.setTime(eshaTime);
        customAnalogJuma.setTime(jumaTime);
        torget(R.id.analog_clockAsr);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.analog_clockFajir:
                setClockTime(1,getResources().getString(R.string.fager),"FajerTime", customAnalogFajir,R.drawable.fajr_report);
                break;
            case R.id.analog_clockZohor:
                setClockTime(2,getResources().getString(R.string.zohor),"ZohorTime", customAnalogZohor,R.drawable.dohor_report);
                break;
            case R.id.analog_clockAsr:
                setClockTime(3,getResources().getString(R.string.aser),"AsirTime", customAnalogAsr,R.drawable.asr_report);
                break;
            case R.id.analog_clockMaqhrib:
                setClockTime(4,getResources().getString(R.string.maghrib),"MaghribTime", customAnalogMaqhrib,R.drawable.maqhrib_report);
                break;
            case R.id.analog_clockEsha:
                setClockTime(5,getResources().getString(R.string.esha),"EshaTime", customAnalogEsha,R.drawable.isha_roport);
                break;

            case R.id.analog_clockJuma:
                setClockTime(6,getResources().getString(R.string.joma),"jumaTime", customAnalogJuma,R.drawable.dohor_report);
                break;
            default:
                Toast.makeText(this, "empty View", Toast.LENGTH_SHORT).show();

        }
    }

    public void setClockTime(final int prayID, final String stringName, final String preferenceName, final CustomAnalogClock customAnalog,final int bigID) {


        // Get Current Time
        final Calendar c = Calendar.getInstance();
        mHour = c.get(Calendar.HOUR_OF_DAY);
        mMinute = c.get(Calendar.MINUTE);
        Log.d(TAG, "setClockTime: "+DateFormat.getDateTimeInstance(DateFormat.SHORT, DateFormat.MEDIUM).format(c.getTimeInMillis()));

        // Launch Time Picker Dialog
        TimePickerDialog timePickerDialog = new TimePickerDialog(this,
                new TimePickerDialog.OnTimeSetListener() {

                    @Override
                    public void onTimeSet(TimePicker view, int hourOfDay,
                                          int minute) {
                        shareEditor = sharedPreferences.edit();
                        c.set(Calendar.HOUR_OF_DAY, hourOfDay);
                        c.set(Calendar.MINUTE, minute);
                        view.setBackgroundColor(getResources().getColor(R.color.colorPrimary));
                        customAnalog.setTime(c);
                        shareEditor.putLong(preferenceName, c.getTimeInMillis());
                        shareEditor.apply();
                        Log.d(TAG, "setClockTime: "+DateFormat.getDateTimeInstance(DateFormat.SHORT, DateFormat.MEDIUM).format(c.getTimeInMillis()));

                        final Intent my_intent = new Intent(PrayerTimeActivity.this, AlarmReceiver.class);
                            my_intent.putExtra("time",stringName);
                            my_intent.putExtra("bigID",bigID);



                        alarmManager = (AlarmManager) getSystemService(Context.ALARM_SERVICE);
                        Log.d(TAG, "setClockTime: "+DateFormat.getDateTimeInstance(DateFormat.SHORT, DateFormat.MEDIUM).format(c.getTimeInMillis()));

                        //create a pending intent that delay the intent
                        //until the specified calender time
                        pendingIntent = PendingIntent.getBroadcast(PrayerTimeActivity.this,  prayID,
                                my_intent, PendingIntent.FLAG_UPDATE_CURRENT);
                        if(alarmManager!=null){
                            alarmManager.cancel(pendingIntent);
                        }

                        if(c.before(Calendar.getInstance())){
                            c.add(Calendar.DATE, 1);
                        }


//                //set the alarm manager
//                alarmManager.set(AlarmManager.RTC_WAKEUP,calendar.getTimeInMillis(),
//                        pendingIntent);

                        ///....
                        alarmManager.setInexactRepeating(AlarmManager.RTC_WAKEUP, c.getTimeInMillis(),
                                AlarmManager.INTERVAL_DAY, pendingIntent);
                        //.........
                        Log.d(TAG, "setClockTime: "+DateFormat.getDateTimeInstance(DateFormat.SHORT, DateFormat.MEDIUM).format(c.getTimeInMillis()-900000));

                        Toast.makeText(PrayerTimeActivity.this, "alarm added " +DateFormat.getDateTimeInstance(DateFormat.SHORT, DateFormat.MEDIUM).format(c.getTimeInMillis()), Toast.LENGTH_SHORT).show();

                    }
                }, mHour, mMinute, false);

        timePickerDialog.show();

    }

    public void torget(int id){


        final FloatingActionButton floatingActionButton = (FloatingActionButton) findViewById(R.id.addTasbihBtn);

        TapTargetView.showFor(this, TapTarget.forView(findViewById(R.id.analog_clockAsr), getString(R.string.newـ), getString(R.string.new_desc))

                .outerCircleAlpha(0.96f)            // Specify the alpha amount for the outer circle
                .titleTextSize(20)                  // Specify the size (in sp) of the title text
                .descriptionTextSize(10)            // Specify the size (in sp) of the description text
                .textTypeface(Typeface.SANS_SERIF)  // Specify a typeface for the text
                .drawShadow(true)                   // Whether to draw a drop shadow or not
                .cancelable(false)                  // Whether tapping outside the outer circle dismisses the view
                .tintTarget(false)                   // Whether to tint the target view's color
                .targetRadius(60)                  // Specify the target radius (in dp)
                .transparentTarget(true)

                .cancelable(false),
                new TapTargetView.Listener() {


            public void onTargetCancel(TapTargetView tapTargetView) {
                super.onTargetCancel(tapTargetView);


            }

            public void onTargetClick(TapTargetView tapTargetView) {
                super.onTargetClick(tapTargetView);


            }
        });
    }


}
