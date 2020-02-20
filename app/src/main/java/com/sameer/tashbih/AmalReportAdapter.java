package com.sameer.tashbih;

import android.content.Context;
import android.database.Cursor;
import android.graphics.Color;
import android.graphics.drawable.Drawable;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.github.mikephil.charting.charts.HorizontalBarChart;
import com.github.mikephil.charting.charts.PieChart;
import com.github.mikephil.charting.components.Legend;
import com.github.mikephil.charting.data.PieData;
import com.github.mikephil.charting.data.PieDataSet;
import com.github.mikephil.charting.data.PieEntry;
import com.github.mikephil.charting.formatter.DefaultAxisValueFormatter;
import com.github.mikephil.charting.utils.ColorTemplate;
import com.sameer.tashbih.data.TooshaContract;

import java.util.ArrayList;
import java.util.List;

import static android.content.ContentValues.TAG;
import static com.sameer.tashbih.data.TooshaContract.AmalEntry.ASR;
import static com.sameer.tashbih.data.TooshaContract.AmalEntry.CHARITY;
import static com.sameer.tashbih.data.TooshaContract.AmalEntry.FJR;
import static com.sameer.tashbih.data.TooshaContract.AmalEntry.ISHA;
import static com.sameer.tashbih.data.TooshaContract.AmalEntry.MAGHRIB;
import static com.sameer.tashbih.data.TooshaContract.AmalEntry.SPORT;
import static com.sameer.tashbih.data.TooshaContract.AmalEntry.STUDY;
import static com.sameer.tashbih.data.TooshaContract.AmalEntry.TAHAJUD;
import static com.sameer.tashbih.data.TooshaContract.AmalEntry.ZOHOR;


public class AmalReportAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {
    private static final int VIEW_ONE = 1;
    private static final int VIEW_ZERO = 0;
    private static final int VIEW_THREE = 3;
    public int a;
    private List<Amal> amals;
    private Context context;
    private Cursor mCursor;


    public AmalReportAdapter(Context context) {
        this.context = context;
        this.amals = new ArrayList<>();
    }


    public void setValueOfPieChart(PieChart pieChart, Context context, int legendSide, String prayerName, int jamatValue, int moqimValue, int qazaValue, int fawtValue) {


        pieChart.setUsePercentValues(false); // don't show the percent in chart value
        pieChart.getDescription().setEnabled(false); // don't show the description text

        // chart margins
        pieChart.setExtraOffsets(0, 3, 1, 3);
        // Legend show the colors and labels in right side of chart
        Legend l = pieChart.getLegend();
        l.setVerticalAlignment(Legend.LegendVerticalAlignment.CENTER);
        if (legendSide == VIEW_ZERO) {
            l.setHorizontalAlignment(Legend.LegendHorizontalAlignment.RIGHT);
        } else {
            l.setHorizontalAlignment(Legend.LegendHorizontalAlignment.LEFT);
        }

        l.setOrientation(Legend.LegendOrientation.VERTICAL);

        // speed of drag of chart view
        // for mor speed must set high value
        pieChart.setDragDecelerationFrictionCoef(0.45f);


        // show  withe in center
        pieChart.setDrawHoleEnabled(true);
        pieChart.setHoleColor(Color.WHITE);
        pieChart.setHoleRadius(36f); //
        pieChart.setTransparentCircleRadius(45f); //shadow of withe
        pieChart.setDrawCenterText(true); // show text in center
        pieChart.setCenterText(prayerName); // the text inside withe
        pieChart.setCenterTextSize(16f); //text size

        // Entry value and label
        pieChart.setDrawEntryLabels(false); //don't show the label
        //pieChart.setEntryLabelTextSize(23f);

        // arrayList of pie data /value
        ArrayList<PieEntry> yValues = new ArrayList<>();
        // add values to array list
        if (context.getResources().getString(R.string.tahajod).equals(prayerName) || context.getResources().getString(R.string.sport).equals(prayerName) || context.getResources().getString(R.string.telawat).equals(prayerName) || context.getResources().getString(R.string.study).equals(prayerName)) {
            yValues.add(new PieEntry(jamatValue, context.getString(R.string.doneAmal)));// green
            yValues.add(new PieEntry(fawtValue, context.getString(R.string.notDoneAmal)));//yellow
        } else {
            yValues.add(new PieEntry(jamatValue, context.getResources().getString(R.string.jamat)));// green
            yValues.add(new PieEntry(qazaValue, context.getResources().getString(R.string.qaza)));//yellow
            yValues.add(new PieEntry(fawtValue, context.getResources().getString(R.string.fawt)));//red
            yValues.add(new PieEntry(moqimValue, context.getResources().getString(R.string.moqim)));//blue
        }
        // initializing Data set of pie  take values and
        PieDataSet dataSet = new PieDataSet(yValues, prayerName);


        // for setting custom Color
        //        ArrayList<Integer> colors = new ArrayList<>();
//        colors.add(Color.RED);
//        colors.add(Color.YELLOW);
//        colors.add(Color.BLUE);
//        colors.add(Color.GREEN);
//

        // setting space between values
        dataSet.setSliceSpace(2f);
        // show mach shift selected value of selected pic chart part
        dataSet.setSelectionShift(5f);
        //setting the colors
        dataSet.setColors(ColorTemplate.MATERIAL_COLORS);
        // format the value of chart tho how like integer value not percent value
        dataSet.setValueFormatter(new DefaultAxisValueFormatter(0));
        // set the pie dataSet to pie
        PieData data = new PieData(dataSet);
        // setting the cart size
//    pieChart.setScaleX(0f);
//    pieChart.setScaleY(0f);
        // setting the chart value text size and color
        data.setValueTextSize(20f);

        data.setValueTextColor(Color.WHITE);
        pieChart.setData(data);
        pieChart.invalidate();
        // setting animation to hour chart
        pieChart.animateXY(1400, 1400);


    }


