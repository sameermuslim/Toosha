package com.sameer.tashbih.data;

import android.content.ContentValues;
import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

import com.sameer.tashbih.data.TooshaContract.AmalEntry;
import com.sameer.tashbih.data.TooshaContract.DailyAmalEntry;
import com.sameer.tashbih.data.TooshaContract.TasbihEntry;

import static android.content.ContentValues.TAG;
import static com.sameer.tashbih.utils.TooshaUtils.getMonthAgoDate;
import static com.sameer.tashbih.utils.TooshaUtils.getToday;
import static com.sameer.tashbih.utils.TooshaUtils.getWeekAgoDate;

/**
 * Database helper for Toosha app. Manages database creation and version management.
 */
public class TooshaDbHelper extends SQLiteOpenHelper {

    public static final String LOG_TAG = TooshaDbHelper.class.getSimpleName();

    /**
     * Name of the database file
     */
    public static final String DATABASE_NAME = "toosha_back.db";

    /**
     * Database version. If you change the database schema, you must increment the database version.
     */
    private static final int DATABASE_VERSION = 20;
    private Context mycontext;

    public TooshaDbHelper(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
        mycontext = context;
    }

    public static long insertAllData(SQLiteDatabase db, String arabic, String pashto, String farsi, String english, String fazilat, int countValue, int category) {


        ContentValues values = new ContentValues();

        values.put(TasbihEntry.COLUMN_TASBIH_ARABIC, arabic);
        values.put(TasbihEntry.COLUMN_TASBIH_PASHTO, pashto);
        values.put(TasbihEntry.COLUMN_TASBIH_FARSI, farsi);
        values.put(TasbihEntry.COLUMN_TASBIH_ENGLISH, english);
        values.put(TasbihEntry.COLUMN_TASBIH_FAZILAT, english);
        values.put(TasbihEntry.COLUMN_TASBIH_COUNT, countValue);
        values.put(TasbihEntry.COLUMN_TASBIH_DEFAULT_COUNT, countValue);
        values.put(TasbihEntry.COLUMN_TASBIH_CATEGORY, category);
        values.put(TasbihEntry.COLUMN_TASBIH_TOTAL_COUNT, 0);
        values.put(TasbihEntry.COLUMN_TASBIH_STATE, 0);
        long insert = db.insert(TasbihEntry.TASBIH_TABLE_NAME, null, values);
        return insert;
    }

