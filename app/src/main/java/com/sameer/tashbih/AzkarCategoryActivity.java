package com.sameer.tashbih;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import androidx.cardview.widget.CardView;
import android.view.View;
import android.widget.Toast;

import com.sameer.tashbih.data.TooshaContract;

import java.util.Objects;

public class AzkarCategoryActivity extends BaseActivity implements View.OnClickListener {
    CardView evenCard, morningCard, dayNightCard, prayerCard, sleepCard, mosqueCard;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_azkar_category);
        Objects.requireNonNull(getSupportActionBar()).setTitle(this.getString(R.string.azkar));


        evenCard = findViewById(R.id.cardEvening);
        morningCard = findViewById(R.id.cardMorningAzkar);
        dayNightCard = findViewById(R.id.cardDayNight);
        prayerCard = findViewById(R.id.cardPrayer);
        sleepCard = findViewById(R.id.cardSleep);
        mosqueCard = findViewById(R.id.cardMosque);


        evenCard.setOnClickListener(this);
        morningCard.setOnClickListener(this);
        dayNightCard.setOnClickListener(this);
        prayerCard.setOnClickListener(this);
        sleepCard.setOnClickListener(this);
        mosqueCard.setOnClickListener(this);


    }

    @Override
    public void onClick(View v) {

        switch (v.getId()) {
            case R.id.cardMosque:
                Intent intent = new Intent(AzkarCategoryActivity.this, AzkarActivity.class);
                intent.putExtra("azkarCategory", TooshaContract.MOSQUE_AZKAR);
                startActivity(intent);
                break;
            case R.id.cardPrayer:
                Intent intent1 = new Intent(AzkarCategoryActivity.this, AzkarActivity.class);
                intent1.putExtra("azkarCategory", TooshaContract.PRAYER_AZKAR);
                startActivity(intent1);
                break;
            case R.id.cardMorningAzkar:
                Intent intent2 = new Intent(AzkarCategoryActivity.this, AzkarActivity.class);
                intent2.putExtra("azkarCategory", TooshaContract.MORNING_AZKAR);
                startActivity(intent2);
                break;
            case R.id.cardEvening:
                Intent intent3 = new Intent(AzkarCategoryActivity.this, AzkarActivity.class);
                intent3.putExtra("azkarCategory", TooshaContract.EVENING_AZKAR);
                startActivity(intent3);
                break;
            case R.id.cardSleep:
                Intent intent5 = new Intent(AzkarCategoryActivity.this, AzkarActivity.class);
                intent5.putExtra("azkarCategory", TooshaContract.SLEEP_AZKAR);
                startActivity(intent5);
                break;
            case R.id.cardDayNight:
                Intent intent6 = new Intent(AzkarCategoryActivity.this, AzkarActivity.class);
                intent6.putExtra("azkarCategory", TooshaContract.DAY_NIGHT_AZkAR);
                startActivity(intent6);
                break;

        }

    }
}
