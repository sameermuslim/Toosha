package com.sameer.tashbih;


import android.animation.ValueAnimator;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.os.Bundle;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.loader.app.LoaderManager;
import androidx.loader.content.CursorLoader;
import androidx.loader.content.Loader;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.Spinner;
import android.widget.TextView;

import com.sameer.tashbih.data.TooshaContract;
import com.sameer.tashbih.data.TooshaDbHelper;

import java.util.ArrayList;

import static android.content.ContentValues.TAG;
import static com.sameer.tashbih.data.TooshaContract.TasbihEntry.TASBIH_TABLE_NAME;


/**
 * A simple {@link Fragment} subclass.
 */
public class TasbihReportFragment extends Fragment implements LoaderManager.LoaderCallbacks<Cursor> {


    private TextView total_evaluate_tv, total_zaker_evaluate_tv, total_tasbih_evaluate_tv;
    private View mView;
    private SQLiteOpenHelper mDbHelper;
    private ArrayList<Tasbih> mAzkarList;
    private Context context;
    private RecyclerView mAzkarListView;
    private int total;
    private int tasbih_count;
    private Spinner filterSpn;
    ArrayAdapter<CharSequence> proprtyTypeAdapter;
   // private ArrayAdapter<CharSequence> filterAdapter;

    public TasbihReportFragment() {
        // Required empty public constructor
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setMenuVisibility(false);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        mView = inflater.inflate(R.layout.fragment_tasbih_reports, container, false);
        context = mView.getContext();
        total_evaluate_tv = mView.findViewById(R.id.total_evaluate_tv);
        total_zaker_evaluate_tv = mView.findViewById(R.id.total_evaluate_azker_tv);
        total_tasbih_evaluate_tv = mView.findViewById(R.id.total_evaluate_tasbih_tv);

        ImageView imageView = mView.findViewById(R.id.imageView);
        imageView.setClipToOutline(true);
       // Toast.makeText(context, "total" + total, Toast.LENGTH_SHORT).show();
        return mView;

    }



    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }


    private void initializingContent() {

        mDbHelper = new TooshaDbHelper(context);
        mAzkarList = new ArrayList<>();
        mAzkarListView = mView.findViewById(R.id.evaluateRecyclerView);

           displayDatabaseInfo();

        TasbihReportAdapter mTasbihEvaluateAdapter = new TasbihReportAdapter(mAzkarList);
        mAzkarListView.setLayoutManager(new GridLayoutManager(context, 2));
        mAzkarListView.setAdapter(mTasbihEvaluateAdapter);
      //  getLoaderManager().initLoader(222, null, this);
    }

    @Override
    public void setMenuVisibility(final boolean visible) {
        super.setMenuVisibility(visible);
        if (visible) {
         //   getLoaderManager().restartLoader(222,null,this);
            initializingContent();
        }
    }


    private void displayDatabaseInfo() {
        // Create and/or open a database to read from it
        SQLiteDatabase db = mDbHelper.getReadableDatabase();
        // Define a projection that specifies which columns from the database
        // you will actually use after this query.

        String[] projection = {
                TooshaContract.TasbihEntry.COLUMN_TASBIH_COUNT,
                TooshaContract.TasbihEntry.COLUMN_TASBIH_CATEGORY,
                " SUM(" + TooshaContract.TasbihEntry.COLUMN_TASBIH_TOTAL_COUNT + ") AS TOTALE"

        };
        String selection = TooshaContract.TasbihEntry.COLUMN_TASBIH_CATEGORY + " =?";

        String[] selectionArgs = {String.valueOf(TooshaContract.MORNING_AZKAR)};
        String groupBy = TooshaContract.TasbihEntry.COLUMN_TASBIH_CATEGORY;
        // String selection = TooshaContract.TasbihEntry.COLUMN_TASBIH_CATEGORY + "=?";

        // String[] selectionArgs = {String.valueOf(category_id)};
        // The table to query
        // The columns to return
        // The values for the WHERE clause
        // Don't group the rows
        // Don't filter by row groups

        /*Cursor cursor = getContentResolver().query(TasbihEntry.CONTENT_URI, projection, null, null, null);*/

        try (Cursor cursor = db.query(
                TASBIH_TABLE_NAME,   // The table to query
                projection,// The columns to return
                null,
                null,                 // The values for the WHERE clause
                groupBy,                // Don't group the rows
                null,                  // Don't filter by row groups
                null)) {
            // Create a header in the Text View that looks like this:
            //
            // The pets table contains <number of rows in Cursor> pets.
            // _id - name - breed - gender - weight
            //
            // In the while loop below, iterate through the rows of the cursor and display
            // the information from each column in this order.
//            displayView.setText("The pets table contains " + cursor.getCount() + " pets.\n\n");
//            displayView.append(TasbihEntry._ID + " - " +
//                    TasbihEntry.COLUMN_AMAL_DATE + " - " +
//                    TasbihEntry.COLUMN_AMAL_MORNING + " - " +
//                    TasbihEntry.COLUMN_AMAL_NOON + "\n");

            // Figure out the index of each column
            int countColumnIndex = cursor.getColumnIndex(TooshaContract.TasbihEntry.COLUMN_TASBIH_COUNT);
            int categoryColumnIndex = cursor.getColumnIndex(TooshaContract.TasbihEntry.COLUMN_TASBIH_CATEGORY);

            int total_index = cursor.getColumnIndex("TOTALE");

            total = 0;
            // Iterate through all the returned rows in the cursor
            while (cursor.moveToNext()) {
                // Use that index to extract the String or Int value of the word
                // at the current row the cursor is on.
                int count = cursor.getInt(countColumnIndex);
                int total_count = cursor.getInt(total_index);
                int category = cursor.getInt(categoryColumnIndex);
                total = total + total_count;
                Log.d(TAG, "displayDatabaseInfo: " + total);


                // Display the values from each column of the cur rent row in the cursor in the TextView

                mAzkarList.add(new Tasbih(1, count, 1, category, total_count));

                if (category == TooshaContract.TASBIH_CATEGORY) {
                    tasbih_count = total_count;
                }
                // Display the values from each column of the current row in the cursor in the TextView
            }
            startCountAnimation(total_evaluate_tv, total);

            startCountAnimation(total_tasbih_evaluate_tv, tasbih_count);
            startCountAnimation(total_zaker_evaluate_tv, (total - tasbih_count));

        }

    }


    private void startCountAnimation(final TextView textView, int count) {
        ValueAnimator animator = ValueAnimator.ofInt(0, count); //0 is min number, 600 is max number
        animator.setDuration(1000); //Duration is in milliseconds
        animator.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() {
            public void onAnimationUpdate(ValueAnimator animation) {
                String myIntAsString = String.format("%d ", animation.getAnimatedValue());
                textView.setText(myIntAsString);
            }
        });
        animator.start();
    }

    @NonNull
    @Override
    public Loader<Cursor> onCreateLoader(int i, @Nullable Bundle bundle) {

        String[] projection = {
                TooshaContract.TasbihEntry.COLUMN_TASBIH_COUNT,
                TooshaContract.TasbihEntry.COLUMN_TASBIH_CATEGORY,
                " SUM(" + TooshaContract.TasbihEntry.COLUMN_TASBIH_TOTAL_COUNT + ") AS TOTALE"

        };
        String selection = TooshaContract.TasbihEntry.COLUMN_TASBIH_CATEGORY + " =?";

        String[] selectionArgs = {String.valueOf(TooshaContract.MORNING_AZKAR)};
        String groupBy = TooshaContract.TasbihEntry.COLUMN_TASBIH_CATEGORY;
        return new CursorLoader(getContext(), TooshaContract.TasbihEntry.CONTENT_URI, projection, null, null, groupBy);
    }

    @Override
    public void onLoadFinished(@NonNull Loader<Cursor> loader, Cursor cursor) {
        int countColumnIndex = cursor.getColumnIndex(TooshaContract.TasbihEntry.COLUMN_TASBIH_COUNT);
        int categoryColumnIndex = cursor.getColumnIndex(TooshaContract.TasbihEntry.COLUMN_TASBIH_CATEGORY);

        int total_index = cursor.getColumnIndex("TOTALE");

        total = 0;
        // Iterate through all the returned rows in the cursor
        while (cursor.moveToNext()) {
            // Use that index to extract the String or Int value of the word
            // at the current row the cursor is on.
            int count = cursor.getInt(countColumnIndex);
            int total_count = cursor.getInt(total_index);
            int category = cursor.getInt(categoryColumnIndex);
            total = total + total_count;
            Log.d(TAG, "displayDatabaseInfo: " + total);


            // Display the values from each column of the cur rent row in the cursor in the TextView

            mAzkarList.add(new Tasbih(1,  count, 1, category, total_count));

            if (category == TooshaContract.TASBIH_CATEGORY) {
                tasbih_count = total_count;
            }
            // Display the values from each column of the current row in the cursor in the TextView
        }
        startCountAnimation(total_evaluate_tv, total);

        startCountAnimation(total_tasbih_evaluate_tv, tasbih_count);
        startCountAnimation(total_zaker_evaluate_tv, (total - tasbih_count));
    }

    @Override
    public void onLoaderReset(@NonNull Loader<Cursor> loader) {
            loader.reset();
    }
    // Always close the cursor when you're done reading from it. This releases all its
    // resources and makes it invalid.
}

