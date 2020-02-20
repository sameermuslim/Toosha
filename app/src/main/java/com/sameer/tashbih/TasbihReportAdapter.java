package com.sameer.tashbih;

import android.animation.ValueAnimator;
import android.content.Context;
import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

import com.sameer.tashbih.data.TooshaContract;
import com.sameer.tashbih.data.TooshaDbHelper;

import java.util.List;

import static android.content.ContentValues.TAG;

public class TasbihReportAdapter extends RecyclerView.Adapter<TasbihReportAdapter.ViewHolder> {

    private List<Tasbih> tasbihs;

    private Context context;
    private int total=0;

    public TasbihReportAdapter(List<Tasbih> tasbihs) {

        this.tasbihs = tasbihs;

    }

    @NonNull
    @Override
    public TasbihReportAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.tasbih_report_item, parent, false);
        context = parent.getContext();
        return new TasbihReportAdapter.ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull final TasbihReportAdapter.ViewHolder holder, int position) {


        int amount = tasbihs.get(position).getTotal_count();
        holder.setAmountTV(amount);

     //   int period =tasbihs.get(position).getTotal_count() / tasbihs.get(position).getCount();
        int category =tasbihs.get(position).getCategory();
        switch (category){
            case TooshaContract.DAY_NIGHT_AZkAR:
                holder.setTitleTV(context.getString(R.string.day_night_azkar));
                break;
            case TooshaContract.TASBIH_CATEGORY:
                holder.setTitleTV(context.getString(R.string.tasbihes));
                break;
            case TooshaContract.MORNING_AZKAR:
                holder.setTitleTV(context.getString(R.string.morning_azkar));
                break;
            case TooshaContract.EVENING_AZKAR:
                holder.setTitleTV(context.getString(R.string.evening_azkar));
                break;
            case TooshaContract.MOSQUE_AZKAR:
                holder.setTitleTV(context.getString(R.string.mosque_azkar));
                break;
            case TooshaContract.PRAYER_AZKAR:
                holder.setTitleTV(context.getString(R.string.prayer_azkar));
                break;
            case TooshaContract.SLEEP_AZKAR:
                holder.setTitleTV(context.getString(R.string.sleep_azkar));
            case TooshaContract.CUSTOM_CATEGORY:
                holder.setTitleTV(context.getString(R.string.custom_Tasbih));

                break;
                default:
                    holder.setTitleTV(String.valueOf(category));
        }

        int count=tasbihs.get(position).getCount();
            if (count !=0) {
                int period = (amount / count);
                holder.setPeriodTV(period);
            }else {
                holder.setPeriodTV(tasbihs.get(position).getCount());
            }
        total=total+amount;
        Toast.makeText(context, "toale = "+total, Toast.LENGTH_LONG).show();
        Log.d(TAG, "onBindViewHolder: "+tasbihs.toString());

    }

    @Override
    public int getItemCount() {

        return tasbihs.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {

        TooshaDbHelper mDbHelper;
        private View mView;
        private TextView amountTV, periodTV,titleTV;



        public ViewHolder(View itemView) {
            super(itemView);
            mView = itemView;
        }


        public void setTitleTV(String title){
            titleTV=mView.findViewById(R.id.tasbih_title_item);
            titleTV.setText(title);
        }

        public void setPeriodTV(int period) {
            periodTV = mView.findViewById(R.id.period_tv);
            startCountAnimation(this.periodTV,period);
        }

        public void setAmountTV(int amountTV) {
            this.amountTV = mView.findViewById(R.id.amount_tv);
            startCountAnimation(this.amountTV,amountTV);
        }
    }

    private void startCountAnimation(final TextView textView, int count) {
        ValueAnimator animator = ValueAnimator.ofInt(0, count); //0 is min number, 600 is max number
        animator.setDuration(2000); //Duration is in milliseconds
        animator.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() {
            public void onAnimationUpdate(ValueAnimator animation) {
                String myIntAsString = String.format("%d ",animation.getAnimatedValue() );
                textView.setText(myIntAsString);

            }
        });
        animator.start();
    }
}