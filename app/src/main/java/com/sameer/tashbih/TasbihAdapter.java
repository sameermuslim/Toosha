package com.sameer.tashbih;

import android.content.ContentValues;
import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Vibrator;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.recyclerview.widget.RecyclerView;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.sameer.tashbih.data.TooshaContract;
import com.sameer.tashbih.data.TooshaDbHelper;

import java.util.ArrayList;

import static android.content.Context.VIBRATOR_SERVICE;

import static android.view.View.inflate;
import static androidx.constraintlayout.widget.Constraints.TAG;
import static com.sameer.tashbih.data.TooshaContract.TASBIH_CATEGORY;
import static com.sameer.tashbih.utils.TooshaUtils.showDialog;
import static com.sameer.tashbih.utils.TooshaUtils.vibrate;

class TasbihAdapter extends RecyclerView.Adapter<TasbihAdapter.ViewHolder> {

    private ArrayList<Tasbih> tasbihs;
    private Context mContext;
    private TooshaDbHelper dbHelper;
    private Cursor mCursor;
    View mView;

    public TasbihAdapter(Context context) {
        mContext = context;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        mContext = parent.getContext();
        mView = LayoutInflater.from(parent.getContext()).inflate(R.layout.tasbih_item, parent, false);
        Toast.makeText(mContext, "constrat", Toast.LENGTH_SHORT).show();
        return new ViewHolder(mView);
    }

    @Override
    public void onBindViewHolder(@NonNull final ViewHolder holder, int position) {
        Log.d(TAG, "onBindViewHolder: " + mCursor.getCount());

        mCursor.moveToPosition(position);

        int id = mCursor.getInt(mCursor.getColumnIndex(TooshaContract.TasbihEntry._ID));
        String arabic = mCursor.getString(mCursor.getColumnIndex(TooshaContract.TasbihEntry.COLUMN_TASBIH_ARABIC));
        String pashto = mCursor.getString(mCursor.getColumnIndex(TooshaContract.TasbihEntry.COLUMN_TASBIH_PASHTO));
        String farsi = mCursor.getString(mCursor.getColumnIndex(TooshaContract.TasbihEntry.COLUMN_TASBIH_FARSI));
        String english = mCursor.getString(mCursor.getColumnIndex(TooshaContract.TasbihEntry.COLUMN_TASBIH_ENGLISH));
        String fazilat = mCursor.getString(mCursor.getColumnIndex(TooshaContract.TasbihEntry.COLUMN_TASBIH_FAZILAT));

        int count = mCursor.getInt(mCursor.getColumnIndex(TooshaContract.TasbihEntry.COLUMN_TASBIH_COUNT));
        int total_count = mCursor.getInt(mCursor.getColumnIndex(TooshaContract.TasbihEntry.COLUMN_TASBIH_TOTAL_COUNT));
        int category = mCursor.getInt(mCursor.getColumnIndex(TooshaContract.TasbihEntry.COLUMN_TASBIH_CATEGORY));
        int state = mCursor.getInt(mCursor.getColumnIndex(TooshaContract.TasbihEntry.COLUMN_TASBIH_STATE));
        Log.d(ContentValues.TAG, "pashto = " + pashto);

        if (state == 1) {
            holder.edit_icon.setVisibility(View.VISIBLE);
            holder.delete_icon.setVisibility(View.VISIBLE);
        } else {
            holder.delete_icon.setVisibility(View.GONE);
            holder.edit_icon.setVisibility(View.GONE);
        }
        holder.arabicText.setText(arabic);
        holder.messagebox(mContext,fazilat);
        holder.setT_count(count);
        holder.clickTasbihItem(id, category, count, total_count);
        holder.edit_icon_onClick(id, count, arabic, pashto, farsi,english,fazilat, total_count);
        holder.delete_icon_OnClick(id);

    }

    @Override
    public int getItemCount() {

        if (mCursor == null) {
            return 0;
        }
        Toast.makeText(mContext, "getcout", Toast.LENGTH_SHORT).show();
        return this.mCursor.getCount();

    }




    public Cursor swapCursor(Cursor cursor) {
        if (mCursor == cursor) {
            return null;
        }
        Cursor cursor2 = mCursor;
        mCursor = cursor;
        if (cursor != null) {
            notifyDataSetChanged();
        }
        return cursor2;
    }

