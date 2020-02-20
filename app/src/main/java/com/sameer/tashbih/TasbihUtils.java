package com.sameer.tashbih;

import android.app.AlarmManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.widget.Toast;

import com.github.mikephil.charting.charts.PieChart;
import com.github.mikephil.charting.components.Legend;
import com.github.mikephil.charting.data.PieData;
import com.github.mikephil.charting.data.PieDataSet;
import com.github.mikephil.charting.data.PieEntry;
import com.github.mikephil.charting.formatter.DefaultAxisValueFormatter;
import com.github.mikephil.charting.utils.ColorTemplate;
import com.sameer.tashbih.alarm.AlarmReceiver;

import java.util.ArrayList;
import java.util.Calendar;

public class TasbihUtils {

     public static void setAlarm(Context context) {

        AlarmManager alarmManager = (AlarmManager) context.getSystemService(Context.ALARM_SERVICE);

        Intent notificationIntent = new Intent(context, AlarmReceiver.class);
        PendingIntent broadcast = PendingIntent.getBroadcast(context, 100, notificationIntent, PendingIntent.FLAG_UPDATE_CURRENT);

        Calendar cal = Calendar.getInstance();
        cal.add(Calendar.SECOND, 1);
        cal.add(Calendar.HOUR_OF_DAY, 17);
        cal.add(Calendar.MINUTE, 25);

        alarmManager.setInexactRepeating(AlarmManager.RTC_WAKEUP, cal.getTimeInMillis(), AlarmManager.INTERVAL_DAY, broadcast);                   //not(MainActivity.this);
        Toast.makeText(context, "alarm added", Toast.LENGTH_SHORT).show();

    }



    public static void setValueOfPieChart(PieChart pieChart,Context context, String prayerName, int jamatValue, int moqimValue, int qazaValue, int fawtValue) {


        pieChart.setUsePercentValues(false); // don't show the percent in chart value
        pieChart.getDescription().setEnabled(false); // don't show the description text

        // chart margins
        pieChart.setExtraOffsets(0, 3, 1, 3);
        // Legend show the colors and labels in right side of chart
        Legend l = pieChart.getLegend();
        l.setVerticalAlignment(Legend.LegendVerticalAlignment.CENTER);
        l.setHorizontalAlignment(Legend.LegendHorizontalAlignment.LEFT);
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
        yValues.add(new PieEntry(jamatValue, context.getResources().getString(R.string.jamat)));// green
        yValues.add(new PieEntry(moqimValue, context.getResources().getString(R.string.moqim)));//yellow
        yValues.add(new PieEntry(qazaValue, context.getResources().getString(R.string.qaza)));//red
        yValues.add(new PieEntry(fawtValue, context.getResources().getString(R.string.fawt)));//blue

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

    public static Calendar setCalender(int hour,int minute){
        Calendar date=Calendar.getInstance();
        date.set(Calendar.HOUR_OF_DAY, hour);
        date.set(Calendar.MINUTE, minute);
        date.set(Calendar.SECOND, 0);
        date.set(Calendar.MILLISECOND, 0);
        return date;

    }


}