    @NonNull
    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {

        switch (viewType) {
            case VIEW_ONE:
                return new AmalReportAdapter.ViewHolder(LayoutInflater.from(parent.getContext()).inflate(R.layout.amal_report_item, parent, false));

            case VIEW_ZERO:
                return new AmalReportAdapter.ViewHolder2(LayoutInflater.from(parent.getContext()).inflate(R.layout.new_item, parent, false));

            case VIEW_THREE:
                return new AmalReportAdapter.ViewHolder3(LayoutInflater.from(parent.getContext()).inflate(R.layout.action_report_item, parent, false));

        }
        return null;
    }

    @Override
    public void onBindViewHolder(@NonNull final RecyclerView.ViewHolder holder, int position) {

        // Iterate through all the returned rows in the cursor
        int totalJamat = 0;
        int totleQaza = 0;
        int totalMoqim = 0;
        int totalFawt = 0;

        while (mCursor.moveToNext()) {
            Log.d(TAG, "onBindViewHolder: " + getItemCount());

            String amalName = mCursor.getString(mCursor.getColumnIndex(TooshaContract.AmalEntry.COLUMN_AMAL_NAME));
            int currentId = mCursor.getInt(mCursor.getColumnIndex(TooshaContract.AmalEntry._ID));
            int currentFawt = mCursor.getInt(mCursor.getColumnIndex("Fawt"));
            int currentJamat = mCursor.getInt(mCursor.getColumnIndex("jamat"));
            int currentMoqim = mCursor.getInt(mCursor.getColumnIndex("monfared"));
            int currentQaza = mCursor.getInt(mCursor.getColumnIndex("Qaza"));
            Log.d(TAG, "onCreate: " + amalName + " " + currentId + " " + currentFawt + " " + currentJamat + " " + currentMoqim + " " + currentQaza);

            int a = 1;
            Log.d(TAG, "displayDatabaseInfo: " + amalName);

            String prayerName = "";
            int image = R.drawable.fajr_report;
            switch (currentId) {
                case FJR:
                    image = R.drawable.fajr_report;
                    a = 2;
                    totalJamat = totalJamat + currentJamat;
                    totalMoqim = totalMoqim + currentMoqim;
                    totalFawt = totalFawt + currentFawt;
                    totleQaza = totleQaza + currentQaza;
                    prayerName = context.getResources().getString(R.string.fager);
                    break;
                case ZOHOR:
                    image = R.drawable.dohor_report;
                    a = 1;
                    totalJamat = totalJamat + currentJamat;
                    totalMoqim = totalMoqim + currentMoqim;
                    totalFawt = totalFawt + currentFawt;
                    totleQaza = totleQaza + currentQaza;
                    a = 1;
                    prayerName = context.getResources().getString(R.string.zohor);
                    break;
                case ASR:
                    image = R.drawable.asr_report;

                    a = 2;
                    totalJamat = totalJamat + currentJamat;
                    totalMoqim = totalMoqim + currentMoqim;
                    totalFawt = totalFawt + currentFawt;
                    totleQaza = totleQaza + currentQaza;
                    prayerName = context.getResources().getString(R.string.aser);
                    break;
                case MAGHRIB:
                    image = R.drawable.maqhrib_report;
                    a = 1;
                    totalJamat = totalJamat + currentJamat;
                    totalMoqim = totalMoqim + currentMoqim;
                    totalFawt = totalFawt + currentFawt;
                    totleQaza = totleQaza + currentQaza;
                    prayerName = context.getResources().getString(R.string.maghrib);
                    break;
                case ISHA:
                    image = R.drawable.isha_roport;
                    totalJamat = totalJamat + currentJamat;
                    totalMoqim = totalMoqim + currentMoqim;
                    totalFawt = totalFawt + currentFawt;
                    totleQaza = totleQaza + currentQaza;
                    a = 2;
                    prayerName = context.getResources().getString(R.string.esha);
                    break;
                case TAHAJUD:
                    image = R.drawable.tahajod_report;
                    a = 2;
                    prayerName = context.getResources().getString(R.string.tahajod);
                    break;
                case SPORT:
                    image = R.drawable.sport_report;
                    a = 2;
                    prayerName = context.getResources().getString(R.string.sport);

                    break;
                case STUDY:
                    image = R.drawable.study_report;
                    a = 1;
                    prayerName = context.getResources().getString(R.string.study);
                    break;
                case CHARITY:
                    image = R.drawable.murmur;
                    a = 1;
                    prayerName = context.getResources().getString(R.string.telawat);
                    break;
                default:

                    prayerName = null;

            }

            int viewType = a;
            amals.add(new Amal(prayerName, currentJamat, currentMoqim, currentQaza, currentFawt, image, viewType));
            a += 1;
            AmalReportFragment.barchat(totalJamat,totalMoqim,totalFawt,totleQaza);
        }


        Amal amals2 = amals.get(position);
        switch (holder.getItemViewType()) {
            case VIEW_ONE:

                AmalReportAdapter.ViewHolder holder1 = (AmalReportAdapter.ViewHolder) holder;
                holder1.setPieChart1(context, VIEW_ONE, amals2.getPrayerName(), amals2.getJamaat(), amals2.getMoqim(), amals2.getQaza(), amals2.getFawot());
                holder1.setImageView1(context.getDrawable(amals2.getImage()));

                break;
            case VIEW_ZERO:

                AmalReportAdapter.ViewHolder2 holder2 = (AmalReportAdapter.ViewHolder2) holder;
                holder2.setPieChart2(context, VIEW_ZERO, amals2.getPrayerName(), amals2.getJamaat(), amals2.getMoqim(), amals2.getQaza(), amals2.getFawot());
                holder2.setImageView2(amals2.getImage());

                break;
            case VIEW_THREE:

                AmalReportAdapter.ViewHolder3 holder3 = (AmalReportAdapter.ViewHolder3) holder;
                holder3.setHorizontalBar(context, amals2.getPrayerName(), amals2.getJamaat(), amals2.getFawot());
                break;


        }


    }

