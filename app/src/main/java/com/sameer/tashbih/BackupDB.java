package com.sameer.tashbih;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.Environment;
import android.util.Log;
import android.widget.Toast;

import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.nio.channels.FileChannel;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;

import static com.sameer.tashbih.data.TooshaDbHelper.DATABASE_NAME;


/**
 * Created by Jawid Mowahed on 2/15/2018.
 */

public class BackupDB {
    public BackupDB() {
    }

    public static void restore(Context context, String backupPath) {
        try {
            File sd = context.getDatabasePath(DATABASE_NAME).getParentFile();
            Log.e("Database Path", "Restore Path :" + sd.getAbsolutePath());
            if (sd.canWrite()) {
                File backupDB = new File(backupPath);
                String dbName = DATABASE_NAME;
                File restoredDB = new File(sd, dbName);
                Log.e("Restored File Created: ", restoredDB.getAbsolutePath());
                FileChannel src = new FileInputStream(backupDB).getChannel();
                FileChannel dst = new FileOutputStream(restoredDB).getChannel();
                dst.transferFrom(src, 0, src.size());
                src.close();
                dst.close();

                Log.e("Restored File Path: ", restoredDB.getAbsolutePath());
                Toast.makeText(context, context.getString(R.string.restore_successful),
                        Toast.LENGTH_SHORT).show();
            }
        } catch (FileNotFoundException e) {
            e.printStackTrace();
            Toast.makeText(context, context.getString(R.string.restore_file_not_found), Toast.LENGTH_SHORT).show();
        } catch (IOException e) {
            e.printStackTrace();
            Toast.makeText(context, context.getString(R.string.restore_failed), Toast.LENGTH_SHORT).show();
        }
    }

    public static void backup(Context context) {
        try {


            String timeStamp = new SimpleDateFormat("ddMMMyyyy_HHmm",
                    Locale.getDefault()).format(new Date());
            String dbFileName = "TOOSHA" + timeStamp + ".bak";

            File sd = new File(Environment.getExternalStorageDirectory() + "/Toosha");

            boolean success = true;
            if (!sd.exists()) {
                success = sd.mkdirs();
            }

            if (success && sd.canWrite()) {
                File currentDB = context.getDatabasePath(DATABASE_NAME);
                File backupDB = new File(sd, dbFileName);

                FileChannel src = new FileInputStream(currentDB).getChannel();
                FileChannel dst = new FileOutputStream(backupDB).getChannel();
                dst.transferFrom(src, 0, src.size());
                src.close();
                dst.close();

                Log.e("Current DB Path :", "" + backupDB.getAbsolutePath());

                Toast.makeText(context, context.getString(R.string.backup_successful) + backupDB.getAbsolutePath().toString(),
                        Toast.LENGTH_SHORT).show();
                Toast.makeText(context, "", Toast.LENGTH_SHORT).show();
            }
        } catch (IOException e) {
            e.printStackTrace();
            Toast.makeText(context, context.getString(R.string.backup_failed), Toast.LENGTH_SHORT).show();
        }
    }


    public static void backupNow(Context context) {
        if (ContextCompat.checkSelfPermission(context,
                android.Manifest.permission.WRITE_EXTERNAL_STORAGE)
                != PackageManager.PERMISSION_GRANTED) {

            // If you do not have permission, request it
            ActivityCompat.requestPermissions((Activity) context,
                    new String[]{android.Manifest.permission.WRITE_EXTERNAL_STORAGE},
                    Constants.RC_BACKUP_DIRECTORY_CHOOSER);
        } else {
            BackupDB.backup(context);
        }
    }

    public static void restoreNow(Context context) {
        if (ContextCompat.checkSelfPermission(context,
                android.Manifest.permission.WRITE_EXTERNAL_STORAGE)
                != PackageManager.PERMISSION_GRANTED) {

            // If you do not have permission, request it
            ActivityCompat.requestPermissions((Activity) context,
                    new String[]{android.Manifest.permission.WRITE_EXTERNAL_STORAGE},
                    Constants.RC_RESTORE_DB);
        } else {
            Intent intent = new Intent(Intent.ACTION_OPEN_DOCUMENT);
            intent.setType("*/*");
            Activity activity = (Activity) context;
            activity.startActivityForResult(intent, Constants.RC_RESTORE_DB);

        }
    }
}