    /**
     * This is called when the database is created for the first time.
     */
    @Override
    public void onCreate(SQLiteDatabase db) {


        // Create a String that contains the SQL statement to create the Amal table
        String SQL_CREATE_AMAL_TABLE = "CREATE TABLE " + AmalEntry.AMAL_TABLE_NAME + " ("
                + AmalEntry._ID + " INTEGER PRIMARY KEY AUTOINCREMENT, "
                + AmalEntry.COLUMN_AMAL_NAME + " TEXT UNIQUE );";

        // Create a String that contains the SQL statement to create the Daily Amal table
        String SQL_CREATE_DAILY_AMAL_TABLE = "CREATE TABLE " + DailyAmalEntry.DAILY_AMAL_TABLE_NAME + "(" +
                DailyAmalEntry.D_ID + " INTEGER PRIMARY KEY AUTOINCREMENT,"
                + DailyAmalEntry.COLUMN_AMAL_ID + " INTEGER ,"
                + DailyAmalEntry.COLUMN_AMAL_DATE + " LONG ,"
                + DailyAmalEntry.COLUMN_AMAL_STATUS + " INTEGER NOT NULL DEFAULT 0," +
                " foreign key (" + DailyAmalEntry.COLUMN_AMAL_ID + ") references " + AmalEntry.AMAL_TABLE_NAME + "(" + AmalEntry._ID + "));";

        // Create a String that contains the SQL statement to create the Tasbih table
        String SQL_CREATE_TASBIH_TABLE = "CREATE TABLE " + TasbihEntry.TASBIH_TABLE_NAME + " ("
                + TasbihEntry._ID + " INTEGER PRIMARY KEY AUTOINCREMENT, "
                + TasbihEntry.COLUMN_TASBIH_ARABIC + " TEXT, "
                + TasbihEntry.COLUMN_TASBIH_PASHTO + " TEXT, "
                + TasbihEntry.COLUMN_TASBIH_FARSI + " TEXT, "
                + TasbihEntry.COLUMN_TASBIH_ENGLISH + " TEXT, "
                + TasbihEntry.COLUMN_TASBIH_FAZILAT + " TEXT, "
                + TasbihEntry.COLUMN_TASBIH_COUNT + " INTEGER,"
                + TasbihEntry.COLUMN_TASBIH_CATEGORY + " INTEGER,"
                + TasbihEntry.COLUMN_TASBIH_TOTAL_COUNT + " INTEGER, "
                + TasbihEntry.COLUMN_TASBIH_DEFAULT_COUNT + " INTEGER, "
                + TasbihEntry.COLUMN_TASBIH_STATE + " INTEGER ) ";

        long month = getMonthAgoDate();
        long week = getWeekAgoDate();
        long day = getToday();
        // Create a String that contains the SQL statement to create the Weekly report view
        String VIEW_WEEK_DAY =
                "\n" +
                        "CREATE VIEW view_week_report AS SELECT a.amal_name,a._id," +
                        "SUM(CASE WHEN d.`status`= 0 THEN 1 ELSE 0 END) AS 'Fawt'," +
                        "SUM(CASE WHEN d.status=1 THEN 1 ELSE 0 END) AS 'jamat'," +
                        "SUM(CASE WHEN d.status=2 THEN 1 ELSE 0 END) AS 'monfared'," +
                        "SUM(CASE WHEN d.status=3 THEN 1 ELSE 0 END) AS 'Qaza'" +
                        "From amal a  JOIN daily_amal d  on a._id=d.amal_id " +
                        "where d.date between " + week + " and " + day + " " +
                        "group by a.amal_name order by a._id ";
        // Create a String that contains the SQL statement to create the Monthly report view
        String VIEW_MONTH =
                "\n" +
                        "CREATE VIEW view_month_report AS SELECT a.amal_name,a._id," +
                        "SUM(CASE WHEN d.`status`= 0 THEN 1 ELSE 0 END) AS 'Fawt'," +
                        "SUM(CASE WHEN d.status=1 THEN 1 ELSE 0 END) AS 'jamat'," +
                        "SUM(CASE WHEN d.status=2 THEN 1 ELSE 0 END) AS 'monfared'," +
                        "SUM(CASE WHEN d.status=3 THEN 1 ELSE 0 END) AS 'Qaza'" +
                        "From amal a  JOIN daily_amal d  on a._id=d.amal_id " +
                        "where d.date between " + month + " and " + day + " " +
                        "group by a.amal_name order by a._id ";

        // Execute the SQL statements
        db.execSQL(SQL_CREATE_AMAL_TABLE);
        db.execSQL(SQL_CREATE_TASBIH_TABLE);
        db.execSQL(SQL_CREATE_DAILY_AMAL_TABLE);
        db.execSQL(VIEW_WEEK_DAY);
        db.execSQL(VIEW_MONTH);

        // inserting data
        ToshaData toshaData = new ToshaData(db);
    }

