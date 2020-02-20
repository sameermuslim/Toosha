package com.sameer.tashbih;


import android.content.ContentValues;
import android.content.Intent;
import android.database.Cursor;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.view.Gravity;
import android.view.View;
import android.view.Window;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.widget.Toolbar;
import androidx.loader.app.LoaderManager;
import androidx.loader.content.CursorLoader;
import androidx.loader.content.Loader;
import androidx.recyclerview.widget.ItemTouchHelper;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.android.material.snackbar.Snackbar;
import com.sameer.tashbih.data.TooshaContract;
import com.sameer.tashbih.utils.GuidUtils;

import java.util.Objects;

import static com.sameer.tashbih.data.TooshaContract.DAY_NIGHT_AZkAR;
import static com.sameer.tashbih.data.TooshaContract.EVENING_AZKAR;
import static com.sameer.tashbih.data.TooshaContract.MORNING_AZKAR;
import static com.sameer.tashbih.data.TooshaContract.MOSQUE_AZKAR;
import static com.sameer.tashbih.data.TooshaContract.PRAYER_AZKAR;
import static com.sameer.tashbih.data.TooshaContract.SLEEP_AZKAR;
import static com.sameer.tashbih.data.TooshaContract.TASBIH_CATEGORY;

public class AzkarActivity extends BaseActivity implements LoaderManager.LoaderCallbacks<Cursor> {

    private static final String TAG = "AzkarActivity";
    private int category_id;
    private TasbihAdapter mTasbihAdapter;
    // for swiping the list item
    ItemTouchHelper.SimpleCallback itemTouchHelper = new ItemTouchHelper.SimpleCallback(0, ItemTouchHelper.LEFT | ItemTouchHelper.RIGHT) {
        @Override
        public boolean onMove(@NonNull RecyclerView recyclerView, @NonNull RecyclerView.ViewHolder viewHolder, @NonNull RecyclerView.ViewHolder viewHolder1) {
            return false;

        }

        @Override
        public void onSwiped(@NonNull RecyclerView.ViewHolder viewHolder, int i) {
            Toast.makeText(AzkarActivity.this, "Position " + i, Toast.LENGTH_SHORT).show();
          //  mTasbihAdapter.remove((TasbihAdapter.ViewHolder) viewHolder);
            Snackbar snackbar = Snackbar
                    .make(viewHolder.itemView, " removed from cart!", Snackbar.LENGTH_LONG);
            snackbar.setAction("UNDO", new View.OnClickListener() {
                @Override
                public void onClick(View view) {

                    // undo is selected, restore the deleted item
                    // mAdapter.restoreItem(deletedItem, deletedIndex);
                }
            });
            snackbar.setActionTextColor(Color.YELLOW);
            snackbar.show();
        }
    };
    private FloatingActionButton addbtn;
    private Animation zoomin, zoomout;
    private ImageView imageView;

