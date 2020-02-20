package com.sameer.tashbih;

import android.app.Activity;
import android.content.ContentValues;
import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteOpenHelper;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Build;
import android.os.Bundle;
import android.os.VibrationEffect;
import android.os.Vibrator;
import android.preference.PreferenceManager;
import android.util.Log;
import android.view.Gravity;
import android.view.KeyEvent;
import android.view.View;
import android.view.Window;
import android.view.animation.LinearInterpolator;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.loader.app.LoaderManager;
import androidx.loader.content.CursorLoader;
import androidx.loader.content.Loader;

import com.sameer.tashbih.data.TooshaContract;
import com.sameer.tashbih.data.TooshaDbHelper;
import com.sameer.tashbih.utils.TooshaUtils;

import java.util.Objects;

import antonkozyriatskyi.circularprogressindicator.CircularProgressIndicator;

import static android.view.View.inflate;
import static com.sameer.tashbih.utils.TooshaUtils.vibrate;


public class TasbihWindow extends BaseActivity implements LoaderManager.LoaderCallbacks<Cursor> {

    private static final String TAG = "TWindow";
    Vibrator vibrator;
    ImageView translate_mv, info_mv;
    private int currentID;
    private TextView counter_tv, total_count, step_count, arabic_tv;
    private int count;
    private CircularProgressIndicator circularProgress;
    private int val;
    private ImageView resetBtn;
    private String farsi;
    private int id;
    private int theCount;
    private String arabic;
    private String pashto;
    private String english;
    private int category;
    private int state;
    private int total;
    private Context context;
    private String fazilat;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_tasbih_window);
        context = this;

        intiliazingContent();

        Toast.makeText(this, "Arabic is " + arabic, Toast.LENGTH_SHORT).show();
        displayDatabaseInfo();
        getSupportLoaderManager().initLoader(111, null, this);

        resetBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {


                AlertDialog.Builder mBuilder = new AlertDialog.Builder(TasbihWindow.this);
                View mView = inflate(TasbihWindow.this, R.layout.dialog_delete_tasbih, null);
                Button mDelete = (Button) mView.findViewById(R.id.delete_dialog_Btn);
                mDelete.setText(getString(R.string.reset_text));
                TextView mTitle = (TextView) mView.findViewById(R.id.dialog_title);
                TextView mContent = (TextView) mView.findViewById(R.id.dialog_content);
                mTitle.setText(R.string.reseting_text);
                mContent.setText(R.string.do_you_reset);
                Button mCancel = (Button) mView.findViewById(R.id.delete_dialog_cancel);
                mBuilder.setView(mView);
                final AlertDialog dialog = mBuilder.create();
                Window window = dialog.getWindow();
                window.setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
                window.setGravity(Gravity.BOTTOM);
                window.getAttributes().windowAnimations = R.style.DialogAnimation;
                dialog.show();
                mDelete.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        ContentValues values = new ContentValues();
                        values.put(TooshaContract.TasbihEntry.COLUMN_TASBIH_COUNT, count);
                        values.put(TooshaContract.TasbihEntry.COLUMN_TASBIH_TOTAL_COUNT, 0);

                        String selection = TooshaContract.TasbihEntry._ID + "=?";

                        String[] selectionArgs = {String.valueOf(currentID)};

                        getContentResolver().update(TooshaContract.TasbihEntry.CONTENT_URI, values, selection, selectionArgs);
                        step_count.setText(String.format("%d", 0));
                        counter_tv.setText(String.format("%d", 0));
                        circularProgress.setCurrentProgress(0);

                        dialog.dismiss();
                    }
                });
                mCancel.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        dialog.dismiss();
                    }
                });


            }
        });

    }

    @Override
    protected void onDestroy() {
        Log.d(TAG, "onDestroy: after");
        Toast.makeText(this, "Destroy", Toast.LENGTH_SHORT).show();
        super.onDestroy();
        Toast.makeText(this, "after on destroy", Toast.LENGTH_SHORT).show();
        Log.d(TAG, "onDestroy: befor");
    }

    @Override
    public boolean dispatchKeyEvent(KeyEvent event) {
        int action = event.getAction();
        int keyCode = event.getKeyCode();
        switch (keyCode) {
            case KeyEvent.KEYCODE_VOLUME_UP:
                if (action == KeyEvent.ACTION_DOWN) {
                    //TODO
                    updateCount();
                }
                return true;
            case KeyEvent.KEYCODE_VOLUME_DOWN:
                if (action == KeyEvent.ACTION_DOWN) {
                    //TODO
                    updateCount();
                }
                return true;
            default:
                return super.dispatchKeyEvent(event);
        }
    }


    private void intiliazingContent() {
        resetBtn = findViewById(R.id.reset_btn);
        circularProgress = findViewById(R.id.progress_circular);
        Intent receivedIntent = getIntent();
        currentID = Objects.requireNonNull(receivedIntent.getExtras()).getInt("currentID");

        counter_tv = findViewById(R.id.counter_tv);
        total_count = findViewById(R.id.tatal_count);
        step_count = findViewById(R.id.steps_tv);
        arabic_tv = findViewById(R.id.arabic_tv);
        translate_mv = findViewById(R.id.translate_tv);
        info_mv=findViewById(R.id.info);
        vibrator = (Vibrator) getSystemService(VIBRATOR_SERVICE);
        step_count.setText(String.format("%d", 0));
    }

    public void progressbar(int max) {

        circularProgress.setInterpolator(new LinearInterpolator());
        circularProgress.setMaxProgress(max);
        circularProgress.setOnProgressChangeListener(new CircularProgressIndicator.OnProgressChangeListener() {
            @Override
            public void onProgressChanged(double progress, double maxProgress) {
                Log.d("PROGRESS", String.format("Current: %1$.0f, max: %2$.0f", progress, maxProgress));
                //     Toast.makeText(TasbihWindow.this, "run " + progress, Toast.LENGTH_SHORT).show();
            }
        });
        circularProgress.setShouldDrawDot(true);
        // circularProgress.setFillBackgroundEnabled(true);
        circularProgress.setAnimationEnabled(true);
        circularProgress.setFocusableInTouchMode(true);

        int endColor = Color.MAGENTA;

        /* Must be one of:
         *  - LINEAR_GRADIENT
         *  - RADIAL_GRADIENT
         *  - SWEEP_GRADIENT
         *  - NO_GRADIENT
         * */
        int gradientType = CircularProgressIndicator.LINEAR_GRADIENT;

        //   circularProgress.setGradient(gradientType, endColor);
        circularProgress.setTextSizePx(100);
        circularProgress.setFillBackgroundEnabled(true);
        circularProgress.getGradientType(); //returns LINEAR_GRADIENT
// you can set max and current progress values individually
        Log.d(TAG, "meme " + max);
        circularProgress.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                updateCount();
            }
        });
