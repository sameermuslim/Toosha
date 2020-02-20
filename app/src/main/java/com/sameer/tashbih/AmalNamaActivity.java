package com.sameer.tashbih;

import android.content.ContentValues;
import android.database.Cursor;
import android.graphics.Color;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;
import android.widget.CheckBox;
import android.widget.ImageView;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.loader.app.LoaderManager;
import androidx.loader.content.CursorLoader;
import androidx.loader.content.Loader;

import com.sameer.tashbih.data.TooshaContract;
import com.sameer.tashbih.data.TooshaContract.DailyAmalEntry;

import java.util.Calendar;
import java.util.Objects;

import devs.mulham.horizontalcalendar.HorizontalCalendar;
import devs.mulham.horizontalcalendar.HorizontalCalendarView;
import devs.mulham.horizontalcalendar.utils.HorizontalCalendarListener;

import static com.sameer.tashbih.Constants.FAUT;
import static com.sameer.tashbih.Constants.JAAMAT;
import static com.sameer.tashbih.Constants.MONFARED;
import static com.sameer.tashbih.Constants.QAZA;
import static com.sameer.tashbih.Constants.R_GROUP_ASIR;
import static com.sameer.tashbih.Constants.R_GROUP_ESHA;
import static com.sameer.tashbih.Constants.R_GROUP_FJIR;
import static com.sameer.tashbih.Constants.R_GROUP_MAQHRIB;
import static com.sameer.tashbih.Constants.R_GROUP_ZOHOR;
import static com.sameer.tashbih.data.TooshaContract.AmalEntry.ASR;
import static com.sameer.tashbih.data.TooshaContract.AmalEntry.CHARITY;
import static com.sameer.tashbih.data.TooshaContract.AmalEntry.FJR;
import static com.sameer.tashbih.data.TooshaContract.AmalEntry.ISHA;
import static com.sameer.tashbih.data.TooshaContract.AmalEntry.MAGHRIB;
import static com.sameer.tashbih.data.TooshaContract.AmalEntry.SPORT;
import static com.sameer.tashbih.data.TooshaContract.AmalEntry.STUDY;
import static com.sameer.tashbih.data.TooshaContract.AmalEntry.TAHAJUD;
import static com.sameer.tashbih.data.TooshaContract.AmalEntry.ZOHOR;
import static com.sameer.tashbih.data.TooshaContract.DailyAmalEntry.COLUMN_AMAL_DATE;
import static com.sameer.tashbih.data.TooshaContract.DailyAmalEntry.COLUMN_AMAL_ID;
import static com.sameer.tashbih.data.TooshaContract.DailyAmalEntry.COLUMN_AMAL_STATUS;
import static com.sameer.tashbih.utils.TooshaUtils.getToday;
import static com.sameer.tashbih.utils.TooshaUtils.imageCheckBox;
import static com.sameer.tashbih.utils.TooshaUtils.set_ch_data;
import static com.sameer.tashbih.utils.TooshaUtils.set_rb_data;

public class AmalNamaActivity extends BaseActivity implements View.OnClickListener, LoaderManager.LoaderCallbacks<Cursor> {
    static final int MIN_DISTANCE = 150;
    private static final int LOADER = 121;
    private static long THE_DATE=233422323;
    private static final String TAG = "amalActivity";
    RadioButton r_f_j, r_f_m, r_f_q,
            r_z_j, r_z_m, r_z_q,
            r_a_j, r_a_m, r_a_q,
            r_m_j, r_m_m, r_m_q,
            r_e_j, r_e_m, r_e_q;
    CheckBox tahajod_cb, murmur_cb, study_cb, sport_cb;
    private float x1;

    private TextView zohorText;
    private Calendar date;
    private HorizontalCalendar horizontalCalendar;
    private long today;
    private RadioGroup faget_Rgroup, zohor_Rgroup, aser_Rgroup, maghrib_Rgroup, esha_Rgroup;


    @Override
    protected void onStart() {
        super.onStart();
        Calendar date = Calendar.getInstance();
        today = getToday(date);
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_amal_nama);
        Objects.requireNonNull(getSupportActionBar()).setTitle(this.getString(R.string.amal_nama));

