package com.example.pillintime.Adapter;

import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.amulyakhare.textdrawable.TextDrawable;
import com.amulyakhare.textdrawable.util.ColorGenerator;
import com.example.pillintime.Models.Reminder;
import com.example.pillintime.R;
import com.example.pillintime.ReminderAttrs.ReminderDate;
import com.example.pillintime.UpdateReminderActivity;
import com.squareup.picasso.Picasso;

import org.w3c.dom.Text;

import java.util.List;

public class ReminderAdapter extends RecyclerView.Adapter<ReminderAdapter.ViewHolder> {

    Context context;
    List<Reminder> reminderList;
    private RecyclerViewClickListener listener;
    private ReminderDate chosenDate;


    public ReminderAdapter(Context context, List<Reminder> reminderList, RecyclerViewClickListener listener, ReminderDate chosenDate) {
        this.context = context;
        this.reminderList = reminderList;
        this.listener = listener;
        this.chosenDate = chosenDate;
    }


    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        return new ViewHolder(LayoutInflater.from(parent.getContext()).
                inflate(R.layout.listing_row2, parent, false));
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        holder.reminderText.setText(reminderList.get(position).getName());
        holder.timeAndDateReminderText.setText(reminderList.get(position).
                getStartReminderDay().getDateStr() + " " +
                reminderList.get(position).getAlarmTimeList().get(0).getAlarmTimeStr());

        for (ReminderDate remDate : reminderList.get(position).getReminderDateList()) {
            if(remDate.getYear() == chosenDate.getYear() && remDate.getMonth() == chosenDate.getMonth()
            && remDate.getDay() == chosenDate.getDay() && remDate.getIsTaken() == true){
                holder.listingRowLayout.setBackgroundColor(context.getResources().getColor(R.color.ripple_effect));
            }

        }

        if(reminderList.get(position).getImg() != null
                && reminderList.get(position).getImg().trim().length() != 0) {
            Picasso.get()
                    .load(reminderList.get(position).getImg())
                    .fit()
                    .centerCrop()
                    .into(holder.thumbnailImage);
        }
    }

    @Override
    public int getItemCount() {
        return reminderList.size();
    }

    class ViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener{

        private TextView reminderText, timeAndDateReminderText;
        private RelativeLayout listingRowLayout;
        private Button btn_take, btn_skip;

        private ImageView thumbnailImage;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            reminderText = (TextView) itemView.findViewById(R.id.recycle_title);
            timeAndDateReminderText = (TextView) itemView.findViewById(R.id.recycle_date_time);
            listingRowLayout = (RelativeLayout) itemView.findViewById(R.id.listing_row_layout2);
            thumbnailImage = (ImageView) itemView.findViewById(R.id.thumbnail_image);
            btn_skip = itemView.findViewById(R.id.skip_btn);
            btn_take = itemView.findViewById(R.id.take_btn);

            String letter = "";

            if (reminderText != null && !reminderText.getText().toString().isEmpty()){
                letter = reminderText.getText().toString().substring(0,1);
            }


            itemView.setOnClickListener(this);
            btn_skip.setOnClickListener(this);
            btn_take.setOnClickListener(this);
        }

        @Override
        public void onClick(View v) {
            if(v == itemView) {
                listener.onClick(v, getAdapterPosition());
            } else if(v == btn_take){
                listener.onTakeBtnClick(v, getAdapterPosition(), listingRowLayout);
            } else if(v == btn_skip){
                listener.onSkipBtnClick(v, getAdapterPosition(), listingRowLayout);
            }
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

    public interface RecyclerViewClickListener{
        void onClick(View v, int position);
        void onSkipBtnClick(View v, int position, RelativeLayout listingRowLayout);
        void onTakeBtnClick(View v, int position, RelativeLayout listingRowLayout);
    }

}
