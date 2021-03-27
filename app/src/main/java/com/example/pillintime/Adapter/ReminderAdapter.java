package com.example.pillintime.Adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.amulyakhare.textdrawable.TextDrawable;
import com.amulyakhare.textdrawable.util.ColorGenerator;
import com.example.pillintime.Models.Reminder;
import com.example.pillintime.R;

import org.w3c.dom.Text;

import java.util.List;

public class ReminderAdapter extends RecyclerView.Adapter<ReminderAdapter.ViewHolder> {

    Context context;
    List<Reminder> reminderList;


    public ReminderAdapter(Context context, List<Reminder> reminderList) {
        this.context = context;
        this.reminderList = reminderList;
    }


    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
//        return new ViewHolder(LayoutInflater.from(parent.getContext()).
//                inflate(R.layout.listing_row, parent, false));

        return new ViewHolder(LayoutInflater.from(parent.getContext()).
                inflate(R.layout.listing_row2, parent, false));
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        holder.reminderText.setText(reminderList.get(position).getName());
        holder.timeAndDateReminderText.setText(reminderList.get(position).
                getStartReminderDay().getDateStr() + " " +
                reminderList.get(position).getAlarmTimeList().get(0).getAlarmTimeStr());

        TextDrawable drawable;
        drawable = holder.getDrawable();

        holder.thumbnailImage.setImageDrawable(drawable);
    }

    @Override
    public int getItemCount() {
        return reminderList.size();
    }

    class ViewHolder extends RecyclerView.ViewHolder{

        private TextView reminderText, timeAndDateReminderText;
        //private LinearLayout listingRowLayout;
        private RelativeLayout listingRowLayout;

        private ImageView thumbnailImage;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            reminderText = (TextView) itemView.findViewById(R.id.recycle_title);
            timeAndDateReminderText = (TextView) itemView.findViewById(R.id.recycle_date_time);
            listingRowLayout = (RelativeLayout) itemView.findViewById(R.id.listing_row_layout2);
            thumbnailImage = (ImageView) itemView.findViewById(R.id.thumbnail_image);

            String letter = "";

            if (reminderText != null && !reminderText.getText().toString().isEmpty()){
                letter = reminderText.getText().toString().substring(0,1);
            }

            ColorGenerator colorGenerator = ColorGenerator.DEFAULT;
            TextDrawable drawable;
            int color = colorGenerator.getRandomColor();

            drawable = TextDrawable.builder().buildRound(letter, color);
            thumbnailImage.setImageDrawable(drawable);

        }
        public TextDrawable getDrawable(){
            String letter = "";

            if (reminderText != null && !reminderText.getText().toString().isEmpty()){
                letter = reminderText.getText().toString().substring(0,1);
            }

            ColorGenerator colorGenerator = ColorGenerator.DEFAULT;
            TextDrawable drawable;
            int color = colorGenerator.getRandomColor();

            drawable = TextDrawable.builder().buildRound(letter, color);

            return drawable;
        }
    }

}
