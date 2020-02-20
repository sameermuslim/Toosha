package com.sameer.tashbih.utils;

import android.annotation.TargetApi;
import android.content.Context;
import android.content.res.Configuration;
import android.content.res.Resources;
import android.os.Build;
import android.preference.PreferenceManager;
import android.util.Log;

import com.sameer.tashbih.Constants;
import com.sameer.tashbih.R;

import java.util.Locale;

import static androidx.constraintlayout.widget.Constraints.TAG;

public class LocalUtils {

    public static void loadLanguage(Context context) {

        String string = PreferenceManager.getDefaultSharedPreferences(context).getString(context.getString(R.string.pref_language_key), context.getString(R.string.pref_persian_value));
        String str = "fa";
        if (context.getString(R.string.pref_pashto_value).equals(string)) {
            str = "ps";
            //  Log.d(TAG, "loadLanguage: ps");
        } else if (context.getString(R.string.pref_english_value).equals(string)) {
            str = "eng";
            // Log.d(TAG, "loadLanguage: eng");
        }
        Log.d(TAG, "current: " + str);
        Locale locale = new Locale(str);
        Locale.setDefault(locale);
        if (Build.VERSION.SDK_INT >= 24) {
            Configuration configuration = context.getResources().getConfiguration();
            configuration.setLocale(locale);
            context.createConfigurationContext(configuration);

        }
        Resources resources = context.getResources();
        Configuration configuration = resources.getConfiguration();
        configuration.locale = locale;
        resources.updateConfiguration(configuration, resources.getDisplayMetrics());

    }

    public static void changeLanguage(Context context, String str) {
        PreferenceManager.getDefaultSharedPreferences(context).edit().putString(context.getString(R.string.pref_language_key), str).apply();
        loadLanguage(context);
    }

    public static String getCurrentLanguage(Context context) {
        return PreferenceManager.getDefaultSharedPreferences(context).getString(context.getString(R.string.pref_language_key), context.getString(R.string.pref_persian_value));
    }

    public static boolean isOnceGuid(Context context) {

        return PreferenceManager.getDefaultSharedPreferences(context).getBoolean(Constants.IS_ONCE_GUID, true);

    }

    public static void setOnceGuid(Context context, boolean z) {

        PreferenceManager.getDefaultSharedPreferences(context).edit().putBoolean(Constants.IS_ONCE_GUID, z).apply();

    }

    @TargetApi(24)
    private Context updateResourcesLocale(Context newBase, Locale locale) {
        Configuration configuration = newBase.getResources().getConfiguration();
        configuration.setLocale(locale);
        return newBase.createConfigurationContext(configuration);
    }


}