    @Override
    public int getItemViewType(int position) {
 return position%2;
     }

    @Override
    public int getItemCount() {
        if (mCursor == null) {
            return 0;
        }
        return mCursor.getCount();
    }

    public Cursor swapCursor(Cursor c) {
        if (mCursor == c){
            return null;
        }

        Cursor temp = mCursor;
        this.mCursor = c;
        if (c != null){
            this.notifyDataSetChanged();
        }

        return temp;
          }

    public class ViewHolder extends RecyclerView.ViewHolder {
        private View mView;
        private PieChart pieChart;
        private ImageView imageView1;


        private ViewHolder(View itemView) {
            super(itemView);
            mView = itemView;
            imageView1 = mView.findViewById(R.id.amal_report_1_image);
            pieChart = mView.findViewById(R.id.pieChart);
        }


        private void setImageView1(Drawable resId) {
            imageView1.setImageDrawable(resId);
        }

        private void setPieChart1(Context context, int legendSide, String prayerName, int jamatValue, int moqimValue, int qazaValue, int fawtValue) {
            setValueOfPieChart(pieChart, context, legendSide, prayerName, jamatValue, moqimValue, qazaValue, fawtValue);
        }
    }

    public class ViewHolder2 extends RecyclerView.ViewHolder {
        private View mView;
        private ImageView imageView2;
        private PieChart pieChart;


        private ViewHolder2(View itemView) {
            super(itemView);
            mView = itemView;
            imageView2 = mView.findViewById(R.id.amal_report_2_image);
            pieChart = mView.findViewById(R.id.horizontalBarchart);
        }

        private void setPieChart2(Context context, int legendSide, String prayerName, int jamatValue, int moqimValue, int qazaValue, int fawtValue) {
            setValueOfPieChart(pieChart, context, legendSide, prayerName, jamatValue, moqimValue, qazaValue, fawtValue);
        }


        public void setImageView2(int resId) {
            imageView2.setImageResource(resId);
        }
    }

    public class ViewHolder3 extends RecyclerView.ViewHolder {
        private View mView;
        private ImageView imageView3;
        private HorizontalBarChart mBarChart;

        public ViewHolder3(View itemView) {
            super(itemView);
            mView = itemView;
            imageView3 = mView.findViewById(R.id.amal_report_3_image);
            mBarChart = mView.findViewById(R.id.horizontalBarchart);
        }

        private void setHorizontalBar(Context context, String prayName, int done, int notDone) {
            //setValueOfHorizontalChart(mBarChart, context, prayName, done, notDone);
        }
    }
}
