package com.sameer.tashbih;

import android.content.Context;
import android.content.SharedPreferences;
import android.database.Cursor;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.RadioButton;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.loader.app.LoaderManager;
import androidx.loader.content.CursorLoader;
import androidx.loader.content.Loader;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.github.mikephil.charting.charts.BarChart;
import com.github.mikephil.charting.components.Legend;
import com.github.mikephil.charting.components.XAxis;
import com.github.mikephil.charting.components.YAxis;
import com.github.mikephil.charting.data.BarData;
import com.github.mikephil.charting.data.BarDataSet;
import com.github.mikephil.charting.data.BarEntry;
import com.github.mikephil.charting.formatter.DefaultAxisValueFormatter;
import com.github.mikephil.charting.utils.ColorTemplate;

import java.util.ArrayList;
import java.util.Objects;

import static android.content.Context.MODE_PRIVATE;
import static com.sameer.tashbih.data.TooshaContract.BASE_CONTENT_URI;
import static com.sameer.tashbih.data.TooshaContract.PATH_AMAL_REPORT_VIEW_MONTH;
import static com.sameer.tashbih.data.TooshaContract.PATH_AMAL_REPORT_VIEW_WEEK;

public class AmalReportFragment extends Fragment implements LoaderManager.LoaderCallbacks<Cursor>, RadioButton.OnClickListener {

    /**
     * The fragment argument representing the section number for this
     * fragment.
     */
    private static final String ARG_SECTION_NUMBER = "section_number";
    private static final String TAG = "azkar";
    private static final int LOADER_WEEK = 7;
    private static final int LOADER_MONTH = 30;
    private static View mView;
    private static Context context;
    private RecyclerView mAzkarListView;
    private AmalReportAdapter mAmalReportAdapter;

    public AmalReportFragment() {
    }

    /**
     * Returns a new instance of this fragment for the given section
     * number.
     */
    public static AmalReportFragment newInstance(int sectionNumber) {
        AmalReportFragment fragment = new AmalReportFragment();
        Bundle args = new Bundle();
        args.putInt(ARG_SECTION_NUMBER, sectionNumber);
        fragment.setArguments(args);
        return fragment;
    }

    public static void barchat(int jamat, int moqim, int fowt, int qaza) {

        BarChart barChart = mView.findViewById(R.id.barchart);
        barChart.setDrawBarShadow(true);

        barChart.setDrawValueAboveBar(true);
        barChart.setFitBars(true);
        barChart.setHighlightFullBarEnabled(true);
        barChart.setClipChildren(true);
        barChart.setVisibleXRange(4, 5);
        barChart.setMaxHighlightDistance(40);
        barChart.setMaxVisibleValueCount(40);
        barChart.setMaxHighlightDistance(40f);
        barChart.setAutoScaleMinMaxEnabled(false);
        barChart.setVisibleXRangeMaximum(40);
        barChart.setVisibleYRangeMaximum(40, null);
        barChart.getDescription().setEnabled(false);
        barChart.setDrawValueAboveBar(true);
        Legend l = barChart.getLegend();
        l.setVerticalAlignment(Legend.LegendVerticalAlignment.BOTTOM);
        l.setHorizontalAlignment(Legend.LegendHorizontalAlignment.LEFT);
        l.setOrientation(Legend.LegendOrientation.HORIZONTAL);
        l.setForm(Legend.LegendForm.CIRCLE);


        barChart.animateXY(1400, 1400);

        ArrayList<BarEntry> yValue = new ArrayList<>();

        yValue.add(new BarEntry(1, jamat));//green
        yValue.add(new BarEntry(2, qaza));//yellow
        yValue.add(new BarEntry(3, fowt));//red
        yValue.add(new BarEntry(4, moqim));//blue


        BarDataSet set;

        set = new BarDataSet(yValue, context.getString(R.string.totalAmal));
        set.setColors(ColorTemplate.MATERIAL_COLORS);
        set.setValueFormatter(new DefaultAxisValueFormatter(0));


        BarData data = new BarData(set);
        data.setValueTextSize(18);
        barChart.setData(data);

//        setData(4, 30, jamat, moqim, fowt, qaza);
        XAxis xAxis = barChart.getXAxis();
        xAxis.setPosition(XAxis.XAxisPosition.BOTTOM);
        xAxis.setDrawLabels(false);


        xAxis.setValueFormatter(new DefaultAxisValueFormatter(0));

        xAxis.setLabelCount(4);
        xAxis.setXOffset(50);
        xAxis.setDrawGridLines(false);
        xAxis.setValueFormatter(new DefaultAxisValueFormatter(0));
        YAxis yAxisLeft = barChart.getAxisLeft();
        yAxisLeft.setLabelCount(5, false);
        yAxisLeft.setSpaceTop(15);
        yAxisLeft.setDrawGridLines(false);
        yAxisLeft.setDrawLabels(false);
        yAxisLeft.setValueFormatter(new DefaultAxisValueFormatter(0));
        YAxis yAxisRight = barChart.getAxisRight();

        yAxisRight.setDrawGridLines(false);
        yAxisRight.setValueFormatter(new DefaultAxisValueFormatter(0));

    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        //setUserVisibleHint(true);

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        mView = inflater.inflate(R.layout.fragment_reports, container, false);
        context = mView.getContext();
        initializingContent();
        mAzkarListView.setLayoutManager(new GridLayoutManager(context, 1));
        mAmalReportAdapter = new AmalReportAdapter(getActivity());
        mAzkarListView.setAdapter(mAmalReportAdapter);

//        RBmonth.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
//            @Override
//            public void onCheckedChanged(CompoundButton compoundButton, boolean b) {
//                LOADER_ID = LOADER_MONTH;
//                Toast.makeText(context, "id is " + LOADER_ID, Toast.LENGTH_SHORT).show();
//                LoaderManager.getInstance(getActivity()).initLoader(LOADER_MONTH, null, AmalReportFragment.this);
//
//            }
//        });
//        RBweek.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
//            @Override
//            public void onCheckedChanged(CompoundButton compoundButton, boolean b) {
//                LOADER_ID = LOADER_WEEK;
//                Toast.makeText(context, "id is " + LOADER_ID, Toast.LENGTH_SHORT).show();
//                LoaderManager.getInstance(getActivity()).restartLoader(LOADER_WEEK, null, AmalReportFragment.this);
//
//
//            }
//        });
        //  LoaderManager.getInstance(this).initLoader(LOADER_WEEK, null, this);
        LoaderManager.getInstance(this).initLoader(LOADER_WEEK, null, this);

        //displayDatabaseInfo();
        // barchat();
        //   Toast.makeText(context, "" + total, Toast.LENGTH_SHORT).show();
        return mView;

    }

