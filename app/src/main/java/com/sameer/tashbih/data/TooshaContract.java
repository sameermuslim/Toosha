package com.sameer.tashbih.data;

import android.content.ContentResolver;
import android.content.Context;
import android.net.Uri;
import android.provider.BaseColumns;


/**
 * Created by Sameer Muslim on 6/26/2019.
 * <p>
 * Toosha Contracts that we use in Project
 */

public final class TooshaContract {

    // Azkar Catagory Constants
    public static final int AZKAR_CATEGORY = 1;
    public static final int TASBIH_CATEGORY = 2;
    public static final int CUSTOM_CATEGORY = 3;
    public static final int PRAYER_AZKAR = 4;
    public static final int MORNING_AZKAR = 5;
    public static final int EVENING_AZKAR = 6;
    public static final int DAY_NIGHT_AZkAR = 7;
    public static final int SLEEP_AZKAR = 8;
    public static final int MOSQUE_AZKAR = 9;

    // Tasbih state Constants use to know difference between Tasbih & Custom-Tasbih
    public static final int STATE_TRUE = 1;
    public static final int STATE_FALSE = 0;
    public static final String CONTENT_AUTHORITY = "com.sameer.tashbih";

    public static final Uri BASE_CONTENT_URI = Uri.parse("content://" + CONTENT_AUTHORITY);
    public static final String PATH_AMALNAMA = "amal";
    public static final String PATH_TASBIH = "tasbih_back";
    public static final String PATH_DAILY_AMAL = "daily_amal";
    public static final String PATH_AMAL_REPORT_VIEW_WEEK = "view_week_report";
    public static final String PATH_AMAL_REPORT_VIEW_MONTH = "view_month_report";
    Context context;


    // To prevent someone from accidentally instantiating the contract class,
    // give it an empty constructor.
    private TooshaContract() {
    }


    /**
     * Inner class that defines constant values for the Toosha database Daily Amal Table.
     * Each entry in the table represents a single  Day Amal.
     */
    public static final class DailyAmalEntry implements BaseColumns {
        public static final Uri CONTENT_URI = Uri.withAppendedPath(BASE_CONTENT_URI, PATH_DAILY_AMAL);
        public static final String CONTENT_LIST_TYPE = ContentResolver.CURSOR_DIR_BASE_TYPE + "/" + CONTENT_AUTHORITY + "/" + PATH_DAILY_AMAL;
        public static final String CONTENT_ITEM_TYPE = ContentResolver.CURSOR_ITEM_BASE_TYPE + "/" + CONTENT_AUTHORITY + "/" + PATH_DAILY_AMAL;

        /* Unique ID number for the Daily Amal (only for use in the database table).
         *
         * Type: INTEGER
         */
        public final static String D_ID = BaseColumns._ID;

        public final static String DAILY_AMAL_TABLE_NAME = "daily_amal";
        /**
         * Date of the daily Amal.
         * <p>
         * Type: LONG
         */
        public final static String COLUMN_AMAL_DATE = "date";
        /**
         * STATUS of the DAILY Amal.
         * <p>
         * Type: BOOLEAN
         */
        public final static String COLUMN_AMAL_STATUS = "status";
        /**
         * Forian key ID  of the  Amal in Daily amal
         * <p>
         * Type: INTEGER
         */
        public final static String COLUMN_AMAL_ID = "amal_id";

    }

    /**
     * Inner class that defines constant values for the Toosha database Amal Table.
     * Each entry in the table represents a single Amal.
     */
    public static final class AmalEntry implements BaseColumns {
        // Constant Values of prayers
        public static final int FJR = 1;
        public static final int ZOHOR = 2;
        public static final int ASR = 3;
        public static final int MAGHRIB = 4;
        public static final int ISHA = 5;
        public static final int TAHAJUD = 6;
        public static final int CHARITY = 7;
        public static final int STUDY = 8;
        public static final int SPORT = 9;


        public static final Uri CONTENT_URI = Uri.withAppendedPath(BASE_CONTENT_URI, PATH_AMALNAMA);

        public static final String CONTENT_LIST_TYPE = ContentResolver.CURSOR_DIR_BASE_TYPE + "/" + CONTENT_AUTHORITY + "/" + PATH_AMALNAMA;

        public static final String CONTENT_ITEM_TYPE = ContentResolver.CURSOR_ITEM_BASE_TYPE + "/" + CONTENT_AUTHORITY + "/" + PATH_AMALNAMA;

        /**
         * Name of the Amal Table
         * <p>
         * Type: TEXT
         */
        public final static String AMAL_TABLE_NAME = "amal";
        /**
         * Unique ID number for the Amal (only for use in the database table).
         * <p>
         * Type: INTEGER
         */
        public final static String _ID = BaseColumns._ID;
        /**
         * Name of the Amal.
         * <p>
         * Type: TEXT
         */
        public final static String COLUMN_AMAL_NAME = "amal_name";
    }

    /**
     * Inner class that defines constant values for the Toosha database Tasbih Table.
     * Each entry in the table represents a single Tasbih.
     */
    public static final class TasbihEntry implements BaseColumns {
        public static final Uri CONTENT_URI = Uri.withAppendedPath(BASE_CONTENT_URI, PATH_TASBIH);

        public static final String CONTENT_LIST_TYPE = ContentResolver.CURSOR_DIR_BASE_TYPE + "/" + CONTENT_AUTHORITY + "/" + PATH_TASBIH;

        public static final String CONTENT_ITEM_TYPE = ContentResolver.CURSOR_ITEM_BASE_TYPE + "/" + CONTENT_AUTHORITY + "/" + PATH_TASBIH;


        /**
         * Name of the Tasbih table.

         */
        public final static String TASBIH_TABLE_NAME = "tasbih_back";

        /* Unique ID number for the Daily Amal (only for use in the database table).
         *
         * Type: INTEGER
         */
        public static final String _ID = BaseColumns._ID;
        /**
         * arabic meaning of the Tasbih.
         * <p>
         * Type: TEXT
         */
        public static final String COLUMN_TASBIH_ARABIC = "arabic";
        //Type: TEXT
        public static final String COLUMN_TASBIH_PASHTO = "pashto";
        //Type: TEXT
        public static final String COLUMN_TASBIH_FARSI = "farsi";
        //Type: TEXT
        public static final String COLUMN_TASBIH_ENGLISH = "english";
        /**
         * Count of the tasbih.
         * <p>
         * Type: INTEGER
         */
        public static final String COLUMN_TASBIH_COUNT = "count";
        /**
         * Category of the tasbih.
         * <p>
         * Type: INTEGER
         */
        public static final String COLUMN_TASBIH_CATEGORY = "category";
        //Type: INTEGER
        public static final String COLUMN_TASBIH_DEFAULT_COUNT = "default_count";
        //Type: BOOLEAN
        public static final String COLUMN_TASBIH_STATE = "state";
        //Type: INTEGER
        public static final String COLUMN_TASBIH_TOTAL_COUNT = "total_count";
        //Type: TEXT
        public static final String COLUMN_TASBIH_FAZILAT = "fazilat";
    }


}