        //initializingContent
        initializingContents();
        //initializingHorizontalCalender
        horizontalDate();
        // getting current day date
        date = Calendar.getInstance();
        today = getToday(date);
        // Cursor loader start Querying
        LoaderManager.getInstance(this).initLoader(LOADER, null, this);

    }


    // working with horizontalDatePicker
    private void horizontalDate() {
        /* starts before 1 month from now */
        Calendar startDate = Calendar.getInstance();
        startDate.add(Calendar.MONTH, -1);

        /* ends in to Today */
        Calendar endDate = Calendar.getInstance();
        endDate.add(Calendar.MONTH, 0);

        // Default Date set to Today.
        horizontalCalendar = new HorizontalCalendar.Builder(this, R.id.calendarView)
                .range(startDate, endDate)
                .datesNumberOnScreen(5)
                .configure()
                .formatTopText("MMM")
                .formatMiddleText("dd")
                .formatBottomText("EEE")
                .showTopText(true)
                .showBottomText(true)
                .textColor(Color.LTGRAY, getResources().getColor(R.color.text_color))
                .colorTextMiddle(Color.DKGRAY, getResources().getColor(R.color.text_color))
                .end()
                .mode(HorizontalCalendar.Mode.DAYS)
//                .disableDates()
                .defaultSelectedDate(date)
                .build();
        horizontalCalendar.refresh();

        horizontalCalendar.setCalendarListener(new HorizontalCalendarListener() {
            @Override
            public void onDateSelected(Calendar thedate, int position) {
                selectData(thedate);
                // horizontalCalendar.refresh();
            }

            @Override
            public void onCalendarScroll(HorizontalCalendarView calendarView,
                                         int dx, int dy) {

            }

            @Override
            public boolean onDateLongClicked(Calendar date, int position) {
                //             horizontalCalendar.centerCalendarToPosition(position);
                return true;
            }
        });


    }

    @Override
    protected void onResume() {
        super.onResume();
        today=THE_DATE;


    }

    private void selectData(Calendar setDate) {

        today = getToday(setDate);
        THE_DATE=today;
        //  Toast.makeText(AmalNamaActivity.this, "date is " + formatDate(date.getTimeInMillis()), Toast.LENGTH_SHORT).show();
        int dayOfWeek = date.get(Calendar.DAY_OF_WEEK);
        if (dayOfWeek == 6) {
            zohorText.setText(getString(R.string.joma));
        } else {
            zohorText.setText(getString(R.string.zohor));
        }

        Log.d(TAG, "onDateSelected is: " + today);
        LoaderManager.getInstance(this).restartLoader(LOADER, null, this);
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {

            case R.id.r_f_j:
                updateAmal(JAAMAT, R_GROUP_FJIR);

                break;
            case R.id.r_f_m:
                updateAmal(MONFARED, R_GROUP_FJIR);
                break;

            case R.id.r_f_q:
                updateAmal(QAZA, R_GROUP_FJIR);
                break;

            case R.id.r_z_j:
                updateAmal(JAAMAT, R_GROUP_ZOHOR);

                break;
            case R.id.r_z_m:
                updateAmal(MONFARED, R_GROUP_ZOHOR);
                break;

            case R.id.r_z_q:
                updateAmal(QAZA, R_GROUP_ZOHOR);
                break;

            case R.id.r_a_j:
                updateAmal(JAAMAT, R_GROUP_ASIR);

                break;
            case R.id.r_a_m:
                updateAmal(MONFARED, R_GROUP_ASIR);
                break;

            case R.id.r_a_q:
                updateAmal(QAZA, R_GROUP_ASIR);
                break;

            case R.id.r_m_j:
                updateAmal(JAAMAT, R_GROUP_MAQHRIB);

                break;
            case R.id.r_m_m:
                updateAmal(MONFARED, R_GROUP_MAQHRIB);
                break;

            case R.id.r_m_q:
                updateAmal(QAZA, R_GROUP_MAQHRIB);
                break;

            case R.id.r_e_j:
                updateAmal(JAAMAT, R_GROUP_ESHA);

                break;
            case R.id.r_e_m:
                updateAmal(MONFARED, R_GROUP_ESHA);
                break;

            case R.id.r_e_q:
                updateAmal(QAZA, R_GROUP_ESHA);
                break;
            case R.id.image_murmur:
                int stateCharity = imageCheckBox(murmur_cb);
                updateAmal(stateCharity, CHARITY);
                break;
            case R.id.image_sport:
                int stateSport = imageCheckBox(sport_cb);
                updateAmal(stateSport, SPORT);
                break;
            case R.id.image_tahajod:
                int stateTahajod = imageCheckBox(tahajod_cb);
                updateAmal(stateTahajod, TAHAJUD);

                break;
            case R.id.image_study:
                int stateStudy = imageCheckBox(study_cb);
                updateAmal(stateStudy, STUDY);
                break;
            case R.id.ch_sport:
                int stateSport1 = imageCheckBox(sport_cb);
                updateAmal(stateSport1, SPORT);
                break;
            case R.id.ch_murmur:int
                    stateCharity1 = imageCheckBox(murmur_cb);
                updateAmal(stateCharity1, CHARITY);
                break;
            case R.id.ch_study:
                int stateStudy1 = imageCheckBox(study_cb);
                updateAmal(stateStudy1, STUDY);
                break;
            case R.id.ch_tahajod:
                int stateTahajod1= imageCheckBox(tahajod_cb);
                updateAmal(stateTahajod1, TAHAJUD);
                break;
            default:
                Toast.makeText(this, "Noting", Toast.LENGTH_SHORT).show();
        }
    }


    @NonNull
    @Override
    public Loader<Cursor> onCreateLoader(int i, @Nullable Bundle bundle) {

        Log.d(TAG, "onCreateLoader: created");
        String[] projection = {
                DailyAmalEntry.D_ID,
                DailyAmalEntry.COLUMN_AMAL_ID,
                COLUMN_AMAL_DATE,
                DailyAmalEntry.COLUMN_AMAL_STATUS,
        };
        String selection = COLUMN_AMAL_DATE + "=?";


        String[] selectionArgs = {String.valueOf(today)};

        return new CursorLoader(this, DailyAmalEntry.CONTENT_URI, projection, selection, selectionArgs, null);
    }

    @Override
    public void onLoadFinished(@NonNull Loader<Cursor> loader, Cursor cursor) {
        Log.d(TAG, "onLoadFinished: finished" + cursor.getCount());

        if (cursor.getCount() > 0) {
            //  Toast.makeText(this, "data count is " + mCursor.getCount(), Toast.LENGTH_SHORT).show();
            int idColumnIndex = cursor.getColumnIndex(DailyAmalEntry._ID);
            int dateColumnIndex = cursor.getColumnIndex(COLUMN_AMAL_DATE);
            int amalIdColumnIndex = cursor.getColumnIndex(DailyAmalEntry.COLUMN_AMAL_ID);
            int amalStatusColumnIndex = cursor.getColumnIndex(DailyAmalEntry.COLUMN_AMAL_STATUS);
            // Iterate through all the returned rows in the cursor
            Toast.makeText(this, "yes " + cursor.getCount(), Toast.LENGTH_SHORT).show();
            while (cursor.moveToNext()) {
                // Use that index to extract the String or Int value of the word
                // at the current row the cursor is on.

                long currentDate = cursor.getLong(dateColumnIndex);
                int amal_id = cursor.getInt(amalIdColumnIndex);
                int currentStatus = cursor.getInt(amalStatusColumnIndex);
                // Toast.makeText(this, "tahajod is " + currentTahajod, Toast.LENGTH_SHORT).show();


                switch (amal_id) {
                    case FJR:
                        Log.d(TAG, "status 1: " + amal_id);
                        set_rb_data(r_f_j, r_f_m, r_f_q, currentStatus, faget_Rgroup);
                        break;
                    case ZOHOR:
                        Log.d(TAG, "status 2: " + amal_id);
                        set_rb_data(r_z_j, r_z_m, r_z_q, currentStatus, zohor_Rgroup);
                        break;
                    case ASR:
                        Log.d(TAG, "status 3: " + amal_id);
                        set_rb_data(r_a_j, r_a_m, r_a_q, currentStatus, aser_Rgroup);
                        break;
                    case MAGHRIB:
                        Log.d(TAG, "status 4: " + amal_id);
                        set_rb_data(r_m_j, r_m_m, r_m_q, currentStatus, maghrib_Rgroup);
                        break;
                    case ISHA:
                        Log.d(TAG, "status 5: " + amal_id);
                        set_rb_data(r_e_j, r_e_m, r_e_q, currentStatus, esha_Rgroup);
                        break;
                    case TAHAJUD:
                        Log.d(TAG, "status 6: " + amal_id);
                        set_ch_data(tahajod_cb, currentStatus);
                        break;
                    case CHARITY:
                        Log.d(TAG, "status 7 : " + amal_id);
                        set_ch_data(murmur_cb, currentStatus);
                        break;
                    case STUDY:
                        Log.d(TAG, "status 8: " + amal_id);
                        set_ch_data(study_cb, currentStatus);
                        break;
                    case SPORT:
                        Log.d(TAG, "status 9: " + amal_id);
                        set_ch_data(sport_cb, currentStatus);
                        break;
                    default:
                        Log.d(TAG, "on default: " + currentStatus);
                }


                Log.d(TAG, "data is" + currentDate + "\n date " + currentDate);
            }

            // String a = formatDate(currentDate);

            // Display the values from each column of the current row in the cursor in the TextView

            Log.d(TAG, "selectData: run");
        } else {
            Log.d(TAG, "selectData: else run");
            Uri insert_data = insertData(today);

            LoaderManager.getInstance(this).restartLoader(LOADER, null, this);
            Toast.makeText(this, "cursor is null data inserted " + insert_data, Toast.LENGTH_SHORT).show();
            Log.d(TAG, "new data: " + insert_data);

        }


    }

    @Override
    public void onLoaderReset(@NonNull Loader<Cursor> loader) {
        loader.reset();

    }

    private Uri insertData(long thedate) {
        ContentValues fjr = new ContentValues();
        fjr.put(COLUMN_AMAL_DATE, thedate);
        fjr.put(COLUMN_AMAL_STATUS, FAUT);
        fjr.put(COLUMN_AMAL_ID, TooshaContract.AmalEntry.FJR);//1
        //
        getContentResolver().insert(DailyAmalEntry.CONTENT_URI, fjr);
        ContentValues zohor = new ContentValues();
        zohor.put(COLUMN_AMAL_DATE, thedate);
        zohor.put(COLUMN_AMAL_STATUS, FAUT);
        zohor.put(COLUMN_AMAL_ID, TooshaContract.AmalEntry.ZOHOR);//2
        getContentResolver().insert(DailyAmalEntry.CONTENT_URI, zohor);

        //
        ContentValues asr = new ContentValues();
        asr.put(COLUMN_AMAL_DATE, thedate);
        asr.put(COLUMN_AMAL_STATUS, FAUT);
        asr.put(COLUMN_AMAL_ID, TooshaContract.AmalEntry.ASR);//3
        //
        getContentResolver().insert(DailyAmalEntry.CONTENT_URI, asr);

        ContentValues maghrib = new ContentValues();
        maghrib.put(COLUMN_AMAL_DATE, thedate);
        maghrib.put(COLUMN_AMAL_STATUS, FAUT);
        maghrib.put(COLUMN_AMAL_ID, TooshaContract.AmalEntry.MAGHRIB);//4
        //
        getContentResolver().insert(DailyAmalEntry.CONTENT_URI, maghrib);

        ContentValues isha = new ContentValues();
        isha.put(COLUMN_AMAL_DATE, thedate);
        isha.put(COLUMN_AMAL_STATUS, FAUT);
        isha.put(COLUMN_AMAL_ID, TooshaContract.AmalEntry.ISHA);//5
        //
        getContentResolver().insert(DailyAmalEntry.CONTENT_URI, isha);

        ContentValues tahajud = new ContentValues();
        tahajud.put(COLUMN_AMAL_DATE, thedate);
        tahajud.put(COLUMN_AMAL_STATUS, FAUT);
        tahajud.put(COLUMN_AMAL_ID, TooshaContract.AmalEntry.TAHAJUD);//6
        //
        getContentResolver().insert(DailyAmalEntry.CONTENT_URI, tahajud);

        ContentValues charity = new ContentValues();
        charity.put(COLUMN_AMAL_DATE, thedate);
        charity.put(COLUMN_AMAL_STATUS, FAUT);
        charity.put(COLUMN_AMAL_ID, TooshaContract.AmalEntry.CHARITY);//7
        //
        getContentResolver().insert(DailyAmalEntry.CONTENT_URI, charity);

        ContentValues study = new ContentValues();
        study.put(COLUMN_AMAL_DATE, thedate);
        study.put(COLUMN_AMAL_STATUS, FAUT);
        study.put(COLUMN_AMAL_ID, TooshaContract.AmalEntry.STUDY);//8
        //
        getContentResolver().insert(DailyAmalEntry.CONTENT_URI, study);

        ContentValues sport = new ContentValues();
        sport.put(COLUMN_AMAL_DATE, thedate);
        sport.put(COLUMN_AMAL_STATUS, FAUT);
        sport.put(COLUMN_AMAL_ID, TooshaContract.AmalEntry.SPORT);//9
        //
        Uri insert_data = getContentResolver().insert(DailyAmalEntry.CONTENT_URI, sport);

        return insert_data;
    }

    private void updateAmal(int statusValue, int amal_id) {
        ContentValues amalValue = new ContentValues();
        amalValue.put("status", statusValue);
        String selection = DailyAmalEntry.COLUMN_AMAL_ID + "=? and " + COLUMN_AMAL_DATE + "=?";
        String[] selectionArgs = {String.valueOf(amal_id), String.valueOf(today)};
        long result = getContentResolver().update(DailyAmalEntry.CONTENT_URI, amalValue, selection, selectionArgs);
        Log.d(TAG, "updateAmal: " + result);
    }


    // touchEvent for sliding right and left
    @Override
    public boolean onTouchEvent(MotionEvent event) {
        switch (event.getAction()) {
            case MotionEvent.ACTION_DOWN:
                x1 = event.getX();
                break;
            case MotionEvent.ACTION_UP:
                float x2 = event.getX();
                float deltaX = x2 - x1;

                if (Math.abs(deltaX) > MIN_DISTANCE) {
                    // Left to Right swipe action
                    if (x2 > x1) {

                        int position = horizontalCalendar.getSelectedDatePosition();
                        horizontalCalendar.centerCalendarToPosition(position + 1);

                        // Toast.makeText(this, "Left to Right swipe [Next]", Toast.LENGTH_SHORT).show();
                    }

                    // Right to left swipe action               \
                    else {
                        int position = horizontalCalendar.getSelectedDatePosition();
                        horizontalCalendar.centerCalendarToPosition(position - 1);

                        //horizontalCalendar.contains(Date date);

                        // Toast.makeText(this, "Right to Left swipe [Previous]", Toast.LENGTH_SHORT).show();
                    }

                } else {
                    // consider as something else - a screen tap for example
                    Toast.makeText(this, "slide", Toast.LENGTH_SHORT).show();
                }
                break;
        }
        return super.onTouchEvent(event);
    }


    private void initializingContents() {
        zohorText = findViewById(R.id.zohr_tv);
        ImageView murmur_image = findViewById(R.id.image_murmur);
        murmur_image.setOnClickListener(this);
        ImageView study_image = findViewById(R.id.image_study);
        study_image.setOnClickListener(this);

        ImageView tahajod_image = findViewById(R.id.image_tahajod);
        tahajod_image.setOnClickListener(this);


        ImageView sport_image = findViewById(R.id.image_sport);
        sport_image.setOnClickListener(this);


        //initiliazingRadioButtons

        faget_Rgroup = findViewById(R.id.radioGroup_fager);
        faget_Rgroup.setOnClickListener(this);
        zohor_Rgroup = findViewById(R.id.radioGroup_zohor);
        zohor_Rgroup.setOnClickListener(this);
        aser_Rgroup = findViewById(R.id.radioGroup_aser);
        aser_Rgroup.setOnClickListener(this);
        maghrib_Rgroup = findViewById(R.id.radioGroup_maghrib);
        maghrib_Rgroup.setOnClickListener(this);
        esha_Rgroup = findViewById(R.id.radioGroup_esha);
        esha_Rgroup.setOnClickListener(this);

        //fager RadioButtons
        r_f_j = findViewById(R.id.r_f_j);
        r_f_j.setOnClickListener(this);
        r_f_m = findViewById(R.id.r_f_m);
        r_f_m.setOnClickListener(this);
        r_f_q = findViewById(R.id.r_f_q);
        r_f_q.setOnClickListener(this);

        //zohor RadioButtons

        r_z_j = findViewById(R.id.r_z_j);
        r_z_j.setOnClickListener(this);

        r_z_m = findViewById(R.id.r_z_m);
        r_z_m.setOnClickListener(this);

        r_z_q = findViewById(R.id.r_z_q);
        r_z_q.setOnClickListener(this);

        //Aser RadioButtons

        r_a_j = findViewById(R.id.r_a_j);
        r_a_j.setOnClickListener(this);

        r_a_m = findViewById(R.id.r_a_m);
        r_a_m.setOnClickListener(this);

        r_a_q = findViewById(R.id.r_a_q);
        r_a_q.setOnClickListener(this);

        //Maghrib RadioButtons

        r_m_j = findViewById(R.id.r_m_j);
        r_m_j.setOnClickListener(this);
        r_m_m = findViewById(R.id.r_m_m);
        r_m_m.setOnClickListener(this);
        r_m_q = findViewById(R.id.r_m_q);
        r_m_q.setOnClickListener(this);

        //Esha RadioButtons

        r_e_j = findViewById(R.id.r_e_j);
        r_e_j.setOnClickListener(this);
        r_e_m = findViewById(R.id.r_e_m);
        r_e_m.setOnClickListener(this);
        r_e_q = findViewById(R.id.r_e_q);
        r_e_q.setOnClickListener(this);


        //initilaizingCheckBox
        tahajod_cb = findViewById(R.id.ch_tahajod);
        murmur_cb = findViewById(R.id.ch_murmur);
        study_cb = findViewById(R.id.ch_study);
        sport_cb = findViewById(R.id.ch_sport);
        sport_cb.setOnClickListener(this);
        study_cb.setOnClickListener(this);
        murmur_cb.setOnClickListener(this);
        tahajod_cb.setOnClickListener(this);


    }


}