    @Override
    public void setUserVisibleHint(boolean isVisibleToUser) {
        super.setUserVisibleHint(isVisibleToUser);

    }


    private void initializingContent() {
        SharedPreferences loginPreferences = Objects.requireNonNull(getActivity()).getApplicationContext().getSharedPreferences("amal_report_type", MODE_PRIVATE);
        SharedPreferences.Editor loginPrefsEditor = loginPreferences.edit();
        RadioButton RBweek = mView.findViewById(R.id.weekRB);
        RadioButton RBmonth = mView.findViewById(R.id.monthRB);
        RBmonth.setOnClickListener(this);
        RBweek.setOnClickListener(this);

        int filterSting = loginPreferences.getInt("filter", 0);

        mAzkarListView = mView.findViewById(R.id.evaluateRecyclerView1);

    }

    public boolean isPost(boolean posted) {
        if (posted) {
            initializingContent();
        } else {
            Toast.makeText(context, "not run", Toast.LENGTH_SHORT).show();
        }

        return posted;
    }

    @NonNull
    @Override
    public Loader<Cursor> onCreateLoader(int i, @Nullable Bundle bundle) {
        switch (i) {
            case LOADER_WEEK:
                final Uri CONTENT_URI_WEEK = Uri.withAppendedPath(BASE_CONTENT_URI, PATH_AMAL_REPORT_VIEW_WEEK);
                return new CursorLoader(context, CONTENT_URI_WEEK, null, null, null, null);
            case LOADER_MONTH:
                final Uri CONTENT_URI_MONTH = Uri.withAppendedPath(BASE_CONTENT_URI, PATH_AMAL_REPORT_VIEW_MONTH);
                return new CursorLoader(context, CONTENT_URI_MONTH, null, null, null, null);
            default:
                Log.d(TAG, "onCreateLoader: loader id is not valid");
        }
        return null;
    }

    @Override
    public void onLoadFinished(@NonNull Loader<Cursor> loader, Cursor cursor1) {
        if (loader.getId() == LOADER_WEEK) {
            mAmalReportAdapter.swapCursor(cursor1);
        } else if (loader.getId() == LOADER_MONTH) {
            mAmalReportAdapter.swapCursor(cursor1);
        }


    }


    @Override
    public void onLoaderReset(@NonNull Loader<Cursor> loader) {
        mAmalReportAdapter.swapCursor(null);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.weekRB:
                Toast.makeText(context, "week is cliced", Toast.LENGTH_SHORT).show();

                LoaderManager.getInstance(this).initLoader(LOADER_WEEK, null, this);
                Log.d(TAG, "onClick: week");
                break;
            case R.id.monthRB:
                Toast.makeText(context, "month is Clicked", Toast.LENGTH_SHORT).show();
                LoaderManager.getInstance(this).initLoader(LOADER_MONTH, null, this);
                break;
        }
    }
}

