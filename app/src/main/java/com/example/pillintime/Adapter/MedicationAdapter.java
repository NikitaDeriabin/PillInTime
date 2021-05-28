package com.example.pillintime.Adapter;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
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
import com.example.pillintime.UpdateReminderActivity;
import com.squareup.picasso.Picasso;

import org.w3c.dom.Text;

import java.util.List;

public class MedicationAdapter extends RecyclerView.Adapter<MedicationAdapter.ViewHolder> {

    Context context;
    List<Reminder> reminderList;
    private RecyclerViewClickListener listener;


    public MedicationAdapter(Context context, List<Reminder> reminderList, RecyclerViewClickListener listener) {
        this.context = context;
        this.reminderList = reminderList;
        this.listener = listener;
    }


    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        return new ViewHolder(LayoutInflater.from(parent.getContext()).
                inflate(R.layout.list_row_medication, parent, false));
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        holder.reminderText.setText(reminderList.get(position).getName());

        TextDrawable drawable;
        drawable = holder.getDrawable();

        //holder.thumbnailImage.setImageDrawable(drawable);

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
        //private LinearLayout listingRowLayout;
        private RelativeLayout listingRowLayout;

        private ImageView thumbnailImage;
        private ImageView deleteIcon;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            reminderText = (TextView) itemView.findViewById(R.id.recycle_title);
            listingRowLayout = (RelativeLayout) itemView.findViewById(R.id.listing_row_layout2);
            thumbnailImage = (ImageView) itemView.findViewById(R.id.thumbnail_image);
            deleteIcon = (ImageView) itemView.findViewById(R.id.delete_reminder_icon);

            String letter = "";

            if (reminderText != null && !reminderText.getText().toString().isEmpty()){
                letter = reminderText.getText().toString().substring(0,1);
            }

            itemView.setOnClickListener(this);
            deleteIcon.setOnClickListener(this);
            thumbnailImage.setOnClickListener(this);
        }

        @Override
        public void onClick(View v) {
            if(v == itemView){
                listener.onClickReminder(v, getAdapterPosition());
            } else if(v == deleteIcon){
                listener.onClickDeleteIcon(v, getAdapterPosition());
            } else if(v == thumbnailImage){
                listener.onClickThumbnailIcon(v, getAdapterPosition());
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
        void onClickReminder(View v, int position);
        void onClickDeleteIcon(View v, int position);
        void onClickThumbnailIcon(View v, int position);
    }

}