    public class ViewHolder extends RecyclerView.ViewHolder {

        TooshaDbHelper mDbHelper;
        ImageView edit_icon, delete_icon;

        private View mView;
        private TextView arabicText, countText;
        private LinearLayout mLinearLayout;
        private SQLiteDatabase db;
        private Vibrator vibrator;
        private ImageView infoImage;

        public ViewHolder(View itemView) {
            super(itemView);
            mView = itemView;
            delete_icon = itemView.findViewById(R.id.delete_icon);
            edit_icon = mView.findViewById(R.id.editable_icon);
            vibrator = (Vibrator) mContext.getSystemService(VIBRATOR_SERVICE);
            mLinearLayout = itemView.findViewById(R.id.tasbih_item_id);
            countText = itemView.findViewById(R.id.count);
            infoImage=itemView.findViewById(R.id.info);
            this.arabicText = itemView.findViewById(R.id.arabic_tv_item);


        }

        public void messagebox(final Context context, final String fazilat) {
            infoImage.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    showDialog(context,context.getString(R.string.fazilat),fazilat);
                }
            });

        }

        public void setT_count(int theCount) {
            if (theCount == 0) {
                countText.setBackground(mContext.getDrawable(R.drawable.counter_shap_seconder_color));
                countText.setText(mContext.getString(R.string.finCount));
            } else {
                countText.setBackground(mContext.getDrawable(R.drawable.counter_shap_primary_color));
                String numberString = String.format("%d", theCount);
                countText.setText(numberString);
            }
        }


        public void edit_icon_onClick(final int id, final int count, final String arabic, final String pashto, final String farsi,final String english ,final String fazilat ,final int total_cont) {
            edit_icon.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {

                    AlertDialog.Builder mBuilder = new AlertDialog.Builder(mContext);
                    View mView = inflate(mContext, R.layout.dialog_insert_tasbih, null);
                    final EditText mArabic = (EditText) mView.findViewById(R.id.arabicText);
                    mArabic.setText(arabic);
                    final EditText mPashto = mView.findViewById(R.id.pashtoText);
                    mPashto.setText(pashto);
                    final EditText mPersian = (EditText) mView.findViewById(R.id.persianText);
                    mPersian.setText(farsi);
                    final EditText mEnglish = (EditText) mView.findViewById(R.id.englishText);
                    mEnglish.setText(english);
                    final EditText mfazilat = (EditText) mView.findViewById(R.id.fazilatText);
                    mfazilat.setText(fazilat);

                    final EditText mAmount = mView.findViewById(R.id.amountText);
                    mAmount.setText(String.valueOf(count));

                    Button mSaveBtn = (Button) mView.findViewById(R.id.addBtn);
                    mBuilder.setView(mView);
                    final AlertDialog dialog = mBuilder.create();
                    Window window = dialog.getWindow();
                    window.setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
                    window.setGravity(Gravity.BOTTOM);
                    window.getAttributes().windowAnimations = R.style.DialogAnimation;
                    dialog.show();
                    mSaveBtn.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View view) {
                            if (!mArabic.getText().toString().isEmpty() && !mAmount.getText().toString().isEmpty()) {
                                ContentValues updateValues = new ContentValues();
                                updateValues.put(TooshaContract.TasbihEntry.COLUMN_TASBIH_ARABIC, mArabic.getText().toString().trim());
                                updateValues.put(TooshaContract.TasbihEntry.COLUMN_TASBIH_PASHTO, mPashto.getText().toString().trim());
                                updateValues.put(TooshaContract.TasbihEntry.COLUMN_TASBIH_FARSI, mPersian.getText().toString().trim());
                                updateValues.put(TooshaContract.TasbihEntry.COLUMN_TASBIH_ENGLISH, mEnglish.getText().toString().trim());
                                updateValues.put(TooshaContract.TasbihEntry.COLUMN_TASBIH_FAZILAT, mfazilat.getText().toString().trim());
                                updateValues.put(TooshaContract.TasbihEntry.COLUMN_TASBIH_COUNT, Integer.valueOf(mAmount.getText().toString().trim()));
                                updateValues.put(TooshaContract.TasbihEntry.COLUMN_TASBIH_TOTAL_COUNT, Integer.valueOf(total_cont));

                                String selection = TooshaContract.TasbihEntry._ID + "=?";
                                Log.d(TAG, "onCreateLoader: " + id);
                                String[] selectionArgs = {String.valueOf(id)};

                                int updated_rows = mContext.getContentResolver().update(TooshaContract.TasbihEntry.CONTENT_URI, updateValues, selection, selectionArgs);
                                //   getSupportLoaderManager().restartLoader(121,null,AzkarActivity.this);
                                Log.d(TAG, "onClick: " + updated_rows + " row updated  ");

                                dialog.dismiss();
                            } else {
                                Toast.makeText(mContext,
                                        mContext.getString(R.string.required_fields),
                                        Toast.LENGTH_SHORT).show();
                            }
                        }
                    });
                }
            });
        }


        public void clickTasbihItem(final int currentID, final int category,
                                    final int theCount, final int total_count) {
            mLinearLayout.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    if (category == TASBIH_CATEGORY || category == TooshaContract.CUSTOM_CATEGORY) {

                        Intent intent = new Intent(mContext, TasbihWindow.class);
                        intent.putExtra("currentID", currentID);
                        mContext.startActivity(intent);

                    } else {
                        Log.d(TAG, "onClick: " + theCount);
                        int stateValue = TooshaContract.STATE_FALSE;
                        if (theCount <= 0) {
                            vibrate(vibrator, 200);
                        } else {
                            if (theCount == 1) {
                                vibrate(vibrator, 200);
                            } else {
                                vibrate(vibrator, 30);

                            }
                            //    countText.setText(String.format("%d", itemCount - 1));
                            // Create a ContentValues object where column names are the keys,
                            // and Toto's pet attributes are the values.
                            ContentValues values = new ContentValues();
                            values.put(TooshaContract.TasbihEntry.COLUMN_TASBIH_COUNT, theCount - 1);
                            values.put(TooshaContract.TasbihEntry.COLUMN_TASBIH_TOTAL_COUNT, total_count + 1);

                            // Insert a new row for Toto in the database, returning the ID of that new row.
                            // The first argument for db.insertAmal() is the pets table name.
                            // The second argument provides the name of a column in which the framework
                            // can insertAmal NULL in the event that the ContentValues is empty (if
                            // this is set to "null", then the framework will not insertAmal a row when
                            // there are no values).
                            // The third argument is the ContentValues object containing the info for Toto.

                            String selection = TooshaContract.TasbihEntry._ID + "=?";

                            String[] selectionArgs = {String.valueOf(currentID)};
                            long newRowId = mContext.getContentResolver().update(TooshaContract.TasbihEntry.CONTENT_URI, values, selection, selectionArgs);

                        }
                    }

                }
            });
        }


        public void delete_icon_OnClick(final int id) {
            delete_icon.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {

                    AlertDialog.Builder mBuilder = new AlertDialog.Builder(mContext);
                    View mView = inflate(mContext, R.layout.dialog_delete_tasbih, null);
                    Button mDelete = (Button) mView.findViewById(R.id.delete_dialog_Btn);
                    Button mCancel= (Button) mView.findViewById(R.id.delete_dialog_cancel);
                    mBuilder.setView(mView);
                    final AlertDialog dialog = mBuilder.create();
                    Window window = dialog.getWindow();
                    window.setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
                    window.setGravity(Gravity.BOTTOM);
                    window.getAttributes().windowAnimations = R.style.DialogAnimation;
                    dialog.show();
                    mDelete.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View view) {
                            String selection = TooshaContract.TasbihEntry._ID + "=?";
                            String[] selectionArgs = {String.valueOf(id)};
                            long newRowId = mContext.getContentResolver().delete(TooshaContract.TasbihEntry.CONTENT_URI, selection, selectionArgs);

                            //Now update Database

                            Toast.makeText(mContext, "Tasbih Deleted", Toast.LENGTH_SHORT).show();

                            //After updating Database, Restart App

                            dialog.dismiss();
                        }
                    });
                    mCancel.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View view) {
                            dialog.dismiss();
                        }
                    });

                }

            });

        }


    }

}