// you can get progress values using following getters
        circularProgress.getProgress(); // returns 5000
        circularProgress.getMaxProgress(); // returns 10000


    }

    private void displayDatabaseInfo() {
        // Display the values from each column of the current row in the cursor in the TextView
        translate_mv.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String strLanguage = PreferenceManager.getDefaultSharedPreferences(TasbihWindow.this).getString(TasbihWindow.this.getString(R.string.pref_language_key), TasbihWindow.this.getString(R.string.pref_persian_value));


                AlertDialog.Builder mBuilder = new AlertDialog.Builder(TasbihWindow.this);
                View mView = inflate(TasbihWindow.this, R.layout.dialog_message_box, null);
                Button mClose = (Button) mView.findViewById(R.id.close_dialog_Btn);
                TextView mContent = (TextView) mView.findViewById(R.id.dialog_message_content);
                mBuilder.setView(mView);
                assert strLanguage != null;
                switch (strLanguage) {
                    case "ps":
                        mContent.setText(pashto);
                        break;
                    case "eng":
                        mContent.setText(english);
                        break;
                    default:
                        mContent.setText(farsi);
                        break;
                }
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
        });
        Toast.makeText(this, "meme " + count, Toast.LENGTH_SHORT).show();

        // Display the values from each column of the current row in the cursor in the TextView
        info_mv.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                TooshaUtils.showDialog(TasbihWindow.this,getString(R.string.fazilat),fazilat);
            }
        });

    }
    // Always close the cursor when you're done reading from it. This releases all its
    // resources and makes it invalid.


    public void updateTasbih(View view) {
        updateCount();
    }

    public void updateCount() {

        int itemCount = Integer.valueOf((String) counter_tv.getText().toString().trim());
        //     double counts = circularProgress.getProgress();

        if (itemCount >= count) {
            itemCount = 0;
            vibrate(vibrator, 100);

            counter_tv.setText(String.format("%d", 0));
            circularProgress.setCurrentProgress(0);
            int step = Integer.valueOf((String) step_count.getText().toString().trim());
            step_count.setText(String.format("%d", step += 1));
        } else {

            counter_tv.setText(String.format("%d", itemCount += 1));
            Toast.makeText(this, "k" + itemCount, Toast.LENGTH_SHORT).show();

            circularProgress.setCurrentProgress(itemCount);
            total_count.setText(String.format("%d", total));
            // Create a ContentValues object where column names are the keys,
            // and Toto's pet attributes are the values.
            ContentValues values = new ContentValues();
            values.put(TooshaContract.TasbihEntry.COLUMN_TASBIH_COUNT, count);
            values.put(TooshaContract.TasbihEntry.COLUMN_TASBIH_TOTAL_COUNT, total + 1);


            // Insert a new row for Toto in the database, returning the ID of that new row.
            // The first argument for db.insertAmal() is the pets table name.
            // The second argument provides the name of a column in which the framework
            // can insertAmal NULL in the event that the ContentValues is empty (if
            // this is set to "null", then the framework will not insertAmal a row when
            // there are no values).
            // The third argument is the ContentValues object containing the info for Toto.

            String selection = TooshaContract.TasbihEntry._ID + "=?";

            String[] selectionArgs = {String.valueOf(currentID)};
            long newRowId = getContentResolver().update(TooshaContract.TasbihEntry.CONTENT_URI, values, selection, selectionArgs);

            Log.d(TAG, "onClick: new row updateAzkar " + newRowId);
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
                vibrator.vibrate(VibrationEffect.createOneShot(10, VibrationEffect.DEFAULT_AMPLITUDE));
            } else {
                vibrate(vibrator, 10);
            }


        }


    }

    @NonNull
    @Override
    public Loader<Cursor> onCreateLoader(int i, @Nullable Bundle bundle) {

        String[] projection = {
                TooshaContract.TasbihEntry._ID,
                TooshaContract.TasbihEntry.COLUMN_TASBIH_ARABIC,
                TooshaContract.TasbihEntry.COLUMN_TASBIH_FARSI,
                TooshaContract.TasbihEntry.COLUMN_TASBIH_PASHTO,
                TooshaContract.TasbihEntry.COLUMN_TASBIH_ENGLISH,
                TooshaContract.TasbihEntry.COLUMN_TASBIH_FAZILAT,
                TooshaContract.TasbihEntry.COLUMN_TASBIH_COUNT,
                TooshaContract.TasbihEntry.COLUMN_TASBIH_CATEGORY,
                TooshaContract.TasbihEntry.COLUMN_TASBIH_STATE,
                TooshaContract.TasbihEntry.COLUMN_TASBIH_TOTAL_COUNT,


        };
        String selection = TooshaContract.TasbihEntry._ID + "=?";

        String[] selectionArgs = {String.valueOf(currentID)};

        return new CursorLoader(this, TooshaContract.TasbihEntry.CONTENT_URI, projection, selection, selectionArgs, null);
    }

    @Override
    public void onLoadFinished(@NonNull Loader<Cursor> loader, Cursor cursor) {
        if (cursor.moveToFirst()) {
            //  Toast.makeText(this, "cursor is "+cursor.getCount(), Toast.LENGTH_SHORT).show();
            id = cursor.getInt(cursor.getColumnIndex(TooshaContract.TasbihEntry._ID));
            arabic = cursor.getString(cursor.getColumnIndex(TooshaContract.TasbihEntry.COLUMN_TASBIH_ARABIC));
            pashto = cursor.getString(cursor.getColumnIndex(TooshaContract.TasbihEntry.COLUMN_TASBIH_PASHTO));
            farsi = cursor.getString(cursor.getColumnIndex(TooshaContract.TasbihEntry.COLUMN_TASBIH_FARSI));
            english = cursor.getString(cursor.getColumnIndex(TooshaContract.TasbihEntry.COLUMN_TASBIH_ENGLISH));
            fazilat = cursor.getString(cursor.getColumnIndex(TooshaContract.TasbihEntry.COLUMN_TASBIH_FAZILAT));
            theCount = cursor.getInt(cursor.getColumnIndex(TooshaContract.TasbihEntry.COLUMN_TASBIH_COUNT));
            total = cursor.getInt(cursor.getColumnIndex(TooshaContract.TasbihEntry.COLUMN_TASBIH_TOTAL_COUNT));
            category = cursor.getInt(cursor.getColumnIndex(TooshaContract.TasbihEntry.COLUMN_TASBIH_CATEGORY));
            state = cursor.getInt(cursor.getColumnIndex(TooshaContract.TasbihEntry.COLUMN_TASBIH_STATE));

            arabic_tv.setText(arabic);
//        translate_tv.setText(farsiData);
            total_count.setText(String.format("%d", total));
            Log.d(TAG, "meme second" + theCount);
            //  counter_tv.setText(String.valueOf(theCount));
            progressbar(theCount);
            count = theCount;
            //
        } else {
            Toast.makeText(this, "Cursor is null", Toast.LENGTH_SHORT).show();
        }
    }

    @Override
    public void onLoaderReset(@NonNull Loader<Cursor> loader) {

    }
}
