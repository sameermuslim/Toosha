package com.sameer.tashbih;

import android.content.Context;
import android.content.Intent;
import android.graphics.Typeface;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;

import androidx.cardview.widget.CardView;

import com.getkeepsafe.taptargetview.TapTarget;
import com.getkeepsafe.taptargetview.TapTargetView;
import com.sameer.tashbih.data.TooshaContract;

import java.util.Objects;

public class MainActivity extends BaseActivity implements View.OnClickListener {
    CardView tasbihCard, azkarCard, customZekerCard, amalNameCard, reportCard, shareCard;



    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Objects.requireNonNull(getSupportActionBar()).setTitle(this.getString(R.string.app_name));


        tasbihCard = findViewById(R.id.cardTasbih);
        azkarCard = findViewById(R.id.cardAzkar);
        customZekerCard = findViewById(R.id.cardCustomAzkar);
        amalNameCard = findViewById(R.id.cardAmalName);
        reportCard = findViewById(R.id.cardReport);
        shareCard = findViewById(R.id.cardShare);

        tasbihCard.setOnClickListener(this);
        azkarCard.setOnClickListener(this);
        customZekerCard.setOnClickListener(this);
        amalNameCard.setOnClickListener(this);
        reportCard.setOnClickListener(this);
        shareCard.setOnClickListener(this);
    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.menu_evaluates, menu);


        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();
        if (id == R.id.action_settings) {
            Intent startSettingsActivity = new Intent(MainActivity.this, SettingsActivity.class);
            startActivity(startSettingsActivity);
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.cardAzkar:
                startActivity(new Intent(MainActivity.this, AzkarCategoryActivity.class));
                break;
            case R.id.cardTasbih:
                Intent tasbihIntent = new Intent(MainActivity.this, AzkarActivity.class);
                tasbihIntent.putExtra("azkarCategory", TooshaContract.TASBIH_CATEGORY);

                startActivity(tasbihIntent);
                break;
            case R.id.cardAmalName:
                startActivity(new Intent(MainActivity.this, AmalNamaActivity.class));
                break;
            case R.id.cardCustomAzkar:
                Intent prayerTime = new Intent(MainActivity.this, PrayerTimeActivity.class);
                prayerTime.putExtra("c_id", TooshaContract.CUSTOM_CATEGORY);
                startActivity(prayerTime);
                break;
            case R.id.cardReport:
                startActivity(new Intent(MainActivity.this, ReportsActivity.class));
                break;
            case R.id.cardShare:
                Intent sharingIntent = new Intent(android.content.Intent.ACTION_SEND);
                sharingIntent.setType("text/plain");
                String shareBody = "Toosha";
                sharingIntent.putExtra(android.content.Intent.EXTRA_SUBJECT, R.string.app_name);
                sharingIntent.putExtra(android.content.Intent.EXTRA_TEXT, shareBody);
                startActivity(Intent.createChooser(sharingIntent, "Share Using"));
                break;
        }
    }

    public void torget(int id){
        TapTargetView.showFor(this,                 // `this` is an Activity
                TapTarget.forView(findViewById(id), "This is a target", "We have the best targets, believe me")
                // All options below are optional
                ,                  // Specify the target radius (in dp)
                new TapTargetView.Listener() {          // The listener can listen for regular clicks, long clicks or cancels
                    @Override
                    public void onTargetClick(TapTargetView view) {
                        super.onTargetClick(view);      // This call is optional
                        // doSomething();
                    }
                });
    }


}