    public long insertNewAmal(Context context, long date) {
        // Gets the database in write mode
        SQLiteDatabase db = new TooshaDbHelper(context).getWritableDatabase();


        // Create a ContentValues object where column names are the keys,
        // and Toto's Toosha attributes are the values.
        ContentValues fjr = new ContentValues();
        fjr.put("date", date);
        fjr.put("status", 0);
        fjr.put("amal_id", AmalEntry.FJR);//1
        //
        ContentValues zohor = new ContentValues();
        zohor.put("date", date);
        zohor.put("status", 3);
        zohor.put("amal_id", AmalEntry.ZOHOR);//2
        //
        ContentValues asr = new ContentValues();
        asr.put("date", date);
        asr.put("status", 1);
        asr.put("amal_id", AmalEntry.ASR);//3
        //
        ContentValues maghrib = new ContentValues();
        maghrib.put("date", date);
        maghrib.put("status", 1);
        maghrib.put("amal_id", AmalEntry.MAGHRIB);//4
        //
        ContentValues isha = new ContentValues();
        isha.put("date", date);
        isha.put("status", 3);
        isha.put("amal_id", AmalEntry.ISHA);//5

        ContentValues tahajud = new ContentValues();
        tahajud.put("date", date);
        tahajud.put("status", 1);
        tahajud.put("amal_id", AmalEntry.TAHAJUD);//6

        ContentValues charity = new ContentValues();
        charity.put("date", date);
        charity.put("status", 1);
        charity.put("amal_id", AmalEntry.CHARITY);//7

        ContentValues study = new ContentValues();
        study.put("date", date);
        study.put("status", 0);
        study.put("amal_id", AmalEntry.STUDY);//8

        ContentValues sport = new ContentValues();
        sport.put("date", date);
        sport.put("status", 1);
        sport.put("amal_id", AmalEntry.SPORT);//9


        // Insert a new row for Toto in the database, returning the ID of that new row.
        // The first argument for db.insertAmal() is the Toos table name.
        // The second argument provides the name of a column in which the framework
        // can insertAmal NULL in the event that the ContentValues is empty (if
        // this is set to "null", then the framework will not insertAmal a row when
        // there are no values).
        // The third argument is the ContentValues object containing the info for Toto.
        long newRowId = 0;
        try {
            newRowId = db.insert("daily_amal", null, fjr);
            newRowId = db.insert("daily_amal", null, zohor);
            newRowId = db.insert("daily_amal", null, asr);
            newRowId = db.insert("daily_amal", null, maghrib);
            newRowId = db.insert("daily_amal", null, isha);
            newRowId = db.insert("daily_amal", null, tahajud);
            newRowId = db.insert("daily_amal", null, charity);
            newRowId = db.insert("daily_amal", null, study);
            newRowId = db.insert("daily_amal", null, sport);

        } catch (Exception e) {
            e.printStackTrace();
            Log.d(TAG, "insertAmal: " + e.getMessage());
        }
        db.close();
        Log.d(TAG, "insertAmal: Amal  " + newRowId);
        return newRowId;

    }

//    public Cursor selectCurrentDate(Context context, long dataOfamal) {
//        // Create and/or open a database to read from it
//        SQLiteDatabase db = new TooshaDbHelper(context).getReadableDatabase();
//        // Define a projection that specifies which columns from the database
//        // you will actually use after this query.
//        String[] projection = {
//                AmalEntry._ID,
//                AmalEntry.COLUMN_AMAL_DATE,
//                AmalEntry.COLUMN_AMAL_MORNING,
//                AmalEntry.COLUMN_AMAL_NOON,
//                AmalEntry.COLUMN_AMAL_AFTERNON,
//                AmalEntry.COLUMN_AMAL_EVE,
//                AmalEntry.COLUMN_AMAL_NIGHT,
//                AmalEntry.COLUMN_AMAL_TAHJOD,
//                AmalEntry.COLUMN_AMAL_MURMUR,
//                AmalEntry.COLUMN_AMAL_STUDY,
//                AmalEntry.COLUMN_AMAL_SPORT
//        };
//        String selection = AmalEntry.COLUMN_AMAL_DATE + "=?";
//
//        String[] selectionArgs = {String.valueOf(dataOfamal)};
//        // The table to query
//        // The columns to return
//        // The columns for the WHERE clause
//        // The values for the WHERE clause
//        // Don't group the rows
//        // Don't filter by row groups
//
//        /*Cursor cursor = getContentResolver().query(AmalEntry.CONTENT_URI, projection, null, null, null);*/
//
//        Cursor cursor = db.query(
//                AMAL_TABLE_NAME,   // The table to query
//                projection,// The columns to return
//                selection,                  // The columns for the WHERE clause
//                selectionArgs,                  // The values for the WHERE clause
//                null,                  // Don't group the rows
//                null,                  // Don't filter by row groups
//                null);
//
//        return cursor;
//    }

    public void updateAzkar(Context context) {


        SQLiteDatabase db = new TooshaDbHelper(context).getReadableDatabase();
        // Create a ContentValues object where column names are the keys,
        // and Toto's Toosha attributes are the values.
        db.execSQL("update " + TooshaContract.TasbihEntry.TASBIH_TABLE_NAME + "  set " + TooshaContract.TasbihEntry.COLUMN_TASBIH_COUNT + " = " + TooshaContract.TasbihEntry.COLUMN_TASBIH_DEFAULT_COUNT);
        db.close();

        Log.d(TAG, "updateAzkar: data");

    }

    /**
     * This is called when the database needs to be upgraded.
     */
    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL("DROP TABLE IF EXISTS " + AmalEntry.AMAL_TABLE_NAME);
        db.execSQL("DROP TABLE IF EXISTS " + TasbihEntry.TASBIH_TABLE_NAME);
        db.execSQL("DROP TABLE IF EXISTS " + DailyAmalEntry.DAILY_AMAL_TABLE_NAME);
        db.execSQL("DROP VIEW IF EXISTS view_week_report");
        db.execSQL("DROP VIEW IF EXISTS view_month_report");


        onCreate(db);
    }

}