    private void bindViews() {
        Toolbar toolbar = findViewById(R.id.azkar_toolbar);
        setSupportActionBar(toolbar);
        imageView = findViewById(R.id.pic_iv);
        Intent receivedIntent = getIntent();
        if (receivedIntent != null) {
            category_id = Objects.requireNonNull(receivedIntent.getExtras()).getInt("azkarCategory");
            addbtn = findViewById(R.id.addTasbihBtn);
            addbtn.hide();
            if (category_id == TASBIH_CATEGORY) {
                addbtn.show();
            // add Button On click show add Dialog
                addbtn.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        insertDialogBox();
                    }
                });
            }
            animatedImage(category_id);

            //   Toast.makeText(this, "category id is " + category_id, Toast.LENGTH_SHORT).show();
        }
    }


    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_azkar_scrolling);

        bindViews();

        GuidUtils.addNew(this);

        setAnimationToImage();

        RecyclerView mAzkarListView = findViewById(R.id.azkarRecycler);

        mAzkarListView.setLayoutManager(new LinearLayoutManager(this));
        mTasbihAdapter = new TasbihAdapter(this);
        new ItemTouchHelper(itemTouchHelper).attachToRecyclerView(mAzkarListView);
        mAzkarListView.setAdapter(mTasbihAdapter);
        //  getSupportLoaderManager().initLoader(121, null,  AzkarActivity.this);
        LoaderManager.getInstance(this).initLoader(121, null, this);


    }

    private void animatedImage(int category_id) {
        switch (category_id) {

            case SLEEP_AZKAR:
                imageView.setImageResource(R.drawable.isha_roport);
                break;
            case MORNING_AZKAR:
                imageView.setImageResource(R.drawable.fajr_report);
                break;
            case MOSQUE_AZKAR:
                imageView.setImageResource(R.drawable.murmur);
                break;
            case EVENING_AZKAR:
                imageView.setImageResource(R.drawable.maqhrib_report);
                break;
            case PRAYER_AZKAR:
                imageView.setImageResource(R.drawable.asr_report);
                break;
            case DAY_NIGHT_AZkAR:
                imageView.setImageResource(R.drawable.tahajod_report);
                break;
            default:
                Toast.makeText(this, "noting", Toast.LENGTH_SHORT).show();
        }
    }

    private void setAnimationToImage() {
        zoomin = AnimationUtils.loadAnimation(this, R.anim.zoomin);
        zoomout = AnimationUtils.loadAnimation(this, R.anim.zoomout);

        imageView.setAnimation(zoomin);
        imageView.setAnimation(zoomout);

        zoomout.setAnimationListener(new Animation.AnimationListener() {

            @Override
            public void onAnimationStart(Animation arg0) {
                // TODO Auto-generated method stub

            }

            @Override
            public void onAnimationRepeat(Animation arg0) {
                // TODO Auto-generated method stub

            }

            @Override
            public void onAnimationEnd(Animation arg0) {
                imageView.startAnimation(zoomin);

            }
        });
        zoomin.setAnimationListener(new Animation.AnimationListener() {

            @Override
            public void onAnimationStart(Animation arg0) {
                // TODO Auto-generated method stub

            }

            @Override
            public void onAnimationRepeat(Animation arg0) {
                // TODO Auto-generated method stub

            }

            @Override
            public void onAnimationEnd(Animation arg0) {
                imageView.startAnimation(zoomout);

            }
        });

    }

    private void insertDialogBox() {


        AlertDialog.Builder mBuilder = new AlertDialog.Builder(AzkarActivity.this);
        View mView = getLayoutInflater().inflate(R.layout.dialog_insert_tasbih, null);
        final EditText mArabic = mView.findViewById(R.id.arabicText);
        final EditText mPashto = mView.findViewById(R.id.pashtoText);
        final EditText mPersian = mView.findViewById(R.id.persianText);
        final EditText mEnglish = mView.findViewById(R.id.englishText);
        final EditText mAmount = mView.findViewById(R.id.amountText);
        final EditText fazilat = mView.findViewById(R.id.fazilatText);
        Button mSaveBtn = mView.findViewById(R.id.addBtn);


        mBuilder.setView(mView);
        final AlertDialog dialog = mBuilder.create();
        Window window = dialog.getWindow();
        Objects.requireNonNull(window).setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        window.setGravity(Gravity.BOTTOM);
        window.getAttributes().windowAnimations = R.style.DialogAnimation;
        dialog.show();
        mSaveBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (!mArabic.getText().toString().isEmpty() && !mAmount.getText().toString().isEmpty()) {
                    ContentValues customTasbih = new ContentValues();
                    customTasbih.put(TooshaContract.TasbihEntry.COLUMN_TASBIH_ARABIC, mArabic.getText().toString().trim());
                    customTasbih.put(TooshaContract.TasbihEntry.COLUMN_TASBIH_PASHTO, mPashto.getText().toString().trim());
                    customTasbih.put(TooshaContract.TasbihEntry.COLUMN_TASBIH_FARSI, mPersian.getText().toString().trim());
                    customTasbih.put(TooshaContract.TasbihEntry.COLUMN_TASBIH_ENGLISH, mEnglish.getText().toString().trim());
                    customTasbih.put(TooshaContract.TasbihEntry.COLUMN_TASBIH_FAZILAT, fazilat.getText().toString().trim());
                    customTasbih.put(TooshaContract.TasbihEntry.COLUMN_TASBIH_COUNT, Integer.valueOf(mAmount.getText().toString().trim()));
                    customTasbih.put(TooshaContract.TasbihEntry.COLUMN_TASBIH_DEFAULT_COUNT, Integer.valueOf(mAmount.getText().toString().trim()));
                    customTasbih.put(TooshaContract.TasbihEntry.COLUMN_TASBIH_CATEGORY, TASBIH_CATEGORY);
                    customTasbih.put(TooshaContract.TasbihEntry.COLUMN_TASBIH_TOTAL_COUNT, 0);
                    customTasbih.put(TooshaContract.TasbihEntry.COLUMN_TASBIH_STATE, 1);
                    Uri uri = getContentResolver().insert(TooshaContract.TasbihEntry.CONTENT_URI, customTasbih);
                    //   getSupportLoaderManager().restartLoader(121,null,AzkarActivity.this);
                    Log.d(TAG, "onClick: " + uri + " row inserted  ");

                    dialog.dismiss();
                } else {
                    Toast.makeText(AzkarActivity.this,
                            getString(R.string.required_fields),
                            Toast.LENGTH_SHORT).show();
                }
            }
        });


    }

    @NonNull
    @Override
    public Loader onCreateLoader(int i, @Nullable Bundle bundle) {

        String[] projection = {
                TooshaContract.TasbihEntry._ID,
                TooshaContract.TasbihEntry.COLUMN_TASBIH_ARABIC,
                TooshaContract.TasbihEntry.COLUMN_TASBIH_PASHTO,
                TooshaContract.TasbihEntry.COLUMN_TASBIH_FARSI,
                TooshaContract.TasbihEntry.COLUMN_TASBIH_ENGLISH,
                TooshaContract.TasbihEntry.COLUMN_TASBIH_FAZILAT,
                TooshaContract.TasbihEntry.COLUMN_TASBIH_COUNT,
                TooshaContract.TasbihEntry.COLUMN_TASBIH_CATEGORY,
                TooshaContract.TasbihEntry.COLUMN_TASBIH_STATE,
                TooshaContract.TasbihEntry.COLUMN_TASBIH_TOTAL_COUNT,


        };

        String selection = TooshaContract.TasbihEntry.COLUMN_TASBIH_CATEGORY + "=?";
        Log.d(TAG, "onCreateLoader: " + category_id);
        String[] selectionArgs = {String.valueOf(category_id)};
        // The table to query
        // The columns to return
        // The values for the WHERE clause
        // Don't group the rows
        // Don't filter by row groups

        return new CursorLoader(this, TooshaContract.TasbihEntry.CONTENT_URI, projection, selection, selectionArgs, null);

    }

    @Override
    public void onLoadFinished(@NonNull Loader<Cursor> loader, Cursor cursor) {
        mTasbihAdapter.swapCursor(cursor);
        Log.d(TAG, "count is " + cursor.getCount());
    }


    @Override
    public void onLoaderReset(@NonNull Loader loader) {

    }
}
